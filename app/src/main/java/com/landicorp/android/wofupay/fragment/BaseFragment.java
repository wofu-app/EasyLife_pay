package com.landicorp.android.wofupay.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.github.ikidou.fragmentBackHandler.FragmentBackHandler;
import com.landicorp.android.wofupay.R;

/**
 * Created by Administrator on 2017/3/18 0018.
 */
public class BaseFragment extends Fragment implements FragmentBackHandler {

    public BaseFragment() {
    }

    @Override
    public final boolean onBackPressed() {
        return interceptBackPressed()
                || (getBackHandleViewPager() == null
                ? BackHandlerHelper.handleBackPress(this)
                : BackHandlerHelper.handleBackPress(getBackHandleViewPager()));
    }

    public boolean interceptBackPressed() {
        return false;
    }

    /**
     * 2.1 版本已经不在需要单独对ViewPager处理
     *
     * @deprecated
     */
    @Deprecated
    public ViewPager getBackHandleViewPager() {
        return null;
    }

    /**
     *  切换页面
     * @param from
     * @param to
     */
    public void switchContent(Fragment from, Fragment to) {
        if (from != to) {
            if (!to.isAdded()) {    // 先判断是否被add过
                getFragmentManager().beginTransaction().hide(from).add(R.id.fl_content, to).addToBackStack("tag").commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                getFragmentManager().beginTransaction().hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }
}
