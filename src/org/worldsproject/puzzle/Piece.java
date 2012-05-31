package org.worldsproject.puzzle;

import android.graphics.Bitmap;

public class Piece
{
	private int x = 0;
	private int y = 0;
	
	/*
	 * 0 is the correct orientation.
	 * 1 is 90 degrees turned clockwise.
	 * 2 is 180 degrees turned clockwise.
	 * 3 is 270 degrees turned clockwise.
	 */
	private int orientation = 0;
	
	public Piece(Bitmap image, Bitmap mask)
	{
		applyMask();
	}
	
	private void applyMask()
	{
		
	}
	
	public void turn()
	{
		orientation++;
		
		if(orientation >= 4)
		{
			orientation = 0;
		}
	}

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}
}
