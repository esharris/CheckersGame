package com.earl.nbyncheckers.input.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Scanner;

import org.junit.jupiter.api.Test;

import com.earl.nbynboard.Board;
import com.earl.nbyncheckers.input.Command;
import com.earl.nbyncheckers.input.PreCommandInput;
import com.earl.utilities.Coordinate;

class TestPreCommandInputImpl {

	private final Board board = new Board(8, 8, true);

	@Test
	void testGetInput() {
		/**
		 * Why not use Mockito to test the error messages?
		 * 
		 * Mockito can test is a method is invoked. Here, it is more appropriate to test
		 * what has gone into the output stream.
		 * 
		 * Why not create your own stub classes?
		 * 
		 * Since PrintStream is not a class, it is unclear how to construct a stub
		 * PrintStream. I.e., if I tried to put a PrintStream wrapper over another
		 * PrintStream, it is unclear how to initialize the wrapper class.
		 */
		final Scanner scanner = new Scanner(
				"M 2 3\n m 0 31 \n R\nm 23 45\nM 45 23\nm 100000000 0\nm a 5\nm 5 \na\nr\nA\n");
		final PreCommandInput preCommandInput = new PreCommandInputImpl(scanner);

		assertEquals(Command.MOVE, preCommandInput.getInput(board));
		assertEquals(new Coordinate(0, 4), preCommandInput.getCoordinate());
		assertEquals(new Coordinate(0, 6), preCommandInput.getDestinationCoordinate());

		assertEquals(Command.MOVE, preCommandInput.getInput(board));
		assertEquals(new Coordinate(0, 0), preCommandInput.getCoordinate());
		assertEquals(new Coordinate(7, 7), preCommandInput.getDestinationCoordinate());

		assertEquals(Command.RESIGN, preCommandInput.getInput(board));
		assertEquals(Command.RESIGN, preCommandInput.getInput(board));

		assertEquals(Command.DRAW, preCommandInput.getInput(board));
	}

}
