package algonquin.cst2335.zhan0703;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ImageView profileImage = findViewById(R.id.imageView);
        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");
        TextView topText = findViewById(R.id.textView3);

        topText.setText("Welcome back "+emailAddress);

        Button callButton = findViewById(R.id.button2);
        callButton.setOnClickListener( view -> {
            Intent call = new Intent(Intent.ACTION_DIAL);
            EditText editPhone = findViewById(R.id.editTextPhone);
            call.setData(Uri.parse("tel:" + editPhone.getText().toString()));
            startActivity(call);
        });

        EditText editPhone = findViewById(R.id.editTextPhone);
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String phoneNumber = prefs.getString("Phone","");
        editPhone.setText(phoneNumber);

        Button cameraButton = findViewById(R.id.button3);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Bitmap thumbnail = data.getParcelableExtra("data");
                            profileImage.setImageBitmap(thumbnail);
                            FileOutputStream fOut = null;
                            try { fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                fOut.flush();
                                fOut.close();
                            }
                            catch (FileNotFoundException e)
                            { e.printStackTrace();
                            }
                            catch (IOException e)
                            { e.printStackTrace();
                            }
                        }
                    }
                });
        File file = new File( getFilesDir(), "Picture.png");
        if(file.exists())
        {
            Bitmap theImage = BitmapFactory.decodeFile(file.toString());
            profileImage.setImageBitmap( theImage );
        }
        cameraButton.setOnClickListener( view -> {
            cameraResult.launch(cameraIntent);
        });

    }
    @Override
    protected void onPause() {
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        EditText editPhone = findViewById(R.id.editTextPhone);
        SharedPreferences.Editor editor = prefs.edit();
        String phoneNumber = editPhone.getText().toString();
        editor.putString("Phone",phoneNumber);
        super.onPause();
        Log.w( TAG, "The application no longer responds to user input" );
        editor.apply();
    }
}
