package org.worldsproject.puzzle;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

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
	
	private static final String DEBUG = "PuzzlePiece";
	
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
		Matrix m = new Matrix();
		m.setRotate(90, image.getWidth()/2, image.getHeight()/2);
		image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), m, true);
		
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
	
	public int getHeight()
	{
		return this.image.getHeight();
	}
	
	public int getWidth()
	{
		return this.image.getWidth();
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
	
	public boolean inTop() //TODO just the relevant corners.
	{
		if(top == null)
			return false;
		
		return inSomething(this.top);
	}
	
	public boolean inRight()
	{
		if(right == null)
			return false;
		
		return inSomething(this.right);
	}
	
	public boolean inBottom()
	{
		if(bottom == null)
			return false;
		
		return inSomething(this.bottom);
	}
	
	public boolean inLeft()
	{
		if(left == null)
			return false;
		
		return inSomething(this.left);
	}
	
	private boolean inSomething(Piece p)
	{
		return p.inMe(this.x, this.x) ||
				p.inMe(this.x + this.image.getWidth(), y) ||
				p.inMe(this.x, this.image.getHeight() + y) ||
				p.inMe(this.x + this.image.getWidth(), this.y + this.image.getHeight());
	}
	
	public void addToGroup(PuzzleGroup pg)
	{
		this.group = pg;
	}
	
	public boolean isInGroup()
	{
		return this.group != null;
	}
	
	public PuzzleGroup getGroup()
	{
		return this.group;
	}
	
	public void setGroup(PuzzleGroup g)
	{
		this.group = g;
	}
	
	public boolean inMe(int x, int y)
	{
		if(x >= this.x && x <= (this.x + this.image.getWidth()) 
				&& y >= this.y && y <= (this.y + this.image.getHeight()))
			return true;
		
		return false;
	}
	
	public void snap(Piece p)
	{
		if(this.isInGroup() == false && p.isInGroup() == false)
		{
			this.group = new PuzzleGroup();
			this.group.addPiece(this);
			this.group.addPiece(p);
			Log.v(DEBUG, "Neither in a group");
		}
		else if(this.isInGroup() && p.isInGroup())
		{
			this.group.addGroup(p.getGroup());
			Log.v(DEBUG, "Both me in a group");
		}
		else if(this.isInGroup())
		{
			Log.v(DEBUG, "Only him in a group");
			this.group.addPiece(p);
		}
		else
		{
			Log.v(DEBUG, "Him in a group");
			p.getGroup().addPiece(this);
		}
		
		if(p == this.top)
		{
			this.setX(p.getX());
			this.setY(p.getY() + p.getHeight());
			Log.v(DEBUG, "Snap to top.");
		}
		
		if(p == this.right)
		{
			this.setX(p.getX() - this.getWidth());
			this.setY(p.getY());
			Log.v(DEBUG, "Snap to right.");
		}
		
		if(p == this.bottom)
		{
			this.setX(p.getX());
			this.setY(p.getY() - this.getHeight());
			Log.v(DEBUG, "Snap to bottom.");
		}
		
		if(p == this.left)
		{
			this.setX(p.getX() + p.getWidth());
			this.setY(p.getY());
			Log.v(DEBUG, "Snap to left.");
		}
	}
}
