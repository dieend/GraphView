package com.jjoe64.graphview.renderer;

import android.graphics.Canvas;

import com.jjoe64.graphview.GraphViewSeries.Values;
import com.jjoe64.graphview.model.GraphViewDataInterface;

public interface HorizontalLabelRenderer {
	public void drawHorizontalLabels(Canvas canvas,
			Values<? extends GraphViewDataInterface>  values,
			float border,
			float graphwidth,
			double diffX,
			float horstart,
			float canvasHeight);
}
