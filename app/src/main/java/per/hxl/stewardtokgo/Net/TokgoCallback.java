package per.hxl.stewardtokgo.Net;

import android.content.Context;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import per.hxl.stewardtokgo.Activity.SplashActivity;
import per.hxl.stewardtokgo.Login.LoginUtil;
import per.hxl.stewardtokgo.utils.ConstantValue;
import per.hxl.stewardtokgo.utils.SPutil;

public abstract class TokgoCallback implements Callback {
    public   Context context = null;
    private  String errmsg = null;
    private boolean isRequest = false;

    public abstract void onResponse(String responsedata);

    protected TokgoCallback() {
    }
    protected TokgoCallback(Context context,String errmsg) {
        this.context = context;
        this.errmsg = errmsg;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if (context !=null)
            Toast.makeText(this.context,this.errmsg,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (response.code()== 401 && !isRequest) {//没有权限
            Login(call, response);
            return;
        }
        onResponse(response.body().string());
        isRequest = true;
    }

    private void Login(Call call, Response response){
        //重新登录获取权限
        if (context !=null) {
            String account = SPutil.getString(context, ConstantValue.USER_ACCOUNT, "");
            String password = SPutil.getString(context, ConstantValue.USER_PASSWORD, "");
            boolean isok = LoginUtil.login(account, password, context);
            if (isok)
                Toast.makeText(this.context,"已经获取权限，请重新请求",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this.context,"获取权限失败，请重新登陆",Toast.LENGTH_SHORT).show();
        }
        //重新请求
        isRequest = true;
    }


}
