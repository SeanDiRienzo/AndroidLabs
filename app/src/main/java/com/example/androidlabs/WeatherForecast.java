package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;

public class WeatherForecast extends AppCompatActivity {

    ProgressBar weatherPB;
    ImageView weatherIV;
    TextView currentTempTV;
    TextView minTempTV;
    TextView maxTempTV;
    TextView uvTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        ForecastQuery theQuery = new ForecastQuery();
        theQuery.execute();

        weatherIV = findViewById(R.id.weatherImageView);
        currentTempTV = findViewById(R.id.currentTemperature);
        minTempTV = findViewById(R.id.minimumTemperature);
        maxTempTV = findViewById(R.id.maximumTemperature);
        uvTV = findViewById(R.id.uvRating);

        weatherPB = findViewById(R.id.weatherProgress);
        weatherPB.setVisibility(View.VISIBLE);

    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        private String wind;
        private String UV;
        private String minTemp;
        private String maxTemp;
        private String currentTemp;
        private Bitmap outlookPic;

        @Override
        protected String doInBackground(String... strings) {    // Type1 is for doInBackground parameter
            String returnStrForOnPostExecute = null;
            String forecastUrlStr = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=bc2d8682d08b58cb83309fa6ef1f89c1&mode=xml&units=metric";

            String iconName = null;


            try {
                //     // connect to the weather forecast server
                URL forecastUrl = new URL(forecastUrlStr);
                HttpURLConnection urlConnection = (HttpURLConnection) forecastUrl.openConnection();
                InputStream inStreamForecast = urlConnection.getInputStream();

                // create parser for the XML
                XmlPullParserFactory pullParserFactory = XmlPullParserFactory.newInstance();
                pullParserFactory.setNamespaceAware(false);
                XmlPullParser xpp = pullParserFactory.newPullParser();
                xpp.setInput(inStreamForecast, "UTF-8");

                int EVENT_TYPE;
                while ((EVENT_TYPE = xpp.getEventType()) != XmlPullParser.END_DOCUMENT) {
                    // check if the tag's name is temperature
                    if (EVENT_TYPE == START_TAG) {
                        String tagName = xpp.getName();
                        switch (tagName) {
                            case "temperature":
                                currentTemp = xpp.getAttributeValue(null, "value");
                                publishProgress(25);
                                minTemp = xpp.getAttributeValue(null, "min");
                                publishProgress(50);
                                maxTemp = xpp.getAttributeValue(null, "max");
                                publishProgress(75);
                                break;
                            case "speed":
                                wind = xpp.getAttributeValue(null, "value");
                                break;
                            case "weather":
                                iconName = xpp.getAttributeValue(null, "icon");
                        }
                    }
                    // move cursor to next XML element
                    xpp.next();
                }

                String fileName = iconName + ".png";


                Log.i("Searching for file", fileName);
                if (fileExistence(fileName)) {
                    Log.i("Found file", fileName);
                    FileInputStream fis = null;
                    try {
                        fis = openFileInput(fileName);
                        outlookPic = BitmapFactory.decodeStream(fis);
                    } catch (FileNotFoundException fne) {
                        fne.printStackTrace();
                    }
                } else {
                    // gets the outlook picture (cloudy, sunny, rainy, etc.) from the server
                    URL outlookPicUrl = new URL("http://openweathermap.org/img/w/" + fileName);
                    urlConnection = (HttpURLConnection) outlookPicUrl.openConnection();
                    urlConnection.connect();
                    int responseCode = urlConnection.getResponseCode();
                    if (responseCode == 200) {
                        outlookPic = BitmapFactory.decodeStream(urlConnection.getInputStream());
                    }

                    // save outlookPic image to local storage
                    Log.i("Downloading file", fileName);
                    FileOutputStream outputStream = openFileOutput(fileName,
                            Context.MODE_PRIVATE);
                    outlookPic.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                }

                publishProgress(100);

                // connects to the UV index server
                String uvUrlStr = "http://api.openweathermap.org/data/2.5/uvi?appid=bc2d8682d08b58cb83309fa6ef1f89c1&lat=45.348945&lon=-75.759389";
                URL uvUrl = new URL(uvUrlStr);
                urlConnection = (HttpURLConnection) uvUrl.openConnection();
                InputStream inStreamUv = urlConnection.getInputStream();

                BufferedReader jsonReader = new BufferedReader(new InputStreamReader(inStreamUv, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder(100);
                String line;
                while ((line = jsonReader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                JSONObject jObj = new JSONObject(result);
                UV = String.valueOf(jObj.getDouble("value"));

            } catch (
                    MalformedURLException mfe) {
                returnStrForOnPostExecute = "MalFormed URL exception";
            } catch (IOException ioe) {
                returnStrForOnPostExecute = "URL connection. Is wi-fi connected?";
            } catch (XmlPullParserException xppe) {
                returnStrForOnPostExecute = "XML Pull exception. The XML is not properly formed";
            } catch (JSONException jsone) {
                returnStrForOnPostExecute = "JSON object exception. There is an issue with the JSON file";
            }

            return returnStrForOnPostExecute;
        }

        @Override                   //Type 3
        protected void onPostExecute(String sentFromDoInBackground) {
            super.onPostExecute(sentFromDoInBackground);
            //update GUI Stuff:
            char degreeC = 0x2103;
            weatherIV.setImageBitmap(outlookPic);
            currentTempTV.setText(String.format("%s%c", currentTemp, degreeC));
            maxTempTV.setText(String.format("High: %s%c", maxTemp, degreeC));
            minTempTV.setText(String.format("Low: %s%c", minTemp, degreeC));
            uvTV.setText(String.format("UV Index: %s", UV));
            weatherPB.setVisibility(View.INVISIBLE);
        }

        @Override                       //Type 2
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //Update GUI stuff only (the ProgressBar):
            weatherPB.setVisibility(View.VISIBLE);
            weatherPB.setProgress(values[0]);
        }



        private boolean fileExistence(String fileName) {
            File file = getBaseContext().getFileStreamPath(fileName);
            return file.exists();
        }
    }
}












