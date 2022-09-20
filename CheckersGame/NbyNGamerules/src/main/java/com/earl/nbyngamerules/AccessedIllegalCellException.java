package com.earl.nbyngamerules;

import com.earl.nbynboard.BoardRow;

/**
 * 
 * @author earlharris
 *
 */
public class AccessedIllegalCellException extends RuntimeException {

	private static final long serialVersionUID = -4159465023578793681L;

	private final BoardRow boardRow;
	private final int i;

	public AccessedIllegalCellException(BoardRow boardRow, int i) {
		super("Tried to accessed cell at " + i + ", but only " + (boardRow.coloredCellCode() == 0 ? "even " : "odd")
				+ " columns are legal.");
		this.boardRow = boardRow;
		this.i = i;
	}

	public BoardRow getBoardRow() {
		return boardRow;
	}

	public int getI() {
		return i;
	}
}
