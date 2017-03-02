package com.landicorp.android.wofupay.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.base.BaseActivity;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("a"+i);
        }
        ArrayList<Integer> list1 = new ArrayList<>();
        list1.add(1);
    }

    private void initView() {
        Toast.makeText(context,"Hello, I'm home page!",Toast.LENGTH_SHORT).show();
        Toast.makeText(context,"Hello, Nice to meet you!",Toast.LENGTH_SHORT).show();
    }
}
