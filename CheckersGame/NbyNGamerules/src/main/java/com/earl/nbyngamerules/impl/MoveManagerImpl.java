package com.earl.nbyngamerules.impl;

import org.springframework.stereotype.Service;

import com.earl.nbynboard.Board;
import com.earl.nbynboard.Cell;
import com.earl.nbynboard.Checker;
import com.earl.nbynboard.CheckerColor;
import com.earl.nbynboard.CheckerRank;
import com.earl.nbyngamerules.CellIsOccupiedException;
import com.earl.nbyngamerules.CoordinateNotOnBoardExcepton;
import com.earl.nbyngamerules.GameRules;
import com.earl.nbyngamerules.IncorrectDestinationException;
import com.earl.nbyngamerules.MoveManager;
import com.earl.nbyngamerules.UnexpectedCellException;
import com.earl.utilities.Coordinate;

@Service
public class MoveManagerImpl implements MoveManager {

	private final GameRules gameRules;

	public MoveManagerImpl(GameRules gameRules) {
		this.gameRules = gameRules;
	}

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
	@Override
	public boolean canMakeAJump(Board board, Coordinate coordinate, CheckerColor checkerColor)
			throws UnexpectedCellException {
		if (board.contains(coordinate)) {
			final Cell cell = gameRules.get(board, coordinate);
			if (cell.isOccupied() && cell.getChecker().getCheckerColor() == checkerColor) {
				return canMakeThisJump(board, coordinate, checkerColor, new Coordinate(1, 1)) || //
						canMakeThisJump(board, coordinate, checkerColor, new Coordinate(1, -1)) || //
						(cell.getChecker().getCheckerRank() == CheckerRank.KING
								&& canMakeThisJump(board, coordinate, checkerColor, new Coordinate(-1, -1)))
						|| //
						(cell.getChecker().getCheckerRank() == CheckerRank.KING
								&& canMakeThisJump(board, coordinate, checkerColor, new Coordinate(-1, 1)));
			} else { // status: ! cell.isOccupied() || cell.getChecker().getCheckerColor() !=
				// checkerColor
				throw new UnexpectedCellException(cell, coordinate, checkerColor, "");
			}
		} else { // status: ! board.isOnThe(coordinate0)
			throw new CoordinateNotOnBoardExcepton(board, coordinate, "");
		}
	}

	/**
	 * 
	 * @param board
	 * @param checkerColor
	 * @return Is there a checker (of color checkerColor) on the board that can jump
	 *         a man?
	 */
	@Override
	public boolean thereIsAJump(Board board, CheckerColor checkerColor) {
		boolean found = false;
		int i = 0;
		while (i < board.getRowCount() && !found) {
			int j = board.get(i).coloredCellCode();
			while (j < board.getColumnCount() && !found) {
				final Coordinate coordinate = new Coordinate(i, j);
				Cell cell = gameRules.get(board, coordinate);
				if (cell.isOccupied() && cell.getChecker().getCheckerColor() == checkerColor) {
					try {
						if (canMakeAJump(board, coordinate, checkerColor)) {
							found = true;
						} else {
							j += 2;
						}
					} catch (UnexpectedCellException e) {
						throw new RuntimeException("System error: ", e);
					}
				} else { // status: designate check color not there.
					j += 2;
				}
			}
			if (!found) {
				i++;
			}
		}
		return found;
	}

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
	@Override
	public void move(Board board, Coordinate coordinate, CheckerColor checkerColor, Coordinate destinationCoordinate)
			throws UnexpectedCellException, IncorrectDestinationException, CellIsOccupiedException {
		/**
		 * Though this method is a little long. But it makes more sense to leave this
		 * method as is. These segments should be visible together.
		 */
		if (board.contains(coordinate)) {
			final Cell cell = gameRules.get(board, coordinate);
			if (cell.isOccupied() && cell.getChecker().getCheckerColor() == checkerColor) {
				final Checker checker = cell.getChecker();
				if (board.contains(destinationCoordinate)) {
					final Coordinate change = destinationCoordinate.subtract(coordinate);

					final int changeX = change.getX();
					final int changeY = change.getY();
					if ((changeX == 1 || (checker.getCheckerRank() == CheckerRank.KING && changeX == -1))
							&& (changeY == 1 || changeY == -1)) {
						final Cell cell1 = gameRules.get(board, destinationCoordinate);
						if (!cell1.isOccupied()) {
							if (destinationCoordinate.getX() < board.getRowCount() - 1
									|| checker.getCheckerRank() == CheckerRank.KING) {
								gameRules.set(board, destinationCoordinate, cell);
							} else {
								gameRules.set(board, destinationCoordinate,
										new Cell(new Checker(checker.getCheckerColor(), CheckerRank.KING)));
							}
							gameRules.set(board, coordinate, Cell.UNOCCUPIED_CELL);
						} else {
							throw new CellIsOccupiedException(cell1, destinationCoordinate);
						}
					} else {
						throw new IncorrectDestinationException(checker, 1, coordinate, destinationCoordinate, changeX,
								changeY);
					}
				} else { // status: ! board.isOnThe(destinationCoordinate)
					throw new CoordinateNotOnBoardExcepton(board, destinationCoordinate, "destination");
				}
			} else { // status: ! cell.isOccupied() || cell.getChecker().getCheckerColor() !=
				// checkerColor
				throw new UnexpectedCellException(cell, coordinate, checkerColor, "");
			}
		} else { // status: ! board.isOnThe(coordinate0)
			throw new CoordinateNotOnBoardExcepton(board, coordinate, "");
		}
	}

