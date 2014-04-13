package com.jjoe64.graphview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class GraphViewHolder extends LinearLayout {

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		for (int i=0; i<getChildCount(); i++) {
			View v = getChildAt(i); 
			if (v instanceof GraphView) {
				((GraphView) v).delegateTouchEvent(event);
			}
		}
		return true;
	}

	public GraphViewHolder(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public GraphViewHolder(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public GraphViewHolder(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

}
