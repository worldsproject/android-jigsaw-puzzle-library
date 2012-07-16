package org.worldsproject.puzzle;

import java.io.File;

import org.worldsproject.puzzle.enums.Difficulty;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class PuzzleSolveActivity extends Activity {
	private PuzzleView pv;
	private Bitmap image;
	private Difficulty x;
	private int puzzle;

	private AlertDialog.Builder builder;
	private AlertDialog alert;
	private ImageView preview_view;
	private Dialog preview_dialog;

	public void onStart() {
		super.onStart();
		builder = new AlertDialog.Builder(this);

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

		preview_view = (ImageView) getLayoutInflater().inflate(
				R.layout.preview_puzzle, null);
		preview_view.setImageBitmap(image);

		preview_dialog = new Dialog(this);
		preview_dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		preview_dialog.setContentView(preview_view);

		preview_view.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				preview_dialog.dismiss();
			}
		});

		Button menu = (Button) this.findViewById(R.id.menu);

		final CharSequence[] items = { getString(R.string.show_cover),
				getString(R.string.solve), getString(R.string.reset) };

		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				switch (item) {
				case 0:
					preview_dialog.show();
					break;
				case 1:
					pv.solve();
					break;
				case 2:
					pv.shuffle();
					break;
				}
			}
		});

		alert = builder.create();

		OnClickListener menuListener = new OnClickListener() {
			public void onClick(View v) {
				alert.show();
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
		String rv = null;
		if (Environment.getExternalStorageState().equals("mounted")) {
			rv = getExternalCacheDir().getAbsolutePath() + "/" + puzzle + "/"
					+ difficulty + "/";
		} else {
			rv = getCacheDir().getAbsolutePath() + "/" + puzzle + "/"
					+ difficulty + "/";
		}
		return rv;
	}
}