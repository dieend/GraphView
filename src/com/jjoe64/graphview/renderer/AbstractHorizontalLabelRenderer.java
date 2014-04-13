package com.jjoe64.graphview.renderer;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;

import com.jjoe64.graphview.GraphViewStyle;
import com.jjoe64.graphview.model.GraphViewDataInterface;

public abstract class AbstractHorizontalLabelRenderer implements HorizontalLabelRenderer {
	String[] horlabels;
	Paint paint = new Paint();
	GraphViewStyle graphViewStyle;

	
	protected abstract String[] generateHorizontalLabels(float graphwidth, List<? extends GraphViewDataInterface> series);
	protected abstract boolean isLabelValid(float graphwidth, List<? extends GraphViewDataInterface> series);

	@Override
	public final void drawHorizontalLabels(Canvas canvas,
			List<? extends GraphViewDataInterface> series, 
			float border, float graphwidth, double diffX, float horstart, float canvasHeight) {
		// horizontal labels + lines
		if (horlabels == null || !isLabelValid(graphwidth, series)) {
			horlabels = generateHorizontalLabels(graphwidth, series);
		}
		int hors = horlabels.length - 1;
		for (int i = 0; i < horlabels.length; i++) {
			paint.setColor(graphViewStyle.getGridColor());
			float x = ((graphwidth / hors) * i) + horstart;
			canvas.drawLine(x, canvasHeight - border, x, border, paint);
            
            paint.setTextAlign(Align.CENTER);
            if (i==horlabels.length-1)
                paint.setTextAlign(Align.RIGHT);
            if (i==0)
                paint.setTextAlign(Align.LEFT);
            paint.setColor(graphViewStyle.getHorizontalLabelsColor());
            if (horlabels[i] != null) {
            	canvas.drawText(horlabels[i], x, canvasHeight - 4, paint);
            }
        
		}
	}
}
