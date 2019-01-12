package per.hxl.stewardtokgo.Net;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import per.hxl.stewardtokgo.utils.ConstantValue;
import per.hxl.stewardtokgo.utils.SPutil;

public class HeartbeartService extends Service {

    private WsManager wsManager;

    @Override
    public void onCreate() {
        super.onCreate();
        //身份验证

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
