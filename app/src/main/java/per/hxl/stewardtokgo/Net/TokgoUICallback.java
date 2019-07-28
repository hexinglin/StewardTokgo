package per.hxl.stewardtokgo.Net;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import per.hxl.stewardtokgo.Login.LoginUtil;
import per.hxl.stewardtokgo.R;
import per.hxl.stewardtokgo.utils.ConstantValue;
import per.hxl.stewardtokgo.utils.SPutil;

public abstract  class TokgoUICallback extends TokgoCallback {
    public   Activity activity = null;
    public abstract void onResponse(JSONObject responsedata);

    protected TokgoUICallback(Activity activity, String errmsg) {
        super(activity,errmsg);
        this.activity = activity;
    }

    @Override
    public void onResponse(final String responsedata) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonheader = JSONObject.parseObject(responsedata);
                    int code = jsonheader.getInteger("code");
                    if(code ==0 ){
                        JSONObject jsondata = JSONObject.parseObject(jsonheader.getString("data"));
                        onResponse(jsondata);
                        return;
                    }
                    Toast.makeText(activity, jsonheader.getString("msg"), Toast.LENGTH_SHORT).show();
                    return;
                } catch (Exception e) { }
                Toast.makeText(activity, "data structure error ", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
