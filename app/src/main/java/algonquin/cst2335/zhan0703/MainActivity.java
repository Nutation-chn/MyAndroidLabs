package algonquin.cst2335.zhan0703;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author Dong Zhang
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /** This holds the editText for user to input password*/
    EditText cityText;
    /** This is the button to login */
    Button forecastBtn ;
    /** This holds the editText for user to input temperature*/
    TextView tempText ;
    /** This holds the editText for max temperature */
    TextView tempMaxText;
    /** This holds the editText for min temperature */
    TextView tempMinText;
    /** This holds the editText for humidity */
    TextView humidityText;
    /** This holds the editText for description */
    TextView descriptionText;
    ImageView iv;

    float oldSize =14;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    Toolbar myToolbar;

    String stringURL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=7e943c97096a9784391a981c4d878b22&units=metric&mode=xml";

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.hide_views:
                tempText.setVisibility(View.INVISIBLE);
                descriptionText.setVisibility(View.INVISIBLE);
                tempMaxText.setVisibility(View.INVISIBLE);
                tempMinText.setVisibility(View.INVISIBLE);
                humidityText.setVisibility(View.INVISIBLE);
                iv.setVisibility(View.INVISIBLE);
                cityText.setText("");
                break;
            case R.id.id_increase:
                oldSize++;
                tempText.setTextSize(oldSize);
                descriptionText.setTextSize(oldSize);
                tempMaxText.setTextSize(oldSize);
                tempMinText.setTextSize(oldSize);
                humidityText.setTextSize(oldSize);
                cityText.setTextSize(oldSize);
                break;
            case R.id.id_decrease:
                oldSize--;
                tempText.setTextSize(oldSize);
                descriptionText.setTextSize(oldSize);
                tempMaxText.setTextSize(oldSize);
                tempMinText.setTextSize(oldSize);
                humidityText.setTextSize(oldSize);
                cityText.setTextSize(oldSize);
                break;
            case 5:
                String cityName = item.getTitle().toString();
                runForeCast(cityName);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.popout_menu);
        navigationView.setNavigationItemSelectedListener((item)-> {
                onOptionsItemSelected(item);
                drawer.closeDrawer(GravityCompat.START);
                return false;
        });
         cityText = findViewById(R.id.cityTextField);
         forecastBtn = findViewById(R.id.forecastButton);
         tempText = findViewById(R.id.textViewTemp);
         tempMaxText = findViewById(R.id.textViewTempMax);
         tempMinText = findViewById(R.id.textViewTempMin);
         humidityText = findViewById(R.id.textViewHumidity);
         descriptionText = findViewById(R.id.textViewDescription);
        iv = findViewById(R.id.imageView4);
        forecastBtn.setOnClickListener((click) -> {
            String cityName =cityText.getText().toString();
            myToolbar.getMenu().add(1,5,10,cityName).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            runForeCast(cityName);

        });
    }

    private void runForeCast(String cityName) {
        cityText.setText(cityName);
        AlertDialog dialog= new AlertDialog.Builder(MainActivity.this)
                .setTitle("Getting forecast")
                .setMessage("We're calling people in "+cityName+" to look outside their windows and tell us what's the weather like over there.")
                .setView(new ProgressBar(MainActivity.this))
                .show();
        Executor newThread = Executors.newSingleThreadExecutor();
        newThread.execute(() -> {
            //this runs another thread
            try {

                String temp = URLEncoder.encode(cityName, "UTF-8");
                String stringURL1 = String.format(stringURL, URLEncoder.encode(cityName, "UTF-8"));
                URL url = new URL(stringURL1);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                // xml parse
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( in  , "UTF-8");

                String description = null;
                String iconName = null;
                String current = null;
                String min = null;
                String max = null;
                String humidity = null;
                while( xpp.next() != XmlPullParser.END_DOCUMENT )
                {
                    switch ( xpp.getEventType())
                    {
                        case XmlPullParser.START_TAG:
                            if (xpp.getName().equals("temperature")) {
                                current = xpp.getAttributeValue(null, "value"); //this gets the current temperature
                                min = xpp.getAttributeValue(null, "min"); //this gets the min temperature
                                max= xpp.getAttributeValue(null, "max"); //this gets the max temperature
                            }else if (xpp.getName().equals("weather"))
                            {
                                description =xpp.getAttributeValue(null, "value");
                                iconName =  xpp.getAttributeValue(null, "icon");
                            }else if (xpp.getName().equals("humidity"))
                            {
                                humidity =xpp.getAttributeValue(null, "value");
                            }
                            break;
                        case XmlPullParser.END_TAG:

                            break;
                        case XmlPullParser.TEXT:
                            break;
                    }
                }

                // text is JSON string

//                    String text = (new BufferedReader(
//                            new InputStreamReader(in, StandardCharsets.UTF_8)))
//                            .lines()
//                            .collect(Collectors.joining("\n"));
//                    JSONObject theDocument = new JSONObject(text); //this converts the String to JSON Object.
//                    JSONObject mainObject = theDocument.getJSONObject( "main" );
//                    double current = mainObject.getDouble("temp");
//                    double min = mainObject.getDouble("temp_min");
//                    double max = mainObject.getDouble("temp_max");
//                    int humidity = mainObject.getInt("humidity");
//                    JSONArray weatherArray = theDocument.getJSONArray("weather");
//                    JSONObject pos0Obj = weatherArray.getJSONObject(0);
//                    String description = pos0Obj.getString("description");
//                    String iconName = pos0Obj.getString("icon");
                //download that URL and store it as a bitmap
                Bitmap image = null;
                File file = new File(getFilesDir(),iconName+".png");
                if (file.exists()){
                    image = BitmapFactory.decodeFile(getFilesDir()+"/"+iconName+".png");
                }else{
                    URL imgUrl = new URL( "https://openweathermap.org/img/w/" + iconName + ".png" );
                    HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        image = BitmapFactory.decodeStream(connection.getInputStream());
                        image.compress(Bitmap.CompressFormat.PNG, 100, openFileOutput( iconName + ".png", Context.MODE_PRIVATE));
                    }
                }

                Bitmap finalImage = image;
                //set value to ui
                String finalCurrent = current;
                String finalDescription = description;
                String finalMin = min;
                String finalMax = max;
                String finalHumidity = humidity;

                runOnUiThread(()->{
                    tempText.setText(String.format("Current temperature: %s" ,finalCurrent));
                    tempText.setVisibility(View.VISIBLE);
                    tempMaxText.setText(String.format("Highest temperature: %s" ,finalMax));
                    tempMaxText.setVisibility(View.VISIBLE);
                    tempMinText.setText(String.format("Lowest temperature: %s" ,finalMin));
                    tempMinText.setVisibility(View.VISIBLE);
                    humidityText.setText(String.format("Humidity: %s" ,finalHumidity)+"%");
                    humidityText.setVisibility(View.VISIBLE);
                    descriptionText.setText(String.format("Description: %s" ,finalDescription));
                    descriptionText.setVisibility(View.VISIBLE);
                    iv.setImageBitmap(finalImage);
                    iv.setVisibility(View.VISIBLE);
                    dialog.hide();

                });
            } catch (IOException ioe) {
                Log.e("Connection error:", ioe.getMessage());
            }
//                catch (JSONException je) {
//                    je.printStackTrace();
//                }
            catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        });
    }

}