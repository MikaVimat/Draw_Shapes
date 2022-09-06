package com.samsung.drawshapes.ListAndRecyclerViews;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.samsung.drawshapes.Drawing.DrawView;
import com.samsung.drawshapes.Drawing.ThreadForDrawing;
import com.samsung.drawshapes.R;
import com.samsung.drawshapes.Logic.ShapeType;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private Activity _activity ;
    private static int selectedNameId;
    private static String mSelectedName;
    private ArrayList<String> _shpaes;
    private int selectedItemPosition;

    public ListViewAdapter(Activity mainActivity, ArrayList<String> shapes){
        this._shpaes = shapes;
        _activity = mainActivity;
    }

    @Override
    public int getCount() {
        return _shpaes.size();
    }

    @Override
    public Object getItem(int i) {
        return _shpaes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(_activity);
            view = layoutInflater.inflate(R.layout.shapelist, viewGroup, false);
        }

        TextView rowText = ((TextView) view.findViewById(R.id.shape_text));
        LinearLayout rowLayout = ((LinearLayout) view.findViewById(R.id.layout_id));
        rowText.setText(_shpaes.get(i));

        if(i == selectedItemPosition){
            rowLayout.setBackgroundColor(Color.GRAY);
        }
        else{
            rowLayout.setBackgroundColor(Color.WHITE);

        }



        rowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedItemPosition = i;
                notifyDataSetChanged();
                mSelectedName = rowText.getText()+"";
                ShapeType shapetype =ShapeType.RECTANGLE;
                System.out.println(mSelectedName);
                switch (mSelectedName){
                    case "Circle":
                        shapetype =  ShapeType.CIRCLE;
                        break;
                    case "Line":
                        shapetype = ShapeType.LINE;
                        break;
                    case "Rectangle":
                        shapetype = ShapeType.RECTANGLE;
                        break;


                }
                DrawView.isMouseDown = false;
                ThreadForDrawing.setShapeType(shapetype);
                //getting all layouts and resetting color to left only one selected layout in the next step.


            }
        });



        return view;
    }



    public static int getClickedUserId(){
        return selectedNameId;
    }
    public static String  getClickedUserName(){
        return mSelectedName;
    }
}
