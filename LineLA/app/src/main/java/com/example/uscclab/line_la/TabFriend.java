package com.example.uscclab.line_la;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Yolo on 2018/6/3.
 */

public class TabFriend extends Fragment {


    ImageView imvAvatar;
    TextView txvName;


    // =====for expandablist=====
    private ExpandableListView expLsvPeople;
    private ExpandableListAdapter listAdapter;      //? the content of expandableList
    private List<String> listDataHeader;            //? the title of expandableList
    private HashMap<String,ArrayList<RoomInfo>> listHash;

    // list content
    private ArrayList<RoomInfo> groupTA;
    private ArrayList<RoomInfo> groupTB;
    private ArrayList<RoomInfo> groupTC;
    private ArrayList<RoomInfo> friend;
    private String userID;

    //=/ =====for expandablist=====

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_friend, container, false);

        imvAvatar = rootView.findViewById(R.id.imvAvatarMain);
        txvName = rootView.findViewById(R.id.txvNameMain);


        // =====for expandablist=====
        initData();
        expLsvPeople = rootView.findViewById(R.id.expanListviewPeople);
        listAdapter = new ExpandableListAdapter(getActivity(),listDataHeader,listHash);


        expLsvPeople.setAdapter(listAdapter);
        expLsvPeople.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                RoomInfo selectedItem = (RoomInfo)listAdapter.getChild(groupPosition,childPosition);

                String chatName = selectedItem.getName();

                Intent goChatRoom = new Intent( getActivity(), ChatroomActivity.class );
                //goChatRoom.putExtra("id",userID);
                //goChatRoom.putExtra("friend_id",friendID);

