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

	public abstract void finalizeShape();

	public void refresh() {
		float[] coords = getCoords();
		vertexBuffer.put(coords);
		vertexBuffer.position(0);
	}

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
	 * moves each corner 'amount' towards the center, effectively shrinking the object
	 * 
	 * @param amount
	 */
	public void shrink(float amount){
		GLVertex center = getCenter();
		
		for(int i = 0; i < this.verticies.size(); i++){
			GLVertex current = this.verticies.get(i);
			
			if (distanceBetween(current, center) > amount){
				//only shrink if it won't invert the object
				
				float x1 = current.getX();
				float y1 = current.getY();
				float x2 = center.getX();
				float y2 = center.getY();
				
				//slope = change(y) / change(x)
				float m = ( y2 - y1 ) / ( x2 - x1 );
				
				float alpha = (float) Math.atan2( (y2 - y1), (x2 - x1));
				
				float xchange = amount * (float) Math.cos(alpha);
				float ychange = amount * (float) Math.sin(alpha);
				
				float x = x1 + xchange;
				float y = y1 + ychange;
				
				current.setX( x );
				current.setY( y );
//				//u = d / sqrt(m2 + 1)
//				float u = amount / (float) Math.sqrt( Math.pow(m, 2) + 1);
//				//v = m*d/ sqrt(m2 + 1)
//				float v = ( m * amount ) / (float) Math.sqrt( Math.pow(m, 2) + 1);
//				
//				float x = 0f;
//				float y = 0f;
//				//either add or subtract
//				if( center.getX() > current.getX()){
//					x = u + x1;
//				}else{
//					x = x1 - u;
//				}
//				if( center.getY() > current.getY()){
//					y = v + y1;										
//				}else{
//					y = y1 - u;
//				}
//				
//				current.setX( x );
//				current.setY( y );
			}else{
				throw new IllegalArgumentException("point you are trying to shrink is too close to center");
			}
//			if( current.getX() - amount > center.getX() ){
//				current.setX( current.getX() - amount );
//			}else if(current.getX() + amount < center.getX() ){
//				current.setX( current.getX() + amount );
//			}else {
//				//already close to center
//			}
//			
//			if(current.getY() - amount > center.getY() ){
//				current.setY( current.getY() - amount);
//			}else if(current.getY() + amount < center.getY() ){
//				current.setY( current.getY() + amount);
//			}else{
//				//already in center
//			}
		}
	}
	
	public float distanceBetween(GLVertex one, GLVertex two){
		return (float)Math.sqrt( 
				Math.pow(two.getX() - one.getX(), 2) + 
				Math.pow(two.getY() - one.getY(), 2)
				);
	}
	
	public GLVertex getCenter(){
		GLVertex centriod = new GLVertex(0, 0, 0);
		float signedArea = 0.0f;
		float x0 = 0.0f; // Current vertex X
		float y0 = 0.0f; // Current vertex Y
		float x1 = 0.0f; // Next vertex X
		float y1 = 0.0f; // Next vertex Y
		float a = 0.0f; // Partial signed area

		// For all vertices except last
		int i = 0;
		for (i = 0; i < this.verticies.size() - 1; ++i) {
			x0 = verticies.get(i).getX();
			y0 = verticies.get(i).getY();
			x1 = verticies.get(i + 1).getX();
			y1 = verticies.get(i + 1).getY();
			a = x0 * y1 - x1 * y0;
			signedArea += a;
			centriod.setX( centriod.getX() + (x0 + x1) * a);
//			centroid += (x0 + x1) * a;
			centriod.setY( centriod.getY() + (y0 + y1) * a);
//			centroid.y += (y0 + y1) * a;
		}

		    // Do last vertex
		    x0 = verticies.get(i).getX();
		    y0 = verticies.get(i).getY();
		    x1 = verticies.get(0).getX();
		    y1 = verticies.get(0).getY();
		    a = x0*y1 - x1*y0;
		    signedArea += a;
		    centriod.setX( centriod.getX() + (x0 + x1) * a);
//			centroid += (x0 + x1) * a;
			centriod.setY( centriod.getY() + (y0 + y1) * a);
//			centroid.y += (y0 + y1) * a;

		    signedArea *= 0.5;
		    //centroid.x /= (6.0*signedArea);
		    centriod.setX( (float) (centriod.getX() / (6.0*signedArea)) );
//		    centroid.y /= (6.0*signedArea);
		    centriod.setY( (float) (centriod.getY() / (6.0*signedArea)) );

		    return centriod;
	}
	
	public void translateAway(GLVertex center, float distance){
		GLVertex myCenter = this.getCenter();
		for(GLVertex current: this.verticies){
			float x1 = current.getX();
			float y1 = current.getY();
			float x2 = center.getX();
			float y2 = center.getY();
			
			//translate away from center
			float alpha = (float) Math.atan2( (myCenter.getY() - y2), (myCenter.getX() - x2));
			
			float xchange = distance * (float) Math.cos(alpha);
			float ychange = distance * (float) Math.sin(alpha);
			
			float x = x1 + xchange;
			float y = y1 + ychange;
			
			current.setX( x );
			current.setY( y );
		}
		
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

	public static void translateAll(ArrayList<Shape> face, char axis, double distance){
		for(int i = 0; i < face.size(); i++){
			Shape s = face.get(i);
			s.translate( axis, distance );
		}
	}


	public static void rotateAll(ArrayList<Shape> face, char axis, float radians){
		for(int i = 0; i < face.size(); i++){
			Shape s = face.get(i);
			s.rotate(axis, radians);
		}
	}

	public static void finalizeAll(ArrayList<Shape> face){
		for(int i = 0; i < face.size(); i++){
			Shape s = face.get(i);
			s.finalizeShape();
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

	@Override
	public boolean equals(Object o) {
		if(this.verticies.equals( ((Shape)o).verticies)){
			return true;
		}
		return false;
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