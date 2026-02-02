package org.csviewer.mgr.analytics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JTabbedPane;

import org.csviewer.gui.analytics.CsXyPlotChartWithLineFitting;
import org.csviewer.mgr.BoneMeasureMgr;
import org.csviewer.mgr.SearchMgr;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

//import dao.BoneMeasureDAO;
import joinery.DataFrame;
import joinery.DataFrame.JoinType;

public class AnalyticsMgr {
	public static int EAAD = 0;
	public static int X_IS_1 = 1;
	
	//private AnimalFamilyOrganizer afOrg;
	//private MeasureDataOrganizer mdOrg;
	private SearchMgr sMgr;
	//private BoneMeasureDAO boneMeasureDAO;
	private BoneMeasureMgr bmMgr;
	private int xyPlotMode = EAAD;
	
	public AnalyticsMgr(SearchMgr sMgr, BoneMeasureMgr bmMgr) {
	//public AnalyticsMgr(SearchMgr sMgr, BoneMeasureDAO boneMeasureDAO) {
		//AnimalFamilyInfoAccessor afi = new AnimalFamilyInfoAccessor();
		//afOrg = new AnimalFamilyOrganizer(afi);
		//mdOrg = new MeasureDataOrganizer(new MeasureDataAccessor());
		this.bmMgr = bmMgr;
		this.sMgr = sMgr;
	}

	public void setXyPlotMode(int mode) {
		this.xyPlotMode  = mode;
	}

	// for xy plots: with EAAD as X or 1st in series as X
	public void buildXyPlotTab(JTabbedPane tabbedPane) {
		DataFrame<Object> measures = bmMgr.selectMeasures();
		DataFrame<Object> animals = sMgr.selectAnimals();
		//int[] animalInfoCols = {1,2,3,4,5}; 
		// for now: UniCode, FounderCode, DateOfBirth, Sex, BirthGroup
		DataFrame<Object> a = animals.retain(0,1,2,3,4);
	    System.out.println("In aMgr, measure cols==>" + measures.columns());
	    System.out.println("In aMgr, animal cols==>" + a.columns());
		
		DataFrame<Object> dataset = a.joinOn(measures, JoinType.INNER, 0);
		System.out.println(dataset.columns() + " (" + dataset.length() + ")");
		
		int len = bmMgr.getSelectedKeys().length + 2;
		String[] colsToInclude = new String[len];
		System.arraycopy(bmMgr.getSelectedKeys(), 0, colsToInclude, 0, 
				bmMgr.getSelectedKeys().length);
		colsToInclude[len-2] = "Unicode";
		colsToInclude[len-1] = "EAAD";
		dataset = dataset.retain(colsToInclude);
		System.out.println(dataset.columns() + " (" + dataset.length() + ")");
		dataset = dataset.dropna();
		
		addXyPlotAsTab(dataset, tabbedPane);
	}
	
	private void addXyPlotAsTab(DataFrame<Object> df, JTabbedPane tabbedPane) {
	    JFreeChart chart;
	    XYDataset dataset;
	    String title, xCaption;
	    
	    if (this.xyPlotMode == EAAD){
	    	dataset = createDatasetForEAAD(df);  
	    	title = "Estimated Age At Death";
	    	xCaption = "EAAD";
	    }
	    else {
	    	dataset = createDatasetForFirstAsX(df);
	    	xCaption = bmMgr.getSelectedKeys()[0];
	    	title = xCaption;
	    }

	    chart = ChartFactory.createScatterPlot("Measure vs. " + title,   
	            xCaption, "Measure", dataset);  

        //Changes background color  
        XYPlot plot = (XYPlot)chart.getPlot();  
        plot.setBackgroundPaint(Color.LIGHT_GRAY);  		          
        //plot.setBackgroundPaint(new Color(255,228,196));  		          
         
        // Create Panel  
        ChartPanel panel = new ChartPanel(chart);  
		tabbedPane.addTab("XY Plot", panel);
	}

