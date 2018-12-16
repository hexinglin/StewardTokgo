package per.hxl.stewardtokgo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import per.hxl.stewardtokgo.R;
import per.hxl.stewardtokgo.utils.ConstantValue;


/**
 * Created by TOKGO on 2017/8/17.
 */

public class InfroItemView extends RelativeLayout {

    private TextView tv_num;
    private TextView tv_title;

    public InfroItemView(Context context) {
        this(context,null);
    }

    public InfroItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public InfroItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.view_item_infro,this);
        tv_title = (TextView) findViewById(R.id.infro_title);
        tv_num = (TextView) findViewById(R.id.infro_num);
        if(attrs==null)
            return;
        tv_title.setText(attrs.getAttributeValue(ConstantValue.NAMEWSPACE, "infrotitle"));
        tv_num.setText(attrs.getAttributeValue(ConstantValue.NAMEWSPACE, "infronum"));
    }

    public void SetTitle(String titlestr){
        tv_title.setText(titlestr);
    }

    public String GetTitle(){
        return tv_title.getText().toString();
    }

    public void SetInfro(String infro){
        tv_num.setText(infro);
    }

    public String GetTnfro(){
        return tv_num.getText().toString();
    }

}
