package com.cjk.safe.cjk360.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cjk.safe.cjk360.R;
import com.cjk.safe.cjk360.Util.L;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/26.
 */
public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private List<Map<Integer, String>> listMap;
    private String imageText[];

    public GridViewAdapter(List<Map<Integer, String>> listMap, Context context) {
        this.context = context;
        this.listMap = listMap;

    }

    @Override
    public int getCount() {
        return listMap.size();
    }

    @Override
    public Object getItem(int i) {
        return listMap.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
//        View SubView = LayoutInflater.from(context).inflate(R.layout.list_gridview, viewGroup);
        ViewHolder holder;
        //观察convertView随ListView滚动情况
        L.v("MyListViewBase", "getView " + i + " " + view);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_gridview,
                    null);
            holder = new ViewHolder();
                    /*得到各个控件的对象*/
            holder.imageView = (ImageView) view.findViewById(R.id.home_image);
            holder.textView = (TextView) view.findViewById(R.id.home_text);

            view.setTag(holder);//绑定ViewHolder对象
        } else {
            holder = (ViewHolder) view.getTag();//取出ViewHolder对象
        }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/
        Map<Integer, String> map = listMap.get(i);

        Iterator iter = map.entrySet().iterator();
        Object key = null;
        Object val = null;
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            key = entry.getKey();
            val = entry.getValue();
        }
        holder.imageView.setBackgroundResource((Integer) key);
        //  iv_icon.setBackgroundResource(mDrawableIds[position]);
        holder.textView.setText((String) val);
        return view;
    }


    class ViewHolder {
        public ImageView imageView;
        public TextView textView;

    }
}
