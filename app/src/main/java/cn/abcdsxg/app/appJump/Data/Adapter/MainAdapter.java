package cn.abcdsxg.app.appJump.Data.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.abcdsxg.app.appJump.R;

/**
 * Author : 时小光
 * Email  : abcdsxg@gmail.com
 * Blog   : http://www.abcdsxg.cn
 * Date   : 2016/7/25 09:00
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> implements View.OnClickListener{

    public interface OnRecycleViewItemClickListener{
        void onItemClick(View v,String data);
    }

    private OnRecycleViewItemClickListener mListener;
    private List<String> mList;
    private Context mContext;
    public MainAdapter(Context context, List<String> list){
        mContext=context;
        mList=list;
    }
    //对外暴露设置监听器的方法
    public void setOnRecycleViewItemClickListener(OnRecycleViewItemClickListener listener){
        mListener=listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_main_recycleview,parent,false);
        //为每个view设置点击事件
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemText.setText(mList.get(position));
        //为view设置tag数据，传给触发点击事件事的参数
        holder.itemView.setTag(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public void onClick(View view) {
        mListener.onItemClick(view,view.getTag().toString());
    }
    //自定义viewHolder,持有可复用的view
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemText;
        ViewHolder(View v){
            super(v);
            itemText=(TextView)v.findViewById(R.id.itemText);
        }
    }
}
