package com.earl.nbyncheckers.input.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Scanner;

import org.junit.jupiter.api.Test;

class TestYesOrNoCommandInputImpl {

	@Test
	void test() {
		final Scanner scanner = new Scanner("Y\ny\nYes\nyes\nN\nn\n z\nNo\nno\n");
		final YesOrNoCommandInputImpl yesOrNoInput = new YesOrNoCommandInputImpl(scanner, "Are you happy (y or n)?");

		assertTrue(yesOrNoInput.getInput());
		assertTrue(yesOrNoInput.getInput());
		assertTrue(yesOrNoInput.getInput());
		assertTrue(yesOrNoInput.getInput());

		assertFalse(yesOrNoInput.getInput());
		assertFalse(yesOrNoInput.getInput());
		assertFalse(yesOrNoInput.getInput());
		assertFalse(yesOrNoInput.getInput());
	}

}
