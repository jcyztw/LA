package com.example.uscclab.line_la;

import android.graphics.Bitmap;

/**
 * Created by uscclab on 2018/6/9.
 */

public class ChatItem {
    private String lastmsg;
    private String name;
    private String lasttime;
    private boolean isGroup;
    private Bitmap avata;
    
    public  ChatItem() {}

    public ChatItem(boolean isGroup, String lastmsg){
        this.lastmsg = lastmsg;
        this.isGroup = isGroup;
    }

    public ChatItem(boolean isGroup, String lastmsg, String name){
        this.lastmsg = lastmsg;
        this.name = name;
        this.isGroup = isGroup;
    }

    public ChatItem(boolean isGroup, String lastmsg, String name, String lasttime){
        this.lastmsg = lastmsg;
        this.name = name;
        this.isGroup = isGroup;
        this.lasttime = lasttime;
    }

    public ChatItem(boolean isGroup, String lastmsg, String name, String lasttime,Bitmap avata){
        this.lastmsg = lastmsg;
        this.name = name;
        this.isGroup = isGroup;
        this.lasttime = lasttime;
        this.avata = avata;
    }

    public  void setLastmsg(String lastmsg) { this.lastmsg = lastmsg; }

    public String getLastmsg() { return  lastmsg; }

    public void setName(String Name) { this.name = name; }

    public String getName() { return  name; }

    public  void setLasttimel(String lasttime) { this.lasttime = lasttime; }

    public String getLasttime() { return  lasttime; }

    public void setisGroup(boolean isGroup) { this.isGroup = isGroup; }

    public boolean getisGroup() { return isGroup; }

    public void setAvata(Bitmap avata) { this.avata = avata; }

    public Bitmap getAvata() { return avata; }

}
