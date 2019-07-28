package per.hxl.stewardtokgo.Activity;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import per.hxl.stewardtokgo.Chat.ChatUIActivity;
import per.hxl.stewardtokgo.Login.LoginActivity;
import per.hxl.stewardtokgo.Login.LoginUtil;
import per.hxl.stewardtokgo.R;
import per.hxl.stewardtokgo.Setting.ServerURLActivity;
import per.hxl.stewardtokgo.utils.ConstantValue;
import per.hxl.stewardtokgo.utils.SPutil;

public class SplashActivity extends AppCompatActivity {

    private boolean IsReady = false;
    private final List<String>  allowlist = new ArrayList<>();
    private List<String> forbidlist ;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (!IsReady){
                finish();
                return;
            }
            Intent intent;
            switch (msg.what){
                case 0:intent = new Intent(SplashActivity.this,MainActivity.class);
                    break;
                case 1:intent = new Intent(SplashActivity.this,LoginActivity.class);
                    break;
                default:intent = new Intent(SplashActivity.this,LoginActivity.class);
            }
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setServerAdrr();
        setContentView(R.layout.activity_splash);
        initSystemService();
        new DeayTime().start();
    }

    private void setServerAdrr() {
        try {
            String spURLStr = SPutil.getString(getBaseContext(), ConstantValue.SERVERADRR_LIST_STR,"");
            List<String> mRULStr = JSONObject.parseArray(spURLStr,String.class);
            int pos = SPutil.getint(getBaseContext(), ConstantValue.SERVERADRR_POS,0);
            if (mRULStr != null || mRULStr.size() != 0){
                ConstantValue.SERVERADRR = mRULStr.get(pos);
            }
        }catch (Exception e){ }
    }

    /**
     *  开启系统所需要的服务
     */
    private void initSystemService() {
//        //开启程序锁服务
//        if (SPutil.getBoolean(this, ConstantValue.OPENCLOSEAPPLOCK,false))
//            startService(new Intent(this,LockService.class));


//        if (SPutil.getBoolean(this, ConstantValue.SHOWSUSPENSIONWINDOW,false)){
//            ConstantValue.SERVERADRR = "http://118.24.6.171:6666";
//        }else {
//            ConstantValue.SERVERADRR  ="http://118.24.6.171:1314";
//
//        }


        getPermission();


    }

    private void getPermission() {
        if (!hasPermission()) {
            //若用户未开启权限，则引导用户开启“Apps with usage access”权限
            startActivityForResult(
                    new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
                    MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
        }
        else if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivity(intent);
        }else if (!isIgnoringBatteryOptimizations()){
            gotoSettingIgnoringBatteryOptimizations();
        }
        else {
            IsReady = true;
        }
    }


    private boolean isIgnoringBatteryOptimizations(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            return pm.isIgnoringBatteryOptimizations(packageName);
        }
     return false;
    }


    private void gotoSettingIgnoringBatteryOptimizations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                Intent intent = new Intent();
                String packageName = getPackageName();
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /***
     *检测程序锁的数据库的数据是否正常
     */
    private void CheckLockDB(){
//        List<PackageInfo> packageInfos = getPackageManager().getInstalledPackages(0);
//        for (PackageInfo pi:packageInfos) {
//            //判断是否为非系统预装的应用程序 //排除系统默认应用。
//            if ((pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0)
//                continue;
//            if (allowlist.contains(pi.packageName)) {
//                if (forbidlist.contains(pi.packageName))
//                    LocKForbiddenDB.delete(getBaseContext(), pi.packageName);
//            }
//            else {
//                    if (!forbidlist.contains(pi.packageName))
//                        LocKForbiddenDB.insert(getBaseContext(), pi.packageName);
//                }
//        }
    }



    //检测用户是否对本app开启了“Apps with usage access”权限
    private boolean hasPermission() {
        setPermissions();
        AppOpsManager appOps = (AppOpsManager)
                getSystemService(Context.APP_OPS_SERVICE);
        int mode = 0;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(), getPackageName());
        }
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    private static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 1101;
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS) {
            if (!hasPermission()) {
                //若用户未开启权限，则引导用户开启“Apps with usage access”权限
                startActivityForResult(
                        new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
                        MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
            }

        }
    }
    static final String[] PERMISSION = new String[]{
            Manifest.permission.READ_SMS,
    };

    private void setPermissions() {

        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //Android 6.0申请权限
            ActivityCompat.requestPermissions(this,PERMISSION,1);
        }else{
            Log.i(ConstantValue.BUGTAG,"权限申请ok");
        }
    }



    class DeayTime extends Thread{
        @Override
        public void run() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String account = SPutil.getString(SplashActivity.this, ConstantValue.USER_ACCOUNT, "");
            String password = SPutil.getString(SplashActivity.this, ConstantValue.USER_PASSWORD, "");
            boolean isok = LoginUtil.login(account,password,SplashActivity.this);
            if (isok)
                handler.sendEmptyMessage(0);
            else
                handler.sendEmptyMessage(1);



//            int count =0;
//            while (!IsReady){
//                try {
//
//
//                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
//
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {}
//                count++;
//                if (count>100)
//                    break;
//            }
//            if (count>20)
//                handler.sendEmptyMessage(0);
//            else
//                handler.sendEmptyMessageDelayed(0,2000-count*100);
        }
    }

}
