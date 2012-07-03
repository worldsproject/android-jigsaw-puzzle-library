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
		if ((new File(path(puzzle)).exists())) {
			pv.loadPuzzle(path(puzzle));
		} else {
			pv.loadPuzzle(image, x, path(puzzle));
		}
		
		Button zoom_out = (Button) this.findViewById(R.id.zoom_out);
		Button zoom_in = (Button) this.findViewById(R.id.zoom_in);

		OnClickListener zoomIn = new OnClickListener() {
			public void onClick(View v) {
				pv.zoomIn();
			}
		};

		OnClickListener zoomOut = new OnClickListener() {
			public void onClick(View v) {
				pv.zoomOut();
			}
		};

		zoom_out.setOnClickListener(zoomOut);
		zoom_in.setOnClickListener(zoomIn);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.solve);
	}

	@Override
	public void onSaveInstanceState(Bundle b) {
		super.onSaveInstanceState(b);
		// Log.v("PATH", getExternalCacheDir().getAbsolutePath() + "/puzzle/");
		b.putInt("puzzle", puzzle);
		pv.savePuzzle(path(puzzle));
	}

	@Override
	public void onRestoreInstanceState(Bundle b) {
		super.onRestoreInstanceState(b);
		puzzle = b.getInt("puzzle");
		pv.loadPuzzle(path(puzzle));
	}

	private String path(int puzzle) {
		return getExternalCacheDir().getAbsolutePath() + "/puzzle/";
	}
}