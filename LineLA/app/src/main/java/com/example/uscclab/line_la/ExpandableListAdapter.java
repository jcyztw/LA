package com.example.uscclab.line_la;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Yolo on 2018/6/6.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listDataHeader;
    private HashMap<String,ArrayList<RoomInfo>> listHashMap;


    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, ArrayList<RoomInfo>> listHashMap) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listHashMap.get(listDataHeader.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return listDataHeader.get(i);
    }

    @Override
    public Object getChild(int i, int j) {
        return listHashMap.get(listDataHeader.get(i)).get(j); // i =Group Item  j = Child Item
    }


    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int j) {
        return j;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String headerTitle = (String)getGroup(i);
        if (view==null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.list_section,null);
        }
        TextView listHeader = (TextView)view.findViewById(R.id.txvHeader);
        //listHeader.setTypeface(null, Typeface.BOLD);
        listHeader.setText(headerTitle);
        return view;
    }

    @Override
    public View getChildView(int i, int j, boolean b, View view, ViewGroup viewGroup) {
        final  RoomInfo roomInfo =(RoomInfo)getChild(i,j);

        if(view==null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.list_item,null);
        }
        TextView txtlistChild = (TextView)view.findViewById(R.id.txvItem);
        txtlistChild.setText(roomInfo.getName());
        ImageView imageChild = (ImageView)view.findViewById(R.id.imvItem);

        LinearLayout lyItem = view.findViewById(R.id.lyItem);


        DisplayMetrics dm = new DisplayMetrics();

        circleImageView(imageChild, roomInfo.getIcon());
        // imageChild.setImageBitmap(roomInfo.getIcon());
        return view;

    }

    @Override
    public boolean isChildSelectable(int i, int j) {
        return true;
    }

    public void circleImageView(ImageView imageView, Bitmap srcBitmap){

        Resources mResources = context.getResources();

        // 將圖片切圓角
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(mResources, srcBitmap);
        roundedBitmapDrawable.setCircular(true);

        // 將轉好的圖貼在imageView中
        imageView.setImageDrawable(roundedBitmapDrawable);

    }
}
