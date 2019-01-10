package com.qixiang.bleskip_teacher.ViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.qixiang.bleskip_teacher.R;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/8/11.
 */

public class MyAdapterForStuInfo extends BaseAdapter implements View.OnClickListener {
    private JSONObject[] mList;
    private Context mContext;
    private LayoutInflater mInflater;

    public int thePosition;
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected;

    private MyAdapterForStuInfo.InnerItemOnclickListener mListener;

    View.OnClickListener nameClick,agreeClick;

    public MyAdapterForStuInfo(JSONObject[] mList, Context mContext, View.OnClickListener nameClick, View.OnClickListener agreeClick) {
        super();
        this.mList = mList;

        this.mContext = mContext;
        isSelected = new HashMap<Integer, Boolean>();
        this.nameClick = nameClick;
        this.agreeClick = agreeClick;
        // 初始化数据
        initDate();
    }

    // 初始化isSelected的数据
    private void initDate() {
        for (int i = 0; i < mList.length; i++) {
            getIsSelected().put(i, false);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        MyAdapterForStuInfo.ViewHolder holder = null;

        if (convertView == null) {
            holder = new MyAdapterForStuInfo.ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_for_stuinfo, null);
            holder.btn_StuName = (Button) convertView.findViewById(R.id.btn_stu_name);
            holder.tv_StuGender = (TextView) convertView.findViewById(R.id.tv_student_gender);
            holder.tv_Mark = (TextView) convertView.findViewById(R.id.tv_stu_mark);
            holder.btn_state = (Button) convertView.findViewById(R.id.btn_stu_state);
            convertView.setTag(holder);
        } else {
            holder = (MyAdapterForStuInfo.ViewHolder) convertView.getTag();
        }

        try {
            holder.btn_StuName.setText(( mList[position].getString("xsname")));
            holder.btn_StuName.setTag(position);
            holder.btn_StuName.setOnClickListener(nameClick);
            if(( mList[position]).getInt("sex")==1)
                holder.tv_StuGender.setText("男");
            else
                holder.tv_StuGender.setText("女");
            holder.tv_Mark.setText(( mList[position].getString("fs")));
            if(( mList[position].getInt("shzt")) == 1){
                holder.btn_state.setText("已加入");
                holder.btn_state.setEnabled(false);
            }else {
                holder.btn_state.setText("待加入");
            }
            holder.btn_state.setTag(position);
            holder.btn_state.setOnClickListener(agreeClick);
        } catch (Exception e)
        {
        }
        return convertView;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        MyAdapterForStuInfo.isSelected = isSelected;
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

    public final static class ViewHolder {
        public Button btn_StuName;
        public TextView tv_StuGender;
        public TextView tv_Mark;
        public Button btn_state;
    }
    public interface InnerItemOnclickListener {
        void itemClick(View v);
    }

    public void setOnInnerItemOnClickListener(MyAdapterForStuInfo.InnerItemOnclickListener listener) {
        this.mListener = listener;
    }
    @Override
    public void onClick(View v) {
        mListener.itemClick(v);
    }
}