package com.earl.nbyncheckers.input;

import java.util.Scanner;

/**
 * 
 * @author earlharris
 *
 */
public interface PreCommandInputFactory {

	PreCommandInput create(Scanner scanner);

}