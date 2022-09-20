package com.earl.nbyncheckers.input.impl;

import java.util.Scanner;

import org.springframework.stereotype.Service;

import com.earl.nbyncheckers.input.PostCommandInput;
import com.earl.nbyncheckers.input.PostCommandInputFactory;

/**
 * 
 * @author earlharris
 *
 */
@Service
public class PostCommandInputFactoryImpl implements PostCommandInputFactory {

	@Override
	public PostCommandInput create(Scanner scanner) {
		return new PostCommandInputImpl(scanner);
	}
}
