package com.earl.nbynboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TestCell {

	private static final Checker CHECKER = new Checker(CheckerColor.BLACK, CheckerRank.KING);
	private static final Cell NON_EMPTY_CELL = new Cell(CHECKER);

	@Test
	void tesIsOccupiedt() {
		assertFalse(Cell.UNOCCUPIED_CELL.isOccupied());
		assertTrue(NON_EMPTY_CELL.isOccupied());
	}

	@Test
	void testGetChecker() {
		assertEquals(CHECKER, NON_EMPTY_CELL.getChecker());
	}

	@Test
	void testFailedGetChecker() {
		assertThrows(CellNotOccupiedException.class, () -> {
			Cell.UNOCCUPIED_CELL.getChecker();
		});
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	void testEquals() {
		final Cell emptyCell = new Cell();

		assertFalse(Cell.UNOCCUPIED_CELL == emptyCell); // different objects
		assertTrue(Cell.UNOCCUPIED_CELL.equals(emptyCell));
		assertTrue(emptyCell.equals(Cell.UNOCCUPIED_CELL));
		assertEquals(emptyCell.hashCode(), Cell.UNOCCUPIED_CELL.hashCode());

		assertTrue(NON_EMPTY_CELL.equals(NON_EMPTY_CELL));
		{
			final Checker checker = new Checker(CheckerColor.BLACK, CheckerRank.KING);
			final Cell nonEmptyCell = new Cell(checker);

			assertTrue(NON_EMPTY_CELL.equals(nonEmptyCell));
			assertTrue(nonEmptyCell.equals(NON_EMPTY_CELL));
			assertEquals(nonEmptyCell.hashCode(), NON_EMPTY_CELL.hashCode());

			assertFalse(NON_EMPTY_CELL.equals(Cell.UNOCCUPIED_CELL));
			assertFalse(Cell.UNOCCUPIED_CELL.equals(NON_EMPTY_CELL));

			assertFalse(NON_EMPTY_CELL.equals(null));
			assertFalse(NON_EMPTY_CELL.equals(55));
		}
	}
}
