package sudoku;


/*
 * This class is used to solve the sudoku puzzle.
 * 
 * 
 */
public class App{

    /*
     * Default dimension of the input sudoku.
     */
    private static final int DIMENSION = 9;

    private Sudoku inputSudoku;
    private SudokuSolver solver;

    public App() {
            
    	this.inputSudoku = new Sudoku(DIMENSION);
        
    }

    private void createSolver() {
        this.solver = new SudokuSolver();
    }

    private void solve() {
        
        try {
            Sudoku solution = solver.solve(inputSudoku);

            if (!solution.isValid()) {
                throw new IllegalStateException(
                        "Exception: The computed solution is not valid.");
            }

        } catch (Exception ex) {
            System.err.println("Exception: " + ex.getMessage());
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            App app = new App();

            try {
                app.createSolver();
            } catch (IllegalArgumentException ex) {
                System.err.println("Exception: " + ex.getMessage());
                System.exit(1);
            }

            
            app.solve();
        } else {
            javax.swing.SwingUtilities.invokeLater(() -> { 
                new SudokuFrame(); 
            });
        }
    }

}











/*
 * References and help sources:
 * 
 * stackexchange.com
 * stackoverflow.com
 * geeksforgeeks.com
 * 
 */

