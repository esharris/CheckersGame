package com.earl.nbyngamerules.impl;

import org.springframework.stereotype.Service;

import com.earl.nbynboard.Board;
import com.earl.nbynboard.BoardRow;
import com.earl.nbynboard.Cell;
import com.earl.nbynboard.Checker;
import com.earl.nbynboard.CheckerColor;
import com.earl.nbynboard.CheckerRank;
import com.earl.nbynboard.UnrecognizedCheckerColorException;
import com.earl.nbyngamerules.AccessedIllegalCellException;
import com.earl.nbyngamerules.GameRules;
import com.earl.utilities.Coordinate;

/**
 * 
 * @author earlharris
 *
 */
@Service
public class GameRulesImpl implements GameRules {

	@Override
	public CheckerColor getOpponent(CheckerColor checkerColor) {
		CheckerColor result;
		switch (checkerColor) {
		case RED:
			result = CheckerColor.BLACK;
			break;
		case BLACK:
			result = CheckerColor.RED;
			break;
		default:
			throw new UnrecognizedCheckerColorException(checkerColor);
		}
		return result;
	}

	@Override
	public boolean isWinner(Board board, CheckerColor checkerColor) {
		CheckerColor opponent = getOpponent(checkerColor);
		return board.occurrences(opponent) == 0;
	}

	/**
	 * 
	 * @param boardRow
	 * @param i
	 * @return the ith element in boardRow, if the ith element is in a legal cell.
	 */
	@Override
	public Cell get(BoardRow boardRow, int i) {
		final int coloredCellCode = boardRow.coloredCellCode();
		if (i % 2 == coloredCellCode) {
			return boardRow.get(i);
		} else {
			throw new AccessedIllegalCellException(boardRow, i);
		}
	}

	/**
	 * 
	 * @param boardRow
	 * @param i
	 * @param cell
	 * 
	 *                 Assign the ith element of boardRow with cell, provided the
	 *                 ith element is in a legal cell.
	 * @throws AccessedIllegalCellException if the i represents an illegal cell.
	 */
	@Override
	public void set(BoardRow boardRow, int i, Cell cell) {
		final int coloredCellCode = boardRow.coloredCellCode();
		if (i % 2 == coloredCellCode) {
			boardRow.set(i, cell);
		} else {
			throw new AccessedIllegalCellException(boardRow, i);
		}
	}

	/**
	 * 
	 * @param board
	 * @param coordinate
	 * @return the cell associated with the coordinate, provided the cell is at a
	 *         legal coordinate.
	 * @throws AccessedIllegalCellException if the coordinate.getY() represents an
	 *                                      illegal cell.
	 */
	@Override
	public Cell get(Board board, Coordinate coordinate) {
		int row = coordinate.getX();
		int column = coordinate.getY();
		return get(board.get(row), column);
	}

	/**
	 * 
	 * @param board
	 * @param coordinate
	 * @param cell
	 * @throws AccessedIllegalCellException if the coordinate.getY() represents an
	 *                                      illegal cell.
	 */
	@Override
	public void set(Board board, Coordinate coordinate, Cell cell) {
		int row = coordinate.getX();
		int column = coordinate.getY();
		set(board.get(row), column, cell);
	}

	/**
	 * Put CheckerColor.RED checkers in each legal space in the first rowNum rows.
	 * Put CheckerColor.BLACK checkers in each legal space in the last rowNum rows.
	 * 
	 * @param board
	 * @param rowNum
	 * @throws RuntimeException if rowNum >= board.getRowCount() / 2 + 1
	 */
	@Override
	public void initialize(Board board, int rowNum) {
		if (rowNum < board.getRowCount() / 2 + 1) {
			clear(board);
			for (int i = 0; i < rowNum; i++) {
				initialize(board.get(i), CheckerColor.RED);
			}
			for (int j = 0; j < rowNum; j++) {
				int i = board.getRowCount() - 1 - j;
				initialize(board.get(i), CheckerColor.BLACK);
			}
		} else {
			throw new RuntimeException("expected rowNumber less than half the row count minus one, but saw: rowNum = "
					+ rowNum + ", row count = " + board.getRowCount());
		}
	}

	private void clear(Board board) {
		for (int i = 0; i < board.getRowCount(); i++) {
			BoardRow boardRow = board.get(i);
			for (int j = boardRow.coloredCellCode(); j < board.getColumnCount(); j += 2) {
				set(boardRow, j, new Cell());
			}
		}
	}

	private void initialize(BoardRow boardRow, CheckerColor checkerColor) {
		/**
		 * You can only put a piece is a legal space.
		 */
		for (int i = boardRow.coloredCellCode(); i < boardRow.getColumnCount(); i += 2) {
			set(boardRow, i, new Cell(new Checker(checkerColor, CheckerRank.REGULAR)));
		}
	}
}
