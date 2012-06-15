package org.worldsproject.puzzle;

import org.worldsproject.puzzle.enums.Difficulty;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PuzzleSolveActivity extends Activity
{
	private PuzzleView pv;
	private Bitmap image;
	private Difficulty x;

	public void onStart()
	{
		super.onStart();
		Log.v("Solve", "onStart() called.");
		
		image = (Bitmap) BitmapFactory.decodeResource(getResources(), this.getIntent().getIntExtra("image", 0));
		String difficulty = this.getIntent().getStringExtra("difficulty");

		if (difficulty.equals("easy"))
			x = Difficulty.EASY;
		else if (difficulty.equals("medium"))
			x = Difficulty.MEDIUM;
		else
			x = Difficulty.HARD;

		pv = (PuzzleView) this.findViewById(R.id.puzzleView);
		pv.loadPuzzle(image, x);
		Button zoom_out = (Button) this.findViewById(R.id.zoom_out);
		Button zoom_in = (Button) this.findViewById(R.id.zoom_in);

		OnClickListener zoomIn = new OnClickListener()
		{
			public void onClick(View v)
			{
				pv.zoomIn();
			}
		};

		OnClickListener zoomOut = new OnClickListener()
		{
			public void onClick(View v)
			{
				pv.zoomOut();
			}
		};

		zoom_out.setOnClickListener(zoomOut);
		zoom_in.setOnClickListener(zoomIn);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.v("Solve", "onCreate() called.");
		setContentView(R.layout.solve);
	}
}