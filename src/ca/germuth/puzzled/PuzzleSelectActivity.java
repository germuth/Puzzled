package ca.germuth.puzzled;

import ca.germuth.puzzled.util.FontManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import at.technikum.mti.fancycoverflow.FancyCoverFlow;
import at.technikum.mti.fancycoverflow.FancyCoverFlowPuzzleAdapter;

public class PuzzleSelectActivity extends PuzzledActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_puzzle_select);
		
		FancyCoverFlow fancyCoverFlow = (FancyCoverFlow) this.findViewById(R.id.activity_puzzle_selection_fcf); 
		fancyCoverFlow.setAdapter(new FancyCoverFlowPuzzleAdapter());
		//adapter . getSomething
		fancyCoverFlow.setMaxRotation(45);
		fancyCoverFlow.setUnselectedAlpha(0.3f);
		fancyCoverFlow.setUnselectedSaturation(0.0f);
		fancyCoverFlow.setUnselectedScale(0.4f);
		
		Button backBtn = (Button) this.findViewById(R.id.activity_puzzle_select_back_button);
		backBtn.setTypeface( FontManager.getTypeface(this, FontManager.AGENT_ORANGE_FONT));
		
		Button optionBtn = (Button) this.findViewById(R.id.activity_puzzle_select_option_button);
		optionBtn.setTypeface( FontManager.getTypeface(this, FontManager.AGENT_ORANGE_FONT));
		
		Button goBtn = (Button) this.findViewById(R.id.activity_puzzle_select_go_button);
		goBtn.setTypeface( FontManager.getTypeface(this, FontManager.AGENT_ORANGE_FONT));
		
		new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(PuzzleSelectActivity.this, GameActivity.class);
                PuzzleSelectActivity.this.startActivity(mainIntent);
                PuzzleSelectActivity.this.finish();
            }
        }, 5000);
	}
}
