package cn.abcdsxg.app.appJump.Fragment;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.abcdsxg.app.appJump.Base.BaseFragment;
import cn.abcdsxg.app.appJump.MainActivity;
import cn.abcdsxg.app.appJump.R;
import okhttp3.OkHttpClient;

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
    LinearLayout zfbText;
    @BindView(R.id.wxText)
    LinearLayout wxText;
    @BindView(R.id.ppText)
    LinearLayout ppText;
    private IInAppBillingService mService;
    private ServiceConnection mServiceConn;
    private String price;

    @Override
    public View setView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_donate, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBilling();
    }

    private void queryItem() {
        ArrayList<String> skuList = new ArrayList<String>();
        skuList.add("donate");
        final Bundle querySkus = new Bundle();
        querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bundle skuDetails = mService.getSkuDetails(3,
                            getContext().getPackageName(), "inapp", querySkus);
                    dealBundle(skuDetails);
                } catch (RemoteException e) {
                    showToast("error");
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void dealBundle(Bundle skuDetails) {
        int response = skuDetails.getInt("RESPONSE_CODE");
        if (response == 0) {
            ArrayList<String> responseList
                    = skuDetails.getStringArrayList("DETAILS_LIST");
            if (responseList == null) {
                return;
            }
            try {
                for (String thisResponse : responseList) {
                    JSONObject object = new JSONObject(thisResponse);

                    String sku = object.getString("productId");
                    String price = object.getString("price");
                    if (sku.equals("donate")) {
                        this.price = price;
                        Log.e("tag", "dealBundle: "+price );
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void dealPay(final String sku) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bundle buyIntentBundle = mService.getBuyIntent(3, getContext().getPackageName(),
                            sku, "inapp", "ok");
                    PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
                    getActivity().startIntentSenderForResult(pendingIntent.getIntentSender(),
                            1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                            Integer.valueOf(0));
                } catch (Exception e) {
                    showToast("error");
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }


    private void initBilling() {
        mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
            }

            @Override
            public void onServiceConnected(ComponentName name,
                                           IBinder service) {
                mService = IInAppBillingService.Stub.asInterface(service);
                dealPay("donate");
            }
        };
        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        getContext().bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
    }

    @OnClick(R.id.donateTExt)
    public void toDonate() {
        MobclickAgent.onEvent(getContext(), "DonateList");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.abcdsxg.cn/ds_list.html"));
        startActivity(intent);
    }

    @OnClick(R.id.ppText)
    public void pp() {
        MobclickAgent.onEvent(getContext(), "copyPaypal");
        ClipboardManager cmb = (ClipboardManager) mApplication.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setPrimaryClip(ClipData.newPlainText("content", "abcdsxg@gmail.com"));
        Toast.makeText(mApplication, R.string.copySuccess, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.zfbText)
    public void zfb() {
        MobclickAgent.onEvent(getContext(), "copyZfb");
        ClipboardManager cmb = (ClipboardManager) mApplication.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setPrimaryClip(ClipData.newPlainText("content", "18336739503"));
        Toast.makeText(mApplication, R.string.copySuccess, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.wxText)
    public void wx() {
        MobclickAgent.onEvent(getContext(), "copyWx");
        ClipboardManager cmb = (ClipboardManager) mApplication.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setPrimaryClip(ClipData.newPlainText("content", "syg596913711"));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            getContext().unbindService(mServiceConn);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

            if (resultCode == Activity.RESULT_OK) {
                try {
                    JSONObject jo = new JSONObject(purchaseData);
                    String sku = jo.getString("productId");
                    showToast("Thanks for your donate!");
                }
                catch (JSONException e) {
                    showToast("Failed to parse purchase data.");
                    e.printStackTrace();
                }
            }
        }
    }
}