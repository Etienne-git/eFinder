package com.example.efinder;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    //All methods in this adapter are required for a bare minimum recyclerview adapter
    private int listItemLayout;
    private ArrayList<String> itemList;
    private static int pos;
    private static boolean clicked = false;
    public static RecyclerViewClickListener itemListener;
    // Constructor of the class
    public RecyclerViewAdapter(int layoutId, RecyclerViewClickListener itemListener,  ArrayList<String> itemList) {
        listItemLayout = layoutId;
        this.itemList = itemList;
        this.itemListener = itemListener;
    }

    // get the size of the list
    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }


    // specify the row layout file and click for each row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(listItemLayout, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        TextView item = holder.item;
        item.setText(itemList.get(listPosition));
    }

    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView item;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            item = (TextView) itemView.findViewById(R.id.list_content_def);
        }
        @Override
        public void onClick(View view) {
            clicked = !clicked;
            if(clicked){
                item.setBackgroundColor(Color.parseColor("#A6D785"));
            }
            else{
                item.setBackgroundColor(Color.parseColor("#DCEEF2"));
            }
            itemListener.recyclerViewListClicked(view,this.getLayoutPosition());
        }
    }
    public interface RecyclerViewClickListener{
        public void recyclerViewListClicked(View v, int position);
    }
}