package sudoku;

import java.io.Serializable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.io.File;
//import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

//import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.border.Border;

/*
 * This class defines the visual GUI component for Sudoku board, 
 * 
 * this Grid GUI is put inside the Sudoku Frame GUI 
 * 
 */

//@SuppressWarnings("serial")
public class SudokuGrid extends JPanel implements Serializable{

    public  JTextField[][] grid;
    public Map<JTextField, Point> mapFieldToCoordinates = new HashMap<>();
    public int dimension;
    public JPanel gridPanel;
    public JPanel buttonPanel;
    public JButton startButton;
    public JButton checkButton;
    public JButton clearButton;
    public JPanel[][] minisquarePanels;
    
    
    SudokuGrid(int dimension) {
        this.grid = new JTextField[dimension][dimension];
        this.dimension = dimension;
        

        for (int y = 0; y < dimension; ++y) {
            for (int x = 0; x < dimension; ++x) {
                JTextField field = new JTextField();
                mapFieldToCoordinates.put(field, new Point(x, y));
                grid[y][x] = field;
            }
        }
        
        this.gridPanel   = new JPanel();
        this.buttonPanel = new JPanel();

        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
        Dimension fieldDimension = new Dimension( 30, 30);

        class PopupMenuListener implements ActionListener {

            private final JTextField field;
            private final int number;
            
            PopupMenuListener(JTextField field, int number) {
                this.field  = field;
                this.number = number; 
            }
            
            @Override
            public void actionPerformed(ActionEvent e) {
                field.setText("" + number);
            }
        }
        
        for (int y = 0; y < dimension; ++y) {
            for (int x = 0; x < dimension; ++x) {
            	
            	// for design of the borders
            	
            	if (   x == 4 && y ==3 || x == 3 && y == 2 || x == 3 && y == 4 || x == 4 && y == 3
                		|| x == 6 && y == 2 || x == 4 && y == 8 || x == 2 && y == 2 || y == 6 && x == 7
                		|| x == 1 && y == 7 || x == 7 && y == 0 || x == 7 && y == 1 || y == 5 && x == 6
                		|| x == 8 && y == 8 || x == 6 && y == 2 || x == 0 && y == 4 || x == 2 && y == 5
                		|| x == 0 && y == 1 || x == 4 && y == 0 || x == 3 && y == 0 || x == 0 && y == 0
                		|| x == 8 && y == 2 || y == 6 && x == 3 || y == 6 && x == 5	|| x == 3 && y == 3
                		|| x == 7 && y == 4 || x == 6 && y == 8 || y == 6 && x == 0 || x == 2 && y == 8)
                		
                	{
            		continue;
                	}
                
            	
                JTextField field = grid[y][x];
                field.setBorder(border);
                field.setHorizontalAlignment(JTextField.CENTER);
                field.setPreferredSize(fieldDimension);
                
                JPopupMenu menu = new JPopupMenu();
                
                for (int i = 0; i <= dimension; ++i) {
                    JMenuItem item = new JMenuItem("" + i);
                    menu.add(item);
                    item.addActionListener(new PopupMenuListener(field, i));
                }
                
                field.add(menu);
                field.setComponentPopupMenu(menu);
            }
        }

        int minisquareDimension = (int) Math.sqrt(dimension);
        this.gridPanel.setLayout(new GridLayout(minisquareDimension,
                                                minisquareDimension));

        this.minisquarePanels = new JPanel[minisquareDimension]
                                          [minisquareDimension];

        Border minisquareBorder = BorderFactory.createLineBorder(Color.BLACK, 1);

        for (int y = 0; y < minisquareDimension; ++y) {
            for (int x = 0; x < minisquareDimension; ++x) {
                JPanel panel = new JPanel();
                panel.setLayout(new GridLayout(minisquareDimension,
                                               minisquareDimension));
                panel.setBorder(minisquareBorder);
                minisquarePanels[y][x] = panel;
                gridPanel.add(panel);
            }
        }

        for (int y = 0; y < dimension; ++y) {
            for (int x = 0; x < dimension; ++x) {
                int minisquareX = x / minisquareDimension;
                int minisquareY = y / minisquareDimension;

                minisquarePanels[minisquareY][minisquareX].add(grid[y][x]);
            }
        }

        this.gridPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 
                                                                1));
        this.clearButton = new JButton("Clear Board");
        this.startButton = new JButton("Start Game");
        this.checkButton = new JButton("Check");

        this.buttonPanel.setLayout(new BorderLayout());
        this.buttonPanel.add(clearButton, BorderLayout.WEST);
        this.buttonPanel.add(startButton, BorderLayout.EAST);
        this.buttonPanel.add(checkButton, BorderLayout.CENTER);

        this.setLayout(new BorderLayout());
        this.add(gridPanel,   BorderLayout.NORTH);
        this.add(buttonPanel, BorderLayout.SOUTH);  

        clearButton.addActionListener((ActionEvent e) -> {
            clearAll();
        });

        startButton.addActionListener((ActionEvent e) -> {
            solve();
        });
        
        checkButton.addActionListener((ActionEvent e) -> {
            if (solve()) {
            	String MESSAGE_TYPE_PROPERTY = "If you only touched the blank fields (without borders) then \n"
            			+ "Looks like you are doing it right. Keep Going!";
            	JOptionPane.showMessageDialog(this, MESSAGE_TYPE_PROPERTY);
            }
        });
    }

    int getDimension() {
        return dimension;
    }
    
    void clearAll() {
        for (JTextField[] row : grid) {
            for (JTextField field : row) {
                field.setText("");
            }
        }
    }
 
    
    Boolean solve() {
        Sudoku sudoku = new Sudoku(dimension);
        for (int y = 0; y < dimension; ++y) {
            for (int x = 0; x < dimension; ++x) {
            	int number = -1;
            	
            	String text = grid[y][x].getText();


                try {
                    number = Integer.parseInt(text.trim());
                } catch (NumberFormatException ex) {
                	 
                }
                sudoku.set(x, y, number);
            }
        }
        
        
        try {
            Sudoku solution = new SudokuSolver().solve(sudoku);
            String skip = dimension < 10 ? " " : "";
            for (int y = 0; y < dimension; ++y) {
                for (int x = 0; x < dimension; ++x) {
                	
                	// generating grid
                	
                	if (   x == 4 && y ==3 || x == 3 && y == 2 || x == 3 && y == 4 || x == 4 && y == 3
                		|| x == 6 && y == 2 || x == 4 && y == 8 || x == 2 && y == 2 || y == 6 && x == 7
                		|| x == 1 && y == 7 || x == 7 && y == 0 || x == 7 && y == 1 || y == 5 && x == 6
                		|| x == 8 && y == 8 || x == 6 && y == 2 || x == 0 && y == 4 || x == 2 && y == 5
                		|| x == 0 && y == 1 || x == 4 && y == 0 || x == 3 && y == 0 || x == 0 && y == 0
                		|| x == 8 && y == 2 || y == 6 && x == 3 || y == 6 && x == 5	|| x == 3 && y == 3
                		|| x == 7 && y == 4 || x == 6 && y == 8 || y == 6 && x == 0 || x == 2 && y == 8)
                		
                	{
                		continue;
                	}
                	
                	
                    grid[y][x].setText(skip + solution.get(x, y));
                }
            }
           

            if (!solution.isValid()) {
                throw new RuntimeException("Something went wrong.");
            }
            else {
            	return true;
            }
        } catch (Exception ex) {
        	
            JOptionPane.showMessageDialog(this, 
                                          ex.getMessage(),
                                          "Error",
                                          JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
