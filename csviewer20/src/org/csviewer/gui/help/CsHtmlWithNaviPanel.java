package org.csviewer.gui.help;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextPane;

public class CsHtmlWithNaviPanel extends JPanel {
	private Color initDotColor = Color.WHITE;
	private Color selectDotColor = new Color(51, 153, 255);
	private Color initArrowColor = new Color(51, 133, 255);
	private Color selectArrowColor = Color.GRAY;
	private int highlightedIndex = 0;
	
	private Image background;
	private String title;
	private String imgFile;
	private String htmlFile;
	private Rectangle bounds = new Rectangle(50, 30, 700, 380);
	//Rectangle barBounds = new Rectangle(255, 430, 300, 50);
	Rectangle barBounds = new Rectangle(125, 430, 525, 50);
	private String htmlContents;
	private Color transparentColor;
	
	private JTextPane htmlContent;
	
	private static String[] htmlPages = {"html/Welcome/Page1.html", 
			"html/Welcome/Page2.html", "html/Welcome/Page3.html", 
			"html/Welcome/Page4.html", "html/Welcome/Page5.html"};

	public CsHtmlWithNaviPanel(String title, String imgFile, 
			String htmlFile) {
		background = new ImageIcon(imgFile).getImage();
		this.title = title;
		this.imgFile = imgFile;
		this.htmlFile = htmlFile;
		//this.bounds = bounds;
		htmlContents = loadHtml();
		
		this.setLayout(null);
		
		htmlContent = new JTextPane();
		htmlContent.setContentType("text/html");
		htmlContent.setText(htmlContents);
		htmlContent.setEditable(false);
		htmlContent.setBackground(Color.WHITE);
		//htmlContent.setBackground(new Color(223, 223, 223));
		htmlContent.setBounds(bounds);
		add(htmlContent);
		
		addNavigationBar();
		
		this.addMouseListener(new PageChoosingListener());
	}
	
	public CsHtmlWithNaviPanel(String title, String imgFile) {
		this(title, imgFile, htmlPages[0]);
	}

	private void addNavigationBar() {
		/*
		JLabel theNavigationBar = new JLabel("Test");
		theNavigationBar.setBounds(barBounds);
		//this.add(theNavigationBar);
		add(theNavigationBar );
		*/
		transparentColor = new Color(155, 200, 255);
	}
	
