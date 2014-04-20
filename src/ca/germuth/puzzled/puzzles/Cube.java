package ca.germuth.puzzled.puzzles;

import java.util.ArrayList;
import java.util.HashMap;
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
		assert(width > 0 && height > 0 && depth > 0);
		
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
	
	public class CubeFace implements PuzzleFace{
		private Colour[][] mFace;
		private Colour mSolvedColour;
		
		public CubeFace(int rows, int columns, Colour colour){
			mFace = new Colour[rows][columns];
			mSolvedColour = colour;
			
			setSolved();
		}
		
		public boolean isSolved(){
			for(int i = 0; i < mFace.length; i++){
				for(int j = 0; j < mFace[i].length; j++){
					if( ! this.mFace[i][j].equals( this.mFace[0][0] )){
						return false;
					}
				}
			}
			return true;
		}
		
		public void setSolved(){
			for(int i = 0; i < mFace.length; i++){
				for(int j = 0; j < mFace[i].length; j++){
					mFace[i][j] = mSolvedColour;
				}
			}
		}
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

	@Override
	public ArrayList<PuzzleTurn> getPrimaryMoves() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<PuzzleTurn> getExtraMoves() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ArrayList<PuzzleTurn> getRotationMoves() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<ChangedTile> getChangedTiles() {
		// TODO Auto-generated method stub
		return null;
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
	
	//---------------------------------------------
	//CUBE SINGLE TURNS
	//---------------------------------------------
	public void RTurn(){
		sideRotation(width - 1);
		rotateFace(this.right);
	}

	public void RPrimeTurn(){
		this.RTurn();
		this.RTurn();
		this.RTurn();
	}

	public void LPrimeTurn(){
		sideRotation(0);
		rotateFace(this.left);
		rotateFace(this.left);
		rotateFace(this.left);
	}

	public void LTurn(){
		this.LPrimeTurn();
		this.LPrimeTurn();
		this.LPrimeTurn();
	}
	public void UTurn(){
		topRotation(0);
		rotateFace(this.top);
	}

	public void UPrimeTurn(){
		this.UTurn();
		this.UTurn();
		this.UTurn();
	}

	public void DPrimeTurn(){
		topRotation(height - 1);
		rotateFace(this.down);
		rotateFace(this.down);
		rotateFace(this.down);
	}

	public void DTurn(){
		this.DPrimeTurn();
		this.DPrimeTurn();
		this.DPrimeTurn();
	}
	
	public void FTurn(){
		frontRotation(depth - 1);
		rotateFace(this.front);
	}

	public void FPrimeTurn(){
		this.FTurn();
		this.FTurn();
		this.FTurn();
	}

	public void BPrimeTurn(){
		frontRotation(0);
		rotateFace(this.back);
		rotateFace(this.back);
		rotateFace(this.back);
	}

	public void BTurn(){
		BPrimeTurn();
		BPrimeTurn();
		BPrimeTurn();
	}

	//---------------------------------------------
	//DOUBLE TURNS
	//---------------------------------------------
	public void rTurn() {
		sideRotation( this.width - 1);
		sideRotation( this.width - 2);
		rotateFace( this.right );
	}
	public void rPrimeTurn() {
		rTurn();
		rTurn();
		rTurn();
	}

	public void uPrimeTurn() {
		uTurn();
		uTurn();
		uTurn();
	}
	public void uTurn() {
		rotateFace(this.top);
		topRotation(0);
		topRotation(1);
	}

	public void fTurn() {
		frontRotation(depth - 1);
		frontRotation(depth - 2);
		rotateFace(this.front);
	}

	public void fPrimeTurn() {
		fTurn();
		fTurn();
		fTurn();
	}

	public void lTurn() {
		lPrimeTurn();
		lPrimeTurn();
		lPrimeTurn();
	}
	public void lPrimeTurn() {
		sideRotation(0);
		sideRotation(1);
		rotateFace(this.left);
		rotateFace(this.left);
		rotateFace(this.left);
	}

	public void dTurn(){
		dPrimeTurn();
		dPrimeTurn();
		dPrimeTurn();
	}

	public void dPrimeTurn(){
		topRotation( height - 1 );
		topRotation( height - 2 );
		rotateFace(this.down);
		rotateFace(this.down);
		rotateFace(this.down);
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
}
