package ca.germuth.puzzled.statistics.graph;

import ca.germuth.puzzled.statistics.text.Highest;
import ca.germuth.puzzled.statistics.text.Lowest;

public interface GraphStatisticsMeasure {
	
	public static final Class<?>[] myMeasures = {Highest.class, Lowest.class};
	
	//protected abstract static ArrayList<Integer> getValues(ArrayList<SolveDB> solves);
	
	//public static abstract ArrayList<String> getNames();
	
}
