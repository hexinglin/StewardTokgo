package per.hxl.stewardtokgo.Word;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import per.hxl.stewardtokgo.R;
import per.hxl.stewardtokgo.Task.PopupWindow;
import per.hxl.stewardtokgo.utils.ConstantValue;
import per.hxl.stewardtokgo.utils.SPutil;

/**
 * Created by TOKGO on 2017/8/14.
 */

public class WordLearnShow extends PopupWindow {
    public static final int MinShowTime = 180;
    private final Context context;
    private TextView tv_time;
    private TextView tv_english;
    private int timei;


    public WordLearnShow(Context context) {
        super(context,R.layout.layout_word_toast);
        this.context = context;
        initTocastUI();
    }

    public void show(String word) {
        super.show();
        this.tv_english.setText(word);
    }

    private void initTocastUI() {
        tv_english =viewtoast.findViewById(R.id.lwt_english);
        tv_time = viewtoast.findViewById(R.id.lwt_time);
        tv_english.setText("");
        tv_time.setText("0s");
        timei = 0;
    }


    @Override
    public void setInfro(Object infro) {
        Integer time = (Integer)infro;
        timei = time;
    }

    @Override
    protected void updateContent() {
        if (timei>=MinShowTime)
            tv_time.setTextColor(Color.BLUE);
        else
            tv_time.setTextColor(Color.RED);
        tv_time.setText(timei+" s");
    }


}
