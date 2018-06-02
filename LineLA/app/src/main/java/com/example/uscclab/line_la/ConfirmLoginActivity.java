package com.example.uscclab.line_la;

import android.content.Intent;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ConfirmLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_login);
        ImageView Img1 = (ImageView) findViewById(R.id.img_profile);
        // 取得螢幕解析度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Img1.setImageBitmap(getRoundedCornerBitmap(
                BitmapFactory.decodeResource(
                        getResources(), R.drawable.welcome),dm.widthPixels/2.0f,dm));

        Button btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goMain = new Intent(ConfirmLoginActivity.this, MainActivity.class);
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
}
