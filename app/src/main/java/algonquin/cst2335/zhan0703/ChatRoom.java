package algonquin.cst2335.zhan0703;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatRoom extends AppCompatActivity {
    RecyclerView chatList;
    EditText edit;
    MyChatAdapter adt;
    Button buttonSend;
    Button buttonReceive;
    ArrayList<ChatMessage> messages = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.chatlayout );
        chatList = findViewById(R.id.myrecycler);
        edit = findViewById(R.id.editTextChat);
        buttonSend = findViewById(R.id.buttonSend);
        buttonReceive = findViewById(R.id.buttonReceive);

        chatList.setLayoutManager(new LinearLayoutManager(this));
        //things to do when click send button
        buttonSend.setOnClickListener( v -> {
            ChatMessage thisMessage = new ChatMessage( edit.getText().toString(), 1,  new Date() );
            messages.add(thisMessage);
            adt.notifyItemInserted(messages.size()-1);
            edit.setText("");
        });
        //things to do when click receive button
        buttonReceive.setOnClickListener(v -> {
            ChatMessage thisMessage = new ChatMessage( edit.getText().toString(), 2,  new Date() );
            messages.add(thisMessage);
            adt.notifyItemInserted(messages.size()-1);
            edit.setText("");
        });
        adt = new MyChatAdapter();
        chatList.setAdapter(adt);
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
    private class MyRowViews extends RecyclerView.ViewHolder{
        TextView messageText;
        TextView timeText;
        public MyRowViews(View itemView){
            super(itemView);
            itemView.setOnClickListener(click->{
                int position = getAbsoluteAdapterPosition();
                //builder for alert message to delete
                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
                builder.setMessage("Do you want to delete this message:" + messageText.getText())
                .setTitle("Question:")
//              nothing happens with "no"
                .setNegativeButton("No",(dialog, cl)->{

                })
//                delete with "yes"
                .setPositiveButton("Yes",(dialog, cl)->{
                    ChatMessage removedMessage = messages.get(position);   //buffer of deleted message, for restoring purpose.
                    messages.remove(position);      //remove item in array
                    adt.notifyItemRemoved(position);    //notify to update UI
                    //Snackbar popup,with Undo option
                    Snackbar.make(messageText, "You deleted message $"+position, Snackbar.LENGTH_LONG)
                            .setAction("Undo",clk ->{
                                messages.add(position,removedMessage);
                                adt.notifyItemInserted(position);
                            })
                            .show(); //show snackbar
                })
                .create().show(); //show AlertDialog
            });
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }

    //Chat message class
    private class ChatMessage
    {
        String message;
        int sendOrReceive;
        String timeSent;

        public ChatMessage(String message, int sendOrReceive, Date timeSent) {
            this.message = message;
            this.sendOrReceive = sendOrReceive;
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE,dd-MMM-yyyy hh-mm-ss a", Locale.getDefault()); //formatting the date-time
            this.timeSent = sdf.format(timeSent); //convert Date to String
        }

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
