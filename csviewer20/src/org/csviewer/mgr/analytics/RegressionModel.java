package org.csviewer.mgr.analytics;

import java.util.Arrays;

public class RegressionModel {
	private RegressionType type;
	private double[] coeffs;

	public RegressionModel(RegressionType type, double[] coeffs) {
		this.type = type;
		this.coeffs = coeffs;
	}
	
	public RegressionType getType() {
		return this.type;
	}
	
	public double[] getCoeffs() {
		return this.coeffs;
	}
	
	public String toString() {
		return type.name() + " " + Arrays.asList(coeffs);
	}
}
