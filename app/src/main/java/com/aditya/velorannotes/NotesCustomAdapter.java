package com.aditya.velorannotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesCustomAdapter extends RecyclerView.Adapter<NotesCustomAdapter.MyViewHolder>
{
    private Context context;
    private ArrayList note_id_CustomAdapter, folder_id_CustomAdapter, note_title_CustomAdapter,note_content_CustomAdapter;

    //item position index in our recylerView
    int position;

    //create an activity object so that we can refresh our recyclerView in our application
    Activity activity;

    //custom Adapter constructor should have a context and four different arrayList
    //because when we initialize this class inside our main activity
    //we want to pass all these array list that we have already created in our MainActivity
    NotesCustomAdapter(Activity activity,Context context , ArrayList note_id,ArrayList folder_id, ArrayList note_title,ArrayList note_content)
    {
        //refreshing our Main activity RecyclerView with new data
        this.activity = activity;

        //setting up the parameter variable of this constructor to our global variable of this class
        //so we can access these objects in our full class
        this.context = context;
        this.note_id_CustomAdapter = note_id;
        this.folder_id_CustomAdapter = folder_id;
        this.note_title_CustomAdapter = note_title;
        this.note_content_CustomAdapter = note_content;
    }

    @NonNull
    @Override
    public NotesCustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //we will be infating our row layout for our RecyclerView
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =  inflater.inflate(R.layout.note_list_card_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        this.position = position;
        //getting data from arrays and setting the data to our textView that is present in row Layout for our RecylerView
        //here we are going to set Text into these textView's
        holder.note_id__note_card_view_layout.setText(String.valueOf(note_id_CustomAdapter.get(position)));
        holder.folder_id_note_row.setText(String.valueOf(folder_id_CustomAdapter.get(position)));
        holder.note_title_note_card_view_layout.setText(String.valueOf(note_title_CustomAdapter.get(position)));
        holder.note_content_id_row.setText(String.valueOf(note_content_CustomAdapter.get(position)));

        //ad onclick listener on recylerView items
        //setting up the onclick Listener for our mainlayout
        holder.note_card_view_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ViewNoteContentActivity.class);
                //sending data to View activity via intent
                intent.putExtra("note_id",String.valueOf(note_id_CustomAdapter.get(position)));
                intent.putExtra("folder_id",String.valueOf(folder_id_CustomAdapter.get(position)));
                intent.putExtra("note_title",String.valueOf(note_title_CustomAdapter.get(position)));
                intent.putExtra("note_content",String.valueOf(note_content_CustomAdapter.get(position)));
                //refreshing our Main activity recylerView with updated data
                activity.startActivityForResult(intent,1);
                //activity transition animation
                activity.overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);
            }
        });

        //add onlong click listener on recyclerview items

        //setting up the onclick Listener for our mainlayout
        holder.note_card_view_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // go to update Folder name Activity when the item is long pressed in the main activity
                Intent intent2 = new Intent(context,NoteUpdateActivity.class);
                //sending data to View activity via intent
                intent2.putExtra("note_id",String.valueOf(note_id_CustomAdapter.get(position)));
                intent2.putExtra("folder_id",String.valueOf(folder_id_CustomAdapter.get(position)));
                intent2.putExtra("note_title",String.valueOf(note_title_CustomAdapter.get(position)));
                intent2.putExtra("note_content",String.valueOf(note_content_CustomAdapter.get(position)));
                //refreshing our Main activity recylerView with updated data
                activity.startActivityForResult(intent2,1);
                //activity transition animation
                activity.overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return note_id_CustomAdapter.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView note_title_note_card_view_layout; //holds the title of a note
        TextView note_id__note_card_view_layout; //holds the id of the note from note.db
        TextView folder_id_note_row; //holds the folder_id
        TextView note_content_id_row; //will hold the contents of the note

        // calling our mainlayout from our recycler_view_row.xml file
        ConstraintLayout note_card_view_layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //now getting the id's of those textView's
            note_title_note_card_view_layout = itemView.findViewById(R.id.note_title_note_card_view_layout);
            note_id__note_card_view_layout = itemView.findViewById(R.id.note_id__note_card_view_layout);
            folder_id_note_row = itemView.findViewById(R.id.folder_id_note_row);
            note_content_id_row = itemView.findViewById(R.id.note_content_id_row);

            // binding our mainlayout from our recycler_view_row.xml file to CustomAdapter class
            note_card_view_layout = itemView.findViewById(R.id.note_card_view_layout);
        }
    }
}
