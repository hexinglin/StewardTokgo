package per.hxl.stewardtokgo.Net;

import android.widget.Toast;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import per.hxl.stewardtokgo.utils.ConstantValue;
import per.hxl.stewardtokgo.utils.SPutil;

public class HttpUtil {

    private static String token = "";

    private HttpUtil(){}

    public static void Get(String url,final TokgoCallback tokgoCallback){
        try {
            Request request = new Request.Builder()
                    .addHeader("Authorization",getToken(tokgoCallback))
                    .get().url(url).build();

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
                    .addHeader("Authorization",getToken(tokgoCallback))
                    .put(formBody).build();
            new OkHttpClient().newCall(request).enqueue(tokgoCallback);
        }catch (Exception  e){
            if (tokgoCallback.context !=null)
                Toast.makeText(tokgoCallback.context,"http 请求错误，检查url",Toast.LENGTH_SHORT).show();
        }
    }



    public static String getToken(TokgoCallback tokgoCallback) {
        if (tokgoCallback.context!= null)
            token = SPutil.getString(tokgoCallback.context,ConstantValue.USER_TOKEN,"");
        return token;
    }

    public static void setToken(String stoken) {
        token = stoken;
    }
}
