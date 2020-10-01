package com.example.cycleurbike.activities;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.example.cycleurbike.R;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherScreen extends AppCompatActivity {
    String CITY = "Jerusalem";
    String API = "97dbf361cd33e8c1312166c0f2c91686";
    EditText enterCity;
    TextView addressTxt, updated_atTxt, statusTxt, tempTxt, temp_minTxt, temp_maxTxt, sunriseTxt,
            sunsetTxt, windTxt, pressureTxt, humidityTxt,feelsLikeTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_screen);
        addressTxt = findViewById(R.id.address);
        updated_atTxt = findViewById(R.id.updated_at);
        statusTxt = findViewById(R.id.status);
        tempTxt = findViewById(R.id.temp);
        temp_minTxt = findViewById(R.id.temp_min);
        temp_maxTxt = findViewById(R.id.temp_max);
        sunriseTxt = findViewById(R.id.sunrise);
        sunsetTxt = findViewById(R.id.sunset);
        windTxt = findViewById(R.id.wind);
        pressureTxt = findViewById(R.id.pressure);
        humidityTxt = findViewById(R.id.humidity);
        enterCity = findViewById(R.id.enterCiy);
        feelsLikeTxt = findViewById(R.id.feelLike);
        new weatherTask().execute();

    }

    public void popUpMessage(String msg) { //פונקציה שמקפיצה הודעת שגיאה באם התיבת חיפוש של העיר ריקה או שהעיר עצמה אינה קיימת
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this,R.style.AlertDialogCustom);

        // set title
        alertDialogBuilder.setTitle(msg);

        // set dialog message
        alertDialogBuilder
                .setMessage("")
                .setCancelable(false)
                .setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {  //כפתור לאישור ההודעה
                        // if this button is clicked, close
                        // current activity
                        // WeatherScreen.this.closeContextMenu();
                        new weatherTask().execute();
                    }
                })
                .setNegativeButton("", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { //כפתור לביטול ההודעה ויציאה מן ההודעה ללא פעולה
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        //dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void citySearch(View view) {//פונקציה שמופעלת בלחיצה על כפתור חיפוש לאחר הקלדת עיר מסויימת ומשנה את המסך בהתאם

        if (!(enterCity.getText().toString().matches(""))) { //אם שדה חיפוש העיר אינו ריק
            CITY = enterCity.getText().toString();
            new weatherTask().execute();
        } else { //אם שדה חיפוש העיר ריק
            popUpMessage("אנא כתוב עיר");
        }
    }
    public static String HttpRequestGet(String targetURL) {  //פונקציה לבקשת HTTP
        URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream is;
            int status = connection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK)
                is = connection.getErrorStream();
            else
                is = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
        class weatherTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                /* Showing the ProgressBar, Making the main design GONE */
                findViewById(R.id.loader).setVisibility(View.VISIBLE);
                findViewById(R.id.mainContainer).setVisibility(View.GONE);
                findViewById(R.id.errorText).setVisibility(View.GONE);
            }

            protected String doInBackground(String... args) {
                String response = HttpRequestGet("https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&lang=he" + "&units=metric&appid=" + API);
                // return response
                return response;
            }

            @Override
            protected void onPostExecute(String result) {

                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONObject main = jsonObj.getJSONObject("main");
                    JSONObject sys = jsonObj.getJSONObject("sys");
                    JSONObject wind = jsonObj.getJSONObject("wind");
                    JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                    Long updatedAt = jsonObj.getLong("dt");
                    String updatedAtText = "עודכן ב: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                    String temp = main.getString("temp") + "°C";
                    String tempMin = "מינ' טמפרטורה: " + main.getString("temp_min") + "°C";
                    String tempMax = "מקס' טמפרטורה: " + main.getString("temp_max") + "°C";
                    String pressure = main.getString("pressure");
                    String humidity = main.getString("humidity");

                    Long sunrise = sys.getLong("sunrise");
                    Long sunset = sys.getLong("sunset");
                    String windSpeed = wind.getString("speed");
                    String weatherDescription = weather.getString("description");

                    String address = jsonObj.getString("name") + ", " + sys.getString("country");
                    String feelsLike = main.getString("feels_like");
                    /* Populating extracted data into our views */
                    addressTxt.setText(address);
                    updated_atTxt.setText(updatedAtText);
                    statusTxt.setText(weatherDescription.toUpperCase());
                    tempTxt.setText(temp);
                    temp_minTxt.setText(tempMin);
                    temp_maxTxt.setText(tempMax);
                    sunriseTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
                    sunsetTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
                    windTxt.setText(windSpeed + " קמ\"ש");
                    pressureTxt.setText(pressure);
                    humidityTxt.setText(humidity + "%");
                    feelsLikeTxt.setText(feelsLike);

                    /* Views populated, Hiding the loader, Showing the main design */
                    findViewById(R.id.loader).setVisibility(View.GONE);
                    findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    //findViewById(R.id.loader).setVisibility(View.GONE);
                    //findViewById(R.id.errorText).setVisibility(View.VISIBLE);
                    popUpMessage("אנא הכנס עיר תקינה");
                    CITY = "jerusalem";
                    enterCity.getText().clear();
                    new weatherTask().execute();
                }
            }
        }
    }

