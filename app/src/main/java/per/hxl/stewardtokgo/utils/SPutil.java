package per.hxl.stewardtokgo.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by TOKGO on 2017/8/13.
 */

public class SPutil {
    private static SharedPreferences sp = null;
    /**
     * 写入boolean变量至sp中
     * @param context 上下文
     * @param key 存储节点名称
     * @param value 存储节点的值 boolean
     * */
    public static void  putBoolean(Context context,String key,boolean value){
        if (sp == null)
             sp = context.getSharedPreferences("SWconfig", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }

    /**
     * 读取sp中boolean变量
     * @param context 上下文
     * @param key 存储节点名称
     * @param defvalue 存储节点的值 boolean
     * @return    默认值或者此节点读取到的值
     * */
    public static boolean  getBoolean(Context context,String key,boolean defvalue){
        if (sp == null)
            sp = context.getSharedPreferences("SWconfig", Context.MODE_PRIVATE);
        return sp.getBoolean(key,defvalue);
    }



    /**
     * 写入long变量至sp中
     * @param context 上下文
     * @param key 存储节点名称
     * @param value 存储节点的值 int
     * */
    public static void  putlong(Context context,String key,long value){
        if (sp == null)
            sp = context.getSharedPreferences("SWconfig", Context.MODE_PRIVATE);
        sp.edit().putLong(key,value).commit();
    }

    /**
     * 读取sp中long变量
     * @param context 上下文
     * @param key 存储节点名称
     * @param defvalue 存储节点的值 int
     * @return    默认值或者此节点读取到的值
     * */
    public static long  getlong(Context context,String key,long defvalue){
        if (sp == null)
            sp = context.getSharedPreferences("SWconfig", Context.MODE_PRIVATE);
        return sp.getLong(key,defvalue);
    }





    /**
     * 写入int变量至sp中
     * @param context 上下文
     * @param key 存储节点名称
     * @param value 存储节点的值 int
     * */
    public static void  putint(Context context,String key,int value){
        if (sp == null)
            sp = context.getSharedPreferences("SWconfig", Context.MODE_PRIVATE);
        sp.edit().putInt(key,value).commit();
    }

    /**
     * 读取sp中int变量
     * @param context 上下文
     * @param key 存储节点名称
     * @param defvalue 存储节点的值 int
     * @return    默认值或者此节点读取到的值
     * */
    public static int  getint(Context context,String key,int defvalue){
        if (sp == null)
            sp = context.getSharedPreferences("SWconfig", Context.MODE_PRIVATE);
        return sp.getInt(key,defvalue);
    }

    /**
     * 写入String变量至sp中
     * @param context 上下文
     * @param key 存储节点名称
     * @param value 存储节点的值 String
     * */
    public static void  putString(Context context,String key,String value){
        if (sp == null)
            sp = context.getSharedPreferences("SWconfig", Context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();
    }

    /**
     * 读取sp中String变量
     * @param context 上下文
     * @param key 存储节点名称
     * @param defvalue 存储节点的值 String
     * @return    默认值或者此节点读取到的值
     * */
    public static String  getString(Context context,String key,String defvalue){
        if (sp == null)
            sp = context.getSharedPreferences("SWconfig", Context.MODE_PRIVATE);
        return sp.getString(key,defvalue);
    }

}
