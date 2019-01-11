package per.hxl.stewardtokgo.Net;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import per.hxl.stewardtokgo.utils.ConstantValue;

public class HeartbeartService extends Service {

    private WsManager wsManager;

    @Override
    public void onCreate() {
        super.onCreate();
        wsManager = new WsManager.Builder(getBaseContext()).client(
                new OkHttpClient().newBuilder()
                        .pingInterval(15, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .build())
                .needReconnect(true)
                .wsUrl("ws://"+ConstantValue.SERVERADRR.split("//")[1]+"/websocket")
                .build();
        wsManager.startConnect();

    }

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
