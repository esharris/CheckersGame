package com.earl.nbyngamerules.base;

import com.earl.nbynboard.Board;
import com.earl.utilities.Coordinate;

/**
 * 
 * @author earlharris
 *
 */
public class BoardCoordinate {

	private final Board board;
	private final Coordinate coordinate;

        /**
         * 
         * @param board
         * @param coordinate 
         */
	public BoardCoordinate(Board board, Coordinate coordinate) {
		this.board = board;
		this.coordinate = coordinate;
	}

	public Board getBoard() {
		return board;
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}
}
