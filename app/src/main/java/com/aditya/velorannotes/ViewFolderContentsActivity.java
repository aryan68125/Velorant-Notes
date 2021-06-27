package com.aditya.velorannotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.aditya.velorannotes.MyNotesDatabaseHelper.COLUMN_FOLDER_ID;
import static com.aditya.velorannotes.MyNotesDatabaseHelper.TABLE_NAME;

public class ViewFolderContentsActivity extends AppCompatActivity {

    //creating an object of MyDatabaseHelper Class
    MyNotesDatabaseHelper notesdb;

    //NotesCustomAdapter
    NotesCustomAdapter NotesCustomAdapter;

    //now create an array list to hold the list of note_id,folder_id,note_title,note_content
    ArrayList<String> note_id,folder_id,note_title,note_content;

    //related to folder.db
    String folder_id_String;
    String folder_name_String,folder_content_String;

    FloatingActionButton Add_Button_view_folder_content_activity ;
    RecyclerView recylerView_view_folder_content_activity;

    ImageView empty_imageView_view_folder_content_activity;
    TextView textView_view_folder_content_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_folder_contents);

        Add_Button_view_folder_content_activity = findViewById(R.id.Add_Button_view_folder_content_activity);
        recylerView_view_folder_content_activity = findViewById(R.id.recylerView_view_folder_content_activity);
        empty_imageView_view_folder_content_activity = findViewById(R.id.empty_imageView_view_folder_content_activity);
        textView_view_folder_content_activity = findViewById(R.id.textView_view_folder_content_activity);

        //making the textView invisible
        textView_view_folder_content_activity.setVisibility(View.INVISIBLE);
        //making th imageView invisible
        empty_imageView_view_folder_content_activity.setVisibility(View.INVISIBLE);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        //First call this
        //this function gets data from the main activity holding the list of folders
        getIntentDataFromCustomAdapter();

        //setting up the title of the activity
        actionBar.setTitle(folder_name_String);

      //add an onclick listener on the floating add button
        Add_Button_view_folder_content_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open AddNote Activity
                Intent intent = new Intent(ViewFolderContentsActivity.this, AddNoteActivity.class);
                //send the folder_id_String via intent to Add note activity
                intent.putExtra("folder_id",folder_id_String);
                startActivity(intent);
                //activity transition animation
                overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);

            }
        });

        notesdb = new MyNotesDatabaseHelper(ViewFolderContentsActivity.this);
        note_id =  new ArrayList<>();
        folder_id =  new ArrayList<>();
        note_title =  new ArrayList<>();
        note_content = new ArrayList<>();

        //calling StoreDataInArrays() function
        StoreDataInArrays();

        //debugging
        Log.i("folder_id_array", String.valueOf(folder_id));

        //initialize our custom Adapter object
        /*
        customAdapter = new CustomAdapter(activity: MainActivity.this,context: this,book_id,book_title,book_author,book_page);
        the above line will refresh the recylerView in our main Activity by updating the incoming data from CustomAdapter class
         */
        NotesCustomAdapter = new NotesCustomAdapter(ViewFolderContentsActivity.this,this,note_id,folder_id,note_title,note_content);
        recylerView_view_folder_content_activity.setAdapter(NotesCustomAdapter);
        recylerView_view_folder_content_activity.setLayoutManager(new LinearLayoutManager(ViewFolderContentsActivity.this));
    }

    //reading notes from database having same folder id as the selected folder in main activity
    public Cursor call_notes_via_folder_id()
    {
        //create MyDatabaseHelper class object
        MyNotesDatabaseHelper MyNotesDB = new MyNotesDatabaseHelper(ViewFolderContentsActivity.this);
        //getting data from
        String Folder_ID = folder_id_String;
        //selecting all the data from our database table
        String query = "SELECT * FROM "+ TABLE_NAME + " WHERE " +COLUMN_FOLDER_ID+ " = "+Folder_ID;
        //creaing an sql database object and get readable database this time
        SQLiteDatabase db = MyNotesDB.getReadableDatabase();
        //creating a cursor object
        Cursor cursor = null;
        if(db!=null)
        {
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    void StoreDataInArrays()
    {
        //here save note in database

        //calling ReadAllData() method or function from MyDatabaseHelper class
        Cursor cursor = call_notes_via_folder_id();
        if(cursor.getCount() == 0) //that means there is no data
        {
            empty_imageView_view_folder_content_activity.setVisibility(View.VISIBLE);
            textView_view_folder_content_activity.setVisibility(View.VISIBLE);
        }
        else
        {
            //move the cursor to the next position in the table
            //adding data from our database to our arrays
            while(cursor.moveToNext())
            {
                    note_id.add(cursor.getString(0));
                    folder_id.add(cursor.getString(1));
                    note_title.add(cursor.getString(2));
                    note_content.add(cursor.getString(3));
                }


        }
    }// DisplayData function closed

    //get data from the CustomAdapter Layout via intent
    //this function gets data from the main activity holding the list of folders
    void getIntentDataFromCustomAdapter()
    {
        if(getIntent().hasExtra("id") && getIntent().hasExtra("folder_name") && getIntent().hasExtra("folder_content"))
        {
            //get data from CustomAdapter class via Intent
            folder_id_String = getIntent().getStringExtra("id");
            folder_name_String = getIntent().getStringExtra("folder_name");
            folder_content_String = getIntent().getStringExtra("folder_content");

            //setting data to the editText
            Log.i("folder_id",folder_id_String);
            Log.i("folder_name",folder_name_String);
        }
        else
        {
            Toast.makeText(this,"No data Err A113 000x2273821",Toast.LENGTH_SHORT).show();
        }
    }

    // this event will enable the back activity action bar back button
    // function to the button on press activity action bar back button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                overridePendingTransition(R.anim.nothing, R.anim.slide_out_right);
                return true;
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

    //refreshing the recylerView in our main activity with new data when mainActivity restarts
    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }
}