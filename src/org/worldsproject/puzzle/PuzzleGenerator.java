package org.worldsproject.puzzle;

import java.util.Random;

import org.worldsproject.puzzle.enums.Difficulty;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;

public class PuzzleGenerator
{
	private static final Random RAN = new Random();

	private Context context;
	private Bitmap image;
	private int pieceSize;
	private Difficulty difficulty;

	Random ran = new Random();

	public PuzzleGenerator(Context c)
	{
		this.context = c;
	}

	public Puzzle generatePuzzle(Bitmap image, Difficulty difficulty)
	{
		// Do we need to scale, and if so, by how much?
		this.pieceSize = difficulty.pieceSize(difficulty);
		this.difficulty = difficulty;
		this.image = Bitmap.createScaledBitmap(image,
				image.getWidth() + (image.getWidth() % pieceSize),
				image.getHeight() + (image.getHeight() % pieceSize), false);

		// Now we need to get our width and height.
		int puzzle_width = (this.image.getWidth() / this.pieceSize);
		int puzzle_height = (this.image.getHeight() / this.pieceSize);

		// We use a linear array, because it is easier to iterate through and
		// each mask
		// knows who its neighbors are. We do not need to represent that again.
		Mask[] masks = new Mask[puzzle_width * puzzle_height];

		// Now we have an image that is able to be cut up perfectly.
		// We should start by generating a corner.
		Mask startPoint = getRandomCorner();
		masks[0] = startPoint;

		// We need some flags. Because we are iterating through in a predictable
		// manner, it will be easy to know which edge and corner we are on.
		//
		// Corner_number keeps track of which corner we have passed. If we are not yet past
		// corner number 1, and we see and edge (although we should only be seeing edges) we know
		// that it is a top edge.
		//
		// left_edge will toggle after corner number 1, and before corner 2 as left and right 
		// alternate in appearance.
		int corner_number = 0;
		boolean left_edge = true;
		
		// We start at 1 because we already have our start point.
		for (int i = 1; i < masks.length; i++)
		{
			if(isCorner(i, puzzle_width-1, puzzle_height-1))
			{
				corner_number++;
				
				if(corner_number == 1)
				{
					masks[i] = new Mask(context, RAN.nextBoolean(), !masks[i-1].isRight(), difficulty);
					masks[i].rotate(2);
				}
				else if(corner_number == 2)
				{
					masks[i] = new Mask(context, !masks[i-puzzle_width].isBottom(), RAN.nextBoolean(), difficulty);
				}
				else
				{
					masks[i] = new Mask(context, !masks[i-1].isRight(), !masks[i-puzzle_width].isBottom(), difficulty);
					masks[i].rotate(3);
				}
				
				continue;
			}
			
			//This is all of the top edge cases.
			if(corner_number < 1)
			{
				masks[i] = new Mask(context, RAN.nextBoolean(), RAN.nextBoolean(), !masks[i-1].isRight(), difficulty);
				masks[i].rotate(1);
				continue;
			}
			
			//This handles all of the bottom edge cases.
			if(corner_number > 2)
			{
				masks[i] = new Mask(context, !masks[i-1].isRight(), !masks[i-puzzle_width].isBottom(), RAN.nextBoolean(), difficulty);
				masks[i].rotate(3);
				continue;
			}
			
			//Now the only possible edge that we could have reached so far are the left
			//and right edges, so we can safely toggle.
			if(isEdge(i, puzzle_width-1, puzzle_height-1))
			{
				if(left_edge)
				{
					masks[i] = new Mask(context, !masks[i-puzzle_width].isBottom(), RAN.nextBoolean(), RAN.nextBoolean(), difficulty);
				}
				else
				{
					masks[i] = new Mask(context, RAN.nextBoolean(), !masks[i-1].isLeft(), !masks[i-puzzle_width].isBottom(), difficulty);
					masks[i].rotate(2);
				}
				
				left_edge = !left_edge;
				continue;
			}
			
			//The only possible option now are the full pieces.
			masks[i] = new Mask(context, !masks[i-puzzle_width].isBottom(), RAN.nextBoolean(), RAN.nextBoolean(), masks[i-1].isLeft(), difficulty);
		}
		
		Bitmap[] images = new Bitmap[masks.length];
		
		int position = 0;
		for(int y = 0; y < this.image.getHeight(); y += this.pieceSize)
		{
			for(int x = 0; x < this.image.getWidth(); x += this.pieceSize)
			{
				Bitmap store = Bitmap.createBitmap(pieceSize, pieceSize, Bitmap.Config.ARGB_8888);
				
				Canvas c = new Canvas(store);
				c.drawBitmap(this.image, -x, -y, null);
				Log.v("GEN", "[" + x + ", " + y + "]" );
//				Paint paint = new Paint();
//				paint.setColor(Color.BLACK);
//				paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
//				
//				c.drawBitmap(masks[position].getMask(), masks[position].getTopLeft().x, masks[position].getTopLeft().y, paint);
				images[position] = store;
				position++;
			}
		}

		return new Puzzle(images, puzzle_width);
	}

	/*
	 * There are only 4 possible corner types, thus we simply generate a random
	 * int from [0,4) and choose a corresponding corner.
	 */
	private Mask getRandomCorner()
	{
		int which = RAN.nextInt(4);
		Mask rv;

		switch (which)
		{
			case 0:
				rv = new Mask(context, false, false, difficulty);
				break;
			case 1:
				rv = new Mask(context, false, true, difficulty);
				break;
			case 2:
				rv = new Mask(context, true, false, difficulty);
				break;
			default:
				rv = new Mask(context, true, true, difficulty);
				break;
		}

		rv.rotate(1);
		return rv;
	}

	/*
	 * Corners are at the extremes of this puzzle. For example, if we have a
	 * puzzle like:
	 * ceeec Our width is 4 as is our height.
	 * e+++e
	 * e+++e Our first corner is at position 0 and 0%4 == 0.
	 * e+++e
	 * ceeec The second corner is at position 4 and 4%4 == 0.
	 * The third corner is at position 20 and 20%4 == 0.
	 * The final corner is at position 24 and 24%4 == 0.
	 */
	private boolean isCorner(int position, int puzzle_width, int puzzle_height)
	{
		return (position % puzzle_width == 0 && position % puzzle_height == 0);
	}

	/*
	 * Edges are a generalize corner case, as a corner IS an edge, just an edge
	 * on two sides. Thus only one of the conditions needs to be true.
	 * Note that because a corner is a special cased edge, one should check to
	 * see if it is a corner before checking for an edge.
	 */
	private boolean isEdge(int position, int puzzle_width, int puzzle_height)
	{
		return (position % puzzle_width == 0 || position % puzzle_height == 0);
	}
}
