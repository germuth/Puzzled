package ca.germuth.puzzled.puzzle.cube;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;

import ca.germuth.puzzled.R;
import ca.germuth.puzzled.openGL.shapes.Shape;
import ca.germuth.puzzled.openGL.shapes.Square;
import ca.germuth.puzzled.puzzle.ChangedTile;
import ca.germuth.puzzled.puzzle.Colour;
import ca.germuth.puzzled.puzzle.Puzzle;
import ca.germuth.puzzled.puzzle.PuzzleTurn;
/**
 * Cube 
 * 
 * Represents any IxJxK puzzle. In this way, it also encompasses 
 * rectangular puzzles such as 3x3x5. All puzzles are non-shapeshifting, this
 * means the 3x3x5 is restricted to 180 degree turns for many cases. 
 * 
 * Most Cube turns are split into two parts. Using 3x3 as example 
 * (1) A face rotation and 
 * (2) The side rotation
 * 
 * The face rotation consists of rotating the 9 pieces of a face
 * 90 degrees. A U or U' turn would turn the top 9 pieces. 
 * 
 * This U turn would also change 3 pieces on the L, R, F, and Back
 * layers. This is called the side rotation. 
 * 
 * The side rotations are done by frontRotatation(), topRotation(), and sideRotation().
 * The face rotation is called by faceRotation();
 * 
 * In this way, A U turn would consist of {
 * 		faceRotation( theTop )
 * 		topRotation ( 0 )
 * }
 * 
 * @author Germuth
 */
public class Cube implements Puzzle{

	private static final int myLayout = R.layout.puzzle_cube;
	
	private int width;
	private int height;
	private int depth;
	
	private CubeFace top;
	private CubeFace front;
	private CubeFace left;
	private CubeFace right;
	private CubeFace down;
	private CubeFace back;
	
	public Cube(int width, int height, int depth){
		assert(width > 1 && height > 1 && depth > 1);
		
		this.width = width;
		this.height = height;
		this.depth = depth;
		
		//make 6 cube faces
		//number of row then column
		//TODO grab saved values here rather than always use defaults
		this.top = new CubeFace(depth, width, new Colour(255, 255, 255, 0));
		this.front = new CubeFace(height, width, new Colour(0, 255, 0, 0));
		this.left = new CubeFace(depth, height, new Colour(255, 128, 0, 0));
		this.right = new CubeFace(depth, height, new Colour(255, 0, 0, 0)); 
		this.down = new CubeFace(depth, width, new Colour(255, 255, 0, 0)); 
		this.back = new CubeFace(height, width, new Colour(0, 0, 255, 0));
	}
	
	@Override
	public boolean isSolved() {
		//check if every colour in top is the same
		//check bottom at the same time
		if( this.top.isSolved() &&
				this.front.isSolved() &&
				this.left.isSolved() &&
				this.right.isSolved() &&
				this.back.isSolved() &&
				this.down.isSolved()){
			return true;
		}
		return false;
	}

	@Override
	public void setSolved() {
		this.front.setSolved();
		this.top.setSolved();
		this.back.setSolved();
		this.down.setSolved();
		this.left.setSolved();
		this.right.setSolved();
	}

	/**
	 * Performs a front turn. Used for F, S, and B Turns
	 * for a 3x3
	 * 		F -> layer = 2
	 * 		S -> layer = 1
	 * 		B -> layer = 0
	 * TODO above might be wrong
	 * @param layer
	 */
	private void frontRotation(int layer){
		//90 degree turn
		if(width == height){
			Colour[] temp = new Colour[width];
			//saves the top
			for(int j = 0; j < width; j++){
				temp[j] = this.top.mFace[layer][j];
			}
			for(int j = 0; j < width; j++){
				//replace top with left
				this.top.mFace[layer][j] = this.left.mFace[layer][j];
			}
			for(int j = 0; j < width; j++){
				//replace left with bottom
				this.left.mFace[layer][j] = this.down.mFace[width - layer - 1][width - j - 1];
			}
			for(int j = 0; j < width; j++){
				//replace bottom with right
				this.down.mFace[width - layer - 1][width - j - 1] = this.right.mFace[layer][j];
			}
			for(int j = 0; j < width; j++){
				//replace top
				this.right.mFace[layer][j] = temp[j];
			}
		}
		//180 degree turn
		if(width != height){
			Colour[] tempTop = new Colour[width];
			//saves the top
			for(int j = 0; j < width; j++){
				tempTop[j] = this.top.mFace[layer][j];
			}
			//move top to bottom
			for(int j = 0; j < width; j++){
				//replace top with left
				this.top.mFace[layer][j] = this.down.mFace[width - layer - 1][width - j - 1];
			}
			//bottom to top
			for(int j = 0; j < width; j++){
				this.down.mFace[width - layer - 1][width - j - 1] = tempTop[j];
			}
			
			Colour[] tempLeft = new Colour[height];
			//save the left
			for(int j = 0; j < width; j++){
				tempLeft[j] = this.left.mFace[layer][j];
			}
			for(int j = 0; j < width; j++){
				//replace left with bottom
				this.left.mFace[layer][j] = this.right.mFace[layer][j];
			}
			for(int j = 0; j < width; j++){
				this.right.mFace[layer][j] = tempLeft[j];
			}
		}
	}
	
