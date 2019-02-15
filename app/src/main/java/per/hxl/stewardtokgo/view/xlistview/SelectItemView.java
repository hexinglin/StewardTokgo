package per.hxl.stewardtokgo.view.xlistview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import per.hxl.stewardtokgo.R;
import per.hxl.stewardtokgo.utils.ConstantValue;


/**
 * Created by TOKGO on 2017/8/15.
 */

public class SelectItemView extends RelativeLayout {
    private TextView tv_des;
    private String selectitle;
    private String saveFalg;
    private int nowIndex = -1;
    private int clickIndex = -1;
    private String[] items = new String[]{};//创建item
    private SharedPreferences sp = null;

    public SelectItemView(Context context) {
        this(context,null);
    }

    public SelectItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SelectItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.view_item_select,this);
        tv_des =  findViewById(R.id.selecttv_des);
        TextView tv_title = findViewById(R.id.selecttv_title);
        tv_title.setText(attrs.getAttributeValue(ConstantValue.NAMEWSPACE, "title"));
        //获取自定义以及原生属性的操作，写在此次，AttributeSet attrs中获取
        selectitle = attrs.getAttributeValue(ConstantValue.NAMEWSPACE, "selecttitle");
        initOnClick();
    }

    private void initOnClick() {
        findViewById(R.id.selectcb_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 如果为items 0 不弹出
                if (items.length<1){

                    return;
                }
                clickIndex = nowIndex;
                AlertDialog alertDialog4 = new AlertDialog.Builder(getContext())
                        .setTitle(selectitle)
                        .setSingleChoiceItems(items, nowIndex, new DialogInterface.OnClickListener() {//添加单选框
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                clickIndex = i;
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                saveSelectContent();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { }
                        })
                        .create();
                alertDialog4.show();
            }
        });

    }

    public int getSelect(){
        return nowIndex;
    }

    public String getSelectText(){
        if (nowIndex<0){
            return "";
        }
        return items[nowIndex];
    }
    private void saveSelectContent() {
        if (!"".equals(saveFalg)){
            putint(saveFalg,clickIndex);
        }
        nowIndex = clickIndex;
        tv_des.setText(items[nowIndex]);
    }


    public void init(String[] items,int defautIndex,String saveFalg){
        this.items =items;
        this.nowIndex = defautIndex;
        this.saveFalg = saveFalg;
        if (items.length < defautIndex+1){
            this.nowIndex = items.length-1;
        }
        initUI();
    }

    private void initUI() {
        if (this.nowIndex<0){
            this.tv_des.setText("");
        }else {
            this.tv_des.setText(items[nowIndex]);
        }
    }

    public void init(String[] items){
        init(items,-1,"");
    }

    public void init(String[] items,int defautIndex){
        init(items,defautIndex,"");
    }

    public void init(String[] items,String saveFalg){
        if (!"".equals(saveFalg)){
            init(items,getint(saveFalg,-1),saveFalg);
        }
        else
            init(items,-1,saveFalg);
    }

    /**
     * 写入int变量至sp中
     * @param key 存储节点名称
     * @param value 存储节点的值 int
     * */
    public void  putint(String key,int value){
        if (sp == null)
            sp = getContext().getSharedPreferences("SWconfig", Context.MODE_PRIVATE);
        sp.edit().putInt(key,value).commit();
    }

    /**
     * 读取sp中int变量
     * @param key 存储节点名称
     * @param defvalue 存储节点的值 int
     * @return    默认值或者此节点读取到的值
     * */
    public int getint(String key,int defvalue){
        if (sp == null)
            sp = getContext().getSharedPreferences("SWconfig", Context.MODE_PRIVATE);
        return sp.getInt(key,defvalue);
    }


}
