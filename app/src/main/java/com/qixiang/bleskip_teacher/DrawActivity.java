package com.qixiang.bleskip_teacher;

/**
 * Created by Administrator on 2019/10/6.
 */

import android.Manifest;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.qixiang.bleskip_teacher.MyView.DrawView;

public class DrawActivity extends Activity implements View.OnClickListener{
    static int i = 0, j = 0;

    public DrawView dv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        this.getFilesDir();

        String[] mPermission = new String[]{
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };
        ActivityCompat.requestPermissions(this, mPermission, 1);

        findViewById(R.id.ib_draw_back).setOnClickListener(this);
        findViewById(R.id.ibtn_clear).setOnClickListener(this);
        findViewById(R.id.ibtn_clearall).setOnClickListener(this);
        findViewById(R.id.ibtn_draw).setOnClickListener(this);
        //PermisionUtils.verifyStoragePermissions(this);   //获取手机SD卡读取权限


         dv = (DrawView) findViewById(R.id.drawView1);    //获取自定义的绘图视图
    }

    // 创建选项菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = new MenuInflater(this);//实例化一个MenuInflater对象
        inflator.inflate(R.menu.toolsmenu, menu);    //解析菜单文件
        return super.onCreateOptionsMenu(menu);
    }

    // 当菜单项被选择时，作出相应的处理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        dv.paint.setXfermode(null);        //取消擦除效果
        dv.paint.setStrokeWidth(35);        //初始化画笔的宽度
        switch (item.getItemId()) {
            case R.id.red:
                dv.paint.setColor(Color.RED);    //设置画笔的颜色为红色
                item.setChecked(true);
                break;
            case R.id.green:
                dv.paint.setColor(Color.GREEN);    //设置画笔的颜色为绿色
                item.setChecked(true);
                break;
            case R.id.blue:
                dv.paint.setColor(Color.BLUE);    //设置画笔的颜色为蓝色
                item.setChecked(true);
                break;
            case R.id.width_1:
                dv.paint.setStrokeWidth(50);    //设置笔触的宽度为1像素
                break;
            case R.id.width_2:
                dv.paint.setStrokeWidth(10);    //设置笔触的宽度为5像素
                break;
            case R.id.width_3:
                dv.paint.setStrokeWidth(15);//设置笔触的宽度为10像素
                break;
            case R.id.clear:
                dv.clear();        //橡皮檫
                break;
            case R.id.back:
                dv.back();        //返回上一步
                break;
            case R.id.nothing:
                dv.nothing();        //清屏
                //DrawView dv1 = (DrawView) findViewById(R.id.drawView1);
                break;
            case R.id.save: {
                j = 0;
                dv.save(i, j);
                i++;    //保存绘画到APP目录
                break;
            }
            case R.id.save_camera: {
                j = 1;
                dv.save(i, j);
                i++;
                if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))//若是获取权限失败，提示手动获取
                    Toast.makeText(DrawActivity.this, "获取权限失败，请手动授权", Toast.LENGTH_SHORT).show();
                break;
            }//保存绘画到相册

        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_draw_back:
                finish();
            break;
            case R.id.ibtn_clear:
                dv.clear(); //橡皮檫
                break;
            case R.id.ibtn_clearall:
                dv.nothing();        //清屏
                break;
            case R.id.ibtn_draw:
                dv.paint.setXfermode(null);        //取消擦除效果
                dv.paint.setStrokeWidth(50);        //初始化画笔的宽度
                break;

        }
    }
}