package com.earl.nbynboard;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TestBoardCreator {

	private final BoardCreator boardCreator = new BoardCreator();

	@Test
	void testInitialize() {
		Board board = new Board(4, 4, true);

		board.get(0).set(0, new Cell(new Checker(CheckerColor.RED, CheckerRank.REGULAR)));
		board.get(0).set(2, new Cell(new Checker(CheckerColor.RED, CheckerRank.KING)));
		board.get(3).set(1, new Cell(new Checker(CheckerColor.BLACK, CheckerRank.KING)));
		board.get(3).set(3, new Cell(new Checker(CheckerColor.BLACK, CheckerRank.REGULAR)));

		Board board1 = boardCreator.create(new String[] { //
				" B b", //
				"    ", //
				"    ", //
				"r R " //
		}, true);

		assertTrue(board.matches(board1));

	}

}
