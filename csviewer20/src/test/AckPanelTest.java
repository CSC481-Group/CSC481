package test;

import javax.swing.*;

import org.csviewer.main.AcknowledgementPanel;

public class AckPanelTest {

	public static void main(String[] args) {
		JFrame f = new JFrame("Acknowledgement");
		AcknowledgementPanel ap = new AcknowledgementPanel();
		f.add(ap);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
	
}

