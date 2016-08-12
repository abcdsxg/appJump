package cn.abcdsxg.app.appJump.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.abcdsxg.app.appJump.Base.BaseActivity;
import cn.abcdsxg.app.appJump.Data.Constant;
import cn.abcdsxg.app.appJump.Data.Utils.SpUtil;
import cn.abcdsxg.app.appJump.Data.Utils.ToolUtils;
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
    TextInputEditText editAppPkgName;
    @BindView(R.id.editAppClsName)
    EditText editAppClsName;
    @BindView(R.id.editAppName)
    EditText editAppName;
    @BindView(R.id.editPos)
    EditText editPos;
    @BindView(R.id.editPage)
    EditText editPage;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.btnBack)
    ImageView btnBack;

    private DBManager dbManager;
    private Long id;
    private StringBuilder sb=new StringBuilder();
    private StringBuilder jsonSb;
    private StringBuilder proSb=new StringBuilder();
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
            //由于page页数从0开始的，需要做下处理
            editPage.setText(String.valueOf(appInfo.getPage() + 1));
            titleBar.setText("编辑当前AppInfo");
        } else {
            //首先判断是否有传值
            try {
                File file = new File(Environment.getExternalStorageDirectory() + "/temp.json");
                if (file.exists()) {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    jsonSb = new StringBuilder("");
                    String str = null;
                    while ((str = reader.readLine()) != null) {
                        jsonSb.append(str);
                    }
                }
                Log.e("tag", "jsonSb: "+jsonSb );
                if (!TextUtils.isEmpty(jsonSb.toString())) {
                    JSONObject jsonObject = new JSONObject(jsonSb.toString());
                    if (intent.getStringExtra("pkgName").equals(jsonObject.getString("pkgName"))) {
                        Iterator it = jsonObject.keys();
                        sb = new StringBuilder();
                        proSb=new StringBuilder();
                        while (it.hasNext()) {
                            String key = it.next().toString();
                            Log.e("tag", "key: "+key );
                            if (key.contains("type")) {
                                proSb.append(" -t ").append(jsonObject.getString(key));
                            } else if (key.contains("data")) {
                                proSb.append(" -d ").append(jsonObject.getString(key));
                            } else if (key.contains("action")) {
                                proSb.append(" -a ").append(jsonObject.getString(key));
                            }else if (key.contains("String")) {
                                JSONObject subJson=jsonObject.getJSONObject(key);
                                key= ToolUtils.getKeyFromJson(subJson);
                                Log.e("tag", "subkey: "+key );
                                sb.append(" -es ").append(key).append(" ").append(subJson.getString(key));
                            } else if (key.contains("Float")) {
                                JSONObject subJson=jsonObject.getJSONObject(key);
                                key= ToolUtils.getKeyFromJson(subJson);
                                sb.append(" -ef ").append(key).append(" ").append(subJson.getString(key));
                            } else if (key.contains("Integer")) {
                                JSONObject subJson=jsonObject.getJSONObject(key);
                                key= ToolUtils.getKeyFromJson(subJson);
                                sb.append(" -ei ").append(key).append(" ").append(subJson.getString(key));
                            } else if (key.contains("Boolean")) {
                                JSONObject subJson=jsonObject.getJSONObject(key);
                                key= ToolUtils.getKeyFromJson(subJson);
                                sb.append(" -ez ").append(key).append(" ").append(subJson.getString(key));
                            } else if (key.contains("Long")) {
                                JSONObject subJson=jsonObject.getJSONObject(key);
                                key= ToolUtils.getKeyFromJson(subJson);
                                sb.append(" -el ").append(key).append(" ").append(subJson.getString(key));
                            } else if (key.contains("longTime")) {
                                sb.append(" -el ").append(jsonObject.getString("longTime")).append(" ").append(System.currentTimeMillis());
                            } else {
                                try {
                                    JSONObject subJson = jsonObject.getJSONObject(key);
                                    key = ToolUtils.getKeyFromJson(subJson);
                                    sb.append(" -es ").append(key).append(" ").append(subJson.getString(key));
                                }catch (Exception e){
                                    Log.e("tag", "errorKey: ");
                                    sb.append(" -es ").append(key).append(" ").append(jsonObject.getString(key));
                                }
                            }
                            Log.e("tag", "getData: "+sb.toString() );
                        }
                    }
                }
                }catch(Exception e){
                    e.printStackTrace();
                }
            //从通知栏点击进入的情况
            if(intent.hasExtra("pkgName")){
                String pkgName=intent.getStringExtra("pkgName");
                String clsName=intent.getStringExtra("clsName");
                editAppPkgName.setText(pkgName);
                editAppClsName.setText(clsName);
            }
            btnDelete.setVisibility(View.GONE);
            titleBar.setText("自定义AppInfo");
        }
    }

    @OnClick(R.id.btnFinish)
    void clickFinish() {
        String pkgName = editAppPkgName.getText().toString();
        String clsName = editAppClsName.getText().toString();
        String appName = editAppName.getText().toString();
        int page, pos;
        try {
            page = Integer.valueOf(editPage.getText().toString());
            pos = Integer.valueOf(editPos.getText().toString());
        } catch (Exception e) {
            showToast("最后两行输入的格式有误");
            return;
        }
        if (TextUtils.isEmpty(pkgName) || TextUtils.isEmpty(pkgName)
                || TextUtils.isEmpty(appName) || pos == 0) {
            showToast("有内容未输入，请检查！");
            return;
        }
        int MaxTabNum = SpUtil.getIntSp(this, "MaxTabNum");
        if (page > MaxTabNum) {
            showToast("填写的标签页数不能大于当前总Tab数！");
            return;
        } else {
            //由于page页数从0开始的，用户输入的最终page页需要减去1
            page--;
        }
        //编辑appInfo的情况
        if (id != -1) {
            dbManager.updateAppInfo(new AppInfo(id, page, pos, pkgName, clsName, appName,null));
        } else {
            String extra=proSb.append(sb.toString()).toString();
            dbManager.insertAppInfo(new AppInfo(pkgName, clsName, appName, page, pos,extra));
            HashMap<String,String> map = new HashMap<>();
            map.put("pkgName",pkgName);
            map.put("clsName",clsName);
            map.put("appName",appName);
            MobclickAgent.onEvent(ItemActivity.this, "newCustome",map);
        }
        finish();

    }

    @OnClick(R.id.btn_delete)
    void deleteAppInfo() {
        showDialog();
    }

    private void showDialog() {
        //弹出带编辑框的对话框
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("确定删除？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        dbManager.deleteAppInfo(id);
                        finish();
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

    @OnClick(R.id.btnBack)
    void back(){
        finish();
    }
}
