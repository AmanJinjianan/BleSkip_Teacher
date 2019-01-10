package com.qixiang.bleskip_teacher.ViewAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qixiang.bleskip_teacher.R;
import com.qixiang.bleskip_teacher.Util.Utils;

import org.json.JSONObject;

/**
 * Created by Administrator on 2018/8/21.
 */

public class MyAdapterForTeachGoing extends BaseAdapter implements View.OnClickListener {

    private JSONObject[] mList;
    private Context mContext;
    private LayoutInflater mInflater;
    boolean ffFlag = true, createFlag = true;
    // 用来控制CheckBox的选中状况
    //private static HashMap<Integer,Boolean> isSelected;

    private MyAdapterForTeachGoing.InnerItemOnclickListener mListener;

    public MyAdapterForTeachGoing(JSONObject[] mList, Context mContext) {
        super();
        this.mList = mList;
        this.mContext = mContext;
        ffFlag = true;
        createFlag = true;
        //isSelected = new HashMap<Integer, Boolean>();
        //initDate();
    }

    // 初始化isSelected的数据
    private void initDate() {
//        for (ClassInfo ci : mList) {
//            ci.setCheck(false);
//        }
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        MyAdapterForTeachGoing.ViewHolder holder = null;

        if (convertView == null) {
            holder = new MyAdapterForTeachGoing.ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_for_teachgoing, null);
            holder.stuName = (TextView) convertView.findViewById(R.id.tv_teachgoing_stuname);
            holder.stuGender = (TextView) convertView.findViewById(R.id.tv_teachgoing_gender);
            holder.stuGotCount = (TextView) convertView.findViewById(R.id.tv_teachgoing_gotcount);
            holder.stuLoseCount = (TextView) convertView.findViewById(R.id.tv_teachgoing_losecount);
            holder.stuMark = (TextView) convertView.findViewById(R.id.tv_teachgoing_mark);
            convertView.setTag(holder);
        } else {
            holder = (MyAdapterForTeachGoing.ViewHolder) convertView.getTag();
        }
        try {
            Utils.LogE("gotcount:::::::::::::::::"+position+"  "+mList[position].getString("gotcount"));
            if(!mList[position].getBoolean("onlinebool"))//不在线(无心跳包)，背景设红.默认无色
                convertView.setBackgroundColor(Color.RED);
            else
                convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));


            if(!mList[position].getBoolean("missionbool"))//任务没回应，名字设灰,默认红
                holder.stuName.setTextColor(Color.RED);
            else
                holder.stuName.setTextColor(Color.parseColor("#000000"));

            holder.stuName.setText(mList[position].getString("xsname"));
            if(mList[position].getInt("sex")==1)
                holder.stuGender.setText("男");
            else
                holder.stuGender.setText("女");

            holder.stuGotCount.setText(mList[position].getString("gotcount"));
            //holder.stuName.setText(mList[position].getString(""));
            //holder.stuName.setText(mList[position].getString(""));
            //holder.stuName.setText(mList[position].getString(""));
        } catch (Exception e)
        {
        }


        return convertView;
    }
    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getCount() {
        return mList.length;
    }

    public final static class ViewHolder {
        public TextView stuName;
        public TextView stuGender;
        public TextView stuGotCount;
        public TextView stuLoseCount;
        public TextView stuMark;
    }

    public interface InnerItemOnclickListener {
        void itemClick(View v);
    }

    public void setOnInnerItemOnClickListener(MyAdapterForTeachGoing.InnerItemOnclickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        mListener.itemClick(v);
    }
}
