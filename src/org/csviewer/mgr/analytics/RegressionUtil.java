package org.csviewer.mgr.analytics;

import java.awt.Rectangle;

import org.jfree.data.statistics.Regression;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.csviewer.dao.BoneMeasureDAO;
import org.csviewer.gui.analytics.CsXyPlotChartWithLineFitting;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import joinery.DataFrame;

public class RegressionUtil {
	public static void createLinearModelForChart(CsXyPlotChartWithLineFitting chart) {
		XYDataset xyData = chart.getDataset();
        double[] coefficients = Regression.getOLSRegression(xyData, 0);

        chart.setShowModel(false, new RegressionModel(RegressionType.Linear, coefficients));
        double b = coefficients[0]; // intercept
        double m = coefficients[1]; // slope       
        XYSeries linear = new XYSeries("Linear");
        double x0 = 0;
        linear.add(x0, m * x0 + b);
        x0 = 32;
        linear.add(x0, m * x0 + b);
        ((XYSeriesCollection) xyData).addSeries(linear);
        int seriesCount = xyData.getSeriesCount();
		((XYLineAndShapeRenderer)chart.getChart().getXYPlot().getRenderer()).
		setSeriesShapesVisible(seriesCount-1, false);
		((XYLineAndShapeRenderer)chart.getChart().getXYPlot().getRenderer()).
		setSeriesLinesVisible(seriesCount-1, true);
	}

	public static XYDataset getXYDataByGender(DataFrame<Object> df, String gender,
			BoneMeasureDAO dao) {
			//MeasureDataOrganizer org) {
	    XYSeriesCollection dataset = new XYSeriesCollection(); 
	    
	    /*
		System.out.println(a.getMeasureDataframe().columns());
		String[] catMeasures = {"FL"};
		//String tattoo = df.col("Tattoo").get(1).toString();
		//System.out.println(tattoo + " is " + df.col("Sex").get(1));
		df = org.getMeasureWithEAAD(catMeasures ).dropna();
	    */
	    
	    df.dropna();
	    
		//MeasureDataAccessor a = new MeasureDataAccessor();
		//MeasureDataOrganizer org = new MeasureDataOrganizer(a);
	    
	    XYSeries y4Gender = new XYSeries(gender);  
	    double x, y;
	    String unicode, genderLabelInDB;
	    
    	//System.out.println("x,y4Gender");
	    Object v;
	    for (int row = 0; row < df.length(); row++) {
	    	v = df.get(row, 1);
	    	if (v == null || v.toString().equalsIgnoreCase("na"))
	    		continue;
	    	x = Double.parseDouble(v.toString());
	    	v = df.get(row, 2);
	    	if (v == null || v.toString().equalsIgnoreCase("na"))
	    		continue;
	    	y = Double.parseDouble(v.toString());
	    	unicode = df.row(row).get(0).toString(); 
	    	//genderLabelInDB = org.getGenderByTattoo(tattoo);
	    	genderLabelInDB = dao.getGenderByUnicode(unicode);
	    	System.out.println("In RegrUtil: " + unicode + " is " + genderLabelInDB);
	    	if (genderLabelInDB.equalsIgnoreCase(gender.substring(0, 1))) {
	    		y4Gender.add(x, y);	// Un-switched order!
		    	//System.out.println(x+","+y);
	    	}
	    }
	    
	    dataset.addSeries(y4Gender);
		return dataset;
	}

	public static void createStepwiseModelForChart(CsXyPlotChartWithLineFitting chart) {
		XYDataset xyData = chart.getDataset();
		StepwiseFitter fitter = new StepwiseFitter(xyData);
        double[] coefficients = fitter.fit(fitter.getObservations());
        	/*
        	{85.30626, // l0
        		4.272242, // am
        		163.662,  // lm
        		0.291367};  // k2
        		*/

        chart.setShowModel(false, new RegressionModel(RegressionType.Stepwise, coefficients));
        XYSeries stepwise = new XYSeries("Stepwise");
        stepwise.add(0, coefficients[0]);
        stepwise.add(coefficients[1], coefficients[2]);
        stepwise.add(32, coefficients[2] + (32 - coefficients[1]) * coefficients[3]);
        ((XYSeriesCollection) xyData).addSeries(stepwise);
        int seriesCount = xyData.getSeriesCount();
		((XYLineAndShapeRenderer)chart.getChart().getXYPlot().getRenderer()).
		setSeriesShapesVisible(seriesCount-1, false);
		((XYLineAndShapeRenderer)chart.getChart().getXYPlot().getRenderer()).
		setSeriesLinesVisible(seriesCount-1, true);
	}

