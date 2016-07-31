package cn.abcdsxg.app.appJump.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.abcdsxg.app.appJump.Base.BaseActivity;
import cn.abcdsxg.app.appJump.Data.greenDao.AppInfo;
import cn.abcdsxg.app.appJump.Data.greenDao.DBManager;
import cn.abcdsxg.app.appJump.R;

/**
 * Author : 时小光
 * Email  : abcdsxg@gmail.com
 * Blog   : http://www.abcdsxg.cn
 * Date   : 2016/7/25 10:18
 */
public class ItemActivity extends BaseActivity {

    @BindView(R.id.titleBar)
    TextView titleBar;
    @BindView(R.id.btnFinish)
    Button btnFinish;
    @BindView(R.id.editAppPkgName)
    EditText editAppPkgName;
    @BindView(R.id.editAppClsName)
    EditText editAppClsName;
    @BindView(R.id.editAppName)
    EditText editAppName;
    @BindView(R.id.editPos)
    EditText editPos;
    @BindView(R.id.editPage)
    EditText editPage;

    private DBManager dbManager;
    private Long id;

    @Override
    public int getViewId() {
        return R.layout.activity_item;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }

    private void getData() {
        dbManager = DBManager.getInstance();
        Intent intent = getIntent();
        id = intent.getLongExtra("id", -1);
        if (id != -1) {
            //编辑appInfo的情况
            AppInfo appInfo = dbManager.queryAppInfo(id).get(0);
            editAppPkgName.setText(appInfo.getPkgName());
            editAppClsName.setText(appInfo.getClsName());
            editAppName.setText(appInfo.getAppName());
            editPos.setText(String.valueOf(appInfo.getPagePos()));
            editPage.setText(String.valueOf(appInfo.getPage()));
        }
    }

    @OnClick(R.id.btnFinish)
    void clickFinish() {
        String pkgName = editAppPkgName.getText().toString();
        String clsName = editAppClsName.getText().toString();
        String appName = editAppName.getText().toString();
        int page,pos;
        try {
            page = Integer.valueOf(editPage.getText().toString());
            pos = Integer.valueOf(editPos.getText().toString());
        }catch(Exception e){
            showToast("最后两行输入的格式有误");
            return ;
        }
        if (TextUtils.isEmpty(pkgName) || TextUtils.isEmpty(pkgName)
                || TextUtils.isEmpty(appName) || pos==0) {
            showToast("有内容未输入，请检查！");
            return;
        }
        //编辑appInfo的情况
        if (id != -1) {
            dbManager.updateAppInfo(new AppInfo(id, page, pos ,pkgName,clsName,appName));
        }else{
            Log.e("tag", "insertAppInfo: " );
            dbManager.insertAppInfo(new AppInfo(pkgName,clsName,appName,page,pos));
        }
        finish();

    }
}
