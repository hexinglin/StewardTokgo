package per.hxl.stewardtokgo.Chat;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.WebSocket;
import per.hxl.stewardtokgo.Chat.adapter.MessageAdapter;
import per.hxl.stewardtokgo.Chat.bean.MessageItem;
import per.hxl.stewardtokgo.Net.NetReceive;
import per.hxl.stewardtokgo.R;
import per.hxl.stewardtokgo.utils.ConstantValue;
import per.hxl.stewardtokgo.utils.SPutil;
import per.hxl.stewardtokgo.view.xlistview.MsgListView;

public class ChatUIActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener,
        MsgListView.IXListViewListener {

    private static final String SYSTEM_ACCOUNT = "system_chat_bot";
    public static int MSGPAGERNUM;
    private WindowManager.LayoutParams mParams;
    private InputMethodManager mInputMethodManager;
    private String account ="";
    private Button mBtnSend;// 发送消息按钮
    private static MessageAdapter adapter;// 发送消息展示的adapter
    private MsgListView mMsgListView;// 展示消息的

    private EditText mEtMsg;
    private Button mBtnAffix;
    private Handler wsHandler = new Handler(Looper.getMainLooper());

    private ChatClient chatClient;

    private boolean isFaceShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_ui);
        mParams = getWindow().getAttributes();
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        MSGPAGERNUM = 0;
        account = SPutil.getString(this, ConstantValue.USER_ACCOUNT, "");
        initView();
        mEtMsgOnKeyListener();
        initNet();
    }




    private void initNet() {
        if (Build.VERSION.SDK_INT >= 11) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        }
        try {
            chatClient = new ChatClient(new NetReceive() {
                @Override
                public void ServersBreakoff() {
                    chatClient = null;
                }

                @Override
                public void Receive(Socket client, String msg) {
                    JSONObject mesageJson = JSON.parseObject(msg);
                    RecMessage(mesageJson.getString("sendaccount"),mesageJson.getString("message"));
                }
            });
            chatClient.Send("{\"action\": \"login\",\"account\": \""+account+"\"}");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private boolean SendMsg(String recAcount,String msg) {
        String sendmsg = "{\"action\": \"chat\",\"sendaccount\": \""+account+"\"" +
                ",\"receiveaccount\": \""+recAcount+"\",\"message\": \""+msg+"\"}";
        if (chatClient!=null){
            if (!"".equals(account)){
                //数据为json格式
                return chatClient.Send(sendmsg);
            }
        }
        return false;
    }

    private void initView() {
        mEtMsg = findViewById(R.id.msg_et);
        // 图片附件
        mBtnAffix = findViewById(R.id.btn_chat_affix);
        mBtnAffix.setOnClickListener(this);



        mBtnSend =  findViewById(R.id.send_btn);
        mBtnSend.setClickable(true);
        mBtnSend.setEnabled(true);
        mBtnSend.setOnClickListener(this);


        adapter = new MessageAdapter(this, initMsgData());
        mMsgListView =  findViewById(R.id.msg_listView);
        // 触摸ListView隐藏表情和输入法
        mMsgListView.setOnTouchListener(this);
        mMsgListView.setPullLoadEnable(false);
        mMsgListView.setXListViewListener(this);
        mMsgListView.setAdapter(adapter);
        mMsgListView.setSelection(adapter.getCount() - 1);
    }


    /**
     * 加载消息历史，从数据库中读出
     */
    private List<MessageItem> initMsgData() {

        List<MessageItem> msgList = new ArrayList<MessageItem>();// 消息对象数组

        return msgList;

    }


    public void RecMessage(final String nick,final String text) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            wsHandler.post(new Runnable() {
                @Override
                public void run() {
                    MessageItem item = new MessageItem(MessageItem.MESSAGE_TYPE_TEXT,
                            nick, System.currentTimeMillis(), text, 0, true, 0, 0);
                    adapter.upDateMsg(item);// 更新界面
                    scrollToBottomListItem();
                }
            });
        } else {
            Log.e("websocket", "WsManager-----error");
        }
    }




    /**
     * @Description 滑动到列表底部
     */
    private void scrollToBottomListItem() {

        // todo eric, why use the last one index + 2 can real scroll to the
        // bottom?
        if (mMsgListView != null) {
            mMsgListView.setSelection(adapter.getCount() + 1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_btn: {
                if (chatClient == null){
                    Toast.makeText(getBaseContext(),"还未链接无服务器，请稍等",Toast.LENGTH_SHORT).show();
                    return;
                }
                // 发送消息
                String msg = mEtMsg.getText().toString();
                MessageItem item = new MessageItem(
                        MessageItem.MESSAGE_TYPE_TEXT, "nick",
                        System.currentTimeMillis(), msg, 0,
                        false, 0, 0);
                adapter.upDateMsg(item);
                SendMsg(SYSTEM_ACCOUNT,msg);
                mMsgListView.setSelection(adapter.getCount() - 1);
                mEtMsg.setText("");

                break;
            }
        }
    }

    private void mEtMsgOnKeyListener() {
        mEtMsg.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (mParams.softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                            || isFaceShow) {
//                        mllFace.setVisibility(View.GONE);
                        isFaceShow = false;
                        // imm.showSoftInput(msgEt, 0);
                        return true;
                    }
                }
                return false;
            }
        });
        mEtMsg.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mBtnSend.setEnabled(true);
                    mBtnAffix.setVisibility(View.GONE);
                    mBtnSend.setVisibility(View.VISIBLE);
                } else {
                    mBtnSend.setEnabled(false);
                    mBtnAffix.setVisibility(View.VISIBLE);
                    mBtnSend.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.msg_listView:
                mInputMethodManager.hideSoftInputFromWindow(
                        mEtMsg.getWindowToken(), 0);
//                mllFace.setVisibility(View.GONE);
                isFaceShow = false;
                break;
            case R.id.msg_et:
                mInputMethodManager.showSoftInput(mEtMsg, 0);
//                mllFace.setVisibility(View.GONE);
                isFaceShow = false;
                break;

            default:
                break;
        }
        return false;
    }

    @Override
    public void onRefresh() {
        // TODO 上拉刷新
//        MSGPAGERNUM++;
//        List<MessageItem> msgList = initMsgData();
//        int position = adapter.getCount();
//        adapter.setmMsgList(msgList);
//        mMsgListView.stopRefresh();
//        mMsgListView.setSelection(adapter.getCount() - position - 1);
//        Log.i(ConstantValue.BUGTAG,"MsgPagerNum = " + MSGPAGERNUM + ", adapter.getCount() = "
//                + adapter.getCount());
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (chatClient !=null){
            chatClient.Close();
            chatClient = null;
        }
    }
}
