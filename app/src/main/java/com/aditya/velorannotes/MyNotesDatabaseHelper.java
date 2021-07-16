package com.aditya.velorannotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyNotesDatabaseHelper extends SQLiteOpenHelper
{
    //create a context object that will help us to point to this MyDatabaseHelper class whenever we need it
    private Context context;

    //Constants
    //database name
    private static final String DATABASE_NAME = "Notes_collection.db";
    private static final int DATABASE_VERSION = 1;

    //table name
    public static final String  TABLE_NAME = "my_notes";

    //creating columns names
    private static final String COLUMN_ID = "notes_id";
    public static final String COLUMN_FOLDER_ID  ="folder_id";
    private static final String COLUMN_NOTE_TITLE ="note_title";
    private static final String COLUMN_NOTE_CONTENT ="note_content";

    public MyNotesDatabaseHelper(@Nullable Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //query is holding the sql statement needed for the operation
        //be sure to add spaces and follow the syntax otherwise it may cause issue
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FOLDER_ID+ " INTEGER, " +
                COLUMN_NOTE_TITLE + " TEXT, " +
                COLUMN_NOTE_CONTENT + " TEXT "+")" ;
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        //whenever we upgrade our database we need to call our oncreate method as well
        onCreate(db);
    }

    //this method will implement add Folder functionality to our application
    void addNote(Integer folder_id_String,String Note_name,String Note_content) {
        //create a sql Lite database object
        //this will refer to our SQLiteOpenHelper class
        //getWritableDatabase() will help us to write to our table
        SQLiteDatabase db = this.getWritableDatabase();
        //ContentValues cv will store all our data from our application and will pass this to our database table
        ContentValues cv = new ContentValues();
        //cv.put(key:"column name",value:"Data")
        if (Note_name.isEmpty() || Note_content.isEmpty()) {
            Toast.makeText(context, "Input fields can't be empty", Toast.LENGTH_LONG).show();
        } else {
            cv.put(COLUMN_FOLDER_ID, folder_id_String);
            cv.put(COLUMN_NOTE_TITLE, Note_name);
            //the folder detail will contain the notes_id which will come from another database
            cv.put(COLUMN_NOTE_CONTENT, Note_content);

            //now we will insert the data inside our database using our SQLite object db
            long result = db.insert(TABLE_NAME, null, cv);

            if (result == -1) //our application failed to insert the data
            {
                Toast.makeText(context, "Err Bad Protocol A113 0007287197x6211963H!", Toast.LENGTH_SHORT).show();
            } else //our application sucessfully inserted the data from our app to our database
            {
                Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
            }

        }
    }
/*
    Cursor ReadAllData()
    {
        //db.rawQuery("SELECT name,address FROM mytable WHERE name = 'Fred'");
        //it will select all the names called Fred

        //selecting all the data from our database table
        String query = "SELECT * FROM "+ TABLE_NAME + " WHERE " +COLUMN_FOLDER_ID+ " = "+Folder_id;
        //creaing an sql database object and get readable database this time
        SQLiteDatabase db = this.getReadableDatabase();
        //creating a cursor object
        Cursor cursor = null;
        if(db!=null)
        {
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }
    */

    /*
this function will be called in updateActivity class so that when press dlete button we will be able to delete
the selected row from the table of the database
*/
    void deleteOneNoteRowFromTheTableOfDatabase(String note_id)
    {
        SQLiteDatabase db = getWritableDatabase();
        long result = db.delete(TABLE_NAME,"notes_id=?",new String[]{note_id});
        if(result ==-1) //that means the delete operation failed
        {
            Toast.makeText(context,"Delete Operation Failed Err Sierra117 00AOSYRUSx$$53321",Toast.LENGTH_SHORT).show();
        }
        else //delete operation sucessfull
        {
            Toast.makeText(context,"Delete Operation Success!",Toast.LENGTH_SHORT).show();
        }
    }

    //this method will be called inside the UpdateActivity
    void UpdateNotes(String Note_id_String, String Note_Title_temp_editText,String Note_content_temp_editText)
    {     //for writing to our database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        if(Note_Title_temp_editText.isEmpty() || Note_content_temp_editText.isEmpty())
        {
            Toast.makeText(context, "Input fields can't be empty", Toast.LENGTH_LONG).show();
        }
        else {
            cv.put(COLUMN_NOTE_TITLE, Note_Title_temp_editText);
            cv.put(COLUMN_NOTE_CONTENT, Note_content_temp_editText);

            long result = db.update(TABLE_NAME, cv, "notes_id=?", new String[]{Note_id_String});

            if (result == -1) {
                Toast.makeText(context, "Update Failed! Err A007 27183xD997", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Update Success!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
this function will be called in updateActivity class so that when press dlete button we will be able to delete
the selected row from the table of the database
*/
    void deleteAllNotesContainingTheIdOfTheFolderTobeDeleted(String folder_id)
    {
        SQLiteDatabase db = getWritableDatabase();
        long result = db.delete(TABLE_NAME,"folder_id=?",new String[]{folder_id});
        if(result ==-1) //that means the delete operation failed
        {
            Toast.makeText(context,"Delete Notes Operation Failed Err Sierra117 00AOSYRUSx$$53321",Toast.LENGTH_SHORT).show();
        }
        else //delete operation sucessfull
        {
            Toast.makeText(context,"Delete Operation Success!",Toast.LENGTH_SHORT).show();
        }
    }
}
