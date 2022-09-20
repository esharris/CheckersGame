package com.earl.nbyncheckers.displayers.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.earl.nbynboard.Cell;
import com.earl.nbynboard.Checker;
import com.earl.nbynboard.CheckerColor;
import com.earl.nbynboard.CheckerRank;
import com.earl.nbyncheckers.displayers.ColoredConsoleCellDisplayer;
import com.earl.nbyncheckers.displayers.ColoredConsoleCheckerDisplayer;

class TestColoredConsoleCellDisplayer {

	private static final Checker CHECKER = new Checker(CheckerColor.BLACK, CheckerRank.KING);
	private static final Cell NON_EMPTY_CELL = new Cell(CHECKER);

	private static final ColoredConsoleCheckerDisplayer coloredConsoleCheckerDisplayerIimpl = //
			new ColoredConsoleCheckerDisplayerIimpl();
	private static final ColoredConsoleCellDisplayer coloredConsoleCellDisplayerImpl = //
			new ColoredConsoleCellDisplayerImpl(coloredConsoleCheckerDisplayerIimpl);

	@Test
	void testDisplay() {
		final Checker checker = new Checker(CheckerColor.RED, CheckerRank.REGULAR);

		assertEquals(" ", coloredConsoleCellDisplayerImpl.display(Cell.UNOCCUPIED_CELL));
		assertEquals(ColoredConsoleCheckerDisplayerIimpl.BLACK_DISPLAY_STRING.toUpperCase(),
				coloredConsoleCellDisplayerImpl.display(NON_EMPTY_CELL));
		assertEquals("r", coloredConsoleCellDisplayerImpl.display(new Cell(checker)));
	}

}
