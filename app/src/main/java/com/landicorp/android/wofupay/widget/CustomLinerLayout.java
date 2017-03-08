package com.landicorp.android.wofupay.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.landicorp.android.wofupay.utils.ScreenUtils;

public class CustomLinerLayout extends LinearLayout  {

	// 子控件的集合
	private List<View> childs = new ArrayList<View>();
	
	private int width;

	// 横向间隔
	private int HorizontalDistance = ScreenUtils.dp2px(getContext(), 8);

	// 纵向间隔
	private int VerticalDistance = ScreenUtils.dp2px(getContext(), 14);

	public CustomLinerLayout(Context context) {
		this(context, null);
	}

	public CustomLinerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = 0;
		if (getChildCount() > 0) {
			int startX = 0;
			int linNum = 0;

			for (int i = 0; i < getChildCount(); i++) {
				View view = getChildAt(i);
				measureChild(view, widthMeasureSpec, widthMeasureSpec);

				// 获取子控件的最右边位置
				int endX = startX + HorizontalDistance * 2
						+ view.getPaddingLeft() + view.getPaddingRight()
						+ view.getMeasuredWidth();

				width = getMeasuredWidth();
				if (endX > getMeasuredWidth()) {
					startX = 0;
					linNum++;
				}
				startX = endX;

				height = (linNum + 1)
						* (VerticalDistance * 2 + view.getPaddingBottom()
								+ view.getPaddingTop() + view
									.getMeasuredHeight());
			}

		}

		setMeasuredDimension(widthMeasureSpec, height);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);

		if (getChildCount() > 0) {
			int startX = 0;
			int startY = 0;
			int rowNum = 0;
			for (int j = 0; j < getChildCount(); j++) {
				View view = getChildAt(j);
				int w = view.getMeasuredWidth() + 2 * HorizontalDistance
						+ startX + view.getPaddingRight()
						+ view.getPaddingLeft();
				
				if (w > width) {
					startX = 0;
					rowNum++;

				}
				startY = (rowNum) * (view.getHeight() + 2 * VerticalDistance);
				view.layout(startX, startY, startX + view.getWidth(), startY
						+ view.getHeight());
				startX += view.getWidth() + 2 * HorizontalDistance;
			}
		}

	}

	

	/**
	 * 根据Index删除子控件
	 * 
	 * @param index
	 */
	public void removeChildAt(int index) {
		childs.remove(index);
		removeViewAt(index);
		postInvalidate();
	}

	/**
	 * 添加子控件
	 * 
	 * @param view
	 */
	public void addChild(View view) {

		childs.add(childs.size(),view);
		addView(view);
		postInvalidate();
	}

	/**
	 * 清空子控件
	 */
	public void clear() {
		childs.clear();
		removeAllViews();
		postInvalidate();
	}
}
