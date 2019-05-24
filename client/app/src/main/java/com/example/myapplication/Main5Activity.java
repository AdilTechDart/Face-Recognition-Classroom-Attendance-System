package com.example.myapplication;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;




public class Main5Activity extends AppCompatActivity implements View.OnClickListener{
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    public static final int UP=1;
    private Bitmap bitmap;
    private ImageView picture;
    private Uri imageUri;
    private String username;
    private String msgaddfacetoken;
    private Handler handler =new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case UP:
                    TextView Text_print=(TextView)findViewById(R.id.Text_printstudent);
                    Text_print.setText(msg.getData().getString("msg"));
                    break;
                default:
                    break;
            }
        }
    };
    private void drawRectangles(Bitmap imageBitmap,int width ,int top1,int left1,int height) {
        int left, top, right, bottom;
        Bitmap mutableBitmap = imageBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
//Canvas canvas = new Canvas(imageBitmap);
        Paint paint = new Paint();
        left = left1;
        top = top1;
        right =width+left1 ;
        bottom =width+top1;
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);//不填充
        paint.setStrokeWidth(10); //线的宽度
        canvas.drawRect(left, top, right, bottom, paint);

        picture.setImageBitmap(mutableBitmap);//img: 定义在xml布局中的ImagView控件
        //img.setImageBitmap(imageBitmap);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        Button takePhoto = (Button) findViewById(R.id.button1);
        Button chooseFromAblum = (Button) findViewById(R.id.choose_from_album);
        picture = (ImageView) findViewById(R.id.imageView1);
        Button studentsearch=(Button)findViewById(R.id.student_search);
        studentsearch.setOnClickListener(this);
        Intent intent =getIntent();
        username=intent.getStringExtra("username");

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建File对象，用于存储拍摄后的照片
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 26) {
                    imageUri = FileProvider.getUriForFile(Main5Activity.this, "com.example.myapplication.fileprovider", outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }
                //启动相机程序

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);

            }
        });
        chooseFromAblum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Main5Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Main5Activity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                } else {
                    openAlbum();
                }
            }
        });
    }
    public void onClick(View v){
         if(v.getId() == R.id.student_search){
            HttpUtil.sendscanfacesetByOkHttp(new User(username, "creat2"), new okhttp3.Callback(){

                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Main5Activity.this, "网络错误，请检查网络连接！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override

                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            Log.d("test", responseData);
                            ScanResponseObj scanResponseObj = null;

                            try{
                                scanResponseObj = new Gson().fromJson(responseData, ScanResponseObj.class);
                                //解析返回信息
                            }catch(Exception e){
                                e.printStackTrace();
                            }


                            if(scanResponseObj.getError().equals("T")){
                                String msg1=scanResponseObj.getage();
                                String msg2=scanResponseObj.getgender();
                                String msg3=scanResponseObj.getnum();
                                String msg4=scanResponseObj.getCode();
                                final String msg11=msg1;
                                final String msg22=msg2;
                                final String msg33=msg3;
                                final String msg44=msg4;
                                msgaddfacetoken=msg44;
                                final String msg="检测到"+msg3+"个人脸\n"+"年龄:"+msg1+"\n"+"性别："+msg2+"\n"+"人脸唯一识别码"+msg4+"\n";
                                Bundle bundle=new Bundle();
                                bundle.putString("msg",msg);
                                drawRectangles(bitmap,scanResponseObj.getWidth(),scanResponseObj.getTop(),scanResponseObj.getLeft(),scanResponseObj.getHeight());

                                //TODO
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Main5Activity.this, "扫描成功!", Toast.LENGTH_SHORT).show();
                                        Message message =new Message();
                                        message.what=UP;
                                        Bundle bundle=new Bundle();
                                        bundle.putString("msg",msg);
                                        message.setData(bundle);
                                        handler.sendMessage(message);
                                    }
                                });

                            }
                            if(scanResponseObj.getError().equals("F")){

                                final String msg="未检测到任何人脸数据！请重新拍照或上传！\n";
                                Bundle bundle=new Bundle();
                                bundle.putString("msg",msg);

                                //TODO
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Main5Activity.this, "扫描成功!", Toast.LENGTH_SHORT).show();
                                        Message message =new Message();
                                        message.what=UP;
                                        Bundle bundle=new Bundle();
                                        bundle.putString("msg",msg);
                                        message.setData(bundle);
                                        handler.sendMessage(message);
                                    }
                                });

                            }
                        }
                    }
            );
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);  //打开相册

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "you denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        //将拍摄的照片显示出来
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        sendImage(bitmap);
                        picture.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data); //4.4以上
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;

        }

    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docID = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents" .equals(uri.getAuthority())) {
                String id = docID.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents" .equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docID));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content" .equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file" .equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
        sendImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);

    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private Bitmap displayImage(String imagePath) {
        if (imagePath != null) {
            bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
            return bitmap;
        } else {
            Toast.makeText(this, "faile to get image", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private void sendImage(Object image) {
        Bitmap bitmap = null;
        if(image instanceof Bitmap) {
            bitmap = (Bitmap)image;
        }
        else if(image instanceof String) {
            bitmap = BitmapFactory.decodeFile((String) image);
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, out);

        byte[] buffer = out.toByteArray();
        String photoStr = Base64.encodeToString(buffer, Base64.DEFAULT);

        String url = "http://39.96.221.44:5000/upload";

        Okhttp.sendImageByOKHttp(photoStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Main5Activity.this, "network failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                ResponseObj responseObj = null;
                try {
                    responseObj = new Gson().fromJson(responseData, ResponseObj.class);
                }catch (Exception e) {
                    e.printStackTrace();
                }

                if(responseObj.getCode() == 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Main5Activity.this, "已成功上传", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if(responseObj.getCode() == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Main5Activity.this, "上传失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }, url, username, "学生上传"+username+".jpeg");//intent传
    }

}
