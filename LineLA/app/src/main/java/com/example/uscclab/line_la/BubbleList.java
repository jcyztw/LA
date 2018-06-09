package com.example.uscclab.line_la;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by uscclab on 2018/6/4.
 */

public class BubbleList extends BaseAdapter{
    private Context context;
    private ArrayList<Bubble> friendList;
    private static LayoutInflater inflater = null;
    private boolean IsGroup = false;
//    private LinearLayout bubble_char_left_name;
    private TextView tv_chat_name;
    public  BubbleList(Context context){
        this.context = context;
        inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return friendList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return friendList.get(position);
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

    public void setFriendList(ArrayList<Bubble> friendList) {
        // TODO Auto-generated method stub
        this.friendList = friendList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Bubble Bubble = (Bubble)getItem(position);

        View rowView = null;
        TextView txt_msg = null;
        int type = Bubble.getType();

        // Opposite
        if(type == 0){
            rowView = inflater.inflate(R.layout.bubble_chat_left, null);
            // chat with friend
            if(!getIsGroup()){
                tv_chat_name = (TextView) rowView.findViewById(R.id.tv_chat_name);
                tv_chat_name.setVisibility(View.GONE);
            }
            // chat in group
            else{
                tv_chat_name = (TextView) rowView.findViewById(R.id.tv_chat_name);
                tv_chat_name.setText(Bubble.getName());
            }
        }
        // me
        else{
            rowView = inflater.inflate(R.layout.bubble_chat_right, null);
        }


        txt_msg = (TextView) rowView.findViewById(R.id.txt_msg);
        txt_msg.setText(Bubble.getTxtmsg());

        return rowView;
    }
}
