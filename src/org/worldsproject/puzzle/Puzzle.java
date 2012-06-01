package org.worldsproject.puzzle;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Puzzle
{
	private static final Random RAN = new Random();
	
	private Piece[][] puzzle;
	private int display_width;
	private int display_height;
	private int piece_width;
	private int piece_height;
	
	public Puzzle(Bitmap[] images, int width, int height, int display_width, int display_height)
	{
		this.display_height = display_height;
		this.display_width = display_width;
		
		piece_width = images[0].getWidth();
		piece_height = images[0].getHeight();
		
		puzzle = new Piece[width][height];
		
		int loc = 0;
		
		for(int i = 0; i < puzzle.length; i++)
		{
			for(int j = 0; j < puzzle[i].length; j++)
			{
				puzzle[i][j] = new Piece(images[loc]);
				loc++;
			}
		}
		
		shuffle();
	}
	
	public void shuffle()
	{
		int maxX = display_width - piece_width;
		int maxY = display_height - piece_height;
		
		for(int i = 0; i < puzzle.length; i++)
		{
			for(int j = 0; j < puzzle[i].length; j++)
			{
				puzzle[i][j].setX(RAN.nextInt(maxX));
				puzzle[i][j].setY(RAN.nextInt(maxY));
			}
		}
	}
	
	public void draw(Canvas c)
	{
		for(int i = 0; i < puzzle.length; i++)
		{
			for(int j = 0; j < puzzle[i].length; j++)
			{
				puzzle[i][j].draw(c);
			}
		}
	}
}
