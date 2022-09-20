package com.earl.nbyngamerules;

import com.earl.nbynboard.Cell;
import com.earl.utilities.Coordinate;

public class CellIsOccupiedException extends Exception {

	private static final long serialVersionUID = 3295922027765526081L;

	private final Cell cell;
	private final Coordinate destinationCoordinate;

	public CellIsOccupiedException(Cell cell, Coordinate destinationCoordinate) {
		super("Expected empty cell at " + destinationCoordinate + ", but saw " + cell + ".");
		this.cell = cell;
		this.destinationCoordinate = destinationCoordinate;
	}

	public Cell getCell() {
		return cell;
	}

	public Coordinate getDestinationCoordinate() {
		return destinationCoordinate;
	}
}
