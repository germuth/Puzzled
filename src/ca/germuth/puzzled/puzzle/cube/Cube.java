package ca.germuth.puzzled.puzzle.cube;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

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
 * Represents any IxJxK puzzle. In this way, it also encompasses rectangular
 * puzzles such as 3x3x5. All puzzles are non-shapeshifting, this means the
 * 3x3x5 is restricted to 180 degree turns for many cases.
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
 * The side rotations are done by frontRotatation(), topRotation(), and
 * sideRotation(). The face rotation is called by faceRotation();
 * 
 * In this way, A U turn would consist of { faceRotation( theTop ) topRotation (
 * 0 ) }
 * 
 * @author Germuth
 */
public class Cube implements Puzzle {

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

	private volatile ArrayList<Tile> changedTiles;
	private OnPuzzleSolvedListener mOnPuzzleSolvedListener;

	public Cube(int width, int height, int depth) {
		assert (width > 1 && height > 1 && depth > 1);

		this.width = width;
		this.height = height;
		this.depth = depth;

		// make 6 cube faces
		// number of row then column
		// TODO grab saved values here rather than always use defaults
		this.top = new CubeFace(depth, width, new Tile(255, 255, 255, 0));
		this.front = new CubeFace(height, width, new Tile(0, 255, 0, 0));
		this.left = new CubeFace(depth, height, new Tile(255, 128, 0, 0));
		this.right = new CubeFace(depth, height, new Tile(255, 0, 0, 0));
		this.down = new CubeFace(depth, width, new Tile(255, 255, 0, 0));
		this.back = new CubeFace(height, width, new Tile(0, 0, 255, 0));

		// TODO also slow and terrible
		this.changedTiles = new ArrayList<Tile>() {
			@Override
			public boolean add(Tile object) {
				if (this.contains(object)) {
					return false;
				} else {
					return super.add(object);
				}
			}
		};
	}
	
	@Override
	public String getName() {
		return this.width + "x" + this.height + "x" + this.depth + " cube";
	}
	
