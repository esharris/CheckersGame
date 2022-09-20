package com.earl.nbyncheckers.displayers.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.earl.nbynboard.Checker;
import com.earl.nbynboard.CheckerColor;
import com.earl.nbynboard.CheckerRank;
import com.earl.nbyncheckers.displayers.ColoredConsoleCheckerDisplayer;

class TestColoredConsoleCheckerDisplayer {

	private static final Checker RED_REGULAR = new Checker(CheckerColor.RED, CheckerRank.REGULAR);
	private static final Checker BLACK_REGULAR = new Checker(CheckerColor.BLACK, CheckerRank.REGULAR);
	private static final Checker RED_KING = new Checker(CheckerColor.RED, CheckerRank.KING);
	private static final Checker BLACK_KING = new Checker(CheckerColor.BLACK, CheckerRank.KING);

	private static final ColoredConsoleCheckerDisplayer coloredConsoleCheckerDisplayerIimpl = //
			new ColoredConsoleCheckerDisplayerIimpl();

	@Test
	void testDisplay() {
		assertEquals(ColoredConsoleCheckerDisplayerIimpl.RED_DISPLAY_STRING,
				coloredConsoleCheckerDisplayerIimpl.display(RED_REGULAR));
		assertEquals(ColoredConsoleCheckerDisplayerIimpl.BLACK_DISPLAY_STRING,
				coloredConsoleCheckerDisplayerIimpl.display(BLACK_REGULAR));
		assertEquals(ColoredConsoleCheckerDisplayerIimpl.RED_DISPLAY_STRING.toUpperCase(),
				coloredConsoleCheckerDisplayerIimpl.display(RED_KING));
		assertEquals(ColoredConsoleCheckerDisplayerIimpl.BLACK_DISPLAY_STRING.toUpperCase(),
				coloredConsoleCheckerDisplayerIimpl.display(BLACK_KING));
	}

}
