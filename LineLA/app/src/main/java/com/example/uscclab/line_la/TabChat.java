package com.example.uscclab.line_la;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Yolo on 2018/6/3.
 */

public class TabChat extends Fragment implements AdapterView.OnItemClickListener{

    private ListView lsv;
    private ArrayList<ChatItem> chatItems = new ArrayList<ChatItem>();
    private ChatItemList chatItemList;
    private ExpandableListView expLsvPeople;
    private TextView friend_txvName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_chat, container, false);
        friend_txvName = getActivity().findViewById(R.id.txvNameMain);
        expLsvPeople = getActivity().findViewById(R.id.expanListviewPeople);
        Context context = rootView.getContext();
        lsv = (ListView) rootView.findViewById(R.id.lsv);
        lsv.setOnItemClickListener(this);
        chatItemList = new ChatItemList(context);
        chatItems.add(new ChatItem(true, "", "交通組", "", BitmapFactory.decodeResource(context.getResources(),R.drawable.travel)));
        chatItemList.setchatList(chatItems);
        lsv.setAdapter(chatItemList);
        lsv.setSelection(chatItemList.getCount());
        return rootView;
    }

    public interface titleSelectInterface{
        public void onTitleSelect(String title);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        String chatName = ((ChatItem)chatItemList.getItem(i)).getName();
        Intent goChatRoom = new Intent( getActivity(), ChatroomActivity.class );
        RoomInfo selectedItem = findSelectedItem(i);

        if(!selectedItem.getIsGroup()){
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            selectedItem.getIcon().compress(Bitmap.CompressFormat.JPEG,100,bs);
            goChatRoom.putExtra("avatar", bs.toByteArray());
        }

        goChatRoom.putExtra("userName", friend_txvName.getText().toString());
        goChatRoom.putExtra("chatRoomID", selectedItem.getchatRoomID());
        goChatRoom.putExtra("isGroup", selectedItem.getIsGroup());
        goChatRoom.putExtra("chatRoomName", selectedItem.getName());
        startActivity(goChatRoom);
    }

    private RoomInfo findSelectedItem(int i) {
        String chatName = ((ChatItem)chatItemList.getItem(i)).getName();

        ExpandableListAdapter listAdapter = (ExpandableListAdapter) expLsvPeople.getExpandableListAdapter();
        int GroupCount = listAdapter.getGroupCount();
        boolean st = false;
        RoomInfo selectedItem = null;
        for( int groupPosition = 0; groupPosition < GroupCount && !st; groupPosition++){
            int ChildrenCount = listAdapter.getChildrenCount(groupPosition);
            for (int childPosition = 0; childPosition < ChildrenCount && !st; childPosition++){
                if(((RoomInfo)listAdapter.getChild(groupPosition,childPosition)).getName().equals(chatName)) {
                    return (RoomInfo)listAdapter.getChild(groupPosition,childPosition);
                };
            }
        }
        return null;
    }
}
