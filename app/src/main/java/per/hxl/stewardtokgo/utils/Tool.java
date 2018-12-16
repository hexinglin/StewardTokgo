package per.hxl.stewardtokgo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;

/**
 * Created by 11315 on 2017/8/19.
 */

public class Tool {

    public static String Int2TimeStr(int t){
        String str="";
        str += (t/86400)+"d "+((t%86400)/3600)+":"+((t%3600)/60)+":"+(t%60);
        return str;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String inttostr(Integer num) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(num);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String doubletostr(double num) {
        DecimalFormat df = new DecimalFormat("#,##");
        return df.format(num);
    }
    /**
     * 弹出无网络连接提示框 点解确定关系当前Activity
     * @param context 上下文
     * */

    public static void ShowNoNet(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("系统" );
        builder.setMessage("与服务器连接失败，无法使用该功能" );
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((Activity)context).finish();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            public void onCancel(DialogInterface dialog) {
                ((Activity)context).finish();
            }
        });
        builder.show();;
    }


    /**
     * 弹出提示框
     * @param context 上下文
     * @param string 提示框内容
     * */
    public static void ShowPrompt( Context context,String string){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("系统" );
        builder.setMessage(string );
        builder.setPositiveButton("确定", null);
        builder.show();;
    }

}
