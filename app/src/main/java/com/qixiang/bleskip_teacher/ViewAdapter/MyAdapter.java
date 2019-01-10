package com.qixiang.bleskip_teacher.ViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.qixiang.bleskip_teacher.Model.ClassInfo;
import com.qixiang.bleskip_teacher.R;
import com.qixiang.bleskip_teacher.Util.Utils;

import java.util.List;

/**
 * Created by Administrator on 2018/7/25.
 */

public class MyAdapter extends BaseAdapter implements View.OnClickListener{

    private List<ClassInfo> mList;
    private Context mContext;
    private LayoutInflater mInflater;
    boolean ffFlag = true,createFlag = true;
    // 用来控制CheckBox的选中状况
    //private static HashMap<Integer,Boolean> isSelected;

    private InnerItemOnclickListener mListener;

    public MyAdapter(List<ClassInfo> mList , Context mContext)
    {
        super();
        this.mList = mList;
        this.mContext = mContext;
        ffFlag = true;
        createFlag = true;
        //isSelected = new HashMap<Integer, Boolean>();
        //initDate();

    }
    // 初始化isSelected的数据
    private void initDate(){
        for(ClassInfo ci :mList) {
            ci.setCheck(false);
        }
    }
//    //反选别的checkbox
//    private HashMap initDate(int position){
//        for(int i=0; i<mList.size();i++) {
//            if(i != position)
//                getIsSelected().put(i,false);
//            else
//                getIsSelected().put(i,true);
//        }
//        return isSelected;
//    }
    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {


        ViewHolder holder = null;
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_class,null);
            holder.className = (TextView)convertView.findViewById(R.id.className);
            holder.classnumber= (TextView)convertView.findViewById(R.id.classNumber);
            holder.yesOrNo = (CheckBox)convertView.findViewById(R.id.cb_class);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        if(mList.get(position).isCheck())
            holder.yesOrNo.setChecked(true);
        else
            holder.yesOrNo.setChecked(false);

        holder.className.setText(mList.get(position).getClassName());
        holder.classnumber.setText(mList.get(position).getClassCount());

        if(createFlag){
            if (position == 0){
                holder.yesOrNo.setChecked(true);
                createFlag = false;
            }
        }
        holder.yesOrNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            int currentNum = -1;
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Utils.LogE("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk99999999:"+position);
                if(ffFlag){
                    ffFlag = false;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Log.e(Utils.TAG,"kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk:"+position);
                            initDate();
                            if(currentNum == -1){ //选中

                                //Utils.ClassIndex = position;
                                mList.get(position).setCheck(true);
                                currentNum = position;
                            }else if(currentNum == position){ //同一个item选中变未选中
                                initDate();
                                currentNum = -1;
                            }else if(currentNum != position){ //不是同一个item选中当前的，去除上一个选中的
                                initDate();
                                mList.get(position).setCheck(true);
                                currentNum = position;
                            }
                            MyAdapter.this.notifyDataSetChanged();//刷新adapter
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            ffFlag = true;
                                        }
                                    },100);

                        }
                    },100);

                    try{
                        Utils.ClassIndex2 = position;

                        Utils.bjNum = Integer.valueOf(Utils.stuInfo[position][0].get("bjnum").toString());

                    }catch (Exception e){}
                }
            }
        });

        return convertView;
    }
//    public static HashMap<Integer,Boolean> getIsSelected() {
//        return isSelected;
//    }
//
//    public static void setIsSelected(HashMap<Integer,Boolean> isSelected) {
//        MyAdapter.isSelected = isSelected;
//    }
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
        return mList.size();
    }

    public final static class ViewHolder{
        public TextView className;
        public TextView classnumber;
        public CheckBox yesOrNo;
    }

    public interface InnerItemOnclickListener{
        void itemClick(View v);
    }

    public void setOnInnerItemOnClickListener(InnerItemOnclickListener listener)
    {
        this.mListener = listener;
    }
    @Override
    public void onClick(View v) {
        mListener.itemClick(v);
    }

}