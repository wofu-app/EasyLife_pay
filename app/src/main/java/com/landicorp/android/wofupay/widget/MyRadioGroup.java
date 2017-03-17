package com.landicorp.android.wofupay.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import com.landicorp.android.wofupay.R;
/**
 * Created by Administrator on 2017/3/16.
 */

public class MyRadioGroup extends LinearLayout implements View.OnClickListener {


    /**
     * 横向间距
     */
    private int childMarginHorizontal = 0;
    /**
     * 纵向间距
     */
    private int childMarginVertical = 0;
    /**
     * 子控件集合
     */
    private List<CheckBox> viewList = new ArrayList<CheckBox>();
    /**
     * 子空间的id，可以自定义一个子控件，但是这里必须是CheckBox
     */
    private int childId = -1;

    /**
     * 是否是单选
     */
    private boolean singleChoice;

    private List<String> values = new ArrayList<String>();

    /* 列数 */
    private int rowNumber;
    private boolean forceLayout;

    /**
     * 靠左对其
     */
    private int LEFT = 1;
    private int CENTER = 0;
    /**
     * 对其方式
     */
    private int gravity = LEFT;
    private int mY;
    private double mX;

    /**
     * 上一次选中的按钮id
     */
    private int mLastCheckedPosition = -1;
    private int startX;
    private int startY;
    private int rowNum;
    private int flagX;
    private int flagY;
    private int sheight;

    public MyRadioGroup(Context context) {
        this(context, null);
    }

    public MyRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    /***
     * 初始化数据
     */
    private void initView(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.MultiLineRadioGroup);
        childMarginHorizontal = typedArray.getDimensionPixelOffset(
                R.styleable.MultiLineRadioGroup_child_margin_horizontal, 15);
        childMarginVertical = typedArray.getDimensionPixelOffset(
                R.styleable.MultiLineRadioGroup_child_margin_vertical, 5);

        childId = typedArray.getResourceId(
                R.styleable.MultiLineRadioGroup_child_layout, 0);

        singleChoice = typedArray.getBoolean(
                R.styleable.MultiLineRadioGroup_single_choice, true);
        if (childId == 0) {
            throw new RuntimeException(
                    "The attr 'child_layout' must be specified!(必须使用child_layout属性指定一个子控件)");
        }

        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                CheckBox cb = getChild();
                viewList.add(cb);

