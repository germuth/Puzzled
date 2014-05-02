package ca.germuth.puzzled.openGL.shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import android.opengl.GLES20;
import ca.germuth.puzzled.openGL.GLVertex;
import ca.germuth.puzzled.openGL.MyRenderer;

public class Triangle extends Shape {

	//drawing order important because it defines
	//which face of triangle is the front face
	private final short drawOrder[] = { 0, 1, 2 };

	// Set color with red, green, blue and alpha (opacity) values
	float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

	public Triangle(GLVertex top, GLVertex bottomLeft, GLVertex bottomRight) {

		this.verticies = new ArrayList<GLVertex>();
		this.verticies.add(top);
		this.verticies.add(bottomLeft);
		this.verticies.add(bottomRight);
	}

	/**
	 * Compile ES Shaders and add to OpenGL ES Program object and link program.
	 * Should only be called once!
	 */
	public void finalizeShape() {
		float[] triangleCoords = getCoords();

		//verticies are passed to openGL in ByteBuffer because it 
		// is more efficenct
		//so grab all of the triangles verticies, and place in byteBuffer

		// initialize vertex byte buffer for shape coordinate
		//size is num verticies * num coordinates (x, y and z) * 4 bytes per float
		ByteBuffer bb = ByteBuffer.allocateDirect(
				this.verticies.size() * 3 * 4);
		// use the device hardware's native byte order
		bb.order(ByteOrder.nativeOrder());

		// create a floating point buffer from the ByteBuffer
		vertexBuffer = bb.asFloatBuffer();
		// add the coordinates to the FloatBuffer
		vertexBuffer.put(triangleCoords);
		// set the buffer to read the first coordinate
		vertexBuffer.position(0);

		// prepare shaders and OpenGL program
		int vertexShader = MyRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
				vertexShaderCode);
		int fragmentShader = MyRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
				fragmentShaderCode);

		mProgram = GLES20.glCreateProgram(); // create empty OpenGL Program
		GLES20.glAttachShader(mProgram, vertexShader); // add the vertex shader
														// to program
		GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment
															// shader to program
		GLES20.glLinkProgram(mProgram); // create OpenGL program executables
	}
	/**
	 * Draw the species shape. Takes in the Model View Projection matrix. 
	 * Shape defines list of coordinates, which are modified by above matrix
	 * to account for different screen sizes (projection) and a camera position (view)
	 */
	public void draw(float[] mvpMatrix) {
		// Add program to OpenGL environment
		GLES20.glUseProgram(mProgram);

		// get handle to vertex shader's vPosition member
		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

		// Enable a handle to the triangle vertices
		GLES20.glEnableVertexAttribArray(mPositionHandle);

		// Prepare the triangle coordinate data
		GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

		// get handle to fragment shader's vColor member
		mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

		// Set color for drawing the triangle
		GLES20.glUniform4fv(mColorHandle, 1, color, 0);

		// get handle to shape's transformation matrix
		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		MyRenderer.checkGlError("glGetUniformLocation");

		// Apply the projection and view transformation
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
		MyRenderer.checkGlError("glUniformMatrix4fv");

		// Draw the triangle
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, drawOrder.length);

		// Disable vertex array
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}

}