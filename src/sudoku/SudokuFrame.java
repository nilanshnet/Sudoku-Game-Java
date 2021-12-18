package sudoku;

import java.awt.event.ActionEvent;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.io.*;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/*
 * This class holds and manages the main frame of the Sudoku game program
 * 
 * This main frame holds all the parts like SUdoku grid, all buttons and other GUI for the game except server 
 * 
 */
public class SudokuFrame implements Serializable {

    private final JFrame frame = new JFrame("Sudoku");
    public SudokuGrid grid;
    public JTextField textField = null;
	public JTextArea textArea = null;
	ObjectOutputStream toServer = null;
	ObjectInputStream fromServer = null;
	
	public Socket socket = null;
	private final int SAVE_STATE = 100;
	private final int LOAD_STATE = 101;
    
	int DIMENSION = 9;
	int cellsToScan = DIMENSION * DIMENSION;
	
    public SudokuFrame() {
        frame.getContentPane().add(grid = new SudokuGrid(9));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buildMenu();
        frame.pack();
        frame.setResizable(true);
        centerView();
        frame.setVisible(true);
    }
    

    public SudokuGrid getstate(){
    	return this.grid;
    }
    
    public void setstate(SudokuGrid grid) {
    	this.grid = grid;
    }
    
    private void buildMenu() {
        JMenuBar bar = new JMenuBar();
        
        JMenu Menu = new JMenu("Features");

        JMenuItem Connect  = new JMenuItem("Connect Server");
        
        JMenuItem disconnect = new JMenuItem("Disconnect Server");
        

        JMenuItem save   = new JMenuItem("Save Game");
        JMenuItem open   = new JMenuItem("Load Game");
        

        Menu.add(Connect);
        Menu.addSeparator();
        Menu.add(disconnect);
        Menu.addSeparator();
        Menu.add(open);
        Menu.addSeparator();
        Menu.add(save);

        bar.add(Menu);
        
        open.addActionListener((ActionEvent e) -> {
        	JFrame f=new JFrame();
        	try {	
	        	
        		String id_String = JOptionPane.showInputDialog(f,"Enter Used ID: ");
			    
	        	int id = Integer.parseInt(id_String);
			    
			    try {
					socket = new Socket("localhost", 8000);
					System.out.println("Game Data Loaded");
					toServer = new ObjectOutputStream(socket.getOutputStream());
					toServer.writeInt(LOAD_STATE);
					toServer.writeInt(id);
					toServer.flush();
					
					
					fromServer = new ObjectInputStream(socket.getInputStream());
					
					System.out.println(fromServer.readInt()+" Player Id"); // Player Id
					
					// input game state from database
					JTextField temp [][] =  (JTextField [][])fromServer.readObject();
					
					// repainting the state on the grid
					
					for(int y = 0; y < 9; ++y) {
						for (int x = 0; x < 9; ++x) {
							String S = temp[y][x].getText();
							
							this.grid.grid[y][x].setText(S);
							
						}
					}
					
					
					} catch (IOException e1) {
						//e1.printStackTrace();
						textArea.setText("connection Failure");
					}
        		}
        		catch(Exception ex) {
        			ex.printStackTrace();
        		}
		    
              });
        
        
        save.addActionListener((ActionEvent e) -> {
        	try {
					socket = new Socket("localhost", 8000);
					
					toServer = new ObjectOutputStream(socket.getOutputStream());
					
					toServer.writeInt(SAVE_STATE); // Flag
					
					toServer.writeObject(this.grid.grid);
					
					toServer.flush(); 
					
					} catch (IOException e1) {
						
						e1.printStackTrace();
						textArea.setText("connection Failure");
					}
        		
		    
              });



        Connect.addActionListener((ActionEvent e) -> {
        
        	try {
    			socket = new Socket("localhost", 8000);
    			System.out.println("connected");
    			
    		} catch (IOException e1) {
    			
    			System.out.println("connection Fail");
    			    		}
        });
        
        disconnect.addActionListener((ActionEvent e) -> {
 
        	try { 
        		socket.close(); 
        		
        		System.out.println("connection closed");
        	} 
        	catch (Exception e1) {
        		System.err.println("error");
        	}
        	
        });
        frame.setJMenuBar(bar);
    }

    

	private void centerView() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();

        frame.setLocation((screen.width - frameSize.width) >> 1,
                          (screen.height - frameSize.height) >> 1);
    }
}
