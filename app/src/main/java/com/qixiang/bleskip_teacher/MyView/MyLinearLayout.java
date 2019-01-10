package com.qixiang.bleskip_teacher.MyView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qixiang.bleskip_teacher.R;

/**
 * Created by Administrator on 2018/7/28.
 */

public class MyLinearLayout extends LinearLayout{
    public MyLinearLayout(Context context,String[] arr) {
        super(context);
        LayoutInflater mInflater = LayoutInflater.from(context);
        View myView = mInflater.inflate(R.layout.item_mission_histroy, null);

        MyAdapter the = new MyAdapter(arr,context);
        ListView ls =  (ListView)findViewById(R.id.lv_mission);
        ls.setAdapter(the);
        addView(myView);
    }

   class MyAdapter extends BaseAdapter{
       String[] mList;
       Context mContext;
       public MyAdapter(String[] mList , Context mContext)
       {
           super();
           this.mList = mList;
           this.mContext = mContext;
       }
       @Override
       public View getView(int position, View convertView, ViewGroup viewGroup) {
           MyLinearLayout.ViewHolder holder = null;

           if(convertView == null)
           {
//               holder = new MyLinearLayout.ViewHolder();
//               convertView = LayoutInflater.from(mContext).inflate(R.layout.twotext,null);
//               holder.missinName = (TextView)convertView.findViewById(R.id.mission_name);
//               holder.count= (TextView)convertView.findViewById(R.id.mission_count);
//               convertView.setTag(holder);
           }else {
               holder = (MyLinearLayout.ViewHolder)convertView.getTag();
           }
           holder.missinName.setText(mList[0]);
           holder.count.setText(mList[1]);
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
           return 0;
       }

   }
    public final  static  class ViewHolder{
        public TextView missinName;
        public TextView count;
    }

}
