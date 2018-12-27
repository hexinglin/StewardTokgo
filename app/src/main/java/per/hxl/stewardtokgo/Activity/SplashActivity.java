package per.hxl.stewardtokgo.Activity;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.List;

import per.hxl.stewardtokgo.Login.LoginActivity;
import per.hxl.stewardtokgo.Login.LoginUtil;
import per.hxl.stewardtokgo.R;
import per.hxl.stewardtokgo.Task.TaskService;
import per.hxl.stewardtokgo.utils.ConstantValue;
import per.hxl.stewardtokgo.utils.SPutil;

public class SplashActivity extends AppCompatActivity {

    private boolean IsReady = false;
//    private NetClient netcom;
    private final List<String>  allowlist = new ArrayList<>();
    private List<String> forbidlist ;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
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
        setContentView(R.layout.activity_splash);
        initSystemService();
        new DeayTime().start();
        finish();
    }

    /**
     *  开启系统所需要的服务
     */
    private void initSystemService() {
//        //开启程序锁服务
//        if (SPutil.getBoolean(this, ConstantValue.OPENCLOSEAPPLOCK,false))
//            startService(new Intent(this,LockService.class));
        if (!hasPermission()) {
            //若用户未开启权限，则引导用户开启“Apps with usage access”权限
            startActivityForResult(
                    new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
                    MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
        }
        //开启系统监控及心跳服务
        startService(new Intent(getBaseContext() ,TaskService.class));

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


    class DeayTime extends Thread{
        @Override
        public void run() {
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
