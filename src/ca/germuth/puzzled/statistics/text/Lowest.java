package ca.germuth.puzzled.statistics.text;

import java.util.ArrayList;

import ca.germuth.puzzled.database.SolveDB;

public class Lowest implements TextStatisticsMeasure{

	//@Override
	public static double getValues(ArrayList<SolveDB> solves) {
		
		if( solves.isEmpty() ){
			throw new IllegalArgumentException();
		}
		
		int max = Integer.MIN_VALUE;
		for(SolveDB s : solves){
			if( s.getmDuration() > max){
				max = s.getmDuration();
			}
		}
		return max;
	}

	//@Override
	public static ArrayList<String> getNames() {
		ArrayList<String> names = new ArrayList<String>();
		names.add("Slowest Solve:");
		return names;
	}
}
