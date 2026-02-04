package org.csviewer.csdb;

import java.awt.Color;

import org.csviewer.csdb.core.CircleNode;
import org.csviewer.csdb.core.Edge;
import org.csviewer.csdb.core.Graph;
import org.csviewer.csdb.core.LineEdge;
import org.csviewer.csdb.core.Node;

public class KinshipGraph extends Graph {
	private String animalId;
	
	   public Node[] getNodePrototypes()
	   {
	      Node[] nodeTypes =
	         {
		        new AnimalNode("Tattoo"),
		        new DyadNode("Dam", "Sire")
		        /*
	            new IconNode("image/AnimalButtonIcon.png"),
	            new IconNode("image/DyadButtonIcon.png"),
	            new CircleNode(Color.WHITE)
	            */
	         };
	      return nodeTypes;
	   }

	   public Edge[] getEdgePrototypes()
	   {
	      Edge[] edgeTypes =
	         {
 	            new LineEdge(),
	            new FounderQuickLink()
	         };
	      return edgeTypes;
	   }

	public String getAnimalId() {
		// TODO Auto-generated method stub
		return this.animalId;
	}

	public void setAnimalId(String tattoo) {
		this.animalId = tattoo;
	}
}
