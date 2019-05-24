package com.example.myapplication;

import android.graphics.Bitmap;

import com.google.gson.Gson;

import java.io.File;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

class RequestImage {
    String image;
    String username;
    String imgfilename;

    public RequestImage(String image, String username, String imgFileName) {
        this.image = image;
        this.username = username;
        this.imgfilename = imgFileName;
    }
}

public class Okhttp {
    public static  void SendP(byte[] byteArray, Callback callback){
    OkHttpClient client = new OkHttpClient();
    RequestBody requestBody = new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("image", "filename.jpg",
                    RequestBody.create(MediaType.parse("image/*jpg"), byteArray))
            .build();
    Request request = new Request.Builder()
            .url("http://39.96.221.44:5000/upload")
            .post(requestBody)
            .build();


        client.newCall(request).enqueue(callback);}
    public static void sendImageByOKHttp(String image, okhttp3.Callback callback, String url, String username, String fileName) {
        OkHttpClient client = new OkHttpClient();

        RequestImage requestImage = new RequestImage(image, username, fileName);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(requestImage));

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(callback);
    }
}

