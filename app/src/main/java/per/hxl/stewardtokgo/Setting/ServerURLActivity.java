package per.hxl.stewardtokgo.Setting;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import per.hxl.stewardtokgo.R;
import per.hxl.stewardtokgo.utils.ConstantValue;
import per.hxl.stewardtokgo.utils.SPutil;

public class ServerURLActivity extends AppCompatActivity {
    private ListView lv_list;
    private List<String> mRULStr;
    private int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_url);
        initURLData();
        setAddAction();
        setSubAction();
        initList();
    }



    private void initURLData() {
        try {
            String spURLStr = SPutil.getString(getBaseContext(), ConstantValue.SERVERADRR_LIST_STR,"");
            mRULStr = JSONObject.parseArray(spURLStr,String.class);
            if (mRULStr == null || mRULStr.size() == 0)
                throw new Exception();
        }catch (Exception e){
            mRULStr = new ArrayList<>();
            mRULStr.add("http://prd-tokgo-api.52wywl.top");
            mRULStr.add("http://qa-tokgo-api.52wywl.top");
        }
        pos = SPutil.getint(getBaseContext(), ConstantValue.SERVERADRR_POS,0);

    }

    private void initList() {
        lv_list = findViewById(R.id.urlset_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mRULStr);//新建并配置ArrayAapeter
        lv_list.setAdapter(adapter);
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i=0;i<parent.getCount();i++){
                    View v=parent.getChildAt(i);
                    if (position == i) {
                        v.setBackgroundColor(Color.RED);
                        setCurrentRUL(i);

                    } else {
                        v.setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }
        });
    }

    private void setCurrentRUL(int pos) {
        this.pos = pos;
        ConstantValue.SERVERADRR = mRULStr.get(pos);
        SPutil.putint(getBaseContext(), ConstantValue.SERVERADRR_POS,pos);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        lv_list .performItemClick(lv_list .getChildAt(pos), pos, lv_list .getItemIdAtPosition(pos));
    }
    private void setAddAction() {
        findViewById(R.id.urlset_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*@setView 装入一个EditView
                 */
                final EditText editText = new EditText(ServerURLActivity.this);
                AlertDialog.Builder inputDialog =
                        new AlertDialog.Builder(ServerURLActivity.this);
                inputDialog.setTitle("输入新的url").setView(editText);
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mRULStr.add(editText.getText().toString());
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ServerURLActivity.this,android.R.layout.simple_list_item_1,mRULStr);//新建并配置ArrayAapeter
                                lv_list.setAdapter(adapter);
                                SPutil.putString(getBaseContext(),ConstantValue.SERVERADRR, JSON.toJSONString(mRULStr));
                            }
                        }).show();
            }
        });
    }

    private void setSubAction() {
        findViewById(R.id.urlset_sub).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* @setIcon 设置对话框图标
                 * @setTitle 设置对话框标题
                 * @setMessage 设置对话框消息提示
                 * setXXX方法返回Dialog对象，因此可以链式设置属性
                 */
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(ServerURLActivity.this);
                normalDialog.setMessage("确定删除:"+mRULStr.get(pos)+" ?");
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mRULStr.remove(pos);
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ServerURLActivity.this,android.R.layout.simple_list_item_1,mRULStr);//新建并配置ArrayAapeter
                                lv_list.setAdapter(adapter);
                                SPutil.putString(getBaseContext(),ConstantValue.SERVERADRR, JSON.toJSONString(mRULStr));
                                setCurrentRUL(0);
                            }
                        });
                normalDialog.setNegativeButton("关闭",null);
                        // 显示
                normalDialog.show();
            }
        });
    }

}
