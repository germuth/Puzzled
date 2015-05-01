package ca.germuth.puzzled.puzzle_layouts.cube;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import ca.germuth.puzzled.GameActivity;
import ca.germuth.puzzled.PuzzleMoveListener;
import ca.germuth.puzzled.PuzzleStateChangeListener;
import ca.germuth.puzzled.openGL.MyGLSurfaceView;

/**
 * Created by Aaron on 4/30/2015.
 */
public abstract class PuzzleLayout extends RelativeLayout implements PuzzleStateChangeListener{
    protected GameActivity mActivity;
    protected MyGLSurfaceView mGlView;
    protected PuzzleMoveListener mPuzzleMoveListener;

    public PuzzleLayout(Context context) {
        super(context);
    }

    public PuzzleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PuzzleLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public PuzzleMoveListener getPuzzleMoveListener() {
        return mPuzzleMoveListener;
    }
    public void setPuzzleMoveListener(PuzzleMoveListener mPuzzleMoveListener) {
        this.mPuzzleMoveListener = mPuzzleMoveListener;
    }
    public GameActivity getActivity() {
        return mActivity;
    }

    public void setActivity(GameActivity mActivity) {
        this.mActivity = (GameActivity)mActivity;
    }

    public MyGLSurfaceView getGlView() {
        return mGlView;
    }

    public void setGlView(MyGLSurfaceView mGlView) {
        this.mGlView = mGlView;
    }
}