	@Override
	public boolean isSolved() {
		// check if every colour in top is the same
		// check bottom at the same time
		if (this.top.isSolved() && this.front.isSolved()
				&& this.left.isSolved() && this.right.isSolved()
				&& this.back.isSolved() && this.down.isSolved()) {
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
	
	private void checkSolved(){
		if(this.isSolved()){
			this.mOnPuzzleSolvedListener.onPuzzleSolved();
		}
	}

	/**
	 * Performs a front turn. Used for F, S, and B Turns for a 3x3 F -> layer =
	 * 2 S -> layer = 1 B -> layer = 0 TODO above might be wrong
	 * 
	 * @param layer
	 */
	private void frontRotation(int layer) {
		// 90 degree turn
		if (width == height) {
			Tile[] temp = new Tile[width];
			// saves the top
			for (int j = 0; j < width; j++) {
				temp[j] = this.top.mFace[layer][j];
			}
			for (int j = 0; j < width; j++) {
				// replace top with left
				this.top.mFace[layer][j] = this.left.mFace[layer][j];
			}
			for (int j = 0; j < width; j++) {
				// replace left with bottom
				this.left.mFace[layer][j] = this.down.mFace[width - layer - 1][width
						- j - 1];
			}
			for (int j = 0; j < width; j++) {
				// replace bottom with right
				this.down.mFace[width - layer - 1][width - j - 1] = this.right.mFace[layer][j];
			}
			for (int j = 0; j < width; j++) {
				// replace top
				this.right.mFace[layer][j] = temp[j];
			}
		}
		// 180 degree turn
		if (width != height) {
			Tile[] tempTop = new Tile[width];
			// saves the top
			for (int j = 0; j < width; j++) {
				tempTop[j] = this.top.mFace[layer][j];
			}
			// move top to bottom
			for (int j = 0; j < width; j++) {
				// replace top with left
				this.top.mFace[layer][j] = this.down.mFace[width - layer - 1][width
						- j - 1];
			}
			// bottom to top
			for (int j = 0; j < width; j++) {
				this.down.mFace[width - layer - 1][width - j - 1] = tempTop[j];
			}

			Tile[] tempLeft = new Tile[height];
			// save the left
			for (int j = 0; j < width; j++) {
				tempLeft[j] = this.left.mFace[layer][j];
			}
			for (int j = 0; j < width; j++) {
				// replace left with bottom
				this.left.mFace[layer][j] = this.right.mFace[layer][j];
			}
			for (int j = 0; j < width; j++) {
				this.right.mFace[layer][j] = tempLeft[j];
			}
		}

		for (int j = 0; j < width; j++) {
			this.changedTiles.add(this.top.mFace[layer][j]);
			this.changedTiles.add(this.left.mFace[layer][j]);
			this.changedTiles.add(this.down.mFace[width - layer - 1][width - j
					- 1]);
			this.changedTiles.add(this.right.mFace[layer][j]);
		}
	}

	/**
	 * Performs a top turn. Used for F, S, and B Turns for a 3x3 U -> layer = 2
	 * E -> layer = 1 D -> layer = 0 TODO above might be wrong
	 * 
	 * @param layer
	 */
	private void topRotation(int layer) {
		// 90 degreeTurn
		if (width == depth) {
			Tile[] temp = new Tile[depth];

			// saves the front
			for (int j = 0; j < depth; j++) {
				temp[j] = this.front.mFace[layer][j];
			}

			for (int j = 0; j < depth; j++) {
				// replace front with right
				this.front.mFace[layer][j] = this.right.mFace[depth - j - 1][layer];
			}
			for (int j = 0; j < depth; j++) {
				// replace right with back
				this.right.mFace[depth - j - 1][layer] = this.back.mFace[depth
						- layer - 1][depth - j - 1];
			}
			for (int j = 0; j < depth; j++) {
				// replace back with left
				this.back.mFace[depth - layer - 1][j] = this.left.mFace[depth
						- j - 1][depth - layer - 1];
			}
			for (int j = 0; j < depth; j++) {
				// replace left
				this.left.mFace[depth - j - 1][depth - layer - 1] = temp[depth
						- j - 1];
			}
		}
		if (width != depth) {
			Tile[] tempFront = new Tile[width];

			// saves the front
			for (int j = 0; j < width; j++) {
				tempFront[j] = this.front.mFace[layer][j];
			}
			for (int j = 0; j < width; j++) {
				// replace front with right
				this.front.mFace[layer][j] = this.back.mFace[depth - layer - 1][depth
						- j - 1];
			}
			for (int j = 0; j < width; j++) {
				// replace back with left
				this.back.mFace[depth - layer - 1][j] = this.front.mFace[layer][j];
			}

			Tile[] tempRight = new Tile[depth];

			for (int j = 0; j < depth; j++) {
				// replace right with back
				tempRight[j] = this.right.mFace[depth - j - 1][layer];
			}
			for (int j = 0; j < depth; j++) {
				// replace left
				// TODO tempRight[j] or[depth - j -1]?
				this.left.mFace[depth - j - 1][depth - layer - 1] = tempRight[j];
			}
			for (int j = 0; j < depth; j++) {
				// replace left
				// TODO tempRight[j] or[depth - j -1]?
				this.right.mFace[depth - j - 1][layer] = this.left.mFace[depth
						- j - 1][depth - layer - 1];
			}
		}

		for (int j = 0; j < depth; j++) {
			this.changedTiles.add(this.front.mFace[layer][j]);
			this.changedTiles.add(this.right.mFace[depth - j - 1][layer]);
			this.changedTiles.add(this.back.mFace[depth - layer - 1][depth - j
					- 1]);
			this.changedTiles.add(this.left.mFace[depth - j - 1][depth - layer
					- 1]);
		}
	}

	/**
	 * Performs a top turn. Used for F, S, and B Turns for a 3x3 R -> layer = 2
	 * M -> layer = 1 L' -> layer = 0 TODO above might be wrong
	 * 
	 * @param layer
	 */
	private void sideRotation(int layer) {
		if (depth == height) {
			Tile[] temp = new Tile[depth];

			// saves the top for later
			for (int j = 0; j < depth; j++) {
				temp[j] = this.top.mFace[j][layer];
			}
			for (int j = 0; j < depth; j++) {
				// replace top with front
				this.top.mFace[j][layer] = this.front.mFace[j][layer];
			}
			for (int j = 0; j < depth; j++) {
				// replace front with bottom
				this.front.mFace[j][layer] = this.down.mFace[j][layer];
			}
			for (int j = 0; j < depth; j++) {
				// replace bottom with back
				this.down.mFace[j][layer] = this.back.mFace[j][layer];
			}
			for (int j = 0; j < depth; j++) {
				// replace back with top
				this.back.mFace[j][layer] = temp[j];
			}
		}

		if (depth != height) {
			Tile[] tempTop = new Tile[depth];

			// saves the top for later
			for (int j = 0; j < depth; j++) {
				tempTop[j] = this.top.mFace[j][layer];
			}
			for (int j = 0; j < depth; j++) {
				// replace bottom with back
				this.down.mFace[j][layer] = this.top.mFace[j][layer];
			}
			for (int j = 0; j < depth; j++) {
				// replace bottom with back
				this.top.mFace[j][layer] = tempTop[j];
			}

			Tile[] tempFront = new Tile[height];
			for (int j = 0; j < height; j++) {
				tempFront[j] = this.front.mFace[j][layer];
			}
			for (int j = 0; j < height; j++) {
				this.front.mFace[j][layer] = this.back.mFace[j][layer];
			}
			for (int j = 0; j < height; j++) {
				this.back.mFace[j][layer] = this.top.mFace[j][layer];
			}
		}

		for (int j = 0; j < height; j++) {
			this.changedTiles.add(this.top.mFace[j][layer]);
			this.changedTiles.add(this.down.mFace[j][layer]);
			this.changedTiles.add(this.front.mFace[j][layer]);
			this.changedTiles.add(this.back.mFace[j][layer]);
		}
	}

	/**
	 * Rotates the face of each turn. For example on a F turn in a 3x3, it would
	 * rotate the 9 squares of the front face
	 * 
	 * rotates 90 or 180 degrees depending on the cube
	 */
	private void rotateFace(CubeFace cf) {

		// 90 degrees
		if (cf.mFace.length == cf.mFace[0].length) {
			rotateEvenFace(cf);
		}// 180 degrees
		else {
			rotateEvenFace(cf);
			rotateEvenFace(cf);
		}

		for (int i = 0; i < cf.mFace.length; i++) {
			for (int j = 0; j < cf.mFace[i].length; j++) {
				this.changedTiles.add(cf.mFace[i][j]);
			}
		}
	}

	/**
	 * Rotates a face 90 degrees
	 * 
	 * @param cf
	 */
	private void rotateEvenFace(CubeFace cf) {
		Tile[][] face = cf.mFace;

		final int M = face.length;
		final int N = face[0].length;

		Tile[][] faceCopy = new Tile[N][M];
		// create copy
		for (int i = 0; i < face.length; i++) {
			Tile[] row = face[i];
			int length = row.length;
			faceCopy[i] = new Tile[length];
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
			// Single Slice Turn

			for (int i = 0; i <= ((width + 1) / 2) - 1; i++) {
				// R, 2R, 3R, etc are single slice turns according to SiGN
				PuzzleTurn R = new PuzzleTurn(this, (i + 1) + "R",
						new Method[] { c.getMethod("RTurn", int.class,
								int.class) }, new Object[] { new Object[] { i,
								i } }, -90f, 'X');

				moves.add(R);
				PuzzleTurn L = new PuzzleTurn(this, (i + 1) + "L'",
						new Method[] { c.getMethod("LPrimeTurn", int.class,
								int.class) }, new Object[] { new Object[] { i,
								i } }, -90f, 'X');
				moves.add(L);
			}
			for (int i = 0; i <= ((height + 1) / 2) - 1; i++) {
				PuzzleTurn U = new PuzzleTurn(this, (i + 1) + "U",
						new Method[] { c.getMethod("UTurn", int.class,
								int.class) }, new Object[] { new Object[] { i,
								i } }, -90f, 'Y');
				moves.add(U);
				PuzzleTurn D = new PuzzleTurn(this, (i + 1) + "D'",
						new Method[] { c.getMethod("DPrimeTurn", int.class,
								int.class) }, new Object[] { new Object[] { i,
								i } }, -90f, 'Y');
				moves.add(D);
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
		return moves;
	}

	public void RTurn(int startLayer, int endLayer) {
		for (int i = startLayer; i <= endLayer; i++) {
			this.sideRotation((depth - 1) - i);
		}
		if(startLayer == 0){
			this.rotateFace(this.right);
		}
		
		checkSolved();
	}

	public void LPrimeTurn(int startLayer, int endLayer) {
		for (int i = startLayer; i <= endLayer; i++) {
			this.sideRotation(i);
		}
		if(startLayer == 0){
			this.rotateFace(this.left);
			this.rotateFace(this.left);
			this.rotateFace(this.left);
		}
		
		checkSolved();
	}

	public void UTurn(int startLayer, int endLayer) {
		for (int i = startLayer; i <= endLayer; i++) {
			this.topRotation(i);
		}
		if(startLayer == 0){
			this.rotateFace(this.top);
		}
		
		checkSolved();
	}

	public void DPrimeTurn(int startLayer, int endLayer) {
		for (int i = startLayer; i <= endLayer; i++) {
			this.topRotation((height - 1) - i);
		}
		if(startLayer == 0){
			this.rotateFace(this.down);
			this.rotateFace(this.down);
			this.rotateFace(this.down);
		}
		
		checkSolved();
	}

	public void FTurn(int startLayer, int endLayer) {
		for (int i = startLayer; i <= endLayer; i++) {
			this.frontRotation((depth - 1) - i);
		}
		if(startLayer == 0){
			this.rotateFace(this.front);
		}
		
		checkSolved();
	}

	public void BPrimeTurn(int startLayer, int endLayer) {
		for (int i = startLayer; i <= endLayer; i++) {
			this.frontRotation(i);
		}
		if(startLayer == 0){
			this.rotateFace(this.back);
			this.rotateFace(this.back);
			this.rotateFace(this.back);
		}
		
		checkSolved();
	}

	// ---------------------------------------------
	// CUBE ROTATIONS
	// ---------------------------------------------
	public void yTurn() {
		for (int i = 0; i < height; i++) {
			topRotation(i);
		}
		this.rotateFace(this.top);
		this.rotateFace(this.down);
		this.rotateFace(this.down);
		this.rotateFace(this.down);
	}

	public void yPrimeTurn() {
		yTurn();
		yTurn();
		yTurn();
	}

	// rotates the cube on R
	public void xTurn() {
		for (int i = 0; i < height; i++) {
			sideRotation(i);
		}
		this.rotateFace(this.right);
		this.rotateFace(this.left);
		this.rotateFace(this.left);
		this.rotateFace(this.left);
	}

	// rotates the cube on F
	public void zTurn() {
		for (int i = 0; i < height; i++) {
			frontRotation(i);
		}
		this.rotateFace(this.front);
		this.rotateFace(this.back);
		this.rotateFace(this.back);
		this.rotateFace(this.back);
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

	public static int getLayout() {
		return myLayout;
	}

	@Override
	public ArrayList<Shape> createPuzzleModel() {
		ArrayList<Shape> myFaces = new ArrayList<Shape>();

		ArrayList<Square> topF = drawFace(top);
		Square.finalizeAll(topF);
		myFaces.addAll(topF);

		ArrayList<Square> frontF = drawFace(front);
		Square.rotateAll(frontF, 'X', (float) Math.PI / 2);
		Square.finalizeAll(frontF);
		myFaces.addAll(frontF);

		ArrayList<Square> backF = drawFace(back);
		Square.rotateAll(backF, 'X', (float) -Math.PI / 2);
		Square.finalizeAll(backF);
		myFaces.addAll(backF);

		ArrayList<Square> leftF = drawFace(left);
		Square.rotateAll(leftF, 'Z', (float) (Math.PI / 2));
		Square.finalizeAll(leftF);
		myFaces.addAll(leftF);

		ArrayList<Square> rightF = drawFace(right);
		Square.rotateAll(rightF, 'Z', (float) -(Math.PI / 2));
		Square.finalizeAll(rightF);
		myFaces.addAll(rightF);

		// bottom can't be rotated while preserving orientation
		// must translate directly down
		// this might not be true somehow
		// definetly not true somehow
		ArrayList<Square> bottomF = drawFace(down);
		// Square.translateAll(mBottom, 'Y', -1.40);
		Square.rotateAll(bottomF, 'X', (float) (Math.PI));
		Square.finalizeAll(bottomF);
		myFaces.addAll(bottomF);

		return myFaces;
	}

	private ArrayList<Square> drawFace(CubeFace side) {
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
				current.setmColour(Shape.colourToGLColour(side.mFace[0][0]));
				face.add(current);

				currentTopRightX += faceLength + spaceLength;
			}

			// once we finish a row, we need to move down to next row
			currentTopRightZ += faceLength + spaceLength;
		}

		// update hashmap with tiles to shapes
		for (int i = 0; i < side.mFace.length; i++) {
			for (int j = 0; j < side.mFace[i].length; j++) {
				int index = i * side.mFace[i].length + j;

				Tile t1 = side.mFace[i][j];
				t1.setmShape(face.get(index));

			}
		}

		return face;
	}

	@Override
	public ArrayList<Tile> getChangedTiles() {
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
}