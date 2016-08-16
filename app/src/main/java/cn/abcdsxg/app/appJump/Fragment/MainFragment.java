package cn.abcdsxg.app.appJump.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.umeng.analytics.MobclickAgent;
import butterknife.BindView;
import butterknife.OnClick;
import cn.abcdsxg.app.appJump.Activity.ItemActivity;
import cn.abcdsxg.app.appJump.Base.BaseFragment;
import cn.abcdsxg.app.appJump.Data.Adapter.GridViewFragmentPageAdapter;
import cn.abcdsxg.app.appJump.Data.Constant;
import cn.abcdsxg.app.appJump.Data.Utils.SpUtil;
import cn.abcdsxg.app.appJump.R;

/**
 * Author : 时小光
 * Email  : abcdsxg@gmail.com
 * Blog   : http://www.abcdsxg.cn
 * Date   : 2016/7/26 09:20
 */
public class MainFragment extends BaseFragment {


    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.fabNewCustome)
    FloatingActionButton fabNewCustome;
    @BindView(R.id.fabNewTab)
    FloatingActionButton fabNewTab;
    @BindView(R.id.menu)
    FloatingActionMenu menu;
    @BindView(R.id.fabEditTab)
    FloatingActionButton fabEditTab;


    private GridViewFragmentPageAdapter fragmentPagerAdapter;
    private final static int ADDNEWTAB=1;
    private final static int EDITTAB=2;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentPagerAdapter = new GridViewFragmentPageAdapter(getFragmentManager(), mApplication,true);
    }

    @Override
    public View setView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewpager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

    }

    //fab按钮的点击事件
    @OnClick({R.id.fabNewCustome, R.id.fabNewTab,R.id.fabEditTab})
    void fabBtnClick(View view) {
        switch (view.getId()) {
            case R.id.fabNewCustome:
                addNewAppInfo();
                break;
            case R.id.fabNewTab:
                showDialog("添加新标签页",ADDNEWTAB);
                break;

            case R.id.fabEditTab:
                showDialog("编辑标签名",EDITTAB);
                break;
        }
        menu.toggle(true);
    }


    private void addNewAppInfo() {
        Intent intent=new Intent(mApplication, ItemActivity.class);
        startActivity(intent);
    }


    private void showDialog(String tilte, final int flag) {
        View dialogView = LayoutInflater.from(mApplication).inflate(R.layout.dialog_edit, null);
        final EditText editText = (EditText) dialogView.findViewById(R.id.editText);
        if(flag==EDITTAB){
            editText.setText(SpUtil.getStringSp(mApplication, String.valueOf(viewpager.getCurrentItem())));
        }
        //弹出带编辑框的对话框
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(tilte)
                .setView(dialogView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        String text = editText.getText().toString();
                        if (TextUtils.isEmpty(text)) {
                            showToast("内容不能为空！");
                        } else {
                            if(flag==ADDNEWTAB) {
                                //添加新tab页并保存tab数
                                int maxTabNum=SpUtil.getIntSp(mApplication,Constant.MAXTABNUM);
                                SpUtil.saveSp(mApplication, Constant.MAXTABNUM,maxTabNum+1);
                                SpUtil.saveSp(mApplication, String.valueOf(maxTabNum), text);
                                fragmentPagerAdapter.notifyDataSetChanged();
                                MobclickAgent.onEvent(getContext(), "newTab");
                            }else{
                                //编辑tab标题
                                SpUtil.saveSp(mApplication, String.valueOf(viewpager.getCurrentItem()), text);
                            }
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(fragmentPagerAdapter!=null) {
            fragmentPagerAdapter.notifyDataSetChanged();
        }
    }
}
