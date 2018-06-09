package com.example.uscclab.line_la;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class AddFriend extends AppCompatActivity {


    private ImageView imvAvatar;
    private TextView txvName;
    private Button btn;
    RoomInfo friendItem ;
    private Bitmap avatar;
    private String name;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        imvAvatar = findViewById(R.id.imvAvatarFri);
        txvName = findViewById(R.id.txvNameFri);
        btn = findViewById(R.id.btnFri);



        getProfile_isfriend();
    }

    private void getProfile_isfriend(){
        String memberID_fri, memberID_me;

        memberID_fri = getIntent().getStringExtra("memberID_fri");
        memberID_me = getIntent().getStringExtra("memberID_me");

        GetData getdata = new GetData();
        getdata.execute(memberID_me, memberID_fri);
    }

    class GetData extends AsyncTask<String,Void, Boolean> {

        ProgressDialog loading;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show( AddFriend.this, "Gain Data"
                    , "Please wait...", true, true);
        }
        @Override
        protected void onPostExecute(Boolean beenFriend) {
            super.onPostExecute(beenFriend);
            loading.dismiss();

            // set avatar, Name
            circleImageView(imvAvatar, avatar);
            txvName.setText(name);

            if(beenFriend){
                Toast.makeText(AddFriend.this, "你們已是好友", Toast.LENGTH_LONG).show();
                btn.setVisibility(View.INVISIBLE);

                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);
            }else{
                btn.setVisibility(View.VISIBLE);
            }
        }
        @Override
        protected Boolean doInBackground(String...params) {

            String jsonStr = null;
            String line = null;
            Boolean beenFriend = false;

            URL url;
            InputStream inputStream;
            BufferedReader bufferedReader;
            StringBuilder builder;


            String addr =
                    "http://140.116.82.39/communicate/isFriendAGetProfile.php?memberID_me="
                            + params[0] + "&memberID_fri=" + params[1];

            // get Data From server
            try {
                url = new URL(addr);
                inputStream = url.openConnection().getInputStream();

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf8"));
                builder = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                inputStream.close();
                jsonStr = builder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // convert data
            try {
                JSONObject jsonData = new JSONObject(jsonStr);
                byte[] byteAvatar = Base64.decode(jsonData.getString("avatar"), Base64.DEFAULT);

                name = jsonData.getString("name");
                avatar = BitmapFactory.decodeByteArray( byteAvatar, 0
                        , byteAvatar.length );

                beenFriend = jsonData.getBoolean("beenFriend");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return beenFriend;
        }
    }

    public void onBeFriend(View v){
        String memberID_fri, memberID_me;

        memberID_fri = getIntent().getStringExtra("memberID_fri");
        memberID_me = getIntent().getStringExtra("memberID_me");

        BeFriend beFriend = new BeFriend();
        beFriend.execute(memberID_me, memberID_fri);
    }

    class BeFriend extends AsyncTask<String,Void, String> {

        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show( AddFriend.this, "Gain Data"
                    , "Please wait...", true, true);
        }
        @Override
        protected void onPostExecute(String resultMsg) {
            super.onPostExecute(resultMsg);
            loading.dismiss();

            Toast.makeText(AddFriend.this,resultMsg, Toast.LENGTH_LONG).show();

            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 500);
        }
        @Override
        protected String doInBackground(String...params) {

            String jsonStr = null;
            String line = null;
            String resultMsg = null;
            Boolean beenFriend = false;

            URL url;
            InputStream inputStream;
            BufferedReader bufferedReader;
            StringBuilder builder;

            String addr =
                    "http://140.116.82.39/communicate/InsertFriendRelation.php?memberID_me="
                            + params[0] + "&memberID_fri=" + params[1];

            // get Data From server
            try {
                url = new URL(addr);
                inputStream = url.openConnection().getInputStream();

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf8"));
                builder = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                inputStream.close();
                resultMsg = builder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return resultMsg;
        }
    }


    public void circleImageView(ImageView imageView, Bitmap srcBitmap){

        Resources mResources = getResources();

        // 將圖片切圓角
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(mResources, srcBitmap);
        roundedBitmapDrawable.setCircular(true);

        // 將轉好的圖貼在imageView中
        imageView.setImageDrawable(roundedBitmapDrawable);

    }

}
