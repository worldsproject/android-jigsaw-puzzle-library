package org.worldsproject.puzzle;

import java.io.InputStream;
import java.util.HashMap;

import org.worldsproject.puzzle.enums.Type;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;

public class Mask
{
	private static HashMap<String, Integer> resources = new HashMap<String, Integer>();

	private boolean top;
	private boolean right;
	private boolean bottom;
	private boolean left;

	private Point topLeft;
	private Point topRight;
	private Point bottomLeft;
	private Point bottomRight;

	private Type type;

	private Bitmap mask;

	private Context context;

	public Mask(Context context, boolean top, boolean right, int width, int height)
	{
		this(context, top, right, false, false, width, height);
		type = Type.CORNER;
	}

	public Mask(Context context, boolean top, boolean right, boolean bottom,
			int width, int height)
	{
		this(context, top, right, bottom, false, width, height);
		type = Type.EDGE;
	}

	public Mask(Context context, boolean top, boolean right, boolean bottom, boolean left,
			int width, int height)
	{
		super();

		this.context = context;

		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
		type = Type.FULL;

		fillResourceMapping();

		mask = loadBitmap(width, height);

		topLeft = new Point(0, 0);
		topRight = new Point(0, mask.getHeight());
		bottomLeft = new Point(mask.getWidth(), 0);
		bottomRight = new Point(mask.getWidth(), mask.getHeight());

		findPoints(topLeft, topRight, bottomLeft, bottomRight);
	}

	private void fillResourceMapping()
	{
		if(resources.containsKey("corner_0_0"))
			return;

		resources.put("corner_0_0", R.raw.corner_0_0);
		resources.put("corner_0_1", R.raw.corner_0_1);
		resources.put("corner_1_0", R.raw.corner_1_0);
		resources.put("corner_1_1", R.raw.corner_1_1);
		resources.put("edge_0_0_0", R.raw.edge_0_0_0);
		resources.put("edge_0_0_1", R.raw.edge_0_0_1);
		resources.put("edge_0_1_0", R.raw.edge_0_1_0);
		resources.put("edge_0_1_1", R.raw.edge_0_1_1);
		resources.put("edge_1_0_0", R.raw.edge_1_0_0);
		resources.put("edge_1_0_1", R.raw.edge_1_0_1);
		resources.put("edge_1_1_0", R.raw.edge_1_1_0);
		resources.put("edge_1_1_1", R.raw.edge_1_1_1);
		resources.put("full_0_0_0_0", R.raw.full_0_0_0_0);
		resources.put("full_0_0_0_1", R.raw.full_0_0_0_1);
		resources.put("full_0_0_1_0", R.raw.full_0_0_1_0);
		resources.put("full_0_0_1_1", R.raw.full_0_0_1_1);
		resources.put("full_0_1_0_0", R.raw.full_0_1_0_0);
		resources.put("full_0_1_0_1", R.raw.full_0_1_0_1);
		resources.put("full_0_1_1_0", R.raw.full_0_1_1_0);
		resources.put("full_0_1_1_1", R.raw.full_0_1_1_1);
		resources.put("full_1_0_0_0", R.raw.full_1_0_0_0);
		resources.put("full_1_0_0_1", R.raw.full_1_0_0_1);
		resources.put("full_1_0_1_0", R.raw.full_1_0_1_0);
		resources.put("full_1_0_1_1", R.raw.full_1_0_1_1);
		resources.put("full_1_1_0_0", R.raw.full_1_1_0_0);
		resources.put("full_1_1_0_1", R.raw.full_1_1_0_1);
		resources.put("full_1_1_1_0", R.raw.full_1_1_1_0);
		resources.put("full_1_1_1_1", R.raw.full_1_1_1_1);
	}

	private Bitmap loadBitmap(int width, int height)
	{
		StringBuffer name = new StringBuffer();

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

		InputStream is = context.getResources().openRawResource(resources.get(name));		

		return Bitmap.createScaledBitmap(BitmapFactory.decodeStream(is), width, height, false);

	}

	private void findPoints(Point tl, Point tr, Point bl, Point br)
	{	
		//First find the top left point.
		while(mask.getPixel(tl.x, tl.y) != Color.BLACK)
		{
			tl.x++;
			tl.y++;
		}

		//Top right point
		while(mask.getPixel(tl.x, tl.y) != Color.BLACK)
		{
			tr.x--;
			tr.y++;
		}

		//Bottom left point
		while(mask.getPixel(tl.x, tl.y) != Color.BLACK)
		{
			tr.x++;
			tr.y--;
		}

		//Bottom right point
		while(mask.getPixel(tl.x, tl.y) != Color.BLACK)
		{
			tr.x--;
			tr.y--;
		}
	}

	private void append(boolean dir, StringBuffer buf)
	{
		if (dir)
			buf.append("_1");
		else
			buf.append("_0");
	}

	/**
	 * Rotates the piece by 90* times.
	 * @param times - amount of 90* turns.
	 */
	public void rotate(int times)
	{
		Matrix rotM = new Matrix();
		rotM.setRotate((90*times)%360, mask.getWidth(), mask.getHeight());
		mask = Bitmap.createBitmap(mask, 0, 0, mask.getWidth(), mask.getHeight(), rotM, true);

		for(int i = 0; i < times; i++)
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

	public Bitmap getMask()
	{
		return mask;
	}

	public Point getTopLeft()
	{
		return topLeft;
	}

	public Point getTopRight()
	{
		return topRight;
	}

	public Point getBottomLeft()
	{
		return bottomLeft;
	}

	public Point getBottomRight()
	{
		return bottomRight;
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
