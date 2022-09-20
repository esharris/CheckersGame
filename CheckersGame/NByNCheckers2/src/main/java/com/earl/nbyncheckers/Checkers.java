package com.earl.nbyncheckers;

import java.util.Scanner;
import java.util.Stack;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.earl.nbynboard.Board;
import com.earl.nbynboard.Checker;
import com.earl.nbynboard.CheckerColor;
import com.earl.nbynboard.CheckerRank;
import com.earl.nbyncheckers.base.Container;
import com.earl.nbyncheckers.base.CoordinateUtils;
import com.earl.nbyncheckers.displayers.ColoredConsoleBoardDisplayer;
import com.earl.nbyncheckers.input.Command;
import com.earl.nbyncheckers.input.MultiCommandInput;
import com.earl.nbyncheckers.input.MultiCommandInputFactory;
import com.earl.nbyncheckers.input.PostCommandInput;
import com.earl.nbyncheckers.input.PostCommandInputFactory;
import com.earl.nbyncheckers.input.PreCommandInput;
import com.earl.nbyncheckers.input.PreCommandInputFactory;
import com.earl.nbyncheckers.input.YesOrNoCommandInput;
import com.earl.nbyncheckers.input.YesOrNoCommandInputFactory;
import com.earl.nbyngamerules.CellIsOccupiedException;
import com.earl.nbyngamerules.GameRules;
import com.earl.nbyngamerules.IncorrectDestinationException;
import com.earl.nbyngamerules.MoveManager;
import com.earl.nbyngamerules.UnexpectedCellException;
import com.earl.nbyngamerules.base.BoardCoordinate;
import com.earl.utilities.Coordinate;

public class Checkers {

