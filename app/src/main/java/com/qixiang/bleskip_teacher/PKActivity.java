package com.qixiang.bleskip_teacher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class PKActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pk);

        findViewById(R.id.ib_pk_back).setOnClickListener(this);
        //setFullScreen();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_pk_back:
                finish();
                break;
        }
    }
}
