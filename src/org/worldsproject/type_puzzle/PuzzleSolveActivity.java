package org.worldsproject.type_puzzle;

import org.worldsproject.puzzle.PuzzleView;
import org.worldsproject.puzzle.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PuzzleSolveActivity extends Activity
{
	private PuzzleView pv;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.solve);
		
		pv = (PuzzleView)this.findViewById(R.id.puzzleView);
		Button zoom_out = (Button)this.findViewById(R.id.zoom_out);
		Button zoom_in = (Button)this.findViewById(R.id.zoom_in);
		
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
}