package org.worldsproject.puzzle;

import org.worldsproject.puzzle.enums.Difficulty;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class PuzzleView extends View implements OnGestureListener,
		OnDoubleTapListener, OnScaleGestureListener, AnimationListener {
	private Puzzle puzzle;
	private GestureDetector gesture;
	private ScaleGestureDetector scaleGesture;
	private Piece tapped;
	private boolean firstDraw = true;
	private float scale = 1.0f;

	public PuzzleView(Context context) {
		super(context);
	}

	public PuzzleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PuzzleView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void loadPuzzle(Bitmap image, Difficulty difficulty, String location) {
		gesture = new GestureDetector(this.getContext(), this);
		scaleGesture = new ScaleGestureDetector(this.getContext(), this);

		puzzle = new PuzzleGenerator(this.getContext()).generatePuzzle(
				this.getContext(), image, difficulty, location);
		puzzle.savePuzzle(getContext(), location, true);
		
		Piece.resetSerial();
	}

	public void loadPuzzle(String location) {
		gesture = new GestureDetector(this.getContext(), this);
		scaleGesture = new ScaleGestureDetector(this.getContext(), this);

		puzzle = new Puzzle(this.getContext(), location);
		firstDraw = false;
		
		Piece.resetSerial();
	}

	public void savePuzzle(String location) {
		puzzle.savePuzzle(this.getContext(), location, false);
	}

	@Override
	public void onDraw(Canvas canvas) {

		if (firstDraw) {
			firstDraw = false;
			puzzle.shuffle(this.getWidth(), this.getHeight());
		}
		canvas.scale(scale, scale);
		puzzle.draw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		scaleGesture.onTouchEvent(event);
		gesture.onTouchEvent(event);
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent arg0) {
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		return true;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		if (checkSurroundings(tapped))
			this.invalidate();

		return true;
	}

	@Override
	public boolean onDown(MotionEvent e1) {
		// Get the piece that is under this tap.
		Piece possibleNewTapped = null;
		boolean shouldPan = true;

		for (int i = this.puzzle.getPieces().size()-1; i >= 0; i--) {
			Piece p = this.puzzle.getPieces().get(i);
			
			if (p.inMe((int) (e1.getX() / scale), (int) (e1.getY() / scale))) {
				if (p == tapped) {
					possibleNewTapped = null;
					break;
				} else {
					possibleNewTapped = p;
				}
				shouldPan = false;
				break;
			}
		}

		if (possibleNewTapped != null) {
			tapped = possibleNewTapped;
		}

		if (shouldPan) {
			tapped = null;
		}

		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (checkSurroundings(tapped))
			this.invalidate();

		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		if (tapped == null) {// We aren't hitting a piece
			puzzle.translate(distanceX / scale, distanceY / scale);
		} else {
			tapped.getGroup().translate((int) (distanceX / scale),
					(int) (distanceY / scale));
		}

		this.invalidate();
		return true;
	}

	private boolean checkSurroundings(Piece tapped) {
		if (tapped == null || tapped.getOrientation() != 0) {
			return false;
		}

		boolean rv = false;

		if (tapped.inLeft()) {
			tapped.snap(tapped.getLeft());
			rv = true;
		}

		if (tapped.inRight()) {
			tapped.snap(tapped.getRight());
			rv = true;
		}

		if (tapped.inBottom()) {
			tapped.snap(tapped.getBottom());
			rv = true;
		}

		if (tapped.inTop()) {
			tapped.snap(tapped.getTop());
			rv = true;
		}

		return rv;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		if (checkSurroundings(tapped))
			this.invalidate();
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		if (checkSurroundings(tapped))
			this.invalidate();

		return true;
	}

	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		scale *= detector.getScaleFactor();
		this.invalidate();
		return true;
	}

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		return true;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {
	}

	public void shuffle() {
		puzzle.shuffle(this.getWidth(), this.getHeight());
		this.invalidate();
	}

	public void solve() {
		Animation a = new AlphaAnimation(1, 0);
		a.setDuration(2000);
		a.setAnimationListener(this);
		this.startAnimation(a);
		
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		this.puzzle.solve();
		Piece zero = this.puzzle.getPieces().get(0);
		zero.getGroup().translate(zero.getX()+5, zero.getY()+5);
		this.invalidate();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}
}
