package ca.germuth.puzzled.statistics.text;

import android.app.Activity;
import ca.germuth.puzzled.ReplayParser;
import ca.germuth.puzzled.database.SolveDB;

public class MoveCount implements TextStatisticsMeasure{

	@Override
	public int getType() {
		return TextStatisticsMeasure.SOLVE_TYPE;
	}

	@Override
	public String getValue(Activity mActivity, Object mDBObject,
			int optionalParam) {
		SolveDB solve = (SolveDB) mDBObject;
		
		return new ReplayParser( solve.getmReplay() ).getMoveCount() + "";
	}

}
