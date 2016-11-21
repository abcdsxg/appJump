package cn.abcdsxg.app.appJump.Activity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.abcdsxg.app.appJump.Base.BaseActivity;
import cn.abcdsxg.app.appJump.Data.Adapter.DialogItemAdapter;
import cn.abcdsxg.app.appJump.Data.Adapter.GridViewAdapter;
import cn.abcdsxg.app.appJump.Data.Adapter.PanelItemAdapter;
import cn.abcdsxg.app.appJump.Data.Constant;
import cn.abcdsxg.app.appJump.Data.Utils.SpUtil;
import cn.abcdsxg.app.appJump.Data.Utils.ToolUtils;
import cn.abcdsxg.app.appJump.Data.greenDao.AppInfo;
import cn.abcdsxg.app.appJump.Data.greenDao.DBManager;
import cn.abcdsxg.app.appJump.Data.greenDao.LancherInfo;
import cn.abcdsxg.app.appJump.R;

/**
 * Author : 时小光
 * Email  : abcdsxg@gmail.com
 * Blog   : http://www.abcdsxg.cn
 * Date   : 16-11-17 16:41
 */
public class EditPanelActivity extends BaseActivity {

    private static final String TAG = "EditPanelActivity";
    @BindView(R.id.gridView)
    GridView gridView;
    private PanelItemAdapter adapter;
    private List<LancherInfo> lancherInfoList;
    private int itemType;
    private List<AppInfo> appInfos;
    private AlertDialog dialog;
    public static final int TYPE_CHOOSE=100;
    public static final int TYPE_SHOWAPP=200;
    private String activityType;
    private String newPanel;
    @Override
    public int getViewId() {
        return R.layout.activity_addpanel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initEvents();
    }


    private void initData() {
        if(getIntent()!=null) {
            activityType = getIntent().getStringExtra(Constant.PANEL);
        }
        lancherInfoList = new ArrayList<>();
        if(activityType.equals(Constant.EDIT_PANEL)) {
            lancherInfoList=ToolUtils.getLancherInfoList(this);
        }else if(activityType.equals(Constant.ADD_PANEL)){
            showDialog("请输入新面板的名字");
            for (int i = 0; i < PanelItemAdapter.EmptyListItemSize; i++) {
                lancherInfoList.add(null);
            }
        }
        adapter=new PanelItemAdapter(this,lancherInfoList,false);
        gridView.setAdapter(adapter);
    }


