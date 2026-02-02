package org.csviewer.gui;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import org.csviewer.dao.FamilyInfoDAO;
import org.csviewer.mgr.SubjectHeadCounter;
import org.csviewer.mgr.SubjectRecordMgr;

import joinery.DataFrame;

public class DataTableViewPanel extends JPanel {
	private JPanel paramPanel;
	private JPanel buttonPanel;
	private DataFrame<Object> data;
	private DefaultTableModel model = new DefaultTableModel();
	private JTable table = new JTable();
	
	private FamilyInfoDAO fInfoDao;

	public DataTableViewPanel(FamilyInfoDAO fInfoDao) {
		this.fInfoDao = fInfoDao;
		
		paramPanel = new JPanel();
		paramPanel.setPreferredSize(new Dimension(200, 280));
		// use form layout from Horstmann's text
		//paramPanel.setLayout(new FormLayout()); 
		initParamPanel();
		
		buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(200, 150));
		initButtonPanel();
		
		// the panel on the left to hold param and button panels
		JPanel jpnlParamAndButtons = new JPanel();
		jpnlParamAndButtons.setPreferredSize(new Dimension(230, 450));
		jpnlParamAndButtons.add(paramPanel);
		jpnlParamAndButtons.add(buttonPanel);
		
		this.add(jpnlParamAndButtons);
		
		data = prepareHeadcount();
		fillOrUpdateJoinedTable();
		
		System.out.println(model.getRowCount() + "x" + 
				model.getColumnCount());
		table.setModel(model);
		System.out.println("In TableView constructor, table dim is: "
				+ table.getPreferredSize());

		this.add(new JScrollPane(table));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}

	private DataFrame<Object> prepareHeadcount() {
		DataFrame<Object> hdCnt = new DataFrame<Object>();
		SubjectRecordMgr sMgr = new SubjectRecordMgr();		
		//DataFrame<Object> df = sMgr.getSriDataframe();		
		//System.out.println(df.length() + "x" + df.size());

		SubjectHeadCounter cntr = new SubjectHeadCounter(fInfoDao);
		//String cutoffDate = "07/01";
		String cutoffDate = "Jul-01";	// should get from the same counter
		int startYear=1955, endYear = 2018;
		int[][] headcountByYear = cntr.calculateHeadCount(cutoffDate, 
				startYear, endYear);
		
		int[][] hcntTransposed = transpose(headcountByYear);
		System.out.println("Number of years = " + hcntTransposed[0].length);

		//hdCnt.add("Year", Arrays.asList(hcntTransposed[0]));
		//hdCnt.add("Count", Arrays.asList(hcntTransposed[1]));
		hdCnt.add("Year", asList(hcntTransposed[0]));
		hdCnt.add("Count", asList(hcntTransposed[1]));
		
		System.out.println("In prepCount " +
				hdCnt.size() + "x" + hdCnt.length());
		
		return hdCnt;
	}
	

	private List<Object> asList(int[] array) {
		List<Object> list = new ArrayList<>();
		
		for (int i=0; i<array.length; i++) {
			list.add(array[i]);
		}
		
		System.out.println("In asList " + list.size());
		return list;
	}

	private int[][] transpose(int[][] headcountByYear) {
		int[][] matrix = new int[2][headcountByYear.length];
		
		for (int i = 0; i < headcountByYear.length; i++) {
			matrix[0][i] = headcountByYear[i][0];
			matrix[1][i] = headcountByYear[i][1];
		}
		
		System.out.println("In transpose " +
				matrix.length + "x" + matrix[0].length);
		
		return matrix;
	}

	private void initButtonPanel() {
		JButton jbtnSave = new JButton("Save");
		buttonPanel.add(jbtnSave);
		buttonPanel.setBorder(
				BorderFactory.createTitledBorder("Take Actions:"));
	}

	private void initParamPanel() {
		// a placeholder for now
		JLabel datasetParamLabel = new JLabel("All families selected");
		paramPanel.add(datasetParamLabel);
		paramPanel.setBorder(
				BorderFactory.createTitledBorder("Selection Parameters"));
	}
	
	public void fillOrUpdateJoinedTable( DataFrame<Object> newData ) {
		data = newData;
		fillOrUpdateJoinedTable();
	}
	
	private void fillOrUpdateJoinedTable() {
		model.setRowCount(0);
		model.setColumnCount(0);
		Set<Object> columns = data.columns();
		
		System.out.println("In fillTable columns.size() ==> " +
				columns.size());

		ArrayList<String> columnList = new ArrayList<String>();
		for( Object column : columns ) {
			model.addColumn( column.toString() );
			columnList.add( column.toString() );
		}
		String[] tableColumns = columnList.toArray(new String[0]);
		String[][] tableValues = new String[data.length()][columns.size()];
		for( int x = 0; x < data.length(); x++ ) {
			for( int y = 0; y < columns.size(); y++ ) {
				Object rawData = data.get(x, y);
				if( rawData == null )
					tableValues[x][y] = "NULL";
				else
					tableValues[x][y] = rawData.toString();
			}
		}
		
		for( int x = 0; x < tableValues.length; x++ )
			model.insertRow( model.getRowCount(), tableValues[x] );
		
		System.out.println("In fillTable " +
				model.getRowCount() + "x" + 
				model.getColumnCount());
		
		JTableHeader header = table.getTableHeader();
		FontMetrics headerMetrics = table.getTableHeader().
				getFontMetrics(header.getFont());
		setFont(header.getFont());
		for( int x = 0; x < tableColumns.length; x++ ) {
			int maxColWidth = headerMetrics.stringWidth( tableColumns[x] );
			for( int y = 0; y < tableValues.length; y++ ) {
				// the split makes everything after the first 
				// parentheses not be considered for spacing
				// since the stuff in parentheses are notes 
				// ( they are not excluded from the CSV file, 
				// the spacing will just have them cutoff in most cases
				int valueWidth = headerMetrics.stringWidth( 
						(tableValues[y][x]).split("\\(")[0] );
				maxColWidth = maxColWidth < valueWidth ? 
							  valueWidth : 
							  maxColWidth;
			}
			//table.getColumnModel().getColumn(x).setPreferredWidth( 
			//		(int) (maxColWidth*1.3) );
		}
	}

}