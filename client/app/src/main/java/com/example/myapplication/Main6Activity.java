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

public class Main6Activity extends AppCompatActivity implements View.OnClickListener{
    private Button buttonsign;
    private Button buttoncreate;
    private String username;
    private Button buttonclean;
    private Button buttonsearch;
    private Button button_f;
    private Button button_addcourse;
    private Button button_addcourse1;
    private Button button_searchcourse;
    private EditText et_in;
    private EditText et_time;
    private Button button_ds;
    private EditText et_ds;
    public static final int CLEAN=5;
    public static final int SEARCH=6;
    public static final int GYX=7;
    public static final int FRESH=8;
    public static final int GYX1=9;
    public static final int GYX3=10;
    private TextView Text_t_print;
    private Button button_gyx;
    public   String studentPname;
    public   String studentUPname;

    private Handler handler_r =new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case FRESH:
                    TextView Text_t_print=(TextView)findViewById(R.id.Text_t_print);
                    Text_t_print.setText(msg.getData().getString("msg_r"));
                    break;
                default:
                    break;
            }
        }
    };


    private Handler handler_gyx2 =new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case GYX3:
                    TextView Text_t_print=(TextView)findViewById(R.id.Text_t_print);
                    Text_t_print.setText(msg.getData().getString("msg_n2"));
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        Button buttonsign = (Button)findViewById(R.id.button_sign);
        Button buttongyx=(Button)findViewById(R.id.button_gyx);
        Button button_f=(Button)findViewById(R.id.button_f);
        TextView Text_t_print=(TextView)findViewById(R.id.Text_t_print) ;
        Button button_addcourse1=(Button)findViewById(R.id.button_addcourse1);
        Button button_searchcourse1=(Button)findViewById(R.id.button_searchcourse1);
        Button button_contronl=(Button)findViewById(R.id.button_control);
        et_in=(EditText)findViewById(R.id.et_in);
        et_time=(EditText)findViewById(R.id.et_time) ;
        button_f.setOnClickListener(this);
        buttonsign.setOnClickListener(this);
        buttongyx.setOnClickListener(this);
        button_addcourse1.setOnClickListener(this);
        button_searchcourse1.setOnClickListener(this);
        button_contronl.setOnClickListener(this);

        Intent intent = getIntent();
        String msg = "欢迎老师" + intent.getStringExtra("username") + " !";
        username= intent.getStringExtra("username");
        TextView textView = (TextView) findViewById(R.id.successname1);
        textView.setText(msg);


    }
    public void onClick(View v) {
        if (v.getId() == R.id.button_control){
            //TODO
            Intent intent = new Intent(Main6Activity.this, Main9Activity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        }
        else if(v.getId()==R.id.button_gyx){
            HttpUtil.sendlookupfacesetByOkHttp(new User(username, "creat2"), new okhttp3.Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Main6Activity.this, "网络错误，请检查网络连接！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override

            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d("test", responseData);
                GyxResponseObj gyxresponseObj = null;
                try{
                    gyxresponseObj = new Gson().fromJson(responseData, GyxResponseObj.class); //解析返回信息
                }catch(Exception e){
                    e.printStackTrace();
                }
                if(gyxresponseObj.getflag().equals("T"))
                {
                    int flag=1;


                    if(flag==1){



                        String Pname=gyxresponseObj.getPname();
                        String UPname=gyxresponseObj.getUPname();
                        final  String msg_Pname=Pname;
                        final String msg_UPname=UPname;
                        studentPname=msg_Pname;
                        studentUPname=msg_UPname;
                        Intent intent = new Intent(Main6Activity.this, Main8Activity.class);
                        intent.putExtra("Pname" ,studentPname);
                        intent.putExtra("UPname" ,studentUPname);
                        intent.putExtra("FLAG","T");
                        startActivity(intent);
                        //TODO
                    }}
                if(gyxresponseObj.getflag().equals("F"))
                {  int flag =0;
                    if(flag== 0) {
                        String UPname1=gyxresponseObj.getUPname1();
                        final String msg_UPname1=UPname1;
                        studentUPname=msg_UPname1;
                        Intent intent = new Intent(Main6Activity.this, Main8Activity.class);
                        intent.putExtra("UPname1" ,studentUPname);
                        intent.putExtra("FLAG","F");
                        startActivity(intent);
                        //TODO
                    }}
                if(gyxresponseObj.getflag().equals("A"))
                {  int flag =3;
                    if(flag== 3) {
                        String UPname=gyxresponseObj.getPname();
                        final String msg_Pname1=UPname;
                        studentPname=msg_Pname1;
                        Intent intent = new Intent(Main6Activity.this, Main8Activity.class);
                        intent.putExtra("Pname1" ,studentPname);
                        intent.putExtra("FLAG","A");
                        startActivity(intent);


                        //TODO

                    }}
                else if(gyxresponseObj.getflag().equals("N"))
                {

                    final String msg_n2 = "无学生数据！" + "\n";
                    Bundle bundle = new Bundle();
                    bundle.putString("msg_n2", msg_n2);

                    //TODO
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Main6Activity.this, "识别失败!", Toast.LENGTH_SHORT).show();
                            Message message = new Message();
                            message.what = GYX3;
                            Bundle bundle = new Bundle();
                            bundle.putString("msg_n2", msg_n2);
                            message.setData(bundle);
                            handler_gyx2.sendMessage(message);
                        }
                    });
                }

            }});

        }
        if (v.getId() == R.id.button_sign){
            //TODO
            Intent intent = new Intent(Main6Activity.this, Main7Activity.class);
            intent.putExtra("username",  username);
            startActivity(intent);}

        else if(v.getId() == R.id.button_f){
            HttpUtil.sendfreshfacesetByOkHttp(new User("creat1", "creat2"), new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Main6Activity.this, "网络错误，请检查网络连接！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override

                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    Log.d("test", responseData);
                    ResponseObj responseObj = null;
                    try{
                        responseObj = new Gson().fromJson(responseData, ResponseObj.class); //解析返回信息
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    if(responseObj.getCode() == 1){
                        //TODO
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Main6Activity.this, "刷新成功!", Toast.LENGTH_SHORT).show();
                                Message message =new Message();
                                message.what=FRESH;
                                Bundle bundle=new Bundle();
                                bundle.putString("msg_r","已经重置所有学生签到信息!");
                                message.setData(bundle);
                                handler_r.sendMessage(message);
                            }
                        });


                    }

                }
            });
        }
        else if(v.getId() == R.id.button_addcourse1){
            //先验证输入框是否为空
            if(et_in.getText().toString().equals("")||et_time.getText().toString().equals("") ){
                Toast.makeText(this, "课程名称和序号不能为空!", Toast.LENGTH_SHORT).show();

            }
            else{
                HttpUtil.addcourse1ByOkHttp(new Teleportation(et_in.getText().toString(), et_time.getText().toString(),studentPname,studentUPname), new okhttp3.Callback(){
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Main6Activity.this, "网络错误，请检查网络连接！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override

                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.d("test", responseData);
                        Addcourse1Obj addcourse1Obj = null;
                        try{
                            addcourse1Obj = new Gson().fromJson(responseData, Addcourse1Obj.class); //解析返回信息
                        }catch(Exception e){
                            e.printStackTrace();
                        }if(addcourse1Obj.getCode() == 1){
                            //TODO
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Main6Activity.this, "记录成功!", Toast.LENGTH_SHORT).show();
                                    Message message =new Message();
                                    message.what=FRESH;
                                    Bundle bundle=new Bundle();
                                    bundle.putString("msg_r","已成功记录该次签到数据!");
                                    message.setData(bundle);
                                    handler_r.sendMessage(message);
                                }
                            });


                        }

                    }
                });
            }
        }
        else if(v.getId() == R.id.button_searchcourse1){{
            if(et_in.getText().toString().equals("")||et_time.getText().toString().equals("") ){
                Toast.makeText(this, "课程名称和序号不能为空!", Toast.LENGTH_SHORT).show();

            }
            else { HttpUtil.lookupByOkHttp(new User(et_in.getText().toString(), et_time.getText().toString()), new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Main6Activity.this, "网络错误，请检查网络连接！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override

                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    Log.d("test", responseData);
                    GyxResponseObj gyxresponseObj = null;
                    try{
                        gyxresponseObj = new Gson().fromJson(responseData, GyxResponseObj.class); //解析返回信息
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    if(gyxresponseObj.getflag().equals("T"))
                    {
                        int flag=1;


                        if(flag==1){



                            String Pname=gyxresponseObj.getPname();
                            String UPname=gyxresponseObj.getUPname();
                            final  String msg_Pname=Pname;
                            final String msg_UPname=UPname;
                            studentPname=msg_Pname;
                            studentUPname=msg_UPname;
                            Intent intent = new Intent(Main6Activity.this, Main8Activity.class);
                            intent.putExtra("Pname" ,studentPname);
                            intent.putExtra("UPname" ,studentUPname);
                            intent.putExtra("FLAG","T");
                            startActivity(intent);
                            //TODO
                        }}
                    if(gyxresponseObj.getflag().equals("F"))
                    {  int flag =0;
                        if(flag== 0) {
                            String UPname1=gyxresponseObj.getUPname1();
                            final String msg_UPname1=UPname1;
                            studentUPname=msg_UPname1;
                            Intent intent = new Intent(Main6Activity.this, Main8Activity.class);
                            intent.putExtra("UPname1" ,studentUPname);
                            intent.putExtra("FLAG","F");
                            startActivity(intent);
                            //TODO
                        }}
                    if(gyxresponseObj.getflag().equals("A"))
                    {  int flag =3;
                        if(flag== 3) {
                            String UPname=gyxresponseObj.getPname();
                            final String msg_Pname1=UPname;
                            studentPname=msg_Pname1;
                            Intent intent = new Intent(Main6Activity.this, Main8Activity.class);
                            intent.putExtra("Pname1" ,studentPname);
                            intent.putExtra("FLAG","A");
                            startActivity(intent);


                            //TODO

                        }}
                    else if(gyxresponseObj.getflag().equals("N"))
                    {

                        final String msg_n2 = "无学生数据！" + "\n";
                        Bundle bundle = new Bundle();
                        bundle.putString("msg_n2", msg_n2);

                        //TODO
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Main6Activity.this, "识别失败!", Toast.LENGTH_SHORT).show();
                                Message message = new Message();
                                message.what = GYX3;
                                Bundle bundle = new Bundle();
                                bundle.putString("msg_n2", msg_n2);
                                message.setData(bundle);
                                handler_gyx2.sendMessage(message);
                            }
                        });
                    }

                }}); } }

        }
        }


    }
