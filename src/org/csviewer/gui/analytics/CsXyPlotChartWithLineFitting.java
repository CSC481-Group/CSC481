package org.csviewer.gui.analytics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.csviewer.dao.BoneMeasureDAO;
import org.csviewer.mgr.SearchMgr;
import org.csviewer.mgr.analytics.AnalyticsMgr;
import org.csviewer.mgr.analytics.RegressionModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.text.TextBlock;
import org.jfree.chart.text.TextBox;
import org.jfree.chart.text.TextLine;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import joinery.DataFrame;

public class CsXyPlotChartWithLineFitting extends ChartPanel {
	private LineFittingMenu menu;
	
	private XYDataset xyData;
	//private MeasureDataOrganizer org;
	private BoneMeasureDAO boneMeasureDAO; //= new BoneMeasureDAO();
	private String curMeasure;
	
	private TextBox modelText;
	
	private boolean showModel = false;
	private RegressionModel model;
	private double[] coeffs;

	private NumberFormat decimalFormat = new DecimalFormat("#.0000");
	
	Rectangle smallRect = new Rectangle(2, 2);

	public CsXyPlotChartWithLineFitting(JFreeChart chart, boolean properties, 
			boolean save, boolean print, boolean zoom, boolean tooltips) {
		//super(chart, properties, save, print, zoom, tooltips);
		super(chart);
		chart.setTitle("Measure vs. EAAD w/ Line Fitting");
		System.out.println(chart.getTitle().getText());
		
		chart.getXYPlot().getRenderer().setSeriesShape(0, new Rectangle(2, 2));
		
		xyData = chart.getXYPlot().getDataset();
		menu = new LineFittingMenu(this);
		addMouseListener(this);
		//System.out.println(menu);
	}
	
	/*
	 * used in v1.1-1.2
	public void setOrg(MeasureDataOrganizer org) {
		this.org = org;
	}
	*/
	
	public void setOrg(BoneMeasureDAO boneMeasureDAO) {
		this.boneMeasureDAO = boneMeasureDAO;
	}
	
	public void mouseClicked(MouseEvent e) {
	     //right mouse click event
	     if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1){
	           menu.show(CsXyPlotChartWithLineFitting.this, e.getX(), e.getY());
	     }        
    }               
	
	public void setXYData(XYDataset newXYData) {
		this.xyData = newXYData;
		JFreeChart chart = ChartFactory.createScatterPlot("Measure vs. " + "xxx",   
	            "EAAD", "Measure", newXYData); 
		chart.getXYPlot().getRenderer().setSeriesShape(0, new Rectangle(2, 2));
		//this.setChart(RegressionTest.createChart(newXYData));
		this.setChart(chart);
		this.repaint();
	}
	
	public void paintComponent(Graphics g) {
		/*
		System.out.println(xyData.getSeriesCount());
		for (int i=0; i<xyData.getSeriesCount(); i++)
			System.out.println(xyData.getSeriesKey(i));
			*/
		super.getChart().setTitle("Measure vs. EAAD w/ Line Fitting");
		super.getChart().getXYPlot().getDomainAxis().setLabel("EAAD");
		String measureLabel = xyData.getSeriesKey(0).toString(); // "Measure";
		super.getChart().getXYPlot().getRangeAxis().setLabel(measureLabel);
		super.paintComponent(g);
			
		if (this.showModel) {
			g.setFont(g.getFont().deriveFont(16f));
			g.setColor(Color.BLUE);
			//g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
			// will draw string horizontally
			TextBlock block = new TextBlock();
			/*
			block.addLine(new TextLine(str));
			//g.drawString(str, location_x, location_y);
			str = "intercept = " + decimalFormat.format(model.getCoeffs()[0]);
			block.addLine(new TextLine(str));
			//g.drawString(str, location_x + 2, location_y + 35);
			str = "slope = " + decimalFormat.format(model.getCoeffs()[1]);
			block.addLine(new TextLine(str));
			//g.drawString(str, location_x + 2, location_y + 70);
			 */
			buildBlockForModel(block);
			modelText = new TextBox(block);
			//modelText.setBackgroundPaint(new Color(0, 0, 50));
			int location_x = (int) (this.getWidth() - 20);
			//modelText.getTextBlock().calculateDimensions((Graphics2D) g).width);
			int location_y = (int) (this.getHeight() - 80);
			//modelText.getTextBlock().calculateDimensions((Graphics2D) g).height);
			//System.out.println(this.getWidth() + "x" + this.getHeight());
			//System.out.println(modelText.getHeight((Graphics2D) g));
			//System.out.println(modelText.getTextBlock().calculateDimensions((Graphics2D) g));
			modelText.draw((Graphics2D) g, location_x, location_y, RectangleAnchor.BOTTOM_RIGHT);
		}
	}


	private void buildBlockForModel(TextBlock block) {
		String[] paramNames = model.getType().getParameterNames();
		String name;
		String str = model.getType().name(); 
		block.addLine(new TextLine(str));
		
		for (int i=0; i<paramNames.length; i++) {
			name = paramNames[i];
			str = name + " = " + decimalFormat.format(model.getCoeffs()[i]);
			block.addLine(new TextLine(str));			
		}
	}

	public void setShowModel(boolean b, RegressionModel model) {
		this.showModel = b;
		this.model = model;
		coeffs = model.getCoeffs();
	}

	public XYDataset getDataset() {
		// TODO Auto-generated method stub
		return xyData;
	}

	public void updateDataWithLinearModel() {
		// TODO Auto-generated method stub
		
	}

	public void setShowModel(boolean b) {
		this.showModel = b;
	}

	public XYDataset getXYData() {
		// TODO Auto-generated method stub
		return xyData;
	}

	public BoneMeasureDAO getBoneMeasureDAO() {
		return this.boneMeasureDAO;
	}

	public void setCurMeasure(String curMeasure) {
		this.curMeasure = curMeasure;
	}
	
	public String getCurMeasure() {
		return curMeasure;
	}
}
