package ca.germuth.puzzled.statistics.text;

import java.util.ArrayList;

import ca.germuth.puzzled.database.SolveDB;

public class Highest implements TextStatisticsMeasure{

//	@Override
	public static double getValues(ArrayList<SolveDB> solves) {
		//ArrayList<Integer> values = new ArrayList<Integer>();
		
		if( solves.isEmpty() ){
			throw new IllegalArgumentException();
		}
		
		int min = Integer.MAX_VALUE;
		for(SolveDB s : solves){
			if( s.getmDuration() < min){
				min = s.getmDuration();
			}
		}
		
		//values.add(min);
		return min;
	}

//	@Override
	public static ArrayList<String> getNames() {
		ArrayList<String> names = new ArrayList<String>();
		names.add("Fastest Solve:");
		return names;
	}

}
