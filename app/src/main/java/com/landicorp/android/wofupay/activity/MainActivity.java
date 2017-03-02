package com.landicorp.android.wofupay.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.base.BaseActivity;

public class MainActivity extends BaseActivity {
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        Toast.makeText(context,"Hello, Im is home page!",Toast.LENGTH_SHORT).show();
    }
}
