package com.example.foreignlanguagepractice.api;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.foreignlanguagepractice.Translate;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Play the translated phrase as audio
 */
public class SpeechTask extends AsyncTask<Context, Void, String> {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected String doInBackground(Context... params) {
        Translate context = (Translate) params[0];
        Wrapper wrapper = new Wrapper();
        wrapper.context = context;

        String urlString = "https://api.us-south.text-to-speech.watson.cloud.ibm.com/instances/9e8f04c7-597d-48d2-bb28-625691ed29fe/v1/synthesize";

        String userCredentials = "apikey:xxx";
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

        String jsonInputString = "{\"text\":\"" + context.translatedTxt + "\"}";

        HttpURLConnection connection = null;
        URL url = null;
        JSONObject object = null;
        InputStream inStream = null;
        OutputStream outStream = null;
        try {
            url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", basicAuth);
            connection.setRequestProperty("Content-Type", "application/json");

            connection.setDoInput(true);
            connection.setDoOutput(true);
            outStream = connection.getOutputStream();
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            outStream.write(input, 0, input.length);

            inStream = connection.getInputStream();

            // save file to app dir
            File path = new File(context.getExternalCacheDir(), "audio");
            if (!path.exists()) {
                path.mkdirs();
            }
            File file = new File(path, "audio1.wav");
            FileOutputStream fileOutput = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ((bufferLength = inStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
            }
            fileOutput.close();

            // play the saved file
            MediaPlayer player = new MediaPlayer();
            player.setDataSource(context, Uri.parse("file://" + file.getPath()));
            player.prepare();
            player.start();
            return "Ok";

        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(context,"Failed to speech", Toast.LENGTH_LONG).show();
        return "Failed to speech";
    }
}
