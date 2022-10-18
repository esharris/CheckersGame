package com.earl.nbynboard;

import com.earl.utilities.Coordinate;

/**
 *
 * A checkerboard.
 *
 * @author earlharris
 *
 */
public final class Board {

    private static final String TO_STRING_PREFIX = "[";
    private static final String T0_STRING_SEPARATOR = ", ";
    private static final String TO_STRING_SUFFIX = "]";

    private final BoardRow[] buffer;
    private final boolean bottomLeftColored;

    /**
     *
     * @param rowCount
     * @param columnCount
     * @param bottomLeftColored
     */
    public Board(int rowCount, int columnCount, boolean bottomLeftColored) {
        buffer = new BoardRow[rowCount];
        for (int i = 0; i < rowCount; i++) {
            buffer[i] = new BoardRow(columnCount, i, bottomLeftColored);
        }
        this.bottomLeftColored = bottomLeftColored;
    }

    public BoardRow get(int r) {
        return buffer[r];
    }

    public int getRowCount() {
        return buffer.length;
    }

    public int getColumnCount() {
        return getRowCount() != 0 ? buffer[0].getColumnCount() : 0;
    }

    public boolean contains(Coordinate coordinate) {
        int row = coordinate.getX();
        int column = coordinate.getY();
        return 0 <= row && row < getRowCount() && 0 <= column && column < getColumnCount();
    }

    public int occurrences(CheckerColor checkerColor) {
        int count = 0;
        for (int i = 0; i != buffer.length; i++) {
            final BoardRow boardRow = buffer[i];
            count += boardRow.occurrences(checkerColor);
        }
        return count;
    }

    /**
     * I can't add equals and hashcode methods, because this class' objects are
     * mutable.
     *
     * @param board
     * @return boolean Does board match this?
     */
    public boolean matches(Board board) {
        boolean result;
        if (buffer.length == board.buffer.length) { // same size
            int i = 0;
            boolean match = true;
            while (i < buffer.length && match) {
                if (buffer[i].matches(board.buffer[i])) {
                    i++;
                } else {
                    match = false;
                }
            }
            result = match && (bottomLeftColored == board.bottomLeftColored);
        } else { // different sizes
            result = false;
        }
        return result;
    }

    public Board copy() {
        Board result = new Board(getRowCount(), getColumnCount(), bottomLeftColored);
        for (int i = 0; i < buffer.length; i++) {
            result.buffer[i] = buffer[i].copy();
        }
        return result;
    }

    public Board flip() {
        /**
         * Maintain the checkerboard arrangement after the flip.
         */
        boolean bottomLeftColored1 = (getRowCount() + getColumnCount()) % 2 == 1 ? !bottomLeftColored
                : bottomLeftColored;
        Board result = new Board(getRowCount(), getColumnCount(), bottomLeftColored1);
        for (int i = 0; i < buffer.length; i++) {
            result.buffer[i] = buffer[(buffer.length - 1) - i].flip(i, bottomLeftColored1);
        }
        return result;
    }

    public boolean isBottomLeftColored() {
        return bottomLeftColored;
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Board.class);
        stringBuilder.append(TO_STRING_PREFIX);
        String separator = "";
        for (BoardRow buffer1 : buffer) {
            stringBuilder.append(separator);
            stringBuilder.append(buffer1);
            separator = T0_STRING_SEPARATOR;
        }
        stringBuilder.append(TO_STRING_SUFFIX);
        return stringBuilder.toString();
    }
}
