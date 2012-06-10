package org.worldsproject.puzzle.enums;

public enum Difficulty
{
	EASY, MEDIUM, HARD;
	
	public int pieceSize(Difficulty dif)
	{
		if(dif == EASY)
			return 64;
		else if(dif == MEDIUM)
			return 48;
		else
			return 32;
	}
}
