package com.earl.nbyncheckers.displayers.impl;

import org.springframework.stereotype.Service;

import com.earl.nbynboard.Checker;
import com.earl.nbynboard.UnrecognizedCheckerColorException;
import com.earl.nbynboard.UnrecognizedCheckerRankException;
import com.earl.nbyncheckers.displayers.ColoredConsoleCheckerDisplayer;

/**
 * 
 * @author earlharris
 *
 */
@Service
public class ColoredConsoleCheckerDisplayerIimpl implements ColoredConsoleCheckerDisplayer {

	public static final String BLACK_DISPLAY_STRING = "b";
	public static final String RED_DISPLAY_STRING = "r";

	/**
	 * 
	 * The string version of a Cell that is suitable for a color console.
	 * 
	 * @param checker
	 * @return String
	 */
	@Override
	public String display(Checker checker) {
		String intermediary;
		switch (checker.getCheckerColor()) {
		case RED:
			intermediary = RED_DISPLAY_STRING;
			break;
		case BLACK:
			intermediary = BLACK_DISPLAY_STRING;
			break;
		default:
			throw new UnrecognizedCheckerColorException(checker.getCheckerColor());
		}
		String result;
		switch (checker.getCheckerRank()) {
		case REGULAR:
			result = intermediary;
			break;
		case KING:
			result = intermediary.toUpperCase();
			break;
		default:
			throw new UnrecognizedCheckerRankException(checker.getCheckerRank());
		}
		return result;
	}
}
