package per.hxl.stewardtokgo.Task;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import static per.hxl.stewardtokgo.utils.ConstantValue.BUGTAG;

/**
 * Created by TOKGO on 2017/9/11.
 */

public class TaskService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        new taskThread().start();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
//        return super.onStartCommand(intent, flags, startId);
    }

    class taskThread extends Thread{
    @Override
    public void run() {
        while (true){
            getTopApp(TaskService.this);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}

        }
    }
}


    private void getTopApp(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager m = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            if (m != null) {
                long now = System.currentTimeMillis();
                //获取60秒之内的应用数据
                List<UsageStats> stats = m.queryUsageStats(UsageStatsManager.INTERVAL_BEST, now - 60 * 1000, now);
                Log.i(BUGTAG, "Running app number in last 60 seconds : " + stats.size());
                String topActivity = "";
                //取得最近运行的一个app，即当前运行的app
                if ((stats != null) && (!stats.isEmpty())) {
                    int j = 0;
                    for (int i = 0; i < stats.size(); i++) {
                        if (stats.get(i).getLastTimeUsed() > stats.get(j).getLastTimeUsed()) {
                            j = i;
                        }
                    }
                    topActivity = stats.get(j).getPackageName();
                }
                Log.i(BUGTAG, "top running app is : "+topActivity);
            }
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
