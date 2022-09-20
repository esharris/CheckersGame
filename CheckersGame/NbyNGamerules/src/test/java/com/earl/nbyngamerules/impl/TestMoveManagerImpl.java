
package com.earl.nbyngamerules.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.earl.nbynboard.Board;
import com.earl.nbynboard.BoardCreator;
import com.earl.nbynboard.Cell;
import com.earl.nbynboard.Checker;
import com.earl.nbynboard.CheckerColor;
import com.earl.nbynboard.CheckerRank;
import com.earl.nbyngamerules.CellIsOccupiedException;
import com.earl.nbyngamerules.CoordinateNotOnBoardExcepton;
import com.earl.nbyngamerules.IncorrectDestinationException;
import com.earl.nbyngamerules.UnexpectedCellException;
import com.earl.utilities.Coordinate;

class TestMoveManagerImpl {

	private final BoardCreator boardCreator = new BoardCreator();

	private final MoveManagerImpl moveManger2 = new MoveManagerImpl(new GameRulesImpl());

	private final Board board = boardCreator.create(new String[] { //
			" b B", //
			"  R ", //
			"    ", //
			"    ", //
			" b  ", //
			"r r " //
	}, true);

	private final Board board1 = boardCreator.create(new String[] { //
			" b B", //
			"    ", //
			"    ", //
			"  b ", //
			"    ", //
			"r r " //
	}, true);

	@Test
	void testCanMakeAJump() throws UnexpectedCellException {
		final Coordinate coordinate = new Coordinate(25, 25); // out of range

		final CoordinateNotOnBoardExcepton coordinateNotOnBoardException = assertThrows(
				CoordinateNotOnBoardExcepton.class, () -> {
					moveManger2.canMakeAJump(board, coordinate, CheckerColor.BLACK);
				});
		assertEquals(coordinate, coordinateNotOnBoardException.getCoordinate());

		final UnexpectedCellException unexpectedCellException = assertThrows(UnexpectedCellException.class, () -> {
			moveManger2.canMakeAJump(board, new Coordinate(1, 1), CheckerColor.RED); // should be black
		});
		assertEquals(CheckerColor.RED, unexpectedCellException.getCheckerColor());
		final Cell cell = unexpectedCellException.getCell();
		assertTrue(cell.isOccupied());
		if (cell.isOccupied()) {
			assertEquals(CheckerColor.BLACK, cell.getChecker().getCheckerColor());
		}

		assertTrue(moveManger2.canMakeAJump(board, new Coordinate(0, 0), CheckerColor.RED));
		assertTrue(moveManger2.canMakeAJump(board, new Coordinate(0, 2), CheckerColor.RED));
		assertFalse(moveManger2.canMakeAJump(board, new Coordinate(1, 1), CheckerColor.BLACK));
		assertTrue(moveManger2.canMakeAJump(board, new Coordinate(5, 3), CheckerColor.BLACK));
		assertFalse(moveManger2.canMakeAJump(board, new Coordinate(5, 1), CheckerColor.BLACK));
		assertFalse(moveManger2.canMakeAJump(board, new Coordinate(4, 2), CheckerColor.RED)); // alleged landing spaces
																								// are occupied
	}

	@Test
	void testThereIsAJump() {
		assertTrue(moveManger2.thereIsAJump(board, CheckerColor.RED));
		assertTrue(moveManger2.thereIsAJump(board, CheckerColor.BLACK));
		assertFalse(moveManger2.thereIsAJump(board1, CheckerColor.RED));
		assertFalse(moveManger2.thereIsAJump(board1, CheckerColor.BLACK));
	}

	@Test
	void testMove() throws UnexpectedCellException, IncorrectDestinationException, CellIsOccupiedException {
		final Board board2 = boardCreator.create(new String[] { //
				" b B", //
				"    ", //
				"   b", //
				"    ", //
				"    ", //
				"r r " //
		}, true);
		moveManger2.move(board1, new Coordinate(2, 2), CheckerColor.BLACK, new Coordinate(3, 3));
		assertTrue(board2.matches(board1));
	}

	@Test
	void testMove2() throws UnexpectedCellException, IncorrectDestinationException, CellIsOccupiedException {
		final Board board3 = boardCreator.create(new String[] { //
				" b  ", //
				"  B ", //
				"    ", //
				"  b ", //
				"    ", //
				"r r " //
		}, true);
		moveManger2.move(board1, new Coordinate(5, 3), CheckerColor.BLACK, new Coordinate(4, 2));
		assertTrue(board3.matches(board1));
	}

