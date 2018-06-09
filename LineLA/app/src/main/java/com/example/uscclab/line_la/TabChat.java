package com.example.uscclab.line_la;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Yolo on 2018/6/3.
 */

public class TabChat extends Fragment implements AdapterView.OnItemClickListener{

    private ListView lsv;
    private ArrayList<ChatItem> chatItems = new ArrayList<ChatItem>();
    private ChatItemList chatItemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_chat, container, false);
        Context context = rootView.getContext();
        lsv = (ListView) rootView.findViewById(R.id.lsv);
        lsv.setOnItemClickListener(this);
        chatItemList = new ChatItemList(context);
//        Bitmap b =  BitmapFactory.decodeResource(context.getResources(),R.drawable.bg_login);
        chatItems.add(new ChatItem(false, "馬匪 任何時候都要剿", "湯師爺", "22:13", BitmapFactory.decodeResource(context.getResources(),R.drawable.bg_login)));
        chatItemList.setchatList(chatItems);
        lsv.setAdapter(chatItemList);
        lsv.setSelection(chatItemList.getCount());
        return rootView;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        
    }
}
