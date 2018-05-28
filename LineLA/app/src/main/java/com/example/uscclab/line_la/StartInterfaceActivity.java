package com.example.uscclab.line_la;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StartInterfaceActivity extends AppCompatActivity {

//    public static boolean LoginOrNot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_interface);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loginIntent = new Intent(StartInterfaceActivity.this, LoginActivity.class);
                StartInterfaceActivity.this.startActivity(loginIntent);
                StartInterfaceActivity.this.finish();
            }
        }, 1500);
    }
}
