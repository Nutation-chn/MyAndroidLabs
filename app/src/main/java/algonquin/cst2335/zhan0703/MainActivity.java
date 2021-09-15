package algonquin.cst2335.zhan0703;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView mytext = findViewById(R.id.textview);
        Button btn = findViewById(R.id.mybutton);
        EditText myedit = findViewById(R.id.myedittext);
        btn.setOnClickListener( vw  -> {  mytext.setText("Your edit text has: " + myedit.getText().toString());  }   );
//        btn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                String editString = myedit.getText().toString();
//                mytext.setText( "Your edit text has: " + editString);
//            }
//        });
        Context context = getApplicationContext();


        CheckBox mycheck1 = findViewById(R.id.mycheckbox1);
        mycheck1.setOnClickListener( vw -> { Toast.makeText(context,("You clicked on the " + (mycheck1.getText()) + " and it is now: " + mycheck1.isChecked()), Toast.LENGTH_SHORT).show(); } );

        CheckBox mycheck2 = findViewById(R.id.mycheckbox2);
        mycheck2.setOnClickListener( vw -> { Toast.makeText(context,("You clicked on the " + (mycheck2.getText()) + " and it is now: " + mycheck2.isChecked()), Toast.LENGTH_SHORT).show(); } );

        Switch myswitch1 = findViewById(R.id.myswitch1);
        myswitch1.setOnClickListener( vw -> { Toast.makeText(context,("You clicked on the " + (myswitch1.getText()) + " and it is now: " + myswitch1.isChecked()), Toast.LENGTH_SHORT).show(); } );

        Switch myswitch2 = findViewById(R.id.myswitch2);
        myswitch2.setOnClickListener( vw -> { Toast.makeText(context,("You clicked on the " + (myswitch2.getText()) + " and it is now: " + myswitch2.isChecked()), Toast.LENGTH_SHORT).show(); } );



        RadioButton myradio1 = findViewById(R.id.myRadio1);
        myradio1.setOnClickListener( vw -> { Toast.makeText(context,("You clicked on the " + (myradio1.getText()) + " and now: " + myradio1.getText() + " is selected"), Toast.LENGTH_SHORT).show(); } );

        RadioButton myradio2 = findViewById(R.id.myRadio2);
        myradio2.setOnClickListener( vw -> { Toast.makeText(context,("You clicked on the " + (myradio2.getText()) + " and now: " + myradio2.getText() + " is selected"), Toast.LENGTH_SHORT).show(); } );


        ImageView myimage = findViewById(R.id.logo_algonquin);

        ImageButton imgbtn = findViewById( R.id.myimagebutton );
        imgbtn.setOnClickListener( vw  -> {  Toast.makeText(context, ("The width= " + imgbtn.getWidth() + " and height = " + imgbtn.getHeight()), Toast.LENGTH_SHORT).show(); }   );

    }
}