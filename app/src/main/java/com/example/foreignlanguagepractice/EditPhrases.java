package com.example.foreignlanguagepractice;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import static com.example.foreignlanguagepractice.MainActivity.phraseDatabase;

public class EditPhrases extends AppCompatActivity {

    ListView checkList;
    ArrayList<String> selectedItems;
    EditText etEditPhrase;
    Button btnEditPhraseEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phrases);
        checkList = findViewById(R.id.checkable_list);
        checkList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        selectedItems = new ArrayList<String>();
        etEditPhrase = findViewById(R.id.etEditPhrase);
        btnEditPhraseEdit = findViewById(R.id.btnEditPhraseEdit);
        viewAll();

    }

    public void viewAll() {
        Cursor res = phraseDatabase.getAllData();
        ArrayList<String> items = new ArrayList<>();

        if (res.getCount() == 0) {
            Toast.makeText(EditPhrases.this, "Nothing to show", Toast.LENGTH_LONG).show();
        } else {
            while (res.moveToNext()) {
                String phrase = res.getString(0);
                items.add(phrase);

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.checkable_list_layout, R.id.txt_title, items);
                checkList.setAdapter(arrayAdapter);


                checkList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // selected item
                        String selectedItem = ((TextView) view).getText().toString();
                        Intent intent = new Intent();
                        intent.setClass(EditPhrases.this, DisplayPhrases.class);
                        intent.putExtra("Phrase", selectedItem);
                        if (selectedItems.contains(selectedItem))
                            selectedItems.remove(selectedItem);
                        else
                            selectedItems.add(selectedItem);
                    }
                });
            }

        }
    }

    public void fillFields(View v){

    }

    public void update(View v){
        boolean isUpdate = phraseDatabase.updateData(
                etEditPhrase.getText().toString());
        if (isUpdate == true) {
            Toast.makeText(EditPhrases.this, "Updated", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this,DisplayPhrases.class);
            finish();
            startActivity(intent);

        } else {
            Toast.makeText(EditPhrases.this, "Not Updated", Toast.LENGTH_LONG).show();

        }
    }
}
