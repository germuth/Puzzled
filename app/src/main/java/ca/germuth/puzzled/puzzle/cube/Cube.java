package ca.germuth.puzzled.puzzle.cube;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import ca.germuth.puzzled.PuzzleMoveListener;
import ca.germuth.puzzled.R;
import ca.germuth.puzzled.openGL.GLVertex;
import ca.germuth.puzzled.openGL.shapes.Shape;
import ca.germuth.puzzled.openGL.shapes.Square;
import ca.germuth.puzzled.puzzle.Puzzle;
import ca.germuth.puzzled.puzzle.PuzzleTurn;
import ca.germuth.puzzled.puzzle.Tile;

/**
 * Cube
 * 
 * Represents any NxNxN puzzle. 
 * The physical cube is encoded into 6 2D arrays of size NxN. 
 * Image looking at the cube from birds eye view, and flattening it like so (3x3x3):
 * 
 *       BBB
 *       BBB
 *       BBB
 * 
 *  LLL  UUU  RRR
 *  LLL  UUU  RRR
 *  LLL  UUU  RRR
 *  
 *       FFF
 *       FFF
 *       FFF
 *       
 *       DDD
 *       DDD
 *       DDD
 *         
 * Each face is labeled using the first word of its side, for example:
 * 
 * Up, Down, Left, Right, Front, Back {U, D, L, R, F, B}
 * In fact the entire cube syntax follows SiGN notation which can be seen here:
 * http://www.mzrg.com/rubik/nota.shtml
 * 
 * Most Cube turns are split into two parts. Using 3x3 as example (1) A face
 * rotation and (2) The side rotation
 * 
 * The face rotation consists of rotating the 9 pieces of a face 90 degrees. A U
 * or U' turn would turn the top 9 pieces.
 * 
 * This U turn would also change 3 pieces on the L, R, F, and Back layers. This
 * is called the side rotation.
 * 
 * The side rotations are done by horizontalRotatation(), verticalRotation(), and
 * intoScreenRotation().
 * 
 * @author Aaron Germuth
 */
public class Cube implements Puzzle {

//	private static final int myLayout = R.layout.cube_layout_button;
	private static final int myLayout = R.layout.cube_layout_swipe;
	private static ArrayList<PuzzleTurn> myMoves;

	//TODO temporary
	private int height, width, depth;
	
	private int size;
	private Tile[][] up;
	private Tile[][] front;
	private Tile[][] left;
	private Tile[][] right;
	private Tile[][] down;
	private Tile[][] back;

//	private ConcurrentLinkedQueue<Tile> changedTiles;
//	private volatile ArrayList<Tile> changedTiles;
	//does this need to be concurrent?
	private HashSet<Tile> changedTiles;
	private OnPuzzleSolvedListener mOnPuzzleSolvedListener;

	public static final String[] constructor_param_titles = {"Width", "Height", "Depth"};
		
	public Cube(int size) {
		this.size = size;
		this.height = size;
		this.width = size;
		this.depth = size;
		
		this.setSolved();
		//shouldn't need anymore because hashmap
		// TODO slow and terrible
		this.changedTiles = new HashSet<Tile>();
//		this.changedTiles = new ConcurrentLinkedQueue<Tile>() {
//			@Override
//			public boolean add(Tile object) {
//				if (this.contains(object)) {
//					return false;
//				} else {
//					return super.add(object);
//				}
//			}
//		};
	}
	
	/**
	 * this name is used for puzzle name in database
	 */
	@Override
	public String getName() {
		return this.size + "x" + this.size + "x" + this.size + " cube";
	}
	
	@Override
	public boolean isSolved() {
		return isFaceSolved(this.up) && isFaceSolved(this.down) && isFaceSolved(this.left)
				&& isFaceSolved(this.right) && isFaceSolved(this.left) && isFaceSolved(this.right);
	}
	
	@Override
	public void setSolved() {
		// TODO grab saved values here rather than always use defaults
		this.up = createCubeFace(new Tile(255, 255, 255, 0), size);
		this.front = createCubeFace(new Tile(0, 255, 0, 0), size);
		this.left = createCubeFace(new Tile(255, 128, 0, 0), size);
		this.right = createCubeFace(new Tile(255, 0, 0, 0), size);
		this.down = createCubeFace(new Tile(255, 255, 0, 0), size);
		this.back = createCubeFace(new Tile(0, 0, 255, 0), size);
	}
	
