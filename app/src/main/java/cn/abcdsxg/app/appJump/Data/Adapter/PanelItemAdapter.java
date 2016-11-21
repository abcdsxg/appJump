package cn.abcdsxg.app.appJump.Data.Adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

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

    public PanelItemAdapter(List<LancherInfo> lancherInfoList, Context mContext) {
        this.lancherInfoList = lancherInfoList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return lancherInfoList==null?0:lancherInfoList.size();
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
        viewHolder vh=null;
        if(vh==null){
            vh=new viewHolder();
            view= LayoutInflater.from(mContext).inflate(R.layout.item_panel,null);
            vh.title= (TextView) view.findViewById(R.id.title);
            vh.icon=(ImageView)view.findViewById(R.id.icon);
            view.setTag(vh);
        }else{
            vh= (viewHolder) view.getTag();
        }
        LancherInfo lancherInfo=lancherInfoList.get(i);
        vh.title.setText(lancherInfo.getTitle());
        return null;
    }

    static class viewHolder{
        private TextView title;
        private ImageView icon;

    }
}

