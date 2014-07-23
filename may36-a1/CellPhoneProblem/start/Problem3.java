/*
 * Maria A Yala
 * may36@drexel.edu
 * CS332, Assignment [1]
 */
package start;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Problem3 {
	static long startTime = System.nanoTime();
	static long elapsedTime;
	static long keycount = 0;
	public static void createAndShowGUI(){
		//Create and set up the window
		JFrame frame = new JFrame("Program3");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(700,100));
		
		JPanel root = new JPanel();
		root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
		
		String s = "Your average hold time for the keys is: Unknown";
		final JLabel label = new JLabel(s);
		final JTextArea entry = new JTextArea("");
		entry.setLineWrap(true);
		entry.setSize(100,1);
		
		root.add(label);
		root.add(entry);
		
		//Start timer as soon as mouse clicks textbox.
		entry.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
					//Start the timer
					startTime = System.nanoTime();
					entry.setText("");
				}
			}
		);
		
		//Stop timing when user clicks enter.
		entry.addKeyListener(new KeyListener(){
			
			@Override
			public void keyPressed(KeyEvent e) {
				// Check for the enter key pressed
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					//Get the current time and calculate elapsed time
					elapsedTime = System.nanoTime() - startTime;
					//Calculate the average
					long eTime = (elapsedTime/1000000)/keycount;
					//Update the label
					label.setText("Your average hold time for the keys is:" + eTime + " milliseconds");
				} else {
					// count the number of keys pressed
					keycount = keycount + 1;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
			}	
		});
		
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
