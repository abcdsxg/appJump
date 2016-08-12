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
    String mainTag;
    Activity mCurrentActivity;
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        mainTag = mainTag + "-" + lpparam.packageName;
        //系统 app 不监控
        if ((lpparam.appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0)
            return;

        //获取当前activity
        Class<?> instrumentation = XposedHelpers.findClass(
                "android.app.Instrumentation", lpparam.classLoader);

        XposedBridge.hookAllMethods(instrumentation, "newActivity", new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                mCurrentActivity = (Activity) param.getResult();

            }
        });

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
    //处理 intent 和 bundle
    public void getIntentAndBundle(Intent it, Bundle bund, String tag) {
        Gson intentGson=new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        HashMap<String,Object> intentMap=new HashMap<>();

        //获取 bundle
        if (bund != null) {
            String NEWLINE = "\n";
            StringBuilder stringBuilder = new StringBuilder();
            Bundle intentBundle = bund;
            Set<String> keySet = intentBundle.keySet();
            int count = 0;

            for (String key : keySet) {
                count++;
                Object thisObject = intentBundle.get(key);
                stringBuilder.append(tag + " Bundle EXTRA ").append(count)
                        .append(":");
                String thisClass = thisObject.getClass().getName();
                if (thisClass != null) {
                    stringBuilder.append(tag + "Bundle Class: ").append(thisClass)
                            .append(NEWLINE);
                }
                stringBuilder.append(tag + " Bundle Key: ").append(key).append(NEWLINE);

                if (thisObject instanceof String || thisObject instanceof Long
                        || thisObject instanceof Integer
                        || thisObject instanceof Boolean) {
                    stringBuilder.append(tag + " Bundle Value: " + thisObject.toString())
                            .append(NEWLINE);
                } else if (thisObject instanceof ArrayList) {
                    stringBuilder.append(tag + " Bundle Values:");
                    ArrayList thisArrayList = (ArrayList) thisObject;
                    for (Object thisArrayListObject : thisArrayList) {
                        stringBuilder.append(thisArrayListObject.toString()
                                + NEWLINE);
                    }
                }
            }
            Log.i(mainTag, tag + " Bundle EXTRA:" + stringBuilder);
        }

        //取得 action
        if (it.getAction() != null) {
            Log.i(mainTag, tag + " Action:" + it.getAction());
            intentMap.put("action",it.getAction());
        }

//        //取得 flag
//        if (it.getFlags() != 0) {
//            String NEWLINE = "\n";
//            StringBuilder stringBuilder = new StringBuilder();
//            ArrayList<String> flagsStrings = getFlags(it);
//            if (flagsStrings.size() > 0) {
//                for (String thisFlagString : flagsStrings) {
//                    stringBuilder.append(thisFlagString).append(NEWLINE);
//                    intentMap.clear();
//                }
//            } else {
//                stringBuilder.append("NONE").append(NEWLINE);
//            }
//            Log.i(mainTag, tag + "  Flags:" + stringBuilder);
//        }

        //取得 data
        if (it.getDataString() != null) {
            Log.i(mainTag, tag + " Data:" + it.getDataString());
            intentMap.put("data", it.getDataString());
        }

        //取得 type
        if (it.getType() != null) {
            Log.i(mainTag, tag + " Type:" + it.getType());
            intentMap.put("type", it.getType());
        }

        //取得 Component
        if (it.getComponent() != null) {
            ComponentName cp = it.getComponent();
            Log.i(mainTag, tag + " Component:" + cp.getPackageName() + "/" + cp.getClassName());
            intentMap.put("pkgName", cp.getPackageName());
        }
        int count = 0;
        if (it.getExtras() != null) {
            String NEWLINE = "\n";
            StringBuilder stringBuilder = new StringBuilder();
            try {
                Bundle intentBundle = it.getExtras();
                if (intentBundle != null) {
                    Set<String> keySet = intentBundle.keySet();


                    for (String key : keySet) {
                        count++;
                        Object thisObject = intentBundle.get(key);
                        stringBuilder.append(NEWLINE).append(tag + " EXTRA ").append(count)
                                .append(":").append(NEWLINE);
                        String thisClass = thisObject.getClass().getName();
                        if (thisClass != null) {
                            stringBuilder.append(tag + " Class: ").append(thisClass)
                                    .append(NEWLINE);
                        }
                        stringBuilder.append(tag + " Key: ").append(key).append(NEWLINE);

                        if (thisObject instanceof ArrayList) {
                            stringBuilder.append(tag + " Values:");
                            ArrayList thisArrayList = (ArrayList) thisObject;
                            HashMap<String, String> valueMap=new HashMap<>();
                            for (Object thisArrayListObject : thisArrayList) {
                                stringBuilder.append(thisArrayListObject.toString()
                                        + NEWLINE);
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

                Log.i(mainTag, tag + " EXTRA: \n" + stringBuilder);
            } catch (Exception e) {
                stringBuilder.append(tag + " BUNDLE:");
                stringBuilder.append(tag + " Error extracting extras");
                e.printStackTrace();
            }
        }
        String json=intentGson.toJson(intentMap);
        Log.e("tag", "json: "+json );
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
//
//    //分离 flag
//    private ArrayList<String> getFlags(Intent editableIntent) {
//        ArrayList<String> flagsStrings = new ArrayList<String>();
//        int flags = editableIntent.getFlags();
//        Set<Map.Entry<Integer, String>> set = FLAGS_MAP.entrySet();
//        Iterator<Map.Entry<Integer, String>> i = set.iterator();
//        while (i.hasNext()) {
//            Map.Entry<Integer, String> thisFlag = (Map.Entry<Integer, String>) i.next();
//            if ((flags & thisFlag.getKey()) != 0) {
//                flagsStrings.add(thisFlag.getValue());
//            }
//        }
//        return flagsStrings;
//    }
//
//
//    // 映射 flag
//    private static final Map<Integer, String> FLAGS_MAP = new HashMap<Integer, String>() {
//        {
//            put(new Integer(Intent.FLAG_GRANT_READ_URI_PERMISSION),
//                    "FLAG_GRANT_READ_URI_PERMISSION");
//            put(new Integer(Intent.FLAG_GRANT_WRITE_URI_PERMISSION),
//                    "FLAG_GRANT_WRITE_URI_PERMISSION");
//            put(new Integer(Intent.FLAG_FROM_BACKGROUND),
//                    "FLAG_FROM_BACKGROUND");
//            put(new Integer(Intent.FLAG_DEBUG_LOG_RESOLUTION),
//                    "FLAG_DEBUG_LOG_RESOLUTION");
//            put(new Integer(Intent.FLAG_EXCLUDE_STOPPED_PACKAGES),
//                    "FLAG_EXCLUDE_STOPPED_PACKAGES");
//            put(new Integer(Intent.FLAG_INCLUDE_STOPPED_PACKAGES),
//                    "FLAG_INCLUDE_STOPPED_PACKAGES");
//            put(new Integer(Intent.FLAG_ACTIVITY_NO_HISTORY),
//                    "FLAG_ACTIVITY_NO_HISTORY");
//            put(new Integer(Intent.FLAG_ACTIVITY_SINGLE_TOP),
//                    "FLAG_ACTIVITY_SINGLE_TOP");
//            put(new Integer(Intent.FLAG_ACTIVITY_NEW_TASK),
//                    "FLAG_ACTIVITY_NEW_TASK");
//            put(new Integer(Intent.FLAG_ACTIVITY_MULTIPLE_TASK),
//                    "FLAG_ACTIVITY_MULTIPLE_TASK");
//            put(new Integer(Intent.FLAG_ACTIVITY_CLEAR_TOP),
//                    "FLAG_ACTIVITY_CLEAR_TOP");
//            put(new Integer(Intent.FLAG_ACTIVITY_FORWARD_RESULT),
//                    "FLAG_ACTIVITY_FORWARD_RESULT");
//            put(new Integer(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP),
//                    "FLAG_ACTIVITY_PREVIOUS_IS_TOP");
//            put(new Integer(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS),
//                    "FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS");
//            put(new Integer(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT),
//                    "FLAG_ACTIVITY_BROUGHT_TO_FRONT");
//            put(new Integer(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED),
//                    "FLAG_ACTIVITY_RESET_TASK_IF_NEEDED");
//            put(new Integer(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY),
//                    "FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY");
//            put(new Integer(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET),
//                    "FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET");
//            put(new Integer(Intent.FLAG_ACTIVITY_NO_USER_ACTION),
//                    "FLAG_ACTIVITY_NO_USER_ACTION");
//            put(new Integer(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT),
//                    "FLAG_ACTIVITY_REORDER_TO_FRONT");
//            put(new Integer(Intent.FLAG_ACTIVITY_NO_ANIMATION),
//                    "FLAG_ACTIVITY_NO_ANIMATION");
//            put(new Integer(Intent.FLAG_ACTIVITY_CLEAR_TASK),
//                    "FLAG_ACTIVITY_CLEAR_TASK");
//            put(new Integer(Intent.FLAG_ACTIVITY_TASK_ON_HOME),
//                    "FLAG_ACTIVITY_TASK_ON_HOME");
//            put(new Integer(Intent.FLAG_RECEIVER_REGISTERED_ONLY),
//                    "FLAG_RECEIVER_REGISTERED_ONLY");
//            put(new Integer(Intent.FLAG_RECEIVER_REPLACE_PENDING),
//                    "FLAG_RECEIVER_REPLACE_PENDING");
//            //put(new Integer(0x10000000),"FLAG_RECEIVER_FOREGROUND");
//            //put(new Integer(0x08000000),"FLAG_RECEIVER_REGISTERED_ONLY_BEFORE_BOOT");
//            put(new Integer(0x04000000),
//                    "FLAG_RECEIVER_BOOT_UPGRADE");
//            //put(new Integer(0x00080000),"FLAG_ACTIVITY_NEW_DOCUMENT");
//            put(new Integer(0x00002000),
//                    "FLAG_ACTIVITY_RETAIN_IN_RECENTS");
//            put(new Integer(0x00000040),
//                    "FLAG_GRANT_PERSISTABLE_URI_PERMISSION");
//            put(new Integer(0x00000080),
//                    "FLAG_GRANT_PREFIX_URI_PERMISSION");
//            //put(new Integer(0x08000000),"FLAG_RECEIVER_NO_ABORT");
//
//        }
//    };

}
