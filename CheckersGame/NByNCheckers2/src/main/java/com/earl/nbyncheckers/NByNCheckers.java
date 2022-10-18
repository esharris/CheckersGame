package com.earl.nbyncheckers;

import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.earl.nbynboard.Board;
import com.earl.nbynboard.BoardCreator;
import com.earl.nbynboard.Cell;
import com.earl.nbynboard.Checker;
import com.earl.nbynboard.CheckerColor;
import com.earl.nbynboard.CheckerRank;
import com.earl.nbyncheckers.displayers.ColoredConsoleBoardDisplayer;
import com.earl.nbyngamerules.GameRules;

/**
 * 
 * @author earlharris
 *
 */
public class NByNCheckers {

	private static final Board board = new Board(3, 4, true);
	private static final Board board1 = new Board(3, 4, false);
	private static final Board checkerBoard = new Board(8, 8, true);

        /**
         * 
         * @param args 
         */
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.scan("com.earl.nbyncheckers.displayers.impl", "com.earl.nbyngamerules.impl");
		try {
			context.refresh();
			final ColoredConsoleBoardDisplayer coloredConsoleBoardDisplayerImpl = //
					context.getBean(ColoredConsoleBoardDisplayer.class);
			final GameRules gameRules = context.getBean(GameRules.class);
			board.get(0).set(2, new Cell(new Checker(CheckerColor.RED, CheckerRank.REGULAR)));
			board1.get(0).set(1, new Cell(new Checker(CheckerColor.RED, CheckerRank.REGULAR)));
			System.out.println(coloredConsoleBoardDisplayerImpl.display(board));
			System.out.println();
			System.out.println(coloredConsoleBoardDisplayerImpl.display(board1));
			System.out.println();
			System.out.println(coloredConsoleBoardDisplayerImpl.display(board.flip()));
			gameRules.initialize(checkerBoard, 3);
			System.out.println(coloredConsoleBoardDisplayerImpl.display(checkerBoard));

			BoardCreator boardCreator = new BoardCreator();
			Board board2 = boardCreator.create(new String[] { //
					" b b b b", //
					"b b b b ", //
					"        ", //
					"        ", //
					" r r r r", //
					"r r r r " }, true);
			System.out.println();
			System.out.println(coloredConsoleBoardDisplayerImpl.display(board2));
			System.out.println();
		} catch (BeansException | IllegalStateException e) {
			e.printStackTrace();
		} finally {
			context.close();
		}
	}
}