    private void showDialog(String tilte) {
            View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit, null);
            final EditText editText = (EditText) dialogView.findViewById(R.id.editText);
            //弹出带编辑框的对话框
            android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(this)
                    .setTitle(tilte)
                    .setView(dialogView)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            String text = editText.getText().toString();
                            if (TextUtils.isEmpty(text)) {
                                showToast(getString(R.string.cannotEmpty));
                            } else {
                                saveSp(text);
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    })
                    .create();
            dialog.show();
    }

    private void saveSp(String text) {
        int panelCount=SpUtil.getIntSp(this,Constant.PANELCOUNT);
        SpUtil.saveSp(this,Constant.PANELCOUNT,++panelCount);
        SpUtil.saveSp(this,Constant.PANEL+panelCount,text);
    }

    private void initEvents() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                showChooSeDialog(pos);
            }
        });
    }

    private void showChooSeDialog(int pos) {
        View dialogView=initShowAppView(pos,TYPE_CHOOSE);
        dialog= new AlertDialog.Builder(this)
                .setTitle("请选择一种添加方式")
                .setView(dialogView)
                .create();
        dialog.show();
    }

    public void showAppDialog(int pos) {
        View dialogView=initShowAppView(pos,TYPE_SHOWAPP);
        dialog= new AlertDialog.Builder(this)
                .setTitle("请选择一个应用程序")
                .setView(dialogView)
                .create();
        dialog.show();
    }

    private View initShowAppView(final int pos,int type) {
        View dialogView= LayoutInflater.from(this)
                .inflate(R.layout.activity_addpanel,null);
        GridView gridView= (GridView) dialogView.findViewById(R.id.gridView);
        appInfos=new ArrayList<>();
        switch (type){
            case TYPE_CHOOSE:
                AppInfo app=new AppInfo();
                app.setAppName("应用程序");
                AppInfo shortCut=new AppInfo();
                shortCut.setAppName("快捷方式");
                appInfos.add(app);
                appInfos.add(shortCut);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        switch (i){
                            case 0:
                                itemType=1;
                                closeDialog();
                                showAppDialog(pos);
                                break;
                            case 1:
                                itemType=2;
                                Intent shortcutsIntent = new Intent(Intent.ACTION_CREATE_SHORTCUT);
                                startActivityForResult(shortcutsIntent,pos);
                                closeDialog();
                                break;
                        }
                    }
                });
                DialogItemAdapter dialogItemAdapter=new DialogItemAdapter(this,appInfos);
                gridView.setAdapter(dialogItemAdapter);
                break;
            case TYPE_SHOWAPP:
                //获取手机上已安装的应用
                Intent intent=new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                List<ResolveInfo> resolveInfos=getPackageManager().queryIntentActivities(intent,0);
                for(ResolveInfo info:resolveInfos){
                    AppInfo appInfo=new AppInfo();
                    appInfo.setClsName(info.activityInfo.name);
                    appInfo.setAppName(info.loadLabel(getPackageManager()).toString());
                    appInfo.setPkgName(info.activityInfo.packageName);
                    Log.e(TAG, "appName: "+appInfo.getAppName()+"  PkgName "+appInfo.getPkgName()+"  clsName "+appInfo.getClsName() );
                    appInfos.add(appInfo);
                }
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int appPos, long l) {
                        AppInfo appInfo=appInfos.get(appPos);
                        Intent intent=new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        // 设置图标
                        Drawable drawableIcon=ToolUtils.getAppIcon(EditPanelActivity.this,appInfo.getPkgName());
                        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON,ToolUtils.drawableToBitamp(drawableIcon));
                        intent.setComponent(new ComponentName(appInfo.getPkgName(),appInfo.getClsName()));
                        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,intent);
                        saveIntent(pos,appPos,intent);
                        closeDialog();

                    }
                });
                GridViewAdapter gridViewAdapter=new GridViewAdapter(this,appInfos);
                gridView.setAdapter(gridViewAdapter);
                break;
        }

        return dialogView;
    }

    private void closeDialog() {
        if(dialog.isShowing()){
            dialog.dismiss();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        saveIntent(requestCode,resultCode,data);
    }
    private void saveIntent(int requestCode, int resultCode, Intent data) {
        if(data==null){
            return;
        }
        Intent shortCutIntent=data.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT);
        String intent=shortCutIntent.toUri(0);
        Log.e(TAG, "toUri: "+intent );
        String name=data.getStringExtra(Intent.EXTRA_SHORTCUT_NAME);

        Bitmap bmp=data.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON);
        byte[] iconByte=null;
        int iconType=0;
        if(bmp!=null) {
            iconByte = ToolUtils.Bitmap2Bytes((Bitmap) data.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON));
            iconType=1;
        }

        String pkgName=null;
        if (data.getComponent()!=null){
            pkgName=data.getComponent().getPackageName();
        }

        Intent.ShortcutIconResource intentRes=data.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE);
        String iconRes=null,iconPgk=null;
        if(intentRes!=null){
            iconType=2;
            iconRes=intentRes.resourceName;
            iconPgk=intentRes.packageName;
        }

        Log.e(TAG, "flag: "+data.getFlags());
        int flag=data.getFlags();

        Log.e(TAG, "uri: "+data.getData());
        String uri=data.getData()==null?null:data.getData().toString();
        //以下分情况考虑
        if(itemType==1) {
                name = appInfos.get(resultCode).getAppName();
        }
        newPanel = SpUtil.getStringSp(this, Constant.PANEL);
        if(activityType.equals(Constant.ADD_PANEL)) {
            newPanel = String.valueOf(Integer.valueOf(newPanel) + 1);
        }
        long id=Long.valueOf(newPanel+requestCode);
        LancherInfo info=new LancherInfo(
                id,name, intent, pkgName,itemType,iconType,iconPgk,iconRes,iconByte,uri,flag,System.currentTimeMillis(),requestCode,newPanel);

        lancherInfoList.set(requestCode, info);
        adapter.notifyDataSetChanged();
        DBManager dbManager=DBManager.getInstance();
        List<LancherInfo> localInfos = DBManager.getInstance().queryLancherInfoByPage(newPanel);
        if(requestCode< localInfos.size()) {
            dbManager.updateLancherInfo(info);
        }else{
            dbManager.insertLancherInfo(info);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && activityType.equals(Constant.ADD_PANEL)) {
            AlertDialog dialog=new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("是否将当前新增面板设置为默认面板?")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SpUtil.saveSp(EditPanelActivity.this,Constant.PANEL,newPanel);
                            finish();
                        }
                    })
                    .setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    }).create();
            dialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }
}
