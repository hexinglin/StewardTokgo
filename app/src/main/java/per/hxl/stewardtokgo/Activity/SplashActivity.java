package per.hxl.stewardtokgo.Activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.List;

import per.hxl.stewardtokgo.R;
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
            initSystemService();
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initNet();
//        new DeayTime().start();
        startActivity(new Intent(SplashActivity.this,MainActivity.class));
        finish();
    }

    /**
     *  开启系统所需要的服务
     */
    private void initSystemService() {
//        //开启程序锁服务
//        if (SPutil.getBoolean(this, ConstantValue.OPENCLOSEAPPLOCK,false))
//            startService(new Intent(this,LockService.class));
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


    private void initNet() {


    }
    class DeayTime extends Thread{
        @Override
        public void run() {
            int count =0;
            while (!IsReady){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {}
                count++;
                if (count>100)
                    break;
            }
            if (count>20)
                handler.sendEmptyMessage(0);
            else
                handler.sendEmptyMessageDelayed(0,2000-count*100);
        }
    }

}
