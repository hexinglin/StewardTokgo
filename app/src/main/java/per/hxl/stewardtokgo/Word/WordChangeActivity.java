package per.hxl.stewardtokgo.Word;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import okhttp3.FormBody;
import per.hxl.stewardtokgo.Net.HttpUtil;
import per.hxl.stewardtokgo.Net.TokgoCallback;
import per.hxl.stewardtokgo.R;
import per.hxl.stewardtokgo.utils.ConstantValue;

public class WordChangeActivity extends AppCompatActivity {

    private EditText et_english;
    private EditText et_chinese;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_change);
        initModel();
        initParam();
        setSubmit();


    }

    private void initParam() {
        et_english = findViewById(R.id.wc_english);
        et_chinese = findViewById(R.id.wl_chinese);
    }

    private void setSubmit() {
        findViewById(R.id.wc_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String english =et_english.getText().toString().trim();
                String chinese=et_chinese.getText().toString().trim();
                if ("".equals(english)||"".equals(chinese) ){
                    Toast.makeText(WordChangeActivity.this, "请填写信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                //构建FormBody，传入要提交的参数
                FormBody formBody = new FormBody.Builder()
                        .add("english", english)
                        .add("chinese",chinese)
                        .build();
                HttpUtil.Post(ConstantValue.SERVERADRR + "/tokgo/word/", formBody, new TokgoCallback(WordChangeActivity.this,"add word error") {
                    @Override
                    public void onResponse(String responsedata) {
                        Looper.prepare();
                        try {
                            JSONObject jsonheader = JSONObject.parseObject(responsedata);
                            if(jsonheader.getInteger("code")==0){
                                et_english.setText("");
                                et_chinese.setText("");
                                Toast.makeText(WordChangeActivity.this, "add word ok ", Toast.LENGTH_SHORT).show();
                            }else
                                Toast.makeText(WordChangeActivity.this, "add word error ", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(WordChangeActivity.this, "data structure error ", Toast.LENGTH_SHORT).show();
                        }
                        Looper.loop();

                    }
                });
            }
        });



    }

    private void initModel() {
        Intent intent = getIntent();
        String oldWord = intent.getStringExtra("oldWord");
        if (oldWord==null || "".equals(oldWord)){
//            findViewById(R.id.wc_oldEnglishLayout).setVisibility(View.INVISIBLE);
        }

    }
}
