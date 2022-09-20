package com.earl.nbyncheckers.displayers;

import com.earl.nbynboard.Cell;

/**
 * 
 * @author earlharris
 *
 */
public interface ColoredConsoleCellDisplayer {

	/**
	 * 
	 * The string version of a Cell that is suitable for a color console.
	 * 
	 * @param cell
	 * 
	 * @return String
	 */
	String display(Cell cell);

}