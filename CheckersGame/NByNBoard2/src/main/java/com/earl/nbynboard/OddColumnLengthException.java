package com.earl.nbynboard;

/**
 * 
 * @author earlharris
 *
 */
public class OddColumnLengthException extends RuntimeException {

	private static final long serialVersionUID = 1963832385791660583L;

	private final int columnCount;

	public OddColumnLengthException(int columnCount) {
		super("Expected even column length, but got " + columnCount);
		this.columnCount = columnCount;
	}

	public int getColumnCount() {
		return columnCount;
	}
}
