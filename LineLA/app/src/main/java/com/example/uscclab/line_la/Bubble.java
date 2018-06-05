package com.example.uscclab.line_la;

/**
 * Created by uscclab on 2018/6/4.
 */

public class Bubble {
    private String txtmsg;
    private int type;
    public  Bubble() {}

    public Bubble(int type, String textmsg){
        this.txtmsg = textmsg;
        this.type = type;
    }

    public String getTxtmsg(){
        return  txtmsg;
    }

    public  void setTxtmsg(String txtmsg) { this.txtmsg = txtmsg; }

    public int getType() { return type; }

    public void setType(int type) { this.type = type; }
}
