package com.qixiang.bleskip_teacher.ViewAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.qixiang.bleskip_teacher.R;
import com.qixiang.bleskip_teacher.Test3Char;
import com.qixiang.bleskip_teacher.Util.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Created by Administrator on 2018/8/21.
 */

public class MyAdapterForGameResult extends BaseAdapter implements View.OnClickListener {

    private JSONObject[] mList;
    private Context mContext;
    private LayoutInflater mInflater;
    boolean ffFlag = true, createFlag = true;
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer,Boolean> isSelected;

    private MyAdapterForGameResult.InnerItemOnclickListener mListener;

    public MyAdapterForGameResult(JSONObject[] mList, Context mContext) {
        super();
        this.mList = mList;
        this.mContext = mContext;
        ffFlag = true;
        createFlag = true;
        isSelected = new HashMap<Integer, Boolean>();
        // 初始化数据
        initDate();
    }
    public HashMap<Integer,Boolean> getIsSelected() {
        return isSelected;
    }
    public  void setIsSelected(int index,boolean flag) {
        isSelected.put(index,flag);
    }
//    public  void setIsSelected(HashMap<Integer,Boolean> isSelected) {
//        this.isSelected = isSelected;
//    }
// 初始化isSelected的数据
private void initDate() {
    for (int i = 0; i < mList.length; i++) {
        getIsSelected().put(i, false);
    }
}
    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        MyAdapterForGameResult.ViewHolder holder = null;
        if (convertView == null) {
                holder = new MyAdapterForGameResult.ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_for_gameresult, null);
                holder.stuName = (TextView) convertView.findViewById(R.id.tv_gameresult_stuname);
                holder.stuGender = (TextView) convertView.findViewById(R.id.tv_gameresult_gender);
                holder.stuGotCount = (TextView) convertView.findViewById(R.id.tv_gameresult_gotcount);
                holder.stuLoseCount = (TextView) convertView.findViewById(R.id.tv_gameresult_losecount);
                holder.stuMark = (TextView) convertView.findViewById(R.id.tv_gameresult_mark);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_gameresult);
                holder.chaColumn = (lecho.lib.hellocharts.view.ColumnChartView) convertView.findViewById(R.id.columnchart111);
                convertView.setTag(holder);
        } else {
            holder = (MyAdapterForGameResult.ViewHolder) convertView.getTag();
        }
        //Utils.LogE("gotcount:::::::::::::::::"+position+"  ");
        try {
            Utils.LogE("losecount:::::::::::::::::"+position+"  "+mList[position].getString("losecount"));
            holder.stuName.setText(mList[position].getString("xsname"));
            holder.chaColumn.setVisibility(View.GONE);
            holder.checkBox.setChecked(isSelected.get(position));
            holder.stuGotCount.setText(mList[position].getString("gotcount"));

            holder.stuLoseCount.setText(mList[position].getString("losecount"));
            holder.stuMark.setText(mList[position].getString("fs"));
            if(mList[position].getString("sex")=="1"){
                holder.stuGender.setText("男");
            } else if(mList[position].getString("sex")=="2"){
                holder.stuGender.setText("女");
            }

            if(mList[position].getString("sex")=="3"){

                holder.chaColumn.setVisibility(View.VISIBLE);
                FrameLayout.LayoutParams pal =(FrameLayout.LayoutParams) holder.chaColumn.getLayoutParams(); //取控件textView当前的布局参数
                pal.height = 500;
                holder.chaColumn.setLayoutParams(pal);

                columnChart = holder.chaColumn;
                Object ob = mList[position].get("YArray");
                //int[] jj = (int[])ob;
                dataInit(mList[position].getString("XArray"),(int[])ob);
                //dataInit();

//                Utils.LogE("name:::::::::::::::::"+mList[position].getString("xsname"));
                holder.stuName.setVisibility(View.GONE);
                holder.checkBox.setVisibility(View.GONE);
                holder.stuGender.setVisibility(View.GONE);
                holder.stuGotCount.setVisibility(View.GONE);
                holder.stuLoseCount.setVisibility(View.GONE);
                holder.stuMark.setVisibility(View.GONE);

            }




//            if(position < mList.length-1){
//                Utils.LogE("gotcount::::::::::::::::1111:");
//
//                holder.stuName.setText(mList[position].getString("xsname"));
//                holder.checkBox.setChecked(isSelected.get(position));
//            }else if (position == mList.length-1){
//                Utils.LogE("gotcount:::::::::::::::22222:"+holder.stuName.getText());
//                convertView.setVisibility(View.VISIBLE);
//                convertView.setMinimumHeight(400);
//            }
        } catch (Exception e)
        {
            Utils.LogE("1122222222222222222222222222222222222"+e.toString());
        }


        return convertView;
    }

    ColumnChartView columnChart;
    //声明所需变量
    public final static String[] months = new String[] { "Jan", "Feb", "Mar",
            "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", };

    //ColumnChartView columnChart;
    ColumnChartData columnData;
    List<Column> lsColumn = new ArrayList<Column>();
    List<SubcolumnValue> lsValue;
    //初始化数据并显示在图表上

    private void dataInit(String monthString,int[] jj) {
        String[] months = monthString.split(",");

        Utils.LogE("gotcount::::::::66666666666666::::::::1111:");
        int numSubcolumns = 1;
        int numColumns = months.length;

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                //values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
                values.add(new SubcolumnValue(jj[i], ChartUtils.pickColor()));
            }
            // 点击柱状图就展示数据量
            axisValues.add(new AxisValue(i).setLabel(months[i]));
            columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
        }

        columnData = new ColumnChartData(columns);

        columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true).setTextColor(Color.BLACK));
        columnData.setAxisYLeft(new Axis().setHasLines(true).setTextColor(Color.BLACK).setMaxLabelChars(2));

        columnChart.setColumnChartData(columnData);

// Set value touch listener that will trigger changes for chartTop.
        columnChart.setOnValueTouchListener(new ValueTouchListener());

// Set selection mode to keep selected month column highlighted.
        columnChart.setValueSelectionEnabled(true);

        columnChart.setZoomType(ZoomType.HORIZONTAL);

    }
    /**
     * 柱状图监听器
     *
     * @author 1017
     *
     */
    private class ValueTouchListener implements ColumnChartOnValueSelectListener {
        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex,SubcolumnValue value) {
            // generateLineData(value.getColor(), 100);
        }

        @Override
        public void onValueDeselected() {
            // generateLineData(ChartUtils.COLOR_GREEN, 0);
        }
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

        public CheckBox checkBox;

        public lecho.lib.hellocharts.view.ColumnChartView chaColumn;
    }

    public interface InnerItemOnclickListener {
        void itemClick(View v);
    }

    public void setOnInnerItemOnClickListener(MyAdapterForGameResult.InnerItemOnclickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        mListener.itemClick(v);
    }
}
