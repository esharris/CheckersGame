package com.earl.nbyngamerules;

import com.earl.nbynboard.Board;
import com.earl.nbynboard.CheckerColor;
import com.earl.utilities.Coordinate;

/**
 * 
 * @author earlharris
 */
public interface MoveManager {

	/**
	 * 
	 * @param board
	 * @param coordinate
	 * @param checkerColor
	 * @return can the checker on the board at location coordinate jump a man?
	 * @throws CoordinateNotOnBoardExcepton, if coordinate is not on the board.
	 * @throws UnexpectedCellException       The cell on the board at location
	 *                                       coordinate is empty of has a checker
	 *                                       whose color is not checkerColor.
	 */
	boolean canMakeAJump(Board board, Coordinate coordinate, CheckerColor checkerColor) throws UnexpectedCellException;

	/**
	 * 
	 * @param board
	 * @param checkerColor
	 * @return Is there a checker (of color checkerColor) on the board that can jump
	 *         a man?
	 */
	public boolean thereIsAJump(Board board, CheckerColor checkerColor);

	/**
	 * 
	 * @param board
	 * @param coordinate
	 * @param checkerColor
	 * @param destinationCoordinate
	 * @throws UnexpectedCellException       The cell on the board at location
	 *                                       coordinate is empty of has a checker
	 *                                       whose color is not checkerColor.
	 * @throws IncorrectDestinationException coordinate and destinationCoordinate
	 *                                       are too far apart.
	 * @throws CellIsOccupiedException
	 * 
	 *                                       A regular checkers can only move
	 *                                       forward left or right, if there is an
	 *                                       available space.
	 * 
	 *                                       A king checker can move forward or
	 *                                       backward, to the right or right, if
	 *                                       there is an available space.
	 * 
	 *                                       If a regular piece crosses the board,
	 *                                       it becomes a king.
	 * 
	 *                                       Before invoking this, make sure there
	 *                                       is no opponent that can be jumped.
	 */
	void move(Board board, Coordinate coordinate, CheckerColor checkerColor, Coordinate destinationCoordinate)
			throws UnexpectedCellException, IncorrectDestinationException, CellIsOccupiedException;

        /**
         * 
         * @param board
         * @param coordinate
         * @param checkerColor
         * @param destinationCoordinate
         * @throws UnexpectedCellException
         * @throws IncorrectDestinationException
         * @throws CellIsOccupiedException 
         */
	void jump(Board board, Coordinate coordinate, CheckerColor checkerColor, Coordinate destinationCoordinate)
			throws UnexpectedCellException, IncorrectDestinationException, CellIsOccupiedException;

        /**
         * 
         * @return 
         */
	GameRules getGameRules();

}