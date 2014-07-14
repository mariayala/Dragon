
/*
 * Maria A Yala
 * may36@drexel.edu
 * CS332, Assignment [1]
 */
 package start;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.*;

public class Program1 {
	/*
	 * Create the GUI and show it.
	 */
	private static void createAndShowGUI() {
		//Create and set up the window
		JFrame frame = new JFrame("Program1");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(540,100));
		
		//Create root panel to hold A & B groups
		JPanel root = new JPanel(new BorderLayout());

		// Group A
		JPanel a = new JPanel(new BorderLayout());		
		root.add(a, BorderLayout.WEST);
		
		// Group B components - in a BorderLayout
		JPanel b = new JPanel(new BorderLayout());
		root.add(b, BorderLayout.CENTER);
		
		// Group A components
		JPanel aGridBag = new JPanel();
		aGridBag.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5,5,5,5);

		c.anchor = GridBagConstraints.SOUTH;
		
		// Add components to the A panel
		a.add(aGridBag, BorderLayout.SOUTH);
		
		JLabel label = new JLabel("Label");
		
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		aGridBag.add(label, c);
		
		JButton button1 = new JButton("Button 1");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2; // 2 columns wide
		aGridBag.add(button1, c);
		
		JLabel longLabel = new JLabel("LongLabel");
		
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		aGridBag.add(longLabel, c);
		
		JButton b2 = new JButton("B2");
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		aGridBag.add(b2, c);
		
		JButton b3 = new JButton("B3");
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		aGridBag.add(b3, c);	
		
		// Add components to the B panel 
		JPanel bchild = new JPanel();
		bchild.setLayout(new GridLayout(1,3));
		b.add(bchild, BorderLayout.CENTER);
		
		// First col that contains the buttons 1,2
		JPanel pan1 = new JPanel();
		pan1.setLayout(new GridLayout(2,1));
		JButton one = new JButton("1");
		JButton two = new JButton("2");
		
		pan1.add(one);
		pan1.add(two);

		// Middle row that contains buttons 3, 4, 5.
		JPanel pan2 = new JPanel(new BorderLayout());
		JButton three = new JButton("3");
		JButton four = new JButton("4");
		JButton five = new JButton("5");
		
		pan2.add(three, BorderLayout.NORTH);
		pan2.add(four, BorderLayout.CENTER);
		pan2.add(five, BorderLayout.SOUTH);
		
		// Third col that contains the buttons 6,7
		JPanel pan3 = new JPanel();
		pan3.setLayout(new GridLayout(2,1));
		JButton six = new JButton("6");
		JButton seven = new JButton("7");
				
		pan3.add(six);
		pan3.add(seven);

		bchild.add(pan1);
		bchild.add(pan2);
		bchild.add(pan3);
		
		// SOUTH
		JButton help = new JButton("help");
		JButton advanced = new JButton("Advanced");
		JButton okay = new JButton("OK");
		JButton cancel = new JButton("Cancel");
		JPanel southPanel = new JPanel();
				
		// Add components to southPanel
		southPanel.add(help);
		southPanel.add(advanced);
		southPanel.add(okay);
		southPanel.add(cancel);
		
		b.add(southPanel, BorderLayout.SOUTH);

		// Add the root to the frame
		frame.getContentPane().add(root);
		
		//Display the window
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
