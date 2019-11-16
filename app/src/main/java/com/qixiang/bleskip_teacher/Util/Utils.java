package com.qixiang.bleskip_teacher.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.qixiang.bleskip_teacher.MainActivity;
import com.qixiang.bleskip_teacher.Model.StuInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.apache.http.util.EncodingUtils;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2018/8/7.
 */

public class Utils {



    public static boolean WXFlag=false;

    public static String openID="";
    public static int ModelFlag = 0;// 1:计时 2：计数
    public static String taskName ="";

    public static int theMaxCount = 0;

    public static String token ="null";
    public static String lsID = "0";//老师ID
    public static String xyID = "0";//学校ID
    public static String bjID = "0";//班级ID

    //用于组网的网络号，（4bytes  那个）
    public static int bjNum = -1;//bjNum
    //保存班级和班级详情信息；
    //public static String[][] ClassInfo = null;

    //保存班级和班级详情信息；
    public static JSONArray ClassInfo = null;

    public static StuInfo[][] jj = null;

    public static JSONObject[][] stuInfo = null;

    public static int selectCount = 0;

    public  static  String TAG = "falter1";
    //标识在班级列表中点击的“班级索引”,用于查看班级详情
    public static int ClassIndex = -1;
    //用于标识“已勾选”的班级标识，进行后面的教学任务
    public static int ClassIndex2 = 0;

    //记录进行任务的所有同学的“在班位置”
    public static int[] stuInClassIndex;

    //数据库名字
    public static String SDLName ="BleSkip_test1";
    //数据库表名（同一时刻只会访问一个班）
    public static String SDL_table_Name ="";

    public static void LogE(String s){
        Log.e("falter1",s);
    }

    public static byte taskNum = -1;
    public static byte GetTaskNum(){
        taskNum++;
        if(taskNum == 0xFF)
            taskNum = 0;
        return taskNum;
    }

    /**
     * 设置用户任务号
     */
    public static void saveMissionCode(int taskCode) {

        //第一个参数  是最终保存的文件名，不用指定文件后缀，因为SharedPreferences这个API默认就是xml格式保存
        //第二个参数是文件操作模式，这里是只能本软件自己访问的私有操作模式
        SharedPreferences preferences = MainActivity.theContext.getSharedPreferences("MissionCode", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("mission", taskCode);
        editor.commit();//把数据提交到文件里，在这之前数据都是存放在内存中
    }
    //得到用户任务号
    public static int GetMissionCode(){
        SharedPreferences preferences = MainActivity.theContext.getSharedPreferences("MissionCode", MODE_PRIVATE);
        int result = preferences.getInt("mission",0);
        result++;
        if(result == 127){
            result = 1;
        }
        saveMissionCode(result);
        return result;
    }

    //int 装换成 byte[]
    public static byte[] intToButeArray(int n) {
        byte[] byteArray = null;
        byte[] byteResult = new byte[2];
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            DataOutputStream dataOut = new DataOutputStream(byteOut);
            dataOut.writeInt(n);
            byteArray = byteOut.toByteArray();
            for (byte b : byteArray) {
                System.out.println(" " + b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        byteResult[0] = byteArray[2];
        byteResult[1] = byteArray[3];
        return byteResult;
    }
//    /**
//     * 设置用户任务号
//     */
//    public static void saveIndexCode(int indexCode) {
//
//        //第一个参数  是最终保存的文件名，不用指定文件后缀，因为SharedPreferences这个API默认就是xml格式保存
//        //第二个参数是文件操作模式，这里是只能本软件自己访问的私有操作模式
//        SharedPreferences preferences = MainActivity.theContext.getSharedPreferences("indexCode", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putInt("indexCode", indexCode);
//        editor.commit();//把数据提交到文件里，在这之前数据都是存放在内存中
//    }
//    //得到用户任务号
//    public static int GetIndexCode(){
//        SharedPreferences preferences = MainActivity.theContext.getSharedPreferences("indexCode", Context.MODE_PRIVATE);
//        int result = preferences.getInt("indexCode",0);
//        result++;
//        if(result == 127){
//            result = 1;
//        }
//        saveIndexCode(result);
//        return result;
//    }

    // 将字符串写入到文本文件中
    public static void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath+fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    // 生成文件
    public static File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    // 生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e+"");
        }
    }

    //写文件在./data/data/com.tt/files/下面
    public static void writeFileData(String fileName,String message,Context con){
        try{
            FileOutputStream fout =con.openFileOutput(fileName, MODE_PRIVATE);
            byte [] bytes = message.getBytes();
            fout.write(bytes);
            fout.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    //读文件在./data/data/com.tt/files/下面
    public static String readFileData(Context con,String fileName){
        String res="";
        try{
            FileInputStream fin = con.openFileInput(fileName);
            int length = fin.available();
            byte [] buffer = new byte[length];
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
            fin.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 字节数组转成16进制表示格式的字符串
     *
     * @param byteArray
     *            需要转换的字节数组
     * @return 16进制表示格式的字符串
     **/
    public static String toHexString(byte[] byteArray) {
        if (byteArray == null || byteArray.length < 1)
            throw new IllegalArgumentException("this byteArray must not be null or empty");

        final StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            if ((byteArray[i] & 0xff) < 0x10)//0~F前面不零
                hexString.append("0");
            hexString.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return hexString.toString().toLowerCase();
    }

    /**
     * 将16进制字符串转换为byte[]
     *
     * @param str
     * @return
     */
    public static byte[] toBytes(String str) {
        if(str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }

    public static byte[] hexToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] bytes = new byte[length];
        String hexDigits = "0123456789ABCDEF";
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            int h = hexDigits.indexOf(hexChars[pos]) << 4;
            int l = hexDigits.indexOf(hexChars[pos + 1]);
            if (h == -1 || l == -1) {
                return null;
            }
            bytes[i] = (byte) (h | l);
        }
        return bytes;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
