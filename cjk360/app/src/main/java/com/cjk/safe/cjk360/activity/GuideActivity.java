package com.cjk.safe.cjk360.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.cjk.safe.cjk360.R;
import com.cjk.safe.cjk360.Util.AppUtils;
import com.cjk.safe.cjk360.Util.HttpUtils;
import com.cjk.safe.cjk360.Util.L;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
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
    private final int ENTER_HOME = 3;
    private final int LOCAL_VERSION_IS_NEW = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.guide);
        checkVersion();
    }

    private void checkVersion() {
        PackageManager pm = this.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(this.getPackageName(), 0);
            final String versionName = pi.versionName;
            final int versionCode = pi.versionCode;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String str = HttpUtils.doGet("http://192.168.1.166:8080/myservice/version.json");
                        int local_versionCode = 0;
                        L.v("str=" + str);
                        if (str != null) {
                            JSONObject jsonObject = new JSONObject(str);
                            String versionCode = jsonObject.getString("versionCode");
                            String versionName = jsonObject.getString("versionName");
                            String apkUrl = jsonObject.getString("apkUrl");
                            Log.i("version", "服务器获取的apk路径----" + apkUrl);
                            String pkName = getApplication().getPackageName();
                            // String local_versionName = getApplication().getPackageManager().getPackageInfo(pkName, 0).versionName;
                            local_versionCode = AppUtils.getVersionCode(getApplicationContext());
                            if (Integer.valueOf(local_versionCode) < Integer.valueOf(versionCode)) {
                                Bundle data = new Bundle();
//		                	 data.putChar("url", apkUrl);
                                data.putString("url", apkUrl);
                                Message message = new Message();
                                message.what = UPDATE;
                                message.setData(data);
                                handler.sendMessage(message);
                            } else if (Integer.valueOf(local_versionCode) == Integer.valueOf(versionCode)) {
                                Message message = new Message();
                                message.what = LOCAL_VERSION_IS_NEW;
                                handler.sendMessage(message);
                            }
                        } else {
                            handler.sendEmptyMessageDelayed(ENTER_HOME, 1500);
                        }
                        Log.i("version", "versionCode------" + versionCode + "versionName-----" + versionName);

                    } catch (JSONException e) {
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
                    handler.sendEmptyMessageDelayed(ENTER_HOME, 1500);
                    break;
                case ENTER_HOME:
                    enterHomeActivity();
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
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
