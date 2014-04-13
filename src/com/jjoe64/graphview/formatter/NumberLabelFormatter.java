package com.jjoe64.graphview.formatter;

import java.text.NumberFormat;

public class NumberLabelFormatter implements LabelFormatter {
	NumberFormat numberformatter;
	double lowestValue;
	double highestValue;
	@Override
	public String formatLabel(double value) {
		if (numberformatter == null) {
			numberformatter = NumberFormat.getNumberInstance();
//			double highestValue = isValueX ? getMaxX(false) : getMaxY();
//			double lowestValue = isValueX ? getMinX(false) : getMinY();
			if (highestValue - lowestValue < 0.1) {
				numberformatter.setMaximumFractionDigits(6);
			} else if (highestValue - lowestValue < 1) {
				numberformatter.setMaximumFractionDigits(4);
			} else if (highestValue - lowestValue < 20) {
				numberformatter.setMaximumFractionDigits(3);
			} else if (highestValue - lowestValue < 100) {
				numberformatter.setMaximumFractionDigits(1);
			} else {
				numberformatter.setMaximumFractionDigits(0);
			}
		}
		return numberformatter.format(value);
	}

}
