package ca.germuth.puzzled.statistics.text;

import java.util.ArrayList;

import android.app.Activity;
import ca.germuth.puzzled.database.PuzzleDB;
import ca.germuth.puzzled.database.PuzzledDatabase;
import ca.germuth.puzzled.database.SolveDB;

public class SolveNumber implements TextStatisticsMeasure{

	@Override
	public int getType() {
		return TextStatisticsMeasure.SOLVE_TYPE;
	}

	@Override
	public String getValue(Activity mActivity, Object mDBObject,
			int optionalParam) {
		SolveDB solve = (SolveDB) mDBObject;
		
		PuzzledDatabase db = new PuzzledDatabase(mActivity);
		ArrayList<SolveDB> solves = db.getAllSolves(solve.getmPuzzle());
		
		for(int i = 0; i < solves.size(); i++){
			if( solves.get(i).equals(solve)){
				return i + 1 + "";
			}
		}
		return "N/A";
	}
}
