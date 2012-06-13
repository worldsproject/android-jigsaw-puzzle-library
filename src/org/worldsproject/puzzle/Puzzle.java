package org.worldsproject.puzzle;

import java.util.ArrayList;
import java.util.Random;

import org.worldsproject.puzzle.enums.Difficulty;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Puzzle
{
	private static final Random RAN = new Random();

	private ArrayList<Piece> pieces = new ArrayList<Piece>();
	private int piece_width;
	private int piece_height;

	public Puzzle(Bitmap[] images, int width, Difficulty d)
	{
		piece_width = images[0].getWidth();
		piece_height = images[0].getHeight();

		for (int i = 0; i < images.length; i++)
		{
			pieces.add(new Piece(images[i], d.getOffset()));
		}
 
		for (int i = 0; i < pieces.size(); i++)
		{
			// Top
			if(i - width >= 0)
			{
				pieces.get(i).setTop(pieces.get(i - width));
			}
			// Right
			if(i+1 < pieces.size() && (i+1)%width != 0)
			{
				pieces.get(i).setRight(pieces.get(i + 1));
			}
			// Bottom
			if(i + width < pieces.size())
			{
				pieces.get(i).setBottom(pieces.get(i + width));
			}
			// Left
			if(i - 1 >= 0 && i%width != 0)
			{
				pieces.get(i).setLeft(pieces.get(i - 1));
			}
		} 
	}

	public void shuffle(int display_width, int display_height)
	{
		int maxX = display_width - piece_width;
		int maxY = display_height - piece_height;

		for (Piece p : this.pieces)
		{
			p.setX(RAN.nextInt(maxX));
			p.setY(RAN.nextInt(maxY));
		}
	}

	public void draw(Canvas c)
	{
		for (Piece p : this.pieces)
		{
			p.draw(c);
		}
	}

	public ArrayList<Piece> getPieces()
	{
		return this.pieces;
	}
	
	public void translate(float x, float y)
	{
		for (Piece p : this.pieces)
		{
			p.setX(p.getX() - (int)x);
			p.setY(p.getY() - (int)y);
		}
	}
	
	public void solve()
	{
		for(Piece p: this.pieces)
		{
			p.snap(p.getLeft());
			p.snap(p.getTop());
		}
	}
}
