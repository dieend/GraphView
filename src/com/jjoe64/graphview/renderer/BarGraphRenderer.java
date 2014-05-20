/**
 * This file is part of GraphView.
 *
 * GraphView is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GraphView is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GraphView.  If not, see <http://www.gnu.org/licenses/lgpl.html>.
 *
 * Copyright Jonas Gehring
 */

package com.jjoe64.graphview.renderer;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;

import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.GraphViewSeries.Values;
import com.jjoe64.graphview.formatter.LabelFormatter;
import com.jjoe64.graphview.formatter.NumberLabelFormatter;
import com.jjoe64.graphview.model.GraphViewDataInterface;

/**
 * Draws a Bar Chart
 * @author Muhammad Shahab Hameed
 */
public class BarGraphRenderer implements Renderer<GraphViewDataInterface>{
	private boolean drawValuesOnTop;
	private int valuesOnTopColor = Color.WHITE;
	private LabelFormatter formatter = new NumberLabelFormatter();
	private Paint paint;
	public BarGraphRenderer() {
		paint = new Paint();
	}

	public boolean getDrawValuesOnTop() {
		return drawValuesOnTop;
	}

	public int getValuesOnTopColor() {
		return valuesOnTopColor;
	}

	/**
	 * You can set the flag to let the GraphView draw the values on top of the bars
	 * @param drawValuesOnTop
	 */
	public void setDrawValuesOnTop(boolean drawValuesOnTop) {
		this.drawValuesOnTop = drawValuesOnTop;
	}

	public void setValuesOnTopColor(int valuesOnTopColor) {
		this.valuesOnTopColor = valuesOnTopColor;
	}

	@Override
	public void drawSeries(Canvas canvas, Values<GraphViewDataInterface> v,
			float graphwidth, float graphheight, float border, double minX,
			double minY, double diffX, double diffY, float horstart,
			GraphViewSeriesStyle style) {
		List<GraphViewDataInterface> values = v.v;
		float colwidth = graphwidth / (v.idxMaxX - v.idxMinX+1);

		paint.setStrokeWidth(style.thickness);

		float offset = 0;

		// draw data
		for (int i = v.idxMinX; i <= v.idxMaxX; i++) {
			float valY = (float) (values.get(i).getY() - minY);
			float ratY = (float) (valY / diffY);
			float y = graphheight * ratY;

			// hook for value dependent color
			if (style.getValueDependentColor() != null) {
				paint.setColor(style.getValueDependentColor().get(values.get(i)));
			} else {
				paint.setColor(style.color);
			}

			float left = (i * colwidth) + horstart -offset;
			float top = (border - y) + graphheight;
			float right = ((i * colwidth) + horstart) + (colwidth - 1) -offset;
			canvas.drawRect(left, top, right, graphheight + border - 1, paint);

			// -----Set values on top of graph---------
			//TODO
			if (drawValuesOnTop) {
				top -= 4;
				if (top<=border) top+=border+4;
				paint.setTextAlign(Align.CENTER);
				paint.setColor(valuesOnTopColor);
				paint.setAntiAlias(true);
				canvas.drawText(formatter.formatLabel(values.get(i).getY()),(left+right)/2, top, paint);
			}
		}
	}
}
