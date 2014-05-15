package ca.germuth.puzzled.statistics.text;

import java.util.ArrayList;

import ca.germuth.puzzled.database.SolveDB;


public interface TextStatisticsMeasure {
	
	static final Class<?>[] myMeasures = {Highest.class, Lowest.class};
	
	//static abstract ArrayList<Integer> getValues(ArrayList<SolveDB> solves);
	
	//static abstract ArrayList<String> getNames();
}
