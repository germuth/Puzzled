package ca.germuth.puzzled.openGL;

import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;




import ca.germuth.puzzled.openGL.shapes.Shape;
import ca.germuth.puzzled.puzzle.Puzzle;
import android.opengl.*;
import android.os.SystemClock;
import android.util.Log;

/**
 * Vertex Shader - OpenGL ES graphics code for rendering the vertices of a
 * shape. Fragment Shader - OpenGL ES code for rendering the face of a shape
 * with colors or textures. Program - An OpenGL ES object that contains the
 * shaders you want to use for drawing one or more shapes.
 * 
 * @author Administrator
 * 
 */
public class MyRenderer implements GLSurfaceView.Renderer {

	private static final String TAG = "MyGLRenderer";
	
	private static final boolean spin = false;
	private static final boolean fortyFive = true;

	private final float[] mMVPMatrix = new float[16];
	private final float[] mProjMatrix = new float[16];
	private final float[] mVMatrix = new float[16];
	private final float[] mRotationMatrix = new float[16];

	private ArrayList<Shape> myFaces;

	// Declare as volatile because we are updating it from another thread
	public volatile float mAngle;
	public volatile float mXAngle;
	public volatile float mYAngle;

	public Puzzle mPuzzle;

	public MyRenderer(Puzzle p){
		this.mPuzzle = p;
	}

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

	    this.myFaces = mPuzzle.drawPuzzle();
	}
	
	/**
	 * Takes bottom left corner of bottom left face
	 * @param bl
	 */
	private void drawFace(ArrayList<Square> face, Colour[][] side){

		int k = 0;
		for(int i = 0; i < side.length; i++){
			for(int j = 0; j < side[i].length; j++){
				Square current = face.get(k);
				k++;
				current.setmColour( colourToGLColour( side[i][j] ) );
			}
		}
	}

	@Override
	public void onDrawFrame(GL10 unused) {

		// GLES20.glClearDepthf(1.0f);
		// Draw background color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		drawFace( this.mTop, this.mCube.getTop() );
		drawFace( this.mFront, this.mCube.getFront() );
		drawFace( this.mBottom, this.mCube.getBottom() );
		drawFace( this.mBack, this.mCube.getBack() );
		drawFace( this.mLeft, this.mCube.getLeft() );
		drawFace( this.mRight, this.mCube.getRight() );

		// Calculate the projection and view transformation
		Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);

		if (fortyFive) {
			float angleInDegrees = 45f;

			// Draw the triangle facing straight on.
			Matrix.setIdentityM(mRotationMatrix, 0);
			Matrix.setRotateM(mRotationMatrix, 0, angleInDegrees, -1.0f, 0.0f,
					.0f);

			// Combine the rotation matrix with the projection and camera view
			Matrix.multiplyMM(mMVPMatrix, 0, mRotationMatrix, 0, mMVPMatrix, 0);

		}
		// Create a rotation for the triangle
		// long time = SystemClock.uptimeMillis() % 4000L;
		// float angle = 0.090f * ((int) time);
		// Matrix.setRotateM(mRotationMatrix, 0, mAngle, mYAngle, 0, mXAngle);

		if (spin) {
			// Do a complete rotation every 10 seconds.
			long time = SystemClock.uptimeMillis() % 30000L;
			float angleInDegrees = (360.0f / 10000.0f) * ((int) time);

			// Draw the triangle facing straight on.
			Matrix.setIdentityM(mRotationMatrix, 0);
			Matrix.setRotateM(mRotationMatrix, 0, angleInDegrees, 1.0f, 0.0f,
					0.0f);
			// Combine the rotation matrix with the projection and camera view
			Matrix.multiplyMM(mMVPMatrix, 0, mRotationMatrix, 0, mMVPMatrix, 0);
		}


		for(int i = 0; i < this.myFaces.size(); i++){
			this.myFaces.get(i).draw(mMVPMatrix);
		}
	}

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

	    //Matrix.frustumM(mProjMatrix, 0, -0.6f, 0.6f, -0.7f, 0.7f, near, far);
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
