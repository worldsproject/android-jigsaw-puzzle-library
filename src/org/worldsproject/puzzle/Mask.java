package org.worldsproject.puzzle;

import java.io.InputStream;
import java.util.HashMap;

import org.worldsproject.puzzle.enums.Difficulty;
import org.worldsproject.puzzle.enums.Type;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class Mask
{
	private static HashMap<String, Integer> resources = new HashMap<String, Integer>();

	private boolean top;
	private boolean right;
	private boolean bottom;
	private boolean left;

	private Type type;

	private Bitmap mask;

	private Context context;

	private Difficulty difficulty;
	
	private int offset;

	public Mask(Context context, boolean top, boolean right,
			Difficulty difficulty)
	{
		this(context, top, right, false, false, difficulty);
		type = Type.CORNER;
		mask = loadBitmap();
	}

	public Mask(Context context, boolean top, boolean right, boolean bottom,
			Difficulty difficulty)
	{
		this(context, top, right, bottom, false, difficulty);
		type = Type.EDGE;
		mask = loadBitmap();
	}

	public Mask(Context context, boolean top, boolean right, boolean bottom,
			boolean left, Difficulty difficulty)
	{
		super();

		this.context = context;

		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
		this.type = Type.FULL;
		this.difficulty = difficulty;

		fillResourceMapping();
		
		// Here we have to account for all that transparent border.
		// We should be making pieces off of the square area
		// With overhangs going 'outside' the piece to make it look
		// All puzzle like and pretty.
		// Because the pieces have already been make, we know the offsets.
		if (difficulty == Difficulty.EASY)
			this.offset = 10;
		else if (difficulty == Difficulty.MEDIUM)
			this.offset = 8;
		else
			this.offset = 5;
		
		mask = loadBitmap();
	}

	private void fillResourceMapping()
	{
		if (resources.isEmpty() == false)
			return;

		resources.put("mask_32_corner_0_0", R.raw.mask_32_corner_0_0);
		resources.put("mask_32_corner_0_1", R.raw.mask_32_corner_0_1);
		resources.put("mask_32_corner_1_0", R.raw.mask_32_corner_1_0);
		resources.put("mask_32_corner_1_1", R.raw.mask_32_corner_1_1);
		resources.put("mask_32_edge_0_0_0", R.raw.mask_32_edge_0_0_0);
		resources.put("mask_32_edge_0_0_1", R.raw.mask_32_edge_0_0_1);
		resources.put("mask_32_edge_0_1_0", R.raw.mask_32_edge_0_1_0);
		resources.put("mask_32_edge_0_1_1", R.raw.mask_32_edge_0_1_1);
		resources.put("mask_32_edge_1_0_0", R.raw.mask_32_edge_1_0_0);
		resources.put("mask_32_edge_1_0_1", R.raw.mask_32_edge_1_0_1);
		resources.put("mask_32_edge_1_1_0", R.raw.mask_32_edge_1_1_0);
		resources.put("mask_32_edge_1_1_1", R.raw.mask_32_edge_1_1_1);
		resources.put("mask_32_full_0_0_0_0", R.raw.mask_32_full_0_0_0_0);
		resources.put("mask_32_full_0_0_0_1", R.raw.mask_32_full_0_0_0_1);
		resources.put("mask_32_full_0_0_1_0", R.raw.mask_32_full_0_0_1_0);
		resources.put("mask_32_full_0_0_1_1", R.raw.mask_32_full_0_0_1_1);
		resources.put("mask_32_full_0_1_0_0", R.raw.mask_32_full_0_1_0_0);
		resources.put("mask_32_full_0_1_0_1", R.raw.mask_32_full_0_1_0_1);
		resources.put("mask_32_full_0_1_1_0", R.raw.mask_32_full_0_1_1_0);
		resources.put("mask_32_full_0_1_1_1", R.raw.mask_32_full_0_1_1_1);
		resources.put("mask_32_full_1_0_0_0", R.raw.mask_32_full_1_0_0_0);
		resources.put("mask_32_full_1_0_0_1", R.raw.mask_32_full_1_0_0_1);
		resources.put("mask_32_full_1_0_1_0", R.raw.mask_32_full_1_0_1_0);
		resources.put("mask_32_full_1_0_1_1", R.raw.mask_32_full_1_0_1_1);
		resources.put("mask_32_full_1_1_0_0", R.raw.mask_32_full_1_1_0_0);
		resources.put("mask_32_full_1_1_0_1", R.raw.mask_32_full_1_1_0_1);
		resources.put("mask_32_full_1_1_1_0", R.raw.mask_32_full_1_1_1_0);
		resources.put("mask_32_full_1_1_1_1", R.raw.mask_32_full_1_1_1_1);

		resources.put("mask_48_corner_0_0", R.raw.mask_48_corner_0_0);
		resources.put("mask_48_corner_0_1", R.raw.mask_48_corner_0_1);
		resources.put("mask_48_corner_1_0", R.raw.mask_48_corner_1_0);
		resources.put("mask_48_corner_1_1", R.raw.mask_48_corner_1_1);
		resources.put("mask_48_edge_0_0_0", R.raw.mask_48_edge_0_0_0);
		resources.put("mask_48_edge_0_0_1", R.raw.mask_48_edge_0_0_1);
		resources.put("mask_48_edge_0_1_0", R.raw.mask_48_edge_0_1_0);
		resources.put("mask_48_edge_0_1_1", R.raw.mask_48_edge_0_1_1);
		resources.put("mask_48_edge_1_0_0", R.raw.mask_48_edge_1_0_0);
		resources.put("mask_48_edge_1_0_1", R.raw.mask_48_edge_1_0_1);
		resources.put("mask_48_edge_1_1_0", R.raw.mask_48_edge_1_1_0);
		resources.put("mask_48_edge_1_1_1", R.raw.mask_48_edge_1_1_1);
		resources.put("mask_48_full_0_0_0_0", R.raw.mask_48_full_0_0_0_0);
		resources.put("mask_48_full_0_0_0_1", R.raw.mask_48_full_0_0_0_1);
		resources.put("mask_48_full_0_0_1_0", R.raw.mask_48_full_0_0_1_0);
		resources.put("mask_48_full_0_0_1_1", R.raw.mask_48_full_0_0_1_1);
		resources.put("mask_48_full_0_1_0_0", R.raw.mask_48_full_0_1_0_0);
		resources.put("mask_48_full_0_1_0_1", R.raw.mask_48_full_0_1_0_1);
		resources.put("mask_48_full_0_1_1_0", R.raw.mask_48_full_0_1_1_0);
		resources.put("mask_48_full_0_1_1_1", R.raw.mask_48_full_0_1_1_1);
		resources.put("mask_48_full_1_0_0_0", R.raw.mask_48_full_1_0_0_0);
		resources.put("mask_48_full_1_0_0_1", R.raw.mask_48_full_1_0_0_1);
		resources.put("mask_48_full_1_0_1_0", R.raw.mask_48_full_1_0_1_0);
		resources.put("mask_48_full_1_0_1_1", R.raw.mask_48_full_1_0_1_1);
		resources.put("mask_48_full_1_1_0_0", R.raw.mask_48_full_1_1_0_0);
		resources.put("mask_48_full_1_1_0_1", R.raw.mask_48_full_1_1_0_1);
		resources.put("mask_48_full_1_1_1_0", R.raw.mask_48_full_1_1_1_0);
		resources.put("mask_48_full_1_1_1_1", R.raw.mask_48_full_1_1_1_1);

		resources.put("mask_64_corner_0_0", R.raw.mask_64_corner_0_0);
		resources.put("mask_64_corner_0_1", R.raw.mask_64_corner_0_1);
		resources.put("mask_64_corner_1_0", R.raw.mask_64_corner_1_0);
		resources.put("mask_64_corner_1_1", R.raw.mask_64_corner_1_1);
		resources.put("mask_64_edge_0_0_0", R.raw.mask_64_edge_0_0_0);
		resources.put("mask_64_edge_0_0_1", R.raw.mask_64_edge_0_0_1);
		resources.put("mask_64_edge_0_1_0", R.raw.mask_64_edge_0_1_0);
		resources.put("mask_64_edge_0_1_1", R.raw.mask_64_edge_0_1_1);
		resources.put("mask_64_edge_1_0_0", R.raw.mask_64_edge_1_0_0);
		resources.put("mask_64_edge_1_0_1", R.raw.mask_64_edge_1_0_1);
		resources.put("mask_64_edge_1_1_0", R.raw.mask_64_edge_1_1_0);
		resources.put("mask_64_edge_1_1_1", R.raw.mask_64_edge_1_1_1);
		resources.put("mask_64_full_0_0_0_0", R.raw.mask_64_full_0_0_0_0);
		resources.put("mask_64_full_0_0_0_1", R.raw.mask_64_full_0_0_0_1);
		resources.put("mask_64_full_0_0_1_0", R.raw.mask_64_full_0_0_1_0);
		resources.put("mask_64_full_0_0_1_1", R.raw.mask_64_full_0_0_1_1);
		resources.put("mask_64_full_0_1_0_0", R.raw.mask_64_full_0_1_0_0);
		resources.put("mask_64_full_0_1_0_1", R.raw.mask_64_full_0_1_0_1);
		resources.put("mask_64_full_0_1_1_0", R.raw.mask_64_full_0_1_1_0);
		resources.put("mask_64_full_0_1_1_1", R.raw.mask_64_full_0_1_1_1);
		resources.put("mask_64_full_1_0_0_0", R.raw.mask_64_full_1_0_0_0);
		resources.put("mask_64_full_1_0_0_1", R.raw.mask_64_full_1_0_0_1);
		resources.put("mask_64_full_1_0_1_0", R.raw.mask_64_full_1_0_1_0);
		resources.put("mask_64_full_1_0_1_1", R.raw.mask_64_full_1_0_1_1);
		resources.put("mask_64_full_1_1_0_0", R.raw.mask_64_full_1_1_0_0);
		resources.put("mask_64_full_1_1_0_1", R.raw.mask_64_full_1_1_0_1);
		resources.put("mask_64_full_1_1_1_0", R.raw.mask_64_full_1_1_1_0);
		resources.put("mask_64_full_1_1_1_1", R.raw.mask_64_full_1_1_1_1);
	}

	private Bitmap loadBitmap()
	{
		StringBuffer name = new StringBuffer("mask_");

		if (difficulty == Difficulty.EASY)
			name.append("64_");
		else if (difficulty == Difficulty.MEDIUM)
			name.append("48_");
		else
			name.append("32_");

		if (type == Type.FULL)
		{
			name.append("full");

			append(top, name);
			append(right, name);
			append(bottom, name);
			append(left, name);
		}
		else if (type == Type.EDGE)
		{
			name.append("edge");
			append(top, name);
			append(right, name);
			append(bottom, name);
		}
		else
		{
			name.append("corner");
			append(top, name);
			append(right, name);
		}
		
		InputStream is = context.getResources().openRawResource(
				resources.get(name.toString()));
		
		return BitmapFactory.decodeStream(is);

	}

	private void append(boolean dir, StringBuffer buf)
	{
		if (dir)
			buf.append("_1");
		else
			buf.append("_0");
	}

	/**
	 * Rotates the mask by 90° x the number given.
	 * 
	 * @param times
	 *            - amount of 90° turns.
	 */
	public void rotate(int times)
	{
		Matrix rotM = new Matrix();
		rotM.setRotate((90 * times) % 360, mask.getWidth(), mask.getHeight());
		mask = Bitmap.createBitmap(mask, 0, 0, mask.getWidth(),
				mask.getHeight(), rotM, true);

		for (int i = 0; i < times; i++)
		{
			boolean newRight = top;
			boolean newBottom = right;
			boolean newLeft = bottom;
			boolean newTop = left;

			top = newTop;
			right = newRight;
			bottom = newBottom;
			left = newLeft;
		}
	}

	public boolean isTop()
	{
		return top;
	}

	public boolean isRight()
	{
		return right;
	}

	public boolean isBottom()
	{
		return bottom;
	}

	public boolean isLeft()
	{
		return left;
	}

	public Type getType()
	{
		return type;
	}

	public Difficulty getDifficulty()
	{
		return difficulty;
	}

	public Bitmap getMask()
	{
		return mask;
	}

	public int getOffset()
	{
		return offset;
	}

	public int getWidth()
	{
		return mask.getWidth();
	}

	public int getHeight()
	{
		return mask.getHeight();
	}
}
