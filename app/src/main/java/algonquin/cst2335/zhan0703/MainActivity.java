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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Dong Zhang
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /** This holds the editText for user to input password*/
    EditText cityText = null;
    /** This is the button to login */
    Button forecastBtn = null;

    String stringURL = "The server URL";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        EditText cityText = findViewById(R.id.cityTextField);
        Button forecastBtn = findViewById(R.id.forecastButton);

        forecastBtn.setOnClickListener((click) -> {
            Executor newThread = Executors.newSingleThreadExecutor();
            newThread.execute(() -> {
                //this runs another thread
                try {
                    String cityName = cityText.getText().toString();
                    stringURL = "https://api.openweathermap.org/data/2.5/weather?q="
                            + URLEncoder.encode(cityName, "UTF-8") +
                            "&appid=7e943c97096a9784391a981c4d878b22";
                    URL url = new URL(stringURL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

//                    String text = (new BufferedReader(
//                            new InputStreamReader(in, StandardCharsets.UTF_8)))
//                            .lines()
//                            .collect(Collectors.joining("\n"));
                } catch (IOException ioe) {
                    Log.e("Connection error:", ioe.getMessage());
                }
            });
        });
    }
}