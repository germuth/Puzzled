package ca.germuth.puzzled.puzzle.minx;

import java.util.ArrayList;

import ca.germuth.puzzled.openGL.GLVertex;
import ca.germuth.puzzled.openGL.shapes.Pentagon;
import ca.germuth.puzzled.openGL.shapes.Shape;
import ca.germuth.puzzled.openGL.shapes.Square;
import ca.germuth.puzzled.puzzle.Puzzle;
import ca.germuth.puzzled.puzzle.PuzzleTurn;
import ca.germuth.puzzled.puzzle.Tile;

public class Minx implements Puzzle {

	// private static final int myLayout = R.layout.puzzle_cube;

	public static int getLayout() {
		// return myLayout;
		return 0;
	}

	// TODO add megaminx layout
	// private static final int myLayout = R.layout.puzzle_cube;

	/**
	 * 1 - normal megaminx 2 - gigaminx 3 - teraminx 4 - petaminb etc. etc.
	 */
	private int size;

	private MinxFace U;
	private MinxFace L;
	private MinxFace R;
	private MinxFace F;
	private MinxFace DL;
	private MinxFace DR;
	private MinxFace UR;
	private MinxFace UL;
	private MinxFace D;
	private MinxFace BL;
	private MinxFace BR;
	private MinxFace B;

	public static final String[] constructor_param_titles = { "Size" };

	public Minx(int size) {
		this.size = size;
	}

