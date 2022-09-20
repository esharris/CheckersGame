package com.earl.nbyncheckers.base;

import com.earl.nbynboard.Board;
import com.earl.nbynboard.BoardRow;
import com.earl.utilities.Coordinate;

public class CoordinateUtils {

	public static int coordinateToIndex(int columnCount, Coordinate coordinate) {
		final int rowIndex = coordinate.getX();
		final int columnIndex = coordinate.getY();
		final int halfColumnCount = columnCount / 2;
		/**
		 * Since columnCount is even, each row has the same number of colored cells,
		 * halfColumnCount. This makes the formula work. The formula is harder when the
		 * columnCount is odd.
		 */
		return rowIndex * halfColumnCount + columnIndex / 2;
	}

	public static Coordinate indexToCoordinate(Board board, int index) {
		final int halfColumnCount = board.getColumnCount() / 2;
		final int row = index / halfColumnCount;
		final BoardRow boardRow = board.get(row);
		final int coloredCellCode = boardRow.coloredCellCode();
		final int column0 = (index % halfColumnCount) * 2;
		/**
		 * Turn column2 into an index to a legal cell.
		 */
		final int column = (column0 % 2 == coloredCellCode) ? column0 : column0 + 1;
		return new Coordinate(row, column);
	}
}
