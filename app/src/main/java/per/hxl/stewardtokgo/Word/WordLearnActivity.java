package per.hxl.stewardtokgo.Word;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
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
import per.hxl.stewardtokgo.Task.TaskService;
import per.hxl.stewardtokgo.utils.ConstantValue;

public class WordLearnActivity extends AppCompatActivity {

    private Integer wordid;
    private boolean isRlearn;
    private Button btn_relearn;
    private Button btn_ok;
    private TaskService taskService;
    private WordLearnShow wordLearnShow = null;



    public static String WORD_APP_LOCAL = "com.xdf.recite";
    private static Integer LEARN_TIME = 180;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_learn);
        inintBtn();
        //获得单词
        getWord();

        wordLearnShow = new WordLearnShow(getBaseContext());
        //绑定Service
        bindService(new Intent(getBaseContext() ,TaskService.class), conn, Context.BIND_AUTO_CREATE);
    }

    private void inintBtn() {
        btn_ok = (Button)findViewById(R.id.wl_btn_finish);
        btn_relearn = (Button)findViewById(R.id.wl_btn_again);
        findViewById(R.id.wl_btn_begin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskService!=null) {
                    String learnWord = ((TextView)findViewById(R.id.wl_english)).getText().toString().trim();
                    taskService.addApplication(WORD_APP_LOCAL,wordLearnShow);
                    wordLearnShow.show(learnWord);
                    // 把要学习的单词放入剪切板
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    if (cm != null) {
                        cm.setPrimaryClip(ClipData.newPlainText(null, learnWord));//参数一：标签，可为空，参数二：要复制到剪贴板的文本
                    }
                    /**知道要跳转应用的包命与目标Activity*/
                    Intent studyIntent = getPackageManager().getLaunchIntentForPackage(WordLearnActivity.WORD_APP_LOCAL);
                    if (studyIntent !=null){
                        startActivity(getPackageManager().getLaunchIntentForPackage(WordLearnActivity.WORD_APP_LOCAL));
                        return;
                    }
                }
                Toast.makeText(getBaseContext(),"学习监控没有打开或者没有找到学习app",Toast.LENGTH_SHORT).show();
            }
        });

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
                                    Looper.prepare();
                                    Toast.makeText(WordLearnActivity.this, "data structure error ", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
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
                if (taskService == null){
                    return;
                }

                if (taskService.getResult(WORD_APP_LOCAL)< LEARN_TIME){
                    Toast.makeText(WordLearnActivity.this, "学习时间不足，请认真学习"+taskService.getResult(WORD_APP_LOCAL), Toast.LENGTH_SHORT).show();
                    return;
                }
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
                                        wordLearnShow.notShow();
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
                            int code = jsonheader.getInteger("code");
                            if(code ==0 ){
                                try {

                                    JSONObject jsondata = JSONObject.parseObject(jsonheader.getString("data"));
                                    wordid = jsondata.getInteger("wid");
                                    isRlearn = jsondata.getBoolean("relearn");
                                    ((TextView)findViewById(R.id.wl_english)).setText(jsondata.getString("english"));
                                    ((TextView)findViewById(R.id.wl_chinese)).setText(jsondata.getString("chinese"));
                                    String task = "学习进度: "+jsondata.getInteger("learned")+"/"+jsondata.getInteger("total");
                                    ((TextView)findViewById(R.id.wl_task)).setText(task);
                                    if (!isRlearn){
                                        btn_relearn.setEnabled(true);
                                    }
                                    btn_ok.setEnabled(true);
                                    return;

                                }catch (Exception e){ }

                            }else if(code == 20001){
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
                                return;
                            }
                            Toast.makeText(WordLearnActivity.this, jsonheader.getString("msg"), Toast.LENGTH_SHORT).show();
                            return;
                        } catch (Exception e) { }
                        Toast.makeText(WordLearnActivity.this, "data structure error ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //返回一个MsgService对象
            taskService = ((TaskService.MsgBinder)service).getService();
        }
    };
}
