package org.worldsproject.puzzle.enums;

public enum Difficulty {
	EASY, MEDIUM, HARD;

	public int pieceSize() {
		if (this == EASY)
			return 64;
		else if (this == MEDIUM)
			return 48;
		else
			return 32;
	}

	public int getOffset() {
		if (this == EASY)
			return 10;
		if (this == MEDIUM)
			return 8;
		else
			return 5;
	}

	public String toString() {
		if (this == EASY)
			return "easy";
		else if (this == MEDIUM)
			return "medium";
		else
			return "hard";
	}

	public static Difficulty getEnumFromString(String e) {
		if (e.equals("easy"))
			return EASY;
		else if (e.equals("medium"))
			return MEDIUM;
		else
			return HARD;
	}
}
