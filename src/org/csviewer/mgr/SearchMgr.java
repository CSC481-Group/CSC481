package org.csviewer.mgr;

import org.csviewer.dao.FamilyInfoDAO;
import org.csviewer.mgr.SubjectHeadCounter;

import joinery.DataFrame;

public class SearchMgr {
	private FamilyInfoDAO fInfoDao;
	private SubjectHeadCounter hCounter;
	
	private String[] selectFamilies;
	
	private String cutoffDate = "07-01";	// for v2.0
	private int minAge = 0;
	private int beginSeason = 1955;
	private int endSeason = 2018;
	
	private char sex = 'B';
	
	public SearchMgr(FamilyInfoDAO fInfoDao) {
		this.fInfoDao = fInfoDao;
		
		hCounter = new SubjectHeadCounter(fInfoDao);
	}	

	// Propagate the request to Headcounter to select subjects by families 
	public void setFamiliesSelected(String[] selectFamilies) {
		this.selectFamilies = selectFamilies;
	}

	public void selectAnimalByFounderIds(String[] selectFamilies) {
		hCounter.selectByValues("FounderCode", selectFamilies);		
	}

	public int selectedSubjectCount() {
		// TODO Auto-generated method stub
		return hCounter.getTotalCount();
	}

	public DataFrame<Object> selectAnimals() {
		return hCounter.getSelectedSubjects();
	}

	// get headcount by year as requested as calculated by SubjectHeadCounter
	public int[][] getHeadcountForRange(String cutoffDate, int startYear, 
			int endYear) {
		return hCounter.calculateHeadCount(cutoffDate, startYear, endYear);
	}

	public void setCutoffDate(String dateString) {
		cutoffDate = dateString;
	}

	
	public DataFrame<Object> getSelectedAnimalSet() {
		return hCounter.getSelectedSubjects();
	}

	public String getCutoffDate() {
		return this.cutoffDate;
	}
	
	public void setMinAge(int minAge) {
		this.minAge = minAge;
	}
	
	public int getMinAge() {
		return this.minAge;
	}

	public int[][] getHeadcountForRange(String cutoffDate2, int startYear, 
			int endYear, int minAge) {
		return hCounter.calculateHeadCount(cutoffDate, startYear, 
				endYear, minAge);
	}

	public void setSelectedGender(String text) {
		sex = text.charAt(0);
		System.out.println("In setSelectedGender sex=" + sex);
		hCounter.selectByValue("Sex", ""+sex);
	}

	public void selectAllFamilies() {
		hCounter.clearAllFilters();
		selectFamilies = null;
	}

	public void setEndSeason(String text) {
		int yr = Integer.parseInt(text);
		this.endSeason = yr;
	}

	public void setBeginSeason(String text) {
		int yr = Integer.parseInt(text);
		this.beginSeason = yr;
	}	
	
	public int getBeginSeason() {
		return this.beginSeason;
	}
	
	public int getEndSeason() {
		return this.endSeason;
	}
}
