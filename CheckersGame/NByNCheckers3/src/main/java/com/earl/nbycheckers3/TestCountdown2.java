package com.earl.nbycheckers3;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class TestCountdown2 {

	private JFrame frame;
	private JLabel displayLabel;
	private JButton resetButton;
	private JButton stopButton;
	private JButton pauseButton;
	private JButton resumeButton;

	private Countdown countdown;

	TestCountdown2() {
		frame = new JFrame();

		displayLabel = new JLabel();
		displayLabel.setBounds(100, 100, 100, 50);
		frame.add(displayLabel);

		resetButton = new JButton();
		resetButton.setBounds(100, 150, 100, 50);
		resetButton.setText("Reset");
		resetButton.addActionListener(e -> countdown.reset());
		frame.add(resetButton);

		stopButton = new JButton();
		stopButton.setBounds(100, 200, 100, 50);
		stopButton.setText("Stop");
		stopButton.addActionListener(e -> countdown.stop());
		frame.add(stopButton);

		pauseButton = new JButton();
		pauseButton.setBounds(100, 250, 100, 50);
		pauseButton.setText("Pause");
		pauseButton.addActionListener(e -> countdown.pause());
		frame.add(pauseButton);

		resumeButton = new JButton();
		resumeButton.setBounds(100, 300, 100, 50);
		resumeButton.setText("Resume");
		resumeButton.addActionListener(e -> countdown.resume());
		frame.add(resumeButton);

		countdown = new Countdown(10, displayLabel, e -> displayLabel.setText("Timed out!"));

		frame.setSize(300, 400);
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		countdown.start();
	}

	public static void main(String[] args) {
		new TestCountdown2();
	}

}
