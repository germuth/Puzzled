package ca.germuth.puzzled.statistics.text;

import java.util.ArrayList;

import android.app.Activity;
import ca.germuth.puzzled.database.PuzzleDB;
import ca.germuth.puzzled.database.PuzzledDatabase;
import ca.germuth.puzzled.database.SolveDB;
import ca.germuth.puzzled.util.Utils;

public class TimesSolved implements TextStatisticsMeasure{
	public static double getValues(ArrayList<SolveDB> solves)
			throws IllegalArgumentException {
		return solves.size();
	}

	@Override
	public int getType() {
		return TextStatisticsMeasure.PUZZLE_TYPE;
	}

	@Override
	public String getValue(Activity mActivity, Object mDBObject,
			int optionalParam) {
		PuzzleDB puzz = (PuzzleDB) mDBObject;
		
		PuzzledDatabase db = new PuzzledDatabase(mActivity);
		ArrayList<SolveDB> solves = db.getAllSolves(puzz);
		
		if( solves.isEmpty() ){
			return "0";
		}
		return solves.size() + "";
	}

}
