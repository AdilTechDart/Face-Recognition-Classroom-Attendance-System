package com.example.myapplication;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{
    EditText et_username1;
    EditText et_password1;
    EditText et_password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button registerBtn = (Button) findViewById(R.id.bt_re1); //以下都是初始化组件或者设置事件监听器代码
        Button gotoLoginBtn = (Button) findViewById(R.id.bt_bos1);
        registerBtn.setOnClickListener(this);
        gotoLoginBtn.setOnClickListener(this);

        et_username1 = (EditText) findViewById(R.id.et_username1);
        et_password1 = (EditText) findViewById(R.id.et_password1);
        et_password2 = (EditText) findViewById(R.id.et_password2);


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bt_re1){ //注册按钮点击
            if(et_username1.getText().toString().equals("") ||
                    et_password1.getText().toString().equals("") ||
                    et_password2.getText().toString().equals("")){
                Toast.makeText(this, "用户名或密码不能为空!", Toast.LENGTH_SHORT).show();
            }
            else if(et_password1.getText().toString().equals(et_password2.getText().toString()) == false){
                Toast.makeText(this, "两次密码输入不一致!", Toast.LENGTH_SHORT).show();
            }
            else{
                HttpUtil.sendRegRequestByOkHttp(new User(et_username1.getText().toString(), et_password1.getText().toString()), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) { //网络请求失败
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Main2Activity.this, "网络错误，请检查连接！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseDate = response.body().string();
                        ResponseObj responseObj = null;
                        try{
                            responseObj = new Gson().fromJson(responseDate, ResponseObj.class);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        if(responseObj.getCode() == 0){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Main2Activity.this, "注册失败！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else if(responseObj.getCode() == 1){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Main2Activity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                });
            }
        }
        else if(v.getId() == R.id.bt_bos1){ //前往登录界面
            //TODO
//            Intent intent = new Intent(RegActivity.this, MainActivity.class);
//            startActivity(intent);
            finish();
        }
    }
}
