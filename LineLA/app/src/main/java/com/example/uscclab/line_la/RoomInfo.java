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
    private String StudentID;
    private Bitmap icon;
    private ArrayList<String> mamberList = null;

    public RoomInfo() {}

    public RoomInfo(String roomName) {
        this.roomName = roomName;
    }

    public RoomInfo(String Name, String StudentID) {
        this.Name = Name;
        this.StudentID = StudentID;
    }

    public RoomInfo(String roomName, String name, String studentID, Bitmap icon, ArrayList<String> mamberList) {
        this.roomName = roomName;
        Name = name;
        StudentID = studentID;
        this.icon = icon;
        this.mamberList = mamberList;
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

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
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