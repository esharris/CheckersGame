package com.earl.nbyncheckers.input.impl;

import java.util.Scanner;

import com.earl.nbyncheckers.input.YesOrNoCommandInput;

/**
 * 
 * @author earlharris
 *
 */
public class YesOrNoCommandInputImpl implements YesOrNoCommandInput {

	private final Scanner scanner;
	private final String message;

	public YesOrNoCommandInputImpl(Scanner scanner, String message) {
		this.scanner = scanner;
		this.message = message;
	}

	@Override
	public String getHeading() {
		return message;
	}

	@Override
	public boolean getInput() {
		boolean result = false;
		boolean done = false;
		while (!done) {
			String token = scanner.next().toLowerCase();
			switch (token) {
			case "y":
			case "yes":
				result = true;
				done = true;
				break;
			case "n":
			case "no":
				result = false;
				done = true;
				break;
			default:
				System.out.println("Unrecognized input: " + token);

			}
			scanner.nextLine();
		}
		return result;
	}

	public Scanner getScanner() {
		return scanner;
	}

	public String getMessage() {
		return message;
	}

}
