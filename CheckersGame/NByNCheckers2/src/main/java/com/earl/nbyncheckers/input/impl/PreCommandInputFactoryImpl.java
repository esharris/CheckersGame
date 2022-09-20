package com.earl.nbyncheckers.input.impl;

import java.util.Scanner;

import org.springframework.stereotype.Service;

import com.earl.nbyncheckers.input.PreCommandInput;
import com.earl.nbyncheckers.input.PreCommandInputFactory;

/**
 * 
 * @author earlharris
 *
 */
@Service
public class PreCommandInputFactoryImpl implements PreCommandInputFactory {

	@Override
	public PreCommandInput create(Scanner scanner) {
		return new PreCommandInputImpl(scanner);
	}
}
