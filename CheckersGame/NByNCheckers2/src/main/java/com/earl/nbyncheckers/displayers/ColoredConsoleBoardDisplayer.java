package com.earl.nbyncheckers.displayers;

import com.earl.nbynboard.Board;

/**
 * 
 * @author earlharris
 *
 */
public interface ColoredConsoleBoardDisplayer {

	/**
	 * 
	 * The string version of a Board that is suitable for a color console.
	 * 
	 * @param board
	 * 
	 * @return String
	 */
	String display(Board board);

}