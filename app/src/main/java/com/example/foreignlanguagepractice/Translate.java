package com.example.foreignlanguagepractice;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foreignlanguagepractice.api.SpeechTask;
import com.example.foreignlanguagepractice.api.TranslateTask;

import java.util.ArrayList;
import java.util.List;

import static com.example.foreignlanguagepractice.MainActivity.phraseDatabase;

public class Translate extends AppCompatActivity {

    public ListView phrase;
    public TextView tvTranslate;
    public Button banPronounce;
    public String translatedTxt = "";
    private Spinner langSpinner;
    List<String> phrases = new ArrayList<>();
    List<String> subscribedLangs = new ArrayList<>();
    List<String> subscribedLangCodes = new ArrayList<>();
    public String selectedLangCode;
    public String selectedPhrase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DB phrasesDatabase = new DB(this);
        setContentView(R.layout.activity_translate);
        phrase = findViewById(R.id.phrase_list);
        phrase.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        langSpinner = findViewById(R.id.lang_spinner);
        tvTranslate = findViewById(R.id.tvTranslate);
        banPronounce = findViewById(R.id.btnPronounce);

        Cursor res = phrasesDatabase.getAllData();
        if (res.getCount() == 0) {
            Toast.makeText(Translate.this, "Nothing to show", Toast.LENGTH_LONG).show();
        } else {
            while (res.moveToNext()) {
                String name = res.getString(1);
                phrases.add(name);
                ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, phrases) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        View view = super.getView(position, convertView, parent);
                        TextView text = (TextView) view.findViewById(android.R.id.text1);

                        text.setTextColor(Color.WHITE);

                        return view;
                    }
                };
                phrase.setAdapter(aa);
            }
            phrase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                    for (int j = 0; j < parent.getChildCount(); j++) {
                        parent.getChildAt(j).setSelected(false);
                        parent.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                    }
                    selectedPhrase = phrases.get(position);
                    view.setBackgroundColor(Color.LTGRAY);
                }
            });
        }

        setSpinner();
    }

    public void setSpinner() {
        Cursor res = phraseDatabase.getAllLangs();

        if (res.getCount() == 0) {
            Toast.makeText(Translate.this, "Nothing have subscribed", Toast.LENGTH_LONG).show();
        } else {
            while (res.moveToNext()) {
                String lang = res.getString(1);
                String langCode = res.getString(2);
                int isSubscribed = res.getInt(3);

                if (isSubscribed == 1) {
                    subscribedLangs.add(lang);
                    subscribedLangCodes.add(langCode);
                }
            }
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subscribedLangs);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter
        langSpinner.setAdapter(dataAdapter);
    }

    public void setTranslatedTxt(String translatedTxt) {
        this.translatedTxt = translatedTxt;
    }

    public void translate(View view) {
        if (selectedPhrase == null) {
            Toast.makeText(Translate.this, "Please select a phrase to translate", Toast.LENGTH_LONG).show();
            return;
        }
        selectedLangCode = subscribedLangCodes.get(langSpinner.getSelectedItemPosition());
        TranslateTask task = new TranslateTask();
        task.execute(this);
    }

    public void play(View view) {
        SpeechTask task = new SpeechTask();
        task.execute(this);
    }
}
