package com.earl.nbyncheckers.input;

import com.earl.nbynboard.Board;
import com.earl.utilities.Coordinate;

/**
 * 
 * @author earlharris
 *
 */
public interface PreCommandInput {

	String getHeading();

	Command getInput(Board board);

	Coordinate getCoordinate();

	Coordinate getDestinationCoordinate();

}