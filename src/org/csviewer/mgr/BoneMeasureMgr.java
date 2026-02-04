package org.csviewer.mgr;

import org.csviewer.dao.BoneMeasureDAO;

import joinery.DataFrame;
//import mgr.measure.MeasureDataOrganizer;

public class BoneMeasureMgr {
	//private MeasureDataOrganizer mdOrg;
	private BoneMeasureDAO boneMeasureDAO;
	
	//private String[] selectedKeys;
	
	public BoneMeasureMgr() {
		//mdOrg = new MeasureDataOrganizer();
		boneMeasureDAO = new BoneMeasureDAO();
	}
	
	public BoneMeasureMgr(BoneMeasureDAO boneMeasureDAO) {
		//mdOrg = new MeasureDataOrganizer();
		this.boneMeasureDAO = boneMeasureDAO;
	}
	
	public DataFrame<Object> selectMeasures() {
		String[] selectedKeys = boneMeasureDAO.getSelectedKeys();
		return boneMeasureDAO.getMeasureWithEAAD(selectedKeys);
	}
	
	public String[] getSelectedKeys() {
		String[] selectedKeys = boneMeasureDAO.getSelectedKeys();
		return selectedKeys;
	}
	
	public void setSelectedKeys(String[] selectedKeys) {
		//this.selectedKeys = selectedKeys;
		boneMeasureDAO.setSelectedKeys(selectedKeys);
	}
	
	public DataFrame<Object> getMeasureByTattoo(String unicode) {
		return boneMeasureDAO.getMeasureForUnicode(unicode);
	}
	
	public boolean hasMeasureForUnicode(String unicode) {
		return boneMeasureDAO.getMeasureForUnicode(unicode).length() == 1;
	}

}
