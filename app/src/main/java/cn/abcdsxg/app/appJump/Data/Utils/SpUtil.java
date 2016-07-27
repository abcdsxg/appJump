package cn.abcdsxg.app.appJump.Data.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Set;
import static android.content.Context.MODE_APPEND;

/**
 * Author : 时小光
 * Email  : abcdsxg@gmail.com
 * Blog   : http://www.abcdsxg.cn
 * Date   : 2016/7/26 16:48
 */
public class SpUtil {
    
    public static int mode=MODE_APPEND;
    
    public static boolean  getBooleanSp(Context context, String name,String tag){
        SharedPreferences sp=context.getSharedPreferences(name,mode);
        return sp.getBoolean(tag, false);
    }

    public static String  getStringSp(Context context, String name,String tag){
        SharedPreferences sp=context.getSharedPreferences(name,mode);
        return sp.getString(tag, null);
    }

    public static int  getIntSp(Context context, String name,String tag){
        SharedPreferences sp=context.getSharedPreferences(name,mode);
        return sp.getInt(tag,-1);
    }

    public static float  getFloatSp(Context context, String name,String tag){
        SharedPreferences sp=context.getSharedPreferences(name,mode);
        return sp.getFloat(tag,0);
    }
    public static long  getLongSp(Context context, String name,String tag){
        SharedPreferences sp=context.getSharedPreferences(name,mode);
        return sp.getLong(tag,0);
    }

    public static Set<String> getSetStringSp(Context context, String name,String tag){
        SharedPreferences sp=context.getSharedPreferences(name,mode);
        return sp.getStringSet(tag,null);
    }
    
    public static void saveSp(Context context,String name,String tag,boolean bool) {
        SharedPreferences sp=context.getSharedPreferences(name,mode);
        SharedPreferences.Editor edit=sp.edit();
        edit.putBoolean(tag,bool);
        edit.apply();
    }
    public static void saveSp(Context context,String name,String tag,String msg) {
        SharedPreferences sp=context.getSharedPreferences(name,mode);
        SharedPreferences.Editor edit=sp.edit();
        edit.putString(tag,msg);
        edit.apply();
    }
    public static void saveSp(Context context,String name,String tag,int msg) {
        SharedPreferences sp=context.getSharedPreferences(name,mode);
        SharedPreferences.Editor edit=sp.edit();
        edit.putInt(tag,msg);
        edit.apply();
    }
    public static void saveSp(Context context,String name,String tag,float msg) {
        SharedPreferences sp=context.getSharedPreferences(name,mode);
        SharedPreferences.Editor edit=sp.edit();
        edit.putFloat(tag,msg);
        edit.apply();
    }
    public static void saveSp(Context context,String name,String tag,long msg) {
        SharedPreferences sp=context.getSharedPreferences(name,mode);
        SharedPreferences.Editor edit=sp.edit();
        edit.putLong(tag,msg);
        edit.apply();
    }
    public static void saveSp(Context context,String name,String tag,Set<String> msg) {
        SharedPreferences sp=context.getSharedPreferences(name,mode);
        SharedPreferences.Editor edit=sp.edit();
        edit.putStringSet(tag,msg);
        edit.apply();
    }
}
