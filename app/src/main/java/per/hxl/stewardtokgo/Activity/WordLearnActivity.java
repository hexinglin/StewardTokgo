package per.hxl.stewardtokgo.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import okhttp3.FormBody;
import per.hxl.stewardtokgo.Net.HttpUtil;
import per.hxl.stewardtokgo.Net.TokgoCallback;
import per.hxl.stewardtokgo.R;
import per.hxl.stewardtokgo.utils.ConstantValue;

public class WordLearnActivity extends AppCompatActivity {

    private Integer wordid;
    private boolean isRlearn;
    private Button btn_relearn;
    private Button btn_ok;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_learn);
        inintBtn();
        //获得单词
        getWord();

    }

    private void inintBtn() {
        btn_ok = (Button)findViewById(R.id.wl_btn_ok);
        btn_relearn = (Button)findViewById(R.id.wl_btn_relearn);
        btn_relearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //构建FormBody，传入要提交的参数
                FormBody formBody = new FormBody.Builder()
                        .add("id", wordid.toString()).build();
                HttpUtil.Put(ConstantValue.SERVERADRR + "/tokgo/word/relearn", formBody
                        , new TokgoCallback(WordLearnActivity.this, "put relearn error") {
                    @Override
                    public void onResponse(final String responsedata) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonheader = JSONObject.parseObject(responsedata);
                                    if (jsonheader.getInteger("code") == 0) {
                                        Toast.makeText(WordLearnActivity.this, jsonheader.getInteger("设置成功"), Toast.LENGTH_SHORT).show();
                                        isRlearn = true;
                                        btn_relearn.setEnabled(false);
                                    } else {
                                        Toast.makeText(WordLearnActivity.this, jsonheader.getString("msg"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(WordLearnActivity.this, "data structure error ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //构建FormBody，传入要提交的参数
                FormBody formBody = new FormBody.Builder()
                        .add("id", wordid.toString()).build();
                HttpUtil.Put(ConstantValue.SERVERADRR + "/tokgo/word/learning", formBody
                        , new TokgoCallback(WordLearnActivity.this, "put learning error") {
                    @Override
                    public void onResponse(final String responsedata) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonheader = JSONObject.parseObject(responsedata);
                                    if(jsonheader.getInteger("code")==0){
                                        getWord();
                                    }
                                    else {
                                        Toast.makeText(WordLearnActivity.this, jsonheader.getString("msg"), Toast.LENGTH_SHORT).show();
                                    }

                                } catch (Exception e) {
                                    Toast.makeText(WordLearnActivity.this, "data structure error ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });

    }
    private void getWord() {
        btn_ok.setEnabled(false);
        btn_relearn.setEnabled(false);
        HttpUtil.Get(ConstantValue.SERVERADRR + "/tokgo/word/todaylearnone"
                , new TokgoCallback(WordLearnActivity.this, "get todaylearnone error") {
            @Override
            public void onResponse(final String responsedata) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonheader = JSONObject.parseObject(responsedata);
                            if(jsonheader.getInteger("code")==0){
                                try {

                                    JSONObject jsondata = JSONObject.parseObject(jsonheader.getString("data"));
                                    wordid = jsondata.getInteger("id");
                                    isRlearn = jsondata.getBoolean("relearn");
                                    ((TextView)findViewById(R.id.wl_english)).setText(jsondata.getString("english"));
                                    ((TextView)findViewById(R.id.wl_chinese)).setText(jsondata.getString("chinese"));
                                    if (!isRlearn){
                                        btn_relearn.setEnabled(true);
                                    }
                                    btn_ok.setEnabled(true);

                                }catch (Exception e){
                                    AlertDialog.Builder builder  = new AlertDialog.Builder(WordLearnActivity.this);
                                    builder.setTitle("提示" ) ;
                                    builder.setMessage("今日的学习任务已经完成了" ) ;
                                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    });
                                    builder.show();
                                }

                            }
                            else {

                                Toast.makeText(WordLearnActivity.this, jsonheader.getString("msg"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(WordLearnActivity.this, "data structure error ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}
