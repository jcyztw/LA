package com.example.uscclab.line_la;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by uscclab on 2018/6/9.
 */

public class ChatItemList extends BaseAdapter {
    private Context context;
    private ArrayList<ChatItem> chatList;
    private static LayoutInflater inflater = null;
    private boolean IsGroup = false;
    private TextView chatroomnameChatItem;
    private TextView lastmsgChatItem;
    private TextView lasttimeChatItem;
    private ImageView imvAvatarChatItem;
    private View rowView = null;
    public  ChatItemList(Context context){
        this.context = context;
        inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return chatList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public boolean getIsGroup() {
        // TODO Auto-generated method stub
        return IsGroup;
    }

    public void setIsGroup(boolean IsGroup) {
        // TODO Auto-generated method stub
        this.IsGroup = IsGroup;
    }

    public void setchatList(ArrayList<ChatItem> chatList) {
        // TODO Auto-generated method stub
        this.chatList = chatList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ChatItem ChatItem = (ChatItem)getItem(position);

        rowView = inflater.inflate(R.layout.chat_item, null);

        imvAvatarChatItem = (ImageView) rowView.findViewById(R.id.imvAvatarChatItem);
        circleImageView(imvAvatarChatItem, ChatItem.getAvata());
        chatroomnameChatItem = (TextView) rowView.findViewById(R.id.chatroomnameChatItem);
        chatroomnameChatItem.setText(ChatItem.getName());
        lastmsgChatItem = (TextView) rowView.findViewById(R.id.lastmsgChatItem);
        lastmsgChatItem.setText(ChatItem.getLastmsg());
        lasttimeChatItem = (TextView) rowView.findViewById(R.id.lasttimeChatItem);
        lasttimeChatItem.setText(ChatItem.getLasttime());

        return rowView;
    }

    public void circleImageView(ImageView imageView, Bitmap srcBitmap){

        Resources mResources = rowView.getResources();

        // 將圖片切圓角
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(mResources, srcBitmap);
        roundedBitmapDrawable.setCircular(true);

        // 將轉好的圖貼在imageView中
        imageView.setImageDrawable(roundedBitmapDrawable);

    }
}
