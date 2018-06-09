package com.example.uscclab.line_la;

/**
 * Created by uscclab on 2018/6/4.
 */

public class Bubble {
    private String txtmsg;
    private String name;
    private int type;       // indicate the left of right
    public  Bubble() {}

    public Bubble(int type, String textmsg){
        this.txtmsg = textmsg;
        this.type = type;
    }

    public Bubble(int type, String textmsg, String name){
        this.txtmsg = textmsg;
        this.type = type;
        this.name = name;
    }

    public String getTxtmsg(){ return  txtmsg; }

    public String getName(){ return  name; }

    public  void setTxtmsg(String txtmsg) { this.txtmsg = txtmsg; }

    public int getType() { return type; }

    public void setType(int type) { this.type = type; }
}
