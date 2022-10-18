package com.earl.nbyncheckers;

import java.util.Scanner;

import com.earl.nbynboard.Board;
import com.earl.nbyncheckers.input.Command;
import com.earl.nbyncheckers.input.PreCommandInput;
import com.earl.nbyncheckers.input.impl.PreCommandInputImpl;

/**
 *
 * @author earlharris
 *
 */
public class TestPreCommandInputImpl {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Board board = new Board(8, 8, true);
        PreCommandInput preCommandInputImpl = new PreCommandInputImpl(scanner);
        boolean done = false;
        while (!done) {
            System.out.println(preCommandInputImpl.getHeading());
            System.out.print("input: ");
            Command command = preCommandInputImpl.getInput(board);
            System.out.println(command);
            if (command == Command.MOVE) {
                System.out.println(preCommandInputImpl.getCoordinate());
                System.out.println(preCommandInputImpl.getDestinationCoordinate());
            } else if (command == Command.RESIGN) {
                done = true;
            }

        }
    }

}
