package com.example.foreignlanguagepractice.api;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.foreignlanguagepractice.R;
import com.example.foreignlanguagepractice.Translate;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TranslateTask extends AsyncTask<Context, Void, Wrapper> {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected Wrapper doInBackground(Context... params) {
        Translate context = (Translate) params[0];
        Wrapper wrapper = new Wrapper();
        wrapper.context = context;

        String urlString = "https://api.us-south.language-translator.watson.cloud.ibm.com/instances/2b084659-4952-4486-a70b-bbeeee671351/v3/translate?version=2018-05-01";
        String apiKey = "apikey:xxx";

        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(apiKey.getBytes()));

        String jsonInputString = "{\"text\": \"" + context.selectedPhrase + "\", \"model_id\":\"en-" + context.selectedLangCode + "\"}";

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
            wrapper.result = (((JSONObject) ((JSONArray) object.get("translations")).get(0)).get("translation")).toString();
            return wrapper;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return wrapper;
    }


    @Override
    protected void onPostExecute(Wrapper wrapper) {
        if (wrapper.result != null) {
            wrapper.context.tvTranslate.setText(wrapper.result);
            wrapper.context.banPronounce.setEnabled(true);
            wrapper.context.setTranslatedTxt(wrapper.result);
        } else {
            wrapper.context.tvTranslate.setText(R.string.failed_to_translate);
        }
    }
}