                // 添加子控件
                addView(cb);
                if (values != null && i < values.size()) {
                    // 设置值
                    cb.setText(values.get(i));
                } else {
                    values.add(cb.getText().toString());
                }
                cb.setTag(i);
                cb.setOnClickListener(this);
            }
        }
        typedArray.recycle();

    }

    /**
     * 获取指定的子控件
     */
    private CheckBox getChild() {
        View view = LayoutInflater.from(getContext()).inflate(
                childId, this, false);
        if (!(view instanceof CheckBox)) {
            throw new RuntimeException(
                    "The attr child_layout's root must be a CheckBox!(指定的子控件必须是checkbox)");
        }
        return (CheckBox) view;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getChildCount() > 0) {
            flagY = 0;
            flagX = 0;
            sheight = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View v = getChildAt(i);
                // 对子控件进行测量
                measureChild(v, widthMeasureSpec, heightMeasureSpec);
                int w = v.getMeasuredWidth() + childMarginHorizontal * 2
                        + flagX + getPaddingLeft() + getPaddingRight();
                if (w >= getMeasuredWidth()) {
                    flagY++;
                    flagX = 0;
                }
                sheight = v.getMeasuredHeight();
                flagX += v.getMeasuredWidth() + 2 * childMarginHorizontal;
            }
            rowNumber = flagY;
        }

        int heitht = (flagY + 1) * (sheight + childMarginVertical)
                + childMarginVertical + getPaddingBottom() + getPaddingTop();
        setMeasuredDimension(getMeasuredWidth(), heitht);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        int[] sX = new int[rowNumber + 1];

        if (getChildCount() > 0) {

            startX = 0;
            startY = 0;
            rowNum = 0;
            for (int j = 0; j < getChildCount(); j++) {
                View view = getChildAt(j);
                int w = view.getMeasuredWidth() + 2 * childMarginHorizontal
                        + startX + getPaddingRight() + getPaddingLeft();
                if (w > getWidth()) {
                    startX = 0;
                    rowNum++;

                }
                startY = rowNum * (view.getHeight() + 2 * childMarginVertical);
                view.layout(startX, startY, startX + view.getMeasuredWidth(),
                        startY + view.getMeasuredHeight());
                startX += view.getMeasuredWidth() + 2 * childMarginHorizontal;

            }
        }
    }

    @Override
    public void onClick(View view) {

        // 单选
        if (singleChoice) {
            int tag = (Integer) view.getTag();
            boolean checked = ((CheckBox) view).isChecked();

            // 设置上次选中事件为false
            if (mLastCheckedPosition != -1 && mLastCheckedPosition != tag) {
                viewList.get(mLastCheckedPosition).setChecked(false);
            }

            if (checked) {
                mLastCheckedPosition = tag;
            } else {
                mLastCheckedPosition = -1;
            }
            if (listener != null) {
                listener.onItemChecked(this, tag, checked);
            }

            // 多选
        } else { // multiChoice
            int i = (Integer) view.getTag();
            CheckBox cb = (CheckBox) view;
            if (null != listener) {
                listener.onItemChecked(this, i, cb.isChecked());
            }
        }
    }

    private OnCheckerListener listener;

    public interface OnCheckerListener {
        void onItemChecked(MyRadioGroup group, int position, boolean checked);
    }

    /**
     * 设置监听接口
     *
     * @param listener
     */
    public void setOnCheckerListener(OnCheckerListener listener) {
        this.listener = listener;
    }

    /**
     * 设置选中的子空间
     *
     * @param position 子控件的下标
     * @return
     */
    public boolean setItemChecked(int position) {
        if (position >= 0 && position < getChildCount()) {
            if (singleChoice) {
                if (mLastCheckedPosition == position) {
                    return true;
                } else if (mLastCheckedPosition >= 0
                        && mLastCheckedPosition < getChildCount()) {
                    ((CheckBox) getChildAt(mLastCheckedPosition))
                            .setChecked(false);

                }
                mLastCheckedPosition = position;

            }
            ((CheckBox) getChildAt(position)).setChecked(true);
            return true;
        }
        return false;

    }

    /**
     * 获取选择模式
     *
     * @return
     */
    public boolean getIsSingleChoice() {
        return singleChoice;
    }

    /**
     * 获取被选中的子控件的值
     *
     * @return
     */
    public List<String> getCheckedValues() {
        List<String> list = new ArrayList<String>();
        if (singleChoice && mLastCheckedPosition >= 0
                && mLastCheckedPosition < viewList.size()) {
            list.add(viewList.get(mLastCheckedPosition).getText().toString());
            return list;
        }
        for (int i = 0; i < viewList.size(); i++) {
            if (viewList.get(i).isChecked()) {
                list.add(viewList.get(i).getText().toString());
            }
        }
        return list;
    }

    /**
     * 获取被选中的子控件的index
     */
    public int[] getCheckedItems() {
        if (singleChoice && mLastCheckedPosition >= 0
                && mLastCheckedPosition < viewList.size()) {
            return new int[]{mLastCheckedPosition};
        }
        SparseIntArray arr = new SparseIntArray();
        for (int i = 0; i < viewList.size(); i++) {
            if (viewList.get(i).isChecked()) {
                arr.put(i, i);
            }
        }
        if (arr.size() != 0) {
            int[] r = new int[arr.size()];
            for (int i = 0; i < arr.size(); i++) {
                r[i] = arr.keyAt(i);
            }
            return r;
        }
        return null;
    }

    /**
     * 添加子控件
     *
     * @param str
     * @return
     */
    public int append(String str) {
        CheckBox cb = getChild();
        cb.setText(str);
        cb.setTag(getChildCount());
        cb.setOnClickListener(this);
        viewList.add(cb);
        addView(cb);
        values.add(str);
        forceLayout = true;
        postInvalidate();
        return getChildCount() - 1;
    }

    /**
     * 批量添加子控件
     *
     * @param list
     */
    public void addAll(List<String> list) {
        if (list != null && list.size() > 0) {
            for (String str : list) {
                append(str);
            }
        }
    }

    /**
     * 移除子控件
     *
     * @param position 子控件位置
     * @return
     */
    public boolean remove(int position) {
        if (position >= 0 && position < viewList.size()) {
            CheckBox cb = viewList.remove(position);
            removeView(cb);
            values.remove(cb.getText().toString());
            forceLayout = true;
            if (position <= mLastCheckedPosition) { // before LastCheck
                if (mLastCheckedPosition == position) {
                    mLastCheckedPosition = -1;
                } else {
                    mLastCheckedPosition--;
                }
            }
            for (int i = position; i < viewList.size(); i++) {
                viewList.get(i).setTag(i);
            }
            postInvalidate();
            return true;
        }
        return false;
    }

    /**
     * 清除所有子控件
     */
    public void removeAll() {

        viewList.clear();
        values.clear();
        mLastCheckedPosition = 0;
        removeAllViews();
    }

    /**
     * 插入子控件
     *
     * @param position 插入子控件的位置
     * @param str      插入子控件的内容
     * @return
     */
    public boolean insert(int position, String str) {
        if (position < 0 || position > viewList.size()) {
            return false;
        }
        CheckBox cb = getChild();
        cb.setText(str);
        cb.setTag(position);
        cb.setOnClickListener(this);
        viewList.add(position, cb);
        addView(cb, position);
        values.add(position, str);
        forceLayout = true;
        if (position <= mLastCheckedPosition) { // before LastCheck
            mLastCheckedPosition++;
        }
        for (int i = position; i < viewList.size(); i++) {
            viewList.get(i).setTag(i);
        }
        postInvalidate();
        return true;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (null != viewList && viewList.size() > 0) {
            for (View v : viewList) {
                v.setEnabled(enabled);
            }
        }
    }

    public void clearChecked() {
        if (singleChoice) {
            if (mLastCheckedPosition >= 0
                    && mLastCheckedPosition < viewList.size()) {
                viewList.get(mLastCheckedPosition).setChecked(false);
                mLastCheckedPosition = -1;
                return;
            }
        }
        for (CheckBox cb : viewList) {
            if (cb.isChecked()) {
                cb.setChecked(false);
            }
        }
    }

    /**
     * 设置值
     *
     * @param list
     */
    public void setValues(List<String> list) {
        values = list;
        postInvalidate();
    }
}