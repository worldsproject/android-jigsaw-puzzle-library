package org.worldsproject.puzzle;

import java.io.File;

import org.worldsproject.puzzle.enums.Difficulty;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PuzzleSolveActivity extends Activity {
	private PuzzleView pv;
	private Bitmap image;
	private Difficulty x;
	private int puzzle;

	public void onStart() {
		super.onStart();

		puzzle = this.getIntent().getIntExtra("image", 0);
		image = (Bitmap) BitmapFactory.decodeResource(getResources(), puzzle);
		String difficulty = this.getIntent().getStringExtra("difficulty");

		if (difficulty.equals("easy"))
			x = Difficulty.EASY;
		else if (difficulty.equals("medium"))
			x = Difficulty.MEDIUM;
		else
			x = Difficulty.HARD;

		pv = (PuzzleView) this.findViewById(R.id.puzzleView);

		// Now we need to test if it already exists.
		File testExistance = new File(path(puzzle, x.toString()));
		
		if (testExistance != null && testExistance.exists()) {
			pv.loadPuzzle(path(puzzle, x.toString()));
		} else {
			pv.loadPuzzle(image, x, path(puzzle, x.toString()));
		}
		
		Button menu = (Button) this.findViewById(R.id.menu);

		OnClickListener menuListener = new OnClickListener() {
			public void onClick(View v) {
				//Bring up menu
			}
		};

		menu.setOnClickListener(menuListener);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.solve);
	}
	
	public void onPause() {
		super.onPause();
		pv.savePuzzle(path(puzzle, x.toString()));
	}

	@Override
	public void onSaveInstanceState(Bundle b) {
		super.onSaveInstanceState(b);
		b.putInt("puzzle", puzzle);
		b.putString("difficulty", x.toString());
	}

	@Override
	public void onRestoreInstanceState(Bundle b) {
		super.onRestoreInstanceState(b);
		puzzle = b.getInt("puzzle");
		x = Difficulty.getEnumFromString(b.getString("difficulty"));
		pv.loadPuzzle(path(puzzle, x.toString()));
	}

	private String path(int puzzle, String difficulty) {
		String rv = getExternalCacheDir().getAbsolutePath() + "/" + puzzle + "/" + difficulty + "/";
		return rv;
	}
}