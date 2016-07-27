package cn.abcdsxg.app.appJump.Base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author : 时小光
 * Email  : abcdsxg@gmail.com
 * Blog   : http://www.abcdsxg.cn
 * Date   : 2016/7/26 09:25
 */
public abstract class BaseFragment extends Fragment {

    private Unbinder unbinder;
    protected Context mApplication;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication=BaseApplication.getInstance().getApplicationContext();
    }

    public abstract View setView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=setView(inflater,container,savedInstanceState);
        unbinder=ButterKnife.bind(this,view);
        return view;
    }

    public void showToast(String msg){
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageStart(getClass().getSimpleName());
        Log.e("tag", "onPause: "+getClass().getSimpleName() );
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }
}
