package sudoku;

/*
 * This class implements an integer set used for keeping track which 
 * cell values are allowed at a particular sudoku cell.
 * 
 */
final class IntSet {

    private final boolean[] table;

    IntSet(int sudokuDimension) {
        this.table = new boolean[sudokuDimension];
    }

    void add(int number) {
        table[number] = true;
    }

    boolean contains(int number) {
        return table[number];
    }

    void remove(int number) {
        table[number] = false;
    }

    void clear() {
        for (int i = 0; i < table.length; ++i) {
            table[i] = false;
        }
}
}