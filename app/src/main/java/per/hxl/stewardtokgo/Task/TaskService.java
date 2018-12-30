package per.hxl.stewardtokgo.Task;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import okhttp3.FormBody;
import per.hxl.stewardtokgo.Net.HttpUtil;
import per.hxl.stewardtokgo.Net.TokgoCallback;
import per.hxl.stewardtokgo.utils.ConstantValue;

import static per.hxl.stewardtokgo.utils.ConstantValue.BUGTAG;

/**
 * Created by TOKGO on 2017/9/11.
 */

public class TaskService extends Service {

    private static int  DEALYTIME_MS = 1000; //单位毫秒

    private static int  HB_COUNT = (10*60*1000)/DEALYTIME_MS; //心跳时间10分钟
    private static String HB_URL= ConstantValue.SERVERADRR + "/tokgo/Account/heartbeat";

    private ContentObserver mObserver;





    @Override
    public void onCreate() {
        super.onCreate();
        new taskThread().start();

        mObserver = new SMSContentObserver(TaskService.this);
        getContentResolver().registerContentObserver(SMSContentObserver.SMS_INBOX_URL, true, mObserver);

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
//        return super.onStartCommand(intent, flags, startId);
    }

    class taskThread extends Thread{
    @Override
    public void run() {
        int HB_NO = 0;
        while (true){
            //检查及设置心跳
            HB_NO = CheckHB(HB_NO+1);
//            mObserver.c
            getTopApp(TaskService.this);
            try {
                Thread.sleep(DEALYTIME_MS);
            } catch (InterruptedException e) {}
        }
    }


    }

    private int CheckHB(int no) {
        if (no < HB_COUNT)
            return no;
        FormBody formBody = new FormBody.Builder().build();
        HttpUtil.Put(HB_URL, formBody, new TokgoCallback(TaskService.this,"hb error") {
            @Override
            public void onResponse(String responsedata) {

            }
        });
        return 0;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        //这个时候就取消这个内容观察者,
        getContentResolver().unregisterContentObserver(mObserver);
    }
}
