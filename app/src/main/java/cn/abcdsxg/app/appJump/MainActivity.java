package cn.abcdsxg.app.appJump;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;
import cn.abcdsxg.app.appJump.Base.BaseActivity;
import cn.abcdsxg.app.appJump.Data.Adapter.MainAdapter;
import cn.abcdsxg.app.appJump.Fragment.DonateFragment;
import cn.abcdsxg.app.appJump.Fragment.HelpFragment;
import cn.abcdsxg.app.appJump.Fragment.MainFragment;
import cn.abcdsxg.app.appJump.Fragment.SettingFragment;
import cn.abcdsxg.app.appJump.Service.GetAppInfoService;

public class MainActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.main_content)
    FrameLayout mMainContent;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.drawLayout)
    DrawerLayout mDrawLayout;
    @BindView(R.id.fabBtn)
    FloatingActionButton fabBtn;
    private ActionBarDrawerToggle mDrawerToggle;


    private String mTitle;
    private List<String> list;
    private MainAdapter mAdapter;

    @Override
    public int getViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initService();
        setupDrawerContent();
    }

    private void initService() {
        Intent intent = new Intent(MainActivity.this, GetAppInfoService.class);
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
            public boolean onNavigationItemSelected(MenuItem item) {
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
                        MobclickAgent.onEvent(MainActivity.this, "ClickDonate");
                        break;
                    case R.id.action_settings:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_content, new SettingFragment()).commit();
                        mTitle = getString(R.string.Settings);
                        MobclickAgent.onEvent(MainActivity.this, "ClickSettings");
                        break;
                    case R.id.action_help:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_content, new HelpFragment()).commit();
                        mTitle = getString(R.string.help);
                        MobclickAgent.onEvent(MainActivity.this, "ClickHelp");
                        break;
                    case R.id.action_evaluation:
                        MobclickAgent.onEvent(MainActivity.this, "ClickEvaluation");
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("market://details?id=" +
                                getPackageName()));
                        startActivity(intent);
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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
