package com.aditya.velorannotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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
import com.google.android.material.snackbar.Snackbar;

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

        //........Item swipe implementation on a recylerView items..........//
        //attach SimpleCallback To our recyclerView
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simplecallback);
        itemTouchHelper.attachToRecyclerView(recylerView_view_folder_content_activity);

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

    //........Item swipe implementation on a recylerView items..........//
    //these variables will hold the data incase if user press undo button
    String deleted_note_title=null;
    String Deleted_note_content=null;
    String Note_id_String = null;
    String Deleted_folder_id = null;
    //flag for refreshing the recylerView when user plress no in delete dialog box
    int position; //0 for no is not pressed //1 = no button is pressed in dialogue box

    //for swiping operation always put drag Dirs - 0
    //Enable left swipe to edit
    //Enable right swipe to Delete
    ItemTouchHelper.SimpleCallback simplecallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT)
    {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            //this method is only needed when you want to rearrange the rows inside the recyclerView
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            //getting the position of the row of the recyclerView
            position = viewHolder.getAdapterPosition();

            //this method is used for handeling the swipe
            switch(direction)
            {
                case ItemTouchHelper.RIGHT:
                    //add delete note logic
                    //open a delete dialog box
                    //add material library in gradel file so that we can use Snackbar inorder to give the user undo button option
                    //implementation 'com.google.android.material:material:<version>'

                    //save the data inside the backup variable before the delete function takes place
                    //getting data from ArrayList
                    deleted_note_title = note_title.get(position);
                    Deleted_note_content = note_content.get(position);
                    Note_id_String = note_id.get(position);
                    Deleted_folder_id = folder_id.get(position);

                    //now call te delete function from notesDatabaseHelper class
                    deleteConfirmDialogBox();

                    //now perform delete operation from the database
                    note_id.remove(position);
                    note_title.remove(position);
                    note_content.remove(position);
                    folder_id.remove(position);

                    NotesCustomAdapter.notifyItemRemoved(position);
                    //send notification to the user

                    Log.i("swipe","Right swipe done");
                    break;
            }

        }
    };

    //this functioon will create a delete confirm dialog box
    //this function willdelete the note from the database
    void deleteConfirmDialogBox()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.danger);
        builder.setTitle("Delete "+ deleted_note_title + " ?");
        builder.setMessage("Are you sure you want to delete note named "+ deleted_note_title+ " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //create MyDatabaseHelper Class object so that we can use the function deleteOneRowFromTheTableOfDatabase(String row_id)
                //from MyDatabaseHelper class
                MyNotesDatabaseHelper mydb = new MyNotesDatabaseHelper(ViewFolderContentsActivity.this);
                mydb.deleteOneNoteRowFromTheTableOfDatabase(Note_id_String);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing if no is pressed by the user in the delete dialogue box
                //recreate(); isnsted of this use the code below to make the user experience a whole lot better
                note_id.add(position,Note_id_String);
                note_title.add(position,deleted_note_title);
                note_content.add(position,Deleted_note_content);
                folder_id.add(position,Deleted_folder_id);
                NotesCustomAdapter.notifyItemInserted(position);
                recylerView_view_folder_content_activity.scrollToPosition(position);
            }
        });
        builder.create().show();
    }
}