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

	private Bitmap original;
	private Bitmap display;
	
	private float zoomScale = 1.0f;

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
		this.original = image;
		this.display = image;
		this.group = new PuzzleGroup();
		this.group.addPiece(this);
	}

	public void turn()
	{
		orientation++;
		Matrix m = new Matrix();
		m.setRotate(90, original.getWidth() / 2, original.getHeight() / 2);
		original = Bitmap.createBitmap(original, 0, 0, original.getWidth(),
				original.getHeight(), m, true);

		if (orientation >= 4)
		{
			orientation = 0;
		}
	}
	
	public void zoomIn()
	{
		zoomScale += 0.1;
		zoomEffect();
	}
	 
	public void zoomOut() 
	{
		if(zoomScale <= 0.1)
			return;
		
		zoomScale -= 0.1;
		zoomEffect();
	}

	private void zoomEffect()
	{
		int oldWidth = display.getWidth();
		int oldHeight = display.getHeight();
		
		Matrix scale = new Matrix();
		scale.postScale(zoomScale, zoomScale, original.getWidth()/2, original.getHeight()/2);
		display = Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), scale, false);
		
		this.setX(this.getX() + (display.getWidth() - oldWidth)/2);
		this.setY(this.getY() - (display.getHeight() - oldHeight)/2);
	}

	public void draw(Canvas c)
	{
		c.drawBitmap(display, x, y, null);
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
		return this.display.getHeight();
	}

	public int getWidth()
	{
		return this.display.getWidth();
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
	
	public int getOrientation()
	{
		return this.orientation;
	}

	public boolean inTop()
	{
		if (top == null)
			return false;

		return top.inMe(this.x, this.y)
				|| top.inMe(this.x + this.display.getWidth(), y);
	}

	public boolean inRight()
	{
		if (right == null)
			return false;

		return right.inMe(this.x + this.display.getWidth(), y)
				|| right.inMe(this.x + this.display.getWidth(), y);
	}

	public boolean inBottom()
	{
		if (bottom == null)
			return false;

		return bottom.inMe(this.x + this.display.getWidth(), y + this.display.getHeight())
				|| bottom.inMe(this.x, this.display.getHeight() + y);
	}

	public boolean inLeft()
	{
		if (left == null)
			return false;

		return left.inMe(this.x, this.y)
				|| left.inMe(this.x, this.y + this.display.getHeight());
	}

	public void addToGroup(PuzzleGroup pg)
	{
		this.group = pg;
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
		if (x >= this.x && x <= (this.x + this.display.getWidth()) && y >= this.y
				&& y <= (this.y + this.display.getHeight()))
		{
			Log.v(DEBUG, "Reported in!");
			return true;
		}

		return false;
	}

	public void snap(Piece p)
	{
		if (group.sameGroup(this, p))
		{
			return;
		}

		if (p == this.top)
		{
			int mx = x - p.getX();
			int my = y - (p.getY() + p.getHeight());
			group.translate(mx, my);
		}

		if (p == this.right)
		{
			int mx = x - (p.getX() - this.getWidth());
			int my = y - p.getY();
			group.translate(mx, my);
		}

		if (p == this.bottom)
		{
			int mx = x - p.getX();
			int my = y - (p.getY() - this.getHeight());
			group.translate(mx, my);
		}

		if (p == this.left)
		{
			int mx = x - (p.getX() + p.getWidth());
			int my = y - p.getY();
			group.translate(mx, my);
		}
		
		this.group.addGroup(p.getGroup());
	}
}
