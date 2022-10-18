package com.earl.nbyncheckers;

import java.util.Scanner;

import com.earl.nbynboard.Board;
import com.earl.nbyncheckers.input.Command;
import com.earl.nbyncheckers.input.MultiCommandInput;
import com.earl.nbyncheckers.input.impl.MultiCommandInputImpl;

/**
 *
 * @author earlharris
 *
 */
public class TestMultiCommandInputImpl {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Board board = new Board(8, 8, true);
        MultiCommandInput preMoveInput = new MultiCommandInputImpl(scanner);
        boolean done = false;
        while (!done) {
            System.out.println(preMoveInput.getHeading());
            System.out.print("input: ");
            Command command = preMoveInput.getInput(board);
            System.out.println(command);
            if (command == Command.MOVE) {
                System.out.println(preMoveInput.getDestinationCoordinate());
            } else if (command == Command.RESIGN) {
                done = true;
            }

        }
    }

}
