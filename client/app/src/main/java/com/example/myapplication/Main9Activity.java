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

public class Main9Activity extends AppCompatActivity implements View.OnClickListener{
    private Button buttonsign;
    private Button buttoncreate;
    private String username;
    private Button buttonclean;
    private Button buttonsearch;
    private Button button_f;
    private Button button_addcourse;
    private Button button_addcourse1;
    private Button button_searchcourse;
    private EditText et_in1;
    private EditText et_time1;
    private Button button_ds;
    private EditText et_ds1;
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

    private Handler handler_c =new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case CLEAN:
                    TextView Text_t_print2=(TextView)findViewById(R.id.Text_t_print2);
                    Text_t_print2.setText(msg.getData().getString("msg_c"));
                    break;
                default:
                    break;
            }
        }
    };
    private Handler handler_r =new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case FRESH:
                    TextView Text_t_print2=(TextView)findViewById(R.id.Text_t_print2);
                    Text_t_print2.setText(msg.getData().getString("msg_r"));
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
                    TextView Text_t_print2=(TextView)findViewById(R.id.Text_t_print2);
                    Text_t_print2.setText(msg.getData().getString("msg_n2"));
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main9);
        Button buttoncreate =(Button)findViewById((R.id.button_create));
        Button buttonclean =(Button)findViewById(R.id.button_clean);
        Button button_addcourse=(Button)findViewById(R.id.button_addcourse);
        Button button_searchcourse=(Button)findViewById(R.id.button_searchcourse);
        Button button_ds=(Button)findViewById(R.id.button_ds);
        et_in1=(EditText)findViewById(R.id.et_in1);
        et_time1=(EditText)findViewById(R.id.et_time1) ;
        et_ds1=(EditText)findViewById(R.id.et_ds1);
        buttoncreate.setOnClickListener(this);
        buttonclean.setOnClickListener(this);
        button_addcourse.setOnClickListener(this);
        button_searchcourse.setOnClickListener(this);
        button_ds.setOnClickListener(this);
        Intent intent = getIntent();
        String msg = "欢迎老师" + intent.getStringExtra("username") + " !";
        username= intent.getStringExtra("username");
        TextView textView = (TextView) findViewById(R.id.successname2);
        textView.setText(msg);


    }
    public void onClick(View v) {

        if(v.getId() == R.id.button_create){
            HttpUtil.sendcreatefacesetByOkHttp(new User("creat1", "creat2"), new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Main9Activity.this, "网络错误，请检查网络连接！", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(Main9Activity.this, "创建成功!", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }

                }
            });
        }
        else if(v.getId() == R.id.button_clean){
            HttpUtil.sendcleanfacesetByOkHttp(new User("creat1", "creat2"), new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Main9Activity.this, "网络错误，请检查网络连接！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override

                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    Log.d("test", responseData);
                    CleanResponseObj cleanresponseObj = null;
                    try{
                        cleanresponseObj = new Gson().fromJson(responseData, CleanResponseObj.class); //解析返回信息
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    if(cleanresponseObj!=null){
                        String msg1=cleanresponseObj.getnumber();

                        final String msg_clean=msg1;
                        final String msg_c="已经清除人脸数目:"+msg_clean+"\n";
                        Bundle bundle=new Bundle();
                        bundle.putString("msg_c",msg_c);

                        //TODO
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Main9Activity.this, "清除成功!", Toast.LENGTH_SHORT).show();
                                Message message =new Message();
                                message.what=CLEAN;
                                Bundle bundle=new Bundle();
                                bundle.putString("msg_c",msg_c);
                                message.setData(bundle);
                                handler_c.sendMessage(message);
                            }
                        });

                    }

                }
            });
        }else if(v.getId() == R.id.button_ds){
            HttpUtil.deletestudentByOkHttp(new User(et_ds1.getText().toString(), et_in1.getText().toString()), new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Main9Activity.this, "网络错误，请检查网络连接！", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(Main9Activity.this, "删除成功!", Toast.LENGTH_SHORT).show();
                                Message message =new Message();
                                message.what=FRESH;
                                Bundle bundle=new Bundle();
                                bundle.putString("msg_r","已在课程中删除学生："+et_ds1.getText().toString()+"!");
                                message.setData(bundle);
                                handler_r.sendMessage(message);
                            }
                        });


                    }

                }
            });
        }
        else if(v.getId() == R.id.button_addcourse){
            //先验证输入框是否为空
            if(et_in1.getText().toString().equals("") ){
                Toast.makeText(this, "课程名称不能为空!", Toast.LENGTH_SHORT).show();

            }
            else{
                HttpUtil.addcourseByOkHttp(new User(et_in1.getText().toString(), et_time1.getText().toString()), new okhttp3.Callback(){
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Main9Activity.this, "网络错误，请检查网络连接！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override

                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.d("test", responseData);
                        AddcourseObj addcourseObj = null;
                        try{
                            addcourseObj = new Gson().fromJson(responseData, AddcourseObj.class); //解析返回信息
                        }catch(Exception e){
                            e.printStackTrace();
                        }if(addcourseObj.getCode() == 1){
                            //TODO
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Main9Activity.this, "创建课程成功!", Toast.LENGTH_SHORT).show();
                                    Message message =new Message();
                                    message.what=FRESH;
                                    Bundle bundle=new Bundle();
                                    bundle.putString("msg_r","创建课程:"+et_in1.getText()+" "+"成功!");
                                    message.setData(bundle);
                                    handler_r.sendMessage(message);
                                }
                            });


                        }

                    }
                });
            }
        }
        else if(v.getId() == R.id.button_searchcourse){
            if(et_in1.getText().toString().equals("")||et_time1.getText().toString().equals("") ){
                Toast.makeText(this, "课程名称和序号不能为空!", Toast.LENGTH_SHORT).show();

            }
            else { HttpUtil.lookupByOkHttp(new User(et_in1.getText().toString(), et_time1.getText().toString()), new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Main9Activity.this, "网络错误，请检查网络连接！", Toast.LENGTH_SHORT).show();
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
                            Intent intent = new Intent(Main9Activity.this, Main8Activity.class);
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
                            Intent intent = new Intent(Main9Activity.this, Main8Activity.class);
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
                            Intent intent = new Intent(Main9Activity.this, Main8Activity.class);
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
                                Toast.makeText(Main9Activity.this, "识别失败!", Toast.LENGTH_SHORT).show();
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

