package com.earl.nbyncheckers.input.impl;

import java.util.Scanner;

import org.springframework.stereotype.Service;

import com.earl.nbyncheckers.input.MultiCommandInput;
import com.earl.nbyncheckers.input.MultiCommandInputFactory;

/**
 * 
 * @author earlharris
 *
 */
@Service
public class MultiCommandInputFactoryImpl implements MultiCommandInputFactory {

	@Override
	public MultiCommandInput create(Scanner scanner) {
		return new MultiCommandInputImpl(scanner);
	}
}
