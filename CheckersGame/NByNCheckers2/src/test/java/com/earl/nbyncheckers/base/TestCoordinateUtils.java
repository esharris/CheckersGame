package com.earl.nbyncheckers.base;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.earl.nbynboard.Board;
import com.earl.utilities.Coordinate;

class TestCoordinateUtils {

	@Test
	void testCoordinateToIndex() {
		assertEquals(0, CoordinateUtils.coordinateToIndex(4, new Coordinate(0, 0)));
		assertEquals(0, CoordinateUtils.coordinateToIndex(4, new Coordinate(0, 1)));
		assertEquals(1, CoordinateUtils.coordinateToIndex(4, new Coordinate(0, 2)));
		assertEquals(3, CoordinateUtils.coordinateToIndex(4, new Coordinate(1, 3)));
		assertEquals(5, CoordinateUtils.coordinateToIndex(4, new Coordinate(2, 2)));
	}

	@Test
	void testIndexToCoorindate() {
		final Board board = new Board(4, 4, true);
		final Board board1 = new Board(4, 4, false);

		assertEquals(new Coordinate(0, 0), CoordinateUtils.indexToCoordinate(board, 0));
		assertEquals(new Coordinate(0, 1), CoordinateUtils.indexToCoordinate(board1, 0));
		assertEquals(new Coordinate(0, 2), CoordinateUtils.indexToCoordinate(board, 1));
		assertEquals(new Coordinate(1, 1), CoordinateUtils.indexToCoordinate(board, 2));
		assertEquals(new Coordinate(1, 3), CoordinateUtils.indexToCoordinate(board, 3));
		assertEquals(new Coordinate(2, 2), CoordinateUtils.indexToCoordinate(board, 5));
	}
}
