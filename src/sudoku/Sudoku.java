package sudoku;

/*
 * This class represents a sudoku matrix of 9X9 dimension and may 
 * contain arbitrary values at each cells.
 */


public class Sudoku {

    private final int[][] matrix;

    public Sudoku(int dimension) {
        this.matrix = new int[dimension][dimension];
    }

    public Sudoku(Sudoku sudoku) {
        this(sudoku.matrix.length);

        for (int y = 0; y < sudoku.matrix.length; ++y) {
            this.matrix[y] = sudoku.matrix[y].clone();
        }
    }

    public int get(int x, int y) {
        return matrix[y][x];
    }

    public void set(int x, int y, int value) {
        matrix[y][x] = value;
    }

    public int getDimension() {
        return matrix.length;
    }

    public boolean isValid() {
        int dimension = getDimension();
        int minisquareDimension = (int) Math.sqrt(dimension); //get Minisquare Dimensions - 3X3
        IntSet[] rowSetArray    = new IntSet[dimension];
        IntSet[] columnSetArray = new IntSet[dimension];
        IntSet[][] minisquareSetMatrix = new IntSet[minisquareDimension]
                                                   [minisquareDimension];

        for (int i = 0; i < dimension; ++i) {
            rowSetArray[i]    = new IntSet(dimension + 1);
            columnSetArray[i] = new IntSet(dimension + 1);
        }

        for (int squareY = 0; squareY < minisquareDimension; ++squareY) {
            for (int squareX = 0; squareX < minisquareDimension; ++squareX) {
                minisquareSetMatrix[squareY]
                                   [squareX] = new IntSet(dimension + 1);
            }
        }

        for (int y = 0; y < dimension; ++y) {
            for (int x = 0; x < dimension; ++x) {
                int currentValue = get(x, y);

                if (rowSetArray[y].contains(currentValue)
                        || columnSetArray[x].contains(currentValue)) {
                    return false;
                }

                int squareX = x / minisquareDimension;
                int squareY = y / minisquareDimension;

                if (minisquareSetMatrix[squareY]
                                       [squareX].contains(currentValue)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public String toString() {
        String maximumCellValueString = "" + matrix.length;
        int fieldLength = maximumCellValueString.length();
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < matrix.length; ++y) {
            sb.append(rowToString(y, fieldLength));
            sb.append('\n');
        }

        return sb.toString();
    }

    private String rowToString(int y, int fieldLength) {
        StringBuilder sb = new StringBuilder();

        for (int x = 0; x < matrix.length; ++x) {
            sb.append(String.format("%" + fieldLength + "d", get(x, y)));

            if (x < matrix.length - 1) {
                sb.append(' ');
            }
        }

        return sb.toString();
    } 
}
