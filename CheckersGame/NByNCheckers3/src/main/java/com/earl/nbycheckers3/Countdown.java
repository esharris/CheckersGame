package com.earl.nbycheckers3;

import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JLabel;

public class Countdown {

	private static final long MILLISECOND_PER_SECOND = 1000;

	private static final String SUFFIX = " secs.";

	/**
	 * How long the timer should go (in seconds).
	 */
	private final long duration;

	private Thread thread;

	/**
	 * Should the timer continue to count down?
	 */
	boolean active;

	/**
	 * If ! active, the amount of time remaining (in milliseconds) before (upon
	 * resumption) there is a time out. Otherwise null.
	 */
	Long remainingTime;

	/**
	 * The date where the timer should stop.
	 */
	private Date endDate;

	private boolean shouldTimeout;

	/**
	 * is the timer ready to stop?
	 */
	private boolean done;

	public Countdown(long duration, JLabel label, ActionListener actionListener) {
		this.duration = duration;

		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					final Calendar cal0 = Calendar.getInstance();
					endDate = new Date(cal0.getTimeInMillis() + duration * MILLISECOND_PER_SECOND);

					active = true;
					remainingTime = null;
					shouldTimeout = true;
					done = false;
					while (!done) {
						final Calendar cal = Calendar.getInstance();
						final Date date = cal.getTime();
						if (active) {
							if (remainingTime != null) {
								endDate = new Date(cal.getTimeInMillis() + remainingTime);
								remainingTime = null;
							}
							label.setText(Long.toString((endDate.getTime() - date.getTime()) / MILLISECOND_PER_SECOND)
									+ SUFFIX);
							Thread.sleep(1000);
							if (date.compareTo(endDate) > 0) {
								label.setText("0" + SUFFIX);
								done = true;
							}
						} else {
							if (remainingTime == null) {
								remainingTime = endDate.getTime() - date.getTime();
							}
						}
					}
					if (shouldTimeout) {
						actionListener.actionPerformed(null);
					}
				} catch (InterruptedException e) {
				} // interval given in milliseconds
			}

		});
	}

	public void start() {
		thread.start();
	}

	public void stop() {
		shouldTimeout = false;
		done = true;
	}

	public void pause() {
		active = false;
	}

	public void resume() {
		active = true;
	}

	public void reset() {
		final Calendar cal0 = Calendar.getInstance();
		endDate = new Date(cal0.getTimeInMillis() + duration * MILLISECOND_PER_SECOND);
	}
}
