package org.worldsproject.puzzle;

import java.util.HashSet;

import android.util.Log;

public class PuzzleGroup
{
	private HashSet<Piece> group = new HashSet<Piece>();
	
	public PuzzleGroup(){}
	
	public void addPiece(Piece piece)
	{
		group.add(piece);
		piece.setGroup(this);
		Log.v("PuzzleGroup", "Size: " + group.size());
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
