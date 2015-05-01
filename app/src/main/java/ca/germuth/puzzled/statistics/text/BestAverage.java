package ca.germuth.puzzled.statistics.text;

import java.util.ArrayList;

import android.app.Activity;
import ca.germuth.puzzled.database.PuzzleDB;
import ca.germuth.puzzled.database.PuzzledDatabase;
import ca.germuth.puzzled.database.SolveDB;
import ca.germuth.puzzled.util.Utils;

public class BestAverage implements TextStatisticsMeasure{

	@Override
	public int getType() {
		return TextStatisticsMeasure.PUZZLE_TYPE;
	}

	@Override
	public String getValue(Activity mActivity, Object mDBObject,
			int optionalParam) {
		PuzzleDB puzz = (PuzzleDB) mDBObject;
		int size = optionalParam;
		
		PuzzledDatabase db = new PuzzledDatabase(mActivity);
		ArrayList<SolveDB> solves = db.getAllSolves(puzz);
		
		int min = Integer.MAX_VALUE;
		
		if( ( size > solves.size() && size != Integer.MAX_VALUE) || solves.isEmpty()){
			return null;
		}
		
		if( size == Integer.MAX_VALUE){
			size = solves.size();
		}
		
		for(int i = 0; i <= solves.size() - size; i++){
			int avg = 0;
			
			for(int j = i; j < i + size; j++){
				avg += solves.get(j).getDuration();
			}
			avg /= size;
			
			if( avg < min ){
				min = avg;
			}
		}
		return Utils.solveDurationToString(min);
	}

}