	/**
	 * Performs a top turn. Used for F, S, and B Turns
	 * for a 3x3
	 * 		U -> layer = 2
	 * 		E -> layer = 1
	 * 		D -> layer = 0
	 * TODO above might be wrong
	 * @param layer
	 */
	private void topRotation(int layer){
		//90 degreeTurn
		if(width == depth){
			Colour[] temp = new Colour[depth];

			//saves the front
			for(int j = 0; j < depth; j++){
				temp[j] = this.front.mFace[layer][j];
			}

			for(int j = 0; j < depth; j++){
				//replace front with right
				this.front.mFace[layer][j] = this.right.mFace[depth - j - 1][layer];
			}
			for(int j = 0; j < depth; j++){
				//replace right with back
				this.right.mFace[depth - j - 1][layer] = this.back.mFace[depth - layer - 1][depth - j - 1];
			}
			for(int j = 0; j < depth; j++){
				//replace back with left
				this.back.mFace[depth - layer - 1][j] = this.left.mFace[depth - j - 1][depth - layer - 1];
			}
			for(int j = 0; j < depth; j++){
				//replace left
				this.left.mFace[depth - j - 1][depth - layer - 1] = temp[depth - j - 1];
			}
		}
		if( width != depth){
			Colour[] tempFront = new Colour[width];

			//saves the front
			for(int j = 0; j < width; j++){
				tempFront[j] = this.front.mFace[layer][j];
			}
			for(int j = 0; j < width; j++){
				//replace front with right
				this.front.mFace[layer][j] = this.back.mFace[depth - layer - 1][depth - j - 1];
			}
			for(int j = 0; j < width; j++){
				//replace back with left
				this.back.mFace[depth - layer - 1][j] = this.front.mFace[layer][j];
			}
			
			Colour[] tempRight = new Colour[depth];
			
			for(int j = 0; j < depth; j++){
				//replace right with back
				tempRight[j] = this.right.mFace[depth - j - 1][layer];
			}
			for(int j = 0; j < depth; j++){
				//replace left
				//TODO tempRight[j] or[depth - j -1]?
				this.left.mFace[depth - j - 1][depth - layer - 1] = tempRight[j];
			}
			for(int j = 0; j < depth; j++){
				//replace left
				//TODO tempRight[j] or[depth - j -1]?
				this.right.mFace[depth - j - 1][layer] = this.left.mFace[depth - j - 1][depth - layer - 1];
			}
		}
	}
	
