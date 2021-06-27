package com.aditya.velorannotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class NoteUpdateActivity extends AppCompatActivity {

    String Note_id_String,folder_ID_String,Note_Title_String,Note_content_String;
    EditText Note_title_Note_update_activity;
    EditText Note_Content_Note_update_activity;

    MyNotesDatabaseHelper MyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_update);
        Note_title_Note_update_activity = findViewById(R.id.Note_title_Note_update_activity);
        Note_Content_Note_update_activity = findViewById(R.id.Note_Content_Note_update_activity);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        getIntentDataFromNotesCustomAdapter();

    }

    //get data from the CustomAdapter Layout via intent
    void getIntentDataFromNotesCustomAdapter()
    {
        if(getIntent().hasExtra("note_id") && getIntent().hasExtra("folder_id") && getIntent().hasExtra("note_title")&& getIntent().hasExtra("note_content"))
        {
            //get data from CustomAdapter class via Intent
            Note_id_String = getIntent().getStringExtra("note_id");
            folder_ID_String = getIntent().getStringExtra("folder_id");
            Note_Title_String = getIntent().getStringExtra("note_title");
            Note_content_String = getIntent().getStringExtra("note_content");

            //setting data to the editText
            Note_title_Note_update_activity.setText(Note_Title_String);
            Note_Content_Note_update_activity.setText(Note_content_String);
            Log.i("note_id",Note_id_String);
            Log.i("folder_id",folder_ID_String);
            Log.i("note_title",Note_Title_String);
            Log.i("note_content",Note_content_String);
        }
        else
        {
            Toast.makeText(this,"No data Err A113 000x2273821",Toast.LENGTH_SHORT).show();
        }
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_note_update_menu, menu);
        return true;
    }

    //it will handle the back button in the tool bar
    // this event will enable the back activity action bar back button
    // function to the button on press activity action bar back button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //android.R.id.home = default id for back button
        if(item.getItemId()==android.R.id.home){
            this.finish();
            overridePendingTransition(R.anim.nothing, R.anim.slide_out_right);
            return true;
        }
        else if(item.getItemId() == R.id.saveNote)
        {
           //add logic to update note in MyNotesDatabase

            //Second initialize the class and call update() function from that class
            //initiating MyDatabaseHelper class
            MyDB = new MyNotesDatabaseHelper(NoteUpdateActivity.this);
            //get text from editText
            String Note_Title_temp_editText = Note_title_Note_update_activity.getText().toString();
            String Note_content_temp_editText = Note_Content_Note_update_activity.getText().toString();
            MyDB.UpdateNotes(Note_id_String,Note_Title_temp_editText,Note_content_temp_editText);
        }
        return super.onOptionsItemSelected(item);
    }

    //this method handles back button of android os
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out_right);

    }
}