	@Test
	void testMove3() throws UnexpectedCellException, IncorrectDestinationException, CellIsOccupiedException {
		final Board board3 = boardCreator.create(new String[] { //
				" b  ", //
				"  b ", //
				"    ", //
				"  b ", //
				"    ", //
				"r r " //
		}, true);
		final Board board4 = boardCreator.create(new String[] { //
				" b B", //
				"    ", //
				"    ", //
				"  b ", //
				"    ", //
				"r r " //
		}, true);
		moveManger2.move(board3, new Coordinate(4, 2), CheckerColor.BLACK, new Coordinate(5, 3)); // The checker
																									// becomes a
																									// king.
		assertTrue(board4.matches(board3));
	}

	@Test
	void testMove4() throws UnexpectedCellException, IncorrectDestinationException, CellIsOccupiedException {
		{
			final CoordinateNotOnBoardExcepton coordinateNotOnBoardExcepton = assertThrows(
					CoordinateNotOnBoardExcepton.class, () -> {
						moveManger2.move(board1, new Coordinate(-1, 0), CheckerColor.BLACK, new Coordinate(3, 3));
					});
			assertEquals(new Coordinate(-1, 0), coordinateNotOnBoardExcepton.getCoordinate());
			assertEquals(board1, coordinateNotOnBoardExcepton.getBoard());
			assertEquals("", coordinateNotOnBoardExcepton.getName());
		}
		{
			final CoordinateNotOnBoardExcepton coordinateNotOnBoardExcepton = assertThrows(
					CoordinateNotOnBoardExcepton.class, () -> {
						moveManger2.move(board1, new Coordinate(2, 2), CheckerColor.BLACK, new Coordinate(3, 6));
					});
			assertEquals(new Coordinate(3, 6), coordinateNotOnBoardExcepton.getCoordinate());
			assertEquals(board1, coordinateNotOnBoardExcepton.getBoard());
			assertEquals("destination", coordinateNotOnBoardExcepton.getName());
		}
		{
			final UnexpectedCellException unexpectedCellException = assertThrows(UnexpectedCellException.class, () -> {
				moveManger2.move(board1, new Coordinate(2, 2), CheckerColor.RED, new Coordinate(3, 3));

			});
			assertEquals(CheckerColor.RED, unexpectedCellException.getCheckerColor());
			assertEquals(new Cell(new Checker(CheckerColor.BLACK, CheckerRank.REGULAR)),
					unexpectedCellException.getCell());
			assertEquals("", unexpectedCellException.getName());
		}
		{
			final IncorrectDestinationException incorrectDestinationException = assertThrows(
					IncorrectDestinationException.class, () -> {
						moveManger2.move(board1, new Coordinate(2, 2), CheckerColor.BLACK, new Coordinate(1, 1));
					});
			assertEquals(new Checker(CheckerColor.BLACK, CheckerRank.REGULAR),
					incorrectDestinationException.getChecker());
			assertEquals(1, incorrectDestinationException.getDistance());
			assertEquals(new Coordinate(2, 2), incorrectDestinationException.getCoordinate());
			assertEquals(new Coordinate(1, 1), incorrectDestinationException.getDestinationCoordinate());
		}
		{
			final Board board5 = boardCreator.create(new String[] { //
					" b B", //
					"    ", //
					"    ", //
					"    ", //
					" b  ", //
					"r r " //
			}, true);
			final CellIsOccupiedException cellIsOccupiedException = assertThrows(CellIsOccupiedException.class, () -> {
				moveManger2.move(board5, new Coordinate(0, 0), CheckerColor.RED, new Coordinate(1, 1));
			});
			assertEquals(new Cell(new Checker(CheckerColor.BLACK, CheckerRank.REGULAR)),
					cellIsOccupiedException.getCell());
			assertEquals(new Coordinate(1, 1), cellIsOccupiedException.getDestinationCoordinate());

		}
	}

	@Test
	void testJump() throws UnexpectedCellException, IncorrectDestinationException, CellIsOccupiedException {
		final Board board2 = boardCreator.create(new String[] { //
				" b B", //
				"  R ", //
				"    ", //
				"r   ", //
				"    ", //
				"r   " //
		}, true);
		moveManger2.jump(board, new Coordinate(0, 2), CheckerColor.RED, new Coordinate(2, 0));
		assertTrue(board2.matches(board));
	}

