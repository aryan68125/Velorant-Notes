package com.aditya.velorannotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class ViewNoteContentActivity extends AppCompatActivity {

    TextView note_content_view_note_activity;

    String Note_id_String,folder_id_String,Note_title_String,Note_content_String;

    //setting up text to speech listener
    TextToSpeech mtts;
    int everythingIsOKmttsIsGoodToGo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note_content);
        note_content_view_note_activity = findViewById(R.id.note_content_view_note_activity);
        // setMovementMethod(new ScrollingMovementMethod()); it will enable the scrolling of our textView
        note_content_view_note_activity.setMovementMethod(new ScrollingMovementMethod());

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        //now get data from NotesCustomAdapter into this ViewNoteContent Activity
        getIntentDataFromNotesCustomAdapter();

        //set the activity actionbar title
        //setting up the title of the activity
        actionBar.setTitle(Note_title_String);

        //code related to text to speech engine
        //setting up text to speech engine
        mtts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    //checking if this set language method was successfull
                    int result = mtts.setLanguage(Locale.ENGLISH); //passing language to our text to speech engine if its initializaton is a success
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        //if there is a missing data or language not supported by the device then we will show an error message
                        Toast.makeText(getApplicationContext(), "Either the language is not supported by your device or the input field is empty", Toast.LENGTH_LONG).show();
                    } else {
                        //if there is no error and text to speech is successfully loaded then button is enabled
                        everythingIsOKmttsIsGoodToGo = 1;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Initialization of text to speech engine failed!!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        //stopping mtts when the app is closed
        if(mtts!=null){
            mtts.stop();
            mtts.shutdown();
        }
        super.onDestroy();
    }

    //get data from the CustomAdapter Layout via intent
    void getIntentDataFromNotesCustomAdapter()
    {
        if(getIntent().hasExtra("note_id") && getIntent().hasExtra("folder_id") && getIntent().hasExtra("note_title") && getIntent().hasExtra("note_content"))
        {
            //get data from CustomAdapter class via Intent
            Note_id_String = getIntent().getStringExtra("note_id");
            folder_id_String = getIntent().getStringExtra("folder_id");
            Note_title_String = getIntent().getStringExtra("note_title");
            Note_content_String = getIntent().getStringExtra("note_content");

            //setting data to the editText
            note_content_view_note_activity.setText(Note_content_String);
            Log.i("note_id",Note_id_String);
            Log.i("folder_id",folder_id_String);
            Log.i("note_title",Note_title_String);
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
        getMenuInflater().inflate(R.menu.view_note_content_activity_actionbar_menu, menu);
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
        else if(item.getItemId() == R.id.DeleteNote)
        {
            //delete note operation from the database
            deleteConfirmDialogBox();
        }
        else if(item.getItemId()==R.id.SpeakNote)
        {
            //add text to speech engine
            if (everythingIsOKmttsIsGoodToGo == 1) {
                String text = note_content_view_note_activity.getText().toString();
                mtts.setPitch(1.1f); //setting up the pitch and speed of the speech in text to speech engine
                mtts.setSpeechRate(1.1f);
                //making text to speech engine to speek our entered text
                //TextToSpeech.QUEUE_FLUSH = current txt is cancled to speak a new one
                //TextToSpeech.QUEUE_ADD the next text is spoken after the previous text is finished
                //mtts.speak(Passing the content of our editText, TextToSpeech.QUEUE_FLUSH,null);
                mtts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //this functioon will create a delete confirm dialog box
    void deleteConfirmDialogBox()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.danger);
        builder.setTitle("Delete "+ Note_title_String + " ?");
        builder.setMessage("Are you sure you want to delete note named "+ Note_title_String+ " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //create MyDatabaseHelper Class object so that we can use the function deleteOneRowFromTheTableOfDatabase(String row_id)
                //from MyDatabaseHelper class
                MyNotesDatabaseHelper mydb = new MyNotesDatabaseHelper(ViewNoteContentActivity.this);
                mydb.deleteOneNoteRowFromTheTableOfDatabase(Note_id_String);
                //finish(); will destroy our update activity and open the main Activity
                finish();
                overridePendingTransition(R.anim.nothing, R.anim.slide_out_right);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing if no is pressed by the user in the delete dialogue box
            }
        });
        builder.create().show();
    }

    //this method handles back button of android os
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out_right);

    }
}