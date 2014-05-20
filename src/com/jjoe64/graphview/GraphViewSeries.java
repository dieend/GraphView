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

package com.jjoe64.graphview;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;

import com.jjoe64.graphview.model.GraphViewDataInterface;
import com.jjoe64.graphview.renderer.HorizontalLabelRenderer;
import com.jjoe64.graphview.renderer.Renderer;

/**
 * a graphview series.
 * holds the data, description and styles
 */
public class GraphViewSeries <T extends GraphViewDataInterface>{
	/**
	 * graph series style: color and thickness
	 */
	static public class GraphViewSeriesStyle {
		public int color = 0xff0077cc;
		public int thickness = 3;
		private ValueDependentColor valueDependentColor;

		public GraphViewSeriesStyle() {
			super();
		}
		public GraphViewSeriesStyle(int color, int thickness) {
			super();
			this.color = color;
			this.thickness = thickness;
		}
		
		public ValueDependentColor getValueDependentColor() {
			return valueDependentColor;
		}
		
		/**
		 * the color depends on the value of the data.
		 * only possible in BarGraphView
		 * @param valueDependentColor
		 */
		public void setValueDependentColor(ValueDependentColor valueDependentColor) {
			this.valueDependentColor = valueDependentColor;
		}
	}
	
	public static class Values<T> {
		public List<T> v;
		public int idxMinX;
		public int idxMaxX;
	}
	
	private List<T> values; 
	final String description;
	private GraphViewSeriesStyle style;
	private final List<GraphView> graphViews = new ArrayList<GraphView>();

	public GraphViewSeries(List<T> values, Renderer<T> renderer) {
		description = null;
		style = new GraphViewSeriesStyle();
		this.values = values;
		this.renderer = renderer;
	}

	public GraphViewSeries(String description, GraphViewSeriesStyle style, List<T> values, Renderer<T> renderer) {
		super();
		this.description = description;
		if (style == null) {
			style = new GraphViewSeriesStyle();
		}
		this.style = style;
		this.values = values;
		this.renderer = renderer;
	}

	/**
	 * this graphview will be redrawn if data changes
	 * @param graphView
	 */
	public void addGraphView(GraphView graphView) {
		this.graphViews.add(graphView);
	}

	/**
	 * add one data to current data
	 * @param value the new data to append
	 * @param scrollToEnd true => graphview will scroll to the end (maxX)
	 * @deprecated please use {@link #appendData(GraphViewDataInterface, boolean, int)} to avoid memory overflow
	 */
	@Deprecated
	public void appendData(T value, boolean scrollToEnd) {
		values.add(value);
		for (GraphView g : graphViews) {
			if (scrollToEnd) {
				g.scrollToEnd();
			}
		}
	}

	/**
	 * add one data to current data
	 * @param value the new data to append
	 * @param scrollToEnd true => graphview will scroll to the end (maxX)
	 * @param maxDataCount if max data count is reached, the oldest data value will be lost
	 */
	public void appendData(T value, boolean scrollToEnd, int maxDataCount) {
		synchronized (values) {
			int curDataCount = values.size();
			if (curDataCount < maxDataCount) {
				values.add(value);
			} else {
				values.remove(0);
				values.add(value);
			}
		}

		// update linked graph views
		for (GraphView g : graphViews) {
			if (scrollToEnd) {
				g.scrollToEnd();
			}
		}
	}

	/**
	 * @return series styles. never null
	 */
	public GraphViewSeriesStyle getStyle() {
		return style;
	}

	public void setStyle(GraphViewSeriesStyle style) {
		this.style = style;
	}
	/**
	 * you should use {@link GraphView#removeSeries(GraphViewSeries)}
	 * @param graphView
	 */
	void removeGraphView(GraphView graphView) {
		graphViews.remove(graphView);
	}

