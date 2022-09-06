package com.samsung.drawshapes.ListAndRecyclerViews;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.samsung.drawshapes.Dialogs.Settings;
import com.samsung.drawshapes.R;
import com.samsung.drawshapes.db.MyColor;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private ArrayList<MyColor> listdata;
    private Activity mainActivity;
    private int selectedPosition;

    // RecyclerView recyclerView;
    public RecyclerViewAdapter(ArrayList<MyColor> listdata, Activity mainActivity) {
        this.listdata = listdata;
        this.mainActivity = mainActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.recycler_view_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.textView.setText(listdata.get(position).colorName);
        holder.colorBgView.setBackgroundColor(listdata.get(position).colorValue);
        //holder.textView.setBackgroundColor(Color.WHITE);
        if(selectedPosition == position){
            holder.textView.setBackgroundColor(Color.GRAY);
            // holder.tv1.setTextColor(Color.parseColor("#ffffff"));
        }
        else
        {
               holder.textView.setBackgroundColor(Color.WHITE);
            //  holder.tv1.setTextColor(Color.parseColor("#000000"));
        }
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = holder.getAdapterPosition();
                //Settings.SetIdToDelete(holder.getAdapterPosition());
                Settings.setSelectedColor(listdata.get(holder.getAdapterPosition()).colorValue);
                notifyDataSetChanged();
           }
        });
        holder.colorBgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = holder.getAdapterPosition();
                //Settings.SetIdToDelete(holder.getAdapterPosition());
                Settings.setSelectedColor(listdata.get(holder.getAdapterPosition()).colorValue);
                notifyDataSetChanged();
            }
        });



    }




    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public View  colorBgView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.level);
            this.colorBgView = itemView.findViewById(R.id.color_view);
        }
    }
}
