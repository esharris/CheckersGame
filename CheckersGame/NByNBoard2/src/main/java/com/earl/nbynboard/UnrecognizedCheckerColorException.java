package com.earl.nbynboard;

/**
 * 
 * @author earlharris
 *
 */
public class UnrecognizedCheckerColorException extends RuntimeException {

	private static final long serialVersionUID = 5544788798544203679L;

	private final CheckerColor checkerColor;

	public UnrecognizedCheckerColorException(CheckerColor checkerColor) {
		super("Unrecognized CheckerColor" + checkerColor);
		this.checkerColor = checkerColor;
	}

	public CheckerColor getCheckerColor() {
		return checkerColor;
	}
}