	public void checkSolved(){
		if(this.isSolved()){
			if(this.mOnPuzzleSolvedListener != null){
				this.mOnPuzzleSolvedListener.onPuzzleSolved();
			}	
		}
	}

	private Tile[][] faceToArray(CubeFace f){
		switch(f){
			case UP: return this.up;
			case DOWN: return this.down;
			case LEFT: return this.left;
			case RIGHT: return this.right;
			case FRONT: return this.front;
			case BACK: return this.back;
			default:
				return null;
		}
	}
	private Tile getTile(CubeFace f, int row, int col){
		return faceToArray(f)[row][col];
	}
	
	private void setTile(CubeFace f, int row, int col, Tile color){
		faceToArray(f)[row][col] = color;
		this.changedTiles.add(faceToArray(f)[row][col]);
	}
	/**
	 * Performs a horizontal rotation when layer == 0, a U turn is done when layer == size, a D'
	 * turn is done
	 */
	private void horizontalRotation(int layer) {
		for (int j = 0; j < size; j++) {
			Tile temp = getTile(CubeFace.FRONT, layer, j);
			setTile(CubeFace.FRONT, layer, j, 
					getTile(CubeFace.RIGHT, size - j - 1, layer));
			setTile(CubeFace.RIGHT, size - j - 1, layer,
					getTile(CubeFace.BACK, size - layer - 1, size - j - 1));
			setTile(CubeFace.BACK, size - layer - 1, size - j - 1,
					getTile(CubeFace.LEFT, j, size - layer - 1));
			setTile(CubeFace.LEFT, j, size - layer - 1, temp);
		}
	}

	/**
	 * Performs a vertical rotation when layer == 0, an R turn when layer == size, L' turn
	 */
	private void verticalRotation(int layer) {
		for (int j = 0; j < size; j++) {
			Tile temp = getTile(CubeFace.UP, j, layer);
			setTile(CubeFace.UP, j, layer,
					getTile(CubeFace.FRONT, j, layer));
			setTile(CubeFace.FRONT, j, layer, 
					getTile(CubeFace.DOWN, j, layer));
			setTile(CubeFace.DOWN, j, layer, 
					getTile(CubeFace.BACK, j, layer));
			setTile(CubeFace.BACK, j, layer, temp);
		}
	}

	/**
	 * Performs a front and back rotation when layer == 0, F turn when layer == size, B turn
	 */
	private void intoScreenRotation(int layer) {
		for (int j = 0; j < size; j++) {
			Tile temp = getTile(CubeFace.UP, layer, j);
			setTile(CubeFace.UP, layer, j,
					getTile(CubeFace.LEFT, layer, j));
			setTile(CubeFace.LEFT, layer, j, 
					getTile(CubeFace.DOWN, size - layer - 1, size - j - 1));
			setTile(CubeFace.DOWN, size - layer - 1, size - j - 1,
					getTile(CubeFace.RIGHT, layer, j));
			setTile(CubeFace.RIGHT, layer, j, temp);
		}
	}

	/**
	 * Rotates the face of each turn. For example on a F turn in a 3x3, it would
	 * rotate the 9 squares of the front face
	 * 
	 * rotates 90 or 180 degrees depending on the cube
	 */
	private void rotateFaceClockwise(CubeFace face) {
		Tile[][] arr = faceToArray(face);
		final int n = arr.length;
		Tile[][] arrCopy = new Tile[n][n];
		for(int i = 0; i < n; i++){
			arrCopy[i] = Arrays.copyOf(arr[i], n);
		}
		
		for (int i = 0; i < n; ++i) {
	        for (int j = 0; j < n; ++j) {
	        	setTile(face, i, j, arrCopy[n-j-1][i]);
			}
		}
	}

