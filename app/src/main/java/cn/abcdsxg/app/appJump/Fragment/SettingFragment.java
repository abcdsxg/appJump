//package cn.abcdsxg.app.appJump.Fragment;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.AppCompatEditText;
//import android.support.v7.widget.SwitchCompat;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.Toast;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import cn.abcdsxg.app.appJump.Base.BaseFragment;
//import cn.abcdsxg.app.appJump.Data.Constant;
//import cn.abcdsxg.app.appJump.Data.Utils.SpUtil;
//import cn.abcdsxg.app.appJump.R;
//import cn.abcdsxg.app.appJump.Service.GetAppInfoService;
//
///**
// * Author : 时小光
// * Email  : abcdsxg@gmail.com
// * Blog   : http://www.abcdsxg.cn
// * Date   : 2016/6/7 18:02
// */
//public class SettingFragment extends BaseFragment {
//
//    @BindView(R.id.switchShowIcon)
//    SwitchCompat switchShowIcon;
//    @BindView(R.id.editFlushTime)
//    AppCompatEditText editFlushTime;
//    @BindView(R.id.switchShowClsName)
//    SwitchCompat switchShowClsName;
//    @BindView(R.id.btn_save)
//    Button btnSave;
//
//    @Override
//    public View setView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_setting, container, false);
//    }
//
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        getSP();
//    }
//
//    private void getSP() {
//        switchShowIcon.setChecked(SpUtil.getBooleanSp(mApplication,Constant.SHOWICON));
//        switchShowClsName.setChecked(SpUtil.getBooleanSp(mApplication,Constant.SHOWCLSNAME));
//        editFlushTime.setText(""+SpUtil.getIntSp(mApplication,Constant.FLUSHTIME));
//    }
//
//    @OnClick(R.id.btn_save)
//    void saveSetting() {
//        try {
//            int flushTime = Integer.valueOf(editFlushTime.getText().toString());
//            SpUtil.saveSp(mApplication, Constant.SHOWCLSNAME, switchShowClsName.isChecked());
//            SpUtil.saveSp(mApplication, Constant.SHOWICON, switchShowIcon.isChecked());
//            SpUtil.saveSp(mApplication, Constant.FLUSHTIME, flushTime);
//            //显示类名，则重启服务
//            if(switchShowClsName.isChecked()){
//                Intent intent=new Intent(mApplication, GetAppInfoService.class);
//                mApplication.startService(intent);
//            }
//            showToast(getString(R.string.settingSaveSucceed));
//        } catch (Exception e) {
//            showToast(getString(R.string.settingSaveFail));
//        }
//    }
//
//}
