package algonquin.cst2335.zhan0703;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView mytext = findViewById(R.id.textview);
        Button btn = findViewById(R.id.mybutton);
        EditText myedit = findViewById(R.id.myedittext);
//        btn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                String editString = myedit.getText().toString();
//                mytext.setText( "Your edit text has: " + editString);
//            }
//        });
        btn.setOnClickListener( vw  -> {  mytext.setText("Your edit text has: " + myedit.getText().toString());  }   );

        ImageView myimage = findViewById(R.id.logo_algonquin);

        ImageButton imgbtn = findViewById( R.id.myimagebutton );
        imgbtn.setOnClickListener( vw  -> {  mytext.setText("The width= " + imgbtn.getWidth() + " and height = " + imgbtn.getHeight()); }   );

    }
}