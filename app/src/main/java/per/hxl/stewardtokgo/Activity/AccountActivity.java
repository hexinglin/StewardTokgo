package per.hxl.stewardtokgo.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.alibaba.fastjson.JSONObject;
import per.hxl.stewardtokgo.Net.HttpUtil;
import per.hxl.stewardtokgo.Net.TokgoCallback;
import per.hxl.stewardtokgo.R;
import per.hxl.stewardtokgo.utils.ConstantValue;
import per.hxl.stewardtokgo.view.InfroItemView;


public class AccountActivity extends AppCompatActivity {

    private String account ="1131515629@qq.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initNet();
    }

    private void initNet() {
        HttpUtil.Get(ConstantValue.SERVERADRR + "/tokgo/account/getinfro?acount=" + account, new TokgoCallback() {
            @Override
            public void onResponse(final String responsedata) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonheader = JSONObject.parseObject(responsedata);
                            if(jsonheader.getInteger("code")==0){
                                JSONObject jsondata = JSONObject.parseObject(jsonheader.getString("data"));
                                ((InfroItemView) findViewById(R.id.account_email)).SetInfro(jsondata.getString("email"));
                                ((InfroItemView) findViewById(R.id.account_nick)).SetInfro(jsondata.getString("nick"));
                                ((InfroItemView) findViewById(R.id.account_xcoin)).SetInfro(jsondata.getString("xcoin"));
                            }
                            else {
                                Toast.makeText(AccountActivity.this,jsonheader.getInteger("msg"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

}