	/**
	 * Performs a top turn. Used for F, S, and B Turns
	 * for a 3x3
	 * 		R -> layer = 2
	 * 		M -> layer = 1
	 * 		L' -> layer = 0
	 * TODO above might be wrong
	 * @param layer
	 */
	private void sideRotation(int layer){
		if(depth == height){
			Colour[] temp = new Colour[depth];

			//saves the top for later
			for(int j = 0; j < depth; j++){
				temp[j] = this.top.mFace[j][layer];
			}
			for(int j = 0; j < depth; j++){
				//replace top with front
				this.top.mFace[j][layer] = this.front.mFace[j][layer];
			}
			for(int j = 0; j < depth; j++){
				//replace front with bottom
				this.front.mFace[j][layer] = this.down.mFace[j][layer];
			}
			for(int j = 0; j < depth; j++){
				//replace bottom with back
				this.down.mFace[j][layer] = this.back.mFace[j][layer];
			}
			for(int j = 0; j < depth; j++){
				//replace back with top
				this.back.mFace[j][layer] = temp[j];
			}	
		}
		
		if(depth != height){
			Colour[] tempTop = new Colour[depth];

			//saves the top for later
			for(int j = 0; j < depth; j++){
				tempTop[j] = this.top.mFace[j][layer];
			}
			for(int j = 0; j < depth; j++){
				//replace bottom with back
				this.down.mFace[j][layer] = this.top.mFace[j][layer];
			}
			for(int j = 0; j < depth; j++){
				//replace bottom with back
				this.top.mFace[j][layer] = tempTop[j];
			}
			
			Colour[] tempFront = new Colour[height];
			for(int j = 0; j < height; j++){
				tempFront[j] = this.front.mFace[j][layer];
			}
			for(int j = 0; j < height; j++){
				this.front.mFace[j][layer] = this.back.mFace[j][layer];
			}
			for(int j = 0; j < height; j++){
				this.back.mFace[j][layer] = this.top.mFace[j][layer];
			}
		}
	}

	/**
	 * Rotates the face of each turn. For example on a F turn in a 3x3, it would
	 * rotate the 9 squares of the front face
	 * 
	 * rotates 90 or 180 degrees depending on the cube
	 */
	private void rotateFace(CubeFace cf) {
		//90 degrees
		if (cf.mFace.length == cf.mFace[0].length) {
			rotateEvenFace(cf);
		}// 180 degrees 
		else {
			rotateEvenFace(cf);
			rotateEvenFace(cf);
		}
	}

	/**
	 * Rotates a face 90 degrees
	 * @param cf
	 */
	private void rotateEvenFace(CubeFace cf) {
		Colour[][] face = cf.mFace;
		
		final int M = face.length;
		final int N = face[0].length;

		Colour[][] faceCopy = new Colour[N][M];
		//create copy
		for (int i = 0; i < face.length; i++) {
			Colour[] row = face[i];
			int length = row.length;
			faceCopy[i] = new Colour[length];
			System.arraycopy(row, 0, faceCopy[i], 0, length);
		}

		// r designates row
		for (int r = 0; r < M; r++) {
			// c designates column
			for (int c = 0; c < N; c++) {
				face[c][M - 1 - r] = faceCopy[r][c];
			}
		}
	}
	
