package com.earl.nbyncheckers.displayers;

import com.earl.nbynboard.Checker;

/**
 * 
 * @author earlharris
 *
 */
public interface ColoredConsoleCheckerDisplayer {

	/**
	 * 
	 * The string version of a Cell that is suitable for a color console.
	 * 
	 * @param checker
	 * @return String
	 */
	String display(Checker checker);

}