package ca.germuth.puzzled;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import at.technikum.mti.fancycoverflow.FancyCoverFlow;
import at.technikum.mti.fancycoverflow.FancyCoverFlow.OnSwipeListener;
import at.technikum.mti.fancycoverflow.FancyCoverFlowPuzzleAdapter;
import at.technikum.mti.fancycoverflow.PuzzleItem;
import ca.germuth.puzzled.database.PuzzleDB;
import ca.germuth.puzzled.database.PuzzledDatabase;
import ca.germuth.puzzled.database.SolveDB;
import ca.germuth.puzzled.puzzle.Puzzle;
import ca.germuth.puzzled.puzzle.cube.Cube;
import ca.germuth.puzzled.puzzle.minx.Minx;
import ca.germuth.puzzled.puzzle.square1.Square1;
import ca.germuth.puzzled.util.FontManager;
import ca.germuth.puzzled.GameActivityType;
/**
 * TODO: A two dimensional FancyCoverFlow would be pretty cool
 * @author Germuth
 *
 */
public class PuzzleSelectActivity extends PuzzledActivity{

	private FancyCoverFlowPuzzleAdapter adapter;
	private ArrayList<Puzzle> puzzles;
	private TextView mPuzzleTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzle_select);

		//creates horizontally sliding list of images
		createFancyCoverFlow();
		
		mPuzzleTitle = (TextView) this.findViewById(R.id.activity_puzzle_selection_puzzle_name);
		mPuzzleTitle.setTypeface(FontManager.getTypeface(this, FontManager.AGENT_ORANGE_FONT));

	}

	/**
	 * Creates the horizontally sliding list of images. It does this by 
	 * getting all puzzles (all classes which implement Puzzle) and calling
	 * the static method getImageResource on each. This returns a int
	 * from R.java that is used in the list of images. 
	 */
	private void createFancyCoverFlow(){
		ArrayList<PuzzleItem> puz = getImageResources();
		FancyCoverFlow fancyCoverFlow = (FancyCoverFlow) this.findViewById(R.id.activity_puzzle_selection_fcf);
		this.adapter = new FancyCoverFlowPuzzleAdapter(this, puz);
		fancyCoverFlow.SetOnSwipeListener(new OnSwipeListener(){
			public void OnSwipe(String index){
				if( ! mPuzzleTitle.getText().equals( index ) ){
					mPuzzleTitle.setText( index );					
				}
			}
		});
		fancyCoverFlow.setAdapter(this.adapter);
		//adapter . getSomething
		fancyCoverFlow.setMaxRotation(45);
		fancyCoverFlow.setUnselectedAlpha(0.3f);
		fancyCoverFlow.setUnselectedSaturation(0.0f);
		fancyCoverFlow.setUnselectedScale(0.4f);
	}

	//TODO do with listeners
	@SuppressLint("NewApi") 
	public void puzzleSelected(int index, PuzzleItem puz){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		//figure out whether there is a solve to start from
		PuzzledDatabase db = new PuzzledDatabase(this);
//		PuzzleDB puzDB = db.getPuzzleByName(this.puzzles.get(index).getName());
//		final SolveDB unfinished = db.getUnfinishedSolve(puzDB);
		final SolveDB unfinished = db.getUnfinishedSolve();

		ArrayList<NumberPicker> numberPickers = new ArrayList<NumberPicker>();
		//get constructor arguments from class
		final Constructor<?> puzConstructor = puz.getmClass().getConstructors()[0];
		Class<?>[] paramTypes = puzConstructor.getParameterTypes();
		//get argument names
		String[] para_titles;
		try {
			para_titles = (String[]) puz.getmClass().getField("constructor_param_titles").get(null);
			
			LinearLayout across = new LinearLayout(this);
			across.setGravity(Gravity.CENTER);
			across.setOrientation(LinearLayout.HORIZONTAL);
			for(int i = 0; i < paramTypes.length; i++){
				LinearLayout oneParam = new LinearLayout(this);
				oneParam.setOrientation(LinearLayout.VERTICAL);
				
				TextView lbl = new TextView(this);
				lbl.setGravity(Gravity.CENTER);
				lbl.setText( para_titles[i]);
				//lbl.setTypeface(FontManager.getTypeface(this, FontManager.AGENT_ORANGE_FONT));
				
				//TODO too new
				NumberPicker np = new NumberPicker(this);
				np.setScaleX(0.7f);
				np.setScaleY(0.7f);
				
				np.setMaxValue(50);
				np.setMinValue(1);
				np.setValue(3);//Default
				numberPickers.add(np);
				
				oneParam.addView(lbl);
				oneParam.addView(np);
				
				across.addView(oneParam);
			}
			final ArrayList<NumberPicker> numPickers = new ArrayList<NumberPicker>(numberPickers);

			DialogInterface.OnClickListener switchActivity = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					Object[] params = new Object[numPickers.size()];
					for(int i = 0; i < numPickers.size(); i++){
						params[i] = numPickers.get(i).getValue();
					}
					try {
						((PuzzledApplication)getApplication()).setPuzzle( (Puzzle)puzConstructor.newInstance( params ) );
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
					Intent myIntent = new Intent(PuzzleSelectActivity.this, GameActivity.class);
					myIntent.putExtra("activity_type", (Parcelable)GameActivityType.PLAY);
					myIntent.putExtra("solve", unfinished);
					startActivity(myIntent);
				}
			};

			builder.setView(across);
			builder.setTitle("Select Puzzle Size");
			builder.setPositiveButton("Ready", switchActivity)
	        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User cancelled the dialog
				}
			});

			if(unfinished != null){
				builder.setNeutralButton("Resume Solve", switchActivity);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		builder.create().show();
	}

	/**
	 * Gets the image from Puzzle.getImage for each puzzle and returns the list.
	 * Images are in the form of an int referencing R.java
	 * @return
	 */
	private ArrayList<PuzzleItem> getImageResources(){
		ArrayList<PuzzleItem> puzz = new ArrayList<PuzzleItem>();
		puzz.add(new PuzzleItem(R.drawable.puzzle_3by3, "Cube", Cube.class));
		puzz.add(new PuzzleItem(R.drawable.megaminx_puzzle, "Megaminx", Minx.class));
		puzz.add(new PuzzleItem(R.drawable.square1_puzzle, "Square 1", Square1.class));
		return puzz;
//		ArrayList<Class<?>> allPuzzles = Utils.getConcreteTypes(Puzzle.class);
//		//get image for each puzzle
//		//once you select a puzzle with GoGO button
//		//then dialog prompts for argument if any
//		ArrayList<Integer> images = new ArrayList<Integer>();
//		puzzles = new ArrayList<Puzzle>();
//		for(int i = 0; i < allPuzzles.size(); i++){
//			try {
//				Class<?> current = allPuzzles.get(i);
//
//				Method getImage = current.getMethod("getLayout", (Class[]) null);
//				int imageResource = (Integer)getImage.invoke(null, (Object[]) null); 
//				images.add(imageResource);
//
//				Constructor<?> constr = current.getConstructors()[0];
//				Object o = constr.newInstance(Integer.valueOf(3), Integer.valueOf(3), Integer.valueOf(3));
//				puzzles.add((Puzzle)o);
//
//			} catch (NoSuchMethodException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			} catch (IllegalArgumentException e) {
//				e.printStackTrace();
//			} catch (InvocationTargetException e) {
//				e.printStackTrace();
//			} catch (InstantiationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return images;
	}
}