	private XYDataset createDatasetForFirstAsX(DataFrame<Object> df) {
	    XYSeriesCollection dataset = new XYSeriesCollection();  
	    Set<Object> columnLabels = df.columns();
	    Map<String, Integer> keyToIndex = new HashMap<>();
	    int index=0;
	    for (Object key : columnLabels) {
	    	keyToIndex.put(key.toString(), index++);
	    	System.out.println(index + "::" + key.toString());
	    }
	    
	    int indexOfX = keyToIndex.get(bmMgr.getSelectedKeys()[0]);
	    System.out.println(xyPlotMode + "==>" + indexOfX);
	    
	    String var = "Measure";
	    XYSeries series1;
	    for (String s : bmMgr.getSelectedKeys()) {
	    	if (s.equals(bmMgr.getSelectedKeys()[0]))
	    		continue;
	    	var = s;
	    // Measure (EAAD,Measure) series  
		    series1 = new XYSeries(var);  
		    index = keyToIndex.get(var);
		    double x, y;
		    for (int i = 0; i<df.length(); i++) {
		    	//x = (Double)(df.get(i, indexOfX));
		    	//y = (Double)(df.get(i, index));
		    	x = Double.parseDouble(df.get(i, indexOfX).toString());
		    	y = Double.parseDouble(df.get(i, index).toString());
		    	System.out.println(i + ": (" + x + ", " + y + ")");
		    	series1.add(x, y);
		    	//series1.add((Double)(dfCase2.get(i, "EAAD")), (Double)(dfCase2.get(i,  "F3")));
		    }
	    
		    dataset.addSeries(series1);
	    }
	    
		return dataset;
	}

	public XYDataset createDatasetForEAAD(DataFrame<Object> df) {
	    XYSeriesCollection dataset = new XYSeriesCollection();  
	    Set<Object> columnLabels = df.columns();
	    Map<String, Integer> keyToIndex = new HashMap<>();
	    int index=0;
	    for (Object key : columnLabels) {
	    	keyToIndex.put(key.toString(), index++);
	    	System.out.println(index + "::" + key.toString());
	    }
	    
	    int indexOfX = keyToIndex.get("EAAD");
	    if (xyPlotMode == 1)
	    	indexOfX = keyToIndex.get(bmMgr.getSelectedKeys()[0]);
	    System.out.println(xyPlotMode + "==>" + indexOfX);
	    
	    String var = "Measure";
	    XYSeries series1;
	    for (Object c : columnLabels) {
	    	if (c.toString().equals("EAAD") || c.toString().equals("Unicode"))
	    		continue;
	    	var = c.toString();
	    // Measure (EAAD,Measure) series  
		    series1 = new XYSeries(var);  
		    index = keyToIndex.get(var);
		    double x, y;
		    String item;
		    for (int i = 0; i<df.length(); i++) {
		    	item = df.get(i, indexOfX).toString();
		    	if (item == null || item.equalsIgnoreCase("na"))
		    		continue;
		    	//x = (Double)(df.get(i, indexOfX));
		    	//y = (Double)(df.get(i, index));
		    	x = Double.parseDouble(item);
		    	item = df.get(i, index).toString();
		    	if (item == null || item.equalsIgnoreCase("na"))
		    		continue;
		    	y = Double.parseDouble(item);
		    	System.out.println(i + ": (" + x + ", " + y + ")");
		    	series1.add(x, y);
		    	//series1.add((Double)(dfCase2.get(i, "EAAD")), (Double)(dfCase2.get(i,  "F3")));
		    }
	    
		    dataset.addSeries(series1);
	    }
	    
		return dataset;
	}

