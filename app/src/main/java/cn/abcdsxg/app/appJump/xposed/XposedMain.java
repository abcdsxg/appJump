package cn.abcdsxg.app.appJump.xposed;

import android.app.Activity;
import android.app.AndroidAppHelper;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cn.abcdsxg.app.appJump.Base.BaseApplication;
import cn.abcdsxg.app.appJump.Data.Constant;
import cn.abcdsxg.app.appJump.Data.Utils.SpUtil;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Author : 时小光
 * Email  : abcdsxg@gmail.com
 * Blog   : http://www.abcdsxg.cn
 * Date   : 2016/8/2 21:57
 */
public class XposedMain implements IXposedHookLoadPackage{
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        //系统 app 不监控
        if ((lpparam.appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0)
            return;

        //hook 启动 activity android.app.Activity#startActivityForResult
        findAndHookMethod("android.app.Activity", lpparam.classLoader, "startActivityForResult", Intent.class, int.class, Bundle.class, new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                        Intent it = (Intent) param.args[0];
                        Bundle bund = (Bundle) param.args[2];
                        String tag = "startXActivity";
                        getIntentAndBundle(it, bund, tag);
                    }
                }
        );

    }
    //处理 intent
    public void getIntentAndBundle(Intent it, Bundle bund, String tag) {
        Gson intentGson=new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        HashMap<String,Object> intentMap=new HashMap<>();


        //取得 action
        if (it.getAction() != null) {
            intentMap.put("action",it.getAction());
        }

        //取得 data
        if (it.getDataString() != null) {
            intentMap.put("data", it.getDataString());
        }

        //取得 type
        if (it.getType() != null) {
            intentMap.put("type", it.getType());
        }

        //取得 Component
        if (it.getComponent() != null) {
            ComponentName cp = it.getComponent();
            intentMap.put("pkgName", cp.getPackageName());
        }
        int count = 0;
        if (it.getExtras() != null) {
            try {
                Bundle intentBundle = it.getExtras();
                if (intentBundle != null) {
                    Set<String> keySet = intentBundle.keySet();

                    for (String key : keySet) {
                        count++;
                        Object thisObject = intentBundle.get(key);
                        String thisClass = thisObject.getClass().getName();

                        if (thisObject instanceof ArrayList) {
                            ArrayList thisArrayList = (ArrayList) thisObject;
                            HashMap<String, String> valueMap=new HashMap<>();
                            for (Object thisArrayListObject : thisArrayList) {
                                valueMap.put("value",thisArrayListObject.toString());
                            }
                            intentMap.put("arrayList",valueMap);
                        }else{
                            if(thisObject instanceof Long && thisObject.toString().length()==13){
                                intentMap.put("longTime",key);
                            }else {
                                HashMap<String, String> valueMap=new HashMap<>();
                                valueMap.put(key, thisObject.toString());
                                intentMap.put(thisClass+count,valueMap);
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String json=intentGson.toJson(intentMap);
        OutputStream out=null;
        try {
            Context context = AndroidAppHelper.currentApplication().getApplicationContext();
            File file=new File(Environment.getExternalStorageDirectory()+"/temp.json");
            file.createNewFile();
            out=new FileOutputStream(file);
            out.write(json.getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
