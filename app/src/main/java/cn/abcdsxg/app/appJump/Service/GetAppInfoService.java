package cn.abcdsxg.app.appJump.Service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import cn.abcdsxg.app.appJump.Activity.ItemActivity;
import cn.abcdsxg.app.appJump.Data.Constant;
import cn.abcdsxg.app.appJump.Data.Utils.SpUtil;
import cn.abcdsxg.app.appJump.Data.greenDao.AppInfo;
import cn.abcdsxg.app.appJump.Data.Utils.SuUtils;
import cn.abcdsxg.app.appJump.R;

/**
 * Author : 时小光
 * Email  : abcdsxg@gmail.com
 * Blog   : http://www.abcdsxg.cn
 * Date   : 2016/7/25 18:22
 */
public class GetAppInfoService extends Service {

    private boolean breakTask;
    private AppInfo getAppInfo(){
        ActivityManager am= (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks=am.getRunningTasks(1);
        String pkgName = runningTasks.get(0).topActivity.getPackageName();
        String clsName = runningTasks.get(0).topActivity.getClassName();
        return new AppInfo(pkgName, clsName);
    }



    private void notifyFlush(AppInfo info) {
        //设置notification的点击intent跳转
        Intent notificationIntent=new Intent(this,ItemActivity.class);
        notificationIntent.putExtra("clsName",info.getClsName());
        notificationIntent.putExtra("pkgName",info.getPkgName());
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent=PendingIntent.getActivity(this
                ,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//        //设置粘贴板
//        if(!info.getClsName().equals(getPackageName()+".MainActivity")) {
//            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//            cm.setPrimaryClip(ClipData.newPlainText("content", info.getClsName()));
//            Toast.makeText(this, "类名复制成功", Toast.LENGTH_SHORT).show();
//        }
        //获取是否显示状态栏图标
        boolean showIcon= SpUtil.getBooleanSp(this, Constant.SHOWICON);
        //这里设置通知栏检测，逻辑取反
        breakTask= !SpUtil.getBooleanSp(this, Constant.SHOWCLSNAME);
        //设置notification并通知
        NotificationManager nf=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification=new NotificationCompat.Builder(getApplicationContext())
                .setAutoCancel(false)
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(showIcon?NotificationCompat.PRIORITY_MAX:NotificationCompat.PRIORITY_MIN)
                .setContentTitle(getString(R.string.notif_title))
                .setContentText(getString(R.string.notif_content)+info.getClsName())
                .setContentIntent(pendingIntent)
                .build();
        nf.notify(0,notification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
            runAppInfoTask();
        return START_REDELIVER_INTENT;
    }

    private void runAppInfoTask() {
        AppInfo info = null;
        //android5.0之后限制了获取最近任务栈，这里根据版本分情况获取
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP) {
            info=getAppInfo();
        }
        if(info==null){
            //如果未获取到就用命令获取，同时也是5.0以后的获取方式
            info = SuUtils.getAppInfo();
        }
        if(info==null){
            //获取AppInfo失败
            breakTask=true;
            Toast.makeText(getBaseContext(), R.string.getError,Toast.LENGTH_SHORT).show();
        }else{
            //更新通知栏
            notifyFlush(info);
        }
        //每秒获取一次
        Timer timer=new Timer();
        int flushTime=SpUtil.getIntSp(this, Constant.FLUSHTIME);
        if(flushTime<0){    flushTime=1000;    }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!breakTask) {
                    runAppInfoTask();
                }else {
                    NotificationManager nf=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    nf.cancelAll();
                    stopSelf();
                }
            }
        },flushTime);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {  return null;    }

}
