package cn.abcdsxg.app.appJump.Data.Utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * Author : 时小光
 * Email  : abcdsxg@gmail.com
 * Blog   : http://www.abcdsxg.cn
 * Date   : 2016/7/26 11:53
 */
public class ToolUtils {


    public static Drawable getAppIcon(Context context,String pkgName){
        PackageManager pm=context.getPackageManager();
        Drawable icon=null;
        try {
            ApplicationInfo info = pm.getApplicationInfo(pkgName, 0);
            icon=info.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return icon;
    }
}
