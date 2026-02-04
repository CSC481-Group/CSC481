package org.csviewer.gui.analytics;

import javax.swing.*;

import org.csviewer.mgr.analytics.RegressionModel;
import org.csviewer.mgr.analytics.RegressionType;
import org.csviewer.mgr.analytics.RegressionUtil;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import java.awt.Component;
import java.awt.event.*;

public class LineFittingMenu extends JPopupMenu {
	private CsXyPlotChartWithLineFitting hostContainer;
	private String[] modelItems = {"Linear", "Log", "Stepwise", " w/ Model"};
	private String[] featureItems = {"Female", "Male"}; //, "Matril"};
	
	public LineFittingMenu(CsXyPlotChartWithLineFitting hostContainer) {
		super("Types:"); 
		
		this.add("Select Model:");
		this.add(new JPopupMenu.Separator());

		this.hostContainer = hostContainer;
		JMenuItem jmItem; 
		for (String caption : modelItems) {
			jmItem = new JMenuItem(caption);
			this.add(jmItem); 
			jmItem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println(caption);
					if (caption.equals("Linear")) {
						//CsXyPlotChartWithLineFitting pLine = 
						//(CsXyPlotChartWithLineFitting)LineFittingMenu.this.hostContainer;
						//pLine.setXYData(RegressionTest.createDataset());
						//RegressionUtil.createLinearModelForChart(pLine);
						//pLine.updateDataWithLinearModel();
						//pLine.repaint();
						RegressionUtil.createLinearModelForChart(hostContainer);
						hostContainer.repaint();
					}
					else if (caption.equals("Log")) {
						RegressionUtil.createLogModelForChart(hostContainer);
						hostContainer.repaint();
					}
					else if (caption.equals("Stepwise")) {
						RegressionUtil.createStepwiseModelForChart(hostContainer);
						hostContainer.repaint();
					}
					else if (caption.equals(" w/ Model")) {
						//CsXyPlotChartWithLineFitting pLine = 
						//(CsXyPlotChartWithLineFitting)LineFittingMenu.this.hostContainer;
						//pLine.setShowModel(true);
						//pLine.repaint();
						hostContainer.setShowModel(true);
						hostContainer.repaint();
					}
				}
				
			});
		}
		this.add(new JPopupMenu.Separator());
		this.add("Select Feature:");
		this.add(new JPopupMenu.Separator());
		for (String caption : featureItems) {
			jmItem = new JMenuItem(caption);
			this.add(jmItem); 
			jmItem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println(caption);
					if (caption.equals("Female")) {
						//hostContainer.setXYData(RegressionUtil.getXYDataByGender(null, caption));
						hostContainer.setXYData(
								RegressionUtil.getXYDataByGender(
										//hostContainer.getXYData().getSeriesKey(0),
										hostContainer.getCurMeasure(),
										caption));
						hostContainer.repaint();
					}
					else if (caption.equals("Male")) {
						hostContainer.setXYData(
								RegressionUtil.getXYDataByGender(
										//hostContainer.getXYData().getSeriesKey(0),
										hostContainer.getCurMeasure(),
										caption));
						hostContainer.repaint();
					}
					/*
					else if (caption.equals("Matril")) {
						hostContainer.setXYData(
								RegressionUtil.getXYDataByMatril(
										hostContainer.getXYData().getSeriesKey(0),
										caption));
						hostContainer.repaint();
					}
					*/
				}
				
			});
		}
		
		JMenu matrilSubmenu = new JMenu("Matril");
		loadMatrilItems(matrilSubmenu);
		this.add(matrilSubmenu);
	 }

	private void loadMatrilItems(JMenu matrilSubmenu) {
		String[] items = {"MCode", "Count",
				"NONE", "252",
				"Q1083", "142",
				"Q1066", "132",
				"Q1056", "127",
				"Q1053", "124",
				"Q1012", "101",
				"Q1019", "81",
				"Q1029", "53",
				"Q1071", "49",
				"Q1021", "40",
				"Q1046", "38",
				"Q1077", "29",
				"Q1000", "28",
				"Q1048", "20",
				"Q1022", "15",
				"Q1065", "13",
				"Q1027", "7",
				"Q1059", "7",
				"Q1094", "4",
				"Q1054", "1",
				"Q1055", "1",
				"Q1033", "1"};
		ActionListener matrilSelector = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String mcode = e.getActionCommand().split(" ")[0];
				System.out.println("In matrilSelector, mcode = " + mcode);
				hostContainer.setXYData(
						RegressionUtil.getXYDataByMatril(
								//hostContainer.getXYData().getSeriesKey(0),
								hostContainer.getCurMeasure(),
								mcode));
				hostContainer.repaint();				
			}
			
		};
		String caption;
		JMenuItem jmItem;
		for (int i=2; i<items.length/2-1; i+=2) {
			caption = items[i] + " (" + items[i+1] +")";
			jmItem = new JMenuItem(caption);
			matrilSubmenu.add(jmItem); 
			jmItem.addActionListener(matrilSelector);
		}
		
	}
	 
	/*
	 public static void main(String args[]) {
		 JFrame frame = new JFrame("Line Fitting Test");
		 ChartPanel cp = new ChartPanel(RegressionTest.createTestChart());
		 LineFittingMenu menu = new LineFittingMenu(cp);
		 
		   frame.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent e) {
			     //right mouse click event
			     if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1){
			           menu.show(frame , e.getX(), e.getY());
			     }        
		    }               
		   });
		   frame.add(menu); 
		   frame.setSize(300,300);
		   frame.setLayout(null);
		   frame.setVisible(true);
	 }
	 */
}