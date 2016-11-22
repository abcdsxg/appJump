package cn.abcdsxg.app.appJump.Data.Utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.SparseArray;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import cn.abcdsxg.app.appJump.Activity.ShortCutActivity;
import cn.abcdsxg.app.appJump.Data.Adapter.PanelItemAdapter;
import cn.abcdsxg.app.appJump.Data.Constant;
import cn.abcdsxg.app.appJump.Data.greenDao.AppInfo;
import cn.abcdsxg.app.appJump.Data.greenDao.DBManager;
import cn.abcdsxg.app.appJump.Data.greenDao.LancherInfo;
import cn.abcdsxg.app.appJump.R;

/**
 * Author : 时小光
 * Email  : abcdsxg@gmail.com
 * Blog   : http://www.abcdsxg.cn
 * Date   : 2016/7/26 11:53
 */
public class ToolUtils {

    private final static int REQUEST_CODE_ASK_PERMISSIONS=10;

    public static Drawable getAppIcon(Context context,String pkgName){
        PackageManager pm=context.getPackageManager();
        Drawable icon;
        try {
            ApplicationInfo info = pm.getApplicationInfo(pkgName, 0);
            icon=info.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return icon;
    }
    /**
     * 获取application中指定的meta-data
     * @return 如果没有获取成功(没有对应值或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkPermission(final Activity activity) {
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }
        int hasWriteContactsPermission = activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!activity.shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||!activity.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    ||!activity.shouldShowRequestPermissionRationale(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                        .setTitle(R.string.permisDia_title)
                        .setMessage(R.string.permisDia_msg)
                        .setPositiveButton(R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                        activity.requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                        activity.requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                });
                builder.show();
            }
            return false;
        }
        return true;
    }

    //创建桌面快捷方式
    public static void sendShortcut(Activity myActivity, AppInfo appInfo,boolean isDesktop) {
        Intent intent = new Intent(myActivity, ShortCutActivity.class);
        intent.putExtra("id",appInfo.getId());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // 不允许重建
        shortcut.putExtra("duplicate", false);
        // 设置名字
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,appInfo.getAppName());
        // 设置图标
        Drawable drawableIcon=getAppIcon(myActivity,appInfo.getPkgName());

        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON,
                drawableToBitamp(drawableIcon));
        // 设置意图和快捷方式关联程序
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT,intent);
        //判断是在桌面创建还是传值给添加快捷方式的app
        if(isDesktop) {
            myActivity.sendBroadcast(shortcut);
            Toast.makeText(myActivity, R.string.createSucceed,Toast.LENGTH_SHORT).show();
        }else{
            myActivity.setResult(Activity.RESULT_OK,shortcut);
            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
                myActivity.finishAndRemoveTask();
            }else{
                myActivity.finish();
            }
        }

    }

    public static Bitmap drawableToBitamp(Drawable drawable) {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        return bd.getBitmap();
    }

    public static byte[] Bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap Bytes2Bimap(byte[] b){
        if(b.length!=0){
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        else {
            return null;
        }
    }

    public static String getKeyFromJson(JSONObject json){
        Iterator<String> it=json.keys();
        return it.next();

    }

    public static List<LancherInfo> getLancherInfoList(Context context) {
        List<LancherInfo> lancherInfoList=new ArrayList<>();
        SparseArray<LancherInfo> temp=new SparseArray<>();
        String panel= SpUtil.getStringSp(context, Constant.PANEL);
        if(panel==null){
            panel="1";
        }
        List<LancherInfo> localInfos= DBManager.getInstance().queryLancherInfoByPage(panel);
        for(LancherInfo info:localInfos){
            temp.put(info.getPosition(),info);
        }
        for (int i = 0; i < PanelItemAdapter.EmptyListItemSize; i++) {
            if(temp.indexOfKey(i)>=0){
                lancherInfoList.add(temp.get(i));
            }else {
                lancherInfoList.add(null);
            }
        }
        temp.clear();
        localInfos.clear();
        return lancherInfoList;
    }
}
