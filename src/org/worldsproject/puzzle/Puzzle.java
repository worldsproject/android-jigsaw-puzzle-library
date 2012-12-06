package org.worldsproject.puzzle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.worldsproject.puzzle.enums.Difficulty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.widget.Toast;

public class Puzzle {
	private static final Random RAN = new Random();

	private final ArrayList<Piece> pieces = new ArrayList<Piece>();
	private final int width;

	public Puzzle(Context c, String location) {
		this.width = this.loadPuzzle(c, location);
        
		this.findNeighbors(width);
	}

	public Puzzle(Context c, Bitmap[] images, String location, int width, Difficulty d) {
		for (int i = 0; i < images.length; i++) {
			pieces.add(new Piece(c, images[i], d.getOffset()));
		}
       
		this.width = width;
		findNeighbors(width);
	}

	private void findNeighbors(int width) {
		// Here we calculate all of the neighbors of each piece.
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
		int maxX = display_width - 20;
		int maxY = display_height - 20;

		for (Piece p : this.pieces) {
			p.setX(RAN.nextInt(maxX));
			p.setY(RAN.nextInt(maxY));
			p.setGroup(new PuzzleGroup(p));
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
		for(Piece p : this.pieces) {
			p.snap(p.getLeft());
			p.snap(p.getTop());
		}
	}
    
    public double percent_complete() {
		int total_groups = countGroups() - 1;
		int max_groups = pieces.size() - 1;
        double rv = (double)(max_groups - total_groups)/(double)max_groups;

		return rv;
    }

	private int countGroups() {
		HashSet<Integer> uniques = new HashSet<Integer>();

		for (Piece p : this.pieces) {
			uniques.add(p.getGroup().getSerial());
		}

		return uniques.size();
	}

	public void savePuzzle(Context context, String location, boolean saveImages) {
		if (location == null) {
			return;
		}
		JSONArray array = new JSONArray();
		array.put(this.pieces.get(0).getOffset());
		array.put(this.width);

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

			if (saveImages) {
				writeImages(location, p);
			}
		}
		
		String puzzleData = array.toString();
		PrintWriter output = null;
		
		try {
			output = new PrintWriter(new File(location + "puzzle_data.txt"));
			output.write(puzzleData);
			output.close();
		} catch (FileNotFoundException e) {
			Toast.makeText(context, context.getText(R.string.sdcard_error), Toast.LENGTH_LONG).show();
		}
	}

	private void writeImages(String location, Piece p) {
		try {
			File checkExists = new File(location + p.getSerial() + ".png");

			if (checkExists.exists() == false) {
				(new File(location)).mkdirs();
				checkExists.createNewFile();

				FileOutputStream out = new FileOutputStream(location
						+ p.getSerial() + ".png");

				p.getOriginal().compress(Bitmap.CompressFormat.PNG, 90, out);
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressLint("UseSparseArrays")
	public int loadPuzzle(Context c, String location) {
		this.pieces.clear();

		File levelFile = new File(location + "puzzle_data.txt");

		StringBuilder text = new StringBuilder();

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(levelFile));
			String line;

			while ((line = br.readLine()) != null) {
				text.append(line);
			}
		} catch (IOException e) {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

		JSONTokener parser = new JSONTokener(text.toString());

		JSONArray items;
		try {
			items = (JSONArray) parser.nextValue();
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		int offset = items.optInt(0);
		int width = items.optInt(1);

		HashMap<Integer, PuzzleGroup> groupMap = new HashMap<Integer, PuzzleGroup>();

		for (int i = 2; i < items.length(); i++) {
			JSONObject piece = items.optJSONObject(i);

			int x;
			int y;
			int groupID;
			int serial;

			x = piece.optInt("x");
			y = piece.optInt("y");
			groupID = piece.optInt("g");
			serial = piece.optInt("s");

			Bitmap pieceImage = BitmapFactory.decodeFile(location + serial
					+ ".png");
			
			Piece p = new Piece(c, pieceImage, offset);
			p.setX(x);
			p.setY(y);

			PuzzleGroup group = groupMap.get(groupID);

			if (group == null) {
				group = new PuzzleGroup(p);
				groupMap.put(groupID, group);
			}

			p.setGroup(group);
			this.pieces.add(p);
		}
        
		return width;
	}
}
