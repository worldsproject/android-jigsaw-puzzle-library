An Android library that facilites the creation of Jigsaw puzzles.

All code is GPLv3

## How do I create puzzles with the puzzle library

So, you have the puzzle library as an eclipse project, and it's built all properly, with no errors.

1. Mark the project as a library
   * Right click on the puzzle library.
   * Click properties
   * Go to Android. 
   * Scroll Down and check "Is Library"

2. Create a new application. This will be your future puzzle app.
   * File > Other
   * New Android Application
   * Give it a name, such as "MyPuzzle"
   * Build SDK should be API 16 and minimum SDK should be API 8
   * Click next until you can click finish.

3. Tie in the Puzzle Library to your new app.
   * Right click on MyPuzzle
   * Click properties
   * Go to Android
   * Click Add...
   * Select the puzzle library.
   * Click Okay

4. Add new images for your puzzle
   * Create a new folder in res called "drawable-nodpi"
   * Add your puzzle images to this folder.
   * As an example we will use "image1" and "image2"

5. Edit MainActivity to create the puzzle.
   * Delete the created method onCreateOptionsMenu
   * Your onCreate method should look like:

``` Java
super.onCreate(savedInstanceState);
		
    int[] mImageIds = { R.drawable.image1, R.drawable.image2, };
		
    Intent intent = new Intent(this, PuzzleSelectActivity.class);
    intent.putExtra("images", mImageIds);
		
    this.startActivity(intent);
```

6. And you should add the following to your manifest:

``` Java
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 
<activity android:name="org.worldsproject.puzzle.PuzzleSolveActivity" >
</activity>
 
<activity android:name="org.worldsproject.puzzle.PuzzleSelectActivity" >
</activity>
```

You should be able to run MyPuzzle and it will display the two images as available puzzles.















