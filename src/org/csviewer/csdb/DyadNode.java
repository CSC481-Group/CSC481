package org.csviewer.csdb;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

//import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

//import mgr.familytree.FamilyTreeMgr;

public class DyadNode extends IconNode {
	private String dam;
	private String sire;
	private LabelIcon[] dyadIcons = new LabelIcon[2];
	
	public DyadNode(String dam, String sire) {
		super(new CompoundIcon(dam, sire));
		this.dam = dam;
		this.sire = sire;
		dyadIcons[0] = new LabelIcon(dam);
		dyadIcons[1] = new LabelIcon(sire);
		//icon = new CompoundIcon(dam, sire);
	}
	   
	// in V2.0: assuming showUnicode is always true
	// 			dam and sire are both unicode
	public DyadNode(String dam, String sire, boolean showUnicode) {
		//super(new DoubleLabelIcon(showUnicode?"A-123":dam, showUnicode?"B-456":dam));
		/*
		 * use unicode only for now
		super(new DoubleLabelIcon(showUnicode?FamilyTreeMgr.getUnicodeForTattoo(dam):dam, 
				showUnicode?FamilyTreeMgr.getUnicodeForTattoo(sire):sire));
		*/
		super(new DoubleLabelIcon(dam, sire));
		// in V2.0: assuming showUnicode is always true
		
		this.dam = dam;
		this.sire = sire;
		//dyadIcons[0] = new LabelIcon(showUnicode?"A-123":dam);
		//dyadIcons[1] = new LabelIcon(showUnicode?"B-456":sire);
		//dyadIcons[0] = new LabelIcon(showUnicode?FamilyTreeMgr.getUnicodeForTattoo(dam):dam);
		//dyadIcons[1] = new LabelIcon(showUnicode?FamilyTreeMgr.getUnicodeForTattoo(sire):sire);

		// in V2.0: assuming showUnicode is always true
		dyadIcons[0] = new LabelIcon(dam);
		dyadIcons[1] = new LabelIcon(sire);
		
		//icon = new CompoundIcon(dam, sire);
	}
	   
   public Object clone() {
	   String d = JOptionPane.showInputDialog(null, "Please provide Dam Tattoo...");
	   String s = JOptionPane.showInputDialog(null, "Please provide Sire Tattoo...");
	   
	   return new DyadNode(d, s);
   }
	
   public static void main(String[] args) {
	   DyadNode d = new DyadNode("38P", "70D", true);
	   /*
       JOptionPane.showConfirmDialog(null, "See my LabelIcon?",
               "LabelIcon Test", JOptionPane.OK_OPTION,
               JOptionPane.PLAIN_MESSAGE, new AnimalNode("Tattoo").icon);
               */
	   ImageIcon ic = d.icon;
	   if (d.icon.getImage() == null)
		   System.out.println("image is null");
	   ic.setImage(d.icon.getImage());
       JOptionPane.showConfirmDialog(null, "See my LabelIcon?",
               "LabelIcon Test", JOptionPane.OK_OPTION,
               JOptionPane.PLAIN_MESSAGE, ic);
       //JOptionPane.PLAIN_MESSAGE, new CompoundIcon("38P", "70D"));
   }

	public String getDam() {
		return dam;
	}

	public String getSire() {
		return sire;
	}
}

class DoubleLabelIcon extends ImageIcon {
	private String caption1, caption2;
	private LabelIcon[] dyadIcons = new LabelIcon[2];
	private int width, height;

	public DoubleLabelIcon(String caption1, String caption2) {
		this.caption1 = caption1.replace("\"", "");
		this.caption2 = caption2.replace("\"", "");
		dyadIcons[0] = new LabelIcon(this.caption1);
		dyadIcons[1] = new LabelIcon(this.caption2);
		width = 80; 
		height = 25;
		Image image = createDoubleLebelImage();
		setImage(image);
	}

	public int getIconHeight() {
        return height;
    }

    public int getIconWidth() {
        return width;
    }
	
	private Image createDoubleLebelImage() {
		BufferedImage image = new BufferedImage(width, height, 
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.createGraphics();
		drawDyadIamge(g);
		return image;
	}

	private void drawDyadIamge(Graphics g) {
    	Color oldColor = g.getColor();
    	int textHeight = g.getFontMetrics().getHeight();
        int textWidth = g.getFontMetrics().charsWidth(caption1.toCharArray(), 0,
        		caption1.length());
        g.setColor(Color.PINK);
        g.drawRect(0, 0, width/2, height-1);
        int left = (width/2 - textWidth)/2 + 1;
        int base = height - (height - textHeight)/2 - 3;
        g.drawString(caption1, left, base);
        textWidth = g.getFontMetrics().charsWidth(caption2.toCharArray(), 0,
        		caption2.length());    
        g.setColor(Color.BLUE);
        g.drawRect(40, 0, width/2-1, height-1);
        left = (width/2 - textWidth)/2 + 41;
        g.drawString(caption2, left, base);
        g.setColor(oldColor);
	}
}

class CompoundIcon extends ImageIcon {
	private String dam, sire;
	private int width, height;
	
	public CompoundIcon(String dam, String sire) {
		width = 80; 
		height = 25;
		this.dam = dam.replace("\"", "");
		this.sire = sire.replace("\"", "");
		Image image = createDyadImage();
		setImage(image);
	}

    private Image createDyadImage() {
		BufferedImage image = new BufferedImage(width, height, 
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.createGraphics();
		drawDyadIamge(g);
		return image;
	}

	public int getIconHeight() {
        return height;
    }

    public int getIconWidth() {
        return width;
    }
	
    public void paintIcon(Component c, Graphics g, int x, int y) {
    	drawDyadIamge(g);
    }
    
    private void drawDyadIamge(Graphics g) {
    	Color oldColor = g.getColor();
    	int textHeight = g.getFontMetrics().getHeight();
        int textWidth = g.getFontMetrics().charsWidth(dam.toCharArray(), 0,
                dam.length());
        g.setColor(Color.PINK);
        g.drawRect(0, 0, width/2, height-1);
        int left = (width/2 - textWidth)/2 + 1;
        int base = height - (height - textHeight)/2 - 3;
        g.drawString(dam, left, base);
        textWidth = g.getFontMetrics().charsWidth(sire.toCharArray(), 0,
                sire.length());    
        g.setColor(Color.BLUE);
        g.drawRect(40, 0, width/2-1, height-1);
        left = (width/2 - textWidth)/2 + 41;
        g.drawString(sire, left, base);
        g.setColor(oldColor);
    }
}
