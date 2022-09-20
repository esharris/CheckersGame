package com.earl.nbyncheckers.input;

import java.util.Scanner;

/**
 * 
 * @author earlharris
 *
 */
public interface PostCommandInputFactory {

	PostCommandInput create(Scanner scanner);

}