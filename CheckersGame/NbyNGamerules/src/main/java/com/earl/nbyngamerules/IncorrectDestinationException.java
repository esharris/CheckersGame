package com.earl.nbyngamerules;

import com.earl.nbynboard.Checker;
import com.earl.nbynboard.CheckerRank;
import com.earl.utilities.Coordinate;

/**
 * 
 * @author earlharris
 */
public class IncorrectDestinationException extends Exception {

	private static final long serialVersionUID = 4711929451581939078L;

	private final Checker checker;
	private final int distance;
	private final Coordinate coordinate;
	private final Coordinate destinationCoordinate;
	private final int changeX;
	private final int changeY;

        /**
         * 
         * @param checker
         * @param distance
         * @param coordinate
         * @param destinationCoordinate
         * @param changeX
         * @param changeY 
         */
	public IncorrectDestinationException(Checker checker, int distance, Coordinate coordinate,
			Coordinate destinationCoordinate, int changeX, int changeY) {
		super("Can only move " + (checker.getCheckerRank() == CheckerRank.REGULAR ? "forward " : "") + "on a diagonal "
				+ distance + " step" + (distance == 1 ? "" : "s") + ". coordinate = " + coordinate + ", destination = "
				+ destinationCoordinate + ", changeX = " + changeX + ", changeY = " + changeY);
		this.checker = checker;
		this.distance = distance;
		this.coordinate = coordinate;
		this.destinationCoordinate = destinationCoordinate;
		this.changeX = changeX;
		this.changeY = changeY;
	}

	public Checker getChecker() {
		return checker;
	}

	public int getDistance() {
		return distance;
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public Coordinate getDestinationCoordinate() {
		return destinationCoordinate;
	}

	public int getChangeX() {
		return changeX;
	}

	public int getChangeY() {
		return changeY;
	}
}
