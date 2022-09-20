package com.earl.nbyngamerules;

import com.earl.nbynboard.Board;
import com.earl.utilities.Coordinate;

public class CoordinateNotOnBoardExcepton extends RuntimeException {

	private static final long serialVersionUID = -4432752825619603393L;

	private final Board board;
	private final Coordinate coordinate;
	private final String name;

	public CoordinateNotOnBoardExcepton(Board board, Coordinate coordinate, String name) {
		super("The " + (name == null || name.isEmpty() ? "" : name + " ")
				+ "Coordinate is not on the board: Coordinate(" + coordinate.getX() + ", " + coordinate.getY()
				+ "), Board(0.." + (board.getRowCount() - 1) + ", 0.." + (board.getColumnCount() - 1) + ")");
		this.board = board;
		this.coordinate = coordinate;
		this.name = name;
	}

	public Board getBoard() {
		return board;
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public String getName() {
		return name;
	}
}
