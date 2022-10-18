package com.earl.nbyncheckers;

import java.util.Scanner;

import com.earl.nbyncheckers.input.YesOrNoCommandInput;
import com.earl.nbyncheckers.input.impl.YesOrNoCommandInputImpl;

/**
 *
 * @author earlharris
 */
public class TestYesOrNoCommandInputImpl {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        final YesOrNoCommandInput yesOrNoCommandInput = new YesOrNoCommandInputImpl(scanner, "Are you silly (y or n)?");
        System.out.println(yesOrNoCommandInput.getHeading());
        boolean answer = yesOrNoCommandInput.getInput();
        if (answer) {
            System.out.println("You're silly.");
        } else {
            System.out.println("You're serious.");
        }
    }

}
