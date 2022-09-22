package com.earl.nbycheckers3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.earl.nbynboard.Board;
import com.earl.nbynboard.Cell;
import com.earl.nbynboard.Checker;
import com.earl.nbynboard.CheckerColor;
import com.earl.nbynboard.CheckerRank;
import com.earl.nbyngamerules.CellIsOccupiedException;
import com.earl.nbyngamerules.GameRules;
import com.earl.nbyngamerules.IncorrectDestinationException;
import com.earl.nbyngamerules.MoveManager;
import com.earl.nbyngamerules.UnexpectedCellException;
import com.earl.nbyngamerules.base.BoardCoordinate;
import com.earl.utilities.Coordinate;

public class Checkers2 {

	/**
	 * The number of tiles on a side of a checker board (vertically and
	 * horizontally).
	 */
	private static final int SIDE_LENGTH = 8;

	/**
	 * The number of rows containing checkers in the initial state.
	 */
	private static final int ROWS_INITIALIZED = 3;

	/**
	 * Margins for the frame.
	 */
	private static final int LEFT_MARGIN = 10;
	private static final int TOP_MARGIN = 10;
	/**
	 * The length of a side of a (graphical) checker button.
	 */
	private static final int CHECKER_BUTTON_SIDE_LENGTH = 100;

	/**
	 * The column for the other buttons (to the right of the (graphical) board.
	 */
	private static final int OTHER_BUTTON_COL = 860;

	/**
	 * The dimensions of the other buttons.
	 */
	private static final int OTHER_BUTTON_WIDTH = 100;
	private static final int OTHER_BUTTON_LENGTH = 50;

	/**
	 * Icons represent various types of checkers.
	 */
	private static final Icon RED_REGULAR_ICON = new ImageIcon("src/main/resources/redRegular.jpg");
	private static final Icon RED_KING_ICON = new ImageIcon("src/main/resources/redKing.jpg");
	private static final Icon BLACK_REGULAR_ICON = new ImageIcon("src/main/resources/blackRegular.jpg");
	private static final Icon BLACK_KING_ICON = new ImageIcon("src/main/resources/blackKing.jpg");

	/**
	 * The row for the message label.
	 */
	private static final int MESSAGE_LABEL_ROW = 820;

	/**
	 * The dimensions of the message label.
	 */
	private static final int MESSAGE_LABEL_WIDTH = 800;
	private static final int MESSAGE_LABEL_HEIGHT = 20;

	/**
	 * The number of seconds allocated to a single turn of the game.
	 */
	private static final long DURATION = 60;

	private static final JFrame frame = new JFrame();

	/**
	 * These represent the legal tiles on our (graphical) checker board.
	 */
	private static final JButton[][] checkerButton2DArray = new JButton[SIDE_LENGTH][SIDE_LENGTH / 2];

	/**
	 * Designates whose turn if is (Red or Black).
	 */
	private static final JLabel turnHeaderLabel = new JLabel("Turn:");
	private static final JLabel turnLabel = new JLabel();

	/**
	 * Indicates how much time is left in the turn?
	 */
	private static final JLabel countDownLabel = new JLabel();

	/**
	 * The buttons to the right of the (graphical) checker board.
	 */
	private static final JButton doneButton = new JButton("DONE");
	private static final JButton undoButton = new JButton("UNDO");
	private static final JButton resignButton = new JButton("RESIGN");
	private static final JButton resetButton = new JButton("RESET");
	private static final JButton drawButton = new JButton("DRAW");

	/**
	 * Displays informational messages to the player.
	 */
	private static final JLabel messageHeaderLabel = new JLabel("Message: ");
	private static final JLabel messageLabel = new JLabel();

	/**
	 * Displays error messages to the player.
	 */
	private static final JLabel errorHeaderLabel = new JLabel("Error: ");
	private static final JLabel errorLabel = new JLabel();

	private static Board board = new Board(SIDE_LENGTH, SIDE_LENGTH, true);
	private static CheckerColor currentPlayer;
	/**
	 * Does this turn have a piece where the current player can jump?
	 */
	private static boolean thereIsAJump;
	private static GameState gameState;
	private static boolean timedOut;

	/**
	 * The location of the first click on the board.
	 */
	private static Coordinate sourceCoordinate;

	/**
	 * The location of the second click on the board.
	 */
	private static Coordinate destinationCoordinate;

	/**
	 * The saved board, before a move. It is used to perform an undo.
	 */
	private static Board savedBoard;

