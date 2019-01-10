package per.hxl.stewardtokgo.Task;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by TOKGO on 2017/8/14.
 */

public abstract class PopupWindow {
    private final Context context;
    private final WindowManager.LayoutParams mparams = new WindowManager.LayoutParams();
    private WindowManager mWM;
    private Boolean isshow = false;
    private long mHits[] = new long[3];

    protected View viewtoast;

    protected abstract void updateContent();

    public PopupWindow(Context context,@LayoutRes int resource) {
        this.context = context;
        initTocast();
        viewtoast = View.inflate(context, resource,null);
        SetMove();
        SetToastClickListener();
        updatethread.start();
    }

    private void initTocast() {
        mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mparams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mparams.width =WindowManager.LayoutParams.WRAP_CONTENT;
        mparams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        mparams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        mparams.format = PixelFormat.TRANSLUCENT;
        mparams.gravity = Gravity.TOP+Gravity.LEFT;
    }

    private void SetToastClickListener() {
        viewtoast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.arraycopy(mHits,1,mHits,0,mHits.length-1);
                mHits[mHits.length-1] = System.currentTimeMillis();
                if (mHits[mHits.length-1] -mHits[0]<500){
                    notShow();
                }
            }
        });
    }


    private void SetMove() {
        viewtoast.setOnTouchListener(new View.OnTouchListener() {
            private int starY;
            private int starX;
            int x,y;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        starX = (int) event.getRawX();
                        starY = (int) event.getRawY();
                        x= mparams.x;
                        y =  mparams.y;
                        break;
                    case MotionEvent.ACTION_MOVE:

                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();
                        mparams.x = x +moveX-starX;
                        mparams.y = y +moveY - starY;
                        mWM.updateViewLayout(viewtoast,mparams);
                        break;
                }
                return false;
            }
        });
    }

    public void setInfro(Object infro) {}

    public void Close(){
        notShow();
    }

    public void show() {
        if (!isshow)
            mWM.addView(viewtoast,mparams);
        isshow = true;
    }

    public void notShow() {
        if (isshow)
            mWM.removeViewImmediate(viewtoast);
        isshow = false;
    }

    public Boolean isShow() {
        return isshow;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (isshow){
                updateContent();
                mWM.updateViewLayout(viewtoast,mparams);
            }
        }
    };



    /// 刷新线程
    private Thread updatethread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(0);
            }
        }
    });

}
