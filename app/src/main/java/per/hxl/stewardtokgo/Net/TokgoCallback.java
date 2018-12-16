package per.hxl.stewardtokgo.Net;

import android.content.Context;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class TokgoCallback implements Callback {
    public   Context context = null;
    private  String errmsg = null;

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
        onResponse(response.body().string());
    }


}
