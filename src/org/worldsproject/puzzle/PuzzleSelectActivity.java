package org.worldsproject.puzzle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

public class PuzzleSelectActivity extends Activity
{
	private ImageView selected;
	private Button button;
	
	private int image;
	private String difficulty = "easy";
	private Intent intent;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selector);

		final Animation in = new AlphaAnimation(0, 1);
		final Animation out = new AlphaAnimation(1, 0);
		
		in.setDuration(1000);
		out.setDuration(500);
		
		Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade_in_out);
		TextView ins = (TextView) findViewById(R.id.instruct);
		ins.setAnimation(fade);
		
		Gallery gallery = (Gallery) findViewById(R.id.gallery);
		gallery.setAdapter(new ImageAdapter(this, this.getIntent().getIntArrayExtra("images")));
		gallery.setSpacing(1);
		
		selected = (ImageView) findViewById(R.id.imageView);

		gallery.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3)
			{
				ImageAdapter ia = (ImageAdapter)arg0.getAdapter();
				image = (int)ia.getItemId(arg2);
				selected.setImageDrawable(getResources().getDrawable(image));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
			}
		});
		
		intent = new Intent(this, PuzzleSolveActivity.class);
		
		selected.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				intent.putExtra("image", image);
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

	private class ImageAdapter extends BaseAdapter
	{
		private Context mContext;

		private int[] mImageIds;

		public ImageAdapter(Context c, int[] ids)
		{
			mContext = c;
			mImageIds = ids;
		}

		public int getCount()
		{
			return mImageIds.length;
		}

		public Object getItem(int position)
		{
			return mImageIds[position];
		}

		public long getItemId(int position)
		{
			return mImageIds[position];
		}

		public View getView(int position, View convertView, ViewGroup parent)
		{
			ImageView imageView = new ImageView(mContext);

			imageView.setImageResource(mImageIds[position]);
			imageView.setLayoutParams(new Gallery.LayoutParams(150, 100));
			imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

			return imageView;
		}

	}
}
