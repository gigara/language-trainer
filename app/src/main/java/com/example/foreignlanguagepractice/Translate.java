package com.example.foreignlanguagepractice;

import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

public class Translate extends AppCompatActivity {

    private ListView phrase;
    private TextView tvTranslate;
    private Button btnPronouce;
    private String translatedTxt;
    public static DatabaseManager phrasesDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phrasesDatabase = new DatabaseManager(this);
        setContentView(R.layout.activity_translate);
        phrase = findViewById(R.id.phrase_list);
        tvTranslate = findViewById(R.id.tvTranslate);
        btnPronouce = findViewById(R.id.btnPronounce);
        viewAll();
    }

    public void viewAll() {
        Cursor res = phrasesDatabase.getAllData();
        ArrayList<String> items = new ArrayList<>();

        if (res.getCount() == 0) {
            Toast.makeText(Translate.this, "Nothing to show", Toast.LENGTH_LONG).show();
        } else {
            while (res.moveToNext()) {
                String name = res.getString(0);
                items.add(name);
                ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
                phrase.setAdapter(aa);
                phrase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> p1, View p2, int i, long p4) {
                        Intent intent = new Intent();
                    }
                });
            }
        }
    }

    public void translate(View view) {
        TranslateTask task = new TranslateTask();
        task.execute();
    }

    public void play(View view) {
        SpeechTask task = new SpeechTask();
        task.execute();
    }

    // TODO: translate the user selected phrase
    private class TranslateTask extends AsyncTask<String, Void, String> {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... urls) {
            String urlString = "https://api.us-south.language-translator.watson.cloud.ibm.com/instances/2b084659-4952-4486-a70b-bbeeee671351/v3/translate?version=2018-05-01";

            String userCredentials = "apikey:xxx";
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

            String jsonInputString = "{\"text\": \"Hello, world! \", \"model_id\":\"en-es\"}";

            HttpURLConnection urlConnection = null;
            URL url = null;
            JSONObject object = null;
            InputStream inStream = null;
            OutputStream outStream = null;
            try {
                url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Authorization", basicAuth);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                outStream = urlConnection.getOutputStream();
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                outStream.write(input, 0, input.length);

                inStream = urlConnection.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
                String temp, response = "";
                while ((temp = bReader.readLine()) != null) {
                    response += temp;
                }
                object = (JSONObject) new JSONTokener(response).nextValue();
                return (((JSONObject) ((JSONArray) object.get("translations")).get(0)).get("translation")).toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Failed to translate";
        }


        @Override
        protected void onPostExecute(String result) {
            tvTranslate.setText(result);
            btnPronouce.setEnabled(true);
            translatedTxt = result;
        }
    }

    // TODO: pronounce the user selected phrase
    private class SpeechTask extends AsyncTask<String, Void, String> {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... urls) {
            String urlString = "https://api.us-south.text-to-speech.watson.cloud.ibm.com/instances/9e8f04c7-597d-48d2-bb28-625691ed29fe/v1/synthesize";

            String userCredentials = "apikey:xxx";
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

            String jsonInputString = "{\"text\":\""+translatedTxt+"\"}";

            HttpURLConnection urlConnection = null;
            URL url = null;
            JSONObject object = null;
            InputStream inStream = null;
            OutputStream outStream = null;
            try {
                url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Authorization", basicAuth);
                urlConnection.setRequestProperty("Content-Type", "application/json");
//                urlConnection.setRequestProperty("Accept", "audio/wav");

                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                outStream = urlConnection.getOutputStream();
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                outStream.write(input, 0, input.length);

                inStream = urlConnection.getInputStream();

                // save file
                File path = new File(getExternalCacheDir(),"ltAudio");
                if (!path.exists()) {
                    path.mkdirs();
                }
                File file = new File(path, "test.wav");
                FileOutputStream fileOutput = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int bufferLength = 0;

                while ( (bufferLength = inStream.read(buffer)) > 0 ) {
                    fileOutput.write(buffer, 0, bufferLength);
                }
                fileOutput.close();

//                object = (JSONObject) new JSONTokener(response).nextValue();
                MediaPlayer mp = new MediaPlayer();
                mp.setDataSource(getApplicationContext(), Uri.parse("file://"+file.getPath()));
                mp.prepare();
                mp.start();
                return "Ok";

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Failed to translate";
        }
    }
}
