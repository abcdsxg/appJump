package cn.abcdsxg.app.appJump.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import cn.abcdsxg.app.appJump.Activity.ItemActivity;
import cn.abcdsxg.app.appJump.Base.BaseFragment;
import cn.abcdsxg.app.appJump.Data.Adapter.GridViewAdapter;
import cn.abcdsxg.app.appJump.Data.Utils.SuUtils;
import cn.abcdsxg.app.appJump.Data.Utils.ToolUtils;
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
    public static final String ISDESKTOP = "ISDESKTOP";
    @BindView(R.id.gridViewApp)
    GridView gridView;
    GridViewAdapter mAdapter;
    List<AppInfo> appInfos;
    private int mPageNum;
    private DBManager dbManager;
    boolean isDesktop;
    @Override
    public View setView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gridview, container, false);
    }

    public static Fragment newInstant(int page,boolean isDesktop) {
        Bundle args = new Bundle();
        args.putInt(PAGE_NUM, page);
        args.putBoolean(ISDESKTOP,isDesktop);
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
        isDesktop=getArguments().getBoolean(ISDESKTOP);
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
        if(!isDesktop){
            AppInfo appInfo = appInfos.get(pos);
            ToolUtils.sendShortcut(getActivity(),appInfo,false);
        }else {
            AppInfo appInfo = appInfos.get(pos);
            try {
                SuUtils.startApp(mApplication, appInfo.getPkgName(), appInfo.getClsName(), appInfo.getExtra());
            } catch (Exception e) {
                showToast(getString(R.string.DoesnotExitError));
            }
        }
    }

    @OnItemLongClick(R.id.gridViewApp)
    boolean onLongClickItem(int pos){
        if(isDesktop) {
            AppInfo appInfo = appInfos.get(pos);
            showDialog(appInfo);
        }
        return true;
    }

    //显示对话框选项
    AlertDialog longClickDialog;
    private void showDialog(final AppInfo appInfo) {
        View view=LayoutInflater.from(mApplication)
                .inflate(R.layout.dialog_longclick,null);
        TextView creatShortCut=(TextView)view.findViewById(R.id.creatShortCut);
        TextView edit=(TextView)view.findViewById(R.id.edit);
        longClickDialog=new AlertDialog.Builder(getContext())
                .setView(view)
                .show();
        creatShortCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                longClickDialog.dismiss();
                //创建快捷方式
                ToolUtils.sendShortcut(getActivity(),appInfo,true);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                longClickDialog.dismiss();
                Intent intent=new Intent(mApplication, ItemActivity.class);
                intent.putExtra("id",appInfo.getId());
                startActivity(intent);
            }
        });
    }


}
