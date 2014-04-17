package ca.germuth.puzzled;

import android.os.Bundle;
import at.technikum.mti.fancycoverflow.FancyCoverFlow;
import at.technikum.mti.fancycoverflow.FancyCoverFlowPuzzleAdapter;

public class CubeSelectionActivity extends PuzzledActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_cube_selection);
		
		FancyCoverFlow fancyCoverFlow = (FancyCoverFlow) this.findViewById(R.id.activity_cube_selection_fcf); 
		fancyCoverFlow.setAdapter(new FancyCoverFlowPuzzleAdapter());
		fancyCoverFlow.setMaxRotation(45);
		fancyCoverFlow.setUnselectedAlpha(0.3f);
		fancyCoverFlow.setUnselectedSaturation(0.0f);
		fancyCoverFlow.setUnselectedScale(0.4f);
	}
	
}
