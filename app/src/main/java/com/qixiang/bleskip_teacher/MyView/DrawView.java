package com.qixiang.bleskip_teacher.MyView;

/**
 * Created by Administrator on 2019/10/6.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.qixiang.bleskip_teacher.R;
import com.qixiang.bleskip_teacher.Util.Utils;

@SuppressLint("DrawAllocation")
public class DrawView extends View {
    private int view_width = 0;    //屏幕的宽度
    private int view_height = 0;    //屏幕的高度
    private float preX;    //起始点的x坐标值
    private float preY;//起始点的y坐标值
    private Path path;    //路径
    public Paint paint = null;    //画笔
    public Paint mEraserPaint = null;//橡皮擦
    Bitmap cacheBitmap = null;// 定义一个内存中的图片，该图片将作为缓冲区
    Canvas cacheCanvas = null;// 定义cacheBitmap上的Canvas对象

    private Context theContext;
    public DrawView(Context context, AttributeSet set) {
        super(context, set);
        theContext = context;
        view_width = context.getResources().getDisplayMetrics().widthPixels; // 获取屏幕的宽度
        view_height = context.getResources().getDisplayMetrics().heightPixels; // 获取屏幕的高度
        System.out.println(view_width + "*" + view_height);
        // 创建一个与该View相同大小的缓存区
        cacheBitmap = Bitmap.createBitmap(view_width, view_height,
                Config.ARGB_8888);
        cacheCanvas = new Canvas();
        path = new Path();
        cacheCanvas.setBitmap(cacheBitmap);// 在cacheCanvas上绘制cacheBitmap
        paint = new Paint(Paint.DITHER_FLAG);
        paint.setColor(Color.RED); // 设置默认的画笔颜色
        // 设置画笔风格
        paint.setStyle(Paint.Style.STROKE);    //设置填充方式为描边
        paint.setStrokeJoin(Paint.Join.ROUND);        //设置笔刷的图形样式
        paint.setStrokeCap(Paint.Cap.ROUND);    //设置画笔转弯处的连接风格
        paint.setStrokeWidth(50); // 设置默认笔触的宽度为5像素
        paint.setAntiAlias(true); // 使用抗锯齿功能
        paint.setDither(true); // 使用抖动效果
    }

    @Override
    public void onDraw(Canvas canvas) {
        //canvas.drawColor(R.drawable.btn_colhome_green2);    //设置背景颜色
        Paint bmpPaint = new Paint();    //采用默认设置创建一个画笔
        canvas.drawBitmap(cacheBitmap, 0, 0, bmpPaint); //绘制cacheBitmap
        canvas.drawPath(path, paint);    //绘制路径
        canvas.save();    //保存canvas的状态
        //canvas.restore();	//恢复canvas之前保存的状态，防止保存后对canvas执行的操作对后续的绘制有影响
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 获取触摸事件的发生位置
        float x = event.getX();
        ar.add(x);
        float y = event.getY();
        ar.add(y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y); // 将绘图的起始点移到（x,y）坐标点的位置
                preX = x;
                preY = y;
                Utils.LogE("ACTION_下下。。。。。。。");
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - preX);
                float dy = Math.abs(y - preY);
                if (dx >= 5 || dy >= 5) { // 判断是否在允许的范围内
                    path.quadTo(preX, preY, (x + preX) / 2, (y + preY) / 2);
                    preX = x;
                    preY = y;
                }
                Utils.LogE("ACTION_中中。。。。。。。");
                invalidate();
                //Utils.LogE("ACTION_MOVEeee:X"+preX+"  Y:"+preY);
                break;
            case MotionEvent.ACTION_UP:
                cacheCanvas.drawPath(path, paint); //绘制路径
                path.reset();
                Utils.LogE("ACTION_起起。。。。。。。");
                invalidate();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        diaClear();
                    }
                },1000);

                break;
        }

        return true;        // 返回true表明处理方法已经处理该事件
    }

    private void diaClear() {
        //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        paint.setColor(Color.BLUE);
        paint.setAlpha(0);
        paint.setStrokeWidth(52);    //设置笔触的宽度
        invalidate();
        if(timer == null)
            timer = new Timer();
        timer.schedule(new MyTimerTask(),1000,200);
    }
    private void diaClear2() {
        //橡皮擦
        mEraserPaint = new Paint();
        mEraserPaint.setColor(Color.BLUE);
        //mEraserPaint.setAlpha(0);
        //这个属性是设置paint为橡皮擦重中之重
        //这是重点
        //下面这句代码是橡皮擦设置的重点
        mEraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        //上面这句代码是橡皮擦设置的重点（重要的事是不是一定要说三遍）
        mEraserPaint.setAntiAlias(true);
        mEraserPaint.setDither(true);
        mEraserPaint.setStyle(Paint.Style.STROKE);
        mEraserPaint.setStrokeJoin(Paint.Join.ROUND);
        mEraserPaint.setStrokeWidth(55);
        invalidate();
        if(timer == null)
            timer = new Timer();
        timer.schedule(new MyTimerTask(),1000,200);
    }
    public  ArrayList<Float> ar = new ArrayList<>();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            float x = ar.get(index);
            float y = ar.get(index+1);
            if(index == 0){
                path.moveTo(x, y); // 将绘图的起始点移到（x,y）坐标点的位置
                preX = x;
                preY = y;
                index = index+2;
                invalidate();
            }else {
                float dx = Math.abs(x - preX);
                float dy = Math.abs(y - preY);
                if (dx >= 5 || dy >= 5) { // 判断是否在允许的范围内
                    path.quadTo(preX, preY, (x + preX) / 2, (y + preY) / 2);
                    preX = x;
                    preY = y;
                }
                invalidate();
                index = index+2;
                if(index+1 > ar.size()){
                    invalidate();
                    return;
                }
        }
        }
    };
    int index =0;
    Timer timer;

    class MyTimerTask extends TimerTask{
        @Override
        public void run() {

            if(index+1 > ar.size()){
                cacheCanvas.drawPath(path, paint); //绘制路径
                path.reset();

                invalidate();
                timer.cancel();
                ar.clear();
                this.cancel();
                timer = null;
                ((Activity)theContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(theContext,"完了",Toast.LENGTH_SHORT).show();

                        nothing();
                    }
                });
                return;
            }
            handler.sendEmptyMessage(1);
        }
    }

    public void clear() {
        //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        //Color.TRANSPARENT
        paint.setColor(Color.YELLOW);
        paint.setAlpha(0);
        paint.setStrokeWidth(52);    //设置笔触的宽度
        invalidate();
    }


    public void back() {
        Canvas canvas = new Canvas();
        canvas.restore();
    }

    public void nothing() {
        cacheBitmap = null;
        // 创建一个与该View相同大小的缓存区
        cacheBitmap = Bitmap.createBitmap(view_width, view_height,
                Config.ARGB_8888);
        cacheCanvas = new Canvas();
        cacheCanvas.setBitmap(cacheBitmap);// 在cacheCanvas上绘制cacheBitmap
        invalidate();
    }

    public void save(int i, int j) {
        //int i=0;

        try {
            saveBitmap("DrawingPicture", "DrawingPicture_" + i, j);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // 保存绘制好的位图到APP目录下
    public void saveBitmap(String filesize, String fileName, int j) throws IOException {

        String directoryPath;
        directoryPath = getFilePath(getContext(), filesize, fileName, j);
        //directoryPath=getFilePath(getContext(),filesize,fileName,j);
        File file = new File(directoryPath);    //创建文件对象
        try {
            file.createNewFile();    //创建一个新文件
            FileOutputStream fileOS = new FileOutputStream(file);    //创建一个文件输出流对象
            cacheBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOS);    //将绘图内容压缩为PNG格式输出到输出流对象中
            fileOS.flush();    //将缓冲区中的数据全部写出到输出流中
            fileOS.close();    //关闭文件输出流对象
            Toast.makeText(getContext(), "成功保存到" + directoryPath, Toast.LENGTH_SHORT).show();//垴村成功，提示保存的路径
        } catch (Exception e) {
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();//保存失败，提示原因
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        getContext().sendBroadcast(intent);//这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片了！，记得要传你更新的file哦
    }

    public static String getFilePath(Context context, String filesize, String dir, int j) {  //获取APP当前目录并且设置图片保存路径
        String directoryPath = "";
        if (j == 0) {
            //判断SD卡是否可用
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                directoryPath = context.getExternalFilesDir(filesize).getAbsolutePath() + File.separator + dir + ".png";

                // directoryPath =context.getExternalCacheDir().getAbsolutePath() ;
            } else {
                //没内存卡就存机身内存
                directoryPath = context.getFilesDir().getAbsolutePath()
                        + File.separator
                        + filesize + File.separator
                        + dir + ".png";

                // directoryPath=context.getCacheDir()+File.separator+dir;
            }
        } else {
            directoryPath = Environment.getExternalStorageDirectory()
                    + File.separator +
                    Environment.DIRECTORY_DCIM
                    + File.separator + "Camera" + File.separator + dir + ".png";
            //	File file = new File(directoryPath);
            //if(!file.exists()){//判断文件目录是否存在
            //	file.mkdirs();
            //directoryPath=directoryPath+File.separator+dir + ".png";
        }
        //LogUtil.i("filePath====>"+directoryPath);
        //}
        return directoryPath;
    }


}