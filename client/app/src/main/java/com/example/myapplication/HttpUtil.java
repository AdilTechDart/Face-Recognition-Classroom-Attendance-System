package com.example.myapplication;

import com.google.gson.Gson;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class HttpUtil {

    public static void sendSubmitRequestByOkHttp(User user, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(user));//添加请求头信息和请求体（json）
        Request request = new Request.Builder()
                .url("http://39.96.221.44:5000/login")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendRegRequestByOkHttp(User user, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(user));
        Request request = new Request.Builder()
                .url("http://39.96.221.44:5000/register")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void sendcreatefacesetByOkHttp(User user,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(user));
        Request request = new Request.Builder()
                .url("http://39.96.221.44:5000/create")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void sendcleanfacesetByOkHttp(User user,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(user));
        Request request = new Request.Builder()
                .url("http://39.96.221.44:5000/clean")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void sendscanfacesetByOkHttp(User user,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(user));
        Request request = new Request.Builder()
                .url("http://39.96.221.44:5000/update")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendaddfacesetByOkHttp(User user,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(user));
        Request request = new Request.Builder()
                .url("http://39.96.221.44:5000/add")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void sendsetfacesetByOkHttp(User user,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(user));
        Request request = new Request.Builder()
                .url("http://39.96.221.44:5000/set")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void sendsearchfacesetByOkHttp(User user,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(user));
        Request request = new Request.Builder()
                .url("http://39.96.221.44:5000/search")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void sendlookupfacesetByOkHttp(User user,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(user));
        Request request = new Request.Builder()
                .url("http://39.96.221.44:5000/lookup")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void sendfreshfacesetByOkHttp(User user,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(user));
        Request request = new Request.Builder()
                .url("http://39.96.221.44:5000/fresh")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void sendviewfacesetByOkHttp(User user,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(user));
        Request request = new Request.Builder()
                .url("http://39.96.221.44:5000/view")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void addcourseByOkHttp(User user,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(user));
        Request request = new Request.Builder()
                .url("http://39.96.221.44:5000/addcourse")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void addcourse1ByOkHttp(Teleportation teleportation,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(teleportation));
        Request request = new Request.Builder()
                .url("http://39.96.221.44:5000/addcourse1")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void deletestudentByOkHttp(User user,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(user));
        Request request = new Request.Builder()
                .url("http://39.96.221.44:5000/deletestudent")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void lookupByOkHttp(User user,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(user));
        Request request = new Request.Builder()
                .url("http://39.96.221.44:5000/lookup1")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
