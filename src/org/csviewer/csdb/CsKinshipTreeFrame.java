package org.csviewer.csdb;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.csviewer.csdb.core.Edge;
import org.csviewer.csdb.core.Graph;
import org.csviewer.csdb.core.GraphFrame;
import org.csviewer.csdb.core.GraphPanel;
import org.csviewer.csdb.core.Node;
import org.csviewer.csdb.core.ToolBar;
import org.csviewer.main.CsvV2MainWindow;
import org.csviewer.main.util.PanelImageUtil;

public class CsKinshipTreeFrame extends GraphFrame {

	public CsKinshipTreeFrame(KinshipGraph graph) {
		super(graph);
		super.setIconImage(CsvV2MainWindow.CS_VIEWER_LOGO);
		// TODO Auto-generated constructor stub
	}
	
	   protected void constructFrameComponents()
	   {
	      toolBar = new ToolBar(graph);
	      panel = new CsKinshipTreePanel(toolBar, (KinshipGraph) graph);
	      addFrameComponents();
	   }
	
	public void constructMenus() {
	      JMenuBar menuBar = new JMenuBar();
	      setJMenuBar(menuBar);
	      JMenu fileMenu = new JMenu("File");
	      menuBar.add(fileMenu);

	      JMenuItem openItem = new JMenuItem("Open");
	      openItem.addActionListener(new
	         ActionListener()
	         {
	            public void actionPerformed(ActionEvent event)
	            {
	               openFile();
	            }
	         });
	      fileMenu.add(openItem);

	      JMenuItem saveItem = new JMenuItem("Save");
	      saveItem.addActionListener(new
	         ActionListener()
	         {
	            public void actionPerformed(ActionEvent event)
	            {
	               saveFile();
	            }
	         });
	      fileMenu.add(saveItem);

	      JMenuItem exitItem = new JMenuItem("Exit");
	      exitItem.addActionListener(new
	         ActionListener()
	         {
	            public void actionPerformed(ActionEvent event)
	            {
	            	CsKinshipTreeFrame.this.dispose();
	            }
	         });
	      fileMenu.add(exitItem);

	      JMenuItem deleteItem = new JMenuItem("Delete");
	      deleteItem.addActionListener(new
	         ActionListener()
	         {
	            public void actionPerformed(ActionEvent event)
	            {
	               panel.removeSelected();
	            }
	         });

	      JMenuItem flipTreeItem
	         = new JMenuItem("Flip Tree");
	      flipTreeItem.addActionListener(new
	         ActionListener()
	         {
	            public void actionPerformed(ActionEvent event)
	            {
	            	CsKinshipTreePanel ktPanel = (CsKinshipTreePanel)panel;
	               ktPanel.flipTree();
	               CsKinshipTreeFrame.this.repaint();
	            }
	         });

	      JMenuItem toggleCodeItem
	         = new JMenuItem("Toggle Code");
	      toggleCodeItem.addActionListener(new
	         ActionListener()
	         {
	            public void actionPerformed(ActionEvent event)
	            {
	            	CsKinshipTreePanel ktPanel = (CsKinshipTreePanel)panel;
	               ktPanel.toggleCode();
	               CsKinshipTreeFrame.this.repaint();
	            }
	         });

	      JMenuItem copyImageItem
	         = new JMenuItem("Copy Image");
	      copyImageItem.addActionListener(new
	         ActionListener()
	         {
				public void actionPerformed(ActionEvent e) {
					PanelImageUtil.copyImage(PanelImageUtil.getPanelImageReverseBkgrd(panel));
				}
	         });

	      JMenuItem saveImageItem = new JMenuItem("Save Image");
	      saveImageItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser fileChooser = new JFileChooser("Save kinship tree image as JPG:");
					fileChooser.setDialogTitle("Specify a file to save");   
					 
					int userSelection = fileChooser.showSaveDialog(CsKinshipTreeFrame.this);
					 
					if (userSelection == JFileChooser.APPROVE_OPTION) {
					    File fileToSave = fileChooser.getSelectedFile();
					    System.out.println("Save as file: " + fileToSave.getAbsolutePath());
						PanelImageUtil.savePanelAsImage(panel, fileToSave);
					}				
				}
				
 	      });
	      JMenu editMenu = new JMenu("Edit");
	      editMenu.add(deleteItem);
	      editMenu.add(flipTreeItem);
	      editMenu.add(toggleCodeItem);
	      editMenu.add(copyImageItem);
	      editMenu.add(saveImageItem);
	      menuBar.add(editMenu);
	}

}