	public void buildHeadcountChartTab(JTabbedPane cPanel) {
		// parameters hard-coded for now
		//int startYear=1955, endYear = 2018;
		int startYear=sMgr.getBeginSeason(), 
				endYear = sMgr.getEndSeason();
		//String cutoffDate = "07/01";
		String cutoffDate = sMgr.getCutoffDate(); //"01-Jul";	// changed to format "dd-MMM-yyyy"
		int minAge = sMgr.getMinAge();
		//int[][] headcountByYear = sMgr.getHeadcountForRange(cutoffDate, startYear, endYear);
		int[][] headcountByYear = sMgr.getHeadcountForRange(cutoffDate, 
				startYear, endYear, minAge);
	    
		XYSeriesCollection dataset = new XYSeriesCollection();
	    XYSeries headcountData = new XYSeries("Headcount");
		for (int i=0; i<headcountByYear.length; i++)
			headcountData.add(headcountByYear[i][0], headcountByYear[i][1]);
	    
		dataset.addSeries(headcountData);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Headcount Demo", 
                "Year", 
                "Headcount", 
                dataset, 
                PlotOrientation.VERTICAL, 
                true, 
                true, 
                false
        );
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setForegroundAlpha(0.75f);
        plot.getRenderer().setDefaultStroke(new BasicStroke(2f));
        NumberAxis axis = (NumberAxis) plot.getDomainAxis();
        axis.setAutoRangeIncludesZero(false);
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        DecimalFormat yearFormat = new DecimalFormat("###0"); 
        axis.setNumberFormatOverride(yearFormat);
        rangeAxis.setNumberFormatOverride(NumberFormat.getIntegerInstance());
       
        ChartPanel chartPanel = new ChartPanel(chart);
        cPanel.addTab("Headcount Line", chartPanel);
        cPanel.setSelectedIndex(cPanel.getTabCount()-1);
	}

	public void buildColumnChartTab(JTabbedPane cPanel) {
		DataFrame<Object> measures = bmMgr.selectMeasures();
		DataFrame<Object> animals = sMgr.selectAnimals();
		DataFrame<Object> dataset = animals.joinOn(measures, JoinType.INNER, 0);
		System.out.println(dataset.columns() + " (" + dataset.length() + ")");
		
		int len = bmMgr.getSelectedKeys().length + 2;
		String[] colsToInclude = new String[len];
		System.arraycopy(bmMgr.getSelectedKeys(), 0, colsToInclude, 0, 
				bmMgr.getSelectedKeys().length);
		colsToInclude[len-2] = "Unicode";
		colsToInclude[len-1] = "EAAD";
		dataset = dataset.retain(colsToInclude);
		System.out.println(dataset.columns() + " (" + dataset.length() + ")");
		dataset = dataset.dropna();
		// appear to be the same as xy-plot
		
		addColumnChartAsTab(dataset, cPanel);
	}

	private void addColumnChartAsTab(DataFrame<Object> df,	
			JTabbedPane cPanel) {	
		// get category names: all columns in dataset except tattoo and eaad
		CSColumnChartDataGenerator cdg = new CSColumnChartDataGenerator();
		cdg.prepareDataFor(df);
		
		CategoryDataset dataset = cdg.createDataset();
        JFreeChart colChart = ChartFactory.createBarChart(
                "Joint Measures by Category",           
                "Category",            
                "Frequence",            
                dataset,          
                PlotOrientation.VERTICAL,           
                true, true, false);
        
        // Create Panel  
        ChartPanel panel = new ChartPanel(colChart);  
        cPanel.addTab("Column Chart", panel);
	}

	public void buildXyPlotWithLineFitting(JTabbedPane cPanel) {
        DataFrame<Object> df = bmMgr.selectMeasures();
        
        //AnalyticsMgr aMgr = new AnalyticsMgr(sMgr, bmMgr);
        
        //XYDataset dataset = RegressionTest.createTestDataset();
        XYDataset dataset = createDatasetForEAAD(df.dropna());
        
        JFreeChart chart = ChartFactory.createScatterPlot("Measure vs. " + "Linear",   
	            "EAAD", "Measure", dataset); 
        		//RegressionTest.createChart(dataset);
        CsXyPlotChartWithLineFitting chartPanel = 
        		new CsXyPlotChartWithLineFitting(chart, true, true, true, true, true);
        //chartPanel.setXYData(dataset); // why does it need this?
        chartPanel.setCurMeasure(bmMgr.getSelectedKeys()[0]);
        cPanel.addTab("XY Plot w/ Fitting", chartPanel);
	}
}
