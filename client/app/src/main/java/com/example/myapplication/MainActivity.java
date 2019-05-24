package com.example.myapplication;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText et_username;
    private EditText et_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button submitBtn = (Button)findViewById(R.id.bt_log);
        Button gotoRegBtn = (Button)findViewById(R.id.bt_re);
        Button gotoCut = (Button)findViewById(R.id.bt_cut);
        submitBtn.setOnClickListener(this); //设置按钮的事件监听器
        gotoRegBtn.setOnClickListener(this);//...
        gotoCut.setOnClickListener(this);

        et_username = (EditText)findViewById(R.id.et_username);
        et_password = (EditText)findViewById(R.id.et_password);
    }

    @Override

    public void onClick(View v) {
        if (v.getId() == R.id.bt_re){ //前往注册按钮点击
            //TODO
            Intent intent = new Intent(MainActivity.this, Main2Activity.class);
            startActivity(intent);}
        if(v.getId() == R.id.bt_cut){
            Intent intent1 = new Intent(MainActivity.this, Main4Activity.class);
            startActivity(intent1);
            }


        else if(v.getId() == R.id.bt_log){ //登录按钮点击
                                    //先验证输入框是否为空
                                    if(et_username.getText().toString().equals("") || et_password.getText().toString().equals("")){
                                        Toast.makeText(this, "用户名或密码不能为空!", Toast.LENGTH_SHORT).show();

                                    }
                                    else{
                                        HttpUtil.sendSubmitRequestByOkHttp(new User(et_username.getText().toString(), et_password.getText().toString()), new okhttp3.Callback(){
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(MainActivity.this, "网络错误，请检查网络连接！", Toast.LENGTH_SHORT).show();
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
                        if(responseObj.getCode() == 1){ //登录成功 前往登陆成功界面
                            //TODO
                            //Toast.makeText(MainActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MainActivity.this, Main3Activity.class); //跳转到登录成功的页面
                            intent.putExtra("username", et_username.getText().toString());
                            startActivity(intent);
                        }
                        else if(responseObj.getCode() == 0){ //登录失败，显示提示
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "登录失败，用户名或密码错误!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        }

    }
}
