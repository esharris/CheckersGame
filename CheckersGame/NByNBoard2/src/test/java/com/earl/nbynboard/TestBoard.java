package com.earl.nbynboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.earl.utilities.Coordinate;

class TestBoard {

	private static final Checker RED_REGULAR = new Checker(CheckerColor.RED, CheckerRank.REGULAR);
//	private static final Checker BLACK_REGULAR = new Checker(CheckerColor.BLACK, CheckerRank.REGULAR);
	private static final Checker RED_KING = new Checker(CheckerColor.RED, CheckerRank.KING);
	private static final Checker BLACK_KING = new Checker(CheckerColor.BLACK, CheckerRank.KING);

	private static final int ODD_COLUMN_COUNT = 3;

	private static final int ROW_COUNT = 2;
	private static final int COLUMN_COUNT = 6;

	private static final Board EMPTY_BOARD = new Board(0, 3, true);

	private final Board board = new Board(ROW_COUNT, COLUMN_COUNT, true);

	private final Board board1 = new Board(3, 4, true);

	@BeforeEach
	public void initEach() {
		board1.get(0).set(2, new Cell(RED_REGULAR));
		board1.get(2).set(2, new Cell(BLACK_KING));
	}

	@Test
	void testConstructor() {
		OddColumnLengthException oddColumnLengthException = assertThrows(OddColumnLengthException.class, () -> {
			new Board(2, ODD_COLUMN_COUNT, false);
		});
		assertEquals(ODD_COLUMN_COUNT, oddColumnLengthException.getColumnCount());
	}

	@Test
	void testRowCount() {
		assertEquals(ROW_COUNT, board.getRowCount());
		assertEquals(0, EMPTY_BOARD.getRowCount());
		assertEquals(3, board1.getRowCount());
	}

	@Test
	void testCoumnCount() {
		assertEquals(COLUMN_COUNT, board.getColumnCount());
		assertEquals(0, EMPTY_BOARD.getColumnCount());
		assertEquals(4, board1.getColumnCount());
	}

	@Test
	void testContains() {
		assertFalse(EMPTY_BOARD.contains(Coordinate.ORIGIN));
		assertTrue(board.contains(Coordinate.ORIGIN));
		assertTrue(board.contains(new Coordinate(ROW_COUNT - 1, COLUMN_COUNT - 1)));
		assertFalse(board.contains(new Coordinate(ROW_COUNT, COLUMN_COUNT - 1)));
		assertFalse(board.contains(new Coordinate(ROW_COUNT - 1, COLUMN_COUNT)));
		assertFalse(board.contains(new Coordinate(-1, COLUMN_COUNT - 1)));
		assertFalse(board.contains(new Coordinate(ROW_COUNT - 1, -1)));
	}

	@Test
	void testOccurrences() {
		board1.get(1).set(2, new Cell(RED_KING));

		assertEquals(2, board1.occurrences(CheckerColor.RED));
		assertEquals(1, board1.occurrences(CheckerColor.BLACK));
		assertEquals(0, EMPTY_BOARD.occurrences(CheckerColor.BLACK));
		assertEquals(0, EMPTY_BOARD.occurrences(CheckerColor.RED));
	}

	@Test
	void testMatch() {
		final Board board2 = new Board(3, 4, true);
		final Board board3 = new Board(3, 4, true);
		final Board board4 = new Board(3, 4, false); // bottom left color different from board1

		board2.get(0).set(2, new Cell(new Checker(CheckerColor.RED, CheckerRank.REGULAR)));
		board2.get(2).set(2, new Cell(BLACK_KING));

		board3.get(1).set(2, new Cell(new Checker(CheckerColor.RED, CheckerRank.REGULAR)));
		board3.get(2).set(2, new Cell(BLACK_KING));

		board4.get(0).set(2, new Cell(new Checker(CheckerColor.RED, CheckerRank.REGULAR)));
		board4.get(2).set(2, new Cell(BLACK_KING));

		assertTrue(board1.matches(board2));
		assertTrue(board1.matches(board1));
		assertTrue(board2.matches(board1));
		assertFalse(board1.matches(board3));
		assertFalse(board1.matches(board4));
	}

	@Test
	void testCopy() {
		final Board board2 = board1.copy();
		final Board emptyBoard = new Board(0, 10, true);
		final Board emptyBoard1 = emptyBoard.copy();

		assertFalse(board1 == board2); // different objects
		assertTrue(board1.matches(board2));

		assertFalse(emptyBoard == emptyBoard1);
		assertTrue(emptyBoard.matches(emptyBoard1));
	}

	@Test
	void testFlip() {
		final Board board5 = board1.flip();
		final Board board6 = new Board(3, 4, false);
		final Board emptyBoard1 = EMPTY_BOARD.flip();

		board6.get(2).set(1, new Cell(RED_REGULAR));
		board6.get(0).set(1, new Cell(BLACK_KING));

		assertEquals(0, emptyBoard1.getRowCount());
		assertEquals(0, emptyBoard1.getColumnCount());
		assertTrue(emptyBoard1.isBottomLeftColored());

		assertTrue(board5.matches(board6));
	}
}
