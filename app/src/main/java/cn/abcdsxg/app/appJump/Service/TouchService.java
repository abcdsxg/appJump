package cn.abcdsxg.app.appJump.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by shingle on 16-11-14.
 */

public class TouchService extends Service implements View.OnTouchListener{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
