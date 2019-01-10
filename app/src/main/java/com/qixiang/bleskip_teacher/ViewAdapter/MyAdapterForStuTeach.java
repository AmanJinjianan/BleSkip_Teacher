package com.qixiang.bleskip_teacher.ViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.qixiang.bleskip_teacher.R;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/8/1.
 */

public class MyAdapterForStuTeach extends BaseAdapter implements View.OnClickListener{

    public JSONObject[] mList;
    private Context mContext;
    private LayoutInflater mInflater;

    public int thePosition;
    // 用来控制CheckBox的选中状况
    private  HashMap<Integer,Boolean> isSelected;

    private MyAdapterForStuTeach.InnerItemOnclickListener mListener;

    public MyAdapterForStuTeach(JSONObject[] mList , Context mContext, CompoundButton.OnCheckedChangeListener listener2)
    {
        super();
        this.mList = mList;
        this.mContext = mContext;
        isSelected = new HashMap<Integer, Boolean>();
        // 初始化数据
        initDate();
    }
    public  HashMap<Integer,Boolean> getIsSelected() {
        return isSelected;
    }

    public  void setIsSelected(HashMap<Integer,Boolean> isSelected) {
        this.isSelected = isSelected;
    }
    // 初始化isSelected的数据
    private void initDate() {
        for (int i = 0; i < mList.length; i++) {
            getIsSelected().put(i, false);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        MyAdapterForStuTeach.ViewHolder holder = null;

        if(convertView == null)
        {
            holder = new MyAdapterForStuTeach.ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_for_stuteach,null);
            holder.tv_Teach_StuName = (TextView)convertView.findViewById(R.id.tv_teach_stuname);
            holder.tv_Teach_StuGender= (TextView)convertView.findViewById(R.id.tv_teach_stugender);
            holder.tv_Teach_Mark = (TextView)convertView.findViewById(R.id.tv_teach_mark);
            holder.tv_Teach_Point= (TextView)convertView.findViewById(R.id.tv_teach_point);
            holder.yesOrNo = (CheckBox) convertView.findViewById(R.id.cb_teach);
            convertView.setTag(holder);
        }else {
            holder = (MyAdapterForStuTeach.ViewHolder)convertView.getTag();
        }
        try {
            holder.tv_Teach_StuName.setText(mList[position].getString("xsname"));
            if(mList[position].getInt("sex") == 1)
                holder.tv_Teach_StuGender.setText("男");
            else
                holder.tv_Teach_StuGender.setText("女");

            holder.tv_Teach_Mark.setText("无数据");
            if (null == String.valueOf(mList[position].getString("fs"))) {
                holder.tv_Teach_Point.setText("无数据");
            }else {
                holder.tv_Teach_Point.setText(String.valueOf(mList[position].getString("fs")));
            }

            //thePosition =position;
            holder.yesOrNo.setChecked(getIsSelected().get(position));

        } catch (Exception e)
        {
            Intent intent = new Intent("com.qixiang.jsonexception");
            mContext.sendBroadcast(intent);
        }

        //holder.yesOrNo.setOnCheckedChangeListener(listener2);
//        holder.yesOrNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    isSelected.put(thePosition,true);
//                }else {
//                    isSelected.remove(thePosition);
//                }
//            }
//        });
        return convertView;
    }




    @Override
    public Object getItem(int i) {
        return mList[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getCount() {
        return mList.length;
    }

    public final static class ViewHolder{
        public TextView tv_Teach_StuName;
        public TextView tv_Teach_StuGender;
        public TextView tv_Teach_Mark;
        public TextView tv_Teach_Point;
        public CheckBox yesOrNo;
    }

    public interface InnerItemOnclickListener{
        void itemClick(View v);
    }

    public void setOnInnerItemOnClickListener(MyAdapterForStuTeach.InnerItemOnclickListener listener)
    {
        this.mListener = listener;
    }
    @Override
    public void onClick(View v) {
        mListener.itemClick(v);
    }

}