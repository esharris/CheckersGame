package com.earl.nbyncheckers.input.impl;

import java.util.Scanner;

import com.earl.nbyncheckers.input.Command;
import com.earl.nbyncheckers.input.PostCommandInput;

/**
 * 
 * @author earlharris
 *
 */
public class PostCommandInputImpl implements PostCommandInput {
	private final Scanner scanner;

	public PostCommandInputImpl(Scanner scanner) {
		this.scanner = scanner;
	}

	@Override
	public String getHeading() {
		return "(D)one, (U)ndo, (R)esign, dr(A)w";
	}

	@Override
	public Command getInput() {
		Command command = Command.UNKNOWN;
		boolean done = false;
		while (!done) {
			String token = scanner.next().toLowerCase();
			if (token.equals("r")) {
				command = Command.RESIGN;
				done = true;
			} else if (token.equals("d")) {
				command = Command.DONE;
				done = true;
			} else if (token.equals("u")) {
				command = Command.UNDO;
				done = true;
			} else if (token.equals("a")) {
				command = Command.DRAW;
				done = true;
			} else {
				System.out.println("Unrecognized input: " + token);
			}
			scanner.nextLine();
		}
		return command;

	}

	public Scanner getScanner() {
		return scanner;
	}
}
