package per.hxl.stewardtokgo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import per.hxl.stewardtokgo.Net.HeartbeartService;
import per.hxl.stewardtokgo.Net.WsManager;
import per.hxl.stewardtokgo.R;
import per.hxl.stewardtokgo.Task.TaskService;
import per.hxl.stewardtokgo.Word.WordChangeActivity;
import per.hxl.stewardtokgo.Word.WordLearnActivity;
import per.hxl.stewardtokgo.Word.WordLearnShow;
import per.hxl.stewardtokgo.utils.ConstantValue;


public class MainActivity extends AppCompatActivity {

    private GridView gv_list;
    private String[] mTitleStr;
    private int[] mDrawableIds;
    WordLearnShow wordLearnShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        //初始化数据的方法
        initData();
        //开启系统监控及心跳服务
        startService(new Intent(getBaseContext() ,TaskService.class));
        startService(new Intent(getBaseContext() ,HeartbeartService.class));

    }

    private void initUI() {
        gv_list = (GridView) findViewById(R.id.gv_home);
    }

    private void initData() {
        //九宫格
        mTitleStr = new String[]{"个人信息","添加单词","单词学习","充值中心","任务","跑步","商城","程序锁","设置中心"};
        mDrawableIds = new int[]{R.mipmap.home_3,R.mipmap.temp,R.mipmap.temp,R.mipmap.temp,
                R.mipmap.home_5, R.mipmap.runicon,R.mipmap.home_8,R.mipmap.home_6,R.mipmap.home_7};
        gv_list.setAdapter(new MyAdapter());
        gv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        startActivity(new Intent(getBaseContext(), AccountActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(getBaseContext(),WordChangeActivity.class));
                        break;
                    case 2:startActivity(new Intent(getBaseContext(),WordLearnActivity.class));
                        break;
                    case 3:
//                        startActivity(new Intent(getBaseContext(),PayActivity.class));
                        wordLearnShow.show("");
                        break;
//                    case 4:startActivity(new Intent(getBaseContext(), TaskActivity.class));
//                        break;
//                    case 5:startActivity(new Intent(getBaseContext(), MapActivity.class));
//                        break;
//                    case 6:startActivity(new Intent(getBaseContext(), ShopActivity.class));
//                        break;
//                    case 7:startActivity(new Intent(getBaseContext(), LockActivity.class));
//                        break;
                    case 8:startActivity(new Intent(getBaseContext(), SettingActivity.class));
                        break;
                }
            }
        });
    }



    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mTitleStr.length;
        }

        @Override
        public Object getItem(int i) {
            return mTitleStr[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = View.inflate(getBaseContext(),R.layout.gridview_item,null);
            ((TextView) view.findViewById(R.id.hometv_title)).setText(mTitleStr[i]);
            view.findViewById(R.id.homeiv_icon).setBackgroundResource(mDrawableIds[i]);
            return view;
        }
    }






    @Override
    protected void onDestroy() {
        super.onDestroy();
        
    }
}
