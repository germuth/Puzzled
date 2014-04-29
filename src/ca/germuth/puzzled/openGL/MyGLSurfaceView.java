package ca.germuth.puzzled.openGL;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import ca.germuth.puzzled.puzzle.Puzzle;
import ca.germuth.puzzled.puzzle.PuzzleTurn;
/**
 * Specialized view for drawing openGL ES graphics
 * @author Germuth
 *
 */
public class MyGLSurfaceView extends GLSurfaceView {

	private MyRenderer mRenderer;
	private Puzzle mPuzzle;

	public MyGLSurfaceView(Context context, AttributeSet attrs){
		super(context, attrs);

		// Create an OpenGL ES 2.0 context.
		
		//say we want to use openGL 2
		setEGLContextClientVersion(2);
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);

	}

	/**
	 * Must be called before view is created, pairs an openGL 
	 * Rendered to the view
	 * @param p
	 */
	public void initializeRenderer(Puzzle p) {
		this.mPuzzle = p;
		// Set the Renderer for drawing on the GLSurfaceView
		mRenderer = new MyRenderer(this.mPuzzle);
		setRenderer(mRenderer);

		// Render the view only when there is a change in the drawing data
		//setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		
		// Render as fast as GPU can
		setRenderMode( GLSurfaceView.RENDERMODE_CONTINUOUSLY);

	}
	
	public void addPuzzleTurn(PuzzleTurn turn){
		mRenderer.pendingMoves.add(turn);
	}
	
	/**
	 * @return the mRenderer
	 */
	public MyRenderer getmRenderer() {
		return mRenderer;
	}

	/**
	 * @param mRenderer the mRenderer to set
	 */
	public void setmRenderer(MyRenderer mRenderer) {
		this.mRenderer = mRenderer;
	}

}