//                Log.i("#####", selectedItem.getchatRoomID());
                goChatRoom.putExtra("chatRoomID", selectedItem.getchatRoomID());
                goChatRoom.putExtra("isGroup", selectedItem.getIsGroup());
                goChatRoom.putExtra("chatRoomName", selectedItem.getName());
                startActivity(goChatRoom);

                return false;
            }
        });

        //=/ =====for expandablist=====

        getProfile();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Log.e("DEBUG", "onResume of LoginFragment");

        clearListConten();
        getRelation();
    }

    private void clearListConten(){
        groupTA.clear();
        groupTB.clear();
        groupTC.clear();
        friend.clear();
    }
    // =====for expandablist=====
    private void initData(){
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();
        listDataHeader.add("群組 : type A");
        listDataHeader.add("群組 : type B");
        listDataHeader.add("群組 : type C");
        listDataHeader.add("好友");

        groupTA = new ArrayList<>();
        groupTB = new ArrayList<>();
        groupTC = new ArrayList<>();
        friend = new ArrayList<>();

        listHash.put(listDataHeader.get(0),groupTA);
        listHash.put(listDataHeader.get(1),groupTB);
        listHash.put(listDataHeader.get(2),groupTC);
        listHash.put(listDataHeader.get(3),friend);
    }
    //=/ =====for expandablist=====

    private void getProfile(){

        class GetData extends AsyncTask<String,Void,ItemData> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show( getActivity(), "Gain Data", "Please wait...", true, true);
            }
            @Override
            protected void onPostExecute(ItemData profile) {
                super.onPostExecute(profile);

                loading.dismiss();

                circleImageView(imvAvatar, profile.avatar);
                txvName.setText( profile.memberName);
            }

            @Override
            protected ItemData doInBackground(String...params) {
                String addr_profile = "http://140.116.82.39/communicate/GetUserProfile.php?memberID=" + params[0];

                String jsonStrProfile = null;

                ItemData profile = new ItemData();

                URL url;
                InputStream inputStream;
                BufferedReader bufferedReader;
                StringBuilder builder;

                String[] result;
                result = new String[2];
                String line = null;


                try {

                    // ======= get userProfile ========
                    url = new URL(addr_profile);
                    inputStream = url.openConnection().getInputStream();

                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"utf8"));
                    builder = new StringBuilder();

                    while((line = bufferedReader.readLine()) != null) {
                        builder.append(line + "\n");
                    }
                    inputStream.close();
                    jsonStrProfile = builder.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // convert data
                try {
                    JSONObject jsonobj = new JSONObject(jsonStrProfile);
                    //JSONArray jsonArray = new JSONArray(input);
                    result[0] = jsonobj.getString("name");
                    result[1] = jsonobj.getString("avatar");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                profile.memberName = result[0];

                byte [] byteAvatar = Base64.decode(result[1], Base64.DEFAULT);

                profile.avatar = BitmapFactory.decodeByteArray(byteAvatar, 0, byteAvatar.length);
                
                return profile;
            }
        }

        String memberID = getActivity().getIntent().getStringExtra("memberID");
        GetData getdata = new GetData();
        getdata.execute(memberID);
    }

    public void getRelation(){

        class GetData extends AsyncTask<String,Void, Void> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show( getActivity(), "Gain Data", "Please wait...", true, true);
            }
            @Override
            protected void onPostExecute(Void tmp) {
                super.onPostExecute(tmp);
                loading.dismiss();

                listHash.put(listDataHeader.get(0), groupTA);
                listHash.put(listDataHeader.get(1), groupTB);
                listHash.put(listDataHeader.get(2), groupTC);
                listHash.put(listDataHeader.get(3), friend);

                updateExpandableList();
            }
            @Override
            protected Void doInBackground(String...params) {

                String addr_relation = "http://140.116.82.39/communicate/GetRelationData.php?memberID=" + params[0];

                String jsonStrRelation = null;
                String line = null;
                String section = new String();

                URL url;
                InputStream inputStream;
                BufferedReader bufferedReader;
                StringBuilder builder;

                // get Data From server
                try {
                    url = new URL(addr_relation);
                    inputStream = url.openConnection().getInputStream();

                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf8"));
                    builder = new StringBuilder();

                    while ((line = bufferedReader.readLine()) != null) {
                        builder.append(line + "\n");
                    }
                    inputStream.close();
                    jsonStrRelation = builder.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // 1. convert data 2. set list data
                try {
                    JSONArray jsonArray = new JSONArray(jsonStrRelation);
                    for (int i = 0; i < jsonArray.length(); ++i) {

                        JSONObject jsonData = jsonArray.getJSONObject(i);

                        byte[] byteAvatar = Base64.decode(jsonData.getString("avatar"), Base64.DEFAULT);

                        RoomInfo item = new RoomInfo();

                        item.setIcon(BitmapFactory.decodeByteArray(byteAvatar, 0, byteAvatar.length));
                        item.setName(jsonData.getString("name"));
                        item.setchatRoomID( jsonData.getString("chatRoomID") );


                        //Log.i("####chatRoomID", item.getchatRoomID());

                        section = jsonData.getString("section");

                        switch (section) {
                            case "0":
                                item.setIsGroup(true);
                                groupTA.add(item);
                                break;
                            case "1":
                                item.setIsGroup(true);
                                groupTB.add(item);
                                break;
                            case "2":
                                item.setIsGroup(true);
                                groupTC.add(item);
                                break;
                            case "3":
                                item.setIsGroup(false);
                                friend.add(item);
                                break;
                            default:
                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
        String memberID = getActivity().getIntent().getStringExtra("memberID");
        GetData getdata = new GetData();
        getdata.execute(memberID);
    }

    public void circleImageView(ImageView imageView, Bitmap srcBitmap){

        Resources mResources = getResources();

        // 將圖片切圓角
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(mResources, srcBitmap);
        roundedBitmapDrawable.setCircular(true);

        // 將轉好的圖貼在imageView中
        imageView.setImageDrawable(roundedBitmapDrawable);
    }

    private void updateExpandableList(){
        for(int i=0; i<4; i++){
            expLsvPeople.collapseGroup(i);
            expLsvPeople.expandGroup(i);
        }
    }
    class ItemData{
        Bitmap avatar;
        String memberName;
    }

}
