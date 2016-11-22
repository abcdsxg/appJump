package cn.abcdsxg.app.appJump;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import butterknife.BindView;
import cn.abcdsxg.app.appJump.Activity.EditPanelActivity;
import cn.abcdsxg.app.appJump.Activity.PreferenceSettingActivity;
import cn.abcdsxg.app.appJump.Base.BaseActivity;
import cn.abcdsxg.app.appJump.Data.Constant;
import cn.abcdsxg.app.appJump.Data.Utils.SpUtil;
import cn.abcdsxg.app.appJump.Data.Utils.SuUtils;
import cn.abcdsxg.app.appJump.Data.Utils.ToolUtils;
import cn.abcdsxg.app.appJump.Data.Utils.Update;
import cn.abcdsxg.app.appJump.Data.greenDao.AppInfo;
import cn.abcdsxg.app.appJump.Data.greenDao.DBManager;
import cn.abcdsxg.app.appJump.Fragment.DonateFragment;
import cn.abcdsxg.app.appJump.Fragment.HelpFragment;
import cn.abcdsxg.app.appJump.Fragment.MainFragment;
import cn.abcdsxg.app.appJump.Service.GetAppInfoService;
import cn.abcdsxg.app.appJump.Service.TouchService;

public class MainActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.main_content)
    FrameLayout mMainContent;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.drawLayout)
    DrawerLayout mDrawLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mTitle;

    @Override
    public int getViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        ToolUtils.checkPermission(this);
        checkUpdate();
        initService();
        setupDrawerContent();
    }

    private void checkUpdate() {
        Update update = new Update(this, 0);
        update.checkUpdateInfo();
    }

    private void initData() {
        int isFirst=SpUtil.getIntSp(this,Constant.ISFIRST);
        if(isFirst==-1){
            if(SuUtils.isRoot()) {
                initDB();
                SpUtil.saveSp(MainActivity.this, Constant.ISFIRST, 1);
            }else{
                showToast(getString(R.string.rootTip));

            }
        } else if(isFirst==1){
            SpUtil.saveSp(this, Constant.FLUSHTIME, "1000");
            SpUtil.saveSp(this, Constant.PANEL, "1");
            SpUtil.saveSp(this, Constant.SHOCK, true);
            SpUtil.saveSp(this, Constant.SLIDEPOS, "1");
            //新增配置，为了兼容之前的版本
            SpUtil.saveSp(MainActivity.this, Constant.ISFIRST, 2);
        }
    }

    private void initDB() {
        Log.e("tag", "initDB: " );
        DBManager dbManager=DBManager.getInstance();
        List<AppInfo> appInfos=new ArrayList<>();
        appInfos.add(new AppInfo(Constant.PACKAGE_ZFB,Constant.ZFB_MONEY,getString(R.string.zfb_zz),0,1,null));
        appInfos.add(new AppInfo(Constant.PACKAGE_ZFB,Constant.ZFB_MONEY_CODE,getString(R.string.zfb_fkm),0,2,null));
        appInfos.add(new AppInfo(Constant.PACKAGE_ZFB,Constant.ZFB_SCAN,getString(R.string.zfb_sys),0,3,null));
        appInfos.add(new AppInfo(Constant.PACKAGE_WX,Constant.WX_MONEY,getString(R.string.wx_zz),0,4,null));
        appInfos.add(new AppInfo(Constant.PACKAGE_WX,Constant.WX_MONEY_CODE,getString(R.string.wx_sfk),0,5,null));
        appInfos.add(new AppInfo(Constant.PACKAGE_WX,Constant.WX_SCAN,getString(R.string.wx_sys),0,6,null));
        dbManager.insertAppInfoList(appInfos);
        SpUtil.saveSp(this,Constant.MAXTABNUM,1);
        SpUtil.saveSp(this,"0",getString(R.string.tabTitle));
        SpUtil.saveSp(this, Constant.FLUSHTIME, "1000");
        SpUtil.saveSp(this, Constant.SHOWCLSNAME, true);
        SpUtil.saveSp(this, Constant.SHOWICON, false);
        SpUtil.saveSp(this, Constant.PANEL, "1");
        SpUtil.saveSp(this, Constant.SHOCK, true);
        SpUtil.saveSp(this, Constant.SLIDEPOS, "1");
    }

    private void initService() {
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, GetAppInfoService.class);
                startService(intent);
            }
        },2000);
        Intent intent = new Intent(MainActivity.this, TouchService.class);
        startService(intent);
    }

    private void initView() {
        mTitle = getString(R.string.app_name);
        mToolbar.setTitle(mTitle);
        setSupportActionBar(mToolbar);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawLayout, mToolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
                getSupportActionBar().setTitle(mTitle);
            }
        };
        mDrawerToggle.syncState();
        mDrawLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, new MainFragment()).commit();
    }

    private void setupDrawerContent() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setCheckable(true);
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_main:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_content, new MainFragment()).commit();
                        mTitle = getString(R.string.app_name);
                        break;
                    case R.id.action_remove:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_content, new DonateFragment()).commit();
                        mTitle = getString(R.string.Donate);
                        MobclickAgent.onEvent(MainActivity.this, "Donate");
                        break;
                    case R.id.action_settings:
                        Intent settingIntent=new Intent(MainActivity.this, PreferenceSettingActivity.class);
                        startActivity(settingIntent);
                        MobclickAgent.onEvent(MainActivity.this, "Settings");
                        break;
                    case R.id.action_help:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_content, new HelpFragment()).commit();
                        mTitle = getString(R.string.help);
                        MobclickAgent.onEvent(MainActivity.this, "Help");
                        break;
                    case R.id.action_evaluation:
                        MobclickAgent.onEvent(MainActivity.this, "Evaluation");
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("market://details?id=" +
                                getPackageName()));
                        try {
                            startActivity(intent);
                        }catch (Exception ignored){

                        }
                        break;
                    case R.id.action_exit:
                        stopService(new Intent(MainActivity.this,GetAppInfoService.class));
                        NotificationManager nf=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        nf.cancelAll();
                        finish();
                        System.exit(0);
                        break;
                }
                mDrawLayout.closeDrawers();
                return true;
            }
        });
        View navView = mNavigationView.inflateHeaderView(R.layout.navigation_header);
        ImageView head_pic = (ImageView) navView.findViewById(R.id.head_pic);
        Glide.with(this)
                .load(R.mipmap.ic_launcher)
                .into(head_pic);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(MainActivity.this, TouchService.class);
            startService(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

}
