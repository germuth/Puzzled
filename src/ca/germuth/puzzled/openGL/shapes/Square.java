package ca.germuth.puzzled.openGL.shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import ca.germuth.puzzled.openGL.GLColour;
import ca.germuth.puzzled.openGL.GLVertex;
import ca.germuth.puzzled.openGL.MyRenderer;
import android.opengl.GLES20;

public class Square extends Shape{

		private final short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw
																// vertices
		/**
		 * Top Left, Bottom Left, Bottom Right, Top Right
		 * 
		 * Positive z is far away
		 * @param one
		 * @param two
		 * @param three
		 * @param four
		 */
		public Square(GLVertex one, GLVertex two, GLVertex three, GLVertex four) {

			this.verticies = new ArrayList<GLVertex>();
			this.verticies.add(one);
			this.verticies.add(two);
			this.verticies.add(three);
			this.verticies.add(four);

		}

		public void finalize() {
			
			float[] squareCoords = getCoords();

			// initialize vertex byte buffer for shape coordinates
			ByteBuffer bb = ByteBuffer.allocateDirect(
			// (# of coordinate values * 4 bytes per float)
			// verticies * coordinates * bytes per float
					this.verticies.size() * 3 * 4);
			bb.order(ByteOrder.nativeOrder());
			vertexBuffer = bb.asFloatBuffer();
			vertexBuffer.put(squareCoords);
			vertexBuffer.position(0);

			// initialize byte buffer for the draw list
			ByteBuffer dlb = ByteBuffer.allocateDirect(
			// (# of coordinate values * 2 bytes per short)
					drawOrder.length * 2);
			dlb.order(ByteOrder.nativeOrder());
			drawListBuffer = dlb.asShortBuffer();
			drawListBuffer.put(drawOrder);
			drawListBuffer.position(0);

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
			float color[] = new float[3];
			color[0] = this.mColour.getRed();
			color[1] = this.mColour.getGreen();
			color[2] = this.mColour.getBlue();
			GLES20.glUniform4fv(mColorHandle, 1, color, 0);
			//GLES20.glUniform3fv(mColorHandle, 1, color, 0);

			// get handle to shape's transformation matrix
			mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
			MyRenderer.checkGlError("glGetUniformLocation");

			// Apply the projection and view transformation
			GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
			MyRenderer.checkGlError("glUniformMatrix4fv");

			// Draw the square
			GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
					GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

			// Disable vertex array
			GLES20.glDisableVertexAttribArray(mPositionHandle);
		}
}
