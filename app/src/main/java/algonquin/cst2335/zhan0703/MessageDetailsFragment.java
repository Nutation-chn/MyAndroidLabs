package algonquin.cst2335.zhan0703;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.fragment.app.Fragment;

public class MessageDetailsFragment extends Fragment{

    MessageListFragment.ChatMessage chosenMessage;
    int chosenPosition;

    public MessageDetailsFragment(MessageListFragment.ChatMessage clicked, int pos){
        chosenMessage = clicked;
        chosenPosition = pos;
    }

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View detailsView = inflater.inflate(R.layout.details_layout,container,false);

        TextView messageView = detailsView.findViewById(R.id.messageView);
        TextView sendView = detailsView.findViewById(R.id.sendView);
        TextView timeView = detailsView.findViewById(R.id.timeView);
        TextView databaseIdView = detailsView.findViewById(R.id.databaseIdView);

        messageView.setText("Message is:"+ chosenMessage.getMessage());
        sendView.setText("Send or Receive? "+ chosenMessage.getSendOrReceive());
        timeView.setText("Time send: "+ chosenMessage.getTimeSent());
        databaseIdView.setText("Database id is:"+ chosenMessage.getId());

//        click close button will close current fragment.
        Button closeButton = detailsView.findViewById(R.id.closeButton);
        closeButton.setOnClickListener( closeClicked ->{
            getParentFragmentManager().beginTransaction().remove(this).commit();
        });

//        click close button will close current fragment.
        Button deleteButton = detailsView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener( deleteClicked ->{
            ChatRoom parentActivity = (ChatRoom)getContext();
            parentActivity.notifyMessageDeleted(chosenMessage,chosenPosition);
            getParentFragmentManager().beginTransaction().remove(this).commit();
        });

        return detailsView;
    }
}
