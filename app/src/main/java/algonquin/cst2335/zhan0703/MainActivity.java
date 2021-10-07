package algonquin.cst2335.zhan0703;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w( TAG, "In onCreate() - Loading Widgets" );
        Button loginButton = findViewById(R.id.button);
        EditText et = findViewById(R.id.editTextTextEmailAddress);
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String emailAddress = prefs.getString("LoginName", "");
        et.setText(emailAddress);
        loginButton.setOnClickListener( clk-> {
            Intent nextPage = new Intent( MainActivity.this, SecondActivity.class);
            String email = et.getText().toString();
            nextPage.putExtra("EmailAddress",email);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("LoginName", email);
//            editor.putInt("IntNum",6);
//            editor.putFloat("FloatNum",6.6f);
            editor.apply();
            startActivity(  nextPage );
        } );

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w( TAG, "In onStart() - application is now visible on screen" );
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w( TAG, "The application no longer responds to user input" );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w( TAG, "In onResume() -  The application is now responding to user input" );
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w( TAG, "In onStop() - The application is no longer visible." );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w( TAG, "In onDestroy() - Any memory used by the application is freed." );
    }


}