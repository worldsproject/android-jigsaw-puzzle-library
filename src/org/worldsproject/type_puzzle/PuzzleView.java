package org.worldsproject.type_puzzle;

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
import android.view.View;

public class PuzzleView extends View
{
	private Bitmap monster;
	private Resources r;
	
	public PuzzleView(Context context)
	{
		super(context);
		r = context.getResources();
		monster = BitmapFactory.decodeResource(r, R.drawable.monster);
	}
	
	

	public PuzzleView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		r = context.getResources();
		monster = BitmapFactory.decodeResource(r, R.drawable.monster);
	}



	public PuzzleView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		r = context.getResources();
		monster = BitmapFactory.decodeResource(r, R.drawable.monster);
	}



	@Override
	public void onDraw(Canvas canvas)
	{
		Paint maskingTape = new Paint();
		maskingTape.setColor(Color.BLACK);
		maskingTape.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		canvas.drawBitmap(monster, 10, 10, null);
		
	}
}
