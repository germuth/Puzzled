package ca.germuth.puzzled.openGL;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;
import ca.germuth.puzzled.openGL.shapes.Shape;
import ca.germuth.puzzled.puzzle.Puzzle;
import ca.germuth.puzzled.puzzle.PuzzleTurn;
import ca.germuth.puzzled.puzzle.Tile;

/**O
 * Renderer
 * 
 * Physically uses openGL to draw colored triangles on the screen.
 * Triangles are specified by an array of floating point numbers for
 * x, y, and z coordinates. Coordinates are stored in ByteBuffer
 * for maximum efficiency
 * 
 * Vertex Shader - OpenGL ES graphics code for rendering the vertices of a
 * shape. 
 * Fragment Shader - OpenGL ES code for rendering the face of a shape
 * with colors or textures. 
 * Program - An OpenGL ES object that contains the
 * shaders you want to use for drawing one or more shapes.
 * 
 * @author Administrator
 * 
 */
public class MyRenderer implements GLSurfaceView.Renderer {

	private static final String TAG = "MyGLRenderer";
	//TODO make configureable
	private static final int TURN_ANIMATION_TIME = 2000;
	
	private static final boolean spin = false;
	private static final boolean fortyFive = true;

	/**
	 * Matrix which combines the effects of the Projection
	 * and View matrix. 
	 * MVP = Model View Projection matrix i think
	 */
	private final float[] mMVPMatrix = new float[16];
	/**
	 * Projection matrix is used to adjust the coordinate of drawn objects
	 * based on the width and height of the GLSUrfaceView.
	 * This means drawn objects have the same relative size
	 * independant of teh GLSurfaceView's size
	 * Only established/changed in onSurfaceChanged()
	 */
	private final float[] mProjMatrix = new float[16];
	/**
	 * View Matrix is used to simulate a camera. It Does
	 * this by adjusting coordinate of drawn objects to
	 * perspective of a camera
	 */
	private final float[] mVMatrix = new float[16];
	/**
	 * Another transformation matrix to rotate objects. Can 
	 * be combined with the projection and view matrix
	 */
	private final float[] mRotationMatrix = new float[16];

	private PuzzleTurn currentTurn;
	private ArrayList<Shape> allFaces;
	private ArrayList<Shape> rotatingFaces;
	
	private float currentAngle;
	private long finalTime;

	public Puzzle mPuzzle;
	/**
	 * Queue of puzzle turns that need to be rendered by renderer. 
	 * This queue is volatile since two threads access/update it
	 * Each puzzle turn consits of an array of individual tiles that
	 * need to be animated turning. 
	 */
	public volatile Queue<PuzzleTurn> pendingMoves;
	private boolean animating;

	public MyRenderer(Puzzle p){
		this.mPuzzle = p;
		this.pendingMoves = new LinkedList<PuzzleTurn>();
		this.animating = false;
		
		this.rotatingFaces = new ArrayList<Shape>();
	}
	
