package per.hxl.stewardtokgo.Task;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import okhttp3.FormBody;
import per.hxl.stewardtokgo.Net.HttpUtil;
import per.hxl.stewardtokgo.Net.TokgoCallback;
import per.hxl.stewardtokgo.utils.ConstantValue;


public class SMSContentObserver extends ContentObserver {
    public static final Uri SMS_INBOX_URL = Uri.parse("content://sms/inbox");
    private Context mContext;
    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {


        }
    };

    private String lastDate = "0";

    public SMSContentObserver(Context context) {
        super(mHandler);
        mContext = context;
        CheckSMS();
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        CheckSMS();
    }


    public void CheckSMS(){
        Cursor c = mContext.getContentResolver().query(SMS_INBOX_URL, new String[] {"thread_id",
                        "date", "address", "body" }, "address=? and date>?",
                new String[] { "95555" ,lastDate} , "date asc"); //按日期【排序  最后条记录应该在最上面  desc 从大到小    asc小到大
        if (c != null) {
            String date ="0";
            while(c.moveToNext()) {
                Log.d("MSMSM",c.getString(c.getColumnIndex("thread_id")));
                Log.d("MSMSM",c.getString(c.getColumnIndex("address")));
                Log.d("MSMSM",c.getString(c.getColumnIndex("date")));
                Log.d("MSMSM",c.getString(c.getColumnIndex("body")));
                date = c.getString(c.getColumnIndex("date"));
                String body = c.getString(c.getColumnIndex("body"));
                //解析内容
                decodeBody(body);
            }
            if (!"0".equals(date)){
                lastDate = date;
                SendValue(date);

            }
            c.close();
        }
    }

    private void SendValue(String date) {
        //构建FormBody，传入要提交的参数
        FormBody formBody = new FormBody.Builder()
                .add("valueStores", "").build();
        HttpUtil.Put(ConstantValue.SERVERADRR + "/tokgo/account/putValue", formBody
                , new TokgoCallback(mContext, "put relearn error") {
                    @Override
                    public void onResponse(final String responsedata) {
                        try {
                            JSONObject jsonheader = JSONObject.parseObject(responsedata);

                        } catch (Exception e) {
                            Toast.makeText(mContext, "data structure error ", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void decodeBody(String body) {

    }

    /*
    　　_id：          短信序号，如100
　　thread_id：对话的序号，如100，与同一个手机号互发的短信，其序号是相同的
　　address：  发件人地址，即手机号，如+86138138000
　　person：   发件人，如果发件人在通讯录中则为具体姓名，陌生人为null
　　date：       日期，long型，如1346988516，可以对日期显示格式进行设置
　　protocol： 协议0SMS_RPOTO短信，1MMS_PROTO彩信
　　read：      是否阅读0未读，1已读
　　status：    短信状态-1接收，0complete,64pending,128failed
　　type：       短信类型1是接收到的，2是已发出
　　body：      短信具体内容
　　service_center：短信服务中心号码编号，如+8613800755500 
    *
    * */

}