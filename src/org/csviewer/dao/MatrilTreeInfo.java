package org.csviewer.dao;

public class MatrilTreeInfo {
	String unicode, momUnicode, dadUnicode, matrilCode, sex;
	String birthGroup, status;
	String dateOfBirth, dOfDeathOrRemoval; // not as Date
	double ageAtDeathOrRemoval;
	int pedigree, siblingNo;
	
	public MatrilTreeInfo(String unicode, String momUnicode, 
			String dadUnicode, String founderCode, String sex,
			String birthGroup, String status, String dateOfBirth, 
			String dOfDeathOrRemoval, double ageAtDeathOrRemoval) {
		this.unicode = unicode;
		this.momUnicode = momUnicode;
		this.dadUnicode = dadUnicode;
		this.matrilCode = founderCode;
		this.sex = sex;
		this.birthGroup = birthGroup;
		this.status = status;
		this.dateOfBirth = dateOfBirth;
		this.dOfDeathOrRemoval = dOfDeathOrRemoval;
		this.ageAtDeathOrRemoval = ageAtDeathOrRemoval;
	}

	public MatrilTreeInfo(String unicode, String momUnicode, 
			String dadUnicode, String founderCode, String sex) {
		this.unicode = unicode;
		this.momUnicode = momUnicode;
		this.dadUnicode = dadUnicode;
		this.matrilCode = founderCode;
		this.sex = sex;
	}

	public MatrilTreeInfo(String animalUnicode, String momUnicode, 
			String founderCode, String sex) {
		this(animalUnicode, momUnicode, null, founderCode, sex);
	}

	public String getMatrilCode() {
		return matrilCode;
	}

	public int getPedigree() {
		return pedigree;
	}

	public void setPedigree(int i) {
		pedigree = i;
	}

	public String getUnicode() {
		return unicode;
	}

	public String getSex() {
		return sex;
	}

	public String getMomUnicode() {
		return momUnicode;
	}
	
	public void setSiblingNo(int i) {
		siblingNo = i;
	}

	public void setDadUnicode(String unicode) {
		this.dadUnicode = unicode;
	}

	public String getDadUnicode() {
		return dadUnicode;
	}

	public String getDoB() {
		return this.dateOfBirth;
	}

	public String getBirthGroup() {
		return this.birthGroup;
	}

	public int getSiblingNo() {
		return this.siblingNo;
	}

	public String getStatus() {
		return this.status;
	}

	public String getDateOfDoR() {
		return this.dOfDeathOrRemoval;
	}

	public double getAgeAtDoR() {
		return this.ageAtDeathOrRemoval;
	}
}