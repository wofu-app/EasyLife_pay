package com.landicorp.android.wofupay.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.IBinder;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.landicorp.android.eptapi.DeviceService;
import com.landicorp.android.eptapi.exception.ReloginException;
import com.landicorp.android.eptapi.exception.RequestException;
import com.landicorp.android.eptapi.exception.ServiceOccupiedException;
import com.landicorp.android.eptapi.exception.UnsupportMultiProcess;
import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.utils.JLog;

import static android.R.attr.id;

/**
 * Created by Administrator on 2017/3/29.
 */

public class InputView extends LinearLayout implements View.OnFocusChangeListener,
        View.OnClickListener {
    public static final int NUMBER = InputType.TYPE_CLASS_NUMBER; //�������������-����
    public static final int TEXT = InputType.TYPE_CLASS_TEXT; //�������������-�ı�
    public static final int PASSWORD = 3;//�������������-����
    public static final int IP = 4;//�������������-IP��ַ
    public static final int NO_CHINESE = 5;//��ֹ��������


    // �?��默认�?
   // private static final int DEFAULT_TEXT_SIZE = TerminalInfo.getInstance().isPortDevice() ? 22 : 28; // 默认的字体大�?
    private static final int NORMAL_BG_RESOURCE = R.drawable.input_editor; // 默认背景资源
    private static final int HOVER_BG_RESOURCE = R.drawable.input_editor; // 聚焦背景资源
    private static final int REFRENCE_IMG_RESOURCE = R.mipmap.input_refrence_img;// 参�?图标资源

    // 组成�?��的控�?
    private TextView labelTv; // label
    private EditText inputEt; // 输入�?
    private ImageView refrenceIv;// 参�?图片

    // 控件�?��应的Layout
    private LayoutParams labelLp; // label对应的LayoutParams
    private LayoutParams inputLp;// 输入框对应的LayoutParams
    private LayoutParams refrenceLp;// 参�?图片对应的LayoutParams

    // 输入类型参数
    private boolean special; // 是否特殊输入
    private boolean refrence; // 是否可以参照
    private String label; // 标识
    private boolean isIMEnabled = true;


    /**
     *
     * @param context
     * @param attrs
     *            设置参数
     */
    public InputView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 初始化参�?
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.InputView);
        label = (String) a.getText(R.styleable.InputView_label);
        special = a.getBoolean(R.styleable.InputView_special, false);
        refrence = a.getBoolean(R.styleable.InputView_refrence, false);
        a.recycle();

        initViews();
        initLayout();
        initAdd();

    }

    public void setIMEnabled(boolean isEnabled) {
        isIMEnabled = isEnabled;
    }

    private void initAdd() {
        this.addView(labelTv, labelLp);
        this.addView(inputEt, inputLp);

        if (refrence) {
            this.addView(refrenceIv, refrenceLp);
            refrenceIv.setVisibility(View.INVISIBLE);
        }
    }

    private void initLayout() {
        setPadding(20, 0, 20, 0);
        setGravity(Gravity.CENTER_VERTICAL);
        setOrientation(LinearLayout.HORIZONTAL);
        setBackgroundResource(NORMAL_BG_RESOURCE);
        setClickable(true);
        setOnClickListener(this);
    }

    /**
     * 初始化控件和布局
     */
    private void initViews() {

        // 初始化labelTv
        labelTv = new TextView(this.getContext());
        labelTv.setTextColor(getContext().getResources().getColorStateList(
                R.color.common_black));
        labelTv.setGravity(Gravity.CENTER_VERTICAL);
        //TODO 默认字体大小
        labelTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
//		labelTv.getPaint().setFakeBoldText(true);
        labelTv.setText(label);
        labelTv.setPadding(20, 0, 20, 0);

        // 初始化inputEt
        inputEt = new EditText(this.getContext()) {
            private String lastText;
            private OnKeyListener listener;
            @Override
            public void setOnKeyListener(OnKeyListener l) {
                listener = l;
                super.setOnKeyListener(l);
            }

            @Override
            public boolean onKeyUp(int keyCode, KeyEvent event) {
                if(listener != null) {
                    if(listener.onKey(this, keyCode, event)) {
                        return true;
                    }
                }
                JLog.d("InputView", "onKeyUp-------------------------------");
                boolean ret = super.onKeyUp(keyCode, event);
                String currentText = getText().toString();
                if(!"".equals(lastText) && !currentText.equals(lastText)) {
                    setText(lastText);
                }
                return ret;
            }

            @Override
            public boolean onKeyDown(int keyCode, KeyEvent event) {
                JLog.d("InputView", "onKeyDown-------------------------------");
                boolean ret = super.onKeyDown(keyCode, event);
                lastText = getText().toString();
                return ret;
            }

            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {

                if (event.getKeyCode() == KeyEvent.KEYCODE_0) {
                    // �������ͳ���0�������д���
                    if ((event.getRepeatCount() > 0) && (getInputType() == InputType.TYPE_CLASS_NUMBER)) {
                        return true;
                    }
                }

                return super.dispatchKeyEvent(event);
            }

        };

        inputEt.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.clear();
            }


        });

        inputEt.setTextColor(getContext().getResources().getColorStateList(
                R.color.common_black));
        inputEt.setBackgroundDrawable(null);
        inputEt.setSingleLine();
        inputEt.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        inputEt.setPadding(0, 0, 0, 0);
        //TODO 默认字体大小
        inputEt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
