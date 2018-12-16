package per.hxl.stewardtokgo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import per.hxl.stewardtokgo.R;
import per.hxl.stewardtokgo.utils.ConstantValue;


/**
 * Created by TOKGO on 2017/8/15.
 */

public class SettingItemView extends RelativeLayout {
    private TextView tv_des;
    private CheckBox cb_box;
    private String destitle;
    private String desoff;
    private String desno;

    public SettingItemView(Context context) {
        this(context,null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.view_item_setting,this);
        TextView tv_title = (TextView) findViewById(R.id.settingtv_title);
        tv_des = (TextView) findViewById(R.id.settingtv_des);
        cb_box = (CheckBox) findViewById(R.id.settingcb_box);
        //获取自定义以及原生属性的操作，写在此次，AttributeSet attrs中获取
        initAttrs(attrs);
        //设置布局文件中的标题
        tv_title.setText(destitle);
    }

    private void initAttrs(AttributeSet attrs) {
        destitle = attrs.getAttributeValue(ConstantValue.NAMEWSPACE, "destitle");
        desoff = attrs.getAttributeValue(ConstantValue.NAMEWSPACE, "desoff");
        desno = attrs.getAttributeValue(ConstantValue.NAMEWSPACE, "deson");
    }

    public boolean isCheck(){
        return  cb_box.isChecked();
    }

    public void SetEnabled(boolean isenabled){
        cb_box.setEnabled(isenabled);
    }

    public void setCheck(boolean ischeck){
        cb_box.setChecked(ischeck);
        if (ischeck)
            tv_des.setText(desno);
        else
            tv_des.setText(desoff);
    }
}
