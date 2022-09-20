package com.earl.nbyncheckers.displayers;

import com.earl.nbynboard.BoardRow;

/**
 * 
 * @author earlharris
 *
 */
public interface ColoredConsoleBoardRowDisplayer {

	/**
	 * 
	 * The string version of a BoardRow that is suitable for a color console.
	 * 
	 * @param boardRow
	 * @return String
	 */
	String display(BoardRow boardRow);

}