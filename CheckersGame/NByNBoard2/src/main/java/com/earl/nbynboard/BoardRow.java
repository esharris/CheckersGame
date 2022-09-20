package com.earl.nbynboard;

import java.util.Arrays;

/**
 * 
 * A row in a checkerboard.
 * 
 * @author earlharris
 *
 */
public final class BoardRow {

	private static final String TO_STRING_PREFIX = "[";
	private static final String T0_STRING_SEPARATOR = ", ";
	private static final String TO_STRING_SUFFIX = "]";

	private final Cell[] buffer;

	/**
	 * In the enclosing board, this is the location. This makes the objects smarter,
	 * but less normalized.
	 */
	private final int index;

	private final boolean bottomLeftColored;

	/**
	 * Create a BoardRow of even length.
	 * 
	 * @param columnCount       an even column count.
	 * @param index
	 * @param bottomLeftColored
	 */
	public BoardRow(int columnCount, int index, boolean bottomLeftColored) {
		if (MathUtils.isEven(columnCount)) {
			buffer = new Cell[columnCount];
			for (int i = 0; i < buffer.length; i++) {
				buffer[i] = Cell.UNOCCUPIED_CELL;
			}
			this.bottomLeftColored = bottomLeftColored;
			this.index = index;
		} else {
			throw new OddColumnLengthException(columnCount);
		}
	}

	public Cell get(int c) {
		return buffer[c];
	}

	public void set(int c, Cell cell) {
		buffer[c] = cell;
	}

	public int getColumnCount() {
		return buffer.length;
	}

	public int occurrences(CheckerColor checkerColor) {
		int count = 0;
		for (int i = 0; i != buffer.length; i++) {
			final Cell cell = buffer[i];
			if (cell.isOccupied()) {
				final CheckerColor checkerColor1 = cell.getChecker().getCheckerColor();
				if (checkerColor == checkerColor1) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * The code associated with this row.
	 * 
	 * 0 indicates that the colored cells are on even columns.
	 * 
	 * 1 indicates that the colored cells are on odd columns.
	 * 
	 * @return
	 */
	public int coloredCellCode() {
		return (index + (bottomLeftColored ? 0 : 1)) % 2;
	}

	/**
	 * I can't add equals and hashcode methods, because this class' objects are
	 * mutable.
	 * 
	 * @param boardRow
	 * @return
	 */
	public boolean matches(BoardRow boardRow) {
		return Arrays.equals(buffer, boardRow.buffer) && index == boardRow.index
				&& bottomLeftColored == boardRow.bottomLeftColored;
	}

	public BoardRow copy() {
		BoardRow result = new BoardRow(buffer.length, index, bottomLeftColored);
		for (int i = 0; i < buffer.length; i++) {
			result.set(i, buffer[i]);
		}
		return result;
	}

	public BoardRow flip(int newIndex, boolean bottomLeftColored) {
		BoardRow result = new BoardRow(buffer.length, newIndex, bottomLeftColored);
		for (int i = 0; i < buffer.length; i++) {
			result.set(i, buffer[(buffer.length - 1) - i]);
		}
		return result;
	}

	public int getIndex() {
		return index;
	}

	public boolean isBottomLeftColored() {
		return bottomLeftColored;
	}

	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(BoardRow.class);
		stringBuilder.append(TO_STRING_PREFIX);
		String separator = "";
		for (int i = 0; i < buffer.length; i++) {
			stringBuilder.append(separator);
			stringBuilder.append(buffer[i].toString());
			separator = T0_STRING_SEPARATOR;
		}
		stringBuilder.append(TO_STRING_SUFFIX);
		return stringBuilder.toString();
	}
}
