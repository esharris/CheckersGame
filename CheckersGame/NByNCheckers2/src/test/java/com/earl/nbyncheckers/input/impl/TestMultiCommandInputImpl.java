package com.earl.nbyncheckers.input.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Scanner;

import org.junit.jupiter.api.Test;

import com.earl.nbynboard.Board;
import com.earl.nbyncheckers.input.Command;
import com.earl.nbyncheckers.input.MultiCommandInput;
import com.earl.utilities.Coordinate;

class TestMultiCommandInputImpl {

	private final Board board = new Board(8, 8, true);

	@Test
	void testGetInput() {
		final Scanner scanner = new Scanner("M 3\n m 31 \n R\nm 45\nm 100000000\nm a\nr\nA\nu\n");
		MultiCommandInput multiCommandInput = new MultiCommandInputImpl(scanner);

		assertEquals(Command.MOVE, multiCommandInput.getInput(board));
		assertEquals(new Coordinate(0, 6), multiCommandInput.getDestinationCoordinate());

		assertEquals(Command.MOVE, multiCommandInput.getInput(board));
		assertEquals(new Coordinate(7, 7), multiCommandInput.getDestinationCoordinate());

		assertEquals(Command.RESIGN, multiCommandInput.getInput(board));
		assertEquals(Command.RESIGN, multiCommandInput.getInput(board));

		assertEquals(Command.DRAW, multiCommandInput.getInput(board));
		assertEquals(Command.UNDO, multiCommandInput.getInput(board));
	}

}
