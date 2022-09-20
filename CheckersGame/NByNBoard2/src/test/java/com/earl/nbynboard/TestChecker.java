package com.earl.nbynboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TestChecker {

	private static final Checker RED_REGULAR = new Checker(CheckerColor.RED, CheckerRank.REGULAR);
	private static final Checker BLACK_REGULAR = new Checker(CheckerColor.BLACK, CheckerRank.REGULAR);
	private static final Checker RED_KING = new Checker(CheckerColor.RED, CheckerRank.KING);
//	private static final Checker BLACK_KING = new Checker(CheckerColor.BLACK, CheckerRank.KING);

	@SuppressWarnings("unlikely-arg-type")
	@Test
	void testEquals() {
		final Checker checker = new Checker(CheckerColor.BLACK, CheckerRank.REGULAR);

		assertFalse(BLACK_REGULAR == checker); // different objects
		assertTrue(BLACK_REGULAR.equals(checker));
		assertTrue(checker.equals(BLACK_REGULAR));
		assertEquals(BLACK_REGULAR.hashCode(), checker.hashCode());

		assertTrue(RED_REGULAR.equals(RED_REGULAR));
		assertFalse(RED_KING.equals(null));
		assertFalse(RED_KING.equals("R"));
		assertFalse(RED_KING.equals(23.5));
		assertFalse(RED_KING.equals(RED_REGULAR));
	}
}
