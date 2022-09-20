package com.earl.nbyncheckers.displayers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.earl.nbynboard.BoardRow;
import com.earl.nbynboard.Cell;
import com.earl.nbyncheckers.base.CoordinateUtils;
import com.earl.nbyncheckers.displayers.ColoredConsoleBoardRowDisplayer;
import com.earl.nbyncheckers.displayers.ColoredConsoleCellDisplayer;
import com.earl.utilities.Coordinate;

/**
 * 
 * @author earlharris
 *
 */
@Service
public class ColoredConsoleBoardRowDisplayerImpl implements ColoredConsoleBoardRowDisplayer {

	private static final String DISPLAY_SEPARATOR = " ";

	private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	private static final String ANSI_RESET = "\u001B[0m";

	private final ColoredConsoleCellDisplayer coloredConsoleCellDisplayerImpl;

	@Autowired
	public ColoredConsoleBoardRowDisplayerImpl(ColoredConsoleCellDisplayer coloredConsoleCellDisplayerImpl) {
		this.coloredConsoleCellDisplayerImpl = coloredConsoleCellDisplayerImpl;
	}

	/**
	 * 
	 * The string version of a BoardRow that is suitable for a color console.
	 * 
	 * @param boardRow the Boardrow this method will display.
	 * @return String
	 */
	@Override
	public String display(BoardRow boardRow) {
		/**
		 * Though this method is a little long. But it makes more sense to leave this
		 * method as is. The three segments should be visible together.
		 */
		final StringBuilder stringBuilder = new StringBuilder();
		final int rowIndex = boardRow.getIndex();
		final int coloredCellCode = boardRow.coloredCellCode();
		for (int i = 0; i < boardRow.getColumnCount(); i++) {
			if (i % 2 == coloredCellCode) {
				stringBuilder.append(ANSI_YELLOW_BACKGROUND);
				stringBuilder.append(DISPLAY_SEPARATOR.repeat(6));
				stringBuilder.append(ANSI_RESET);
			} else {
				stringBuilder.append(DISPLAY_SEPARATOR.repeat(6));
			}
		}
		stringBuilder.append(System.lineSeparator());
		for (int i = 0; i < boardRow.getColumnCount(); i++) {
			if (i % 2 == coloredCellCode) {
				stringBuilder.append(ANSI_YELLOW_BACKGROUND);
				formatCell(stringBuilder, boardRow.get(i), boardRow.getColumnCount(), i, rowIndex,
						boardRow.isBottomLeftColored(), coloredCellCode);
				stringBuilder.append(ANSI_RESET);
			} else {
				formatCell(stringBuilder, boardRow.get(i), boardRow.getColumnCount(), i, rowIndex,
						boardRow.isBottomLeftColored(), coloredCellCode);
			}
		}
		stringBuilder.append(System.lineSeparator());
		for (int i = 0; i < boardRow.getColumnCount(); i++) {
			if (i % 2 == coloredCellCode) {
				stringBuilder.append(ANSI_YELLOW_BACKGROUND);
				formatCell1(stringBuilder, boardRow.get(i), boardRow.getColumnCount(), i, rowIndex,
						boardRow.isBottomLeftColored(), coloredCellCode);
				stringBuilder.append(ANSI_RESET);
			} else {
				formatCell1(stringBuilder, boardRow.get(i), boardRow.getColumnCount(), i, rowIndex,
						boardRow.isBottomLeftColored(), coloredCellCode);
			}
		}
		stringBuilder.append(DISPLAY_SEPARATOR);
		return stringBuilder.toString();
	}

	public ColoredConsoleCellDisplayer getColoredConsoleCellDisplayer() {
		return coloredConsoleCellDisplayerImpl;
	}

	private void formatCell(StringBuilder stringBuilder, Cell cell, int columnCount, int i, int rowIndex,
			boolean bottomLeftColored, int coloredCellCode) {
		stringBuilder.append(DISPLAY_SEPARATOR.repeat(1));
		stringBuilder.append(coloredConsoleCellDisplayerImpl.display(cell).repeat(3));

		stringBuilder.append(DISPLAY_SEPARATOR.repeat(1));
		stringBuilder.append(DISPLAY_SEPARATOR);
	}

	private void formatCell1(StringBuilder stringBuilder, Cell cell, int columnCount, int i, int rowIndex,
			boolean bottomLeftColored, int coloredCellCode) {
		stringBuilder.append(DISPLAY_SEPARATOR.repeat(2));

		if (i % 2 == coloredCellCode) {
			stringBuilder.append(DISPLAY_SEPARATOR);
			final int index = CoordinateUtils.coordinateToIndex(columnCount, new Coordinate(rowIndex, i));
			if (index < 10) {
				stringBuilder.append(DISPLAY_SEPARATOR);
			}
			stringBuilder.append(index);
		} else {
			/**
			 * The columns won't line up, if the board dimension are > 10.
			 */
			stringBuilder.append(DISPLAY_SEPARATOR.repeat(3));
		}
		stringBuilder.append(DISPLAY_SEPARATOR);
	}
}
