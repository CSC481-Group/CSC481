package org.csviewer.mgr;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.csviewer.dao.FamilyInfoDAO;

import joinery.DataFrame;
import joinery.DataFrame.Predicate;

public class SubjectHeadCounter {
	private FamilyInfoDAO fInfoDao;
	private DataFrame<Object> selectedSubjects;
	
	// assuming a subject with the In CS status will still be alive that day
	public final static String ASSUMED_DATE_ALIVE_FOR_IN_CS = "2019/12/31";
	private String assumedDate = ASSUMED_DATE_ALIVE_FOR_IN_CS;


	public SubjectHeadCounter(FamilyInfoDAO fInfoDao) {
		this.fInfoDao = fInfoDao;
		selectedSubjects = fInfoDao.getFamilyInfoDF();
		
		retainValidStatus();
	}

	private void retainValidStatus() {
  	  	int index = getIndexByCaption("Status", selectedSubjects.columns());
  	  	String[] invalidStatus = {"?", "aborted", "misscarria", "missing", "stillbirth"};
  	  	List<String> removeList = Arrays.asList(invalidStatus);
  	  	
		DataFrame<Object> df1 = selectedSubjects.select(new Predicate<Object>() {
	          @Override
	          public Boolean apply(List<Object> values) {
	      	      //System.out.println("In selectByValue(...), tattoo = " + values.get(0).toString());
	        	  String valueToCheck = values.get(index).toString().toLowerCase();
	              return !removeList.contains(valueToCheck);
	          }
	          
	      });
		
		selectedSubjects = df1;
	}

	// set the first filtering condition
	public void selectByValue(String caption, String value) {
  	  	int index = getIndexByCaption(caption, selectedSubjects.columns());
	    System.out.println("In selectByValue(...), index = " + index);

		DataFrame<Object> df1 = selectedSubjects.select(new Predicate<Object>() {
	          @Override
	          public Boolean apply(List<Object> values) {
	      	      //System.out.println("In selectByValue(...), tattoo = " + values.get(0).toString());
	        	  String valueToCheck = values.get(index).toString();
	              return valueToCheck.equalsIgnoreCase(value);
	          }
	          
	      });
		
		selectedSubjects = df1;
	}
	
	// get column index for a given column caption
	private int getIndexByCaption(String caption, Set<Object> headers) {
		int index = -1;
		
		int i = 0;
		
		for (Object h : headers) {
			if (h.toString().equalsIgnoreCase(caption)) {
				//System.out.println(caption + "==>" + i);
				index = i;
				break;
			}
				
			i++;
		}
		
		return index;
	}

	// add one more condition for filtering/selection
	public void addCondition(String caption, String value) {
  	  	int index = getIndexByCaption(caption, selectedSubjects.columns());
	    System.out.println("In addCondition(...), index = " + index);
	    
	    int[] count = {0};

		DataFrame<Object> df1 = selectedSubjects.select(new Predicate<Object>() {
	          @Override
	          public Boolean apply(List<Object> values) {
	      	      //System.out.print("In addCondition(...), matril = " + values.get(index).toString() + "<==>" + value);
	        	  String valueToCheck = values.get(index).toString();
	        	  if (valueToCheck.equalsIgnoreCase(value))
	        		  System.out.println(++count[0]);
	              return valueToCheck.equalsIgnoreCase(value);
	          }
	          
	      });
		
		selectedSubjects = df1;
	}

	// calculates and returns headcount array for range of year bounded by startYear and endYear
	public int[][] calculateHeadCount(String cutoffDate, int startYear, int endYear) {
		return calculateHeadCount(cutoffDate, startYear, endYear, 0);
	}

	public int[][] calculateHeadCount(String cutoffDate, int startYear, int endYear, int minAge) {
		int count;
		int years = endYear - startYear + 1;
		int[][] result = new int[years][2];
		
		for (int year = startYear; year <= endYear; year++) {
			if (minAge == 0) 
				//count = headcountOnDate(cutoffDate + "-" + year);  
				count = headcountOnDate(year + "-" + cutoffDate);  
			//count = headcountOnDateOlderThan(year + "/" + cutoffDate);
			else
				count = headcountOnDateOlderThan(year + "-" + cutoffDate, minAge);
				//count = headcountOnDateOlderThan(year + "/" + cutoffDate, minAge);
			result[year-startYear][0] = year;
			result[year-startYear][1] = count;
		}
		return result;
	}

