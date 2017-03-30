package com.landicorp.android.wofupay.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.utils.JLog;

import java.util.List;

import static com.landicorp.android.wofupay.R.styleable.InputView;

/**
 * Created by Administrator on 2017/3/29.
 */

public class MCustomDialog extends Dialog {

    public static final int INFO_DIALOG = 0;// 弹出框类型——提示信息
    public static final int EDIT_DIALOG = 1;// 输入框，最多3个
    public static final int PRO_DIALOG = 2;// 进度条
    public static final int Pro_BACK = 3;// 进度条

    public static final int NUMBER = InputType.TYPE_CLASS_NUMBER; // 输入类型——数字
    public static final int TEXT = InputType.TYPE_CLASS_TEXT; // 文本
    public static final int PASSWORD = 3;// 密码

    private TextView info;
    private InputView[] inputViews;
    private WaveBgProgress progressBar;// 等待提示框布局
    private Button ok, cancel;
    private LinearLayout bt_layout;
    private int num, mDialogType;

    private OnClickListener onClickListener;
    private WindowManager.LayoutParams lp;

    public MCustomDialog(Context context) {
        this(context, PRO_DIALOG);
    }

    public MCustomDialog(Context context, int dialogType) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogType = dialogType;
        this.setContentView(R.layout.mcustom_dialog);

