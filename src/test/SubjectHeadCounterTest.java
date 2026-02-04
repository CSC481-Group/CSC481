package test;

import org.csviewer.dao.FamilyInfoDAO;
import org.csviewer.mgr.SubjectHeadCounter;

public class SubjectHeadCounterTest {

	public static void main(String[] args) {
		//SubjectRecordMgr srMgr = new SubjectRecordMgr();
		//SubjectHeadCounter hcntr = new SubjectHeadCounter(srMgr);
		FamilyInfoDAO fInfoDao = new FamilyInfoDAO();
		SubjectHeadCounter hcntr = new SubjectHeadCounter(fInfoDao);
		System.out.println(hcntr.getTotalCount());
		
		int[][] countByYr = hcntr.calculateHeadCount(
				"01/01", 1955, 2018	// now in yyyy/MM/dd with v2.0
				//srMgr.getMinAge(), sMgr.getBeginSeaon, sMgr.getEndSeason()
				);
		System.out.println(countByYr.length);
		
		displayCountByYear(countByYr);
		
		hcntr.selectByValue("Sex", "M");
		// now in yyyy/MM/dd with v2.0
		countByYr = hcntr.calculateHeadCount("01/01", 1955, 2018);
		displayCountByYear(countByYr);
	}

	private static void displayCountByYear(int[][] countByYr) {
		for (int row=0; row<countByYr.length; row++) {
			//for (int col=0; col<2; col++) {
				System.out.println(countByYr[row][0]
						+ "\t-->\t" + countByYr[row][1]);
			//}
		}
	}

}
