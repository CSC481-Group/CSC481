package org.csviewer.dao;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

import joinery.DataFrame;

public class PatrilTreeDAO {
	private DataFrame<Object> dfPatrilTreeInfo;
	
	LinkedList<String> chronicalSireSet = new LinkedList<String>();
   	Map<String, DefaultMutableTreeNode> unicodeToNodeLookup = 
   			new HashMap<String, DefaultMutableTreeNode>(); 
   	
   	FamilyInfoDAO maTreeDAO;

	private DefaultMutableTreeNode paTreeRoot = 
			new DefaultMutableTreeNode("CS Patril Trees");

	public PatrilTreeDAO(FamilyInfoDAO maTreeDAO) {
		this.maTreeDAO = maTreeDAO;
		
		try {
			dfPatrilTreeInfo = DataFrame.readCsv(
					"data/qAnimalsSortedBySireUnicode.txt");

			System.out.println(dfPatrilTreeInfo.length() + "x" +
					dfPatrilTreeInfo.size());			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		buildTree();
		
		System.out.println(paTreeRoot.getDepth());	
	}

	private void buildTree() {
		DefaultMutableTreeNode chdNode, dadNode;
		String chdUnicode, dadUnicode;
		MatrilTreeInfo dadMaTreeInfo, chdMaTreeInfo;
		int dadCount = 0;

		for (int i=0; i<dfPatrilTreeInfo.length(); i++) {
			dadUnicode = dfPatrilTreeInfo.get(i, 1).toString();
			// to create dadNode when dadUnicode is not in the list
			if (unicodeToNodeLookup.get(dadUnicode) == null) {
				dadCount++;
				chronicalSireSet.add(dadUnicode);
				dadMaTreeInfo = maTreeDAO.getFamilyInfoByUnicode(dadUnicode);
				dadNode = new DefaultMutableTreeNode(dadMaTreeInfo);
				unicodeToNodeLookup.put(dadUnicode, dadNode);
				System.out.println("Adding new dad (" + dadCount + 
						") for " + dadUnicode);
				if (dadMaTreeInfo.getDadUnicode() == null)
					paTreeRoot.add(dadNode);
				
			}
			else {	// adding subsequent children to the same dadNode
				dadNode = this.unicodeToNodeLookup.get(dadUnicode);	
			}
			
			chdUnicode = dfPatrilTreeInfo.get(i, 2).toString();
			chdMaTreeInfo = maTreeDAO.getFamilyInfoByUnicode(chdUnicode);
			chdMaTreeInfo.setDadUnicode(dadUnicode);
			chdNode = new DefaultMutableTreeNode(chdMaTreeInfo);
			dadNode.add(chdNode);
			unicodeToNodeLookup.put(chdUnicode, chdNode);
			/*
			System.out.println("::Adding animal #" + i +": " + chdUnicode 
					+ "[" + chdMaTreeInfo.getSex() + "]" + "-->" + dadUnicode
					+ "x" +dadNode.getChildCount() + "==" +
					((MatrilTreeInfo)dadNode.getUserObject()).unicode);
			*/
		}
	}

	public DefaultMutableTreeNode getPaTreeRoot() {
		return paTreeRoot;
	}

	public DefaultMutableTreeNode getTreeNodeById(String unicode) {
		return this.unicodeToNodeLookup.get(unicode);
	}
}
