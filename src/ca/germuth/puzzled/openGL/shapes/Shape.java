package ca.germuth.puzzled.openGL.shapes;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import ca.germuth.puzzled.openGL.GLColour;
import ca.germuth.puzzled.openGL.GLVertex;
import ca.germuth.puzzled.puzzle.Tile;

public abstract class Shape {
	/**
	 * Used with MyRenederer.loadShader to compile and load this shader
	 */
	protected final String vertexShaderCode =
	// This matrix member variable provides a hook to manipulate
	// the coordinates of the objects that use this vertex shader
	"uniform mat4 uMVPMatrix;" +
	"attribute vec4 vPosition;" + 
	"void main() {" +
	// the matrix must be included as a modifier of gl_Position
			"  gl_Position = vPosition * uMVPMatrix;" +
	"}";

	/**
	 * Also used with loadShaper to compile/load
	 */
	protected final String fragmentShaderCode = 
			"precision mediump float;" + 
			"uniform vec4 vColor;" + 
			"void main() {"
			+ "  gl_FragColor = vColor;" + 
			"}";

	protected FloatBuffer vertexBuffer;
	protected ShortBuffer drawListBuffer;
	protected int mProgram;
	protected int mPositionHandle;
	protected int mColorHandle;
	protected int mMVPMatrixHandle;
	protected GLColour mColour;
	
	// number of coordinates per vertex in this array
	protected static final int COORDS_PER_VERTEX = 3;
	
	protected final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per
	// vertex

	/**
	 * The List of verticies for this shape
	 */
	protected ArrayList<GLVertex> verticies;
	
	public abstract void finalize();
	
	/**
	 * Draw the species shape. Takes in the Model View Projection matrix. 
	 * Shape defines list of coordinates, which are modified by above matrix
	 * to account for different screen sizes (projection) and a camera position (view)
	 * @param mvpMatrix
	 */
	public abstract void draw(float[] mvpMatrix);
	
	protected float[] getCoords(){
		float[] coords = new float[this.verticies.size() * 3];
		int last = 0;
		for(int i = 0; i < this.verticies.size(); i++){
			coords[last++] = this.verticies.get(i).getX();
			coords[last++] = this.verticies.get(i).getY();
			coords[last++] = this.verticies.get(i).getZ();
		}
		return coords;
	}
	/**
	 * Rotates this square around either the x y or z axis, by an amount in radians
	 * @param axis, the axis you are rotating around, may be 'X', 'Y', or 'Z'
	 * @param radians, the degree in radians you want to rotate around it
	 */
	public void rotate(char axis, float radians){
		for(int i = 0; i < this.verticies.size(); i++){
			GLVertex current = this.verticies.get(i);
			current.rotate(axis, radians);
		}	
	}
	
	public void translate(char axis, double distance){
		for(int i = 0; i < this.verticies.size(); i++){
			this.verticies.get(i).translate(axis, distance);
		}
	}

	public static void translateAll(ArrayList<Square> face, char axis, double distance){
		for(int i = 0; i < face.size(); i++){
			Square s = face.get(i);
			s.translate( axis, distance );
		}
	}


	public static void rotateAll(ArrayList<Square> face, char axis, float radians){
		for(int i = 0; i < face.size(); i++){
			Square s = face.get(i);
			s.rotate(axis, radians);
		}
	}

	public static void finalizeAll(ArrayList<Square> face){
		for(int i = 0; i < face.size(); i++){
			Square s = face.get(i);
			s.finalize();
		}
	}
	
	public static GLColour colourToGLColour( Tile c){
		float one = 1f;
		float half = 0.5f;

		float red = ((float)c.getRed()) / 255f;
		float blue = ((float)c.getGreen()) / 255f;
		float green = ((float)c.getBlue()) / 255f;
		return new GLColour( red, blue ,green);
	}


	/**
	 * @return the mColour
	 */
	public GLColour getmColour() {
		return mColour;
	}

	/**
	 * @param mColour the mColour to set
	 */
	public void setmColour(GLColour mColour) {
		this.mColour = mColour;
	}
}
