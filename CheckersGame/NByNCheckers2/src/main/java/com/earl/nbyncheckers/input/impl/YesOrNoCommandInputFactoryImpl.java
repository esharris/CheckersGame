package com.earl.nbyncheckers.input.impl;

import java.util.Scanner;

import org.springframework.stereotype.Service;

import com.earl.nbyncheckers.input.YesOrNoCommandInput;
import com.earl.nbyncheckers.input.YesOrNoCommandInputFactory;

/**
 * 
 * @author earlharris
 *
 */
@Service
public class YesOrNoCommandInputFactoryImpl implements YesOrNoCommandInputFactory {

	@Override
	public YesOrNoCommandInput create(Scanner scanner, String message) {
		return new YesOrNoCommandInputImpl(scanner, message);
	}
}
