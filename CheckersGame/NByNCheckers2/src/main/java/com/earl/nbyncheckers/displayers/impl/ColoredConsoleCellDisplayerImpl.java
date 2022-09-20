package com.earl.nbyncheckers.displayers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.earl.nbynboard.Cell;
import com.earl.nbyncheckers.displayers.ColoredConsoleCellDisplayer;
import com.earl.nbyncheckers.displayers.ColoredConsoleCheckerDisplayer;

/**
 * 
 * @author earlharris
 *
 */
@Service
public class ColoredConsoleCellDisplayerImpl implements ColoredConsoleCellDisplayer {

	private static final String UNOCCUPIED_DISPLAY_STRING = " ";

	private final ColoredConsoleCheckerDisplayer coloredConsoleCheckerDisplayerIimpl;

	@Autowired
	public ColoredConsoleCellDisplayerImpl(ColoredConsoleCheckerDisplayer coloredConsoleCheckerDisplayerIimpl) {
		this.coloredConsoleCheckerDisplayerIimpl = coloredConsoleCheckerDisplayerIimpl;
	}

	/**
	 * 
	 * The string version of a Cell that is suitable for a color console.
	 * 
	 * @param cell
	 * 
	 * @return String
	 */
	@Override
	public String display(Cell cell) {
		return cell.isOccupied() ? coloredConsoleCheckerDisplayerIimpl.display(cell.getChecker())
				: UNOCCUPIED_DISPLAY_STRING;
	}

	public ColoredConsoleCheckerDisplayer getColoredConsoleCheckerDisplayer() {
		return coloredConsoleCheckerDisplayerIimpl;
	}
}
