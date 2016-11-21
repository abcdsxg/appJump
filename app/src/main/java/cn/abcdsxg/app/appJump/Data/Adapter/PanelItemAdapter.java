package cn.abcdsxg.app.appJump.Data.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import cn.abcdsxg.app.appJump.Data.Utils.ToolUtils;
import cn.abcdsxg.app.appJump.Data.greenDao.LancherInfo;
import cn.abcdsxg.app.appJump.R;


/**
 * Author : 时小光
 * Email  : abcdsxg@gmail.com
 * Blog   : http://www.abcdsxg.cn
 * Date   : 16-11-17 16:48
 */
public class PanelItemAdapter extends BaseAdapter{

    private List<LancherInfo> lancherInfoList;
    private Context mContext;
    private boolean fromeTouchView;
    public static final int EmptyListItemSize=12;

    public PanelItemAdapter( Context mContext,List<LancherInfo> lancherInfoList,boolean fromeTouchView) {
        this.lancherInfoList = lancherInfoList;
        this.mContext = mContext;
        this.fromeTouchView=fromeTouchView;
    }


    @Override
    public int getCount() {
        return EmptyListItemSize;
    }

    @Override
    public Object getItem(int i) {
        return lancherInfoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh=new ViewHolder();
        if(view==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.item_panel,viewGroup,false);
            vh.title= (TextView) view.findViewById(R.id.title);
            vh.icon=(ImageView)view.findViewById(R.id.icon);
            vh.cirlce=(ImageView)view.findViewById(R.id.circle);
            view.setTag(vh);
        }else{
            vh= (ViewHolder) view.getTag();
        }
        LancherInfo lancherInfo= lancherInfoList.get(i);
        Log.e("tag", "getView: "+i);
        if(lancherInfo!=null){
            vh.title.setText(lancherInfo.getTitle());
            Drawable drawable=null;
            switch (lancherInfo.getIconType()) {
                case 0:
                    break;
                case 1:
                    drawable = new BitmapDrawable(mContext.getResources(), ToolUtils.Bytes2Bimap(lancherInfo.getIconData()));
                    break;
                case 2:
                    try {
                        //获取对应packageName的Resources对象
                        Resources resources = mContext.getPackageManager().getResourcesForApplication(lancherInfo.getIconPkg());
                        //获取对应图片的id号
                        int iconid = resources.getIdentifier(lancherInfo.getIconRes(), null, null);
                        //获取资源图片
                        drawable= resources.getDrawable(iconid);
                    } catch (Exception ignored) {
                    }
                    break;
            }
            if(drawable!=null) {
                vh.icon.setImageDrawable(drawable);
            }

        }
        vh.cirlce.setImageDrawable(mContext.getResources().getDrawable(R.drawable.panel_item_grey_shape));

        return view;
    }


    private static class ViewHolder {
        private TextView title;
        private ImageView icon;
        private ImageView cirlce;

    }
}

