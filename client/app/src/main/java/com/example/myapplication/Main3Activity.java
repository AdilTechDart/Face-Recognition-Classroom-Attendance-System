package com.example.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class Main3Activity extends AppCompatActivity implements View.OnClickListener{
    private Button take_photo;
    private Button button_s;
    private Button button_set;
    private String username;
    private String studentfacetoken;
    private EditText et_studentname;
    private TextView Text_print;
    private EditText et_view;
    private Button button_v;
    public static final int UP=1;
    public static final int ADD=2;
    public static final int SET=3;
    public static final int VIEW=4;

    private Handler handler_v =new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case VIEW:
                    TextView Text_print=(TextView)findViewById(R.id.Text_print);
                    Text_print.setText(msg.getData().getString("msg_v"));
                    break;
                default:
                    break;
            }
        }
    };


    private Handler handler =new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case UP:
                    TextView Text_print=(TextView)findViewById(R.id.Text_print);
                    Text_print.setText(msg.getData().getString("msg"));
                    break;
                    default:
                        break;
            }
        }
    };
    private Handler handler_a =new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case ADD:
                    TextView Text_print=(TextView)findViewById(R.id.Text_print);
                    Text_print.setText(msg.getData().getString("msg_a"));
                    break;
                default:
                    break;
            }
        }
    };
    private Handler handler_set =new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case SET:
                    TextView Text_print=(TextView)findViewById(R.id.Text_print);
                    Text_print.setText(msg.getData().getString("msg_setID"));
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Button takephotob = (Button)findViewById(R.id.take_photo);
        Button button_a=(Button)findViewById(R.id.button_a) ;
        et_studentname = (EditText)findViewById(R.id.et_studentname);
        Button button_set=(Button)findViewById(R.id.button_set);
        et_view=(EditText)findViewById(R.id.et_view);
        Button button_v=(Button)findViewById(R.id.button_v);
        button_a.setOnClickListener(this);
        takephotob.setOnClickListener(this);
        button_set.setOnClickListener(this);
        button_v.setOnClickListener(this);
        Intent intent = getIntent();
        String msg = "欢迎学生" + intent.getStringExtra("username") + " !";
        username= intent.getStringExtra("username");
        TextView textView = (TextView) findViewById(R.id.successname);
        textView.setText(msg);



    }
    public void onClick(View v) {
        if (v.getId() == R.id.take_photo){
            //TODO
            Intent intent = new Intent(Main3Activity.this, Main5Activity.class);
            intent.putExtra("username",  username);
            //Toast.makeText(this, username, Toast.LENGTH_SHORT).show();
            startActivity(intent);}
        //if (v.getId() == R.id.goto7){
            //TODO
           // Intent intent = new Intent(Main3Activity.this, Main6Activity.class);
            //startActivity(intent);}
        else if(v.getId() == R.id.button_a){

            HttpUtil.sendaddfacesetByOkHttp(new User(username, "creat2"), new okhttp3.Callback(){

                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Main3Activity.this, "网络错误，请检查网络连接！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override

                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            Log.d("test", responseData);
                            AddResponseObj addResponseObj = null;

                            try{
                                addResponseObj = new Gson().fromJson(responseData, AddResponseObj.class);
                                //解析返回信息
                            }catch(Exception e){
                                e.printStackTrace();
                            }

                            if(addResponseObj!=null){
                                String msg1=addResponseObj.getNum();
                                String msg2=addResponseObj.getSum();
                                studentfacetoken=addResponseObj.getStudentcode();
                                final String msg_a11=msg1;
                                final String msg_a22=msg2;
                                final String msg_a="添加了"+msg_a11+"个人脸入库\n"+"库中总人脸数目:"+msg_a22+"\n";
                                Bundle bundle=new Bundle();
                                bundle.putString("msg_a",msg_a);


                                //TODO
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Main3Activity.this, "添加成功!", Toast.LENGTH_SHORT).show();
                                        Message message =new Message();
                                        message.what=ADD;
                                        Bundle bundle=new Bundle();
                                        bundle.putString("msg_a",msg_a);
                                        message.setData(bundle);
                                        handler_a.sendMessage(message);
                                    }
                                });

                            }
                        }
                    }
            );
        }else if(v.getId() == R.id.button_set){
            //先验证输入框是否为空
            if(et_studentname.getText().toString().equals("") || et_studentname.getText().toString().equals("")){
                Toast.makeText(this, "学生姓名不能为空!", Toast.LENGTH_SHORT).show();
            }
            else{
                HttpUtil.sendsetfacesetByOkHttp(new User(et_studentname.getText().toString(), studentfacetoken), new okhttp3.Callback(){
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Main3Activity.this, "网络错误，请检查网络连接！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override

                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.d("test", responseData);
                        SetResponseObj setresponseObj = null;
                        try{
                            setresponseObj = new Gson().fromJson(responseData, SetResponseObj.class); //解析返回信息
                        }catch(Exception e){
                            e.printStackTrace();
                        }if(setresponseObj!=null){
                            String msg1=setresponseObj.getID();

                            final String msg_ID=msg1;
                            final String msg_setID="已经标记该人脸姓名:"+msg1+"\n";
                            Bundle bundle=new Bundle();
                            bundle.putString("msg_setID",msg_setID);

                            //TODO
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Main3Activity.this, "设置姓名成功!", Toast.LENGTH_SHORT).show();
                                    Message message =new Message();
                                    message.what=SET;
                                    Bundle bundle=new Bundle();
                                    bundle.putString("msg_setID",msg_setID);
                                    message.setData(bundle);
                                    handler_set.sendMessage(message);
                                }
                            });

                        }


                    }
                });
            }
        }
        else if(v.getId() == R.id.button_v){
            //            //先验证输入框是否为空
            if(et_view.getText().toString().equals("") || et_view.getText().toString().equals("")){
                Toast.makeText(this, "学生姓名不能为空!", Toast.LENGTH_SHORT).show();
            }
            else{
                HttpUtil.sendviewfacesetByOkHttp(new User(et_view.getText().toString(), "123"), new okhttp3.Callback(){
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Main3Activity.this, "网络错误，请检查网络连接！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override

                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.d("test", responseData);
                        ViewResponseObj viewresponseObj = null;
                        try{
                            viewresponseObj = new Gson().fromJson(responseData, ViewResponseObj.class); //解析返回信息
                        }catch(Exception e){
                            e.printStackTrace();
                        }if(viewresponseObj!=null){
                            String msg_vn=viewresponseObj.getViewname();
                            final String msg_Vn=msg_vn;
                            if(viewresponseObj.getViewcode().equals("0")){
                            final String msg_T="该学生姓名:"+msg_Vn+"\n"+"状态："+"缺勤"+"\n";
                            Bundle bundle=new Bundle();
                            bundle.putString("msg_v",msg_T);

                            //TODO
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Main3Activity.this, "查看成功!", Toast.LENGTH_SHORT).show();
                                    Message message =new Message();
                                    message.what=VIEW;
                                    Bundle bundle=new Bundle();
                                    bundle.putString("msg_v",msg_T);
                                    message.setData(bundle);
                                    handler_v.sendMessage(message);
                                }
                            });}
                            else if(viewresponseObj.getViewcode().equals("1")){
                                final String msg_T="该学生姓名:"+msg_Vn+"\n"+"状态：已经签到!"+"\n";
                                Bundle bundle=new Bundle();
                                bundle.putString("msg_v",msg_T);

                                //TODO
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Main3Activity.this, "查看成功!", Toast.LENGTH_SHORT).show();
                                        Message message =new Message();
                                        message.what=VIEW;
                                        Bundle bundle=new Bundle();
                                        bundle.putString("msg_v",msg_T);
                                        message.setData(bundle);
                                        handler_v.sendMessage(message);
                                    }
                                });}

                        }


                    }
                });
            }
        }

}}
