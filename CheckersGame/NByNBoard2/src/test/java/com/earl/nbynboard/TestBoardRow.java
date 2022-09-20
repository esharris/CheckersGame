package com.earl.nbynboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestBoardRow {

	private static final Checker RED_REGULAR = new Checker(CheckerColor.RED, CheckerRank.REGULAR);
	private static final Checker BLACK_REGULAR = new Checker(CheckerColor.BLACK, CheckerRank.REGULAR);
//	private static final Checker RED_KING = new Checker(CheckerColor.RED, CheckerRank.KING);
	private static final Checker BLACK_KING = new Checker(CheckerColor.BLACK, CheckerRank.KING);

	private static final int ODD_COLUMN_COUNT = 3;

	private static final int COLUMN_COUNT = 6;

	private final BoardRow boardRow = new BoardRow(COLUMN_COUNT, 0, true);
	private final BoardRow emptyRow = new BoardRow(0, 0, true);

	@BeforeEach
	public void initEach() {
		boardRow.set(0, new Cell(RED_REGULAR));
		boardRow.set(1, new Cell(BLACK_KING));
	}

	@Test
	void testConstructor() {
		OddColumnLengthException oddColumnLengthException = assertThrows(OddColumnLengthException.class, () -> {
			new BoardRow(ODD_COLUMN_COUNT, 0, false);
		});
		assertEquals(ODD_COLUMN_COUNT, oddColumnLengthException.getColumnCount());
	}

	@Test
	void testColoredCellCode() {
		final BoardRow boardRow1 = new BoardRow(COLUMN_COUNT, 2, false);

		assertEquals(0, boardRow.coloredCellCode());
		assertEquals(1, boardRow1.coloredCellCode());
	}

	@Test
	void testOccurrences() {
		boardRow.set(2, new Cell(BLACK_REGULAR));

		assertEquals(1, boardRow.occurrences(CheckerColor.RED));
		assertEquals(2, boardRow.occurrences(CheckerColor.BLACK));
	}

	@Test
	void testMatch() {
		final BoardRow boardRow1 = new BoardRow(COLUMN_COUNT, 0, true);
		final BoardRow boardRow2 = new BoardRow(COLUMN_COUNT, 0, true);
		final BoardRow boardRow3 = new BoardRow(4, 0, true);
		final BoardRow boardRow4 = new BoardRow(COLUMN_COUNT, 3, true);

		boardRow1.set(0, new Cell(RED_REGULAR));
		boardRow1.set(1, new Cell(BLACK_KING));

		boardRow2.set(0, new Cell(RED_REGULAR));
		boardRow2.set(2, new Cell(BLACK_KING));

		boardRow3.set(0, new Cell(RED_REGULAR));
		boardRow3.set(1, new Cell(BLACK_KING));

		boardRow4.set(0, new Cell(RED_REGULAR));
		boardRow4.set(1, new Cell(BLACK_KING));

		assertTrue(boardRow.matches(boardRow));

		assertTrue(boardRow.matches(boardRow1));
		assertTrue(boardRow1.matches(boardRow));

		assertFalse(boardRow.matches(boardRow2));
		assertFalse(boardRow3.matches(boardRow)); // different sizes

		assertFalse(boardRow.matches(boardRow4));
	}

	@Test
	void testCopy() {

		final BoardRow boardRow1 = boardRow.copy();
		final BoardRow emptyRow1 = emptyRow.copy();

		assertFalse(boardRow == boardRow1);
		assertTrue(boardRow.matches(boardRow1));
		assertTrue(boardRow1.matches(boardRow));

		assertFalse(emptyRow == emptyRow1);
		assertTrue(emptyRow.matches(emptyRow1));

	}

	@Test
	void testFlip() {
		final int newIndex = 3;
		final BoardRow boardRow1 = new BoardRow(COLUMN_COUNT, newIndex, true);
		final BoardRow emptyRow1 = emptyRow.flip(0, true);

		boardRow1.set(5, new Cell(RED_REGULAR));
		boardRow1.set(4, new Cell(BLACK_KING));

		assertTrue(boardRow.flip(newIndex, true).matches(boardRow1));

		assertEquals(0, emptyRow1.getColumnCount());
		assertEquals(0, emptyRow1.getIndex());
	}
}
