package cn.abcdsxg.app.appJump.Base;

import android.app.Application;

/**
 * Author : 时小光
 * Email  : abcdsxg@gmail.com
 * Blog   : http://www.abcdsxg.cn
 * Date   : 2016/7/9 17:04
 */
public class BaseApplication extends Application {

    private static BaseApplication application;

    @Override
    public void onCreate()
    {
        super.onCreate();
        application = this;
    }

    /**
     * 取得Application单件
     *
     * @return
     */
    public static BaseApplication getInstance()
    {
        return application;
    }
}
