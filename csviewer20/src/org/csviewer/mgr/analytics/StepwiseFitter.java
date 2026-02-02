package org.csviewer.mgr.analytics;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.fitting.AbstractCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresBuilder;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.linear.DiagonalMatrix;
import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.XYDataset;

import joinery.DataFrame;

public class StepwiseFitter extends AbstractCurveFitter {

	public static void main(String[] args) {
		String file = "data/FLxyByGender.csv";
		final WeightedObservedPoints obs = readObs(file , "M"); //getObs();
		List<WeightedObservedPoint> points = obs.toList();
		
		StepwiseFitter fitter = new StepwiseFitter();
		
        final double coeffs[] = fitter.fit(points);
        System.out.println(Arrays.toString(coeffs));
        //System.out.println(fitter.);
	}

	private WeightedObservedPoints observations;
	
	public StepwiseFitter() {
		
	}
	
	public StepwiseFitter(WeightedObservedPoints observations) {
		this.observations = observations;
	}
	
	public StepwiseFitter(XYDataset dataset) {
		setPointsWithDataSet(dataset);
	}
	
	public void setPointsWithDataSet(XYDataset dataset) {
		this.observations = new WeightedObservedPoints();
		
		for (int row=0; row<dataset.getItemCount(0); row++) {
			observations.add(dataset.getXValue(0, row), dataset.getYValue(0, row));
		}
	}
	
	private static WeightedObservedPoints readObs(String file, String gender) {
		WeightedObservedPoints obs = new WeightedObservedPoints();
		try {
			DataFrame<Object> df = DataFrame.readCsv(file);
			double x, y;
			int c = (gender.equals("F"))?1:3;
			String yForGender;
			int count = 0;
			for (int r=0; r<df.length(); r++) {
				x = Double.parseDouble(df.get(r, 0).toString());
				if (df.get(r, c) != null) {
					yForGender = df.get(r, c).toString();
					y = Double.parseDouble(yForGender);
					obs.add(x, y);
					count++;
				}
			}
			
			System.out.println(gender + " x" + count);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obs;
	}

	@Override
	protected LeastSquaresProblem getProblem(Collection<WeightedObservedPoint> points) {
        final int len = points.size();
        final double[] target  = new double[len];
        final double[] weights = new double[len];
        final double[] initialGuess = { 50.0, 3.5, 100.0, 0.0 };

        int i = 0;
        for(WeightedObservedPoint point : points) {
            target[i]  = point.getY();
            weights[i] = point.getWeight();
            i += 1;
        }

        final AbstractCurveFitter.TheoreticalValuesFunction model = new
            AbstractCurveFitter.TheoreticalValuesFunction(new TwoStepLinearFunction(), points);

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
		return observations.toList();
	}

	public void setPoints(WeightedObservedPoints obs) {
		this.observations = obs;
	}

}

class TwoStepLinearFunction implements ParametricUnivariateFunction {
    public double value(double t, double... parameters) {
    	// p[0] : l0 == length at birth
    	// p[1] : am == age of mature
    	// p[2] : lm == length at mature age
    	// p[3] : k2 == slope after mature
    	double k1 = (parameters[2] - parameters[0]) / parameters[1];
    	double y = parameters[0];
    	if (t < parameters[1]) 
    		y = parameters[0] + k1 * t;
    	else
    		y = parameters[2] + parameters[3] * (t - parameters[1]);
        return y;
    }
    
    public double[] gradient(double t, double... parameters) {
        final double l0 = parameters[0];
        final double am = parameters[1];
        final double lm = parameters[2];
        final double k2 = parameters[3];
    	//double k1 = (parameters[2] - parameters[0]) / parameters[1];
        
        double[] grad = new double[4];
    	if (t < parameters[1]) {
    		grad[0] = 1.0 - t/am;
    		grad[1] = (l0 - lm) * t / am / am;
    		grad[2] = t / am;
    		grad[3] = 0.0;
    	}
    	else{
    		grad[0] = 0.0;
    		grad[1] = -k2;
    		grad[2] = 1.0;
    		grad[3] = t - am;
    	}
    	
        return grad;
    }
}