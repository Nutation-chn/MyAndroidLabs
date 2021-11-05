package algonquin.cst2335.zhan0703;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Dong Zhang
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
     /** This holds the text at the center of the screen*/
    TextView tv = null;
    /** This holds the editText for user to input password*/
    EditText et = null;
    /** This is the button to login */
    Button btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        TextView tv = findViewById(R.id.textView);
        EditText et = findViewById(R.id.editTextPassword);
        Button btn = findViewById(R.id.buttonLogin);

        btn.setOnClickListener( clk->{
            String password = et.getText().toString();
            if(checkPasswordComplexity(password))
                tv.setText("Your password is complex enough.");
            else
                tv.setText("You shall not pass!");

        });
    }

    /**this function check the complexity of a given string, complexity is defined as containing
     * at least an Upper Case letter, a lower case letter, a number, and a special symbol
     *
     * @param pw The String that need to check complexity.
     * @return Returns true if and only if the input string has enough complexity
     */
    Boolean checkPasswordComplexity(@NonNull String pw){
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;
        Context context = getApplicationContext();

//        start looping through string
        for(int i=0; i< pw.length(); i++){
            char c = pw.charAt(i);
            Log.i("looking at char:","   "+c);
            if (Character.isUpperCase(c)) foundUpperCase = true;
            if (Character.isLowerCase(c)) foundLowerCase = true;
            if (Character.isDigit(c)) foundNumber = true;
            if (isSpecialCharacter(c)) foundSpecial = true;
        }
        if(!foundUpperCase)
        {
            Toast.makeText(context,"Password should contain Uppercase letter.", Toast.LENGTH_SHORT).show();
//            et.setError("Password should contain Uppercase letter."); //et is null, why??
            return false;
        }

        else if( ! foundLowerCase)
        {
            Toast.makeText(context,"Password should contain lowercase letter.", Toast.LENGTH_SHORT).show();
//            et.setError("Password should contain lowercase letter.");
            return false;
        }

        else if( ! foundNumber)
        {
            Toast.makeText(context,"Password should contain number.", Toast.LENGTH_SHORT).show();
//            et.setError("Password should contain number.");
            return false;
        }
        else if(! foundSpecial)
        {
            Toast.makeText(context,"Password should contain special character i.e. #$%^&*!@?", Toast.LENGTH_SHORT).show();
//            et.setError("Password should contain special character i.e. #$%^&*!@?");
            return false;
        }
        else
           return true;
    }

    /**this function check if a given character is a special character
     *
     * @param c the character to be checked if it is a special character: #$%^&*!@?.
     * @return return true if c is a special character, return false if it is not.
     */
    boolean isSpecialCharacter(char c){
        switch (c){
            case '#':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*':
            case '!':
            case '@':
            case '?':
                return true;
            default:
                return false;
        }
    }

}