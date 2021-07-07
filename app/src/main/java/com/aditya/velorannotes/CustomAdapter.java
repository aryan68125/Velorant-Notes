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

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>
{
    private Context context;
    private ArrayList folder_id_CustomAdapter, folder_title_CustomAdapter, folder_detail_CustomAdapter;

    //item position index in our recylerView
    int position;

    //create an activity object so that we can refresh our recyclerView in our application
    Activity activity;

    //custom Adapter constructor should have a context and four different arrayList
    //because when we initialize this class inside our main activity
    //we want to pass all these array list that we have already created in our MainActivity
    CustomAdapter(Activity activity,Context context , ArrayList folder_id,ArrayList folder_title, ArrayList folder_detail)
    {
        //refreshing our Main activity RecyclerView with new data
        this.activity = activity;

        //setting up the parameter variable of this constructor to our global variable of this class
        //so we can access these objects in our full class
        this.context = context;
        this.folder_id_CustomAdapter = folder_id;
        this.folder_title_CustomAdapter = folder_title;
        this.folder_detail_CustomAdapter = folder_detail;

    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //we will be infating our row layout for our RecyclerView
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =  inflater.inflate(R.layout.custome_list_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {

        this.position = position;
        //getting data from arrays and setting the data to our textView that is present in row Layout for our RecylerView
        //here we are going to set Text into these textView's
        holder.folder_id_row.setText(String.valueOf(folder_id_CustomAdapter.get(position)));
        holder.folder_title_row_card_view_layout.setText(String.valueOf(folder_title_CustomAdapter.get(position)));

        //setting up the onclick Listener for our mainlayout
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ViewFolderContentsActivity.class);
                //sending data to View activity via intent
                intent.putExtra("id",String.valueOf(folder_id_CustomAdapter.get(position)));
                intent.putExtra("folder_name",String.valueOf(folder_title_CustomAdapter.get(position)));
                intent.putExtra("folder_content",String.valueOf(folder_detail_CustomAdapter.get(position)));
                //refreshing our Main activity recylerView with updated data
                activity.startActivityForResult(intent,1);
                //activity transition animation
                activity.overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);
            }
        });

        //setting up the onclick Listener for our mainlayout
        holder.mainLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // go to update Folder name Activity when the item is long pressed in the main activity
                Intent intent2 = new Intent(context,FolderUpdateNameActivity.class);
                //sending data to View activity via intent
                intent2.putExtra("id_folder_update",String.valueOf(folder_id_CustomAdapter.get(position)));
                intent2.putExtra("folder_name_folder_update",String.valueOf(folder_title_CustomAdapter.get(position)));
                intent2.putExtra("folder_content_folder_update",String.valueOf(folder_detail_CustomAdapter.get(position)));
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
        return folder_id_CustomAdapter.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView folder_id_row;
        TextView folder_title_row_card_view_layout;
        TextView folder_detail_row_card_view_layout;

        // calling our mainlayout from our recycler_view_row.xml file
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            folder_id_row = itemView.findViewById(R.id.folder_id_row);
            folder_title_row_card_view_layout = itemView.findViewById(R.id.folder_title_row_card_view_layout);
            folder_detail_row_card_view_layout = itemView.findViewById(R.id.folder_detail_row_card_view_layout);

            // binding our mainlayout from our recycler_view_row.xml file to CustomAdapter class
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}