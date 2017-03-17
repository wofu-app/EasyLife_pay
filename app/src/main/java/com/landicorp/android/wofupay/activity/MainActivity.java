package com.landicorp.android.wofupay.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.base.BaseApplication;
import com.landicorp.android.wofupay.fragment.MainFragment;
import com.landicorp.android.wofupay.loader.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

public class MainActivity extends FragmentActivity implements OnBannerListener{
    private Context context = this;
    private Banner mBanner;
    private FrameLayout mParentLy;
    private Button mainUI_btn;

    private MainFragment mMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initFragment();
        changeFragment(mMainFragment);
    }

    private void changeFragment(MainFragment mainFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_content,mainFragment);
        fragmentTransaction.addToBackStack("tag");
        fragmentTransaction.commit();
    }

    private void initFragment() {
        mMainFragment = new MainFragment();
    }

    private void initView() {
        mParentLy = (FrameLayout) findViewById(R.id.fl_content);

        mainUI_btn = (Button) findViewById(R.id.mainUI_btn);
//        mainUI_btn.setText("宽："+getSreenWidth()+","+"高："+getSreenHeight());

        mainUI_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mParentLy.setVisibility(View.VISIBLE);
                mainUI_btn.setVisibility(View.GONE);
                ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f,1.0f, 0.0f, 1.0f,Animation.RELATIVE_TO_SELF,0.6f,Animation.RELATIVE_TO_SELF,0.6f);
                scaleAnimation.setDuration(1000);
                scaleAnimation.setFillAfter(true);

                mBanner.startAnimation(scaleAnimation);
            }
        });

        mBanner = (Banner) findViewById(R.id.mainUI_banner);
        mBanner.setImages(BaseApplication.images)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(this)
                .start();
    }

    @Override
    public void OnBannerClick(int position) {
        Toast.makeText(getApplicationContext(),"你点击了："+position,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)) {
            super.onBackPressed();
        }
    }

}
