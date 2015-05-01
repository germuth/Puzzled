package ca.germuth.puzzled.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.TextView;
import java.text.DecimalFormat;

/**
 * TextView that runs it own thread to update its current text.
 * Can count down or up. At every tick, or when clock finishes,
 * a Listener is fired through OnChronometerFinishListener for ex.
 * 
 * Modified from The Android Chronometer widget
 * @author Aaron
 */
public class Chronometer extends TextView {
	public interface OnChronometerTickListener {
		void onChronometerTick(Chronometer chronometer);
	}

	public interface OnChronometerFinishListener {
		void onChronometerFinish(Chronometer chronometer);
	}

	private long mBase;
	private boolean mVisible;
	private boolean mStarted;
	private boolean mRunning;
	private boolean mCountingDown;
	private long mLimit;
	private OnChronometerFinishListener mOnChronometerFinishListener;

	private static final int TICK_WHAT = 2;

	private long timeElapsed;

	public Chronometer(Context context) {
		this(context, null, 0);
	}

	public Chronometer(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public Chronometer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mBase = SystemClock.elapsedRealtime();
		updateText(mBase);

	}

	// TODO: perhaps move to onWIndow Size Changed
	// @Override
	// public void onWindowFocusChanged(boolean hasWindowFocus) {
	// super.onWindowFocusChanged(hasWindowFocus);
	//
	// int width = this.getWidth();
	//
	// int density = (int)getResources().getDisplayMetrics().density;
	// int dpWidth = width / density;
	//
	// int extraOnEitherSide = dpWidth - DP_WIDTH_OF_TEXT;
	// extraOnEitherSide /= 2;
	// Utils.setMargins(this, extraOnEitherSide, 0, 0, 0);
	// Utils.setMargins(this, 0, 0, extraOnEitherSide, 0);
	// }

	public void setBase(long base) {
		mBase = base;
		updateText(SystemClock.elapsedRealtime());
	}

	public long getBase() {
		return mBase;
	}

	public void startCountingDown(long milliTime) {
		mBase = SystemClock.elapsedRealtime();
		mStarted = true;
		mCountingDown = true;
		mLimit = milliTime;
		updateRunning();
	}

	public void startCountingUp() {
		mBase = SystemClock.elapsedRealtime();
		mStarted = true;
		mCountingDown = false;
		updateRunning();
	}

	public void stop() {
		mStarted = false;
		updateRunning();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mVisible = false;
		updateRunning();
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		mVisible = visibility == VISIBLE;
		updateRunning();
	}

	private synchronized void updateText(long now) {
		if (mCountingDown) {
			timeElapsed = now - mBase;
			timeElapsed = mLimit - timeElapsed;

			if (timeElapsed < 0) {
				this.stop();
				dispatchChronometerFinish();
			}
		} else {
			timeElapsed = now - mBase;
		}

		DecimalFormat df = new DecimalFormat("00");

		int hours = (int) (timeElapsed / (3600 * 1000));
		int remaining = (int) (timeElapsed % (3600 * 1000));

		int minutes = (int) (remaining / (60 * 1000));
		remaining = (int) (remaining % (60 * 1000));

		int seconds = (int) (remaining / 1000);
		remaining = (int) (remaining % (1000));
		// TODO make option to decide how many digits you want to show
		int milliseconds = (int) (((int) timeElapsed % 1000) / 10);

		String text = "";

		if (hours > 0) {
			text += df.format(hours) + ":";
		}

		text += df.format(minutes) + ":";
		text += df.format(seconds) + ":";
		text += Integer.toString(milliseconds);
		if (milliseconds < 10) {
			text += "0";
		}
		setText(text);
	}

	private void updateRunning() {
		boolean running = mVisible && mStarted;
		if (running != mRunning) {
			if (running) {
				updateText(SystemClock.elapsedRealtime());
				mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), 50);
			} else {
				mHandler.removeMessages(TICK_WHAT);
			}
			mRunning = running;
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message m) {
			if (mRunning) {
				updateText(SystemClock.elapsedRealtime());
				sendMessageDelayed(Message.obtain(this, TICK_WHAT), 50);
			}
		}
	};

	private void dispatchChronometerFinish() {
		if (mOnChronometerFinishListener != null) {
			mOnChronometerFinishListener.onChronometerFinish(this);
		}
	}

	public long getTimeElapsed() {
		return timeElapsed;
	}

	public OnChronometerFinishListener getOnChronometerFinishListener() {
		return mOnChronometerFinishListener;
	}

	public void setOnChronometerFinishListener(
			OnChronometerFinishListener mOnChronometerFinishListener) {
		this.mOnChronometerFinishListener = mOnChronometerFinishListener;
	}
}
