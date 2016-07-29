package cn.abcdsxg.app.appJump.Data.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.abcdsxg.app.appJump.Data.greenDao.AppInfo;
import cn.abcdsxg.app.appJump.Data.Utils.ToolUtils;
import cn.abcdsxg.app.appJump.R;

/**
 * Author : 时小光
 * Email  : abcdsxg@gmail.com
 * Blog   : http://www.abcdsxg.cn
 * Date   : 2016/7/26 11:42
 */
public class GridViewAdapter extends BaseAdapter {

    Context mContext;
    List<AppInfo> appInfos;

    public GridViewAdapter(Context context, List<AppInfo> appInfos) {
        mContext = context;
        this.appInfos = appInfos;
    }

    @Override
    public int getCount() {
        return appInfos == null ? 0 : appInfos.size();
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
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_gridview, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        AppInfo info=appInfos.get(i);
        Drawable icon= ToolUtils.getAppIcon(mContext,info.getPkgName());
        holder.appName.setText(info.getAppName());
        //holder.appIcon.setImageDrawable(icon);
        Glide.with(mContext)
                .load("")
                .placeholder(icon)
                .into(holder.appIcon);
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.appIcon)
        ImageView appIcon;
        @BindView(R.id.appName)
        TextView appName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
