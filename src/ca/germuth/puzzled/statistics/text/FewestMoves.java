package ca.germuth.puzzled.statistics.text;

import java.util.ArrayList;

import android.app.Activity;
import ca.germuth.puzzled.database.PuzzleDB;
import ca.germuth.puzzled.database.PuzzledDatabase;
import ca.germuth.puzzled.database.SolveDB;
import ca.germuth.puzzled.util.Utils;

public class FewestMoves implements TextStatisticsMeasure{

	@Override
	public int getType() {
		return TextStatisticsMeasure.SOLVE_TYPE;
	}

	@Override
	public String getValue(Activity mActivity, Object mDBObject,
			int optionalParam) {
		PuzzleDB puzz = (PuzzleDB) mDBObject;
		int size = optionalParam;
		
		PuzzledDatabase db = new PuzzledDatabase(mActivity);
		ArrayList<SolveDB> solves = db.getAllSolves(puzz);
		
		if( solves.isEmpty() ){
			return null;
		}
		
		int min = Integer.MAX_VALUE;
		MoveCount cm = new MoveCount();
		for (SolveDB s : solves) {
			//TODO just length of string
			if (s.getmReplay().length() < min) {
				min = Integer.parseInt( cm.getValue(mActivity, s, -1) );
			}
		}

		return min + "";
	}
}
