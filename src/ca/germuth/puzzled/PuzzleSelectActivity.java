package ca.germuth.puzzled;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import at.technikum.mti.fancycoverflow.FancyCoverFlow;
import at.technikum.mti.fancycoverflow.FancyCoverFlowPuzzleAdapter;
import ca.germuth.puzzled.puzzle.Puzzle;
import ca.germuth.puzzled.util.FontManager;
import ca.germuth.puzzled.util.Utils;
/**
 * TODO: A two dimensional FancyCoverFlow would be pretty cool
 * Horizontal switches through puzzle types
 * Vertical changes through sizes of that puzzle 2x2-7x7
 * 	Wouldn't work because cube has 2x3x3, 2x3x4, etc
 * @author Germuth
 *
 */
public class PuzzleSelectActivity extends PuzzledActivity implements OnClickListener {

	private FancyCoverFlowPuzzleAdapter adapter;
	private ArrayList<Puzzle> puzzles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzle_select);

		//creates horizontally sliding list of images
		createFancyCoverFlow();

		Button backBtn = (Button) this.findViewById(R.id.activity_puzzle_select_back_button);
		backBtn.setTypeface( FontManager.getTypeface(this, FontManager.AGENT_ORANGE_FONT));
		backBtn.setOnClickListener(this);

		Button optionBtn = (Button) this.findViewById(R.id.activity_puzzle_select_option_button);
		optionBtn.setTypeface( FontManager.getTypeface(this, FontManager.AGENT_ORANGE_FONT));
		optionBtn.setOnClickListener(this);

		Button goBtn = (Button) this.findViewById(R.id.activity_puzzle_select_go_button);
		goBtn.setTypeface( FontManager.getTypeface(this, FontManager.AGENT_ORANGE_FONT));
		goBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_puzzle_select_back_button:
			this.finish();
			break;
		case R.id.activity_puzzle_select_option_button:
			break;
		case R.id.activity_puzzle_select_go_button:
			//this.adapter.get
			break;
		}
	}

	/**
	 * Creates the horizontally sliding list of images. It does this by 
	 * getting all puzzles (all classes which implement Puzzle) and calling
	 * the static method getImageResource on each. This returns a int
	 * from R.java that is used in the list of images. 
	 */
	private void createFancyCoverFlow(){
		//get the image for each puzzle
		ArrayList<Integer> puzzleImageResources = getImageResources();

		FancyCoverFlow fancyCoverFlow = (FancyCoverFlow) this.findViewById(R.id.activity_puzzle_selection_fcf); 
		this.adapter = new FancyCoverFlowPuzzleAdapter(this, puzzleImageResources);
		fancyCoverFlow.setAdapter(this.adapter);
		//adapter . getSomething
		fancyCoverFlow.setMaxRotation(45);
		fancyCoverFlow.setUnselectedAlpha(0.3f);
		fancyCoverFlow.setUnselectedSaturation(0.0f);
		fancyCoverFlow.setUnselectedScale(0.4f);
	}

	public void puzzleSelected(int i){
		Puzzle p = puzzles.get(i);

		Intent myIntent = new Intent(PuzzleSelectActivity.this, GameActivity.class);
		this.startActivity(myIntent);
	}

	/**
	 * Gets the image from Puzzle.getImage for each puzzle and returns the list.
	 * Images are in the form of an int referencing R.java
	 * @return
	 */
	private ArrayList<Integer> getImageResources(){
		ArrayList<Class<?>> allPuzzles = Utils.getConcreteTypes(Puzzle.class);
		//get image for each puzzle
		//once you select a puzzle with GoGO button
		//then dialog prompts for argument if any
		ArrayList<Integer> images = new ArrayList<Integer>();
		puzzles = new ArrayList<Puzzle>();
		for(int i = 0; i < allPuzzles.size(); i++){
			try {
				Class<?> current = allPuzzles.get(i);

				Method getImage = current.getMethod("getLayout", (Class[]) null);
				int imageResource = (Integer)getImage.invoke(null, (Object[]) null); 
				images.add(imageResource);

				Constructor<?> constr = current.getConstructors()[0];
				Object o = constr.newInstance(Integer.valueOf(3), Integer.valueOf(3), Integer.valueOf(3));
				puzzles.add((Puzzle)o);

			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return images;
	}
}