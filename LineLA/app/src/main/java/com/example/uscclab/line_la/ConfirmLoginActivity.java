package com.example.uscclab.line_la;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class ConfirmLoginActivity extends AppCompatActivity {

    private TextView tv_memberID;
    private ImageView Img1;
    private TextView tv_name;
    private Boolean isExist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_login);

        Intent intentFromLogin = getIntent();
        final String memberID = intentFromLogin.getStringExtra("memberID");

        tv_memberID = (TextView) findViewById(R.id.tv_memberID);
        tv_name = (TextView) findViewById(R.id.tv_name);


        tv_memberID.setText("學員卡號 : " + memberID);

        Img1 = (ImageView) findViewById(R.id.img_profile);
        getProfile();

        // 取得螢幕解析度
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);

//        Img1.setImageBitmap(getRoundedCornerBitmap(
//                BitmapFactory.decodeResource(
//                        getResources(), R.drawable.welcome),dm.widthPixels/2.0f,dm));

        Button btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goMain = new Intent(ConfirmLoginActivity.this, MainActivity.class);
                goMain.putExtra("memberID", memberID);
                startActivity(goMain);
                finish();
            }
        });

        Button btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goLogin = new Intent(ConfirmLoginActivity.this, LoginActivity.class);
                startActivity(goLogin);
                finish();
            }
        });
    }

    private void getProfile(){

        class GetData extends AsyncTask<String,Void,ItemData> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show( ConfirmLoginActivity.this, "Gain Data", "Please wait...", true, true);
            }
            @Override
            protected void onPostExecute(ItemData profile) {
                super.onPostExecute(profile);

                loading.dismiss();

                if(!isExist){
                    //Looper.prepare();
                    Toast.makeText(ConfirmLoginActivity.this,"查無此人，請重新掃描", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ConfirmLoginActivity.this, LoginActivity.class));
                    finish();
                   //Looper.loop();
                }else{
                    circleImageView(Img1,profile.avatar);
                    tv_name.setText("姓名 : "+profile.memberName);
                }
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
                if(!jsonStrProfile.equals("0\n")){
                    isExist = true;
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
                }
                return profile;
            }
        }

        isExist = false;
        String memberID = ConfirmLoginActivity.this.getIntent().getStringExtra("memberID");
        GetData getdata = new GetData();
        getdata.execute(memberID);
    }

    class ItemData{
        Bitmap avatar;
        String memberName;
    }

    public void circleImageView(ImageView imageView, Bitmap srcBitmap){

        Resources mResources = this.getResources();

        // 將圖片切圓角
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(mResources, srcBitmap);
        roundedBitmapDrawable.setCircular(true);

        // 將轉好的圖貼在imageView中
        imageView.setImageDrawable(roundedBitmapDrawable);

    }
}