	@Override
	public void jump(Board board, Coordinate coordinate, CheckerColor checkerColor, Coordinate destinationCoordinate)
			throws UnexpectedCellException, IncorrectDestinationException, CellIsOccupiedException {
		if (board.contains(coordinate)) {
			final Cell cell = gameRules.get(board, coordinate);
			if (cell.isOccupied() && cell.getChecker().getCheckerColor() == checkerColor) {
				final Checker checker = cell.getChecker();
				if (board.contains(destinationCoordinate)) {
					final Coordinate change = destinationCoordinate.subtract(coordinate);

					final int changeX = change.getX();
					final int changeY = change.getY();
					if ((changeX == 2 || (checker.getCheckerRank() == CheckerRank.KING && changeX == -2))
							&& (changeY == 2 || changeY == -2)) {
						final Cell cell1 = gameRules.get(board, destinationCoordinate);
						if (!cell1.isOccupied()) {
							final int halfChangeX = changeX / 2;
							final int halfChangeY = changeY / 2;
							final Coordinate midpointCoordinate = coordinate
									.add(new Coordinate(halfChangeX, halfChangeY));
							final Cell cell2 = gameRules.get(board, midpointCoordinate);
							if (cell2.isOccupied()
									&& cell2.getChecker().getCheckerColor() == gameRules.getOpponent(checkerColor)) {
								if (destinationCoordinate.getX() < board.getRowCount() - 1
										|| checker.getCheckerRank() == CheckerRank.KING) {
									gameRules.set(board, destinationCoordinate, cell);
									gameRules.set(board, midpointCoordinate, Cell.UNOCCUPIED_CELL);
									gameRules.set(board, coordinate, Cell.UNOCCUPIED_CELL);
								} else {
									gameRules.set(board, destinationCoordinate,
											new Cell(new Checker(checker.getCheckerColor(), CheckerRank.KING)));
									gameRules.set(board, midpointCoordinate, Cell.UNOCCUPIED_CELL);
									gameRules.set(board, coordinate, Cell.UNOCCUPIED_CELL);
								}
								gameRules.set(board, coordinate, Cell.UNOCCUPIED_CELL);

							} else {
								throw new UnexpectedCellException(cell2, midpointCoordinate,
										gameRules.getOpponent(checkerColor), "jumpee");
							}

						} else {
							throw new CellIsOccupiedException(cell1, destinationCoordinate);
						}
					} else {
						throw new IncorrectDestinationException(checker, 2, coordinate, destinationCoordinate, changeX,
								changeY);
					}
				} else { // status: ! board.isOnThe(destinationCoordinate)
					throw new CoordinateNotOnBoardExcepton(board, destinationCoordinate, "destination");
				}
			} else { // status: ! cell.isOccupied() || cell.getChecker().getCheckerColor() !=
				// checkerColor
				throw new UnexpectedCellException(cell, coordinate, checkerColor, "jumper");
			}
		} else { // status: ! board.isOnThe(coordinate0)
			throw new CoordinateNotOnBoardExcepton(board, coordinate, "");
		}
	}

	private boolean canMakeThisJump(Board board, Coordinate coordinate, CheckerColor checkerColor, Coordinate delta)
			throws UnexpectedCellException {
		boolean result;
		if (board.contains(coordinate)) {
			final Cell cell = gameRules.get(board, coordinate);
			if (cell.isOccupied() && cell.getChecker().getCheckerColor() == checkerColor) {
				Coordinate coordinate2 = coordinate.add(delta.mult(2));
				if (board.contains(coordinate2)) {
					Cell cell2 = gameRules.get(board, coordinate2);
					if (!cell2.isOccupied()) {
						Coordinate coordinate1 = coordinate.add(delta);
						Cell cell1 = gameRules.get(board, coordinate1);
						result = cell1.isOccupied()
								&& cell1.getChecker().getCheckerColor() == gameRules.getOpponent(checkerColor);

					} else {
						result = false;
					}
				} else { // status: ! board.isOnThe(coordinate2)
					result = false;
				}
				return result;
			} else { // status: ! cell.isOccupied() || cell.getChecker().getCheckerColor() !=
				// checkerColor
				throw new UnexpectedCellException(cell, coordinate, checkerColor, "");
			}
		} else { // status: ! board.isOnThe(coordinate0)
			throw new CoordinateNotOnBoardExcepton(board, coordinate, "");
		}
	}

	@Override
	public GameRules getGameRules() {
		return gameRules;
	}
}
