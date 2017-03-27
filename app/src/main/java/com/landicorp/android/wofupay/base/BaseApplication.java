package com.landicorp.android.wofupay.base;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.model.FunctionBean;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static android.R.id.list;

public class BaseApplication extends Application {
    private static Context context;
    public static List<?> images = new ArrayList<>();

    public Map<Integer, FunctionBean.DataBean> functions = null;

    private static BaseApplication mApplication;
    public static RequestQueue queue;;

    /**
     * 获取上下文
     * @return
     */
    public static Context getContext(){
        return context;
    }

    public synchronized static BaseApplication getInstance() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        mApplication = this;
        LitePal.initialize(context);

     //   String[] urls = getResources().getStringArray(R.array.url);
        Integer [] urls = {R.mipmap.picture1,R.mipmap.picture2,R.mipmap.picture3,R.mipmap.picture4,R.mipmap.picture5,R.mipmap.picture6,R.mipmap.picture7};
        List list = Arrays.asList(urls);
        images = new ArrayList(list);
        queue = Volley.newRequestQueue(context);
    }

    public void setFunction(Map<Integer, FunctionBean.DataBean> map){
        functions = map;
    }

    public Map<Integer, FunctionBean.DataBean> getFunction(){
        return functions;
    }

}
