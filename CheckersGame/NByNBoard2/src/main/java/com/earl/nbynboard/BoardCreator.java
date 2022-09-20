package com.earl.nbynboard;

public class BoardCreator {

	public Board create(String[] b, boolean bottomLeftColored) {
		if (b.length > 0) {
			Board result = new Board(b.length, b[0].length(), bottomLeftColored);
			for (int i = 0; i < result.getRowCount(); i++) {
				/**
				 * Visually, the zeroth row in the String array is actually the last row in the
				 * corresponding Board.
				 */
				final int i1 = (result.getRowCount() - 1) - i;
				final BoardRow boardRow = result.get(i1);
				String br = b[i];
				for (int j = 0; j < boardRow.getColumnCount(); j++) {
					final Cell cell = create(br.charAt(j));
					boardRow.set(j, cell);
				}
			}
			return result;
		} else {
			throw new RuntimeException("Can't create Board from String array of length 0");
		}
	}

	private Cell create(char c) {
		Cell result;
		switch (c) {
		case ' ':
			result = Cell.UNOCCUPIED_CELL;
			break;
		case 'r':
			result = new Cell(new Checker(CheckerColor.RED, CheckerRank.REGULAR));
			break;
		case 'R':
			result = new Cell(new Checker(CheckerColor.RED, CheckerRank.KING));
			break;
		case 'b':
			result = new Cell(new Checker(CheckerColor.BLACK, CheckerRank.REGULAR));
			break;
		case 'B':
			result = new Cell(new Checker(CheckerColor.BLACK, CheckerRank.KING));
			break;
		default:
			throw new RuntimeException("Unrecognized character :" + c);
		}
		return result;
	}
}
