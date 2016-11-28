package cn.abcdsxg.app.appJump.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import cn.abcdsxg.app.appJump.Data.Constant;
import cn.abcdsxg.app.appJump.Data.Utils.SpUtil;

/**
 * Author : 时小光
 * Email  : abcdsxg@gmail.com
 * Blog   : http://www.abcdsxg.cn
 * Date   : 16-11-28 15:37
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp=context.getSharedPreferences(Constant.TOUCHSERVICE,Context.MODE_APPEND);
        boolean enanbleTouchService=sp.getBoolean(SpUtil.name, true);
        if(enanbleTouchService) {
            Intent service = new Intent(context, TouchService.class);
            context.startService(service);
        }
    }
}
