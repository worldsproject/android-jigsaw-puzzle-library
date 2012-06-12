package org.worldsproject.puzzle;

import org.worldsproject.puzzle.enums.Difficulty;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public class PuzzleView extends View implements OnGestureListener, OnDoubleTapListener
{
	private Puzzle puzzle;
	private GestureDetector gesture;
	private Piece tapped;
	private boolean firstDraw = true;
	private float scale = 1.0f;

	public PuzzleView(Context context)
	{
		super(context);

		loadPuzzle();
	}

	public PuzzleView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		loadPuzzle();
	}

	public PuzzleView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		loadPuzzle();
	}

	private void loadPuzzle()
	{
		gesture = new GestureDetector(this.getContext(), this);

//		Bitmap[] monsters = {
//				BitmapFactory.decodeResource(r, R.drawable.monster1),
//				BitmapFactory.decodeResource(r, R.drawable.monster2),
//				BitmapFactory.decodeResource(r, R.drawable.monster3),
//				BitmapFactory.decodeResource(r, R.drawable.monster4),
//				BitmapFactory.decodeResource(r, R.drawable.monster5),
//				BitmapFactory.decodeResource(r, R.drawable.monster6),
//				BitmapFactory.decodeResource(r, R.drawable.monster7),
//				BitmapFactory.decodeResource(r, R.drawable.monster8),
//				BitmapFactory.decodeResource(r, R.drawable.monster9),
//				BitmapFactory.decodeResource(r, R.drawable.monster10),
//				BitmapFactory.decodeResource(r, R.drawable.monster11),
//				BitmapFactory.decodeResource(r, R.drawable.monster12), };
//
//		puzzle = new Puzzle(monsters, 4);
//		
		Bitmap monster = BitmapFactory.decodeResource(this.getResources(), R.drawable.monster);
		
		puzzle = new PuzzleGenerator(this.getContext()).generatePuzzle(monster, Difficulty.HARD);
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		
		if (firstDraw)
		{
			firstDraw = false;
			puzzle.shuffle(this.getWidth(), this.getHeight());
		}
		canvas.scale(scale, scale);
		puzzle.draw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return gesture.onTouchEvent(event);
	}

	@Override
	public boolean onDoubleTap(MotionEvent arg0)
	{
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e)
	{

		for (Piece p : this.puzzle.getPieces())
		{
			if (p.inMe((int) e.getX(), (int) e.getY()))
			{
				p.turn();
			}
			this.invalidate();
			break;
		}
		return true;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e)
	{
		if (checkSurroundings(tapped))
			this.invalidate();

		return true;
	}

	@Override
	public boolean onDown(MotionEvent e1)
	{
		// Get the piece that is under this tap.
		Piece possibleNewTapped = null;
		boolean shouldPan = true;
		for (Piece p : this.puzzle.getPieces())
		{
			if (p.inMe((int) (e1.getX()/scale), (int) (e1.getY()/scale)))
			{
				if (p == tapped)
				{
					possibleNewTapped = null;
					break;
				}
				else
				{
					possibleNewTapped = p;
				}
				shouldPan = false;
				break;
			}
		}
		
		if (possibleNewTapped != null)
		{
			tapped = possibleNewTapped;
		}
		
		if(shouldPan)
		{
			tapped = null;
		}

		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY)
	{
		if (checkSurroundings(tapped))
			this.invalidate();

		return true;
	}

	@Override
	public void onLongPress(MotionEvent e)
	{

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY)
	{
		if (tapped == null) // We aren't hitting a piece
		{
			puzzle.translate(distanceX/scale, distanceY/scale);
			
			this.invalidate();
		}
		else
		{
			tapped.getGroup().translate((int) (distanceX/scale), (int) (distanceY/scale));

			this.invalidate();
		}
		return true;
	}

	private boolean checkSurroundings(Piece tapped)
	{
		if (tapped == null || tapped.getOrientation() != 0)
		{
			return false;
		}

		boolean rv = false;

		if (tapped.inLeft())
		{
			tapped.snap(tapped.getLeft());
			rv = true;
		}

		if (tapped.inRight())
		{
			tapped.snap(tapped.getRight());
			rv = true;
		}

		if (tapped.inBottom())
		{
			tapped.snap(tapped.getBottom());
			rv = true;
		}

		if (tapped.inTop())
		{
			tapped.snap(tapped.getTop());
			rv = true;
		}

		return rv;
	}

	@Override
	public void onShowPress(MotionEvent e)
	{
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e)
	{
		if (checkSurroundings(tapped))
			this.invalidate();

		return true;
	}

	public void zoomIn()
	{
		scale += 0.1;
		this.invalidate();
	}

	public void zoomOut()
	{
		scale -= 0.1;
		this.invalidate();
	}
}
