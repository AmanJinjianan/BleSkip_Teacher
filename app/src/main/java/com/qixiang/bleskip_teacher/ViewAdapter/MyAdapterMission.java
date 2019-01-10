package com.qixiang.bleskip_teacher.ViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qixiang.bleskip_teacher.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/31.
 */
//记录某个班的教学历史
public class MyAdapterMission extends BaseAdapter implements View.OnClickListener{
    private List<Map<String, String>> mList;
    private Context mContext;
    private LayoutInflater mInflater;

    private MyAdapterMission.InnerItemOnclickListener mListener;

    public MyAdapterMission(List<Map<String, String>> mList, Context mContext) {
        super();
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        MyAdapterMission.ViewHolder holder = null;

        if (convertView == null) {
            holder = new MyAdapterMission.ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_mission_histroy, null);
            holder.missionDate = (TextView) convertView.findViewById(R.id.mission_date);
            holder.missionTitle = (TextView) convertView.findViewById(R.id.mission_title);
            holder.missionCount = (TextView) convertView.findViewById(R.id.mission_count);
            convertView.setTag(holder);
        } else {
            holder = (MyAdapterMission.ViewHolder) convertView.getTag();
        }
        holder.missionDate.setText(mList.get(position).get("tasktime").substring(0,11));
        holder.missionTitle.setText(mList.get(position).get("taskname"));
        holder.missionCount.setText(mList.get(position).get("taskpeoplecompletecount")+"/"+(String) mList.get(position).get("taskpeoplecount")+"人");

        convertView.findViewById(R.id.mission_title).setVisibility(View.VISIBLE);
        convertView.findViewById(R.id.mission_count).setVisibility(View.VISIBLE);
        convertView.findViewById(R.id.mission_date).setVisibility(View.VISIBLE);
        if(mList.get(position).get("flag").equals("0")){
            convertView.findViewById(R.id.mission_title).setVisibility(View.GONE);
            convertView.findViewById(R.id.mission_count).setVisibility(View.GONE);
        }else {
            convertView.findViewById(R.id.mission_date).setVisibility(View.GONE);
        }
//        if(position == 1)
//            convertView.findViewById(R.id.mission_date).setVisibility(View.GONE);
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
        if(mList !=null)
        return mList.size();
        else
            return 0;
    }

    public final static class ViewHolder {
        public TextView missionDate;
        public TextView missionTitle;
        public TextView missionCount;
    }

    public interface InnerItemOnclickListener {
        void itemClick(View v);
    }

    public void setOnInnerItemOnClickListener(MyAdapterMission.InnerItemOnclickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        mListener.itemClick(v);
    }
}
