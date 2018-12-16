package per.hxl.stewardtokgo.Net;

import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {

    private HttpUtil(){}

    public static void Get(String url,final TokgoCallback tokgoCallback){
        try {
            Request request = new Request.Builder().get().url(url).build();
            new OkHttpClient().newCall(request).enqueue(tokgoCallback);
        }catch (Exception  e){
            if (tokgoCallback.context !=null)
                Toast.makeText(tokgoCallback.context,"http 请求错误，检查url",Toast.LENGTH_SHORT).show();
        }

    }

    public static void Put(String url, FormBody formBody, final TokgoCallback tokgoCallback){
        try {
            final Request request = new Request.Builder()
                    .url(url)
                    .put(formBody).build();
            new OkHttpClient().newCall(request).enqueue(tokgoCallback);
        }catch (Exception  e){
            if (tokgoCallback.context !=null)
                Toast.makeText(tokgoCallback.context,"http 请求错误，检查url",Toast.LENGTH_SHORT).show();
        }
    }



}
