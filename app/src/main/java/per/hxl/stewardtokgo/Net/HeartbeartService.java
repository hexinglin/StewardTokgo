package per.hxl.stewardtokgo.Net;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import per.hxl.stewardtokgo.Activity.MainActivity;
import per.hxl.stewardtokgo.utils.ConstantValue;
import per.hxl.stewardtokgo.utils.SPutil;

public class HeartbeartService extends Service {

    private WsManager wsManager;

    @Override
    public void onCreate() {
        super.onCreate();
        //身份验证
        Notification notification = new Notification();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        //把该service创建为前台service
        startForeground(1, notification);

        wsManager = new WsManager.Builder(getBaseContext()).client(
                new OkHttpClient().newBuilder()
                        .pingInterval(15, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .build())
                .needReconnect(true)
                .wsUrl("ws://"+ConstantValue.SERVERADRR.split("//")[1]+"/websocket")
                .build();
        wsManager.startConnect();
        hbThread.start();

    }
    private Integer SLEEPTIME = 1000*60*5;

    private Thread hbThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (wsManager!=null){
                String account = SPutil.getString(HeartbeartService.this, ConstantValue.USER_ACCOUNT, "");
                wsManager.sendMessage("heartbeat\r\n"+account);
                try {
                    Thread.sleep(SLEEPTIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wsManager !=null){
            wsManager.stopConnect();
            wsManager = null;
        }
    }
}