        initView();
    }

    private void initView() {

        Window dialogWindow = getWindow();
        lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.CENTER);
        lp.x = 200;

        dialogWindow.setAttributes(lp);

        info = (TextView) findViewById(R.id.textview_info);
        inputViews = new InputView[3];
        inputViews[0] = (InputView) findViewById(R.id.inputview0);
        inputViews[1] = (InputView) findViewById(R.id.inputview1);
        inputViews[2] = (InputView) findViewById(R.id.inputview2);
        progressBar = (WaveBgProgress) findViewById(R.id.progress_wait);
        bt_layout = (LinearLayout) findViewById(R.id.bt_layout);

        inputViews[1].setVisibility(View.GONE);
        inputViews[2].setVisibility(View.GONE);

        for (int i = 0; i < 3; i++) {
            inputViews[i].getInputEt().setOnKeyListener(
                    new View.OnKeyListener() {

                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if (KeyEvent.ACTION_UP == event.getAction()) {
                                switch (keyCode) {
                                    case KeyEvent.KEYCODE_ENTER:
                                        onClickListener.onOkClicked();
                                        return true;
                                    case KeyEvent.KEYCODE_CLEAR:
                                    case KeyEvent.KEYCODE_BACK:
                                        onClickListener.onCancelClicked();
                                        return true;
                                }
                            }
                            return false;
                        }
                    });
        }

        ok = (Button) findViewById(R.id.button_ok);
        cancel = (Button) findViewById(R.id.button_cancel);

        switch (mDialogType) {
            case INFO_DIALOG:
                inputViews[0].setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                ok.setEnabled(true);
                cancel.setEnabled(true);
                ok.setClickable(true);
                break;
            case EDIT_DIALOG:
                info.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                break;
            case PRO_DIALOG:
                progressBar.setVisibility(View.VISIBLE);
                inputViews[0].setVisibility(View.GONE);
                bt_layout.setVisibility(View.GONE);
                break;
            case Pro_BACK:
                progressBar.setVisibility(View.VISIBLE);
                inputViews[0].setVisibility(View.GONE);
                bt_layout.setVisibility(View.GONE);
                break;
        }

        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                JLog.i("-------------ok键被点击了------------------");
                onClickListener.onOkClicked();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onClickListener.onCancelClicked();
            }
        });

        // 屏蔽HOME键
        this.setOnShowListener(new OnShowListener() {

            @Override
            public void onShow(DialogInterface arg0) {
                if (mDialogType != Pro_BACK) {
                    MCustomDialog.this.getWindow().setType(
                            WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
                } else {
                    MCustomDialog.this.getWindow().setType(
                            WindowManager.LayoutParams.TYPE_TOAST);
                }
            }
        });

        this.setCanceledOnTouchOutside(false);// 点击空白处不消失
    }

    /**
     * 功能：设置提示信息 参数：String text 返回值：
     */
    public void setInfoText(String text) {
        info.setText(text);
    }

    public void setButtonText(String entry, String cancle) {

        ok.setText(entry+"");
        cancel.setText(cancle+"");
    }

    /**
     * 功能：设置输入框提示信息 参数：List<String> texts —— 根据list的size决定显示多少个输入框，最多三个 返回值：
     */
    public void setLabelText(List<String> texts) {
        num = texts.size();

        inputViews[0].setVisibility(View.GONE);
        inputViews[1].setVisibility(View.GONE);
        inputViews[2].setVisibility(View.GONE);

        switch (texts.size()) {
            case 3:
                inputViews[2].setVisibility(View.VISIBLE);
                inputViews[2].getLabelTv().setText(texts.get(2));
            case 2:
                inputViews[1].setVisibility(View.VISIBLE);
                inputViews[1].getLabelTv().setText(texts.get(1));
            case 1:
                inputViews[0].setVisibility(View.VISIBLE);
                inputViews[0].getLabelTv().setText(texts.get(0));
                break;
        }
    }

    /**
     * 功能：设置输入框属性：当前第n个输入框、输入类型 和 最大长度 参数：int editNum ——当前第几个输入框 int type
     * ——输入框输入类型：NUMBER/TEXT/PASSWORD int maxLength—— 输入框最大输入长度 返回值：
     */
    public void setEditProperty(int editNum, int type, int maxLength) {

        // if(!inputViews[editNum].isShown())
        // return;
        if (editNum > num)
            return;

        switch (type) {
            case NUMBER:
                inputViews[editNum].getInputEt().setInputType(
                        InputType.TYPE_CLASS_NUMBER);
                break;
            case TEXT:
                inputViews[editNum].getInputEt().setInputType(
                        InputType.TYPE_CLASS_TEXT);
                break;
            case PASSWORD:
                inputViews[editNum].getInputEt().setInputType(
                        InputType.TYPE_CLASS_NUMBER);
                inputViews[editNum].getInputEt().setTransformationMethod(
                        PasswordTransformationMethod.getInstance());
                break;
        }

        inputViews[editNum].getInputEt().setFilters(
                new InputFilter[] { new InputFilter.LengthFilter(maxLength) });
    }

    public void setHindeText(int editNum,String msg){
        inputViews[editNum].getInputEt().setHint(msg);;
    }

    /**
     * 功能：设置弹出框背景图偏移量 参数： 返回值：
     */
    public void setOffset(int x, int y) {
        lp.x = x;
        // lp.y = y;
    }

    /**
     * 功能：获取输入框输入内容 参数： 返回值：String
     */
    public EditText getInputEt(int editNum) {
        switch (editNum) {
            case 0:
                if (inputViews[0].isShown())
                    return inputViews[0].getInputEt();
                else
                    break;
            case 1:
                if (inputViews[1].isShown())
                    return inputViews[1].getInputEt();
                else
                    break;
            case 2:
                if (inputViews[2].isShown())
                    return inputViews[2].getInputEt();
                else
                    break;
            default:
                break;
        }
        return null;
    }

    /**
     * 功能：设置确定、取消按钮是否隐藏 参数：boolean isOkHidden——确定按钮是否隐藏 boolean
     * isCancelHidde——取消按钮是否隐藏 返回值：
     */
    public void setButtonHidden(boolean isOkHidden, boolean isCancelHidden) {
        if (isOkHidden) {
            ok.setVisibility(View.GONE);
        } else {
            ok.setVisibility(View.VISIBLE);
        }

        if (isCancelHidden) {
            cancel.setVisibility(View.GONE);
        } else {
            cancel.setVisibility(View.VISIBLE);
        }
        updateUI();
    }

    /**
     * 功能：设置点动态图是否隐藏 参数：boolean hidden——点动态图是否隐藏 返回值：
     */
    public void setProgressBarHidden(boolean hidden) {
        // if (hidden) {
        // progressBar.setVisibility(View.GONE);
        // } else {
        // progressBar.setVisibility(View.VISIBLE);
        // }
        // updateUI();
    }

    /**
     * 功能：更新dialog显示内容 参数： 返回值：
     */
    public void updateUI() {
        handler.sendEmptyMessage(0);
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 0) {
                info.invalidate();
                for (int i = 0; i < inputViews.length; i++) {
                    inputViews[i].invalidate();
                }
                progressBar.invalidate();
            }

            super.handleMessage(msg);

        }

    };

    public void setOnClickListener(MCustomDialog.OnClickListener listener) {
        onClickListener = listener;
    }

    public OnClickListener getClickListener() {
        return onClickListener;
    }



    public interface OnClickListener {
        abstract void onOkClicked();

        abstract void onCancelClicked();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (!isShowing()) {
            return true;
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_CLEAR:
            case KeyEvent.KEYCODE_BACK:

                JLog.i("---------------KEYCODE_BACK-----------------");

                if (onClickListener != null && cancel.isShown()) {
                    onClickListener.onCancelClicked();
                }
                return true;
            case KeyEvent.KEYCODE_ENTER:

                JLog.i("---------------KEYCODE_ENTER-----------------");

                if (onClickListener != null && ok.isShown()) {
                    onClickListener.onOkClicked();
                }
                return true;
        }

        return super.onKeyUp(keyCode, event);
    }

}
