package com.landicorp.android.wofupay.utils;

import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.view.WindowManager;

import com.landicorp.android.wofupay.widget.MCustomDialog;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/3/29.
 */

public class DialogUtils {

    /**
     * 弹出框类型——提示信息
     */
    public static final int INFO_DIALOG = 0;
    /**
     * 输入框，最多3个
     */
    public static final int EDIT_DIALOG = 1;// 输入框，最多3个
    /**
     * 进度条
     */
    public static final int PRO_DIALOG = 2;// 进度条

    /**
     * 输入类型——数字
     */
    public static final int NUMBER = InputType.TYPE_CLASS_NUMBER; // 输入类型——数字
    /**
     * 文本
     */
    public static final int TEXT = InputType.TYPE_CLASS_TEXT; // 文本
    /**
     * 密码
     */
    public static final int PASSWORD = 3;// 密码

    private static DialogUtils dialogUtils;
    private MCustomDialog dialog;

    private MCustomDialog.OnClickListener listener;
    private Context context;
    private int dialogType = PRO_DIALOG;

    public DialogUtils(Context context, int dialogType) {
        this.context = context;
        this.dialogType = dialogType;
        dialog = initProgress(context, dialogType);
    };

    public static DialogUtils getInstance(Context context) {
        getInstance(context, MCustomDialog.PRO_DIALOG, null);
        return dialogUtils;
    }

    public static DialogUtils getInstance(Context context, int dialogType) {
        getInstance(context, dialogType, null);
        return dialogUtils;
    }

    public static DialogUtils getInstance(Context context, int dialogType,
                                          MCustomDialog.OnClickListener clickListener) {
        if (dialogUtils != null) {
            dialogUtils.dismiss();
            dialogUtils = null;
        }
        dialogUtils = new DialogUtils(context, dialogType);
        return dialogUtils;
    }

    /**
     * 显示弹窗
     *
     * @param msg
     *            显示的信息
     *
     */
    public void show(String msg) {
        if (dialog != null) {
            dialog.setInfoText(msg);
            if (!isShowing()) {
                dialog.show();
            } else {

            }
        }
    }

    public void upData(String msg) {
        if (dialog != null) {
            dialog.setInfoText(msg);
        }

    }

    public boolean isShowing() {
        if (dialog != null) {
            return dialog.isShowing();
        }
        return false;
    }

    /**
     * 隐藏弹窗
     */
    public void dismiss() {

        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
            Observable.just(1).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Integer>() {

                        @Override
                        public void call(Integer t) {
                            dialog = initProgress(context, dialogType);
                        }
                    });

        }
    }

    /**
     * 在服务中显示弹窗
     *
     * @param msg
     */
    public void showSystemDialog(String msg) {
        dialog.getWindow()
                .setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        show(msg);
    }

    /**
     * 初始化弹窗
     *
     * @param
     * @return
     */
    private MCustomDialog initProgress(Context context, int dialogType) {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        dialog = new MCustomDialog(context, dialogType);
        if (listener == null) {
            listener = new MCustomDialog.OnClickListener() {

                @Override
                public void onOkClicked() {
                    dismiss();

                }

                @Override
                public void onCancelClicked() {
                    dismiss();
                }
            };
        }
        dialog.setOnClickListener(listener);
        return dialog;
    }

    public void setListener(MCustomDialog.OnClickListener listener) {
        this.listener = listener;
        dialog.setOnClickListener(listener);
    }

    /**
     * 一个简单系统形式能的弹窗，但是屏蔽home间
     *
     * @param msg
     * @param entry
     * @param cancle
     */
    public void dialog(String msg, String entry, String cancle) {
        if (TextUtils.isEmpty(entry)) {
            entry = "确认";
        }
        if (TextUtils.isEmpty(cancle)) {
            cancle = "取消";
        }
        dialog.setButtonText(entry, cancle);
        dialog.setInfoText(msg);
        dialog.show();
    }

    public interface OnDialogClick {
        void onClickEntry();

        void onClickCancle();
    }

    public void setTextButton(String entry, String cancle) {
        dialog.setButtonText(entry, cancle);
    }

}
