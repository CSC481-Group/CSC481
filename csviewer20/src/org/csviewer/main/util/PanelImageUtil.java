package org.csviewer.main.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.csviewer.csdb.core.GraphPanel;

public class PanelImageUtil {
	private static CsvClipboardOwner clipboard = new CsvClipboardOwner();
	
	// copy image to clipboard
	public static void copyImage(BufferedImage bi)
    {
        TransferableImage trans = new TransferableImage( bi );
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        c.setContents( trans, clipboard );
    }
	
	public static void savePanelAsImage(JComponent panel, File file) {
		BufferedImage image = getPanelImage(panel);

		// Write the BufferedImage as a JPEG image to the specified file
	    try {
	        ImageIO.write(image, "jpg", file);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	public static void savePanelAsImage(JComponent panel, String filename) {
		File file = new File(filename);
		savePanelAsImage(panel, file);
	}

	public static BufferedImage getPanelImage(JComponent panel) {
	    // Create a BufferedImage object with the size of the JPanel
	    BufferedImage image = new BufferedImage(panel.getWidth(), 
	    		panel.getHeight(), BufferedImage.TYPE_INT_RGB);
	    
	    Color defaultColor = image.getGraphics().getColor();
	    image.getGraphics().drawRect(0, 0, panel.getWidth(), 
	    		panel.getHeight());
	    // paint the JPanel's contents to BufferedImage's Graphics 
	    panel.paint(image.getGraphics());
	    image.getGraphics().setColor(defaultColor);
	    
		return image;
	}

	public static void copyImage(JComponent panel) {
		copyImage(getPanelImage(panel));
	}

	public static void copyImage(Component c) {
		copyImage((JComponent)c);
	}

	public static BufferedImage getPanelImageReverseBkgrd(GraphPanel panel) {
		BufferedImage image = getPanelImage(panel);
		
		for (int row=0; row<image.getHeight(); row++) {
			for (int col=0; col<image.getWidth(); col++) {
				//System.out.print(row + "," + col);
				if (new Color(image.getRGB(col, row)).equals(Color.BLACK) )
					image.setRGB(col, row, -1);
				//System.out.println("==>" + image.getRGB(col, row));
			}
		}
		return image;
	}
}

	class CsvClipboardOwner implements ClipboardOwner {

		@Override
		public void lostOwnership(Clipboard clipboard, Transferable contents) {
			System.out.println( "Lost Clipboard Ownership for " + 
					contents.getClass());
		}
	}
	
    class TransferableImage implements Transferable {

        Image i;

        public TransferableImage( Image i ) {
            this.i = i;
        }

        public Object getTransferData( DataFlavor flavor )
        throws UnsupportedFlavorException, IOException {
            if ( flavor.equals( DataFlavor.imageFlavor ) && i != null ) {
                return i;
            }
            else {
                throw new UnsupportedFlavorException( flavor );
            }
        }

        public DataFlavor[] getTransferDataFlavors() {
            DataFlavor[] flavors = new DataFlavor[ 1 ];
            flavors[ 0 ] = DataFlavor.imageFlavor;
            return flavors;
        }

        public boolean isDataFlavorSupported( DataFlavor flavor ) {
            DataFlavor[] flavors = getTransferDataFlavors();
            for ( int i = 0; i < flavors.length; i++ ) {
                if ( flavor.equals( flavors[ i ] ) ) {
                    return true;
                }
            }

            return false;
        }
    }
//}
