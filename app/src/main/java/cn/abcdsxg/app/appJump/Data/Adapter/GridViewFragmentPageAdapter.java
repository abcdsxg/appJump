package cn.abcdsxg.app.appJump.Data.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.abcdsxg.app.appJump.Fragment.GridViewFragment;

/**
 * Author : 时小光
 * Email  : abcdsxg@gmail.com
 * Blog   : http://www.abcdsxg.cn
 * Date   : 2016/7/26 11:09
 */
public class GridViewFragmentPageAdapter extends FragmentPagerAdapter {

    Context context;

    public GridViewFragmentPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {
        return GridViewFragment.newInstant(position);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //获取当前fragment的标题
        return super.getPageTitle(position);
    }
}
