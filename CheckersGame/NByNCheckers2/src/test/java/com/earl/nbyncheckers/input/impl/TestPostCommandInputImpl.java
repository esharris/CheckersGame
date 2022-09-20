package com.earl.nbyncheckers.input.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Scanner;

import org.junit.jupiter.api.Test;

import com.earl.nbyncheckers.input.Command;
import com.earl.nbyncheckers.input.PostCommandInput;

class TestPostCommandInputImpl {

	@Test
	void testGetInput() {
		final Scanner scanner = new Scanner("D\n d \n R\nr\nu\nq\nU\na\n");
		PostCommandInput postCommandInput = new PostCommandInputImpl(scanner);

		assertEquals(Command.DONE, postCommandInput.getInput());
		assertEquals(Command.DONE, postCommandInput.getInput());

		assertEquals(Command.RESIGN, postCommandInput.getInput());
		assertEquals(Command.RESIGN, postCommandInput.getInput());

		assertEquals(Command.UNDO, postCommandInput.getInput());
		assertEquals(Command.UNDO, postCommandInput.getInput());
		assertEquals(Command.DRAW, postCommandInput.getInput());
	}

}