	@Override
	public ArrayList<PuzzleTurn> getAllMoves() {
		ArrayList<PuzzleTurn> moves = new ArrayList<PuzzleTurn>();
		try {
			Class<? extends Cube> c = this.getClass();
			// Single Slice Turn
			
			float angle = -90f;
			if(height != depth){
				angle *= 2;
			}
			for (int i = 0; i <= ((width + 1) / 2) - 1; i++) {
				// R, 2R, 3R, etc are single slice turns according to SiGN
				PuzzleTurn R = new PuzzleTurn(this, (i + 1) + "R",
						new Method[] { c.getMethod("RTurn", int.class,
								int.class) }, new Object[] { new Object[] { i,
								i } }, angle, 'X');

				moves.add(R);
				PuzzleTurn L = new PuzzleTurn(this, (i + 1) + "L'",
						new Method[] { c.getMethod("LPrimeTurn", int.class,
								int.class) }, new Object[] { new Object[] { i,
								i } }, angle, 'X');
				moves.add(L);
			}
			angle = -90f;
			if(width != depth){
				angle *= 2;
			}
			for (int i = 0; i <= ((height + 1) / 2) - 1; i++) {
				PuzzleTurn U = new PuzzleTurn(this, (i + 1) + "U",
						new Method[] { c.getMethod("UTurn", int.class,
								int.class) }, new Object[] { new Object[] { i,
								i } }, angle, 'Y');
				moves.add(U);
				PuzzleTurn D = new PuzzleTurn(this, (i + 1) + "D'",
						new Method[] { c.getMethod("DPrimeTurn", int.class,
								int.class) }, new Object[] { new Object[] { i,
								i } }, angle, 'Y');
				moves.add(D);
			}
			angle = -90f;
			if(width != height){
				angle *= 2;
			}
			for (int i = 0; i <= ((depth + 1) / 2) - 1; i++) {
				PuzzleTurn F = new PuzzleTurn(this, (i + 1) + "F",
						new Method[] { c.getMethod("FTurn", int.class,
								int.class) }, new Object[] { new Object[] { i,
								i } }, -90f, 'Z');
				moves.add(F);
				PuzzleTurn B = new PuzzleTurn(this, (i + 1) + "B'",
						new Method[] { c.getMethod("BPrimeTurn", int.class,
								int.class) }, new Object[] { new Object[] { i,
								i } }, -90f, 'Z');
				moves.add(B);
			}
			// add the multislice turns
			// grab first 6 turns above
			int size = moves.size();
			for (int i = 0; i < size; i++) {
				PuzzleTurn current = moves.get(i);

				char firstChar = current.getmName().charAt(0);
				if (Character.isDigit(firstChar)) {
					String newName = current.getmName().toLowerCase(Locale.US);
					if (firstChar == '2') {
						newName = newName.substring(1);
					}
					Object[] args = current.getmArguments();
					Object[] args1 = (Object[]) args[0];
					PuzzleTurn bigTurn = new PuzzleTurn(this, newName,
							current.getMethods(), new Object[] { new Object[] {
									0, (Integer) (args1[0]) } },
							(current.getmAngle()), current.getAxis());
					moves.add(bigTurn);
				}
			}
			size = moves.size();
			// for each element in the array, add the reverse turns
			for (int i = 0; i < size; i++) {
				PuzzleTurn current = moves.get(i);
				String name = null;
				if (current.getmName().endsWith("'")) {
					name = current.getmName().substring(0,
							current.getmName().length() - 1);
				} else {
					name = current.getmName() + "'";
				}
				Object[] args = current.getmArguments();
				Object[] arg1 = (Object[]) args[0];
				PuzzleTurn reverseTurn = new PuzzleTurn(this,
						name,
						// triple whatever turn was done before to turn the
						// other way
						PuzzleTurn.concatenate(current.getMethods(), 3),
						new Object[] { new Object[] { arg1[0], arg1[1] },
								new Object[] { arg1[0], arg1[1] },
								new Object[] { arg1[0], arg1[1] } },
						(current.getmAngle() * -1), current.getAxis());
				moves.add(reverseTurn);

			}
			
			ArrayList<PuzzleTurn> rotations = new ArrayList<PuzzleTurn>();
			// add rotations
			PuzzleTurn Y = new PuzzleTurn(this, "y",
					new Method[] { c.getMethod("yTurn", (Class[]) null) },
					new Object[]{null}, -90f, 'Y');
			Y.setRotation(true);
			rotations.add(Y);
			PuzzleTurn X = new PuzzleTurn(this, "x",
					new Method[] { c.getMethod("xTurn", (Class[]) null) },
					new Object[]{null}, -90f, 'X');
			X.setRotation(true);
			rotations.add(X);
			PuzzleTurn Z = new PuzzleTurn(this, "z",
					new Method[] { c.getMethod("zTurn", (Class[]) null)},
					new Object[]{null}, -90, 'Z');
			Z.setRotation(true);
			rotations.add(Z);

			for (int i = 0; i < rotations.size(); i++) {
				PuzzleTurn current = rotations.get(i);

				PuzzleTurn reverse = new PuzzleTurn(this,
						current.getmName() + "'", PuzzleTurn.concatenate(
								current.getMethods(), 3), new Object[]{null, null, null}, 
								current.getmAngle() * -1, current.getAxis());
				reverse.setRotation(true);
				moves.add(reverse);
			}
			
			for(int k = 0; k < rotations.size(); k++){
				moves.add(rotations.get(k));
			}
			
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		myMoves = moves;
		return moves;
	}

	public void RTurn(int startLayer, int endLayer) {
		for (int i = startLayer; i <= endLayer; i++) {
			this.verticalRotation((depth - 1) - i);
		}
		if(startLayer == 0){
			this.rotateFaceClockwise(CubeFace.RIGHT);
		}
	}

	public void LPrimeTurn(int startLayer, int endLayer) {
		for (int i = startLayer; i <= endLayer; i++) {
			this.verticalRotation(i);
		}
		if(startLayer == 0){
			this.rotateFaceClockwise(CubeFace.LEFT);
			this.rotateFaceClockwise(CubeFace.LEFT);
			this.rotateFaceClockwise(CubeFace.LEFT);
		}
	}

	public void UTurn(int startLayer, int endLayer) {
		for (int i = startLayer; i <= endLayer; i++) {
			this.horizontalRotation(i);
		}
		if(startLayer == 0){
			this.rotateFaceClockwise(CubeFace.UP);
		}
	}

	public void DPrimeTurn(int startLayer, int endLayer) {
		for (int i = startLayer; i <= endLayer; i++) {
			this.horizontalRotation((height - 1) - i);
		}
		if(startLayer == 0){
			this.rotateFaceClockwise(CubeFace.DOWN);
			this.rotateFaceClockwise(CubeFace.DOWN);
			this.rotateFaceClockwise(CubeFace.DOWN);
		}
	}

	public void FTurn(int startLayer, int endLayer) {
		for (int i = startLayer; i <= endLayer; i++) {
			this.intoScreenRotation((depth - 1) - i);
		}
		if(startLayer == 0){
			this.rotateFaceClockwise(CubeFace.FRONT);
		}
	}

	public void BPrimeTurn(int startLayer, int endLayer) {
		for (int i = startLayer; i <= endLayer; i++) {
			this.intoScreenRotation(i);
		}
		if(startLayer == 0){
			this.rotateFaceClockwise(CubeFace.BACK);
			this.rotateFaceClockwise(CubeFace.BACK);
			this.rotateFaceClockwise(CubeFace.BACK);
		}
	}

	// ---------------------------------------------
	// CUBE ROTATIONS
	// ---------------------------------------------
	public void yTurn() {
		for (int i = 0; i < height; i++) {
			horizontalRotation(i);
		}
		this.rotateFaceClockwise(CubeFace.UP);
		this.rotateFaceClockwise(CubeFace.DOWN);
		this.rotateFaceClockwise(CubeFace.DOWN);
		this.rotateFaceClockwise(CubeFace.DOWN);
	}

	public void yPrimeTurn() {
		yTurn();
		yTurn();
		yTurn();
	}

	// rotates the cube on R
	public void xTurn() {
		for (int i = 0; i < height; i++) {
			verticalRotation(i);
		}
		this.rotateFaceClockwise(CubeFace.RIGHT);
		this.rotateFaceClockwise(CubeFace.LEFT);
		this.rotateFaceClockwise(CubeFace.LEFT);
		this.rotateFaceClockwise(CubeFace.LEFT);
	}

	// rotates the cube on F
	public void zTurn() {
		for (int i = 0; i < height; i++) {
			intoScreenRotation(i);
		}
		this.rotateFaceClockwise(CubeFace.FRONT);
		this.rotateFaceClockwise(CubeFace.BACK);
		this.rotateFaceClockwise(CubeFace.BACK);
		this.rotateFaceClockwise(CubeFace.BACK);
	}

	public void zPrimeTurn() {
		zTurn();
		zTurn();
		zTurn();
	}

	public void xPrimeTurn() {
		xTurn();
		xTurn();
		xTurn();
	}

	public static int getLayout(String inputType) {
		if(inputType.equals("Buttons")) {//BUTTONS
			return R.layout.cube_layout_button;
		}else if(inputType.equals("Swipe")) {//SWIPE
			return R.layout.cube_layout_swipe;
		}else {
			return myLayout;
		}
	}

	@Override
	public ArrayList<Shape> createPuzzleModel() {
		ArrayList<Shape> myFaces = new ArrayList<Shape>();

		ArrayList<Shape> topF = drawFace(up, depth, width, height);
		Square.finalizeAll(topF);
		myFaces.addAll(topF);

		ArrayList<Shape> frontF = drawFace(front, height, width, depth);
		Square.rotateAll(frontF, 'X', (float) Math.PI / 2);
		Square.finalizeAll(frontF);
		myFaces.addAll(frontF);

		ArrayList<Shape> backF = drawFace(back, height, width, depth);
		Square.rotateAll(backF, 'X', (float) -Math.PI / 2);
		Square.finalizeAll(backF);
		myFaces.addAll(backF);

		ArrayList<Shape> leftF = drawFace(left, depth, height, width);
		Square.rotateAll(leftF, 'Z', (float) (Math.PI / 2));
		Square.finalizeAll(leftF);
		myFaces.addAll(leftF);

		ArrayList<Shape> rightF = drawFace(right, depth, height, width);
		Square.rotateAll(rightF, 'Z', (float) -(Math.PI / 2));
		Square.finalizeAll(rightF);
		myFaces.addAll(rightF);

		// bottom can't be rotated while preserving orientation
		// must translate directly down
		// this might not be true somehow
		// definetly not true somehow
		ArrayList<Shape> bottomF = drawFace(down, depth, width, height);
		// Square.translateAll(mBottom, 'Y', -1.40);
		Square.rotateAll(bottomF, 'X', (float) (Math.PI));
		Square.finalizeAll(bottomF);
		myFaces.addAll(bottomF);

		return myFaces;
	}

	private ArrayList<Shape> drawFace(Tile[][] side, int heightLocal, int widthLocal, int centerDistance) {
		ArrayList<Shape> face = new ArrayList<Shape>();

		int largestSide = (Math.max(Math.max(this.width, this.height), this.depth));
		float faceLength = (240f / largestSide );
		//divide by 100 since entire model is with 1 of origin
		faceLength /= 100;
		
		float spaceLength = 40f / (largestSide + 1);
		spaceLength /= 100;
		
		float height = 1.40f;
		//adjust height based on smaller of width or height
		height = 1.40f * centerDistance / largestSide;
		
		//actual topCorner is only 1.4 if current side we are drawing has maximum amount of pieces
		float topCornerX = -1.40f;
		float topCornerZ = -1.40f;
		
		//adjust topCorner depending on Local height and width
		//  largest     height
		//  1.4            x
		topCornerX = -1.40f * widthLocal / largestSide;
		topCornerZ = -1.40f * heightLocal / largestSide;
				
		topCornerX += spaceLength;
		topCornerZ += spaceLength;

		float currentTopRightX = topCornerX;
		float currentTopRightZ = topCornerZ;

		// iterate down
		for (int i = 0; i < heightLocal; i++) {
			// reset top right coordinates after each row
			currentTopRightX = topCornerX;

			// iterate across
			for (int j = 0; j < widthLocal; j++) {

				// make face
				Square current = new Square(new GLVertex(currentTopRightX,
						height, currentTopRightZ),
						new GLVertex(currentTopRightX, height, currentTopRightZ
								+ faceLength), new GLVertex(currentTopRightX
								+ faceLength, height, currentTopRightZ
								+ faceLength), new GLVertex(currentTopRightX
								+ faceLength, height, currentTopRightZ));
				current.setmColour(Shape.colourToGLColour(side[0][0]));
				face.add(current);

				currentTopRightX += faceLength + spaceLength;
			}

			// once we finish a row, we need to move down to next row
			currentTopRightZ += faceLength + spaceLength;
		}

		for (int i = 0; i < side.length; i++) {
			for (int j = 0; j < side[i].length; j++) {
				int index = i * side[i].length + j;
				Tile t1 = side[i][j];
				t1.setmShape(face.get(index));
			}
		}

		return face;
	}
	
	private ArrayList<Square> drawFaceBACKUP(Tile[][] side) {
		ArrayList<Square> face = new ArrayList<Square>();

		// TODO this will only work for square cubes!
		// int cubeSize = this.mCube.getSize();
		int cubeSize = width;

		//move up from 70 to 140
		
		float faceLength = 240f / cubeSize;
		faceLength /= 100;
		float spaceLength = 40f / (cubeSize + 1);
		spaceLength /= 100;
		
		final float height = 1.40f;

		float topCornerX = -1.40f;
		float topCornerZ = -1.40f;

		topCornerX += spaceLength;
		topCornerZ += spaceLength;

		float currentTopRightX = topCornerX;
		float currentTopRightZ = topCornerZ;

		// iterate down
		for (int i = 0; i < cubeSize; i++) {
			// reset top right coordinates after each row
			currentTopRightX = topCornerX;

			// iterate across
			for (int j = 0; j < cubeSize; j++) {

				// make face
				Square current = new Square(new GLVertex(currentTopRightX,
						height, currentTopRightZ),
						new GLVertex(currentTopRightX, height, currentTopRightZ
								+ faceLength), new GLVertex(currentTopRightX
								+ faceLength, height, currentTopRightZ
								+ faceLength), new GLVertex(currentTopRightX
								+ faceLength, height, currentTopRightZ));
				current.setmColour(Shape.colourToGLColour(side[0][0]));
				face.add(current);

				currentTopRightX += faceLength + spaceLength;
			}

			// once we finish a row, we need to move down to next row
			currentTopRightZ += faceLength + spaceLength;
		}

		for (int i = 0; i < side.length; i++) {
			for (int j = 0; j < side[i].length; j++) {
				int index = i * side[i].length + j;
				Tile t1 = side[i][j];
				t1.setmShape(face.get(index));
			}
		}

		return face;
	}

	@Override
	public HashSet<Tile> getChangedTiles() {
		return this.changedTiles;
	}

	@Override
	public void moveFinished() {
		this.changedTiles.clear();
	}

	@Override
	public void setOnPuzzleSolvedListener(OnPuzzleSolvedListener list) {
		mOnPuzzleSolvedListener = list;
	}

	@Override
	public OnPuzzleSolvedListener getOnPuzzleSolvedListener() {
		return mOnPuzzleSolvedListener;
	}

	public int getWidth() {
		return width;
	}
	
	private String orientMoves;
	
	private void OrientCube(){
//		Log.wtf("DEBUG", "BEFORE: " + this.top[1][1] + " " + this.front[1][1]);
		orientMoves = "";
		//move white center to the top
		Tile[][] white = getCenter(new Tile(255, 255, 255, 0));
		if(white == this.up){
			// none
		}else if( white == this.front){
			// x'
			this.xTurn();
			orientMoves += "x' ";
		}else if( white == this.left){
			// yyy x
			this.yTurn();
			this.xTurn();
			orientMoves += "y' x' ";
		}else if( white == this.right){
			//y x
			this.yTurn();
			this.xPrimeTurn();
			orientMoves += "y' x";
		}else if( white == this.back){
			//one x
			this.xPrimeTurn();
			orientMoves += "x ";
		}else if( white == this.down){
			//two xx
			this.xTurn();
			this.xTurn();
			orientMoves += "x' x' ";
		}
		
		//move green center to front
		Tile[][] green = getCenter(new Tile(0, 255, 0, 0));
		if(green == this.front){
			
		}else if( green == this.back){
			this.yPrimeTurn();
			this.yPrimeTurn();
			orientMoves += "y y ";
		}else if( green == this.right){
			this.yTurn();
			orientMoves +="y' ";
		}else if( green == this.left){
			this.yPrimeTurn();
			orientMoves +="y ";
		}
	}
	
	private void ReOrientCube(){
		String[] moves = this.orientMoves.split(" ");
		ArrayList<PuzzleTurn> pt = myMoves;
		for(int i = moves.length - 1; i >= 0; i--){
			for(int j = 0; j < pt.size(); j++){
				if( pt.get(j).getmName().equals(moves[i].trim())){
					PuzzleMoveListener.executePuzzleTurn(this, pt.get(j));
				}
			}
		}
	}
	
	private ArrayList<Tile[][]> getFaceList(){
		ArrayList<Tile[][]> faces = new ArrayList<Tile[][]>();
		faces.add(this.back);
		faces.add(this.down);
		faces.add(this.front);
		faces.add(this.up);
		faces.add(this.left);
		faces.add(this.right);
		return faces;
	}
	
	private Tile[][] getCenterFace(Tile t) {
		// find white center
		ArrayList<Tile[][]> faces = getFaceList();

		for (int i = 0; i < faces.size(); i++) {
			Tile[][] current = faces.get(i);
			if (Tile.matches(current[1][1], t)) {
				return current;
			}
		}
		return null;
	}
	
	private Tile[][] getCenter(Tile t) {
		return getCenterFace(t);
	}
	
	public boolean isCrossSolved(){
		OrientCube();
		
		Tile[][] white = getCenter(new Tile(255, 255, 255, 0));
		Tile[][] green = getCenter(new Tile(0  , 255,   0, 0));
		Tile[][] blue = getCenter(new Tile(   0,   0, 255, 0));
		Tile[][] orange = getCenter(new Tile(255, 128, 0, 0));
		Tile[][] red = getCenter(new Tile(  255,   0,   0,  0));
		
		if( Tile.matches(white[1][1], white[0][1]) &&
				Tile.matches(white[1][1], white[1][0]) &&
				Tile.matches(white[1][1], white[2][1]) &&
				Tile.matches(white[1][1], white[1][2]) &&
				Tile.matches(green[1][1], green[0][1]) &&
				Tile.matches(orange[1][1], orange[1][2]) &&
				Tile.matches(red[1][1], red[1][0]) &&
				Tile.matches(blue[1][1], blue[2][1])){
			ReOrientCube();
			return true;
		}
		ReOrientCube();
		return false;
	}
	
	public int pairsSolved(){
		OrientCube(); 
		
		Tile[][] white = getCenter(new Tile(255, 255, 255, 0));
		Tile[][] green = getCenter(new Tile(0  , 255,   0, 0));
		Tile[][] blue = getCenter(new Tile(   0,   0, 255, 0));
		Tile[][] orange = getCenter(new Tile(255, 128, 0, 0));
		Tile[][] red = getCenter(new Tile(  255,   0,   0,  0));
		
		int solved = 0;
		//FL pair
		if( Tile.matches(white[1][1], white[2][0]) &&
				Tile.matches(orange[1][1], orange[2][2]) &&
				Tile.matches(orange[1][1], orange[2][1]) &&
				Tile.matches(green[1][1], green[0][0]) &&
				Tile.matches(green[1][1], green[1][0])){
			solved++;
		}
		//FR pair
		if( Tile.matches(white[1][1], white[2][2]) &&
				Tile.matches(red[1][1], red[2][0]) &&
				Tile.matches(red[1][1], red[2][1]) &&
				Tile.matches(green[1][1], green[0][2]) &&
				Tile.matches(green[1][1], green[1][2])){
			solved++;
		}
		//BL pair
		if( Tile.matches(white[1][1], white[0][0]) &&
				Tile.matches(orange[1][1], orange[0][2]) &&
				Tile.matches(orange[1][1], orange[0][1]) &&
				Tile.matches(blue[1][1], blue[1][0]) &&
				Tile.matches(blue[1][1], blue[2][0])){
			solved++;
		}
		//BR pair
		if( Tile.matches(white[1][1], white[0][2]) &&
				Tile.matches(red[1][1], red[0][0]) &&
				Tile.matches(red[1][1], red[0][1]) &&
				Tile.matches(blue[1][1], blue[1][2]) &&
				Tile.matches(blue[1][1], blue[2][2])){
			solved++;
		}
		
		ReOrientCube();
		
		return solved;
	}
	
	public boolean isOLLSolved(){
		return isFaceSolved(this.getCenterFace( new Tile(255, 255, 0, 0)));
	}
	
	
	/**
	 * Creates a new 2d array of length size*size
	 * All elements initialized to Color c
	 */
	private static Tile[][] createCubeFace(Tile tile, int size) {
		Tile[][] arr = new Tile[size][size];
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				arr[i][j] = new Tile(tile.getRed(), tile.getGreen(), tile.getBlue(),
						tile.getAlpha());
			}
		}
		return arr;
	}
	
	/**
	 * Returns whether all tiles in arr have the same color
	 */
	public static boolean isFaceSolved(Tile[][] arr){
		for(int i = 0; i < arr.length; i++){
			for(int j = 0; j < arr[i].length; j++){
				if( ! Tile.matches(arr[i][j], arr[0][0] )){
					return false;
				}
			}
		}
		return true;
	}
}