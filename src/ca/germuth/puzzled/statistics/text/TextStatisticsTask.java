package ca.germuth.puzzled.statistics.text;

import java.lang.reflect.InvocationTargetException;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;
import ca.germuth.puzzled.database.ObjectDB;
import ca.germuth.puzzled.database.SolveDB;

public class TextStatisticsTask extends AsyncTask<Void, Void, Void>{
	private Activity mActivity;
	private Class<?> mClass;
	private TextView mView;
	private int mOptional;
	private ObjectDB mObjectDB;
	private String mResult;
	
	public static void runAll(Class<?>[] classes, TextView[] views, int[] optionals, ObjectDB dbObject, Activity a){
		for(int i = 0; i < classes.length; i++){
			new TextStatisticsTask(classes[i], views[i], optionals[i], dbObject, a).execute((Void[])null);
		}
	}
	
	public TextStatisticsTask(Class<?> c, TextView view, int optional, ObjectDB theSolve, Activity a){
		mActivity = a;
		mClass = c;
		mView = view;
		mObjectDB = theSolve;
		mOptional = optional;
	}

	@Override
	protected Void doInBackground(Void... params) {
		
		TextStatisticsMeasure tt;
		try {
			tt = (TextStatisticsMeasure) mClass.getConstructors()[0].newInstance();
			if( tt.getType() == TextStatisticsMeasure.PUZZLE_TYPE){
				if( mObjectDB instanceof SolveDB){
					mResult = tt.getValue(mActivity, ((SolveDB)mObjectDB).getPuzzle(), mOptional);
				}else{
					mResult = tt.getValue(mActivity, mObjectDB, mOptional);
				}
			}else if( tt.getType() == TextStatisticsMeasure.SOLVE_TYPE){
				mResult = tt.getValue(mActivity, mObjectDB, mOptional);
			}
		} catch (InstantiationException e) {
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
		
		mView.setText(mResult);
	}
}
