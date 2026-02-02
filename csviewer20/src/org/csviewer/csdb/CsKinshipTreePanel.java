package org.csviewer.csdb;

import java.awt.Color;
import java.awt.Graphics;

import org.csviewer.csdb.core.Graph;
import org.csviewer.csdb.core.GraphPanel;
import org.csviewer.csdb.core.ToolBar;

public class CsKinshipTreePanel extends GraphPanel {
	private boolean founderOnTop = true;
	private boolean showUnicode = false;
	private KinshipGraphEditor kgEd;

	public CsKinshipTreePanel(ToolBar aToolBar, KinshipGraph aGraph) {
		super(new ToolBar(aGraph), aGraph);
		kgEd = new KinshipGraphEditor();
		kgEd.setCodeChoice(showUnicode);
	}

	public void flipTree() {
		founderOnTop = !founderOnTop;
		buildTreeGraph();
	}
	
	private void buildTreeGraph() {
		String tattoo = ((KinshipGraph)super.graph).getAnimalId();
		kgEd = new KinshipGraphEditor();
		kgEd.setCodeChoice(showUnicode);
		
		//System.out.println("Flip it...");
		if (founderOnTop)
			kgEd.buildGraphforAnimalTopDown(tattoo, 3, true);
		else
			kgEd.buildGraphforAnimal(tattoo, 3, true);
		super.graph = kgEd.kinGraph;
	}

	public void toggleCode() {
		System.out.println("Toggle it...");
		//kgEd = new KinshipGraphEditor();
		showUnicode = !showUnicode;
		buildTreeGraph();
	}

   public void paintComponent(Graphics g)
   {
	   Color temp = g.getColor();
	   g.setColor(Color.WHITE);
	   g.drawRect(0,  0, 1000, 1000);
	   //g.drawRect(0,  0, WIDTH, HEIGHT);
	   g.setColor(temp);
	   super.paintComponent(g);
   }	
}
