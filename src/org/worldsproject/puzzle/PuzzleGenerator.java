package org.worldsproject.puzzle;

import java.util.ArrayList;
import java.util.Random;

import org.worldsproject.puzzle.enums.Difficulty;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

public class PuzzleGenerator
{
	private Context context;
	private Bitmap image;

	Random ran = new Random();

	public PuzzleGenerator(Context c, Bitmap image, Difficulty difficulty)
	{

		this.context = c;
		this.image = image;

		Point[][] points = getGridPoints(image, difficulty);
		int width = getScaling(image.getWidth(), points.length);
		int height = getScaling(image.getWidth(), points[0].length);

		Mask[][] masks = generateMasks(points, width, height);
		
		ArrayList<Bitmap> pieces = createSubimages(image, masks, points);
	}

	private int getScaling(int totalWidth, int requiredSpaces)
	{
		int idealWidth = totalWidth/requiredSpaces;
		
		
		return 0;
	}

	private Point[][] getGridPoints(Bitmap image, Difficulty difficulty)
	{
		return null;
	}

	private Mask[][] generateMasks(Point[][] points, int width, int height)
	{
		Mask[][] rv = new Mask[points.length][points[0].length];

		//First we start in the top left corner (points[0][0] and generate a random corner.
		rv[0][0] = generateCorner(width, height);
		rv[0][0].rotate(1);

		//And so on for the other 3 corners.
		rv[rv.length][0] = generateCorner(width, height);
		rv[rv.length][0].rotate(2);

		rv[0][rv[0].length] = generateCorner(width, height);

		rv[rv.length][rv[0].length] = generateCorner(width, height);
		rv[rv.length][rv[0].length].rotate(3);

		//Now we need to generate the edges.

		int pos = 1;

		while(rv[pos][0] == null) //top edges
		{
			if(rv[pos-1][0].isRight())
			{
				rv[pos][0] = new Mask(context, ran.nextBoolean(), ran.nextBoolean(), false, width, height);
				rv[pos][0].rotate(1);
			}
			else
			{
				rv[pos][0] = new Mask(context, ran.nextBoolean(), ran.nextBoolean(), true, width, height);
				rv[pos][0].rotate(1);
			}
			pos++;
		}

		//rightmost top edge piece.
		boolean one = !rv[pos-1][0].isRight();
		boolean two = !rv[pos+1][0].isLeft();

		rv[pos][0] = new Mask(context, two, ran.nextBoolean(), one, width, height);
		rv[pos][0].rotate(1);

		//Right edge pieces
		pos = 1;
		int rvl = rv.length-1;
		while(rv[rvl][pos] == null)
		{
			if(rv[rvl][pos-1].isBottom())
			{
				rv[rvl][pos] = new Mask(context, ran.nextBoolean(), ran.nextBoolean(), false, width, height);
				rv[rvl][pos].rotate(2);
			}
			else
			{
				rv[rvl][pos] = new Mask(context, ran.nextBoolean(), ran.nextBoolean(), true, width, height);
				rv[rvl][pos].rotate(2);
			}

			pos++;
		}

		//bottommost right edge piece.
		one = !rv[rvl][pos-1].isBottom();
		two = !rv[rvl][pos+1].isTop();

		rv[rvl][pos] = new Mask(context, two, ran.nextBoolean(), one, width, height);
		rv[rvl][pos].rotate(2);

		//Bottom edge pieces.
		pos = 1;
		rvl = rv[0].length-1;

		while(rv[pos][rvl] == null)
		{
			if(rv[pos-1][rvl].isRight())
			{
				rv[pos][rvl] = new Mask(context, false, ran.nextBoolean(), ran.nextBoolean(), width, height);
				rv[pos][rvl].rotate(3);
			}
			else
			{
				rv[pos][rvl] = new Mask(context, true, ran.nextBoolean(), ran.nextBoolean(), width, height);
				rv[pos][rvl].rotate(3);
			}
			pos++;
		}

		//rightmost bottom edge piece.
		one = !rv[pos-1][rvl].isRight();
		two = !rv[pos+1][rvl].isLeft();

		rv[pos][rvl] = new Mask(context, one, ran.nextBoolean(), two, width, height);
		rv[pos][rvl].rotate(3);

		//Right edge pieces
		pos = 1;
		rvl = 0;
		
		while(rv[rvl][pos] == null)
		{
			if(rv[rvl][pos-1].isBottom())
			{
				rv[rvl][pos] = new Mask(context, false, ran.nextBoolean(), ran.nextBoolean(), width, height);
			}
			else
			{
				rv[rvl][pos] = new Mask(context, true, ran.nextBoolean(), ran.nextBoolean(), width, height);
			}

			pos++;
		}

		//bottommost right edge piece.
		one = !rv[rvl][pos-1].isBottom();
		two = !rv[rvl][pos+1].isTop();

		rv[rvl][pos] = new Mask(context, one, ran.nextBoolean(), two, width, height);
		
		//Now we have to fill in all the middle pieces.
		
		boolean top;
		boolean right;
		boolean bottom;
		boolean left;
		
		for(int x = 1; x < rv.length-2; x++)
		{
			for(int y = 1; y < rv[0].length-2; y++)
			{
				top = !rv[x][y-1].isBottom(); //Top ALWAYS exists.
				left = !rv[x-1][y].isRight(); //Left ALWAYS exists.
				
				if(rv[x+1][y] == null)
					right = ran.nextBoolean();
				else
					right = !rv[x+1][y].isLeft();
				
				if(rv[x][y+1] == null)
					bottom = ran.nextBoolean();
				else
					bottom = !rv[x][y+1].isTop();
				
				rv[x][y] = new Mask(context, top, right, bottom, left, width, height);
			}
		}
		
		return rv;
	}

	private ArrayList<Bitmap> createSubimages(Bitmap image, Mask[][] masks, Point[][] points)
	{
		Paint maskingTape = new Paint();
		maskingTape.setColor(Color.BLACK);
		maskingTape.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		
		ArrayList<Bitmap> rv = new ArrayList<Bitmap>();
		
		for(int i = 0; i < masks.length; i++)
		{
			for(int j = 0; j < masks[i].length; j++)
			{
				int x = points[i][j].x - masks[i][j].getTopLeft().x;
				int y = points[i][j].y - masks[i][j].getTopLeft().y;
				
				Bitmap temp = Bitmap.createBitmap(image, x, y, masks[i][j].getWidth(), masks[i][j].getHeight());
				rv.add(temp);
			}
		}
		
		return null;
	}
	
	private Mask generateCorner(int width, int height)
	{
		Mask rv = null;

		int start = ran.nextInt(4);

		switch(start)
		{
			case 0: rv = new Mask(context, false, false, width, height); break;
			case 1: rv = new Mask(context, true, false, width, height); break;
			case 2: rv = new Mask(context, false, true, width, height); break;
			case 3: rv = new Mask(context, true, true, width, height); break;
		}

		return rv;
	}
}
