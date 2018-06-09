package com.example.uscclab.line_la;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Yolo on 2018/6/6.
 */

public class RoomInfo implements Serializable {

    private String roomName;
    private String Name;
    private String chatRoomID;
    private Bitmap icon;
    private ArrayList<String> mamberList = null;
    private Boolean isGroup;

    public RoomInfo() {}

    public RoomInfo(String roomName) {
        this.roomName = roomName;
    }

    public RoomInfo(String Name, String chatRoomID) {
        this.Name = Name;
        this.chatRoomID = chatRoomID;
    }

    public RoomInfo(String roomName, String name, String chatRoomID, Bitmap icon, ArrayList<String> mamberList) {
        this.roomName = roomName;
        Name = name;
        chatRoomID = chatRoomID;
        this.icon = icon;
        this.mamberList = mamberList;
    }

    public boolean getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }
    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getchatRoomID() {
        return chatRoomID;
    }

    public void setchatRoomID(String chatRoomID) {
        this.chatRoomID = chatRoomID;
    }

    public Bitmap getIcon() {
        return icon;
    }


    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public ArrayList<String> getMamberList() {
        return mamberList;
    }

    public void setMamberList(ArrayList<String> mamberList) {
        this.mamberList = mamberList;
    }
}