	@Override
	public boolean isSolved() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setSolved() {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<PuzzleTurn> getAllMoves() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void moveFinished() {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<Shape> createPuzzleModel() {
		/*
		 * size of center must get smaller every time (probably) the real cubes
		 * don't actually do this - 1/2 -> 3-8 -> 5/16 etc - can get points of
		 * outer pentagon using center - get get inner pentagon (center) using
		 * fraction - then can get two corners of each square at each corner of
		 * pentagon and create nxn pieces there - get other corners with
		 * extrapolation of inner pentagon line? - then generate edge pieces
		 * inbetween each nxn square
		 */
		// GLVertex l = new GLVertex(-CENTER_OUTSIDE_DISTANCE, 0, 0);
		// GLVertex bl = new GLVertex(-CENTER_OUTSIDE_DISTANCE, 0, 0);
		// //equivalent to 108 degrees
		// //angle of each corner in pentagon
		// bl.rotate('Z', 1.88495559f);
		// GLVertex br = new GLVertex(-CENTER_OUTSIDE_DISTANCE, 0, 0);
		// br.rotate('Z', 1.88495559f);
		// br.rotate('Z', 1.88495559f);
		// GLVertex r = new GLVertex(CENTER_OUTSIDE_DISTANCE, 0, 0);
		// GLVertex t = new GLVertex(0, CENTER_OUTSIDE_DISTANCE, 0);
		ArrayList<Shape> myFaces = new ArrayList<Shape>();
		ArrayList<Shape> top = drawFace();

		myFaces.addAll(top);

		// ArrayList<Square> topF = drawFace(top, depth, width, height);
		// Square.finalizeAll(topF);
		// myFaces.addAll(topF);
		//
		// ArrayList<Square> frontF = drawFace(front, height, width, depth);
		// Square.rotateAll(frontF, 'X', (float) Math.PI / 2);
		// Square.finalizeAll(frontF);
		// myFaces.addAll(frontF);
		//
		// ArrayList<Square> backF = drawFace(back, height, width, depth);
		// Square.rotateAll(backF, 'X', (float) -Math.PI / 2);
		// Square.finalizeAll(backF);
		// myFaces.addAll(backF);
		//
		// ArrayList<Square> leftF = drawFace(left, depth, height, width);
		// Square.rotateAll(leftF, 'Z', (float) (Math.PI / 2));
		// Square.finalizeAll(leftF);
		// myFaces.addAll(leftF);
		//
		// ArrayList<Square> rightF = drawFace(right, depth, height, width);
		// Square.rotateAll(rightF, 'Z', (float) -(Math.PI / 2));
		// Square.finalizeAll(rightF);
		// myFaces.addAll(rightF);
		//
		// // bottom can't be rotated while preserving orientation
		// // must translate directly down
		// // this might not be true somehow
		// // definetly not true somehow
		// ArrayList<Square> bottomF = drawFace(down, depth, width, height);
		// // Square.translateAll(mBottom, 'Y', -1.40);
		// Square.rotateAll(bottomF, 'X', (float) (Math.PI));
		// Square.finalizeAll(bottomF);
		// myFaces.addAll(bottomF);

		return myFaces;
	}

	private ArrayList<Shape> drawFace() {
		//OKAY need to do this new way
		//we get outside and inside pentagon the same way
		//then we manually grab two of these points each to create 
		//line segments, and calculate the intersection of the line segments
		/*
		 * 
		 * public static Point lineIntersect(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
  double denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
  if (denom == 0.0) { // Lines are parallel.
     return null;
  }
  double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3))/denom;
  double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3))/denom;
    if (ua >= 0.0f && ua <= 1.0f && ub >= 0.0f && ub <= 1.0f) {
        // Get the intersection point.
        return new Point((int) (x1 + ua*(x2 - x1)), (int) (y1 + ua*(y2 - y1)));
    }

  return null;
  }
		 */
		//this gives us the four corner bounds to every section piece
		//might be difficult with adding small empty space between pieces
		//because it will cause the measurements to be off slightly
		//perhaps i could make a method that moves each point 0.5 closer to the center of the piece
		//then just call that method on everything
		//actually that sounds great
		final float CENTER_OUTSIDE_DISTANCE = 2.0f;
		final float FRACTION = 0.50f;
		final float ANGLE_BETWEEN = 1.25663706f; // = 72 degrees

		ArrayList<Shape> face = new ArrayList<Shape>();

		// outer corners
		GLVertex t = new GLVertex(0, CENTER_OUTSIDE_DISTANCE, 0);
		GLVertex l = new GLVertex(0, CENTER_OUTSIDE_DISTANCE, 0);
		l.rotate('Z', ANGLE_BETWEEN);
		GLVertex bl = new GLVertex(0, CENTER_OUTSIDE_DISTANCE, 0);
		// rotate for each corner in pentagon
		bl.rotate('Z', ANGLE_BETWEEN * 2);
		GLVertex br = new GLVertex(0, CENTER_OUTSIDE_DISTANCE, 0);
		br.rotate('Z', ANGLE_BETWEEN * 3);
		GLVertex r = new GLVertex(0, CENTER_OUTSIDE_DISTANCE, 0);
		r.rotate('Z', -ANGLE_BETWEEN);

		// inner corners
		GLVertex ti = new GLVertex(0, CENTER_OUTSIDE_DISTANCE * FRACTION, 0);
		GLVertex li = new GLVertex(0, CENTER_OUTSIDE_DISTANCE * FRACTION, 0);
		li.rotate('Z', ANGLE_BETWEEN);
		GLVertex bli = new GLVertex(0, CENTER_OUTSIDE_DISTANCE * FRACTION, 0);
		// rotate for each corner in pentagon
		bli.rotate('Z', ANGLE_BETWEEN * 2);
		GLVertex bri = new GLVertex(0, CENTER_OUTSIDE_DISTANCE * FRACTION, 0);
		bri.rotate('Z', ANGLE_BETWEEN * 3);
		GLVertex ri = new GLVertex(0, CENTER_OUTSIDE_DISTANCE * FRACTION, 0);
		ri.rotate('Z', -ANGLE_BETWEEN);

		Pentagon p = new Pentagon(li, bli, bri, ri, ti);

		p.setmColour(Shape.colourToGLColour(new Tile(255, 255, 255, 0)));
		p.finalizeShape();
		face.add(p);

		// corners of inner and outer pentagon make opposite vertices of square
		// from these two vertices we can figure out other two vertices of
		// square
		Square topSq = calculateSquare(t, ti);
		Square leftSq = calculateSquare(l, li);
		Square blSq = calculateSquare(bl, bli);
		Square brSq = calculateSquare(br, bri);
		Square rSq = calculateSquare(r, ri);
		
		topSq.setmColour(Shape.colourToGLColour(new Tile(255, 255, 255, 0)));
		topSq.finalizeShape();
		leftSq.setmColour(Shape.colourToGLColour(new Tile(255, 255, 255, 0)));
		leftSq.finalizeShape();
		blSq.setmColour(Shape.colourToGLColour(new Tile(255, 255, 255, 0)));
		blSq.finalizeShape();
		brSq.setmColour(Shape.colourToGLColour(new Tile(255, 255, 255, 0)));
		brSq.finalizeShape();
		rSq.setmColour(Shape.colourToGLColour(new Tile(255, 255, 255, 0)));
		rSq.finalizeShape();
		
		face.add(topSq);
		face.add(leftSq);
		face.add(blSq);
		face.add(brSq);
		face.add(rSq);
		
		return face;
	}
	
	/**
	 * Takes two diagonal corners of a square and calculates the other two corners.
	 * Ensures the square is given in counter-clockwise order
	 * http://math.stackexchange.com/questions/506785/given-two-diagonally-opposite-points-on-a-square-how-to-calculate-the-other-two
	 * @param corner1
	 * @param corner2
	 * @return
	 */
	private Square calculateSquare(GLVertex corner1, GLVertex corner2){
		GLVertex corner3 = getThirdCorner(corner1.getX(), corner1.getY(),
				corner2.getX(), corner2.getY());
		GLVertex corner4 = getFourthCorner(corner1.getX(), corner1.getY(),
				corner2.getX(), corner2.getY());

		Square topSquare = null;
		// square needs to be counter clockwise
		// corner 1 is top
		if (corner1.getY() > corner2.getY()) {
			// find which corner(3 or 4) is more left
			if (corner3.getX() < corner4.getX()) {
				// corner 3 is more left
				topSquare = new Square(corner1, corner3, corner2, corner4);
			} else if (corner3.getX() > corner4.getX()) {
				topSquare = new Square(corner1, corner4, corner2, corner3);
			}
		}// corner 2 is top
		else if (corner1.getY() < corner2.getX()) {
			// then do the opposite
			if (corner3.getX() > corner4.getX()) {
				topSquare = new Square(corner1, corner3, corner2, corner4);
			} else if (corner3.getX() < corner4.getX()) {
				topSquare = new Square(corner1, corner4, corner2, corner3);
			}
		}// square is sideways
		else {
			// find if square 1 is left or right
			if (corner1.getX() < corner2.getX()) {
				// outer edge is left
				// must find top edge
				if (corner3.getY() > corner4.getY()) {
					// corner 3 is next
					topSquare = new Square(corner1, corner3, corner2, corner4);
				} else {
					topSquare = new Square(corner1, corner4, corner2, corner3);
				}
			}
		}
		return topSquare;
	}

	private GLVertex getThirdCorner(float x1, float y1, float x2, float y2) {
		float xc = (x1 + x2) / 2;
		float yc = (y1 + y2) / 2; // Center point
		float xd = (x1 - x2) / 2;
		float yd = (y1 - y2) / 2; // Half-diagonal

		float x3 = xc - yd;
		float y3 = yc + xd; // Third corner
		return new GLVertex(x3, y3, 0);
	}

	private GLVertex getFourthCorner(float x1, float y1, float x2, float y2) {
		float xc = (x1 + x2) / 2;
		float yc = (y1 + y2) / 2; // Center point
		float xd = (x1 - x2) / 2;
		float yd = (y1 - y2) / 2; // Half-diagonal

		float x4 = xc + yd;
		float y4 = yc - xd; // Fourth corner
		return new GLVertex(x4, y4, 0);
	}

	@Override
	public ArrayList<Tile> getChangedTiles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOnPuzzleSolvedListener(OnPuzzleSolvedListener list) {
		// TODO Auto-generated method stub

	}

	@Override
	public OnPuzzleSolvedListener getOnPuzzleSolvedListener() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void checkSolved() {
		// TODO Auto-generated method stub

	}
}
