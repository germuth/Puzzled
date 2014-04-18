package ca.germuth.puzzled.puzzles;

import java.lang.reflect.Method;
import java.util.HashMap;

public class Cube implements Puzzle{

	private int width;
	private int height;
	private int depth;
	/**
	 * HashMap from each face of the cube to it's colour in
	 * the solved position
	 */
	private HashMap<Colour[][], Colour> myColours;
	
	private Colour[][] top;
	private Colour[][] front;
	private Colour[][] left;
	private Colour[][] right;
	private Colour[][] bottom;
	private Colour[][] back;
	
	public Cube(int width, int height, int depth){
		assert(width > 0 && height > 0 && depth > 0);
		
		this.width = width;
		this.height = height;
		this.depth = depth;
		
		//number of row then column
		this.top = new Colour[depth][width];
		this.front = new Colour[height][width];
		this.left = new Colour[depth][height];
		this.right = new Colour[depth][height];
		this.bottom = new Colour[depth][width];
		this.back = new Colour[height][width];
		
		//save colour for each face in its solved state
		//TODO grab saved values here rather than always use defaults
		this.myColours = new HashMap<Colour[][], Colour>();
		this.myColours.put(top, new Colour(255, 255, 255, 0));
		this.myColours.put(front, new Colour(0, 255, 0, 0));
		this.myColours.put(left, new Colour(255, 128, 0, 0));
		this.myColours.put(right, new Colour(255, 0, 0, 0));
		this.myColours.put(bottom, new Colour(255, 255, 0, 0));
		this.myColours.put(back, new Colour(0, 0, 255, 0));
	}
	
	@Override
	public boolean isSolved() {
		//check if every colour in top is the same
		//check bottom at the same time
		for(int i = 0; i < depth; i++){
			for(int j = 0; j < width; j++){
				if( ! this.top[i][j].equals( this.top[0][0] ) ||
						! this.bottom[i][j].equals( this.bottom[0][0] ) ){
					return false;
				}
			}
		}
		//check left and right
		for(int i = 0; i < depth; i++){
			for(int j = 0; j < height; j++){
				if( ! this.left[i][j].equals( this.left[0][0] ) ||
						! this.right[i][j].equals( this.right[0][0] ) ){
					return false;
				}
			}
		}
		//check front and back
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				if( ! this.front[i][j].equals( this.front[0][0] ) ||
						! this.back[i][j].equals( this.back[0][0] ) ){
					return false;
				}
			}
		}
		
		return true;
	}

	@Override
	public void setSolved() {
		//set top and bottom
		for(int i = 0; i < depth; i++){
			for(int j = 0; j < width; j++){
				this.top[i][j] = myColours.get(top);
				this.bottom[i][j] = myColours.get(bottom);
			}
		}
		//set left and right
		for(int i = 0; i < depth; i++){
			for(int j = 0; j < height; j++){
				this.left[i][j] = myColours.get(left);
				this.right[i][j] = myColours.get(right);
			}
		}
		//set front and back
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				this.front[i][j] = myColours.get(front);
				this.back[i][j] = myColours.get(back);
			}
		}
	}

	@Override
	public String scramble(int scramble_length) {
		return "";
	}

	@Override
	public String[] getPrimaryMoves() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getExtraMoves() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Colour[] getColours() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Colour[] setColours() {
		// TODO Auto-generated method stub
		return null;
	}
}
