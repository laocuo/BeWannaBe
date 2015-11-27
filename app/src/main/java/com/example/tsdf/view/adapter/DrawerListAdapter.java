
package com.example.tsdf.view.adapter;

import com.example.tsdf.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerListAdapter extends BaseAdapter {

    private int[] mStringList;
    private int[] mIconList;
    private Context mContext;

    public DrawerListAdapter(Context context, int[] mStringList, int[] mIconList) {
        // TODO Auto-generated constructor stub
        this.mStringList = mStringList;
        this.mIconList = mIconList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mStringList.length;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        if (arg1 == null) {
            arg1 = LayoutInflater.from(mContext).inflate(R.layout.drawer_list_item, arg2, false);
            ViewHolder vh = new ViewHolder();
            vh.tv = (TextView) arg1.findViewById(R.id.text);
            vh.iv = (ImageView) arg1.findViewById(R.id.icon);
            arg1.setTag(vh);
        }
        ViewHolder vh = (ViewHolder) arg1.getTag();
        if (vh != null) {
            vh.tv.setText(mStringList[arg0]);
            if (arg0 < mIconList.length) {
                vh.iv.setImageResource(mIconList[arg0]);
            }
        }
        return arg1;
    }

    private class ViewHolder {
        TextView tv;
        ImageView iv;
    }
}
