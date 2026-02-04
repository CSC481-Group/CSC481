package org.csviewer.mgr.analytics;

import java.util.Collection;

import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.fitting.AbstractCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresBuilder;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.linear.DiagonalMatrix;
import org.jfree.data.xy.XYDataset;

public class LogFitter extends AbstractCurveFitter {

	private WeightedObservedPoints observations;

	public LogFitter() {
		
	}
	
	public LogFitter(WeightedObservedPoints observations) {
		this.observations = observations;
	}
	
	public LogFitter(XYDataset xyData) {
		setPointsWithDataSet(xyData);
	}

	private void setPointsWithDataSet(XYDataset xyData) {
		this.observations = new WeightedObservedPoints();
		
		for (int row=0; row<xyData.getItemCount(0); row++) {
			observations.add(xyData.getXValue(0, row), xyData.getYValue(0, row));
		}
	}

	@Override
	protected LeastSquaresProblem getProblem(Collection<WeightedObservedPoint> points) {
        final int len = points.size();
        final double[] target  = new double[len];
        final double[] weights = new double[len];
        final double[] initialGuess = { 1.0, 1.0 };

        int i = 0;
        for(WeightedObservedPoint point : points) {
            target[i]  = point.getY();
            weights[i] = point.getWeight();
            i += 1;
        }

        final AbstractCurveFitter.TheoreticalValuesFunction model = new
            AbstractCurveFitter.TheoreticalValuesFunction(new LogFunction(), points);

        return new LeastSquaresBuilder().
            maxEvaluations(Integer.MAX_VALUE).
            maxIterations(Integer.MAX_VALUE).
            start(initialGuess).
            target(target).
            weight(new DiagonalMatrix(weights)).
            model(model.getModelFunction(), model.getModelFunctionJacobian()).
            build();
	}

	public Collection<WeightedObservedPoint> getObservations() {
		return this.observations.toList();
	}

}

class LogFunction implements ParametricUnivariateFunction {
    public double value(double t, double... parameters) {
        return parameters[0] * Math.log(t) + parameters[1]; 
    }
    
    public double[] gradient(double t, double... parameters) {
        //final double a = parameters[0];
        //final double b = parameters[1];

        return new double[] {
        		Math.log(t),
        		1.0
        };
    }
}
