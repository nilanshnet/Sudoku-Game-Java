package sudoku;

import java.awt.Point;
import java.util.Objects;

/*
 * This class contains the algorithm for solving 9 x 9 Sudoku grid.
 */
 
public class SudokuSolver {


    private final int dimension;
    private final int minisquareDimension;
    private Sudoku input;
    private final IntSet[] rowSetArray;
    private final IntSet[] columnSetArray;
    private final IntSet[][] minisquareSetMatrix;
    private final Point point = new Point();
    private Sudoku solution;

    public SudokuSolver() {
        this.minisquareDimension = 3; 
        this.dimension = 9;

        rowSetArray    = new IntSet[dimension];
        columnSetArray = new IntSet[dimension];

        minisquareSetMatrix = new IntSet[minisquareDimension]
                                        [minisquareDimension];

        for (int i = 0; i < dimension; ++i) {
            rowSetArray   [i] = new IntSet(dimension + 1);
            columnSetArray[i] = new IntSet(dimension + 1);
        }

        for (int y = 0; y < minisquareDimension; ++y) {
            for (int x = 0; x < minisquareDimension; ++x) {
                minisquareSetMatrix[y][x] = new IntSet(dimension + 1);
            }
        }
    }

    public Sudoku solve(Sudoku input) {
        Objects.requireNonNull(input, "The input sudoku is null.");
        this.input = new Sudoku(input);
        fixInputSudoku();
        clearSets();
        tryInitializeSets();
        solution = new Sudoku(dimension);
        solve();
        return solution;
    }

    
    private void fixInputSudoku() {
        int dimension = input.getDimension();
        
        
        for (int y = 0; y < dimension; ++y) {
            for (int x = 0; x < dimension; ++x) {
                int currentValue = input.get(x, y);

                if (currentValue < 1 || currentValue > dimension) {
                	
                	input.set(x, y, 0);
                }
            }
        }
    }

    
    private void clearSets() {
        for (int i = 0; i < dimension; ++i) {
            rowSetArray   [i].clear();
            columnSetArray[i].clear();
        }

        for (int y = 0; y < minisquareDimension; ++y) {
            for (int x = 0; x < minisquareDimension; ++x) {
                minisquareSetMatrix[y][x].clear();
            }
        }
    }

    
    
    private void tryInitializeSets() {
        for (int y = 0; y < dimension; ++y) {
            for (int x = 0; x < dimension; ++x) {
            	
            	int currentValue = input.get(x, y);

                if (rowSetArray[y].contains(currentValue)) {
                    throw new IllegalArgumentException(
                        "The cell (x = " + x + ", y = " + y + ") with " +
                        "value " + currentValue + 
                        " is a duplicate in its row.");
                }

                if (columnSetArray[x].contains(currentValue)) {
                    throw new IllegalArgumentException(
                        "The cell (x = " + x + ", y = " + y + ") with " +
                        "value " + currentValue + 
                        " is a duplicate in its column.");
                }

                loadMinisquareCoordinates(x, y);

                if (minisquareSetMatrix[point.y][point.x].contains(currentValue)) {
                    throw new IllegalArgumentException(
                        "The cell (x = " + x + ", y = " + y + ") with " +
                        "value " + currentValue + 
                        " is a duplicate in its minisquare.");
                }

                if (isValidCellValue(currentValue)) {
                    rowSetArray   [y].add(currentValue);
                    columnSetArray[x].add(currentValue);
                    // This call saves the result in the field 'point'.
                    minisquareSetMatrix[point.y][point.x].add(currentValue);
                }
            }
        }
    }

    
    private boolean isValidCellValue(int value) {
        return 0 < value && value <= dimension;
    }

    
    private void loadMinisquareCoordinates(int x, int y) {
        point.x = x / minisquareDimension;
        point.y = y / minisquareDimension;
    }

    private void solve() {
        solve(0, 0);
    }

    private boolean solve(int x, int y) {
        if (x == dimension) {
            // after getting done with row 'y', move to the row
            // 'y + 1' and set 'x' to zero.
            x = 0;
            ++y;
        }

        if (y == dimension) {
            // found a solution, return 'true'.
            return true;
        }

        if (input.get(x, y) != 0) {
            // Just load a predefined value from the input matrix 
            solution.set(x, y, input.get(x, y));
            return solve(x + 1, y);
        } 

        // Find least number fitting in the current cell (x, y).
        for (int i = 1; i <= dimension; ++i) {
            if (!columnSetArray[x].contains(i)
                    && !rowSetArray[y].contains(i)) {
                loadMinisquareCoordinates(x, y);

                if (!minisquareSetMatrix[point.y][point.x].contains(i)) {
                    solution.set(x, y, i);
                    rowSetArray   [y].add(i);
                    columnSetArray[x].add(i);
                    minisquareSetMatrix[point.y][point.x].add(i);

                    if (solve(x + 1, y)) {
                        // solution found; 
                    	// stop backtracking by returning
                        // at each recursion level.
                        return true;
                    }

                    // Setting 'i' at current cell (x, y) did not lead towards
                    // solution; remove from the sets and try larger value 
                    // for 'i' in the next iteration.
                    rowSetArray   [y].remove(i);
                    columnSetArray[x].remove(i);

                    // Reload the minisquare coordinates as they are likely to
                    // be invalid due to recursion.
                    loadMinisquareCoordinates(x, y);
                    minisquareSetMatrix[point.y][point.x].remove(i);
                }
            }
        }
        
        return false;
    }
}
