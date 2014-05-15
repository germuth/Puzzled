package ca.germuth.puzzled.statistics.text;

import java.util.ArrayList;

import ca.germuth.puzzled.database.SolveDB;

public class BestAverage implements TextStatisticsMeasure{

	//@Override
	public static ArrayList<String> getNames() {
//		ArrayList<String> names = new ArrayList<String>();
//		names.add("Average of 3:");
//		names.add("")
		return null;
	}

	//@Override
	public static double getValues(ArrayList<SolveDB> solves, int size) throws IllegalArgumentException {
		double min = Integer.MAX_VALUE;
		
		if( ( size > solves.size() && size != Integer.MAX_VALUE) || solves.isEmpty()){
			throw new IllegalArgumentException();
		}
		if( size == Integer.MAX_VALUE){
			size = solves.size();
		}
		
		for(int i = 0; i <= solves.size() - size; i++){
			double avg = 0;
			
			for(int j = i; j < i + size; j++){
				avg += solves.get(j).getmDuration();
			}
			avg /= size;
			
			if( avg < min ){
				min = avg;
			}
		}
		return min;
	}

}
