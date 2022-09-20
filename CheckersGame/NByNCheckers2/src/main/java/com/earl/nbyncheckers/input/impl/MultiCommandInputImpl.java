package com.earl.nbyncheckers.input.impl;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.earl.nbynboard.Board;
import com.earl.nbyncheckers.base.CoordinateUtils;
import com.earl.nbyncheckers.input.Command;
import com.earl.nbyncheckers.input.MultiCommandInput;
import com.earl.utilities.Coordinate;

/**
 * 
 * @author earlharris
 *
 */
public class MultiCommandInputImpl implements MultiCommandInput {
	private final Scanner scanner;
	private Coordinate destinationCoordinate;

	public MultiCommandInputImpl(Scanner scanner) {
		this.scanner = scanner;
	}

	@Override
	public String getHeading() {
		return "(M)ove {end index}, (U)ndo, (R)esign, dr(A)w";
	}

	@Override
	public Command getInput(Board board) {
		Command command = Command.UNKNOWN;
		boolean done = false;
		while (!done) {
			String token = scanner.next().toLowerCase();
			if (token.equals("r")) {
				command = Command.RESIGN;
				done = true;
			} else if (token.equals("u")) {
				command = Command.UNDO;
				done = true;
			} else if (token.equals("m")) {
				try {
					int destinationIndex = scanner.nextInt();
					if (0 <= destinationIndex && destinationIndex < board.getColumnCount() * board.getRowCount() / 2) {
						destinationCoordinate = CoordinateUtils.indexToCoordinate(board, destinationIndex);
						done = true;
						command = Command.MOVE;
					} else {
						System.out.println(destinationIndex + " is out of range.");
					}
				} catch (InputMismatchException e) {
					System.out.println("The input is not an integer or it is out of range.");
				} catch (NoSuchElementException e) {
					System.out.println("The input is exhausted.");
				} catch (IllegalStateException e) {
					System.out.println("The scanner is closed.");
				}
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

	@Override
	public Coordinate getDestinationCoordinate() {
		return destinationCoordinate;
	}

	public void setDestinationCoordinate(Coordinate destinationCoordinate) {
		this.destinationCoordinate = destinationCoordinate;
	}
}
