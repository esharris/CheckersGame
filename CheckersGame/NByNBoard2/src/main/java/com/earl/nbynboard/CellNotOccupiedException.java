package com.earl.nbynboard;

/**
 * 
 * @author earlharris
 *
 */
public class CellNotOccupiedException extends RuntimeException {

	private static final long serialVersionUID = 2380840091503469146L;

	public CellNotOccupiedException() {
		super("Cell is not occupied.");
	}
}
