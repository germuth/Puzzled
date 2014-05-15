package ca.germuth.puzzled.statistics.text;

import java.util.ArrayList;

import ca.germuth.puzzled.database.SolveDB;

public class FewestMoves {

	public static double getValues(ArrayList<SolveDB> solves)
			throws IllegalArgumentException {

		if( solves.isEmpty() ){
			throw new IllegalArgumentException();
		}
		
		int min = Integer.MAX_VALUE;
		for (SolveDB s : solves) {
			//TODO just length of string
			if (s.getmReplay().length() < min) {
				min = s.getmReplay().length();
			}
		}

		return min;
	}
}