	private String loadHtml() {
		Scanner input = null;
		try {
			System.out.println("loading html file : " + htmlFile);
			input = new Scanner(new File(htmlFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String html = "";
		while (input.hasNextLine()) {
			html += "\n" + input.nextLine();
		}
		return html;
	}

	@Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
 
        //g.drawImage(background, 0, 0, null); // image full size
        g.drawImage(background, 0, 0, getWidth(), getHeight(), null); // image scaled
        drawNavigationBar(g);
    }
	
	private void drawNavigationBar(Graphics g) {
		// draw background banner
		Graphics2D g2d = (Graphics2D)g;
		g2d.setComposite(AlphaComposite.SrcOver.derive(0.8f));
        g.setColor(transparentColor);
        g2d.fill(this.barBounds);
		//g.fillRect(250, 400, 300, 50);
		g2d.setComposite(AlphaComposite.SrcOver);
		
		// draw arrows
		drawDetails(g2d);
	}
	
	private void drawDetails(Graphics2D g2) {
	    // make arrow as GeneralPath
	    drawArrows(g2);

	    // file dots
	    Shape dot;
		Color dotColor = Color.WHITE;
	    Color selectDotColor = new Color(51, 153, 255);
	    
	    Color temp = g2.getColor();
	    for (int i=0; i<5; i++) {
	    	//dot = new Ellipse2D.Double(330 + i*34, 450, 10, 10);
	    	dot = new Ellipse2D.Double(220 + i*84, 450, 10, 10);
			if (i==highlightedIndex)
	    		g2.setColor(selectDotColor);
			else
				g2.setColor(dotColor);
				
	    	g2.fill(dot);
	    }
		g2.setColor(temp);
	}
 
    private void drawArrows(Graphics2D g2) {
		Shape polygon;
	    Color temp = g2.getColor();
	    Color arrowColor;
	    
		// draw right arrow
	    polygon = makeArrow("RIGHT");
	    if (this.highlightedIndex == 4)
	    	arrowColor = this.selectArrowColor; 
	    else
	    	arrowColor = this.initArrowColor;
		g2.setColor(arrowColor);
		g2.fill(polygon);
		
		// draw left arrow
	    polygon = makeArrow("LEFT");
	    if (this.highlightedIndex == 0)
	    	arrowColor = this.selectArrowColor; 
	    else
	    	arrowColor = this.initArrowColor;
		g2.setColor(arrowColor);
		g2.fill(polygon);
		
		g2.setColor(temp);    	
    }

	private Shape makeArrow(String direction) {
	    //Point[] refs = {new Point(295, 445), new Point(500, 445)};
	    Point[] refs = {new Point(165, 445), new Point(610, 445)};
	    int increments[] = {15, 10};
	    
	    int [][][] arrow = {{{1, 0}, {-1, 1}, {1, 1}, {-1, 0}, {-1, -1}, {1, -1}},
	    					{{1, 0}, {1, 1}, {-1, 1}, {-1, 0}, {1, -1}, {-1, -1}}};
	    int x, y, iDir;
	    
    	GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 6);
    	if (direction.equals("RIGHT")) {
    		iDir = 1;
    	}
    	else 
    		iDir = 0;
    	
		x = refs[iDir].x;
	    y = refs[iDir].y;
	    polygon.moveTo(x, y);
	
	    for (int p=0; p<arrow[0].length; p++) {
		    x += increments[0] * arrow[iDir][p][0];
		    y += increments[1] * arrow[iDir][p][1];
		    polygon.lineTo(x, y);
	    }
	    polygon.closePath();	
	    
	    return polygon;
	}

	@Override
    public Dimension getPreferredSize()
    {
        return new Dimension(background.getWidth(this), background.getHeight(this));
    }


	public void setPage(int selectedItem) {
		htmlFile = this.htmlPages[selectedItem];
		htmlContents = this.loadHtml();
		//System.out.println(htmlContents);
		this.htmlContent.setText(htmlContents);
		this.highlightedIndex = selectedItem;
	}
	
	class PageChoosingListener extends MouseAdapter {
        public void mousePressed(MouseEvent mouseEvent) {
        	Point mousePoint = mouseEvent.getPoint();
        	int selectedItem = -100;
        	
        	if (barBounds.contains(mousePoint)) {
	        	//if (mousePoint.x <= 310 && mousePoint.x >= 280)
		        if (mousePoint.x <= 195 && mousePoint.x >= 125)
	        		selectedItem = -10;
	        	//else if (mousePoint.x <= 530 && mousePoint.x >= 500)
		        else if (mousePoint.x <= 650 && mousePoint.x >= 595)
	        		selectedItem = 10;
	        	else if (mousePoint.x <= 555 && mousePoint.x >= 145)
	        		selectedItem = ((int)(mousePoint.x - 180))/84;
        	}

        		//selectedItem = ((int)(mousePoint.x - 330))/33;
        	System.out.println(selectedItem + "@" + mousePoint);

        	switch (selectedItem) {
        	case 10: // right
        		if (highlightedIndex < 4) // has next page
        			setPage(++highlightedIndex);
        		else
        			return; // no need for change
        		break;
        	case -10: // right
        		if (highlightedIndex > 0) // has next page
        			setPage(--highlightedIndex);
        		else
        			return; // no need for change
        		break;
        	default: // a specific page
        		if (highlightedIndex != selectedItem) // has next page
        			setPage(selectedItem);
        		else
        			return; // no need for change
        		break;

        	case -100: // no item selected
        		return;
        	}

        	repaint();	// something changed
        }
	}
}
