package com.cjk.safe.cjk360;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/7/25.
 */
public class GuideActivity extends AppCompatActivity {
    private final int INSTALLAPK = 2;
    private final int UPDATE = 1;
    private final int LOCAL_VERSION_IS_NEW = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.guide);
        checkVersion();
    }

    private void checkVersion() {
        PackageManager pm = this.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(this.getPackageName(), 0);
            String versionName = pi.versionName;
            int versionCode = pi.versionCode;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL("http://192.168.1.166:8080/myservice/version.json");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setConnectTimeout(5000);
                        connection.setRequestMethod("GET");
                        if (connection.getResponseCode() == 200) {
                            InputStream iStream = connection.getInputStream();
                            BufferedReader read = new BufferedReader(new InputStreamReader(iStream));
                            String line;
                            StringBuffer json = new StringBuffer();
                            while ((line = read.readLine()) != null) {
                                json.append(line);
                            }
                            Log.i("inputstream", json.toString());
                            JSONObject jsonObject = new JSONObject(json.toString());
                            String versionCode = jsonObject.getString("versionCode");
                            String versionName = jsonObject.getString("versionName");
                            String apkUrl = jsonObject.getString("apkUrl");
                            Log.i("version", "服务器获取的apk路径----" + apkUrl);
                            String pkName = getApplication().getPackageName();
                            String local_versionName = getApplication().getPackageManager().getPackageInfo(pkName, 0).versionName;
                            int local_versionCode = getApplication().getPackageManager().getPackageInfo(pkName, 0).versionCode;
                            if (Integer.valueOf(local_versionCode) < Integer.valueOf(versionCode)) {
                             Bundle data = new Bundle();
//		                	 data.putChar("url", apkUrl);
                                data.putString("url", apkUrl);
                                Message message = new Message();
                                message.what=UPDATE;
                                message.setData(data);
                                handler.sendMessage(message);
                            }
                            else if (Integer.valueOf(local_versionCode)==Integer.valueOf(versionCode)){
                                Message message = new Message();
                                message.what=LOCAL_VERSION_IS_NEW;
                                handler.sendMessage(message);
                            }
                            Log.i("version", "versionCode------" + versionCode + "versionName-----" + versionName);
                        }
                    } catch (MalformedURLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (PackageManager.NameNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int i = msg.what;
            switch (i) {
                case UPDATE:
                    Bundle bundle = msg.getData();
                    String downloadPath = bundle.getString("url");
                    Log.i("version", "handler的apkUrl---" + downloadPath);
                    // ShowUpdateDialoag(downloadPath);
                    break;
                case LOCAL_VERSION_IS_NEW:
                    Toast.makeText(getApplicationContext(), "当前版本是最新的", Toast.LENGTH_SHORT).show();
                    enterHomeActivity();
                    break;
                case INSTALLAPK:
                   /* File file = new File(Environment.getExternalStorageDirectory(),"RemoteUpdates.apk");
                    // 静默安装
                    String path = Environment.getExternalStorageDirectory()+File.separator+"RemoteUpdates.apk";
                    boolean install = ApkController.install(path,getApplicationContext());
                    if(install == true){
                        Toast.makeText(getApplicationContext(), "安装成功", 0).show();
                    }*/
                    //installApk(file);
                default:
                    break;
            }
        }

    };

    private void enterHomeActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
