package per.hxl.stewardtokgo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import per.hxl.stewardtokgo.R;
import per.hxl.stewardtokgo.utils.ConstantValue;
import per.hxl.stewardtokgo.utils.SPutil;
import per.hxl.stewardtokgo.view.SettingItemView;


public class SettingActivity extends AppCompatActivity {

    private SettingItemView siv_timesw = null;
    private SettingItemView siv_prompttone = null;
    private SettingItemView siv_applock = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initUI();
        initPrompttone();
        initTimeSW();
        initapplock();
    }

    private void initTimeSW() {

        //临时作为
        siv_timesw = (SettingItemView) findViewById(R.id.setting_timesw);
        siv_timesw.setCheck(SPutil.getBoolean(this, ConstantValue.SHOWSUSPENSIONWINDOW,false));
        siv_timesw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ischeck = siv_timesw.isCheck();
                SPutil.putBoolean(getBaseContext(),ConstantValue.SHOWSUSPENSIONWINDOW,!ischeck);
                siv_timesw.setCheck(!ischeck);
            }
        });
    }

    //初始化是否打开提示音
    private void initPrompttone() {
       siv_prompttone = (SettingItemView) findViewById(R.id.setting_prompt);
        siv_prompttone.setCheck(SPutil.getBoolean(this, ConstantValue.OPENPROMPTTONE,false));
        siv_prompttone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ischeck = siv_prompttone.isCheck();
                SPutil.putBoolean(getBaseContext(),ConstantValue.OPENPROMPTTONE,!ischeck);
                siv_prompttone.setCheck(!ischeck);
            }
        });
    }

    //初始化是否打开程序锁
    private void initapplock() {
        siv_applock = (SettingItemView) findViewById(R.id.setting_applock);
        siv_applock.setCheck(SPutil.getBoolean(this, ConstantValue.OPENCLOSEAPPLOCK,false));
        Check1Enable();
        siv_applock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ischeck = siv_applock.isCheck();
                SPutil.putBoolean(getBaseContext(),ConstantValue.OPENCLOSEAPPLOCK,!ischeck);
                siv_applock.setCheck(!ischeck);
                Check1Enable();
            }
        });
    }
    private void Check1Enable(){

        //TODO TOKGO
//        if (siv_applock.isCheck()){
//            startService(new Intent(this,LockService.class));
//            siv_timesw.setEnabled(true);
//            siv_prompttone.setEnabled(true);
//        }else {
//            //关闭服务
//            stopService(new Intent(getBaseContext(), LockService.class));
//            siv_timesw.setEnabled(false);
//            siv_prompttone.setEnabled(false);
//        }
    }


    private void initUI() {

    }
}
