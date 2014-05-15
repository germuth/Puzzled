package ca.germuth.puzzled.statistics.text;

import java.util.ArrayList;

import ca.germuth.puzzled.database.SolveDB;

public class Average {
	
	public static double getValues(ArrayList<SolveDB> solves, int size) throws IllegalArgumentException {
		
		if( size > solves.size() || solves.isEmpty()){
			throw new IllegalArgumentException();
		}
		if( size == Integer.MAX_VALUE){
			size = solves.size();
		}
		double avg = 0;
		int count = 0;
		//iterate backwards since most recent solves are at the back
		for(int i = solves.size() - 1; solves.size() - size <= i; i--){
			avg += solves.get(i).getmDuration();
			count++;
		}
		avg /= count;
		
		return avg;
	}
}
