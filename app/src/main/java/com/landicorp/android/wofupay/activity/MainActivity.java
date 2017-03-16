package com.landicorp.android.wofupay.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.base.BaseActivity;
import com.landicorp.android.wofupay.base.BaseApplication;
import com.landicorp.android.wofupay.fragment.MainFragment;
import com.landicorp.android.wofupay.loader.GlideImageLoader;
import com.landicorp.android.wofupay.model.FileBean;
import com.landicorp.android.wofupay.model.FunctionBean;
import com.landicorp.android.wofupay.utils.AppUtils;
import com.landicorp.android.wofupay.widget.CustomLinerLayout;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements OnBannerListener, View.OnClickListener {
    private Context context = this;
    private Banner mBanner;

    private LinearLayout mParentLy;
  //  private CustomLinerLayout mCusLy;
    private Button mainUI_btn;

    private List<Integer> functions;

    private int[] ids = { R.mipmap.buscard, R.mipmap.yecx, R.mipmap.gay,
            R.mipmap.phone, R.mipmap.caipiao, R.mipmap.game,
            R.mipmap.zhunong, R.mipmap.water,R.mipmap.train_name,
            R.mipmap.wofu_bank, R.mipmap.gongyi,R.mipmap.market,};
    private FragmentManager mSupportFragmentManager;
    private MainFragment mMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        mSupportFragmentManager = getSupportFragmentManager();
        initFragment();
        changeFragment(mMainFragment);
    }

    private void changeFragment(MainFragment mainFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_content,mainFragment);
        fragmentTransaction.commit();
    }

    private void initFragment() {
        mMainFragment = new MainFragment();
    }

    private void initView() {

        mParentLy = (LinearLayout) findViewById(R.id.mainUI_parentLy);
     //   mCusLy = (CustomLinerLayout) findViewById(R.id.mainUI_cusLy);

        mainUI_btn = (Button) findViewById(R.id.mainUI_btn);
        mainUI_btn.setText("宽："+getSreenWidth()+","+"高："+getSreenHeight());

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
    protected void onResume() {
        super.onResume();
       // showView(getDatas());
    }

//    public void showView(List<Integer> ids) {
//        mCusLy.removeAllViews();
//        for (int i = 0; i < ids.size(); i++) {
//            ImageButton imageButton = null;
//            Integer id = ids.get(i);
//            if (id == R.mipmap.buscard) {
//                imageButton = (ImageButton) getLayoutInflater().inflate(
//                        R.layout.main_big_item, mCusLy, false);
//            } else {
//                imageButton = (ImageButton) getLayoutInflater().inflate(
//                        R.layout.main_small_item, mCusLy, false);
//            }
//            imageButton.setImageResource(id);
//            imageButton.setTag(id);
//            imageButton.setOnClickListener(this);
//            mCusLy.addChild(imageButton);
//        }
//
//    }

    public List<Integer> getDatas() {

        FunctionBean readFromFile = AppUtils.readFromFile(FileBean.FUNCTION,
                FileBean.FUNCTIONNANE);
        AppUtils.upFucntion(readFromFile);

        functions = new ArrayList<Integer>();
        functions.clear();

        for (Integer id : ids) {
            if (Build.VERSION.SDK_INT < 15) {
                if (id == R.mipmap.yecx || id == R.mipmap.zhunong) {
                    continue;
                }
            }

            int function = getFunction(id);
            int functionState = AppUtils.getFunctionState(function);
            if (functionState == 2) {
                continue;
            }

            functions.add(id);
        }

//        if (PayContacts.isDebug) {
//            functions.add(R.drawable.violation);
//        }
        return functions;
    }

    /**
     * 根据id获取按钮状态
     *
     * @param buttonID
     *            id 功能id 1:公交卡充值 2.cpu卡充值 3.pboc卡充值 4.余额查询 5.加油卡充值 6.手机充值
     *            7.话费充值 8.流量充值 9.游戏充值 10.彩票 11福彩 12.体彩 13.助农取款 14.公益 15.水电煤
     */
    public int getFunction(int buttonID) {

        int id = -1;

        switch (buttonID) {
            // 公交卡充值
            case R.mipmap.buscard:
                id = 1;
                break;
            case R.mipmap.yecx:
                id = 4;
                break;
            // 加油卡充值
            case R.mipmap.gay:
                id = 5;
                break;
            // 手机充值
            case R.mipmap.phone:
                id = 6;
                break;
            // 游戏充值
            case R.mipmap.game:
                id = 9;
                break;
            // 彩票
            case R.mipmap.caipiao:
                id = 10;
                break;

            case R.mipmap.zhunong:// 助农
                id = 13;
                break;

            // 公益
            case R.mipmap.gongyi:
                id = 14;
                break;
            // 水电煤
            case R.mipmap.water:
                id = 15;
                break;

            default:
                id = 100;
                break;
        }
        return id;
    }

    @Override
    public void OnBannerClick(int position) {
        Toast.makeText(getApplicationContext(),"你点击了："+position,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        Class<?> c = null;
        int tag = (Integer) v.getTag();
        int id = -1;

        switch (tag) {
            // 公交卡充值
            case R.mipmap.buscard:
                id = 1;
                c = null;
                Toast.makeText(this,"点击了公交卡充值",Toast.LENGTH_SHORT).show();
                break;
            case R.mipmap.phone:
                id = 6;
                Toast.makeText(this,"点击了手机充值",Toast.LENGTH_SHORT).show();
                break;

        }
    }
    /**
     * 添加Fragment到回退栈
     *
     * @param fragment
     */
    public void addToBackStack(Fragment fragment) {
        FragmentTransaction transaction = mSupportFragmentManager.beginTransaction();
        transaction.replace(R.id.fl_content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        addToBackStack(mMainFragment);
    }
}
