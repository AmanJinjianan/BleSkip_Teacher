package com.qixiang.bleskip_teacher.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.qixiang.bleskip_teacher.LoginActivity;
import com.qixiang.bleskip_teacher.R;
import com.qixiang.bleskip_teacher.Util.Utils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by Administrator on 2018/10/11.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("savedInstanceState"," 5555555555555555555555555555555555555555555555");

        //注册API
        api = WXAPIFactory.createWXAPI(this, "wxe571406b7f0d80e3");
        api.handleIntent(getIntent(), this);
        Log.e("savedInstanceState"," sacvsa"+api.handleIntent(getIntent(), this));
    }
    @Override
    public void onReq(BaseReq baseReq) {
        Log.e("newRespnewResp","   code22    :"+baseReq.openId);
    }
    //  发送到微信请求的响应结果
    @Override
    public void onResp(BaseResp resp) {

        Log.e("newRespnewResp","   code1    :"+resp.openId);

        try{
            Utils.openID =((WXLaunchMiniProgram.Resp)resp).extMsg;
            SharedPreferences sp = getSharedPreferences("skiprope_demo", Context.MODE_PRIVATE);
            sp.edit().putString("openid", Utils.openID).commit();

            Utils.WXFlag = true;
        }catch (Exception e){

        }
        //Utils.openID = resp.e;
        //Utils.openID = resp.openId;
        Intent intent = new Intent(WXEntryActivity.this, LoginActivity.class);
        //Intent intent = new Intent();
        //intent.setAction("com.qixiang.bleskip_teacher.tologin");
        startActivity(intent);
        if(resp instanceof SendAuth.Resp){
            SendAuth.Resp newResp = (SendAuth.Resp) resp;
            //获取微信传回的code
            String code = newResp.code;
            Log.i("newRespnewResp","   code2    :"+code);


        }

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                Log.i("savedInstanceState","发送成功ERR_OKERR_OK");
                //发送成功
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Log.i("savedInstanceState","发送取消ERR_USER_CANCEL");
                //发送取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Log.i("savedInstanceState","发送取消ERR_AUTH_DENIEDERR_AUTH_DENIEDERR_AUTH_DENIED");
                //发送被拒绝
                break;
            default:
                Log.i("savedInstanceState","发送返回breakbreakbreak");
                //发送返回
                break;
        }
        //finish();

    }

}
