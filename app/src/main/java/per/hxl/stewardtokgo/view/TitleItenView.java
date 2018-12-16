package per.hxl.stewardtokgo.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import per.hxl.stewardtokgo.R;
import per.hxl.stewardtokgo.utils.ConstantValue;

/**
 * Created by 11315 on 2017/8/19.
 */

public class TitleItenView extends RelativeLayout {
    private Activity activity;
    public TitleItenView(Context context) {
        this(context,null);
    }

    public TitleItenView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TitleItenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        activity = (Activity)context;
        View.inflate(context, R.layout.view_item_title,this);
        ((TextView) findViewById(R.id.vit_title)).setText(attrs.getAttributeValue(ConstantValue.NAMEWSPACE, "mytitle"));

       findViewById(R.id.vit_back).setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
                activity.finish();
           }
       });

    }
}
