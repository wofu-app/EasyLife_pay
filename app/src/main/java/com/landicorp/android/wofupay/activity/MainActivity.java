package com.landicorp.android.wofupay.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.base.BaseApplication;
import com.landicorp.android.wofupay.fragment.MainFragment;
import com.landicorp.android.wofupay.loader.GlideImageLoader;
import com.yanzhenjie.fragment.CompatActivity;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

public class MainActivity extends CompatActivity implements OnBannerListener{
    private Context context = this;
    private Banner mBanner;
    private FrameLayout mParentLy;
    private Button mainUI_btn;

    private int screenwidth;
    private int screenHeight;

    /** 获取屏幕的宽度*/
    public int getSreenWidth(){
        return screenwidth;
    }

    /** 获取屏幕的高度*/
    public int getSreenHeight(){
        return screenHeight;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenwidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    private void initView() {
        mParentLy = (FrameLayout) findViewById(R.id.fl_content);
        mainUI_btn = (Button) findViewById(R.id.mainUI_btn);
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

        startFragment(MainFragment.newInstance("",""));
    }

    @Override
    public void OnBannerClick(int position) {
        Toast.makeText(getApplicationContext(),"你点击了："+position,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected int fragmentLayoutId() {
        return R.id.fl_content;
    }

}
