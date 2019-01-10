package per.hxl.stewardtokgo.Login;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import per.hxl.stewardtokgo.Net.HttpUtil;
import per.hxl.stewardtokgo.utils.ConstantValue;
import per.hxl.stewardtokgo.utils.DigestUtils;
import per.hxl.stewardtokgo.utils.SPutil;

public class LoginUtil {
    private static String loginurl = ConstantValue.SERVERADRR +"/oauth/token";
    private static String authName = "client_app";
    private static String authPassord = "123456";
    private static boolean isFinish = false;
    private static boolean islogin = false;


    public static boolean login(String account, String password){
        return login(account,password,null);
    }

    public static boolean login(String account, String password, final Context context){
        isFinish = false;
        islogin = false;
        //对密码进行md5加密
        password = DigestUtils.md5DigestAsHex((password).getBytes());
        password = DigestUtils.md5DigestAsHex(("tokgo"+password+"md5").getBytes());

        //构建FormBody，传入要提交的参数
        FormBody formBody = new FormBody.Builder()
                .add("username", account)
                .add("password",password)
                .add("grant_type","password")
                .build();
        String credential = Credentials.basic(authName, authPassord);
        final Request request = new Request.Builder()
                .addHeader("Content-Type","application/x-www-form-urlencoded")
                .addHeader("Authorization",credential)
                .url(loginurl)
                .post(formBody).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                if (context !=null)
                    Toast.makeText(context,"http 请求错误，检查url",Toast.LENGTH_SHORT).show();
                isFinish = true;
                islogin = false;
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.code()!=200)
                        islogin = false;
                    else {
                        JSONObject jsonheader = JSONObject.parseObject(response.body().string());
                        String token = jsonheader.getString("access_token");
                        String type = jsonheader.getString("token_type");
                        if (context != null) {
                            SPutil.putString(context, ConstantValue.USER_TOKEN, type + " " + token);
                            HttpUtil.setToken(SPutil.getString(context, ConstantValue.USER_TOKEN, ""));
                        }
                        islogin = true;
                    }
                }catch (Exception e){
                    islogin = false;
                }
                isFinish = true;
            }
        });
        WaitLogin();
        return islogin;
    }

    private static void WaitLogin() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
                isFinish =true;
            }
        }).start();
        while (!isFinish){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }
    }

}