	@Test
	void testJump1() throws UnexpectedCellException, IncorrectDestinationException, CellIsOccupiedException {
		final Board board2 = boardCreator.create(new String[] { //
				" b  ", //
				"    ", //
				" B  ", //
				"    ", //
				" b  ", //
				"r r " //
		}, true);
		moveManger2.jump(board, new Coordinate(5, 3), CheckerColor.BLACK, new Coordinate(3, 1));
		assertTrue(board2.matches(board));
	}

	@Test
	void testJump2() throws UnexpectedCellException, IncorrectDestinationException, CellIsOccupiedException {
		{
			final CoordinateNotOnBoardExcepton coordinateNotOnBoardExcepton = assertThrows(
					CoordinateNotOnBoardExcepton.class, () -> {
						moveManger2.jump(board1, new Coordinate(-1, 0), CheckerColor.BLACK, new Coordinate(3, 3));
					});
			assertEquals(new Coordinate(-1, 0), coordinateNotOnBoardExcepton.getCoordinate());
			assertEquals(board1, coordinateNotOnBoardExcepton.getBoard());
			assertEquals("", coordinateNotOnBoardExcepton.getName());
		}
		{
			final CoordinateNotOnBoardExcepton coordinateNotOnBoardExcepton = assertThrows(
					CoordinateNotOnBoardExcepton.class, () -> {
						moveManger2.jump(board1, new Coordinate(2, 2), CheckerColor.BLACK, new Coordinate(3, 6));
					});
			assertEquals(new Coordinate(3, 6), coordinateNotOnBoardExcepton.getCoordinate());
			assertEquals(board1, coordinateNotOnBoardExcepton.getBoard());
			assertEquals("destination", coordinateNotOnBoardExcepton.getName());
		}
		{
			final UnexpectedCellException unexpectedCellException = assertThrows(UnexpectedCellException.class, () -> {
				moveManger2.jump(board1, new Coordinate(2, 2), CheckerColor.RED, new Coordinate(3, 3));

			});
			assertEquals(CheckerColor.RED, unexpectedCellException.getCheckerColor());
			assertEquals(new Cell(new Checker(CheckerColor.BLACK, CheckerRank.REGULAR)),
					unexpectedCellException.getCell());
			assertEquals("jumper", unexpectedCellException.getName());
		}
		{
			final IncorrectDestinationException incorrectDestinationException = assertThrows(
					IncorrectDestinationException.class, () -> {
						moveManger2.jump(board1, new Coordinate(2, 2), CheckerColor.BLACK, new Coordinate(1, 1));
					});
			assertEquals(new Checker(CheckerColor.BLACK, CheckerRank.REGULAR),
					incorrectDestinationException.getChecker());
			assertEquals(2, incorrectDestinationException.getDistance());
			assertEquals(new Coordinate(2, 2), incorrectDestinationException.getCoordinate());
			assertEquals(new Coordinate(1, 1), incorrectDestinationException.getDestinationCoordinate());
			assertEquals(-1, incorrectDestinationException.getChangeX());
			assertEquals(-1, incorrectDestinationException.getChangeY());
		}
		{
			final Board board5 = boardCreator.create(new String[] { //
					" b B", //
					"    ", //
					"    ", //
					"  r ", //
					" b  ", //
					"r r " //
			}, true);
			final CellIsOccupiedException cellIsOccupiedException = assertThrows(CellIsOccupiedException.class, () -> {
				moveManger2.jump(board5, new Coordinate(0, 0), CheckerColor.RED, new Coordinate(2, 2));
			});
			assertEquals(new Cell(new Checker(CheckerColor.RED, CheckerRank.REGULAR)),
					cellIsOccupiedException.getCell());
			assertEquals(new Coordinate(2, 2), cellIsOccupiedException.getDestinationCoordinate());

		}
		{
			final Board board5 = boardCreator.create(new String[] { //
					" b B", //
					"    ", //
					"    ", //
					"    ", //
					" r  ", //
					"r r " //
			}, true);
			final UnexpectedCellException unexpectedCellException = assertThrows(UnexpectedCellException.class, () -> {
				moveManger2.jump(board5, new Coordinate(0, 0), CheckerColor.RED, new Coordinate(2, 2));
			});
			assertEquals(new Cell(new Checker(CheckerColor.RED, CheckerRank.REGULAR)),
					unexpectedCellException.getCell());
			assertEquals(CheckerColor.BLACK, unexpectedCellException.getCheckerColor());
			assertEquals("jumpee", unexpectedCellException.getName());

		}
	}
}
