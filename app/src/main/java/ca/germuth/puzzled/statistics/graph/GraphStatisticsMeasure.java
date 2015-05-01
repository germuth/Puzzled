package ca.germuth.puzzled.statistics.graph;

import android.app.Activity;
import ca.germuth.puzzled.database.ObjectDB;
import ca.germuth.puzzled.gui.graph.Graph;
import ca.germuth.puzzled.statistics.text.Highest;
import ca.germuth.puzzled.statistics.text.Lowest;

public interface GraphStatisticsMeasure {
	
	public static final Class<?>[] myMeasures = {Highest.class, Lowest.class};
	
	public Graph getGraph(Activity activity, ObjectDB ob);
	//protected abstract static ArrayList<Integer> getValues(ArrayList<SolveDB> solves);
	
	//public static abstract ArrayList<String> getNames();
	
}