	/**
	 * The saved board and current coordinate, before a multi-jump. It is used to
	 * perform undoes.
	 */
	private static Stack<BoardCoordinate> boardCoordinateStack;

	/**
	 * How much time is left in the turn?
	 */
	private static Countdown countdown;

	/**
	 * Used to support mutual exclusion between the game and the countdown.
	 */
	private static Object theMonitor = new Object();

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.scan("com.earl.nbyngamerules.impl");
		try {
			context.refresh();
			/**
			 * Get the beans
			 */
			final GameRules gameRules = context.getBean(GameRules.class);
			final MoveManager moveManager = context.getBean(MoveManager.class);

			/**
			 * Initialize buttons in button2DArray
			 */
			for (int i = 0; i < SIDE_LENGTH; i++) {
				/**
				 * We start from the last row and work backwards. I'm not sure why.
				 */
				int i1 = SIDE_LENGTH - 1 - i;
				JButton[] checkerButtonArray = checkerButton2DArray[i1];
				for (int j = 0; j < SIDE_LENGTH / 2; j++) {
					checkerButtonArray[j] = new JButton();
					checkerButtonArray[j].setBounds(
							LEFT_MARGIN + 2 * CHECKER_BUTTON_SIDE_LENGTH * j
									+ (i1 % 2 == 0 ? 0 : CHECKER_BUTTON_SIDE_LENGTH),
							TOP_MARGIN + CHECKER_BUTTON_SIDE_LENGTH * i, CHECKER_BUTTON_SIDE_LENGTH,
							CHECKER_BUTTON_SIDE_LENGTH);
					checkerButtonArray[j].addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							/**
							 * Set i and j to the row and column of the designation tile/button in the 2D
							 * array.
							 */
							int i = 0;
							int j = 0;
							boolean found = false;
							while (i < SIDE_LENGTH && !found) {
								JButton[] checkerButtonArray = checkerButton2DArray[i];
								j = 0;
								while (j < SIDE_LENGTH / 2 && !found) {
									if (e.getSource() == checkerButtonArray[j]) {
										found = true;
									} else {
										j++;
									}
								}
								if (!found) {
									i++;
								}
							}
							final Coordinate currentCoordinate = buttonIndicesToBoardCoordinate(i, j);
							synchronized (theMonitor) {
								if (!timedOut) {
									switch (gameState) {
									case WAIT_FOR_1ST_MOVE_COORDINATE:
										Cell cell = gameRules.get(board, currentCoordinate);
										if (cell.isOccupied() && cell.getChecker().getCheckerColor() == currentPlayer) {
											sourceCoordinate = currentCoordinate;
											gameState = GameState.WAIT_FOR_2ND_MOVE_COORDINATE;
											messageLabel.setText("Where do you want the checker to go?");
											errorLabel.setText("");
										} else {
											waitFor1stMoveCoordinate();
											errorLabel
													.setText("That cell doesn't have a " + currentPlayer + " checker");
										}
										break;
									case WAIT_FOR_2ND_MOVE_COORDINATE:
										destinationCoordinate = currentCoordinate;
										if (thereIsAJump) {
											savedBoard = board.copy();
											try {
												moveManager.jump(board, sourceCoordinate, currentPlayer,
														destinationCoordinate);
												refreshGUIBoard(gameRules);
												if (moveManager.canMakeAJump(board, destinationCoordinate,
														currentPlayer)) {
													waitForSubsequentMoveCoordinate(false);
												} else {
													moveVerdict();
												}
											} catch (UnexpectedCellException e1) {
												handleUnexpectedCellException(e1);
												waitFor1stMoveCoordinate();
											} catch (IncorrectDestinationException e1) {
												handelIncorrectDestinationException(e1);
												waitFor1stMoveCoordinate();
											} catch (CellIsOccupiedException e1) {
												handleCellIsOccupiedException(e1);
												waitFor1stMoveCoordinate();
											}
										} else {
											try {
												savedBoard = board.copy();
												moveManager.move(board, sourceCoordinate, currentPlayer,
														destinationCoordinate);
												refreshGUIBoard(gameRules);
												moveVerdict();
											} catch (UnexpectedCellException e1) {
												handleUnexpectedCellException(e1);
												waitFor1stMoveCoordinate();
											} catch (IncorrectDestinationException e1) {
												handelIncorrectDestinationException(e1);
												waitFor1stMoveCoordinate();
											} catch (CellIsOccupiedException e1) {
												handleCellIsOccupiedException(e1);
												waitFor1stMoveCoordinate();
											}
										}
										break;
									case WAIT_FOR_SUBSEQUENT_MOVE_COORDINATE:
										destinationCoordinate = currentCoordinate;
										try {
											final Board localSavedBoard = board.copy();
											moveManager.jump(board, sourceCoordinate, currentPlayer,
													destinationCoordinate);
											boardCoordinateStack
													.push(new BoardCoordinate(localSavedBoard, sourceCoordinate));
											if (moveManager.canMakeAJump(board, destinationCoordinate, currentPlayer)) {
												waitForSubsequentMoveCoordinate(false);
											} else {
												moveVerdict();
											}
										} catch (UnexpectedCellException e1) {
											handleUnexpectedCellException(e1);
										} catch (IncorrectDestinationException e1) {
											handelIncorrectDestinationException(e1);
										} catch (CellIsOccupiedException e1) {
											handleCellIsOccupiedException(e1);
										}
										refreshGUIBoard(gameRules);
										break;
									default:
										// ignore other states
										break;
									} // switch
								} // if
							} // synchronized
						}
					} // action listener declaration
					);
					frame.add(checkerButtonArray[j]);
				} // for
			} // for

			turnHeaderLabel.setBounds(OTHER_BUTTON_COL, TOP_MARGIN, OTHER_BUTTON_WIDTH / 2, OTHER_BUTTON_LENGTH / 2);
			frame.add(turnHeaderLabel);

			turnLabel.setBounds(OTHER_BUTTON_COL + OTHER_BUTTON_WIDTH / 2, TOP_MARGIN, OTHER_BUTTON_WIDTH / 2,
					OTHER_BUTTON_LENGTH / 2);
			frame.add(turnLabel);

			countDownLabel.setBounds(OTHER_BUTTON_COL, TOP_MARGIN + 10, OTHER_BUTTON_WIDTH, OTHER_BUTTON_LENGTH);
			frame.add(countDownLabel);

			doneButton.setBounds(OTHER_BUTTON_COL, TOP_MARGIN + 50, OTHER_BUTTON_WIDTH, OTHER_BUTTON_LENGTH);
			doneButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					synchronized (theMonitor) {
						if (!timedOut) {
							switch (gameState) {
							case WAIT_FOR_1ST_MOVE_COORDINATE:
							case WAIT_FOR_2ND_MOVE_COORDINATE:
								errorLabel.setText("System error: done button should be disabled.");
								break;
							case MOVE_VERDICT:
								if (gameRules.isWinner(board, currentPlayer)) {
									gameState = GameState.GAME_OVER;
									messageLabel.setText(currentPlayer + " has won.");
									errorLabel.setText("");
									doneButton.setEnabled(false);
									undoButton.setEnabled(false);
									resignButton.setEnabled(false);
									resetButton.setEnabled(true);
									drawButton.setEnabled(false);
								} else {
									// opponent's turn.
									countdown.reset();
									currentPlayer = gameRules.getOpponent(currentPlayer);
									turnLabel.setText(currentPlayer.toString());
									board = board.flip();
									thereIsAJump = moveManager.thereIsAJump(board, currentPlayer);
									boardCoordinateStack = new Stack<>();
									refreshGUIBoard(gameRules);
									waitFor1stMoveCoordinate();
								}
								break;
							default:
								break;
							} // switch
						} // if
					} // synchronized
				}
			} // action listener declaration
			);
			frame.add(doneButton);

			undoButton.setBounds(OTHER_BUTTON_COL, TOP_MARGIN + 100, OTHER_BUTTON_WIDTH, OTHER_BUTTON_LENGTH);
			undoButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					synchronized (theMonitor) {
						if (!timedOut) {
							switch (gameState) {
							case WAIT_FOR_1ST_MOVE_COORDINATE:
							case WAIT_FOR_2ND_MOVE_COORDINATE:
								errorLabel.setText("System error: button should have been disabled.");
								break;
							case MOVE_VERDICT:
							case WAIT_FOR_SUBSEQUENT_MOVE_COORDINATE:
								if (boardCoordinateStack.isEmpty()) {
									board = savedBoard;
									refreshGUIBoard(gameRules);
									waitFor1stMoveCoordinate();
								} else {
									final BoardCoordinate boardCoordinate = boardCoordinateStack.peek();
									board = boardCoordinate.getBoard();
									sourceCoordinate = boardCoordinate.getCoordinate();
									refreshGUIBoard(gameRules);
									boardCoordinateStack.pop();
									waitForSubsequentMoveCoordinate(true);
								}
								break;
							default:
								break;
							} // switch
						} // if
					} // synchronize
				} // action performed
			} // action listener declaration
			);
			frame.add(undoButton);

			resignButton.setBounds(OTHER_BUTTON_COL, TOP_MARGIN + 150, OTHER_BUTTON_WIDTH, OTHER_BUTTON_LENGTH);
			resignButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					int result = JOptionPane.showConfirmDialog(null, "Are you sure?");
					if (result == JOptionPane.YES_NO_OPTION) {
						synchronized (theMonitor) {
							resignGame(gameRules, "resigned");
						}
					}
				}
			});
			frame.add(resignButton);

			resetButton.setBounds(OTHER_BUTTON_COL, TOP_MARGIN + 200, OTHER_BUTTON_WIDTH, OTHER_BUTTON_LENGTH);
			resetButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					newGame(gameRules, moveManager);
				}
			});
			frame.add(resetButton);

			drawButton.setBounds(OTHER_BUTTON_COL, 260, OTHER_BUTTON_WIDTH, OTHER_BUTTON_LENGTH);
			drawButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					board = board.flip();
					refreshGUIBoard(gameRules);
					countdown.pause();
					int result = JOptionPane.showConfirmDialog(null,
							gameRules.getOpponent(currentPlayer) + " would you accept a draw?");
					board = board.flip();
					refreshGUIBoard(gameRules);
					if (result == JOptionPane.YES_NO_OPTION) {
						synchronized (theMonitor) {
							gameState = GameState.GAME_OVER;
							messageLabel.setText("The game ends in a draw.");
							errorLabel.setText("");
							doneButton.setEnabled(false);
							undoButton.setEnabled(false);
							resignButton.setEnabled(false);
							drawButton.setEnabled(false);
							resetButton.setEnabled(true);
							countdown.stop();
						}
					} else {
						countdown.resume();
					}
				}
			});
			frame.add(drawButton);

			messageHeaderLabel.setBounds(LEFT_MARGIN, MESSAGE_LABEL_ROW, 100, MESSAGE_LABEL_HEIGHT);
			frame.add(messageHeaderLabel);
			messageLabel.setBounds(110, MESSAGE_LABEL_ROW, MESSAGE_LABEL_WIDTH, MESSAGE_LABEL_HEIGHT);
			frame.add(messageLabel);

			errorHeaderLabel.setBounds(LEFT_MARGIN, MESSAGE_LABEL_ROW + MESSAGE_LABEL_HEIGHT, 100,
					MESSAGE_LABEL_HEIGHT);
			frame.add(errorHeaderLabel);
			errorLabel.setBounds(110, MESSAGE_LABEL_ROW + MESSAGE_LABEL_HEIGHT, MESSAGE_LABEL_WIDTH,
					MESSAGE_LABEL_HEIGHT);
			frame.add(errorLabel);

			newGame(gameRules, moveManager);

			frame.setLayout(null);
			frame.setSize(1000, 900);
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch (BeansException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} finally {
			context.close();
		}
	}

	/**
	 * @param gameRules
	 * @param moveManager
	 */
	private static void newGame(final GameRules gameRules, MoveManager moveManager) {
		gameRules.initialize(board, ROWS_INITIALIZED);
		refreshGUIBoard(gameRules);
		currentPlayer = CheckerColor.RED;
		turnLabel.setText(currentPlayer.toString());
		thereIsAJump = moveManager.thereIsAJump(board, currentPlayer);
		boardCoordinateStack = new Stack<>();
		waitFor1stMoveCoordinate();
		timedOut = false;
		if (countdown != null) {
			countdown.stop();
		}
		countdown = new Countdown(DURATION, countDownLabel, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized (theMonitor) {
					if (!timedOut) {
						if (gameState != GameState.GAME_OVER) {
							resignGame(gameRules, "timed out");
						}
						timedOut = true;
					}
				}
			}
		});
		countdown.start();
	}

	private static Coordinate buttonIndicesToBoardCoordinate(int i, int j) {
		return new Coordinate(i, (2 * j + (i % 2 == 0 ? 0 : 1)));
	}

	private static void refreshGUIBoard(GameRules gameRules) {
		for (int i = 0; i < SIDE_LENGTH; i++) {
			/**
			 * We start from the last row and work backwards. I'm not sure why.
			 */
			int i1 = SIDE_LENGTH - 1 - i;
			JButton[] checkerButtonArray = checkerButton2DArray[i1];
			for (int j = 0; j < SIDE_LENGTH / 2; j++) {
				final Coordinate coordinate = buttonIndicesToBoardCoordinate(i1, j);
				final Cell cell = gameRules.get(board, coordinate);
				if (cell.isOccupied()) {
					Checker checker = cell.getChecker();
					Icon icon;
					switch (checker.getCheckerColor()) {
					case RED:
						if (checker.getCheckerRank() == CheckerRank.KING) {
							icon = RED_KING_ICON;
						} else {
							icon = RED_REGULAR_ICON;
						}
						break;
					case BLACK:
						if (checker.getCheckerRank() == CheckerRank.KING) {
							icon = BLACK_KING_ICON;
						} else {
							icon = BLACK_REGULAR_ICON;
						}
						break;
					default:
						throw new RuntimeException("Unrecognized checker color: " + checker.getCheckerColor());
					}
					checkerButtonArray[j].setIcon(icon);
				} else {
					checkerButtonArray[j].setIcon(null);
				}
			} // for
		} // for
	}

	/**
	 * 
	 */
	private static void waitFor1stMoveCoordinate() {
		gameState = GameState.WAIT_FOR_1ST_MOVE_COORDINATE;
		doneButton.setEnabled(false);
		undoButton.setEnabled(false);
		resignButton.setEnabled(true);
		resetButton.setEnabled(false);
		drawButton.setEnabled(true);
		if (thereIsAJump) {
			messageLabel.setText("Click checker that will perform the jump.");
		} else {
			messageLabel.setText("Click checker to be moved.");
		}
	}

	private static void waitForSubsequentMoveCoordinate(boolean fromUndo) {
		gameState = GameState.WAIT_FOR_SUBSEQUENT_MOVE_COORDINATE;
		/**
		 * When ! fromUndo, we need to “move forward” and update the sourceCoordinate.
		 * Otherwise, the stack has already set sourceCoordinate and it doesn’t need to
		 * change.
		 */
		if (!fromUndo) {
			sourceCoordinate = destinationCoordinate;
		}
		doneButton.setEnabled(false);
		undoButton.setEnabled(true);
		messageLabel.setText("You must make another jump.");
		errorLabel.setText("");
	}

	/**
	 * 
	 */
	private static void moveVerdict() {
		gameState = GameState.MOVE_VERDICT;
		messageLabel.setText("Are you satisfied with your move?");
		doneButton.setEnabled(true);
		undoButton.setEnabled(true);
	}

	/**
	 * @param gameRules
	 * @param event
	 */
	private static void resignGame(final GameRules gameRules, String event) {
		gameState = GameState.GAME_OVER;
		messageLabel
				.setText(currentPlayer + " has " + event + ". " + gameRules.getOpponent(currentPlayer) + " has won.");
		errorLabel.setText("");
		doneButton.setEnabled(false);
		undoButton.setEnabled(false);
		resignButton.setEnabled(false);
		resetButton.setEnabled(true);
		drawButton.setEnabled(false);
		countdown.stop();
	}

	private static void handleUnexpectedCellException(UnexpectedCellException e) {
		final String name = e.getName();
		final CheckerColor checkerColor = e.getCheckerColor();
		final Coordinate coordinate = e.getCoordinate();
		errorLabel
				.setText("Expected " + (name == null || name.isEmpty() ? "" : name + " ") + "cell with " + checkerColor
						+ " checker, at location (" + (coordinate.getX() + 1) + ", " + (coordinate.getY() + 1) + ").");
	}

	private static void handleCellIsOccupiedException(CellIsOccupiedException e) {
		final Coordinate destinationCoordinate = e.getDestinationCoordinate();
		errorLabel.setText("Expected empty cell at (" + (destinationCoordinate.getX() + 1) + ", "
				+ (destinationCoordinate.getY() + 1) + ").");
	}

	private static void handelIncorrectDestinationException(IncorrectDestinationException e) {
		final Checker checker = e.getChecker();
		final int distance = e.getDistance();
		final int changeX = e.getChangeX();
		final int changeY = e.getChangeY();
		errorLabel.setText("Can only move " + (checker.getCheckerRank() == CheckerRank.REGULAR ? "forward " : "")
				+ "on a diagonal " + distance + " step" + (distance == 1 ? "" : "s") + ". changeX = " + changeX
				+ ", changeY = " + changeY);
	}
}