	@Override
	public ArrayList<PuzzleTurn> getAllMoves() {
		ArrayList<PuzzleTurn> moves = new ArrayList<PuzzleTurn>();
		try {
			Class<? extends Cube> c = this.getClass();
			//Single Slice Turn
			
			for (int i = 0; i <= ((width + 1) / 2) - 1; i++) {
				//R, 2R, 3R, etc are single slice turns according to SiGN
				PuzzleTurn R = new PuzzleTurn(this, (i+1) + "R",
						new Method[]{c.getMethod("RTurn", int.class, int.class)},
						new Object[]{ i, i},
						null, (float)Math.PI);
				moves.add(R);
				PuzzleTurn L = new PuzzleTurn(this, (i+1) + "L'",
						new Method[]{c.getMethod("LPrimeTurn", int.class, int.class)},
						new Object[]{i, i},
						null, (float)Math.PI);
				moves.add(L);
			}
			for(int i = 0; i <= ((height + 1) / 2) - 1; i++){
				PuzzleTurn U = new PuzzleTurn(this, (i+1) + "U",
						new Method[]{c.getMethod("UTurn", int.class, int.class)},
						new Object[]{i, i},
						null, (float)Math.PI);
				moves.add(U);
				PuzzleTurn D = new PuzzleTurn(this, (i+1) + "D",
						new Method[]{c.getMethod("DTurn", int.class, int.class)},
						new Object[]{i, i},
						null, (float)Math.PI);
				moves.add(D);
			}
			for(int i = 0; i <= ((depth + 1) / 2) - 1; i++){
				PuzzleTurn F = new PuzzleTurn(this, (i+1) + "F",
						new Method[]{c.getMethod("FTurn", int.class, int.class)},
						new Object[]{i, i},
						null, (float)Math.PI);
				moves.add(F);
				PuzzleTurn B = new PuzzleTurn(this, (i+1) + "B'",
						new Method[]{c.getMethod("BPrimeTurn", int.class, int.class)},
						new Object[]{i, i},
						null, (float)Math.PI);
				moves.add(B);
			}
			//add the multislice turns
			//grab first 6 turns above
			int size = moves.size();
			for (int i = 0; i < size; i++) {
				PuzzleTurn current = moves.get(i);

				char firstChar = current.getmName().charAt(0);
				if ( Character.isDigit(firstChar)) {
					String newName = current.getmName().toLowerCase(Locale.US);
					if( firstChar == '2'){
						newName = newName.substring(1);
					}
					PuzzleTurn bigTurn = new PuzzleTurn(this, newName, current.getmMethod(),
							new Object[] { 0, (Integer) (current.getmArguments()[0]) },
							null, (current.getmRotation()));
					moves.add(bigTurn);
				}
			}
			size = moves.size();
			//for each element in the array, add the reverse turns
			for(int i = 0; i < size; i++){
				PuzzleTurn current = moves.get(i);
				String name = null;
				if( current.getmName().endsWith("'")){
					name = current.getmName().substring(0, current.getmName().length());
				}else{
					name = current.getmName() + "'";
				}
				PuzzleTurn reverseTurn = new PuzzleTurn(this, name, 
						//triple whatever turn was done before to turn the other way
						PuzzleTurn.concatenate(current.getmMethod(), 3),
						new Object[]{i, i}, null, -(current.getmRotation()));
				moves.add(reverseTurn);
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return moves;
	}
	
	@Override
	public ArrayList<PuzzleTurn> getAllRotationMoves() {
		ArrayList<PuzzleTurn> rotations = new ArrayList<PuzzleTurn>();
		try{
			Class<? extends Cube> c = this.getClass();
			PuzzleTurn Y = new PuzzleTurn(this, "y",
					new Method[]{c.getMethod("YTurn", int.class, int.class)},
					null, null, 0);
			rotations.add(Y);
			PuzzleTurn X = new PuzzleTurn(this, "x",
					new Method[]{c.getMethod("XTurn", int.class, int.class)},
					null, null, 0);
			rotations.add(X);
			PuzzleTurn Z = new PuzzleTurn(this, "z",
					new Method[]{c.getMethod("ZTurn", int.class, int.class)},
					null, null, 0);
			rotations.add(Z);
			
			for(int i = 0; i < rotations.size(); i++){
				PuzzleTurn current = rotations.get(i);
				
				PuzzleTurn reverse = new PuzzleTurn(this,
						current.getmName() + "'", 
						PuzzleTurn.concatenate(current.getmMethod(), 3),
						null, null, 0);
				rotations.add(reverse);
			}
		}catch(NoSuchMethodException e){
			e.printStackTrace();
		}
		
		return rotations;
	}

	@Override
	public ArrayList<ChangedTile> getChangedTiles() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void RTurn(int startLayer, int endLayer){
		for(int i = startLayer; i <= endLayer; i++){
			this.sideRotation((depth - 1) - i);
		}
		this.rotateFace(this.right);
	}
	
	public void LPrimeTurn(int startLayer, int endLayer){
		for(int i = startLayer; i <= endLayer; i++){
			this.sideRotation(i);
		}
		this.rotateFace(this.left);
		this.rotateFace(this.left);
		this.rotateFace(this.left);
	}
	
	public void UTurn(int startLayer, int endLayer){
		for(int i = startLayer; i <= endLayer; i++){
			this.topRotation(i);
		}
		this.rotateFace(this.top);
	}
	
	public void DTurn(int startLayer, int endLayer){
		for(int i = startLayer; i <= endLayer; i++){
			this.sideRotation((height - 1) - i);
		}
		this.rotateFace(this.down);
		this.rotateFace(this.down);
		this.rotateFace(this.down);
	}

	public void FTurn(int startLayer, int endLayer){
		for(int i = startLayer; i <= endLayer; i++){
			this.frontRotation((depth - 1) - i);
		}
		this.rotateFace(this.front);
	}

	public void BPrimeTurn(int startLayer, int endLayer){
		for(int i = startLayer; i <= endLayer; i++){
			this.frontRotation(i);
		}
		this.rotateFace(this.back);
		this.rotateFace(this.back);
		this.rotateFace(this.back);
	}
	
	//---------------------------------------------
	//CUBE ROTATIONS
	//---------------------------------------------
	public void yTurn(){
		for(int i = 0; i < height; i++){
			topRotation(i);
		}
		this.rotateFace(this.top);
		this.rotateFace(this.down);
		this.rotateFace(this.down);
		this.rotateFace(this.down);
	}

	public void yPrimeTurn(){
		yTurn();
		yTurn();
		yTurn();
	}

	//rotates the cube on R
	public void xTurn(){
		for(int i = 0; i < height; i++){
			sideRotation(i);
		}
		this.rotateFace(this.right);
		this.rotateFace(this.left);
		this.rotateFace(this.left);
		this.rotateFace(this.left);
	}

	//rotates the cube on F
	public void zTurn(){
		for(int i = 0; i < height; i++){
			frontRotation(i);
		}
		this.rotateFace(this.front);
		this.rotateFace(this.back);
		this.rotateFace(this.back);
		this.rotateFace(this.back);
	}

	public void zPrimeTurn(){
		zTurn();
		zTurn();
		zTurn();
	}

	public void xPrimeTurn(){
		xTurn();
		xTurn();
		xTurn();
	}
	
	public static int getLayout(){
		return myLayout;
	}

	@Override
	public ArrayList<Shape> drawPuzzle() {
		ArrayList<Shape> myFaces = new ArrayList<Shape>();
		
		ArrayList<Square> topF = drawFace(top);
		Square.finalizeAll(mTop);
		myFaces.addAll(topF);

		ArrayList<Square> frontF = drawFace(this.mCube.getFront());
		Square.rotateAll(mFront, 'X', (float) Math.PI / 2);
		Square.finalizeAll(mFront);
		myFaces.addAll(frontF);

		mBack = drawFace(this.mCube.getBack());
		Square.rotateAll(mBack, 'X', (float) -Math.PI / 2);
		Square.finalizeAll(mBack);
		myFaces.addAll(mBack);

		mLeft = drawFace(this.mCube.getLeft());
		Square.rotateAll(mLeft, 'Z', (float) (Math.PI / 2));
		Square.finalizeAll(mLeft);
		myFaces.addAll(mLeft);

		mRight = drawFace(this.mCube.getRight());
		Square.rotateAll(mRight, 'Z', (float) -(Math.PI / 2));
		Square.finalizeAll(mRight);
		myFaces.addAll(mRight);

		//bottom can't be rotated while preserving orientation
		//must translate directly down
		//this might not be true somehow
		//definetly not true somehow
	  	mBottom = drawFace(this.mCube.getBottom());
		//Square.translateAll(mBottom, 'Y', -1.40);
	  	Square.rotateAll(mBottom, 'X', (float) (Math.PI ));
		Square.finalizeAll(mBottom);
		myFaces.addAll(mBottom);
		
		return null;
	}
	
	private ArrayList<Square> drawFace( CubeFace side ){
		ArrayList<Square> face = new ArrayList<Square>();

		int cubeSize = this.mCube.getSize(); 

		float faceLength = 120f / cubeSize;
		faceLength /= 100;
		float spaceLength = 20f / ( cubeSize + 1 );
		spaceLength /= 100;
		final float height = 0.70f;

		float topCornerX = -0.70f;
		float topCornerZ = -0.70f;

		topCornerX += spaceLength;
		topCornerZ += spaceLength;

		float currentTopRightX = topCornerX;
		float currentTopRightZ = topCornerZ;

		//iterate down
		for(int i = 0; i < cubeSize; i++){
			//reset top right coordinates after each row
			currentTopRightX = topCornerX;

			//iterate across
			for(int j = 0; j < cubeSize; j++){

				//make face
				Square current = new Square(
						new GLVertex( currentTopRightX,              height, currentTopRightZ ), 
						new GLVertex( currentTopRightX,              height, currentTopRightZ + faceLength ), 
						new GLVertex( currentTopRightX + faceLength, height, currentTopRightZ + faceLength), 
						new GLVertex( currentTopRightX + faceLength, height, currentTopRightZ ) );
				current.setmColour( colourToGLColour(side[0][0]) );
				face.add( current );

				currentTopRightX += faceLength + spaceLength;
			}

			//once we finish a row, we need to move down to next row
			currentTopRightZ += faceLength + spaceLength;
		}

		return face;
	}
}
