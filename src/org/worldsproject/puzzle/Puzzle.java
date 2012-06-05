package org.worldsproject.puzzle;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Puzzle
{
	private static final Random RAN = new Random();

	private ArrayList<Piece> pieces = new ArrayList<Piece>();
	private int display_width;
	private int display_height;
	private int piece_width;
	private int piece_height;

	public Puzzle(Bitmap[] images, int width, int display_width,
			int display_height)
	{
		this.display_height = display_height;
		this.display_width = display_width;

		piece_width = images[0].getWidth();
		piece_height = images[0].getHeight();

		for (int i = 0; i < images.length; i++)
		{
			pieces.add(new Piece(images[i]));
		}

		for (int i = 0; i < pieces.size(); i++)
		{
			// Top
			if(i - width >= 0)
			{
				pieces.get(i).setTop(pieces.get(i - width));
			}
			// Right
			if(i+1 < pieces.size())
			{
				pieces.get(i).setRight(pieces.get(i + 1));
			}
			// Bottom
			if(i + width < pieces.size())
			{
				pieces.get(i).setBottom(pieces.get(i + width));
			}
			// Left
			if(i - 1 >= 0)
			{
				pieces.get(i).setLeft(pieces.get(i - 1));
			}
		} 

		shuffle();
	}

	public void shuffle()
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
}
