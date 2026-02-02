package org.csviewer.dao;

import java.io.IOException;
import java.util.List;

import joinery.DataFrame;
import joinery.DataFrame.Predicate;

public class BoneMeasureDAO {
	private DataFrame<Object> dfBoneMeasureTable;
	
	private String[] selectedKeys;

	public BoneMeasureDAO() {
		try {
			dfBoneMeasureTable = DataFrame.readCsv(
					"data/qMeasureWithUnicode.csv");

			System.out.println(dfBoneMeasureTable.length() + "x" +
					dfBoneMeasureTable.size());			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		BoneMeasureDAO dao = new BoneMeasureDAO();
		
		boolean b = dao.hasMeasureForUnicode("E-590");
		System.out.println(b); // false
		
		//b = dao.hasMeasureForTattoo("G-100");
		//System.out.println(b); // true
		//if (b) 
			System.out.println(dao.getMeasureForUnicode("G-100"));
	}

	public boolean hasMeasureForUnicode(String unicode) {
		//System.out.println("In bmDAO, checking measure for " + unicode);
		DataFrame<Object> df = dfBoneMeasureTable.select(
				new Predicate<Object>() {
			@Override
			public Boolean apply(List<Object> values) {
				String aUnicode = values.get(2).toString();
				//System.out.println("..checking for -->" + aUnicode);
				return aUnicode.equalsIgnoreCase(unicode);
			}
		});

		return df.length() == 1;
	}

	public DataFrame<Object> getMeasureForUnicode(String unicode) {
		DataFrame<Object> df = dfBoneMeasureTable.select(
				new Predicate<Object>() {
			@Override
			public Boolean apply(List<Object> values) {
				String aUnicode = values.get(2).toString();
				return aUnicode.equalsIgnoreCase(unicode);
			}
		});

		return df;
	}

	public DataFrame<Object> getMcodeForUnicode(String unicode) {
		DataFrame<Object> df = dfBoneMeasureTable.select(
				new Predicate<Object>() {
			@Override
			public Boolean apply(List<Object> values) {
				String aUnicode = values.get(2).toString();
				return aUnicode.equalsIgnoreCase(unicode);
			}
		});

		return df;
	}

	public DataFrame<Object> getMeasureWithEAAD(String[] measureKeys) {
		DataFrame<Object> df = this.dfBoneMeasureTable.retain("Unicode", "EAAD");
		for (String k : measureKeys)
			df.add(k, this.dfBoneMeasureTable.col(k));
		
		return df;
	}

	public DataFrame<Object> getMeasureWithEAAD(String measureKey) {
		DataFrame<Object> df = this.dfBoneMeasureTable.retain("Unicode", "EAAD");
		df.add(measureKey, this.dfBoneMeasureTable.col(measureKey));
		
		return df;
	}

	public void setSelectedKeys(String[] array) {
		this.selectedKeys = array;
	}

	public String[] getSelectedKeys() {
		return this.selectedKeys;
	}

	public DataFrame<Object> selectMeasures() {
		return this.getMeasureWithEAAD(selectedKeys);
	}

	public String getGenderByUnicode(String unicode) {
		Predicate<Object> p = new Predicate<Object>() {
			
	          @Override
	          public Boolean apply(List<Object> values) {
	        	  String theUnicode = values.get(2).toString();
	              return theUnicode.equalsIgnoreCase(unicode);
	          }
		};
		
		return dfBoneMeasureTable.select(p).col("Sex").get(0).toString();
	}

	public String getMcodeByUnicode(String unicode) {
		System.out.println("In RegressionUtil: NOT implemented for " + unicode); 
		return null;
	}
}
