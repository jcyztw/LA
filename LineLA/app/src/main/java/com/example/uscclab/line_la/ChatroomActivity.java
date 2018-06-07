package com.example.uscclab.line_la;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatroomActivity extends AppCompatActivity {

    static private String MQTTHOST = "tcp://140.116.82.34:1883";
    static private String USERNAME = "LineLA";
    static private String PASSWORD = "LineLA";
    private String topicStr = "test";
    private MqttAndroidClient client;

    private int type;
    private TextView tv_chatroomname;
//    private Toolbar toolbarTitle;
    private ArrayList<Bubble> bubble = new ArrayList<Bubble>();
    private BubbleList bubblelist;
    private ListView lv_chat;
    private EditText et_msg;
    private ImageButton btn_send;
    private static ExecutorService FULL_TASK_EXECUTOR;
    static {FULL_TASK_EXECUTOR = (ExecutorService) Executors.newCachedThreadPool();}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // By Intent and the label is chatroomname. The chatroomname is friendname or groupname.
//        Intent intentFromLogin = getIntent();
//        String chatroomname = intentFromLogin.getStringExtra("chatroomname");
        tv_chatroomname = (TextView) findViewById(R.id.tv_chatroomname);
        tv_chatroomname.setText("湯師爺"); // (chatroomname);

        // ListViewChat
        lv_chat = (ListView) findViewById(R.id.lv_chat);

        //test chat ex. in
        bubble.add(new Bubble(1,"我出錢 你剿匪！"));
        bubblelist = new BubbleList(ChatroomActivity.this,bubble);
        lv_chat.setAdapter(bubblelist);
        lv_chat.setSelection(bubblelist.getCount());
        bubble.add(new Bubble(0,"馬匪 任何時候都要剿！"));
        bubblelist = new BubbleList(ChatroomActivity.this,bubble);
        lv_chat.setAdapter(bubblelist);
        lv_chat.setSelection(bubblelist.getCount());

        // et_msg
        et_msg = (EditText) findViewById(R.id.et_msg);

        // btn_send send message.
        btn_send = (ImageButton) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //test
                String str = et_msg.getText().toString();
                et_msg.setText("", TextView.BufferType.EDITABLE);
                if(!str.isEmpty()){
                    type = 1;
                    pub(str);
                }
                else{
                Toast.makeText(ChatroomActivity.this, "請輸入訊息！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Create new MQTT connection and Subscribe.
        Connect();

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String msg = new String(message.getPayload());
                bubble.add(new Bubble(type, msg));
                bubblelist = new BubbleList(ChatroomActivity.this, bubble);
                lv_chat.setAdapter(bubblelist);
                lv_chat.setSelection(bubblelist.getCount());
                type = 0;
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

    }

    public void Connect(){
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), MQTTHOST, clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(ChatroomActivity.this,"連線成功!",Toast.LENGTH_LONG).show();
                    Subscribe();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(ChatroomActivity.this,"您連線尚未連線!",Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void Subscribe() {
        int qos = 0;
        try {
            IMqttToken subToken = client.subscribe("test",0);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // The message was published
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // pub msg to mqtt server
    public void pub(String msg){
        String topic = topicStr;
        try {
            client.publish(topic, msg.getBytes(), 0, false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
