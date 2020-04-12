package com.example.foreignlanguagepractice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static DatabaseManager phraseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phraseDatabase = new DatabaseManager(this);

        Button buttonAddPhrases = findViewById(R.id.addPhrases);
        Button buttonDisplayPhrases = findViewById(R.id.displayPhrases);
        Button buttonEditPhrases = findViewById(R.id.editPhrases);
        Button buttonLanguageSubscription = findViewById(R.id.languageSubscription);
        Button buttonTranslate = findViewById(R.id.translate);
        Button buttonOffline = findViewById(R.id.offline);


        buttonAddPhrases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), AddPhrases.class);
                startActivity(startIntent);
            }
        });

        buttonDisplayPhrases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), DisplayPhrases.class);
                startActivity(startIntent);
            }
        });

        buttonEditPhrases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), EditPhrases.class);
                startActivity(startIntent);
            }
        });

        buttonLanguageSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), LanguageSubscription.class);
                startActivity(startIntent);
            }
        });

        buttonTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), Translate.class);
                startActivity(startIntent);
            }
        });

        buttonOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), Offline.class);
                startActivity(startIntent);
            }
        });
    }
}
