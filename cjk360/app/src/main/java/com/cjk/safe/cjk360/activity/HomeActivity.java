package com.cjk.safe.cjk360.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cjk.safe.cjk360.R;
import com.cjk.safe.cjk360.adapter.GridViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/26.
 */
public class HomeActivity extends Activity{
    private int imageID[]={R.drawable.home_safe,R.drawable.home_callmsgsafe,R.drawable.home_apps,R.drawable.home_taskmanager,R.drawable.home_netmanager,
                   R.drawable.home_trojan,R.drawable.home_sysoptimize,R.drawable.home_tools,R.drawable.home_settings};
    private  String imageText[]={"网关防盗","通信卫士","软件管理","进程管理","流量统计","网关杀毒","缓存清理","高级工具","设置中心"};

    private List<Integer> list_imageId=new ArrayList<Integer>();
    private List<String> list_imageString = new ArrayList<String>();
    private List<Map<Integer,String>> listMap = new ArrayList<>();
    private GridView gridView;
    private ImageView imageView;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        init();
        GridViewAdapter adapter = new GridViewAdapter(listMap,getApplicationContext());
        gridView.setAdapter(adapter);
    }

    private void init() {
        gridView = (GridView) findViewById(R.id.gridView);

        for(int i=0;i<imageID.length;i++){
            Map<Integer,String>  map=new HashMap<>();
            map.put(imageID[i],imageText[i]);
            listMap.add(map);
        }

    }
}
