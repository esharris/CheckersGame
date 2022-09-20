package com.earl.nbyngamerules;

import com.earl.nbynboard.Board;
import com.earl.nbynboard.BoardRow;
import com.earl.nbynboard.Cell;
import com.earl.nbynboard.CheckerColor;
import com.earl.utilities.Coordinate;

public interface GameRules {

	CheckerColor getOpponent(CheckerColor checkerColor);

	boolean isWinner(Board board, CheckerColor checkerColor);

	/**
	 * 
	 * @param boardRow
	 * @param i
	 * @return the ith element in boardRow, if the ith element is in a legal cell.
	 */
	Cell get(BoardRow boardRow, int i);

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
	void set(BoardRow boardRow, int i, Cell cell);

	/**
	 * 
	 * @param board
	 * @param coordinate
	 * @return the cell associated with the coordinate, provided the cell is at a
	 *         legal coordinate.
	 * @throws AccessedIllegalCellException if the coordinate.getY() represents an
	 *                                      illegal cell.
	 */
	Cell get(Board board, Coordinate coordinate);

	/**
	 * 
	 * @param board
	 * @param coordinate
	 * @param cell
	 * @throws AccessedIllegalCellException if the coordinate.getY() represents an
	 *                                      illegal cell.
	 */
	void set(Board board, Coordinate coordinate, Cell cell);

	/**
	 * Put CheckerColor.RED checkers in each legal space in the first rowNum rows.
	 * Put CheckerColor.BLACK checkers in each legal space in the last rowNum rows.
	 * 
	 * @param board
	 * @param rowNum
	 * @throws RuntimeException if rowNum >= board.getRowCount() / 2 + 1
	 */
	void initialize(Board board, int rowNum);

}