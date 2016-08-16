package cn.abcdsxg.app.appJump.Data.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.abcdsxg.app.appJump.Data.Utils.SpUtil;
import cn.abcdsxg.app.appJump.Fragment.GridViewFragment;

/**
 * Author : 时小光
 * Email  : abcdsxg@gmail.com
 * Blog   : http://www.abcdsxg.cn
 * Date   : 2016/7/26 11:09
 */
public class GridViewFragmentPageAdapter extends FragmentStatePagerAdapter {

    Context context;
    List<String> tagList=new ArrayList<>();
    FragmentManager fm;
    boolean isDesktop;

    public GridViewFragmentPageAdapter(FragmentManager fm, Context context,boolean isDesktop) {
        super(fm);
        this.fm=fm;
        this.context=context;
        this.isDesktop=isDesktop;
    }

    @Override
    public Fragment getItem(int position) {
        return GridViewFragment.newInstant(position,isDesktop);
    }

    @Override
    public int getCount() {
        return SpUtil.getIntSp(context,"MaxTabNum");
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //获取当前fragment的标题
        return SpUtil.getStringSp(context,String.valueOf(position));
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
