package cn.abcdsxg.app.appJump.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import java.util.List;
import butterknife.BindView;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import cn.abcdsxg.app.appJump.Activity.ItemActivity;
import cn.abcdsxg.app.appJump.Base.BaseFragment;
import cn.abcdsxg.app.appJump.Data.Adapter.GridViewAdapter;
import cn.abcdsxg.app.appJump.Data.Utils.SuUtils;
import cn.abcdsxg.app.appJump.Data.greenDao.AppInfo;
import cn.abcdsxg.app.appJump.Data.greenDao.DBManager;
import cn.abcdsxg.app.appJump.R;

/**
 * Author : 时小光
 * Email  : abcdsxg@gmail.com
 * Blog   : http://www.abcdsxg.cn
 * Date   : 2016/7/26 11:12
 */
public class GridViewFragment extends BaseFragment {

    private static final String TAG = "GridViewFragment";
    public static final String PAGE_NUM = "PAGE_NUM";
    @BindView(R.id.gridViewApp)
    GridView gridView;
    GridViewAdapter mAdapter;
    List<AppInfo> appInfos;
    private int mPageNum;
    private DBManager dbManager;
    @Override
    public View setView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gridview, container, false);
    }

    public static Fragment newInstant(int page) {
        Bundle args = new Bundle();
        args.putInt(PAGE_NUM, page);
        GridViewFragment pageFragment = new GridViewFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAppInfos();
    }

    private void getAppInfos() {
        dbManager=DBManager.getInstance();
        mPageNum = getArguments().getInt(PAGE_NUM);
        appInfos= dbManager.queryAppInfoListByPage(mPageNum);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter=new GridViewAdapter(mApplication,appInfos);
        gridView.setAdapter(mAdapter);

    }

    @OnItemClick(R.id.gridViewApp)
    void onClickItem(int pos){
        AppInfo appInfo=appInfos.get(pos);
        String pkgName=appInfo.getPkgName();
        //检查应用是否停用，并启用已停用的应用
        PackageManager pm=mApplication.getPackageManager();
        int statue=pm.getApplicationEnabledSetting(pkgName);
        if(statue!=PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            SuUtils.enableApp(pkgName);
        }
        SuUtils.startApp(appInfo.getPkgName(),appInfo.getClsName(),null);
    }

    @OnItemLongClick(R.id.gridViewApp)
    boolean onLongClickItem(int pos){
        AppInfo appInfo=appInfos.get(pos);
        Intent intent=new Intent(mApplication, ItemActivity.class);
        intent.putExtra("id",appInfo.getId());
        startActivity(intent);
        return true;
    }


}
