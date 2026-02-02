package org.csviewer.dao;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import javax.swing.tree.DefaultMutableTreeNode;

import joinery.DataFrame;

public class FamilyInfoDAO {
	private DataFrame<Object> dfFamilyInfo;
	
	private DefaultMutableTreeNode maTreeRoot = 
			new DefaultMutableTreeNode("CS Matril Trees");
	private Map<String, DefaultMutableTreeNode> unicodeNodeLookup 
			= new HashMap<String, DefaultMutableTreeNode>();
	
	LinkedList<String> founders = new LinkedList<>();
	Map<String, Integer> animalPedigree = new TreeMap<String, Integer>();	
	
	// used to show in summary panel
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	
	public FamilyInfoDAO() {
		try {
			dfFamilyInfo = DataFrame.readCsv( //"data/test1.txt");
					"data/qAnimalFamilyInfoAllInUnicode.txt");
			
			Scanner founderInput = new Scanner(
					new File("data/FounderCode.csv"));
			String[] parts;
			
			founderInput.nextLine(); // header, skip
			while (founderInput.hasNextLine()) {
				parts = founderInput.nextLine().split(",");
				founders.add(parts[1].substring(1, parts[1].length()-1));
			}
			
			for (int i=0; i<21; i++) {
				founders.add("Q20" + ((i>9)?"":"0") + i);
			}
			
			//System.out.println(founders);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		buildTree();
	}

	private void buildTree() {
		addFoundersToTree();
		
		System.out.println("In buildTree, Founder Count = " 
				+ maTreeRoot.getChildCount());
		
		addAnimals();
	}

	private void addAnimals() {
		DefaultMutableTreeNode animalNode, momNode;
		String animalUnicode, momUnicode, founderCode, sex;
		String birthGroup, status;
		Date ddd;
		String dateOfBirth, dOfDeathOrRemoval;
		double ageAtDeathOrRemoval;
		MatrilTreeInfo mMaTreeInfo, cMaTreeInfo;
		int pedigree;
		Object col2;
		
		//indexes in fiDAO
		//UniCode,FounderCode,DateOfBirth,Sex,BirthGroup,MomUnicode,
		//	0		1			2			3		4		5
		//CurrentGroup,BirthSeason,Status,DateOfDeath,DateOfRemove,
		//	6				7		8			9		10			
		//AgeOfDelivery,AgeOfDOD,AgeOfRemove,SireUniCode,DamUniCode
		//	11				12			13		14			15

		for (int i=0; i<dfFamilyInfo.length(); i++) {
			animalUnicode = dfFamilyInfo.get(i, 0).toString();
			momUnicode = dfFamilyInfo.get(i, 5).toString();
			
			ddd = (Date) dfFamilyInfo.get(i, 2);
			dateOfBirth = sdf.format(ddd);
			/*
			if (dateOfBirth.indexOf(" ") > 0)
				dateOfBirth = dateOfBirth.substring(0, 
						dateOfBirth.indexOf(" "));
			*/
			sex = dfFamilyInfo.get(i, 3).toString();
			birthGroup = dfFamilyInfo.get(i, 4).toString();
			status = dfFamilyInfo.get(i, 8).toString();
			if (status.equalsIgnoreCase("DEAD")) {
				if (dfFamilyInfo.get(i, 9) == null) {
					dOfDeathOrRemoval = "--";
					ageAtDeathOrRemoval = -2; // missing
					System.out.println("In addAnimals, dod is missing " +
							animalUnicode); 
				} else {	
					ddd = (Date) dfFamilyInfo.get(i, 9);					
					dOfDeathOrRemoval = sdf.format(ddd);
					// leave it here for now
					if (dOfDeathOrRemoval.indexOf(" ") > 0)
						dOfDeathOrRemoval = dOfDeathOrRemoval.substring(0, 
								dOfDeathOrRemoval.indexOf(" "));
					ageAtDeathOrRemoval = Double.parseDouble(
							dfFamilyInfo.get(i, 12).toString());
				}
			}
			else if (status.equalsIgnoreCase("Removed")){
				if (dfFamilyInfo.get(i, 10) == null) {
					dOfDeathOrRemoval = "--";
					ageAtDeathOrRemoval = -2; // missing
					System.out.println("In addAnimals, dor is missing " +
							animalUnicode); 
				} else {
					ddd = (Date) dfFamilyInfo.get(i, 10);					
					dOfDeathOrRemoval = sdf.format(ddd);
					// leave it here for now
					if (dOfDeathOrRemoval.indexOf(" ") > 0)
						dOfDeathOrRemoval = dOfDeathOrRemoval.substring(0, 
								dOfDeathOrRemoval.indexOf(" "));
					ageAtDeathOrRemoval = Double.parseDouble(
							dfFamilyInfo.get(i, 13).toString());
				}
			}
			else if (status.equalsIgnoreCase("In CS")){
				dOfDeathOrRemoval = "";
				ageAtDeathOrRemoval = -1;	// alive by end of 2019
			}
			else if (status.equalsIgnoreCase("Stillbirth")){
				dOfDeathOrRemoval = dateOfBirth;
				ageAtDeathOrRemoval = 0;	// alive by end of 2019
			}
			else {
				dOfDeathOrRemoval = "na";
				ageAtDeathOrRemoval = -2;	// missing
				System.out.println("In addAnimals, Status is invalid " +
						animalUnicode); 
			}
			//System.out.println("Adding animal #" + i +": " + animalUnicode 
			//		+ "(" + sex + ")" + "-->" + momUnicode);
			
			momNode = unicodeNodeLookup.get(momUnicode);

			col2 = dfFamilyInfo.get(i, 1);
			if (momUnicode.startsWith("Q2") && col2 == null) {
				founderCode = momUnicode;
				pedigree = 2;
			}
			else if (col2 != null) {
				founderCode = col2.toString();
				pedigree = 2;
			}
			else {
				mMaTreeInfo = (MatrilTreeInfo) momNode.getUserObject();
				// derive founderCode for subject #i from the mom
				founderCode = mMaTreeInfo.getMatrilCode();
				// & set back to the df where it's missing in input file
				dfFamilyInfo.set(i, 1, founderCode);
				pedigree = mMaTreeInfo.getPedigree() + 1;
			}

			//cMaTreeInfo = new MatrilTreeInfo(animalUnicode, momUnicode, 
			//		founderCode, sex);
			cMaTreeInfo = new MatrilTreeInfo(animalUnicode, momUnicode, 
					null, founderCode, sex, birthGroup, status,  
					dateOfBirth, dOfDeathOrRemoval, ageAtDeathOrRemoval);
			cMaTreeInfo.setPedigree(pedigree);
			
			animalNode = new DefaultMutableTreeNode(cMaTreeInfo);
			momNode.add(animalNode);
			cMaTreeInfo.setSiblingNo(momNode.getChildCount());
			this.unicodeNodeLookup.put(animalUnicode, animalNode);
		}
	}

	private void addFoundersToTree() {
		DefaultMutableTreeNode founderNode;
		for (String founderEntry : founders) {
			founderNode = new DefaultMutableTreeNode(founderEntry);
			maTreeRoot.add(founderNode);
			this.unicodeNodeLookup.put(founderEntry, founderNode);
			System.out.println("adding founder " + founderEntry + 
			"==>" + founderNode);
			
		}
	}

	public static void main(String[] args) {
		FamilyInfoDAO dao = new FamilyInfoDAO();
		
		System.out.println(dao.dfFamilyInfo.size() + "x"
				+ dao.dfFamilyInfo.length());
		
		Date dob = (Date) dao.dfFamilyInfo.get(10, 3);
		Date dor = (Date) dao.dfFamilyInfo.get(10, 11);
		System.out.println(dob + " --> " + dor);
		System.out.println(dao.dfFamilyInfo.get(10, 1) + ": " +
				dao.dfFamilyInfo.get(10, 3) +
				"==>" + dao.dfFamilyInfo.get(10, 11));
		/*
		JTree maTree = new JTree(dao.maTreeRoot);
		CSTreePanel maTreePanel = new CSTreePanel(maTree, null);
		
		JFrame f = new JFrame("CSViewer V2.0");
		f.add(maTreePanel);
		f.setSize(500, 750);
		f.setVisible(true);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		*/
	}

	public DataFrame<Object> getFamilyInfoDF() {
		return this.dfFamilyInfo;
	}

	public DefaultMutableTreeNode getMaTreeRoot() {
		return this.maTreeRoot;
	}

	public MatrilTreeInfo getFamilyInfoByUnicode(String unicode) {
		return (MatrilTreeInfo) unicodeNodeLookup.get(unicode).getUserObject();
	}

	public DefaultMutableTreeNode getTreeNodeById(String unicode) {
		return unicodeNodeLookup.get(unicode);
	}

}

