package org.worldsproject.puzzle;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Puzzle
{
	private static final Random RAN = new Random();
	
	private Piece[][] puzzle;
	private ArrayList<Piece> pieces = new ArrayList<Piece>();
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
				pieces.add(puzzle[i][j]);
				loc++;
			}
		}
		
		for(int i = 0; i < puzzle.length; i++)
		{
			for(int j = 0; j < puzzle[i].length; j++)
			{
				//Top
				if(j-1 >= 0)
				{
					puzzle[i][j].setTop(puzzle[i][j-1]);
				}
				//Right
				if(i+1 < puzzle.length)
				{
					puzzle[i][j].setRight(puzzle[i+1][j-1]);
				}
				//Bottom
				if(j+1 < puzzle[i].length)
				{
					puzzle[i][j].setBottom(puzzle[i][j+1]);
				}
				//Left
				if(i-1 >= 0)
				{
					puzzle[i-1][j].setLeft(puzzle[i-1][j]);
				}
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
	
	public ArrayList<Piece> getPieces()
	{
		return this.pieces;
	}
}
