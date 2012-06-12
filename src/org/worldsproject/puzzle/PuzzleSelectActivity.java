package org.worldsproject.puzzle;

import org.worldsproject.puzzle.R;
import org.worldsproject.puzzle.R.drawable;
import org.worldsproject.puzzle.R.id;
import org.worldsproject.puzzle.R.layout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

public class PuzzleSelectActivity extends Activity
{
	ImageView selected;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selector);

		Gallery gallery = (Gallery) findViewById(R.id.gallery);
		gallery.setAdapter(new ImageAdapter(this));
		
		selected = (ImageView) findViewById(R.id.imageView);

		gallery.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id)
			{
				Toast.makeText(PuzzleSelectActivity.this, "" + position,
						Toast.LENGTH_SHORT).show();
			}
		});

		gallery.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3)
			{
				Log.v("PuzzleSelectActivity", "Image Selected");
				ImageAdapter ia = (ImageAdapter)arg0.getAdapter();
				selected.setImageDrawable(getResources().getDrawable((Integer)ia.getItem(arg2)));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
			}
		});
	}

	private class ImageAdapter extends BaseAdapter
	{
		private Context mContext;

		private Integer[] mImageIds = { R.drawable.monster,
				R.drawable.monster1, R.drawable.monster2, R.drawable.monster3,
				R.drawable.monster4, R.drawable.monster5, R.drawable.monster6,
				R.drawable.monster7, };

		public ImageAdapter(Context c)
		{
			mContext = c;
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
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent)
		{
			ImageView imageView = new ImageView(mContext);

			imageView.setImageResource(mImageIds[position]);
			imageView.setLayoutParams(new Gallery.LayoutParams(150, 100));
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);

			return imageView;
		}

	}
}
