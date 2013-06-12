package com.blstream.urbangame.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.blstream.urbangame.R;

public class ExpandableListHeaderDivider extends View {
	
	private Paint paint;
	private static final int DEFAULT_STROKE = 3;
	
	public ExpandableListHeaderDivider(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs, 0);
	}
	
	public ExpandableListHeaderDivider(Context context) {
		super(context);
		init(context, null, 0);
	}
	
	public ExpandableListHeaderDivider(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}
	
	private void init(Context context, AttributeSet attrs, int defStyle) {
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		if (attrs != null) {
			TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ExpandableListHeaderDivider, 0, 0);
			int colorValue = ta.getColor(R.styleable.ExpandableListHeaderDivider_valueColor,
				R.color.expandable_list_header);
			int strokeValue = ta.getInteger(R.styleable.ExpandableListHeaderDivider_lineStroke, DEFAULT_STROKE);
			paint.setColor(colorValue);
			paint.setStrokeWidth(strokeValue);
			ta.recycle();
		}
		else {
			paint.setColor(context.getResources().getColor(R.color.expandable_list_header));
			paint.setStrokeWidth(DEFAULT_STROKE);
		}
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		
		// 5% of width
		int widthPadding = 5 * width / 100;
		
		canvas.drawLine(widthPadding, height / 2, width - widthPadding, height / 2, paint);
	}
}
