package com.example.uscclab.line_la;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.MemoryFile;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

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
    private Bitmap avatar;
    private String groupID;
    private String userName;
    private HashMap<String, Bitmap > memberProfile;

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
        userName = getIntent().getStringExtra("userName");


        // get friend avatar
        if(!isGroup){
            byte[] b = getIntent().getByteArrayExtra("avatar");
            avatar = BitmapFactory.decodeByteArray(b, 0, b.length);

            oppositeName = chatRoomName;
        }
        // get group memeber name & avatar
        else
        {
            groupID = topic;
            getGroupMemberData();
        }


        chatRoomName = getIntent().getStringExtra("chatRoomName");


        // bubblelist
        bubblelist = new BubbleList(ChatroomActivity.this);
        bubblelist.setIsGroup(isGroup);

        //The chatroomname is friendname or groupname.
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
                    pub(userName + ":" + str);
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

                String [] msgs = msg.split(":");

                if(!isGroup){
                    bubbles.add(new Bubble(type, msgs[1], msgs[0], avatar));
                }
                else{
                    bubbles.add(new Bubble(type, msgs[1], msgs[0], memberProfile.get(msgs[0])));
                }

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


    public void getGroupMemberData(){
        memberProfile = new HashMap<>();
        GetData getdata = new GetData();
        getdata.execute(groupID);
    }
    class GetData extends AsyncTask<String,Void, Void> {

        ProgressDialog loading;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show( ChatroomActivity.this, "Gain Data", "Please wait...", true, true);
        }
        @Override
        protected void onPostExecute(Void tmp) {
            super.onPostExecute(tmp);
            loading.dismiss();
        }
        @Override
        protected Void doInBackground(String...params) {

            String addr_relation = "http://140.116.82.39/communicate/GetGroupMember.php?groupID=" + params[0];

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

            // 1. convert data
            try {
                JSONArray jsonArray = new JSONArray(jsonStrRelation);
                for (int i = 0; i < jsonArray.length(); ++i) {

                    JSONObject jsonData = jsonArray.getJSONObject(i);

                    byte[] byteAvatar = Base64.decode(jsonData.getString("avatar")
                            , Base64.DEFAULT);

                    memberProfile.put(jsonData.getString("name")
                            , BitmapFactory.decodeByteArray(byteAvatar, 0, byteAvatar.length));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
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
