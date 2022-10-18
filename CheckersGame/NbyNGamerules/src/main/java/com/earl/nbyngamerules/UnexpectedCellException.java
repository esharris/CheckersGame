package com.earl.nbyngamerules;

import com.earl.nbynboard.Cell;
import com.earl.nbynboard.CheckerColor;
import com.earl.utilities.Coordinate;

/**
 * 
 * @author earlharris
 */
public class UnexpectedCellException extends Exception {

	private static final long serialVersionUID = 2642484607876134103L;

	private final Cell cell;
	private final Coordinate coordinate;
	private final CheckerColor checkerColor;
	private final String name;

        /**
         * 
         * @param cell
         * @param coordinate
         * @param checkerColor
         * @param name 
         */
	public UnexpectedCellException(Cell cell, Coordinate coordinate, CheckerColor checkerColor, String name) {
		super("Expected " + (name == null || name.isEmpty() ? "" : name + " ") + "cell with " + checkerColor
				+ " checker, but saw " + cell + ".");
		this.cell = cell;
		this.coordinate = coordinate;
		this.checkerColor = checkerColor;
		this.name = name;
	}

	public Cell getCell() {
		return cell;
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public CheckerColor getCheckerColor() {
		return checkerColor;
	}

	public String getName() {
		return name;
	}
}
