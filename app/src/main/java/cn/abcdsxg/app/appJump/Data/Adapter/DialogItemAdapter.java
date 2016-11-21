package cn.abcdsxg.app.appJump.Data.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.List;
import cn.abcdsxg.app.appJump.Data.greenDao.AppInfo;
import cn.abcdsxg.app.appJump.R;

/**
 * Author : 时小光
 * Email  : abcdsxg@gmail.com
 * Blog   : http://www.abcdsxg.cn
 * Date   : 16-11-21 12:10
 */
public class DialogItemAdapter extends BaseAdapter {

    private List<AppInfo> appInfos;
    private Context mContext;

    public DialogItemAdapter(Context mContext,List<AppInfo> appInfos){
        this.appInfos=appInfos;
        this.mContext=mContext;
    }
    @Override
    public int getCount() {
        return appInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return appInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        GridViewAdapter.ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_gridview, viewGroup, false);
            holder = new GridViewAdapter.ViewHolder(view);
            view.setTag(holder);
        }else{
            holder= (GridViewAdapter.ViewHolder) view.getTag();
        }
        AppInfo info=appInfos.get(i);
        holder.appName.setText(info.getAppName());
        switch (i){
            case 0:
                holder.appIcon.setImageResource(R.drawable.android);
                break;
            case 1:
                holder.appIcon.setImageResource(R.mipmap.ic_launcher);
                break;
        }
        return view;
    }
}
