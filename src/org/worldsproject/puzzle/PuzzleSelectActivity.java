package org.worldsproject.puzzle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

public class PuzzleSelectActivity extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Intent i;

		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			i = new Intent(this, PuzzleSelectActivity_Pre3.class);
		} else {
			i = new Intent(this, PuzzleSelectActivity_Post3.class);
		}
		
		i.putExtra("images", this.getIntent().getIntArrayExtra("images"));
		i.putExtra("thumbs", this.getIntent().getIntArrayExtra("thumbs"));
		this.startActivity(i);
	}
}
