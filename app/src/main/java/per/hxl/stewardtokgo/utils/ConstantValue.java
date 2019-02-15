package per.hxl.stewardtokgo.utils;

/**
 * Created by TOKGO on 2017/8/13.
 */

public class ConstantValue {
    //程序是否调试
    public static final Boolean IsBug = false;
    //程序调试TAG
    public static final String BUGTAG = "TAGTOKGO";
    public static final String[] SERVERADRRITEM =new String[]{
            "http://prd-tokgo-api.52wywl.top",
            "http://qa-tokgo-api.52wywl.top",
            "http://prd-tokgo-api.tokgo.cn",
            "http://qa-tokgo-api.tokgo.cn"
    };
    public static String SERVERADRR = "http://prd-tokgo-api.tokgo.cn";

    public static final String SERVERADRRSAVEFALG = "serveradrrsavefalg";

    public static final int CHATQAPORT = 6667;
    public static final int CHATPRDPORT = 1315;


    //程序剩余时间
    public static final String FREETIME = "freetime";
    //用户token
    public static final String USER_TOKEN = "usertoken";
    //用户账号
    public static final String USER_ACCOUNT = "useraccount";
    //用户密码
    public static final String USER_PASSWORD = "userpassword";
    //显示悬浮窗
    public static final String SHOWSUSPENSIONWINDOW = "shosuspensionwindow";
    //程序锁监控
    public static final String OPENCLOSEAPPLOCK = "opencloseapplock";
    //是否打开提示音
    public static final String OPENPROMPTTONE = "openprompttone";
    //网络通信的广播的消息标签
    public static final String NETFLAG = "netflag";
    //网络通信的数据
    public static final String NETMSG = "netmsg";
    //昨日消耗的总时间
    public static final String YESTERDAYCOST = "yesterdaycosttime";
    //今天消耗的总时间
    public static final String TODAYCOST = "todaycosttime";
    //今天时间日期
    public static final String MEMORYBEGINTIME = "memorybegintime";
    //自定义命名空间标识
    public static final String NAMEWSPACE = "http://schemas.android.com/apk/res/per.hxl.stewardtokgo";
    //购买星币的标记
    public static final String XCOINBUYFLAG = "buyxcoinflag";
    private ConstantValue() {}
}