//		inputEt.getPaint().setFakeBoldText(true);

        // 初始化布�?
        labelLp = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.FILL_PARENT);
        inputLp = new LayoutParams(0, LayoutParams.FILL_PARENT);
        inputLp.weight = 1;

        // 如果�?��参�?则初始化这个控件
        if (refrence) {
            refrenceIv = new ImageView(this.getContext());
            refrenceIv = new ImageView(getContext());
            refrenceIv.setImageResource(REFRENCE_IMG_RESOURCE);
            refrenceIv.setPadding(30, 15, 0, 15);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                refrenceLp= new LayoutParams(labelLp);
            }
        }

        if (special) {
            inputEt.setEnabled(false);
            inputEt.setFocusable(false);
            if (refrence) {
                refrenceIv.setPadding(30, 0, 0, 0);
            }
        }

        inputEt.setOnFocusChangeListener(this);
    }

    /**
     * @return
     */
    public EditText getInputEt() {
        return inputEt;
    }

    public TextView getLabelTv() {
        return labelTv;
    }

    public ImageView getRefrenceIv() {
        return refrenceIv;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            setBackgroundResource(NORMAL_BG_RESOURCE);
//			setPadding(20, 0, 20, 0);
            inputEt.setGravity(Gravity.CENTER_VERTICAL);
            inputEt.setTextColor(getContext().getResources().getColorStateList(
                    R.color.common_black));
            labelTv.setTextColor(getContext().getResources().getColorStateList(
                    R.color.common_black));

            if (refrence) {
                refrenceIv.setVisibility(View.INVISIBLE);
            }
            //ʧȥ����ʱ�������뷨
            //TODO 输入框的操作
            //  InputMethodManager im = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
          //  im.setInputMethodPara(InputMethodManager.INPUTMETHODPARA_ENABLEINPUT, null);
        } else {
            try {
                DeviceService.login(getContext());
//				IMEUtil.useGoogleIME();
            } catch (ServiceOccupiedException e) {
            } catch (ReloginException e) {
            } catch (UnsupportMultiProcess e) {
            } catch (RequestException e) {
            }
            setBackgroundResource(HOVER_BG_RESOURCE);
            inputEt.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            inputEt.setPadding(0, 0, 0, 0);
            inputEt.setSelection(inputEt.getText().length());
//			setPadding(20, 0, 20, 0);
            //TODO 输入框的操作
//            if(inputEt.getInputType() == InputType.TYPE_CLASS_NUMBER || !isIMEnabled) {
//                im.hideSoftInputFromWindow(getApplicationWindowToken(), 0);
//                im.setInputMethodPara(InputMethodManager.INPUTMETHODPARA_DISABLEINPUT, null);
//            }
//            else if(isIMEnabled){
//                im.setInputMethodPara(InputMethodManager.INPUTMETHODPARA_ENABLEINPUT, null);
//            }
            inputEt.setTextColor(getContext().getResources().getColorStateList(
                    R.color.common_black));
            labelTv.setTextColor(getContext().getResources().getColorStateList(
                    R.color.common_black));

            if (refrence) {
                refrenceIv.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        inputEt.requestFocus();
    }
    public void setEditProperty(int type, int maxLength) {
        switch (type) {
            case NUMBER:
                inputEt.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case TEXT:
                inputEt.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case PASSWORD:
                inputEt.setInputType(InputType.TYPE_CLASS_NUMBER);
                inputEt.setTransformationMethod(PasswordTransformationMethod
                        .getInstance());
                break;
            case NO_CHINESE:
                inputEt.addTextChangedListener(NoChineseTextWatcher);
                break;

        }

        if (maxLength > 0) {
            inputEt.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
                    maxLength) });
        }
    }

    private TextWatcher NoChineseTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            try {

                String temp = s.toString();

                String tem = temp.substring(temp.length() - 1, temp.length());

                byte [] bytes = tem.getBytes("UTF-8");

                if (bytes.length == tem.length()) {
                    return;
                }

                s.delete(temp.length() - 1, temp.length());

            } catch (Exception e) {

            }

        }
    };

}
