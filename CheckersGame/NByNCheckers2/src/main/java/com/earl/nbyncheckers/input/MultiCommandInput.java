package com.earl.nbyncheckers.input;

import com.earl.nbynboard.Board;
import com.earl.utilities.Coordinate;

/**
 * 
 * @author earlharris
 *
 */
public interface MultiCommandInput {

	String getHeading();

	Command getInput(Board board);

	Coordinate getDestinationCoordinate();

}