	public int headcountOnDate(String date) {
		return headcountOnDateOlderThan(date, 0);
	}

	public int headcountOnDateOlderThan(String date, int minAge) {
		int count = 0;

		String daoDataFormat = "yyyy/MM/dd";
		SimpleDateFormat sdfFamInfoDao = new SimpleDateFormat(daoDataFormat);
	    DateTimeFormatter dtfFamInfoDao = DateTimeFormatter.ofPattern(daoDataFormat);
		//String selectDataFormat = "yyyy-MMM-dd";
		String selectDataFormat = "yyyy-MM-dd";
		SimpleDateFormat sdfSelect = new SimpleDateFormat(selectDataFormat);
	    DateTimeFormatter dtfSelect = DateTimeFormatter.ofPattern(selectDataFormat);
	    LocalDate theDate = LocalDate.parse(date, dtfSelect);

	    Object field;
	    String dob, dod, status;
	    //String[] fields;
	    LocalDate dateOfB, dateOfD;
	    
	    int indexOfDoB = getIndexByCaption("DateOfBirth", selectedSubjects.columns());
	    int indexOfDoD = getIndexByCaption("DateOfDeath", selectedSubjects.columns());
	    int indexOfDoR = getIndexByCaption("DateOfRemove", selectedSubjects.columns());
	    int indexOfStatus = getIndexByCaption("Status", selectedSubjects.columns());
	    
	    double ageOnDate; // used when
	    
	    for (int i=0; i<this.selectedSubjects.length(); i++) {
	    	status = selectedSubjects.get(i, indexOfStatus).toString();
    		//dob = selectedSubjects.get(i, indexOfDoB).toString();
    		dob = sdfFamInfoDao.format(selectedSubjects.get(i, indexOfDoB));
    		//dob = to4digityear(dob);	// pad the 2-digit year to 4-digit
    		//System.out.println("In headcount: " + dob +  "#" + i);
	    	dateOfB = LocalDate.parse(dob, dtfFamInfoDao);
	    	
	    	if (status.equalsIgnoreCase("Dead")) {
	    		field = selectedSubjects.get(i, indexOfDoD);
	    		
	    		if (field == null)
	    			continue;
	    		
	    		//dod = field.toString();
	    		dod = sdfFamInfoDao.format(field);

	    		//dod = to4digityear(dod);	// pad the 2-digit year to 4-digit
	    	}
	    	else if (status.equalsIgnoreCase("Removed")) {
	    		field = selectedSubjects.get(i, indexOfDoR);
	    		
	    		if (field == null)
	    			continue;
	    		
	    		//dod = field.toString();
	    		dod = sdfFamInfoDao.format(field);
	    		//dod = to4digityear(dod);	// pad the 2-digit year to 4-digit
				//System.out.println(count + ": " + selectedSubjects.get(i, 0) + 	//SubjID
				//		"\t" + dateOfB + "-->" + dod + " @" + status);		// details
	    	}
	    	else if (status.equalsIgnoreCase("In CS")) {
	    		dod = assumedDate;			// already in 4-digit
	    	}
	    	else
	    		continue;	// invalid status should have been excluded; skip for safety
	    	
	    	dateOfD = LocalDate.parse(dod, dtfFamInfoDao);
	    	
			//System.out.println(t + "-->" + dateOfB + " || " + theDate + "<--" + dateOfD);
	    	// for specific minAge ==> check it: younger than skip!
	    	if (minAge > 0) {
	    		ageOnDate = Period.between(dateOfB, theDate).getYears();
	    		if (ageOnDate < minAge)
	    			continue;
	    	}
	    	// check R001
	    	//if (selectedSubjects.get(i, 0).toString().equalsIgnoreCase("R001"))
	    		//System.out.println("R001: " + selectedSubjects.get(i, 0) + 	//SubjID
				//		"\t" + dateOfB + "-->" + dateOfD + " @" + theDate);	
	    	// for all or minAge = 0
	    	if (dateOfB.isBefore(theDate) && dateOfD.isAfter(theDate)) {
	    		count++;
				//System.out.println(count + ": " + selectedSubjects.get(i, 0) + 	//SubjID
				//		"\t" + dateOfB + "-->" + dateOfD + " @" + status);		// details
	    	}
	    }
	    
		return count;
	}