	public static XYDataset getXYDataByGender(Comparable seriesKey, String gender) {
		//MeasureDataAccessor a = new MeasureDataAccessor();
		//MeasureDataOrganizer org = new MeasureDataOrganizer(a);
		BoneMeasureDAO dao = new BoneMeasureDAO();
		DataFrame<Object> df = dao.getMeasureWithEAAD(seriesKey.toString());
    	System.out.println("In RegrUtil " + seriesKey.toString() + " has " + df.length());
		
		return getXYDataByGender(df, gender, dao);
	}

	public static void createLogModelForChart(CsXyPlotChartWithLineFitting chart) {
		XYDataset xyData = chart.getDataset();
		LogFitter fitter = new LogFitter(xyData);
        double[] coefficients = fitter.fit(fitter.getObservations());
        	/*
        	{85.30626, // l0
        		4.272242, // am
        		163.662,  // lm
        		0.291367};  // k2
        		*/

        chart.setShowModel(false, new RegressionModel(RegressionType.Log, coefficients));
        XYSeries logModel = new XYSeries("Log");
        double x, y;
        double mm = 1/12.;
        
        // fit month 1 through 11
        for (int m=1; m<=11; m++) {
        	x = m * mm;
        	logModel.add(x, coefficients[1] + coefficients[0] * Math.log(x));
        }
        
        // fit year 1 through 32
        for (int i=1; i <=32; i++) {
        	x = i;
        	y = coefficients[1] + coefficients[0] * Math.log(x);
        	logModel.add(x, y);
        }
        
        System.out.println(xyData.getSeriesCount() + " is " + xyData.getSeriesKey(0));
        ((XYSeriesCollection) xyData).addSeries(logModel);
        int seriesCount = xyData.getSeriesCount();
		((XYLineAndShapeRenderer)chart.getChart().getXYPlot().getRenderer()).
		setSeriesShapesVisible(seriesCount-1, false);
		((XYLineAndShapeRenderer)chart.getChart().getXYPlot().getRenderer()).
		setSeriesLinesVisible(seriesCount-1, true);
	}

	public static XYDataset getXYDataByMatril(Comparable seriesKey, String mcode) {
		//MeasureDataAccessor a = new MeasureDataAccessor();
		//MeasureDataOrganizer org = new MeasureDataOrganizer(a);
		BoneMeasureDAO dao = new BoneMeasureDAO();
		DataFrame<Object> df = dao.getMeasureWithEAAD(seriesKey.toString());
    	System.out.println("In RegrUtil " + seriesKey.toString() + " has " + df.length());
		
		return getXYDataByMatril(df, mcode, dao);
	}

	private static XYDataset getXYDataByMatril(DataFrame<Object> df, String mcode,
			BoneMeasureDAO dao) {
			//MeasureDataOrganizer org) {
		System.out.println("In RegrUtil, mcode = " + mcode);
	    XYSeriesCollection dataset = new XYSeriesCollection(); 

	    df.dropna();
	    
	    XYSeries y4Mcode = new XYSeries(mcode);  
	    double x, y;
	    String unicode, mcodeInDB;
	    
    	//System.out.println("x,y4Gender");
	    Object v;
	    for (int row = 0; row < df.length(); row++) {
	    	v = df.get(row, 1);
	    	if (v == null || v.toString().equalsIgnoreCase("na"))
	    		continue;
	    	x = Double.parseDouble(v.toString());
	    	v = df.get(row, 2);
	    	if (v == null || v.toString().equalsIgnoreCase("na"))
	    		continue;
	    	y = Double.parseDouble(v.toString());
	    	unicode = df.row(row).get(0).toString(); 
	    	//mcodeInDB = org.getMcodeByTattoo(tattoo);
	    	mcodeInDB = dao.getMcodeByUnicode(unicode);
	    	//System.out.println("In RegrUtil: " + tattoo + " is " + mcodeInDB);
	    	if (mcode.equals("NONE")) {
	    		if (mcodeInDB == null || mcodeInDB.length() == 0 || 
	    				mcodeInDB.equals("Prob wt ID") || 
	    				mcodeInDB.equals("NONE"))
		    		y4Mcode.add(x, y);	// Un-switched order
	    	}  		
	    	else 
	    		if (mcodeInDB.equalsIgnoreCase(mcode)) {
		    		y4Mcode.add(x, y);	// Un-switched order
		    	//System.out.println(x+","+y);
	    	}
	    }
	    
	    dataset.addSeries(y4Mcode);
		return dataset;
	}
}
