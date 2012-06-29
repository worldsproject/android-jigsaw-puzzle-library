package org.worldsproject.puzzle;

import java.util.HashSet;

public class PuzzleGroup
{
	private static int idSource = 0;
	private final int serial = ++idSource;
	
	private HashSet<Piece> group = new HashSet<Piece>();
	
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
	
	public void addGroup(PuzzleGroup oldGroup)
	{
		for(Piece p : oldGroup.getGroup())
		{
			this.addPiece(p);
		}
	}
	
	public HashSet<Piece> getGroup()
	{
		return group;
	}
	
	public boolean sameGroup(Piece a, Piece b)
	{
		return a.getGroup().getSerial() == b.getGroup().getSerial();
	}
	
	public int getSerial()
	{
		return serial;
	}
}
