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
	
	public Cube(int width, int height, int depth) {
		assert (width > 1 && height > 1 && depth > 1);

		this.width = width;
		this.height = height;
		this.depth = depth;
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

	@Override
	public ArrayList<PuzzleTurn> getAllMoves() {
		ArrayList<PuzzleTurn> moves = new ArrayList<PuzzleTurn>();
		try {
			Class<? extends Cube> c = this.getClass();
			// Single Slice Turn

			for (int i = 0; i <= ((width + 1) / 2) - 1; i++) {
				// R, 2R, 3R, etc are single slice turns according to SiGN
				PuzzleTurn R = new PuzzleTurn(this, (i + 1) + "R",
						90f, 'X');
				
				moves.add(R);
				PuzzleTurn L = new PuzzleTurn(this, (i + 1) + "L'",
						new Method[] { c.getMethod("LPrimeTurn", int.class, int.class) }, 
						new Object[] { new Object[] { i, i } },
						90f, 'X');
				moves.add(L);
			}
			for (int i = 0; i <= ((height + 1) / 2) - 1; i++) {
				PuzzleTurn U = new PuzzleTurn(this, (i + 1) + "U",
						new Method[] { c.getMethod("UTurn", int.class,
								int.class) }, new Object[] { new Object[] { i, i } },
						90f, 'Y');
				moves.add(U);
				PuzzleTurn D = new PuzzleTurn(this, (i + 1) + "D'",
						new Method[] { c.getMethod("DPrimeTurn", int.class,
								int.class) }, new Object[] { new Object[] { i, i } },
						90f, 'Y');
				moves.add(D);
			}
			for (int i = 0; i <= ((depth + 1) / 2) - 1; i++) {
				PuzzleTurn F = new PuzzleTurn(this, (i + 1) + "F",
						new Method[] { c.getMethod("FTurn", int.class,
								int.class) }, new Object[] { new Object[] { i, i } },
						90f, 'Z');
				moves.add(F);
				PuzzleTurn B = new PuzzleTurn(this, (i + 1) + "B'",
						new Method[] { c.getMethod("BPrimeTurn", int.class,
								int.class) }, new Object[] { new Object[] { i, i } },
						90f, 'Z');
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
					Object[] args1 = (Object[])args[0];
					PuzzleTurn bigTurn = new PuzzleTurn(this, newName,
							current.getMethods(), 
							new Object[] { new Object[] { 0, (Integer) (args1[0]) } },
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
				Object[] arg1 = (Object[])args[0];
				PuzzleTurn reverseTurn = new PuzzleTurn(this, name,
						// triple whatever turn was done before to turn the
						// other way
						PuzzleTurn.concatenate(current.getMethods(), 3),
						new Object[] { new Object[] { arg1[0], arg1[1] }, 
					new Object[] {arg1[0], arg1[1]}, new Object[]{arg1[0], arg1[1]} },
						-(current.getmAngle()), current.getAxis());
				moves.add(reverseTurn);
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return moves;
	}

//	@Override
	public ArrayList<PuzzleTurn> getAllRotationMoves() {
		ArrayList<PuzzleTurn> rotations = new ArrayList<PuzzleTurn>();
//		try {
//			Class<? extends Cube> c = this.getClass();
////			PuzzleTurn Y = new PuzzleTurn(
////					this,
////					"y",
////					new Method[] { c.getMethod("YTurn", int.class, int.class) },
////					null, 0);
////			rotations.add(Y);
////			PuzzleTurn X = new PuzzleTurn(
////					this,
////					"x",
////					new Method[] { c.getMethod("XTurn", int.class, int.class) },
////					null, 0);
////			rotations.add(X);
////			PuzzleTurn Z = new PuzzleTurn(
////					this,
////					"z",
////					new Method[] { c.getMethod("ZTurn", int.class, int.class) },
////					null, 0);
////			rotations.add(Z);
////
////			for (int i = 0; i < rotations.size(); i++) {
////				PuzzleTurn current = rotations.get(i);
////
////				PuzzleTurn reverse = new PuzzleTurn(this, current.getmName()
////						+ "'", PuzzleTurn.concatenate(current.getMethods(), 3),
////						null, 0);
////				rotations.add(reverse);
////			}
//		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
//		}

		return rotations;
	}

	public static int getLayout() {
		return myLayout;
	}

	@Override
	public ArrayList<Shape> createPuzzleModel() {
		ArrayList<Shape> myFaces = new ArrayList<Shape>();

		Square[][] topF = drawFace();
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

	private Square[][] drawFace(){
		Square[][] face = new Square[this.depth][this.depth];

		//TODO this will only work for square cubes!
		//int cubeSize = this.mCube.getSize();
		int cubeSize = width;

		float faceLength = 120f / cubeSize;
		faceLength /= 100;
		float spaceLength = 20f / (cubeSize + 1);
		spaceLength /= 100;
		final float height = 0.70f;

		float topCornerX = -0.70f;
		float topCornerZ = -0.70f;

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
				face[i][j] = current;

				currentTopRightX += faceLength + spaceLength;
			}

			// once we finish a row, we need to move down to next row
			currentTopRightZ += faceLength + spaceLength;
		}

		return face;
	}
}
