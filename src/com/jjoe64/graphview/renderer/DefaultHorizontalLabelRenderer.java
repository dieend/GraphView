package com.jjoe64.graphview.renderer;

import java.security.InvalidParameterException;

import android.graphics.Rect;

import com.jjoe64.graphview.GraphViewSeries.Values;
import com.jjoe64.graphview.GraphViewStyle;
import com.jjoe64.graphview.formatter.LabelFormatter;
import com.jjoe64.graphview.formatter.NumberLabelFormatter;
import com.jjoe64.graphview.model.GraphViewDataInterface;

public class DefaultHorizontalLabelRenderer extends AbstractHorizontalLabelRenderer{

	LabelFormatter labelFormatter;
	Rect textBounds;
	public DefaultHorizontalLabelRenderer(GraphViewStyle style) {
		graphViewStyle = style;
		textBounds = new Rect();
		labelFormatter = new NumberLabelFormatter();
	}
	public DefaultHorizontalLabelRenderer(GraphViewStyle style, LabelFormatter formatter) {
		if (formatter == null) throw new InvalidParameterException("formatter must not null");
		graphViewStyle = style;
		textBounds = new Rect();
		labelFormatter = formatter;
	}	
	
	private float calculateOptimalTextSize(String testWord, float rectWidth) {
		// TODO auto text size
		float ret = 1;
		return ret;
	}
	
	
	@Override
	protected String[] generateHorizontalLabels(float graphwidth,
			Values<? extends GraphViewDataInterface> series) {
		// TODO numlabels
		int numLabels = 5;
		String[] labels = new String[numLabels];
		
		if (series.idxMaxX>=series.idxMinX) {
			double min = series.v.get(series.idxMinX).getX();
			double max = series.v.get(series.idxMaxX).getX();
			
			String longestWord = "";
			for (int i=0; i<numLabels; i++) {
				labels[i] = labelFormatter.formatLabel(min + ((max-min)*i/numLabels));
				longestWord = longestWord.length() > labels[i].length()? longestWord:labels[i];
			}
		}
		return labels;
	}

	@Override
	protected boolean isLabelValid(float graphwidth,
			Values<? extends GraphViewDataInterface> series) {
		// TODO invalidate
		return false;
	}

	
	
}
