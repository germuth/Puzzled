package ca.germuth.puzzled.statistics.text;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import ca.germuth.puzzled.R;
import ca.germuth.puzzled.database.PuzzledDatabase;
import ca.germuth.puzzled.database.SolveDB;
import ca.germuth.puzzled.fragments.PuzzleFragment;
import ca.germuth.puzzled.gui.StatisticsPanel;
import ca.germuth.puzzled.puzzle.Puzzle;
import ca.germuth.puzzled.puzzle.cube.Cube;

public class TextStatisticsTask extends AsyncTask<Void, Void, Void>{
	private ProgressBar mPB;
	private ArrayList<String> nameList;
	private ArrayList<Integer> valueList;
	private ViewGroup list;
	private StatisticsPanel sPanel;
	private Activity mActivity;
	private Class<?> mClass;
	
	public TextStatisticsTask(Activity activity, Class<?> c, ViewGroup list, StatisticsPanel panel){
		mActivity = activity;
		this.list = list;
		sPanel = panel;
		mClass = c;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		mPB = new ProgressBar(mActivity);
		sPanel.addView(mPB);
	}

	@Override
	protected Void doInBackground(Void... params) {
		
		//get info from database
		PuzzledDatabase db = new PuzzledDatabase( mActivity );
		//TODO fix
		Puzzle puz = new Cube(3, 3 , 3);
		ArrayList<SolveDB> allSolves = db.getAllSolves( db.convert(puz) );
		db.close();
		
		try {
			Method names = mClass.getMethod("getNames",
					(Class<?>[]) null);
			Method values = mClass.getMethod("getValues",
					new Class[] { ArrayList.class });
			
			nameList = (ArrayList<String>) names.invoke(null, null);
			valueList =  (ArrayList<Integer>) values.invoke(null, allSolves);
			
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		
		LinearLayout ll = (LinearLayout) sPanel.findViewById(R.id.statistic_panel_list);
		
		for(int i = 0; i < this.nameList.size(); i++){
			String name = nameList.get(i);
			int value = valueList.get(i);
			
			TextView first = new TextView( mActivity );
			first.setText(name);
			
			TextView second = new TextView( mActivity );
			second.setText(value + "");
			
			LinearLayout inner = new LinearLayout( mActivity );
			inner.addView(first);
			inner.addView(second);
			ll.addView(inner);
		}
		
		sPanel.removeView(mPB);
		sPanel.addView(ll);
		list.addView(sPanel);
	}
}
