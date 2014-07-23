/*
 * Maria A Yala
 * may36@drexel.edu
 * CS332, Assignment [1]
 */
package start;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Program2 {
	static JTextField upper;
	static JTextField lower;
	public static void createAndShowGUI(){
		//Create and set up the window
		JFrame frame = new JFrame("Program2");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(250,100));
		
		//Create root panel 
		JPanel root = new JPanel(new BorderLayout());
			
		// CENTER
		upper = new JTextField("+");
		upper.setEditable(true);
		lower = new JTextField("+");
		lower.setEditable(false);
		
		upper.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				if(upper.isEditable()){
					upper.setText("");
				}
			}
		});
		
		lower.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				if(lower.isEditable()){
					lower.setText("");	
				}
			}
		});
				
		// Add a listener to the textfields
		upper.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String t;
				if(upper.isEditable()){
					t = "+" + upper.getText();
					upper.setText("");
					upper.setText(t);
				} else {
					t = "+";
					upper.setText(t);
				}
			}
		});
		
		// Add a listener to the textfields
		lower.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String t;
				if(lower.isEditable()){
					t = "+" + lower.getText();
					lower.setText("");
					lower.setText(t);
				} else {
					t = "+";
					lower.setText(t);
					}
				}
			});
		
		// NORTH
		JButton up = new JButton("Up");
		root.add(up, BorderLayout.NORTH);
		up.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				upper.setEditable(true);
				lower.setEditable(false);
			}
		});
		
		// SOUTH
		JButton down = new JButton("Down");
		root.add(down, BorderLayout.SOUTH);
		down.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				upper.setEditable(false);
				lower.setEditable(true);
			}
		});
		
		// WEST
		JButton swap = new JButton("Swap");
		root.add(swap, BorderLayout.WEST);
		swap.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String up = upper.getText();
				String low = lower.getText();
				upper.setText(low);
				lower.setText(up);
			}
		});
		
		// EAST
		JButton reset = new JButton("Reset");
		root.add(reset, BorderLayout.EAST);
		reset.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String t = "+";
				upper.setText(t);
				lower.setText(t);
			}
		});
		
		// Add the textboxes to the center
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(2,1));
		center.add(upper);
		center.add(lower);
		root.add(center, BorderLayout.CENTER);		
		
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
