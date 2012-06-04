package org.worldsproject.puzzle;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Piece
{
	private int x = 0;
	private int y = 0;
	
	private Piece top = null;
	private Piece right = null;
	private Piece bottom = null;
	private Piece left = null;
	
	private PuzzleGroup group;
	
	private Bitmap image;
	
	/*
	 * 0 is the correct orientation.
	 * 1 is 90 degrees turned clockwise.
	 * 2 is 180 degrees turned clockwise.
	 * 3 is 270 degrees turned clockwise.
	 */
	private int orientation = 0;
	
	public Piece(Bitmap image)
	{
		this.image = image;
	}
	
	public void turn()
	{
		orientation++;
		
		if(orientation >= 4)
		{
			orientation = 0;
		}
	}
	
	public void draw(Canvas c)
	{
		c.drawBitmap(image, x, y, null);
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

	public Piece getTop()
	{
		return top;
	}

	public void setTop(Piece top)
	{
		this.top = top;
	}

	public Piece getRight()
	{
		return right;
	}

	public void setRight(Piece right)
	{
		this.right = right;
	}

	public Piece getBottom()
	{
		return bottom;
	}

	public void setBottom(Piece bottom)
	{
		this.bottom = bottom;
	}

	public Piece getLeft()
	{
		return left;
	}

	public void setLeft(Piece left)
	{
		this.left = left;
	}
	
	public void addToGroup(PuzzleGroup pg)
	{
		this.group = pg;
	}
	
	public boolean isInGroup()
	{
		return this.group != null;
	}
	
	public boolean inMe(int x, int y)
	{
		if(x >= this.x && x <= (this.x + this.image.getWidth()) 
				&& y >= this.y && y <= (this.y + this.image.getHeight()))
			return true;
		
		return false;
	}
}
