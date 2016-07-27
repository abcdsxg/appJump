package cn.abcdsxg.app.appJump.Data.Utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.abcdsxg.app.appJump.R;
import okhttp3.Call;

public class Update  {
    private Context mContext;

    //返回的安装包url
    private String apkUrl ;

    private String saveFileName;

    private ProgressDialog downloadDialog;

    private int Max;
    private static final int DOWN_UPDATE = 1;

    private static final int DOWN_OVER = 2;

    private int progress;
    //更新的标记，0为不显示toast，1为显示;
    private int statue;

    private boolean interceptFlag = false;


    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    downloadDialog.setProgress(progress);
                    break;
                case DOWN_OVER:
                    downloadDialog.dismiss();
                    installApk();
                    break;
                case 3:
                    showNoticeDialog(msg.obj.toString());
                    break;
                case 4:
                    Toast.makeText(mContext, R.string.theNewVersion, Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    new Thread(){
                        public void run(){
                            try {
                                    /* 下载包安装路径 */
                                String savePath = Environment
                                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();

                                saveFileName = savePath + "/SyncSend.apk";
                                URL url = new URL(apkUrl);
                                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                                conn.connect();
                                Max= conn.getContentLength();
                                InputStream is = conn.getInputStream();
                                File file = new File(savePath);
                                if(!file.exists()){
                                    file.mkdir();
                                }
                                String apkFile = saveFileName;
                                File ApkFile = new File(apkFile);
                                Log.e("file",ApkFile.getAbsolutePath());
                                FileOutputStream fos = new FileOutputStream(ApkFile);
                                int count = 0;
                                byte buf[] = new byte[1024];
                                do{
                                    int numread = is.read(buf);
                                    count += numread;
                                    progress =(int)(((float)count / Max) * 100);
                                    //更新进度
                                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                                    if(numread <= 0){
                                        //下载完成通知安装
                                        mHandler.sendEmptyMessage(DOWN_OVER);
                                        break;
                                    }
                                    fos.write(buf,0,numread);
                                }while(!interceptFlag);//点击取消就停止下载.

                                fos.close();
                                is.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    break;
                default:
                    break;
            }
        }
    };

    public  Update(Context context,int statue) {
        this.mContext = context;
        this.statue=statue;
    }
    private static String getVersion(Context context)//获取版本号
    {
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();

        }
        return "unknow";
    }
    //外部接口让主Activity调用
    public void checkUpdateInfo(){
        getUpdateInfo();
    }

    private void getUpdateInfo(){
        OkHttpUtils.get()
                .url("http://www.abcdsxg.cn/app/update.php?version="
                        + getVersion(mContext)+"&pkg="+mContext.getPackageName())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response,int id) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String content=jsonObject.getString("msg");
                            apkUrl=jsonObject.getString("url");

                            if (content.equals("none")) {
                                if (statue == 1) {
                                    mHandler.sendEmptyMessage(4);
                                }
                            } else {
                                Message msg=new Message();
                                msg.what=3;
                                msg.obj=content;
                                mHandler.sendMessage(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void showNoticeDialog(String s){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle(R.string.updateDialogTitle)
                .setMessage(s)
                .setPositiveButton(R.string.updateText,
                        new OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                                showDownloadDialog();
                            }
                        })
                .setNegativeButton(R.string.nextTime, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    private void showDownloadDialog(){
        ProgressDialog builder = new ProgressDialog(mContext);
        builder.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        builder.setTitle(R.string.updateDialogTitle);
        builder.setButton(ProgressDialog.BUTTON_NEGATIVE, mContext.getString(R.string.cancel), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag = true;
            }
        });

        downloadDialog = builder;
        downloadDialog.setCancelable(true);
        downloadDialog.setCanceledOnTouchOutside(false);
        downloadDialog.show();
        mHandler.sendEmptyMessage(5);
    }

    private void installApk(){
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);

    }


}