	/* no need for new file with unicode
	private String to4digityear(String dateWith2digitYear) {
		int yr = Integer.parseInt(dateWith2digitYear.substring(7));
		
		String s4d = dateWith2digitYear.substring(0, 7) + ((yr<20)?"20":"19") + dateWith2digitYear.substring(7);
		return s4d;
	}
	*/
	
	// get the total count for selected subjects
	public int getTotalCount() {
		return selectedSubjects.length();
	}

	public void selectByBirthSeasons(int[] birthSeasons) {
 	  	int index = getIndexByCaption("BirthSeason", selectedSubjects.columns());
	    System.out.println("In selectByBirthSeasons(...), index = " + index);

		DataFrame<Object> df1 = selectedSubjects.select(new Predicate<Object>() {
	          @Override
	          public Boolean apply(List<Object> record) {
	      	      //System.out.println("In selectByValue(...), tattoo = " + values.get(0).toString());
	        	  int valueToCheck = Integer.parseInt(record.get(index).toString());
	              return containsSeason(birthSeasons, valueToCheck);
	          }
	          
	          private boolean containsSeason(int[] list, int value) {
	        	  boolean b = false;
	        	  
	        	  for (int n : list) {
	        		  if (n == value)
	        			  return true;
	        	  }
	        	  
	        	  return b;
	          }
	      });
		
		selectedSubjects = df1;		
	}

	// defines birthSeasons by min and max values, inclusive
	public void selectByBirthSeasonRange(int beginSeason, int endSeason) {
 	  	int index = getIndexByCaption("BirthSeason", selectedSubjects.columns());
	    System.out.println("In selectByBirthSeasonRange(...), index = " + index);

		DataFrame<Object> df1 = selectedSubjects.select(new Predicate<Object>() {
	          @Override
	          public Boolean apply(List<Object> record) {
	      	      //System.out.println("In selectByValue(...), tattoo = " + values.get(0).toString());
	        	  int valueToCheck = Integer.parseInt(record.get(index).toString());
	              return valueToCheck >= beginSeason && valueToCheck <= endSeason;
	          }
	    });
		
		selectedSubjects = df1;		
	}
	
	// used to specify multiple values for one attribute 
	public void selectByValues(String caption, String[] values) {
  	  	int index = getIndexByCaption(caption, selectedSubjects.columns());
  	  	//if (index > 500) // checking for Q1050 and Q1053
	    //System.out.println("In selectByValues(...), index = " + index);

		DataFrame<Object> df1 = selectedSubjects.select(new Predicate<Object>() {
	          @Override
	          public Boolean apply(List<Object> record) {
	        	  String valueToCheck = null;
	        	  try {
		      	      System.out.println("In selectByValue(...), tattoo = " + 
		      	    		  record.get(0).toString());
		        	  valueToCheck = record.get(index).toString();
		        	  } catch (Exception e) {
	        		  System.out.println(record);
	        	  }
	              return containsIgnoreCase(values, valueToCheck);
	          }
	          
	          private boolean containsIgnoreCase(String[] list, String value) {
	        	  boolean b = false;
	        	  
	        	  for (String s : list) {
	        		  if (s.equalsIgnoreCase(value))
	        			  return true;
	        	  }
	        	  
	        	  return b;
	          }
	      });
		
		selectedSubjects = df1;
	}

	public void clearAllFilters() {
		selectedSubjects = this.fInfoDao.getFamilyInfoDF();
		
		retainValidStatus();
	}

	public DataFrame<Object> getSelectedSubjects() {
		// TODO Auto-generated method stub
		return this.selectedSubjects;
	}
}
