package org.worldsproject.puzzle;

import java.util.ArrayList;

public class PuzzleGroup
{
	private ArrayList<Piece> group = new ArrayList<Piece>();
	
	public PuzzleGroup(){}
	
	public void addPiece(Piece piece)
	{
		group.add(piece);
		piece.setGroup(this);
	}
	
	public void translate(int x, int y)
	{
		for(Piece p : group)
		{
			p.setX(p.getX()-x);
			p.setY(p.getY()-y);
		}
	}
	
	public void addGroup(PuzzleGroup group)
	{
		for(Piece p : group.group)
		{
			this.group.add(p);
		}
	}
}
