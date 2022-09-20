package com.earl.nbyngamerules.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.earl.nbynboard.Board;
import com.earl.nbynboard.BoardCreator;
import com.earl.nbynboard.Cell;
import com.earl.nbynboard.Checker;
import com.earl.nbynboard.CheckerColor;
import com.earl.nbynboard.CheckerRank;
import com.earl.nbyngamerules.AccessedIllegalCellException;
import com.earl.nbyngamerules.GameRules;
import com.earl.utilities.Coordinate;

class TestGameRulesImpl {

	private static final Checker RED_REGULAR = new Checker(CheckerColor.RED, CheckerRank.REGULAR);
//	private static final Checker BLACK_REGULAR = new Checker(CheckerColor.BLACK, CheckerRank.REGULAR);
	private static final Checker RED_KING = new Checker(CheckerColor.RED, CheckerRank.KING);
	private static final Checker BLACK_KING = new Checker(CheckerColor.BLACK, CheckerRank.KING);

	private static final int ROW_COUNT = 2;
	private static final int COLUMN_COUNT = 6;

	private static final Board EMPTY_BOARD = new Board(0, 3, true);

	private final Board board = new Board(ROW_COUNT, COLUMN_COUNT, true);

	private final GameRules gameRules = new GameRulesImpl();
	private final BoardCreator boardCreator = new BoardCreator();

	@BeforeEach
	public void initEach() {
		board.get(0).set(2, new Cell(RED_REGULAR));
		board.get(1).set(2, new Cell(RED_KING)); // assign to illegal cell.
	}

	@Test
	void testGetOpponent() {
		assertEquals(CheckerColor.BLACK, gameRules.getOpponent(CheckerColor.RED));
		assertEquals(CheckerColor.RED, gameRules.getOpponent(CheckerColor.BLACK));
	}

	@Test
	void testIsWinner() {
		assertTrue(gameRules.isWinner(board, CheckerColor.RED));
		assertFalse(gameRules.isWinner(board, CheckerColor.BLACK));
		assertTrue(gameRules.isWinner(EMPTY_BOARD, CheckerColor.RED));
		assertTrue(gameRules.isWinner(EMPTY_BOARD, CheckerColor.BLACK));

	}

	@Test
	void testGet() {
		assertEquals(new Cell(RED_REGULAR), gameRules.get(board.get(0), 2));

		AccessedIllegalCellException accessedIllegalCellException = assertThrows(AccessedIllegalCellException.class,
				() -> {
					gameRules.get(board.get(1), 2);
				});
		assertEquals(board.get(1), accessedIllegalCellException.getBoardRow());
		assertEquals(2, accessedIllegalCellException.getI());
	}

	@Test
	void testSet() {
		gameRules.set(board.get(0), 2, new Cell(BLACK_KING));
		assertEquals(board.get(0).get(2), new Cell(BLACK_KING));
		AccessedIllegalCellException accessedIllegalCellException = assertThrows(AccessedIllegalCellException.class,
				() -> {
					gameRules.set(board.get(1), 2, new Cell(BLACK_KING));
				});
		assertEquals(board.get(1), accessedIllegalCellException.getBoardRow());
		assertEquals(2, accessedIllegalCellException.getI());
	}

	@Test
	void testSet2() {
		gameRules.set(board, new Coordinate(0, 2), new Cell(BLACK_KING));
		assertEquals(board.get(0).get(2), new Cell(BLACK_KING));
		AccessedIllegalCellException accessedIllegalCellException = assertThrows(AccessedIllegalCellException.class,
				() -> {
					gameRules.set(board, new Coordinate(1, 2), new Cell(BLACK_KING));
				});
		assertEquals(board.get(1), accessedIllegalCellException.getBoardRow());
		assertEquals(2, accessedIllegalCellException.getI());
	}

	@Test
	void testInitialize() {
		Board board = new Board(4, 4, true);
		Board board1 = boardCreator.create(new String[] { //
				" b b", //
				"    ", //
				"    ", //
				"r r " //
		}, true);

		gameRules.initialize(board, 1);

		assertTrue(board.matches(board1));

	}

	@Test
	void testInitialize2() {
		Board board = new Board(4, 4, true);
		gameRules.set(board, new Coordinate(1, 1), new Cell(BLACK_KING));
		Board board1 = boardCreator.create(new String[] { //
				" b b", //
				"    ", //
				"    ", //
				"r r " //
		}, true);

		gameRules.initialize(board, 1);

		assertTrue(board.matches(board1));

	}
}
