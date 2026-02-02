package test;

import javax.swing.JFrame;
import javax.swing.JTree;

import org.csviewer.dao.FamilyInfoDAO;
import org.csviewer.dao.PatrilTreeDAO;
import org.csviewer.gui.CSTreePanel;

public class FamilyTreeTest {

	public static void main(String[] args) {
		FamilyInfoDAO maDao = new FamilyInfoDAO();
		
		System.out.println(maDao.getFamilyInfoDF().size() + "x"
				+ maDao.getFamilyInfoDF().length());
		
		PatrilTreeDAO paDao = new PatrilTreeDAO(maDao);
		
		JTree maTree = new JTree(maDao.getMaTreeRoot());
		CSTreePanel maTreePanel = new CSTreePanel(maTree, null);
		//JTree paTree = new JTree(paDao.getPaTreeRoot());
		//CSTreePanel maTreePanel = new CSTreePanel(paTree);
		
		JFrame f = new JFrame("CSViewer V2.0");
		f.add(maTreePanel);
		f.setSize(500, 750);
		f.setVisible(true);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
