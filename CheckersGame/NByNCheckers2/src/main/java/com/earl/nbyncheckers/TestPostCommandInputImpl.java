package com.earl.nbyncheckers;

import java.util.Scanner;

import com.earl.nbyncheckers.input.Command;
import com.earl.nbyncheckers.input.PostCommandInput;
import com.earl.nbyncheckers.input.impl.PostCommandInputImpl;

/**
 * 
 * @author earlharris
 *
 */
public class TestPostCommandInputImpl {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		PostCommandInput preMoveInput = new PostCommandInputImpl(scanner);
		boolean done = false;
		while (!done) {
			System.out.println(preMoveInput.getHeading());
			System.out.print("input: ");
			Command command = preMoveInput.getInput();
			System.out.println(command);
			if (command == Command.RESIGN) {
				done = true;
			}

		}
	}

}
