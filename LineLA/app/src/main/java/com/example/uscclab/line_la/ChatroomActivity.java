package com.example.uscclab.line_la;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.util.ArrayList;

public class ChatroomActivity extends AppCompatActivity {

    static private String MQTTHOST = "tcp://140.116.82.34:1883";
    static private String USERNAME = "LineLA";
    static private String PASSWORD = "LineLA";
    private String topic;       // groupID or friendRelationID
    private MqttAndroidClient client;

    private int type;
    private boolean isGroup;
    private LinearLayout ll_chatroom;
    private TextView tv_chatroomname;
    private ArrayList<Bubble> bubbles = new ArrayList<Bubble>(); //? items
    private BubbleList bubblelist;          //? adapter
    private ListView lv_chat;
    private EditText editMsg;
    private ImageButton btn_send;
    private String chatRoomName;  // chatroom name
    private String oppositeName = "QAQ";

    @Override
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // hide keyboard when enter into the page
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_chatroom);

        getWindow().setBackgroundDrawableResource(R.drawable.bg_chatroom);


        // set topic
        topic = getIntent().getStringExtra("chatRoomID");

        isGroup = getIntent().getBooleanExtra("isGroup", false);

        // bubblelist
        bubblelist = new BubbleList(ChatroomActivity.this);
        bubblelist.setIsGroup(isGroup);

        //The chatroomname is friendname or groupname.
        Intent intentFromLogin = getIntent();
        chatRoomName = intentFromLogin.getStringExtra("chatRoomName");
        tv_chatroomname = (TextView) findViewById(R.id.tv_chatroomname);
        tv_chatroomname.setText(chatRoomName);


        // ListViewChat
        lv_chat = (ListView) findViewById(R.id.lv_chat);

        // editMsg
        editMsg = (EditText) findViewById(R.id.et_msg);
        editMsg.setMovementMethod( ScrollingMovementMethod.getInstance() );

        // btn_send send message.
        btn_send = (ImageButton) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //test
                String str = editMsg.getText().toString();
                editMsg.setText("", TextView.BufferType.EDITABLE);  // clear text in editText
                if(!str.isEmpty()){
                    type = 1;
                    pub(str);
                }
                else{
                    Toast.makeText(ChatroomActivity.this, "請輸入訊息！"
                                    , Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Create new MQTT connection and Subscribe.
        Connect();

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            // get Msg from server
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String msg = new String(message.getPayload());
                bubbles.add(new Bubble(type, msg, oppositeName));
                bubblelist.setFriendList(bubbles);
                lv_chat.setAdapter(bubblelist);
                lv_chat.setSelection(bubblelist.getCount());
                type = 0;
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    // MQTT Connect.
    public void Connect(){

        // connection setting
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), MQTTHOST, clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());

        // connet to server
        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(ChatroomActivity.this,"連線成功!",Toast.LENGTH_SHORT).show();
                    Subscribe();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(ChatroomActivity.this,"連線失敗!",Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void Subscribe() {
        int qos = 0;
        try {
            IMqttToken subToken = client.subscribe(topic,0);
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
        try {
            client.publish(topic, msg.getBytes(), 0, false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
