package per.hxl.stewardtokgo.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

import okhttp3.FormBody;
import per.hxl.stewardtokgo.Net.HttpUtil;
import per.hxl.stewardtokgo.Net.TokgoCallback;
import per.hxl.stewardtokgo.R;
import per.hxl.stewardtokgo.utils.ConstantValue;
import per.hxl.stewardtokgo.utils.SPutil;

public class MemoryActivity extends AppCompatActivity {

    private String content ="";
    private Integer contentid =0;
    private EditText et_input;
    private TextView tv_content;
    private TextView tv_content_show;
    private Button btn_sub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);
        initUI();
        getContent();
    }

    private void getContent() {
        HttpUtil.Get(ConstantValue.SERVERADRR + "/tokgo/memory/todaylearnone"
                , new TokgoCallback(getBaseContext(), "get memory error") {
                    @Override
                    public void onResponse(final String responsedata) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonheader = JSONObject.parseObject(responsedata);
                                    int code = jsonheader.getInteger("code");
                                    if(code ==0 ){
                                        JSONObject jsondata = JSONObject.parseObject(jsonheader.getString("data"));
                                        contentid = jsondata.getInteger("id");
                                        content = jsondata.getString("content");
                                        return;
                                    }else if(code == 20001){
                                        AlertDialog.Builder builder  = new AlertDialog.Builder(MemoryActivity.this);
                                        builder.setTitle("提示" ) ;
                                        builder.setMessage("今日的学习任务已经完成了" ) ;
                                        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                btn_sub.setEnabled(false);
                                                tv_content_show.setEnabled(false);
                                            }
                                        });
                                        builder.show();
                                        return;
                                    }
                                    Toast.makeText(getBaseContext(), jsonheader.getString("msg"), Toast.LENGTH_SHORT).show();

                                } catch (Exception e) { }
                                Toast.makeText(getBaseContext(), "data structure error ", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    private void initUI() {
        et_input = findViewById(R.id.memory_sub_content);
        tv_content = findViewById(R.id.memory_content);
        btn_sub =  findViewById(R.id.memory_sub);
        tv_content_show = findViewById(R.id.memory_content_show);
        tv_content_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_content.setText(content);
                SPutil.putlong(getBaseContext(),ConstantValue.MEMORYBEGINTIME,new Date().getTime());
                findViewById(R.id.memory_content_show).setVisibility(View.GONE);
            }
        });

        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckInput()){
                    HttpUtil.Put(ConstantValue.SERVERADRR + "/tokgo/memory/finish/"+contentid
                            , new TokgoCallback(getBaseContext(), "put error") {
                                @Override
                                public void onResponse(final String responsedata) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                JSONObject jsonheader = JSONObject.parseObject(responsedata);
                                                int code = jsonheader.getInteger("code");
                                                if(code ==0 ){
                                                    AlertDialog.Builder builder  = new AlertDialog.Builder(MemoryActivity.this);
                                                    builder.setTitle("提示" ) ;
                                                    builder.setMessage("今日的学习任务已经完成了" ) ;
                                                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            finish();
                                                        }
                                                    });
                                                    builder.show();
                                                    return;
                                                }
                                                Toast.makeText(getBaseContext(), jsonheader.getString("msg"), Toast.LENGTH_SHORT).show();

                                            } catch (Exception e) { }
                                            Toast.makeText(getBaseContext(), "data structure error ", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                }
            }
        });

        findViewById(R.id.memory_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String iputcontent = et_input.getText().toString().trim();
                if (iputcontent.length()>1){
                    //构建FormBody，传入要提交的参数
                    FormBody formBody = new FormBody.Builder()
                            .add("content", iputcontent)
                            .build();
                    HttpUtil.Post(ConstantValue.SERVERADRR + "/tokgo/memory/add", formBody, new TokgoCallback() {
                        @Override
                        public void onResponse(final String responsedata) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject jsonheader = JSONObject.parseObject(responsedata);
                                        int code = jsonheader.getInteger("code");
                                        if(code ==0 ){
                                            Toast.makeText(getBaseContext(), "添加成功", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        Toast.makeText(getBaseContext(), jsonheader.getString("msg"), Toast.LENGTH_SHORT).show();

                                    } catch (Exception e) { }
                                    Toast.makeText(getBaseContext(), "data structure error ", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }});
                }else {
                    Toast.makeText(getBaseContext(), "你还没有输入内容", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private boolean CheckInput() {
        //检查时间
        long nowtime = new Date().getTime();
        long time = SPutil.getlong(getBaseContext(),ConstantValue.MEMORYBEGINTIME,nowtime);
        if (Math.abs(time-nowtime)<1000*60*30) {//半个小时
            Toast.makeText(getBaseContext(), "时间还未到", Toast.LENGTH_SHORT).show();
            return false;
        }


        String iputcontent = et_input.getText().toString().trim();
        String tempcontent = content;
        iputcontent.replaceAll("[\\p{P}+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]" , "");
        tempcontent.replaceAll("[\\p{P}+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]" , "");
        if (tempcontent.equals(iputcontent)){
            return true;
        }
        Toast.makeText(getBaseContext(), "输入内容不对", Toast.LENGTH_SHORT).show();
        return false;
    }


}
