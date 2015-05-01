package ca.germuth.puzzled.statistics.text;

import java.util.ArrayList;

import android.app.Activity;
import ca.germuth.puzzled.database.SolveDB;


public interface TextStatisticsMeasure {
	
	public static final int SOLVE_TYPE = 0;
	public static final int PUZZLE_TYPE = 1;
	
	public abstract int getType();
	public abstract String getValue(Activity mActivity, Object mDBObject, int optionalParam);
	
}
