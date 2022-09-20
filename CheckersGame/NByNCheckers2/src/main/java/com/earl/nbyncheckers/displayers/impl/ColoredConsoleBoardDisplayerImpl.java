package com.earl.nbyncheckers.displayers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.earl.nbynboard.Board;
import com.earl.nbyncheckers.displayers.ColoredConsoleBoardDisplayer;
import com.earl.nbyncheckers.displayers.ColoredConsoleBoardRowDisplayer;

/**
 * 
 * @author earlharris
 *
 */
@Service
public class ColoredConsoleBoardDisplayerImpl implements ColoredConsoleBoardDisplayer {

	private final ColoredConsoleBoardRowDisplayer coloredConsoleBoardRowDisplayerImpl;

	@Autowired
	public ColoredConsoleBoardDisplayerImpl(ColoredConsoleBoardRowDisplayer coloredConsoleBoardRowDisplayerImpl) {
		this.coloredConsoleBoardRowDisplayerImpl = coloredConsoleBoardRowDisplayerImpl;
	}

	/**
	 * 
	 * The string version of a Board that is suitable for a color console.
	 * 
	 * @param board
	 * 
	 * @return String
	 */
	@Override
	public String display(Board board) {
		final StringBuilder stringBuilder = new StringBuilder();
		/*
		 * Place the board so that each player has a light-colored square on the corner
		 * of the board on his or her right side.
		 */
		final int length = board.getRowCount();
		for (int i = 0; i < length; i++) {
			final int i1 = (length - 1) - i;
			stringBuilder.append(coloredConsoleBoardRowDisplayerImpl.display(board.get(i1)));
			stringBuilder.append(System.lineSeparator());
		}
		return stringBuilder.toString();
	}

	public ColoredConsoleBoardRowDisplayer getColoredConsoleBoardRowDisplayer() {
		return coloredConsoleBoardRowDisplayerImpl;
	}
}
