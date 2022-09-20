package com.earl.nbyncheckers.input;

import java.util.Scanner;

public interface YesOrNoCommandInputFactory {

	YesOrNoCommandInput create(Scanner scanner, String message);

}