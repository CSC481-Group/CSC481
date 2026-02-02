package org.csviewer.gui.search;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormatSymbols;
import java.time.YearMonth;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.csviewer.gui.search.SeasonSelectionPanel;
import org.csviewer.mgr.SearchMgr;

public class SeasonSelectionPanel extends JPanel {
	private JList<String> seasonList;
	private JTextField beginSeason;
	private JTextField endSeason;
	//private ButtonGroup genderGroup;
	//private ButtonGroup rangeGroup;

	private JRadioButton /*jrbBefore, jrbAfter,*/ jrbFemale, jrbMale;
	private Dimension defaultSize = new Dimension(450, 510);
	
	private  TitledBorder blockBorder;
	private JRadioButton jrbBoth;
	//private JRadioButton jrbNA;
	
	private JComboBox<String> months;
	private JComboBox<Integer> days;
	private JTextField jtfMinAge;
	
	private SearchMgr sMgr;
	//private JLabel paddingLabel = new JLabel("           ");

	public SeasonSelectionPanel(SearchMgr sMgr) {
		this.sMgr = sMgr;
		this.setMinimumSize(defaultSize);
		this.setMaximumSize(defaultSize);
		this.setPreferredSize(defaultSize);
		this.setLayout(new GridLayout(1, 2));
		loadSeasons();
		initGui();
	}

	private void initGui() {
		JScrollPane sp = new JScrollPane(seasonList);
		//sp.setBounds(25, 25, 25, 25);
		this.add(sp);
		JPanel selPanel = new JPanel();
		loadSelPanel(selPanel);
	    this.add(selPanel); 
	}

	private void loadSelPanel(JPanel selPanel) {
		selPanel.setLayout(new GridLayout(6, 1));
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		blockBorder = BorderFactory.createTitledBorder(loweredbevel, "Beginning Season:");
		
		// adding the Beginning Season block
		JPanel seasonBlock = new JPanel();
		beginSeason = new JTextField(6);
		beginSeason.setText("1955"); // default value for beginning season
		beginSeason.setEditable(false);
		beginSeason.setHorizontalAlignment(JTextField.CENTER);
		seasonBlock.add(beginSeason);	
		
		seasonBlock.add(new JLabel("                                         "));
		JCheckBox jcbSetBeginSeason = new JCheckBox();
		jcbSetBeginSeason.addActionListener(evt-> {
			if (jcbSetBeginSeason.isSelected()) {
				beginSeason.setEditable(true); 
			}
			else{
				beginSeason.setEditable(false);
			}
		});
		seasonBlock.add(jcbSetBeginSeason);
		
		seasonBlock.add(new JLabel("Set Beginning Season"));
		seasonBlock.setBorder(blockBorder);
		//temp.setSize(beginSeason.getPreferredSize());
		selPanel.add(seasonBlock);

		// adding the Ending Season block
		blockBorder = BorderFactory.createTitledBorder(loweredbevel, "Ending Season:");
		seasonBlock = new JPanel();
		endSeason = new JTextField(6);
		endSeason.setText("2018"); // default value for ending season
		endSeason.setEditable(false);
		endSeason.setHorizontalAlignment(JTextField.CENTER);
		seasonBlock.add(endSeason);
		
		seasonBlock.add(new JLabel("                                       "));
		JCheckBox jcbSetEndSeason = new JCheckBox();
		jcbSetEndSeason.addActionListener(evt-> {
			if (jcbSetEndSeason.isSelected()) {
				endSeason.setEditable(true); 
			}
			else{
				endSeason.setEditable(false);
			}
		});
		seasonBlock.add(jcbSetEndSeason);
		seasonBlock.add(new JLabel("Set Ending Season"));
		seasonBlock.setBorder(blockBorder);
		selPanel.add(seasonBlock);

		JPanel monthDaySelector = createMonthDaySelectorP();
		selPanel.add(monthDaySelector);
		
		JPanel jpnlSetMinAge = createMinAgeP();
		selPanel.add(jpnlSetMinAge);
		
		JPanel genderPanel = createGenderP();
		selPanel.add(genderPanel);

		JPanel confirmPanel = createConfirmP();
		selPanel.add(confirmPanel);
	}