	/**
	 * clears the current data and set the new.
	 * redraws the graphview(s)
	 * @param values new data
	 */
	public void resetData(List<T> values) {
		this.values.clear();
		this.values.addAll(values); 
		for (GraphView g : graphViews) {
			g.redrawAll();
		}
	}
	private Renderer<T> renderer;
	private HorizontalLabelRenderer horizontalLabelRenderer;
	/**
	 * without viewport. return all values
	 * @return
	 */
	public Values<T> valuesToDraw() {
		Values<T> ret = new Values<T>();
		ret.v = values;
		ret.idxMinX = 0;
		ret.idxMaxX = values.size()-1;
		return ret;
	}
	/**
	 * with X viewport
	 * @param minx
	 * @param sizex
	 * @return
	 */
	public Values<T> valuesToDraw(double minx, double sizex) {
		// TODO modify y when able to zoom in Y
		
		boolean found = false;
		boolean finish = false;
		Values<T> ret = new Values<T>();
		ret.v = values;
		ret.idxMinX = 0;
		ret.idxMaxX = -1;
		for (int i=0; i<values.size(); i++) {
			if (!found && (values.get(i).getX() >= minx) && (values.get(i).getX() <= minx+sizex)) {
				found = true;
				ret.idxMinX = i;
			} else if (found && (values.get(i).getX() > minx+sizex)) {
				finish = true;
				ret.idxMaxX = i;
			}
		}
		if (found && !finish) {
			ret.idxMaxX = values.size()-1;
		}
		return ret;
	}
	
	public double getMaxX() {
		double highest = 0;
		if (values.size() > 0) {
			highest = Math.max(highest, values.get(values.size()-1).getX());
		}
		return highest;
	}
	public double getMaxY() {
		double largest= Integer.MIN_VALUE;
		Values<T> values = valuesToDraw();
		for (int ii=values.idxMinX; ii<=values.idxMaxX; ii++) {
			if (values.v.get(ii).getY() > largest) {
				largest = values.v.get(ii).getY();
			}
		}
		return largest;
	}
	public double getMaxY(double minx, double sizex) {
		double largest= Integer.MIN_VALUE;
		Values<T> values = valuesToDraw(minx, sizex);
		for (int ii=values.idxMinX; ii<=values.idxMaxX; ii++) {
			if (values.v.get(ii).getY() > largest) {
				largest = values.v.get(ii).getY();
			}
		}
		return largest;
	}

	public double getMinX() {
		// 
		double lowest = 0;
		if (values.size() > 0) {
			lowest = values.get(0).getX();
		}
		return lowest;
	}
	
	public double getMinY() {
		double smallest;
		smallest = Integer.MAX_VALUE;
		Values<T> values = valuesToDraw();
		for (int ii=values.idxMinX; ii<=values.idxMaxX; ii++) {
			if (values.v.get(ii).getY() < smallest) {
				smallest = values.v.get(ii).getY();
			}
		}
		return smallest;
	}
	
	public double getMinY(double minx, double sizex) {
		double smallest;
		smallest = Integer.MAX_VALUE;
		Values<T> values = valuesToDraw(minx, sizex);
		for (int ii=values.idxMinX; ii<=values.idxMaxX; ii++) {
			if (values.v.get(ii).getY() < smallest) {
				smallest = values.v.get(ii).getY();
			}
		}
		return smallest;
	}
	public void drawSeries(Canvas canvas,
			float graphwidth, float graphheight, float border, double minX,
			double minY, double diffX, double diffY, float horstart) {
		renderer.drawSeries(canvas, valuesToDraw(minX, diffX), graphwidth, graphheight, border, minX, minY, diffX, diffY, horstart, style);
//		renderer.drawSeries(canvas, valuesToDraw(), graphwidth, graphheight, border, minX, minY, diffX, diffY, horstart, style);
		
	}
	public void drawHorizontalLabels(Canvas canvas,
			float border,
			float graphwidth,
			double minX,
			double diffX,
			float horstart,
			float canvasHeight,
			GraphViewStyle graphViewStyle) {
		if (horizontalLabelRenderer != null) {
			horizontalLabelRenderer.drawHorizontalLabels(canvas, valuesToDraw(minX, diffX), border, graphwidth, diffX, horstart, canvasHeight, graphViewStyle);
//			horizontalLabelRenderer.drawHorizontalLabels(canvas, valuesToDraw(), border, graphwidth, diffX, horstart, canvasHeight);
		}
	}
	public void setHorizontalLabelRenderer(HorizontalLabelRenderer horizontalLabelRenderer) {
		this.horizontalLabelRenderer = horizontalLabelRenderer;
	}
}
