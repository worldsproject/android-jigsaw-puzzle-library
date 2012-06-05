package org.worldsproject.puzzle;

import org.worldsproject.type_puzzle.PuzzleActivity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class PuzzleView extends View implements OnGestureListener,
OnDoubleTapListener
{
	private static final String DEBUG = "PuzzleView";
	private Resources r;
	private Puzzle puzzle;
	private GestureDetector gesture;
	
	
	public PuzzleView(Context context)
	{
		super(context);
		r = context.getResources();
		
		loadPuzzle();
	}
	
	

	public PuzzleView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		r = context.getResources();
		
		loadPuzzle();
	}



	public PuzzleView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		r = context.getResources();
		
		loadPuzzle();
	}

	private void loadPuzzle()
	{
		gesture = new GestureDetector(this.getContext(), this);
		
		Bitmap[] monsters = 
			{
				BitmapFactory.decodeResource(r, R.drawable.monster1),
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
//				BitmapFactory.decodeResource(r, R.drawable.monster12),
			};
		
		WindowManager wm = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		int width = display.getWidth();
		int height = display.getHeight();
		
		puzzle = new Puzzle(monsters, 1, 1, width, height);
	}

	@Override
	public void onDraw(Canvas canvas)
	{
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
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean onDoubleTapEvent(MotionEvent e)
	{
		Log.v(DEBUG, "Double Click");
		
		for(Piece p: this.puzzle.getPieces())
		{
			if(p.inMe((int)e.getX(), (int)e.getY()))
					p.turn();
			this.invalidate();
			break;
		}
		return true;
	}



	@Override
	public boolean onSingleTapConfirmed(MotionEvent e)
	{
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean onDown(MotionEvent arg0)
	{
		return true;
	}



	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY)
	{
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void onLongPress(MotionEvent e)
	{
		// TODO Auto-generated method stub
		
	}



	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY)
	{
		//Get the piece that is under this tap.
		Piece tapped = null;
		
		for(Piece p: this.puzzle.getPieces())
		{
			if(p.inMe((int)e1.getX(), (int)e1.getY()))
			{
				Log.v(DEBUG, "In a piece");
				tapped = p;
				break;
			}
		}

		if(tapped == null) //We aren't hitting a piece
		{
			return false;
		}
		else
		{
			tapped.setX(tapped.getX() - (int)distanceX);
			tapped.setY(tapped.getY() - (int)distanceY);
			Log.v(DEBUG, "Moving a piece");
			this.invalidate();
		}
		return true;
	}



	@Override
	public void onShowPress(MotionEvent e)
	{
		// TODO Auto-generated method stub
		
	}



	@Override
	public boolean onSingleTapUp(MotionEvent e)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