	private JPanel createMonthDaySelectorP() {
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		blockBorder = BorderFactory.createTitledBorder(loweredbevel, "Cutoff Birth Date");
		JPanel p = new JPanel();
		p.setBorder( blockBorder );
		p.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		// get a list of full-names for months 
		DateFormatSymbols dfs = new DateFormatSymbols();	 
        //String[] monthNames = dfs.getShortMonths();
        String[] monthNames = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        
        int year = 2025;	// a normal year with 28 days in Feb
        int month = 12; 	// Dec
        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();

        // set default cutoff date as December 31
		/*JComboBox<String> */months = new JComboBox<String>(monthNames);
		months.setSelectedIndex(11);
		Integer[] dayOptions = new Integer[2];
		dayOptions[0] = 1;
		dayOptions[1] = daysInMonth;
		months.setEnabled(false);
		/*JComboBox<Integer> */days = new JComboBox<Integer>(dayOptions);
		days.setSelectedIndex(1);
		days.setEnabled(false);

		months.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				days.removeItemAt(1);
				int month = months.getSelectedIndex() + 1;
				YearMonth yearMonth = YearMonth.of(year, month);
		        int daysInMonth = yearMonth.lengthOfMonth();
				days.addItem(daysInMonth);
				//int cutoffMonth = months.getSelectedIndex()+1;
				//int cutoffDay = days.getSelectedIndex()+1;
				String cutoffMonth = months.getSelectedItem().toString();
				String cutoffDay = days.getSelectedItem().toString();
				if (cutoffDay.length() == 1)
					cutoffDay = "0" + cutoffDay;
				//sMgr.setCutoffDate(cutoffDay+"-"+cutoffMonth);
		        System.out.println("Cutoff Birth Date: set to " + cutoffDay+"-"+cutoffMonth);
			}
			
		});
		
		days.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//int cutoffMonth = months.getSelectedIndex()+1;
				//int cutoffDay = days.getSelectedIndex()+1;
				String cutoffMonth = months.getSelectedItem().toString();
				String cutoffDay = days.getSelectedItem().toString();
				if (cutoffDay.length() == 1)
					cutoffDay = "0" + cutoffDay;
				//sMgr.setCutoffDate(cutoffDay+"-"+cutoffMonth);
		        System.out.println("Cutoff Birth Date: set to " + cutoffDay+"-"+cutoffMonth);
			}
		});
		p.add(months);
		p.add(days);
		p.add(new JLabel("                  	 		  	 "));
		JCheckBox jcbSetCutoffDate = new JCheckBox();
		jcbSetCutoffDate.addActionListener(evt-> {
			if (jcbSetCutoffDate.isSelected()) {
				months.setEnabled(true); 
				days.setEnabled(true);
			}
			else{
				months.setEnabled(false); 
				days.setEnabled(false);
			}
		});
		p.add(jcbSetCutoffDate);
		
		p.add(new JLabel("Set Cutoff Birth Date"));
        return p;
	}

	private JPanel createConfirmP() {
		JPanel p = new JPanel();
		p.add(new JLabel("                                               "));
		
		JButton jbtnConfirm = new JButton("Confirm");
		
		// for now, just handle minAge
		// cutoffDate is currently handled at the actionlistener logic
		// to change to handle all selections
		jbtnConfirm.addActionListener(evt -> {
			int minAge = Integer.parseInt(jtfMinAge.getText());
			sMgr.setMinAge(minAge);
			
			String cutoffMonth = months.getSelectedItem().toString();
			String cutoffDay = days.getSelectedItem().toString();
			if (cutoffDay.length() == 1)
				cutoffDay = "0" + cutoffDay;
			sMgr.setCutoffDate(cutoffMonth+"-"+cutoffDay);
			System.out.println("Cutoff Birth Date: confirm " + 
					cutoffMonth+"-"+cutoffDay);
			
			sMgr.setEndSeason(this.endSeason.getText());
			sMgr.setBeginSeason(this.beginSeason.getText());
		});
		p.add(jbtnConfirm);
		JButton jbtnReset = new JButton("Reset");
		p.add(jbtnReset);
		
		return p;
	}

	private JPanel createGenderP() {
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		blockBorder = BorderFactory.createTitledBorder(loweredbevel, "Select Sex:");
		JPanel p = new JPanel();
		p.setBorder(blockBorder);
		blockBorder.setTitleJustification(TitledBorder.LEFT);
		//blockBorder.setTitlePosition(TitledBorder.ABOVE_TOP);
		p.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.jrbBoth = new JRadioButton("Both");
		this.jrbFemale = new JRadioButton("Female");
		this.jrbMale = new JRadioButton("Male");
		
		ActionListener radioListener = new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            JRadioButton selected = (JRadioButton) e.getSource();
	            sMgr.setSelectedGender( selected.getText() );
	        }
	    };
	    jrbBoth.addActionListener(radioListener);
	    jrbFemale.addActionListener(radioListener);
	    jrbMale.addActionListener(radioListener);
	    
		this.jrbBoth.setBounds(30, 20, 60, 12);
		this.jrbFemale.setBounds(30, 50, 60, 12);
		this.jrbMale.setBounds(30, 80, 60, 12);
		ButtonGroup bg = new ButtonGroup();   
		bg.add(jrbBoth);
		bg.add(jrbFemale);
		bg.add(jrbMale);
		jrbBoth.setSelected(true);
		p.add(jrbBoth);
		p.add(jrbFemale);
		p.add(jrbMale);
		//p.add(new JLabel("Female Male"));
		return p;
	}

	private JPanel createMinAgeP() {
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		blockBorder = BorderFactory.createTitledBorder(loweredbevel, "Set Minimal Age:");
		JPanel p = new JPanel();
		p.setBorder(blockBorder);
		blockBorder.setTitleJustification(TitledBorder.LEFT);
		
		jtfMinAge = new JTextField("0", 3);
		jtfMinAge.setHorizontalAlignment(JTextField.RIGHT);
		jtfMinAge.setEditable(false);
		p.add(jtfMinAge);
		p.add(new JLabel("                                                "));
		
		JCheckBox jcbSetMinAge = new JCheckBox();
		jcbSetMinAge.addActionListener(evt -> {
			if (jcbSetMinAge.isSelected()) 
				jtfMinAge.setEditable(true);
			else
				jtfMinAge.setEditable(false);
		});
		p.add(jcbSetMinAge);
		
		p.add(new JLabel("Set Minimal Age"));
		return p;
	}

	private void loadSeasons() {
		ArrayList<String> allSeasons = new ArrayList<String>();
		for (int yr = 1955; yr < 2020; yr++) {
			allSeasons.add("" + yr);
		}		
		seasonList = new JList<>(
				allSeasons.toArray(new String[allSeasons.size()]));
	}


	public static void main(String args[]) {
		JFrame f = new JFrame("Season Selection Test");
		SeasonSelectionPanel p = new SeasonSelectionPanel(null);
		f.add(p);
		f.pack();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

}
