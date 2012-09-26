package org.worldsproject.puzzle;

import org.worldsproject.puzzle.coverflow.CoverFlow;
import org.worldsproject.puzzle.coverflow.ReflectingImageAdapter;
import org.worldsproject.puzzle.coverflow.ResourceImageAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

public class PuzzleSelectActivity extends Activity
{
	private Button button;
	
	private String difficulty = "easy";
	private int[] images;
	private Intent intent;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.selector);
		
		CoverFlow cf = (CoverFlow)findViewById(R.id.coverflow);
		ResourceImageAdapter ria = new ResourceImageAdapter(this);
		images = this.getIntent().getIntArrayExtra("images");
		ria.setResources(images);
		ReflectingImageAdapter coverImageAdapter = new ReflectingImageAdapter(ria);
		cf.setAdapter(coverImageAdapter);
        cf.setSelection(0, true);
		
		intent = new Intent(this, PuzzleSolveActivity.class);
		
		cf.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView< ? > parent, final View view, final int position, final long id) {
                intent.putExtra("image", images[position]);
                intent.putExtra("difficulty", difficulty);
				PuzzleSelectActivity.this.startActivity(intent);
            }

        });
		
		button = (Button)findViewById(R.id.difficulty);
		
		button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(difficulty.equals("easy"))
				{
					difficulty = "medium";
					button.setText(R.string.medium);
				}
				else if(difficulty.equals("medium"))
				{
					difficulty = "hard";
					button.setText(R.string.hard);
				}
				else
				{
					difficulty = "easy";
					button.setText(R.string.easy);
				}
			}
			
		});
	}
}
