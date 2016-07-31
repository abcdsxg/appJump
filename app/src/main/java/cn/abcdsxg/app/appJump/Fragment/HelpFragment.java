package cn.abcdsxg.app.appJump.Fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.Unbinder;
import cn.abcdsxg.app.appJump.Base.BaseFragment;
import cn.abcdsxg.app.appJump.Data.Utils.Update;
import cn.abcdsxg.app.appJump.R;


/**
 * Author : 时小光
 * Email  : abcdsxg@gmail.com
 * Blog   : http://www.abcdsxg.cn
 * Date   : 2016/6/19 16:23
 */
public class HelpFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.videoHelp)
    LinearLayout videoHelp;
    @BindView(R.id.questions)
    LinearLayout questions;
    @BindView(R.id.suggestion)
    LinearLayout suggestion;
    @BindView(R.id.checkUpdate)
    LinearLayout checkUpdate;


    @Override
    public View setView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_help, container, false);
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        videoHelp.setOnClickListener(this);
        questions.setOnClickListener(this);
        checkUpdate.setOnClickListener(this);
        suggestion.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.videoHelp:
                MobclickAgent.onEvent(mApplication, "CilckWatchVideo");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.abcdsxg.cn/app/google/syncsend/video.php"));
                startActivity(intent);
                break;
            case R.id.questions:
                MobclickAgent.onEvent(mApplication, "CilckWatchQuestions");
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.abcdsxg.cn/app/google/syncsend/question.html"));
                startActivity(intent);
                break;
            case R.id.checkUpdate:
                Update update = new Update(mApplication, 1);
                update.checkUpdateInfo();
                break;
            case R.id.suggestion:
                MobclickAgent.onEvent(mApplication, "CopyEmail");
                ClipboardManager cmb = (ClipboardManager) mApplication.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setPrimaryClip(ClipData.newPlainText("email", getString(R.string.myEmail)));
                Toast.makeText(mApplication, R.string.copySuccess, Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
