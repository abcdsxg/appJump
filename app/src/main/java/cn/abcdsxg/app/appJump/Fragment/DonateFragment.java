package cn.abcdsxg.app.appJump.Fragment;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.abcdsxg.app.appJump.Base.BaseFragment;
import cn.abcdsxg.app.appJump.R;

/**
 * Author : 时小光
 * Email  : abcdsxg@gmail.com
 * Blog   : http://www.abcdsxg.cn
 * Date   : 2016/6/7 18:02
 */
public class DonateFragment extends BaseFragment {

    @BindView(R.id.donateTExt)
    TextView donateTExt;
    @BindView(R.id.zfbText)
    TextView zfbText;
    @BindView(R.id.wxText)
    TextView wxText;
    @BindView(R.id.ppText)
    TextView ppText;
    private Unbinder unbinder;



    @Override
    public View setView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_donate, container, false);
    }



    @OnClick(R.id.donateTExt)
    public void toDonate() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.abcdsxg.cn/ds_list.html"));
        startActivity(intent);
    }
    @OnClick(R.id.ppText)
    public void pp() {
        ClipboardManager cmb = (ClipboardManager) mApplication.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setPrimaryClip(ClipData.newPlainText("content", "abcdsxg@gmail.com"));
        Toast.makeText(mApplication, R.string.copySuccess, Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.zfbText)
    public void zfb() {
        ClipboardManager cmb = (ClipboardManager) mApplication.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setPrimaryClip(ClipData.newPlainText("content", "18336739503"));
        Toast.makeText(mApplication, R.string.copySuccess, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.wxText)
    public void wx() {
        ClipboardManager cmb = (ClipboardManager) mApplication.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setPrimaryClip(ClipData.newPlainText("content", "syg596913711"));
    }






}