	/**
	 * Called once when rendered is first created.
	 * Should initialize all shapse here like:
	 * Triangle t = new Triangle()
	 * Square s = new Square
	 */
	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {

		GLES20.glEnable( GLES20.GL_DEPTH_TEST );
		GLES20.glDepthFunc( GLES20.GL_LEQUAL );
		GLES20.glDepthMask( true );

		// Set the background frame color
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		// Position the eye behind the origin.
	    final float eyeX = 0.0f;
	    final float eyeY = 0.0f;
	    final float eyeZ = 2.9f;
	    // WAS 0, 0, 2.5

	    // We are looking toward the distance
	    final float lookX = 0.0f;
	    final float lookY = 0.0f;
	    final float lookZ = 0.0f;

	    // Set our up vector. This is where our head would be pointing were we holding the camera.
	    final float upX = 0.0f;
	    final float upY = 1.0f;
	    final float upZ = 0.0f;

	    // Set the view matrix. This matrix can be said to represent the camera position.
	    Matrix.setLookAtM(mVMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

	    this.allFaces = mPuzzle.createPuzzleModel();
	}

	/**
	 * Called for each redraw of a frame
	 */
	@Override
	public void onDrawFrame(GL10 unused) {

		
		// GLES20.glClearDepthf(1.0f);
		// Draw background color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		// Combine the projection matrix (objects look good on any screen)
		// and the View Matrix (simulate a camera) into the MVP Matrix
		// Calculate the projection and view transformation
		Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);

		//Rotate entire cube 45 degrees downwards to get better perspective
		float angleInDegrees = 45f;

		// clear rotation matrix from whatever is was before to identity matrix
		Matrix.setIdentityM(mRotationMatrix, 0);
		// set rotation matrix to specified rotation
		Matrix.setRotateM(mRotationMatrix, 0, angleInDegrees, -1.0f, 0.0f, .0f);

		// Combine the rotation matrix with the projection and camera view
		Matrix.multiplyMM(mMVPMatrix, 0, mRotationMatrix, 0, mMVPMatrix, 0);

		if( ! this.animating ){
			if( ! this.pendingMoves.isEmpty() ){
				
				this.animating = true;
				this.currentAngle = 0f;
				this.currentTurn = pendingMoves.poll();
				
				//get tiles from the positions in the array that change for each given turn
				ArrayList<Tile> changed = currentTurn.getmChangedTiles();
				
				//covert each tile into the shape for it
				//split myFaces into ones we are rotating and ones we are not
				for(int i = 0; i < changed.size(); i++){
					//Shape sh = mPuzzle.getShapeFor( changed.get(i) );
					Shape sh = changed.get(i).getmShape();
					this.rotatingFaces.add(sh);
				}
				
				this.finalTime = SystemClock.uptimeMillis() + MyRenderer.TURN_ANIMATION_TIME;
			}
		}
		
		//TODO this solution is terrible and slow
		//Draw all faces that aren't rotating
		for(int i = 0; i < this.allFaces.size(); i++){
			Shape curr = this.allFaces.get(i);
			if( ! this.rotatingFaces.contains(curr) ){
				this.allFaces.get(i).draw(mMVPMatrix);
			}
		}
		
		if(animating){
			//get current time and compare to final time
			//from that find angle
			//if current time == final or current time > final time
			//display at the end and animating = false
			long currentTime = SystemClock.uptimeMillis();
			float angleToRotate = 0f;
			
			if( currentTime >= finalTime){
				animating = false;

				for(int i = 0; i < this.rotatingFaces.size(); i++){
					Shape current = this.rotatingFaces.get(i);
					//TODO why is this necessary, I think you should abandon openGL rotation
					//and just use our vertex rotations
					float rotAngle = this.currentTurn.getmAngle();
					if( rotAngle >= 0){
						current.rotate(this.currentTurn.getAxis(), 
								(float)-Math.toRadians( this.currentTurn.getmAngle()));
					}else{
						current.rotate(this.currentTurn.getAxis(), 
								(float)Math.toRadians( this.currentTurn.getmAngle()));
					}
					current.refresh();
					current.draw(mMVPMatrix);
				}
				this.rotatingFaces.clear();
				this.currentTurn.getmChangedTiles().clear();
			}else{
				float net_time = finalTime - currentTime;
				float fraction = net_time / (float)MyRenderer.TURN_ANIMATION_TIME;
				angleToRotate = currentTurn.getmAngle() * (1.0f - fraction);
				angleToRotate -= this.currentAngle;
				
				Matrix.setIdentityM(mRotationMatrix, 0);
				if(this.currentTurn.getAxis() == 'X'){
					Matrix.setRotateM(mRotationMatrix, 0, angleToRotate, 1.0f, 0.0f,
							0.0f);
				}
				else if(this.currentTurn.getAxis() == 'Y'){
					Matrix.setRotateM(mRotationMatrix, 0, angleToRotate, 0.0f, 1.0f,
							0.0f);
				}
				else if(this.currentTurn.getAxis() == 'Z'){
					Matrix.setRotateM(mRotationMatrix, 0, angleToRotate, 0.0f, 0.0f,
							1.0f);
				}
				
				// Combine the rotation matrix with the projection and camera view
				Matrix.multiplyMM(mMVPMatrix, 0, mRotationMatrix, 0, mMVPMatrix, 0);
				
				//draw all the rotating faces
				for(Shape s : this.rotatingFaces){
					s.rotate(this.currentTurn.getAxis(), (float)Math.toRadians(angleToRotate));
					s.draw(mMVPMatrix);
				}
			}
			
		}
	}

	/**
	 * Called if geometry of view changes, like turning device from
	 * portriat to landscape
	 */
	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		// Adjust the viewport based on geometry changes,
		// such as screen rotation
		GLES20.glViewport(0, 0, width, height);

		// Create a new perspective projection matrix. The height will stay the same
	    // while the width will vary as per aspect ratio.
	    final float ratio = (float) width / height;
	    final float left = -ratio;
	    final float right = ratio;
	    final float bottom = -1.0f;
	    final float top = 1.0f;
	    final float near = 0.5f;
	    final float far = 1.5f;

	    //populates projection Matrix with appropriate values based on view 
	    //size and desired frustrum
	    Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1f, 1f, 1.9f, 100.5f);
	}

	public static int loadShader(int type, String shaderCode) {

		// create a vertex shader type (GLES20.GL_VERTEX_SHADER)
		// or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
		int shader = GLES20.glCreateShader(type);

		// add the source code to the shader and compile it
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);

		return shader;
	}

	/**
	 * Utility method for debugging OpenGL calls. Provide the name of the call
	 * just after making it:
	 * 
	 * <pre>
	 * mColorHandle = GLES20.glGetUniformLocation(mProgram, &quot;vColor&quot;);
	 * MyGLRenderer.checkGlError(&quot;glGetUniformLocation&quot;);
	 * </pre>
	 * 
	 * If the operation is not successful, the check throws an error.
	 * 
	 * @param glOperation
	 *            - Name of the OpenGL call to check.
	 */
	public static void checkGlError(String glOperation) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e(TAG, glOperation + ": glError " + error);
			throw new RuntimeException(glOperation + ": glError " + error);
		}
	}
}
