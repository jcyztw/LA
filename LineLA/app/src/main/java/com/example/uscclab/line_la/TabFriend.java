package com.example.uscclab.line_la;

import android.app.ProgressDialog;
import android.content.Intent;
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
                int group_class = groupPosition;
//                if(group_class == 1){
//                    //Log.d("Tag","好友click");
//                    RoomInfo tmp = (RoomInfo)listAdapter.getChild(groupPosition,childPosition);
//                    String friendID = tmp.getStudentID();
//                    String chatName = tmp.getName();
//
//                    Intent chat = new Intent(Main.this,Chatroom.class);
//                    chat.putExtra("id",userID);
//                    chat.putExtra("friend_id",friendID);
//                    chat.putExtra("chatName", chatName);
//                    startActivity(chat);
//
//                } else if(group_class == 0) {
//                    groupPosition=1;
//                    RoomInfo tmp = (RoomInfo)listAdapter.getChild(groupPosition,childPosition);
//                    String chatName = "aaa";
//                    String roomID = tmp.getStudentID();
//
//                    Intent chat = new Intent(Main.this,Chatroom.class);
//                    chat.putExtra("id",userID);
//                    chat.putExtra("friend_id",roomID);
//                    chat.putExtra("chatName", chatName);
//                    startActivity(chat);
//                }
                return false;
            }
        });

        //=/ =====for expandablist=====

        getProfile();
        getRelation();



        return rootView;
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

                DisplayMetrics dm = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                imvAvatar.setImageBitmap( getRoundedCornerBitmap(
                        profile.avatar,dm.widthPixels/2.0f,dm));
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

                byte [] byteAvatar = Base64.decode(result[1],Base64.DEFAULT);

                profile.avatar = BitmapFactory.decodeByteArray(byteAvatar, 0, byteAvatar.length);
                
                return profile;
            }
        }

        String memberID = getActivity().getIntent().getStringExtra("memberID");
        GetData getdata = new GetData();
        getdata.execute(memberID);
    }

    private void getRelation(){

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

                listHash.put(listDataHeader.get(0),groupTA);
                listHash.put(listDataHeader.get(1),groupTB);
                listHash.put(listDataHeader.get(2),groupTC);

                updateExpandableList();

            }

            @Override
            protected Void doInBackground(String...params) {

                String addr_relation = "http://140.116.82.39/communicate/GetRelationData.php?memberID=" + params[0];

                String jsonStrRelation = null;
                String line = null;
                String type = new String();


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
                        type = jsonData.getString("type");

                        switch (type) {
                            case "0":
                                groupTA.add(item);
                                break;
                            case "1":
                                groupTB.add(item);
                                break;
                            case "2":
                                groupTC.add(item);
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
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx,DisplayMetrics dm)
    {
        final int size = dm.widthPixels/4*3;

        //縮小圖片
        int mRadius = Math.min(dm.widthPixels, dm.heightPixels)/2;
        float mScale = (mRadius * 2.0f) / Math.min(bitmap.getHeight(), bitmap.getWidth());
        Matrix matrix = new Matrix();
        matrix.postScale(mScale,mScale); //長寬比例

        //取得縮小圖片
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap,0,
                0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);

        //新增畫布
        Bitmap output = Bitmap.createBitmap(size,size,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();

        // 在畫布上繪製圓角後的矩形(圓形)
        Rect rect = new Rect(0, 0, size,size);
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        //去掉圓角後矩形之外多餘的像素並繪製
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap1, 0, 0, paint);
        return output;
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
