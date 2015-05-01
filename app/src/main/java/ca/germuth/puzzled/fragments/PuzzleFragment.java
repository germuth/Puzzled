package ca.germuth.puzzled.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ca.germuth.puzzled.R;
import ca.germuth.puzzled.openGL.MyGLSurfaceView;
import ca.germuth.puzzled.util.Chronometer;
//TODO use this fragment to avoid code reuse
public class PuzzleFragment extends Fragment {
	private MyGLSurfaceView mGlView;
	private Chronometer mTimer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// init essential components that should be retained when paused and resumed
	}

	// container is layout in activity, saved instance state is object to hold info from last time
	// fragment was run, if
	// it was previously paused and has now resumed
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
		// return a view from this method that is root of layout, called once for first time
		// for example beow
		// Inflate the layout for this fragment
		// return inflater.inflate(R.layout.example_fragment, container, false);
	}

	@Override
	public void onViewCreated(View root, Bundle savedInstanceState) {
		super.onViewCreated(root, savedInstanceState);

		// grab and set up timer
		mTimer = (Chronometer) root.findViewById(R.id.activity_game_timer);
		// grab openGL view
		mGlView = (MyGLSurfaceView) root.findViewById(R.id.activity_game_gl_surface_view);
	}

	@Override
	public void onPause() {
		super.onPause();
		// save changes, allow it to restart here
	}

}
