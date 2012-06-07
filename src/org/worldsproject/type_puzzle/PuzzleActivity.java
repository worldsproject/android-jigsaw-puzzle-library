package org.worldsproject.type_puzzle;

import org.worldsproject.puzzle.R;

import android.app.Activity;
import android.os.Bundle;

public class PuzzleActivity extends Activity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}
}