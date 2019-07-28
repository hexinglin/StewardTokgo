package per.hxl.stewardtokgo.Task;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.Color;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import per.hxl.stewardtokgo.Activity.MainActivity;
import per.hxl.stewardtokgo.Net.HttpUtil;
import per.hxl.stewardtokgo.Net.TokgoCallback;
import per.hxl.stewardtokgo.R;
import per.hxl.stewardtokgo.utils.ConstantValue;

/**
 * Created by TOKGO on 2017/9/11.
 */

public class TaskService extends Service {

    private static int  DEALYTIME_MS = 1000; //单位毫秒

    private static int  HB_COUNT = (10*60*1000)/DEALYTIME_MS; //心跳时间10分钟
    private static String HB_URL= ConstantValue.SERVERADRR + "/tokgo/account/heartbeat";



    private ContentObserver mObserver;

    private String lastActivity="";
    private String nowActivity="";

    Map<String,ApplicationInfo> applicationInfoMap = new HashMap<>();



    @Override
    public void onCreate() {
        super.onCreate();
        new taskThread().start();
        OpenTaskService();

        mObserver = new SMSContentObserver(TaskService.this);
        getContentResolver().registerContentObserver(SMSContentObserver.SMS_INBOX_URL, true, mObserver);

    }

    private void OpenTaskService() {
        String CHANNEL_ONE_ID = "CHANNEL_ONE_ID";
        String CHANNEL_ONE_NAME= "CHANNEL_ONE_ID";
        NotificationChannel notificationChannel= null;
        //进行8.0的判断
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel= new NotificationChannel(CHANNEL_ONE_ID,
                    CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
        }
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent= PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification= new Notification.Builder(this).setChannelId(CHANNEL_ONE_ID)
                .setTicker("Nature")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("星梦管家后台运行中")
                .setContentIntent(pendingIntent)
                .build();
        notification.flags|= Notification.FLAG_NO_CLEAR;
        startForeground(1, notification);
    }

    class taskThread extends Thread{
    @Override
    public void run() {
        int HB_NO = 0;
        while (true){
            //检查及设置心跳
            HB_NO = CheckHB(HB_NO+1);
            getTopApp(TaskService.this);
            dealTopActivity();
            try {
                Thread.sleep(DEALYTIME_MS);
            } catch (InterruptedException e) {}
        }
    }

    }

    private void dealTopActivity() {
        ApplicationInfo applicationInfo = applicationInfoMap.get(nowActivity);
        if(applicationInfo == null)
            return ;
        applicationInfo.addCount();


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
                lastActivity = nowActivity;
                nowActivity ="";
                //取得最近运行的一个app，即当前运行的app
                if ((stats != null) && (!stats.isEmpty())) {
                    int j = 0;
                    for (int i = 0; i < stats.size(); i++) {
                        if (stats.get(i).getLastTimeUsed() > stats.get(j).getLastTimeUsed()) {
                            j = i;
                        }
                    }
                    nowActivity = stats.get(j).getPackageName();
                }
                if ("".equals(nowActivity))
                    nowActivity = lastActivity;
//                Log.i(ConstantValue.BUGTAG, "top running app is : "+nowActivity);
            }
        }
    }

    public void addApplication(String application){
        ApplicationInfo applicationInfo = new ApplicationInfo(application);
        applicationInfoMap.put(application,applicationInfo);
    }


    public void addApplication(String application,PopupWindow popupWindow){
        ApplicationInfo applicationInfo = new ApplicationInfo(application,popupWindow);
        applicationInfoMap.put(application,applicationInfo);
    }

    public void addApplication(String application,Integer maxCount){
        ApplicationInfo applicationInfo = new ApplicationInfo(application,maxCount);
        applicationInfoMap.put(application,applicationInfo);
    }

    public int getResult(String application){
        ApplicationInfo applicationInfo = applicationInfoMap.get(application);
        if(applicationInfo == null)
            return 0;
        return applicationInfo.getCount();
    }




    @Override
    public IBinder onBind(Intent intent) {
        return new MsgBinder();
    }

    public class MsgBinder extends Binder {
        /**
         * 获取当前Service的实例
         * @return
         */
        public TaskService getService(){
            return TaskService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //这个时候就取消这个内容观察者,
        getContentResolver().unregisterContentObserver(mObserver);
    }

    class ApplicationInfo {
        private String applicationName ="";
        private int count = 0;
        private int maxcount = 0;
        private boolean isContinuous = false;
        private PopupWindow popupWindow = null;


        public ApplicationInfo(String applicationName) {
            this.applicationName = applicationName;
            this.isContinuous = false;
        }
        public ApplicationInfo(String applicationName,PopupWindow popupWindow) {
            this.applicationName = applicationName;
            this.isContinuous = false;
            this.popupWindow = popupWindow;
        }

        public ApplicationInfo(String applicationName, int maxcount) {
            this.applicationName = applicationName;
            this.isContinuous = true;
            this.maxcount = maxcount;
        }

        public int getCount() {
            return count;
        }

        public void addCount(){
            if (!applicationName.equals(nowActivity))
                return;
            if (isContinuous){
                if (nowActivity.equals(lastActivity) && applicationName.equals(nowActivity)){
                    count++;
                }else {
                    if (count < maxcount){
                        count = 0;
                    }
                }
            }
            else {
                count++;
            }
            if (popupWindow != null) {
                popupWindow.setInfro(count);
            }
        }
    }

}

