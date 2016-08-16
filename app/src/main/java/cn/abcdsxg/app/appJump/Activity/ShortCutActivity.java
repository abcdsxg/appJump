package cn.abcdsxg.app.appJump.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TableLayout;

import java.util.List;

import cn.abcdsxg.app.appJump.Base.BaseActivity;
import cn.abcdsxg.app.appJump.Data.Adapter.GridViewFragmentPageAdapter;
import cn.abcdsxg.app.appJump.Data.Utils.SuUtils;
import cn.abcdsxg.app.appJump.Data.greenDao.AppInfo;
import cn.abcdsxg.app.appJump.Data.greenDao.DBManager;
import cn.abcdsxg.app.appJump.R;

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
        if(Intent.ACTION_CREATE_SHORTCUT.equals(intent.getAction())){
            setContentView(R.layout.activity_shortcut);
            TabLayout tabLayout=(TabLayout)findViewById(R.id.tabLayout);
            ViewPager viewPager=(ViewPager)findViewById(R.id.viewpager);
            GridViewFragmentPageAdapter fragmentPagerAdapter = new GridViewFragmentPageAdapter(getSupportFragmentManager(),ShortCutActivity.this,false);
            viewPager.setAdapter(fragmentPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        }else{
            Long id=intent.getLongExtra("id",-1);
            Log.e("tag", "id: "+id );
            DBManager dbManager=DBManager.getInstance();
            List<AppInfo> list=dbManager.queryAppInfo(id);
            if(list!=null && list.size()>0){
                AppInfo appInfo=list.get(0);
                SuUtils.startApp(this,appInfo.getPkgName(),appInfo.getClsName(),appInfo.getExtra());
                finish();
                System.exit(0);
            }
        }
    }
}
