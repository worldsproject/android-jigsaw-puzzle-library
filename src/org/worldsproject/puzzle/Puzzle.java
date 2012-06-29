package org.worldsproject.puzzle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.worldsproject.puzzle.enums.Difficulty;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

public class Puzzle {
	private static final Random RAN = new Random();

	private ArrayList<Piece> pieces = new ArrayList<Piece>();
	private int piece_width;
	private int piece_height;

	public Puzzle(Bitmap[] images, int width, Difficulty d) {
		piece_width = images[0].getWidth();
		piece_height = images[0].getHeight();

		for (int i = 0; i < images.length; i++) {
			pieces.add(new Piece(images[i], d.getOffset()));
		}

		//Here we calculate all of the neighbors of each piece.
		for (int i = 0; i < pieces.size(); i++) {
			// Top
			if (i - width >= 0) {
				pieces.get(i).setTop(pieces.get(i - width));
			}
			// Right
			if (i + 1 < pieces.size() && (i + 1) % width != 0) {
				pieces.get(i).setRight(pieces.get(i + 1));
			}
			// Bottom
			if (i + width < pieces.size()) {
				pieces.get(i).setBottom(pieces.get(i + width));
			}
			// Left
			if (i - 1 >= 0 && i % width != 0) {
				pieces.get(i).setLeft(pieces.get(i - 1));
			}
		}
	}

	public void shuffle(int display_width, int display_height) {
		int maxX = display_width - piece_width;
		int maxY = display_height - piece_height;

		for (Piece p : this.pieces) {
			p.setX(RAN.nextInt(maxX));
			p.setY(RAN.nextInt(maxY));
		}
	}

	public void draw(Canvas c) {
		for (Piece p : this.pieces) {
			p.draw(c);
		}
	}

	public ArrayList<Piece> getPieces() {
		return this.pieces;
	}

	public void translate(float x, float y) {
		for (Piece p : this.pieces) {
			p.setX(p.getX() - (int) x);
			p.setY(p.getY() - (int) y);
		}
	}

	public void solve() {
		for (Piece p : this.pieces) {
			p.snap(p.getLeft());
			p.snap(p.getTop());
		}
	}

	public void savePuzzle(String location) {
		JSONArray array = new JSONArray();
		array.put(this.pieces.get(0).getOffset());

		for (Piece p : this.pieces) {
			JSONObject obj = new JSONObject();

			try {
				obj.put("x", p.getX());
				obj.put("y", p.getY());
				obj.put("g", p.getGroup().getSerial());
				obj.put("s", p.getSerial());
			} catch (JSONException e) {
				// We are specifically saving ints, thus the error should never
				// be thrown.
				throw new RuntimeException(e);
			}

			array.put(obj);

			try {
				FileOutputStream out = new FileOutputStream(location
						+ p.getSerial() + ".png");
				File checkExists = new File(location + p.getSerial() + ".png");

				if (checkExists.exists() == false)
					p.getOriginal()
							.compress(Bitmap.CompressFormat.PNG, 90, out);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		String puzzleData = array.toString();
		PrintWriter output = null;

		try {
			output = new PrintWriter(new File(location + "puzzle_data.txt"));
		} catch (FileNotFoundException e) {
			Log.e("Write Data", "Puzzle failed to open a level file.", e);
			throw new RuntimeException(e);
		}

		output.write(puzzleData);
	}

	public void loadPuzzle(String location) throws FileNotFoundException {
		this.pieces.clear();

		File levelFile = new File(location + "puzzle_data.txt");

		Scanner scan = new Scanner(levelFile);

		String levelText = scan.next();

		JSONTokener parser = new JSONTokener(levelText);
		int offset = 0;

		try {
			offset = (Integer) parser.nextValue();
		} catch (JSONException e) {
			// The only way this happens is if the textfile has been modified so
			// that the offset isn't the first value.
			throw new RuntimeException(e);
		}

		HashMap<Integer, PuzzleGroup> groupMap = new HashMap<Integer, PuzzleGroup>();

		while (parser.more()) {
			JSONObject piece;

			try {
				piece = (JSONObject) parser.nextValue();
			} catch (JSONException e) {
				// The only way this happens is if the text file has been
				// modified. Nothing we can do but crash.
				throw new RuntimeException(e);
			}
			int x;
			int y;
			int groupID;
			int serial;

			try {
				x = piece.getInt("x");
				y = piece.getInt("y");
				groupID = piece.getInt("g");
				serial = piece.getInt("s");
			} catch (JSONException e) {
				// See above.
				throw new RuntimeException(e);
			}

			Bitmap pieceImage = BitmapFactory.decodeFile(location + serial
					+ ".png");

			Piece p = new Piece(pieceImage, offset);
			p.setX(x);
			p.setY(y);

			PuzzleGroup group = groupMap.get(groupID);
			
			if (group == null) {
				group = new PuzzleGroup();
				groupMap.put(groupID, group);
			}
			
			p.setGroup(group);
			this.pieces.add(p);
		}
	}
}
