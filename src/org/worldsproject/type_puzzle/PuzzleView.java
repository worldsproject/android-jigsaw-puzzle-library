package org.worldsproject.type_puzzle;

import org.worldsproject.puzzle.Puzzle;
import org.worldsproject.puzzle.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class PuzzleView extends View
{
	private Resources r;
	private Puzzle puzzle;
	
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
		Bitmap[] monsters = 
			{
				BitmapFactory.decodeResource(r, R.drawable.monster1),
				BitmapFactory.decodeResource(r, R.drawable.monster2),
				BitmapFactory.decodeResource(r, R.drawable.monster3),
				BitmapFactory.decodeResource(r, R.drawable.monster4),
				BitmapFactory.decodeResource(r, R.drawable.monster5),
				BitmapFactory.decodeResource(r, R.drawable.monster6),
				BitmapFactory.decodeResource(r, R.drawable.monster7),
				BitmapFactory.decodeResource(r, R.drawable.monster8),
				BitmapFactory.decodeResource(r, R.drawable.monster9),
				BitmapFactory.decodeResource(r, R.drawable.monster10),
				BitmapFactory.decodeResource(r, R.drawable.monster11),
				BitmapFactory.decodeResource(r, R.drawable.monster12),
			};
		
		WindowManager wm = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		int width = display.getWidth();
		int height = display.getHeight();
		
		puzzle = new Puzzle(monsters, 4, 3, width, height);
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		puzzle.draw(canvas);
	}
}
