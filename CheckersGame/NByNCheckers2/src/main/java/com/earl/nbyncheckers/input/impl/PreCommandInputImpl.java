package com.earl.nbyncheckers.input.impl;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.earl.nbynboard.Board;
import com.earl.nbyncheckers.base.CoordinateUtils;
import com.earl.nbyncheckers.input.Command;
import com.earl.nbyncheckers.input.PreCommandInput;
import com.earl.utilities.Coordinate;

/**
 * 
 * @author earlharris
 *
 */
public class PreCommandInputImpl implements PreCommandInput {
	private final Scanner scanner;
	private Coordinate coordinate;
	private Coordinate destinationCoordinate;

	public PreCommandInputImpl(Scanner scanner) {
		this.scanner = scanner;
	}

	@Override
	public String getHeading() {
		return "(M)ove {start index} {end index}, (R)esign, dr(A)w";
	}

	@Override
	public Command getInput(Board board) {
		/**
		 * This method is a little long. But since command and done transcends the body
		 * of the while loop and get modified, it seems unnatural to break the method
		 * down further.
		 */
		Command command = Command.UNKNOWN;
		boolean done = false;
		while (!done) {
			String token = scanner.next().toLowerCase();
			if (token.equals("r")) {
				done = true;
				command = Command.RESIGN;
			} else if (token.equals("m")) {
				try {
					int index = scanner.nextInt();
					if (0 <= index && index < board.getColumnCount() * board.getRowCount() / 2) {
						try {
							int destinationIndex = scanner.nextInt();
							if (0 <= destinationIndex
									&& destinationIndex < board.getColumnCount() * board.getRowCount() / 2) {
								coordinate = CoordinateUtils.indexToCoordinate(board, index);
								destinationCoordinate = CoordinateUtils.indexToCoordinate(board, destinationIndex);
								done = true;
								command = Command.MOVE;
							} else {
								System.out.println("Second parameter " + destinationIndex + " is out of range.");
							}
						} catch (InputMismatchException e) {
							System.out.println("Second input is not an integer or it is out of range.");
						} catch (NoSuchElementException e) {
							System.out.println("The input is exhausted.");
						} catch (IllegalStateException e) {
							System.out.println("The scanner is closed.");
						}
					} else {
						System.out.println("First parameter " + index + " is out of range.");
					}
				} catch (InputMismatchException e) {
					System.out.println("First input is not an integer or it is out of range.");
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
	public Coordinate getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Coordinate coordination) {
		this.coordinate = coordination;
	}

	@Override
	public Coordinate getDestinationCoordinate() {
		return destinationCoordinate;
	}

	public void setDestinationCoordinate(Coordinate destinationCoordinate) {
		this.destinationCoordinate = destinationCoordinate;
	}
}
