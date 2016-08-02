package cn.abcdsxg.app.appJump.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import cn.abcdsxg.app.appJump.Base.BaseActivity;
import cn.abcdsxg.app.appJump.Data.Utils.SuUtils;
import cn.abcdsxg.app.appJump.Data.greenDao.AppInfo;
import cn.abcdsxg.app.appJump.Data.greenDao.DBManager;

/**
 * Author : 时小光
 * Email  : abcdsxg@gmail.com
 * Blog   : http://www.abcdsxg.cn
 * Date   : 2016/8/2 16:17
 */
public class ShortCutActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        if(intent!=null){
            Long id=intent.getLongExtra("id",-1);
            Log.e("tag", "id: "+id );
            DBManager dbManager=DBManager.getInstance();
            List<AppInfo> list=dbManager.queryAppInfo(id);
            if(list!=null && list.size()>0){
                AppInfo appInfo=list.get(0);
                SuUtils.startApp(this,appInfo.getPkgName(),appInfo.getClsName(),null);
                finish();
                System.exit(0);
            }
        }
    }
}
