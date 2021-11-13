package algonquin.cst2335.zhan0703;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MessageListFragment extends Fragment {
    RecyclerView chatList;
    EditText edit;
    MyChatAdapter adt;
    Button buttonSend;
    Button buttonReceive;
    MyOpenHelper opener;
    SQLiteDatabase db;
    ArrayList<ChatMessage> messages = new ArrayList<>();

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View chatLayout = inflater.inflate(R.layout.chatlayout,container,false);
        chatList = chatLayout.findViewById(R.id.myrecycler);
        edit = chatLayout.findViewById(R.id.editTextChat);
        buttonSend = chatLayout.findViewById(R.id.buttonSend);
        buttonReceive = chatLayout.findViewById(R.id.buttonReceive);
        //contest should be set who load this fragment
        opener = new MyOpenHelper(getContext());
        db = opener.getWritableDatabase();

        //select messages with certain condition.
//        Cursor results = db.rawQuery("Select * from " + MyOpenHelper.TABLE_NAME + " where  _id > ? and TimeSent like ?;",
//                new String[]{"3", "%10-Nov%"});

        Cursor results = db.rawQuery("Select * from " + MyOpenHelper.TABLE_NAME + ";", null);

//        loop through the table to read all messages
        while(results.moveToNext()){
            //get the column number
            int _idCol = results.getColumnIndex( "_id");
            int messageCol = results.getColumnIndex(MyOpenHelper.col_message);
            int sendCol = results.getColumnIndex(MyOpenHelper.col_send_receive);
            int timeCol = results.getColumnIndex(MyOpenHelper.col_time_sent);

            //get data using column number and cursor
            long id = results.getInt(_idCol);
            String message = results.getString( messageCol );
            String time = results.getString( timeCol );
            int sendOrReceive = results.getInt( sendCol);
            messages.add(new ChatMessage(message, sendOrReceive, time, id));
        }


        chatList.setLayoutManager(new LinearLayoutManager(getContext()));
        //things to do when click send button
        buttonSend.setOnClickListener( v -> {
            ChatMessage thisMessage = new ChatMessage( edit.getText().toString(), 1,  new Date() );

            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.col_message,thisMessage.getMessage());
            newRow.put(MyOpenHelper.col_send_receive,thisMessage.getSendOrReceive());
            newRow.put(MyOpenHelper.col_time_sent,thisMessage.getTimeSent());
            long newId =db.insert(MyOpenHelper.TABLE_NAME,MyOpenHelper.col_message,newRow);
            thisMessage.setId(newId);

            messages.add(thisMessage);
            adt.notifyItemInserted(messages.size()-1);
            edit.setText("");
        });
        //things to do when click receive button
        buttonReceive.setOnClickListener(v -> {
            ChatMessage thisMessage = new ChatMessage( edit.getText().toString(), 2,  new Date() );

            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.col_message,thisMessage.getMessage());
            newRow.put(MyOpenHelper.col_send_receive,thisMessage.getSendOrReceive());
            newRow.put(MyOpenHelper.col_time_sent,thisMessage.getTimeSent());
            long newId =db.insert(MyOpenHelper.TABLE_NAME,MyOpenHelper.col_message,newRow);
            thisMessage.setId(newId);

            messages.add(thisMessage);
            adt.notifyItemInserted(messages.size()-1);
            edit.setText("");
        });
        adt = new MyChatAdapter();
        chatList.setAdapter(adt);
        return chatLayout;
    }

    //delete chosen message
    public void notifyMessageDeleted(MessageListFragment.ChatMessage chosenMessage, int chosenPosition) {

        //builder for alert message to delete
        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
        builder.setMessage("Do you want to delete this message:" + chosenMessage.getMessage())
                .setTitle("Question:")
//              nothing happens with "no"
                .setNegativeButton("No",(dialog, cl)->{})
//                delete with "yes"
                .setPositiveButton("Yes",(dialog, cl)->{
                    MessageListFragment.ChatMessage removedMessage = messages.get(chosenPosition);   //buffer of deleted message, for restoring purpose.
                    messages.remove(chosenPosition);      //remove item in array
                    adt.notifyItemRemoved(chosenPosition);    //notify to update UI
                    //delete the message in database
                    db.delete(MyOpenHelper.TABLE_NAME,"_id=?", new String[] { Long.toString(removedMessage.getId()) });
                    //Snackbar popup,with Undo option

                    Snackbar.make(buttonSend, "You deleted message $"+chosenPosition, Snackbar.LENGTH_LONG)
                            .setAction("Undo",clk ->{
                                messages.add(chosenPosition,removedMessage);
                                adt.notifyItemInserted(chosenPosition);

                                //recover delete message to database
                                db.execSQL(String.format("Insert into \"%s\" values ( %d, \"%s\", %d, \"%s\" );"
                                        , MyOpenHelper.TABLE_NAME, removedMessage.getId(), removedMessage.getMessage()
                                        , removedMessage.getSendOrReceive(), removedMessage.getTimeSent()));

                            })
                            .show(); //show snackbar
                })
                .create().show(); //show AlertDialog
    }


    //    construct adapter
    private class MyChatAdapter extends RecyclerView.Adapter<MyRowViews>{
        //get viewType send or receive
        @Override
        public int getItemViewType(int position) {
            return messages.get(position).getSendOrReceive();
        }
        //inflate to send or receive based on viewType value
        @Override
        public MyRowViews  onCreateViewHolder(ViewGroup parent, int viewType) {
            int layoutID;
            if (viewType==1)
                layoutID=R.layout.sent_message;
            else
                layoutID=R.layout.receive_message;
            return new MyRowViews(getLayoutInflater().inflate(layoutID,parent,false));
        }
        //bind message and time to view
        @Override
        public void onBindViewHolder(MyRowViews  holder, int position) {
            holder.messageText.setText(messages.get(position).getMessage());
            holder.timeText.setText(messages.get(position).getTimeSent());
        }
        //the size of messages array
        @Override
        public int getItemCount() {
            return messages.size();
        }
    }

    // ViewHolder class
    private class MyRowViews extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MyRowViews(View itemView) {
            super(itemView);


            itemView.setOnClickListener(click -> {
                ChatRoom parentActivity = (ChatRoom) getContext();
                int position = getAbsoluteAdapterPosition();
                parentActivity.userClickedMessage(messages.get(position), position);
            });


            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);

        }

    }
    /**
     *
     */
    //Chat message class
    public class ChatMessage
    {
        String message;
        int sendOrReceive;
        String timeSent;
        long id;
        public ChatMessage(String message, int sendOrReceive, Date timeSent) {
            this.message = message;
            this.sendOrReceive = sendOrReceive;
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE,dd-MMM-yyyy hh-mm-ss a", Locale.getDefault()); //formatting the date-time
            this.timeSent = sdf.format(timeSent); //convert Date to String
        }

        public ChatMessage(String message, int sendOrReceive, String timeSent, long id) {
            this.message = message;
            this.sendOrReceive = sendOrReceive;
            this.timeSent = timeSent;
            this.id = id;
        }

        public void setId (long l){id =l;}
        public long getId() {return id;}
        public String getMessage() {
            return message;
        }

        public int getSendOrReceive() {
            return sendOrReceive;
        }

        public String getTimeSent() {
            return timeSent;
        }
    }

}