	private static final int ROW_COUNT = 8;
	private static final int COLUMN_COUNT = 8;
	private static final int ROWS_INITIALIZED = 3;

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.scan("com.earl.nbyncheckers.displayers.impl", "com.earl.nbyngamerules.impl",
				"com.earl.nbyncheckers.input.impl");
		try {
			context.refresh();
			/**
			 * Get the beans
			 */
			final ColoredConsoleBoardDisplayer coloredConsoleBoardDisplayer = //
					context.getBean(ColoredConsoleBoardDisplayer.class);
			final GameRules gameRules = context.getBean(GameRules.class);
			final MoveManager moveManger = context.getBean(MoveManager.class);
			final PreCommandInputFactory preCommandInputFactory = context.getBean(PreCommandInputFactory.class);
			final MultiCommandInputFactory multiCommandInputFactory = context.getBean(MultiCommandInputFactory.class);
			final PostCommandInputFactory postCommandInputFactory = context.getBean(PostCommandInputFactory.class);
			final YesOrNoCommandInputFactory yesOrNoCommandInputFactory = context
					.getBean(YesOrNoCommandInputFactory.class);

			final Scanner scanner = new Scanner(System.in);

			/**
			 * Create command input objects
			 */
			final PreCommandInput preCommandInput = preCommandInputFactory.create(scanner);
			final MultiCommandInput multiCommandInput = multiCommandInputFactory.create(scanner);
			final PostCommandInput postCommandInput = postCommandInputFactory.create(scanner);
			final YesOrNoCommandInput resignYesOrNoCommandInput = yesOrNoCommandInputFactory.create(scanner,
					"Are you sure (y or n)?");
			final YesOrNoCommandInput drawYesOrNoCommandInput = yesOrNoCommandInputFactory.create(scanner,
					", would you like to accept a draw (y or n)?");

			/**
			 * Initialize a standard checker game.
			 */
			final Container<Board> boardContainer = new Container<Board>(new Board(ROW_COUNT, COLUMN_COUNT, true));
			gameRules.initialize(boardContainer.getValue(), ROWS_INITIALIZED);
			CheckerColor currentPlayer = CheckerColor.RED;

			boolean done = false;
			try {
				while (!done) {
					System.out.println(coloredConsoleBoardDisplayer.display(boardContainer.getValue()));
					System.out.println("The current player is : " + currentPlayer);

					if (moveManger.thereIsAJump(boardContainer.getValue(), currentPlayer)) {
						jumpTurn(coloredConsoleBoardDisplayer, gameRules, moveManger, preCommandInput,
								multiCommandInput, postCommandInput, resignYesOrNoCommandInput, drawYesOrNoCommandInput,
								boardContainer, currentPlayer);
					} else {
						moveTurn(coloredConsoleBoardDisplayer, gameRules, moveManger, preCommandInput, postCommandInput,
								resignYesOrNoCommandInput, drawYesOrNoCommandInput, boardContainer, currentPlayer);
					}
					if (gameRules.isWinner(boardContainer.getValue(), currentPlayer)) {
						System.out.println(coloredConsoleBoardDisplayer.display(boardContainer.getValue()));
						System.out.println("The current player " + currentPlayer + " has won.");
						done = true;
					} else {
						currentPlayer = gameRules.getOpponent(currentPlayer);
						boardContainer.setValue(boardContainer.getValue().flip());
					}
				}
			} catch (ResignFromGameException e) {
				System.out.println(coloredConsoleBoardDisplayer.display(boardContainer.getValue()));
				System.out.println("The current player " + currentPlayer + " has resigned.");
			} catch (GameIsDrawException e) {
				System.out.println(coloredConsoleBoardDisplayer.display(boardContainer.getValue()));
				System.out.println("The game is a draw.");
			}
		} finally {
			context.close();
		}
	}

	/**
	 * 
	 * @param coloredConsoleBoardDisplayer
	 * @param gameRules
	 * @param moveManger
	 * @param preCommandInput
	 * @param postCommandInput
	 * @param resignYesOrNoCommandInput
	 * @param drawYesOrNoCommandInput
	 * @param boardContainer
	 * @param currentPlayer
	 * @throws ResignFromGameException
	 * 
	 *                                 Try to complete an entire move turn.
	 * @throws GameIsDrawException
	 */
	private static void moveTurn(final ColoredConsoleBoardDisplayer coloredConsoleBoardDisplayer, GameRules gameRules,
			final MoveManager moveManger, final PreCommandInput preCommandInput,
			final PostCommandInput postCommandInput, YesOrNoCommandInput resignYesOrNoCommandInput,
			YesOrNoCommandInput drawYesOrNoCommandInput, Container<Board> boardContainer, CheckerColor currentPlayer)
			throws ResignFromGameException, GameIsDrawException {
		System.out.println("Move one space.");
		Container<Board> savedBoardContainer = new Container<Board>();
		Container<Boolean> doneContainer = new Container<Boolean>(false);
		while (!doneContainer.getValue()) {
			tryToMove(coloredConsoleBoardDisplayer, gameRules, moveManger, preCommandInput, resignYesOrNoCommandInput,
					drawYesOrNoCommandInput, boardContainer.getValue(), currentPlayer, savedBoardContainer);
			tryToCompleteMove(coloredConsoleBoardDisplayer, gameRules, postCommandInput, resignYesOrNoCommandInput,
					drawYesOrNoCommandInput, currentPlayer, boardContainer, doneContainer,
					savedBoardContainer.getValue());
		}
	}

	/**
	 * @param coloredConsoleBoardDisplayer
	 * @param gameRules
	 * @param moveManger
	 * @param preCommandInput
	 * @param resignYesOrNoCommandInput
	 * @param drawYesOrNoCommandInput
	 * @param currentPlayer
	 * @param savedBoardContainer
	 * @param boardContainer
	 * @throws ResignFromGameException
	 * @throws GameIsDrawException
	 * 
	 *                                 Repeat until I either make a successful move
	 *                                 or I resign. If I make a successful move, set
	 *                                 saveBoard to the previous board.
	 */
	private static void tryToMove(final ColoredConsoleBoardDisplayer coloredConsoleBoardDisplayer, GameRules gameRules,
			final MoveManager moveManger, final PreCommandInput preCommandInput,
			YesOrNoCommandInput resignYesOrNoCommandInput, YesOrNoCommandInput drawYesOrNoCommandInput, Board board,
			CheckerColor currentPlayer, Container<Board> savedBoardContainer)
			throws ResignFromGameException, GameIsDrawException {
		boolean done = false;
		while (!done) {
			System.out.println(preCommandInput.getHeading());
			final Command input = preCommandInput.getInput(board);
			switch (input) {
			case RESIGN:
				System.out.println(resignYesOrNoCommandInput.getHeading());
				if (resignYesOrNoCommandInput.getInput()) {
					throw new ResignFromGameException();
				}
				// loop around and try again.
				break;
			case MOVE: {
				try {
					savedBoardContainer.setValue(board.copy());
					moveManger.move(board, preCommandInput.getCoordinate(), currentPlayer,
							preCommandInput.getDestinationCoordinate());
					System.out.println(coloredConsoleBoardDisplayer.display(board));
					done = true;
				} catch (UnexpectedCellException e) {
					handleUnexpectedCellException(board, e);
					// loop around and try again.
				} catch (IncorrectDestinationException e) {
					handelIncorrectDestinationException(e);
					// loop around and try again.
				} catch (CellIsOccupiedException e) {
					handleCellIsOccupiedException(e, board.getColumnCount());
					// loop around and try again.
				}
			}
				break;
			case DRAW:
				System.out.println(coloredConsoleBoardDisplayer.display(board.flip()));
				System.out.println(gameRules.getOpponent(currentPlayer) + drawYesOrNoCommandInput.getHeading());
				if (drawYesOrNoCommandInput.getInput()) {
					System.out.println(coloredConsoleBoardDisplayer.display(board));
					throw new GameIsDrawException();
				}
				System.out.println(coloredConsoleBoardDisplayer.display(board));
				// loop around and try again.
				break;
			default:
				System.out.println("Unknown Command: " + input);
				// loop around and try again.
			}
		}
	}

	/**
	 * @param coloredConsoleBoardDisplayer
	 * @param gameRules
	 * @param postCommandInput
	 * @param resignYesOrNoCommandInput
	 * @param drawYesOrNoCommandInput
	 * @param currentPlayer
	 * @param boardContainer
	 * @param doneContainer
	 * @param savedBoard
	 * @throws ResignFromGameException
	 * @throws GameIsDrawException
	 */
	private static void tryToCompleteMove(final ColoredConsoleBoardDisplayer coloredConsoleBoardDisplayer,
			GameRules gameRules, final PostCommandInput postCommandInput, YesOrNoCommandInput resignYesOrNoCommandInput,
			YesOrNoCommandInput drawYesOrNoCommandInput, CheckerColor currentPlayer, Container<Board> boardContainer,
			Container<Boolean> doneContainer, final Board savedBoard)
			throws ResignFromGameException, GameIsDrawException {
		boolean done = false;
		while (!done) {
			System.out.println(postCommandInput.getHeading());
			Command input = postCommandInput.getInput();
			switch (input) {
			case RESIGN:
				System.out.println(resignYesOrNoCommandInput.getHeading());
				if (resignYesOrNoCommandInput.getInput()) {
					throw new ResignFromGameException();
				}
				// loop around and try again.
				break;
			case DONE:
				done = true;
				/**
				 * The entire move is complete.
				 */
				doneContainer.setValue(true);
				break;
			case UNDO:
				/**
				 * Restore previous board.
				 */
				boardContainer.setValue(savedBoard);
				System.out.println(coloredConsoleBoardDisplayer.display(boardContainer.getValue()));
				done = true;
				break;
			case DRAW:
				System.out.println(coloredConsoleBoardDisplayer.display(boardContainer.getValue().flip()));
				System.out.println(gameRules.getOpponent(currentPlayer) + drawYesOrNoCommandInput.getHeading());
				if (drawYesOrNoCommandInput.getInput()) {
					System.out.println(coloredConsoleBoardDisplayer.display(boardContainer.getValue()));
					throw new GameIsDrawException();
				}
				System.out.println(coloredConsoleBoardDisplayer.display(boardContainer.getValue()));
				// loop around and try again.
				break;
			default:
				System.out.println("Unknown Command: " + input);
				// loop around and try again.
			}
		}
	}

	/**
	 * 
	 * @param coloredConsoleBoardDisplayer
	 * @param gameRules
	 * @param moveManger
	 * @param preCommandInput
	 * @param multiCommandInput
	 * @param postCommandInput
	 * @param resignYesOrNoCommandInput
	 * @param drawYesOrNoCommandInput
	 * @param boardContainer
	 * @param currentPlayer
	 * @throws ResignFromGameException
	 * @throws GameIsDrawException
	 */
	private static void jumpTurn(final ColoredConsoleBoardDisplayer coloredConsoleBoardDisplayer, GameRules gameRules,
			final MoveManager moveManger, final PreCommandInput preCommandInput,
			final MultiCommandInput multiCommandInput, final PostCommandInput postCommandInput,
			YesOrNoCommandInput resignYesOrNoCommandInput, YesOrNoCommandInput drawYesOrNoCommandInput,
			Container<Board> boardContainer, CheckerColor currentPlayer)
			throws ResignFromGameException, GameIsDrawException {
		System.out.println("You must make a jump.");
		/**
		 * Supporting multiple jumps and undoes require a backtracking mechanism. The
		 * boardCoordinateStack store the previous state (the board) and the current
		 * destination coordinate.
		 */
		final Stack<BoardCoordinate> boardCoordinateStack = new Stack<BoardCoordinate>();
		Container<Boolean> doneContainer = new Container<Boolean>(false);
		while (!doneContainer.getValue()) {
			if (boardCoordinateStack.isEmpty()) {
				/**
				 * status: I am at the initial state. We haven't made my first jump. There is no
				 * undo here.
				 */
				tryToJump(coloredConsoleBoardDisplayer, gameRules, moveManger, preCommandInput,
						resignYesOrNoCommandInput, drawYesOrNoCommandInput, boardContainer.getValue(), currentPlayer,
						boardCoordinateStack);
			} else {
				/**
				 * status: I made at least on jump. Checking for possible multiple jumps.
				 */
				try {
					if (moveManger.canMakeAJump(boardContainer.getValue(), boardCoordinateStack.peek().getCoordinate(),
							currentPlayer)) {
						tryToMultiJump(coloredConsoleBoardDisplayer, gameRules, moveManger, multiCommandInput,
								resignYesOrNoCommandInput, drawYesOrNoCommandInput, boardContainer.getValue(),
								boardContainer, currentPlayer, boardCoordinateStack);
					} else {
						/**
						 * Status: No more jumps for the current Checker.
						 * 
						 * Attempt to complete the turn.
						 */
						tryToCompleteJumps(coloredConsoleBoardDisplayer, gameRules, postCommandInput,
								resignYesOrNoCommandInput, drawYesOrNoCommandInput, currentPlayer, boardContainer,
								boardCoordinateStack, doneContainer);
					}
				} catch (UnexpectedCellException e) {
					throw new RuntimeException("System error: ", e);
				}
			}
		}
	}

	/**
	 * @param coloredConsoleBoardDisplayer
	 * @param gameRules
	 * @param moveManger
	 * @param preCommandInput
	 * @param resignYesOrNoCommandInput
	 * @param drawYesOrNoCommandInput
	 * @param currentPlayer
	 * @param boardCoordinateStack
	 * @param boardContainer
	 * @throws ResignFromGameException
	 * @throws GameIsDrawException
	 * 
	 *                                 Repeat until I either make a successful jump
	 *                                 or I resign. If I make a successful move,
	 *                                 push previous board and destination
	 *                                 coordinate onto the stack.
	 */
	private static void tryToJump(final ColoredConsoleBoardDisplayer coloredConsoleBoardDisplayer, GameRules gameRules,
			final MoveManager moveManger, final PreCommandInput preCommandInput,
			YesOrNoCommandInput resignYesOrNoCommandInput, YesOrNoCommandInput drawYesOrNoCommandInput, Board board,
			CheckerColor currentPlayer, final Stack<BoardCoordinate> boardCoordinateStack)
			throws ResignFromGameException, GameIsDrawException {
		boolean done = false;
		while (!done) {
			System.out.println(preCommandInput.getHeading());
			final Command input = preCommandInput.getInput(board);
			switch (input) {
			case RESIGN:
				System.out.println(resignYesOrNoCommandInput.getHeading());
				if (resignYesOrNoCommandInput.getInput()) {
					throw new ResignFromGameException();
				}
				// loop around and try again.
				break;
			case MOVE: {
				try {
					final Board savedBoard = board.copy();
					moveManger.jump(board, preCommandInput.getCoordinate(), currentPlayer,
							preCommandInput.getDestinationCoordinate());
					boardCoordinateStack
							.push(new BoardCoordinate(savedBoard, preCommandInput.getDestinationCoordinate()));
					System.out.println(coloredConsoleBoardDisplayer.display(board));
					done = true;
				} catch (UnexpectedCellException e) {
					handleUnexpectedCellException(board, e);
					// loop around and try again.
				} catch (IncorrectDestinationException e) {
					handelIncorrectDestinationException(e);
					// loop around and try again.
				} catch (CellIsOccupiedException e) {
					handleCellIsOccupiedException(e, board.getColumnCount());
					// loop around and try again.
				}
			}
				break;
			case DRAW:
				System.out.println(coloredConsoleBoardDisplayer.display(board.flip()));
				System.out.println(gameRules.getOpponent(currentPlayer) + drawYesOrNoCommandInput.getHeading());
				if (drawYesOrNoCommandInput.getInput()) {
					System.out.println(coloredConsoleBoardDisplayer.display(board));
					throw new GameIsDrawException();
				}
				System.out.println(coloredConsoleBoardDisplayer.display(board));
				// loop around and try again.
				break;
			default:
				/**
				 * Loop around and try again.
				 */
				System.out.println("Unknown Command: " + input);
				// loop around and try again.
			}
		}
	}

	/**
	 * @param coloredConsoleBoardDisplayer
	 * @param gameRules
	 * @param moveManger
	 * @param multiCommandInput
	 * @param resignYesOrNoCommandInput
	 * @param drawYesOrNoCommandInput
	 * @param boardContainer
	 * @param currentPlayer
	 * @param boardCoordinateStack
	 * @throws ResignFromGameException
	 * @throws UnexpectedCellException
	 * @throws GameIsDrawException
	 * 
	 *                                 Repeat until I either make a successful jump
	 *                                 (at the designated coordinate) or I resign.
	 *                                 If I make a successful move, push previous
	 *                                 board and destination coordinate onto the
	 *                                 stack.
	 */
	private static void tryToMultiJump(final ColoredConsoleBoardDisplayer coloredConsoleBoardDisplayer,
			GameRules gameRules, final MoveManager moveManger, final MultiCommandInput multiCommandInput,
			YesOrNoCommandInput resignYesOrNoCommandInput, YesOrNoCommandInput drawYesOrNoCommandInput, Board board,
			Container<Board> boardContainer, CheckerColor currentPlayer,
			final Stack<BoardCoordinate> boardCoordinateStack)
			throws ResignFromGameException, UnexpectedCellException, GameIsDrawException {
		/**
		 * Status: There is another jump for the current Checker.
		 * 
		 * Attempt next jump.
		 */
		System.out.println("You must make another jump.");
		boolean done = false;
		while (!done) {
			System.out.println(multiCommandInput.getHeading());
			Command input = multiCommandInput.getInput(boardContainer.getValue());
			switch (input) {
			case RESIGN:
				System.out.println(resignYesOrNoCommandInput.getHeading());
				if (resignYesOrNoCommandInput.getInput()) {
					throw new ResignFromGameException();
				}
				// loop around and try again.
				break;
			case MOVE:
				final Board savedBoard = boardContainer.getValue().copy();
				try {
					moveManger.jump(boardContainer.getValue(), boardCoordinateStack.peek().getCoordinate(),
							currentPlayer, multiCommandInput.getDestinationCoordinate());
					boardCoordinateStack
							.push(new BoardCoordinate(savedBoard, multiCommandInput.getDestinationCoordinate()));
					System.out.println(coloredConsoleBoardDisplayer.display(boardContainer.getValue()));
					done = true;
				} catch (IncorrectDestinationException e) {
					handelIncorrectDestinationException(e);
					// loop around and try again.
				} catch (CellIsOccupiedException e) {
					handleCellIsOccupiedException(e, boardContainer.getValue().getColumnCount());
					// loop around and try again.
				}
				break;
			case UNDO:
				boardContainer.setValue(boardCoordinateStack.peek().getBoard());
				boardCoordinateStack.pop();
				System.out.println(coloredConsoleBoardDisplayer.display(boardContainer.getValue()));
				done = true;
				break;
			case DRAW:
				System.out.println(coloredConsoleBoardDisplayer.display(boardContainer.getValue().flip()));
				System.out.println(gameRules.getOpponent(currentPlayer) + drawYesOrNoCommandInput.getHeading());
				if (drawYesOrNoCommandInput.getInput()) {
					System.out.println(coloredConsoleBoardDisplayer.display(boardContainer.getValue()));
					throw new GameIsDrawException();
				}
				System.out.println(coloredConsoleBoardDisplayer.display(boardContainer.getValue()));
				// loop around and try again.
				break;
			default:
				System.out.println("Unknown Command: " + input);
				// loop around and try again.
			}
		}
	}

	/**
	 * @param coloredConsoleBoardDisplayer
	 * @param gameRules
	 * @param postCommandInput
	 * @param resignYesOrNoCommandInput
	 * @param drawYesOrNoCommandInput
	 * @param currentPlayer
	 * @param boardContainer
	 * @param boardCoordinateStack
	 * @param doneContainer
	 * @throws ResignFromGameException
	 * @throws GameIsDrawException
	 */
	private static void tryToCompleteJumps(final ColoredConsoleBoardDisplayer coloredConsoleBoardDisplayer,
			GameRules gameRules, final PostCommandInput postCommandInput, YesOrNoCommandInput resignYesOrNoCommandInput,
			YesOrNoCommandInput drawYesOrNoCommandInput, CheckerColor currentPlayer, Container<Board> boardContainer,
			final Stack<BoardCoordinate> boardCoordinateStack, Container<Boolean> doneContainer)
			throws ResignFromGameException, GameIsDrawException {
		boolean done = false;
		while (!done) {
			System.out.println(postCommandInput.getHeading());
			Command input = postCommandInput.getInput();
			switch (input) {
			case RESIGN:
				System.out.println(resignYesOrNoCommandInput.getHeading());
				if (resignYesOrNoCommandInput.getInput()) {
					throw new ResignFromGameException();
				}
				break;
			case DONE:
				done = true;
				/**
				 * The entire move is complete.
				 */
				doneContainer.setValue(true);
				break;
			case UNDO:
				/**
				 * Restore previous board and pop the stack.
				 */
				boardContainer.setValue(boardCoordinateStack.peek().getBoard());
				boardCoordinateStack.pop();
				System.out.println(coloredConsoleBoardDisplayer.display(boardContainer.getValue()));
				done = true;
				break;
			case DRAW:
				System.out.println(coloredConsoleBoardDisplayer.display(boardContainer.getValue().flip()));
				System.out.println(gameRules.getOpponent(currentPlayer) + drawYesOrNoCommandInput.getHeading());
				if (drawYesOrNoCommandInput.getInput()) {
					System.out.println(coloredConsoleBoardDisplayer.display(boardContainer.getValue()));
					throw new GameIsDrawException();
				}
				System.out.println(coloredConsoleBoardDisplayer.display(boardContainer.getValue()));
				// loop around and try again.
				break;
			default:
				System.out.println("Unknown Command: " + input);
			}
		}
	}

	private static void handleUnexpectedCellException(Board board, UnexpectedCellException e) {
		final String name = e.getName();
		final CheckerColor checkerColor = e.getCheckerColor();
		final Coordinate coordinate = e.getCoordinate();
		System.out.println("Expected " + (name == null || name.isEmpty() ? "" : name + " ") + "cell with "
				+ checkerColor + " checker, at location "
				+ CoordinateUtils.coordinateToIndex(board.getColumnCount(), coordinate) + ".");
	}

	private static void handleCellIsOccupiedException(CellIsOccupiedException e, int columnCount) {
		final Coordinate destinationCoordinate = e.getDestinationCoordinate();
		System.out.println("Expected empty cell at "
				+ CoordinateUtils.coordinateToIndex(columnCount, destinationCoordinate) + ".");
	}

	private static void handelIncorrectDestinationException(IncorrectDestinationException e) {
		final Checker checker = e.getChecker();
		final int distance = e.getDistance();
		final int changeX = e.getChangeX();
		final int changeY = e.getChangeY();
		System.out.println("Can only move " + (checker.getCheckerRank() == CheckerRank.REGULAR ? "forward " : "")
				+ "on a diagonal " + distance + " step" + (distance == 1 ? "" : "s") + ". changeX = " + changeX
				+ ", changeY = " + changeY);
	}
}
