package ca.germuth.puzzled.statistics.graph;

import android.app.Activity;
import ca.germuth.puzzled.database.ObjectDB;
import ca.germuth.puzzled.database.PuzzleDB;
import ca.germuth.puzzled.database.PuzzledDatabase;
import ca.germuth.puzzled.gui.graph.Graph;
import ca.germuth.puzzled.gui.graph.HistogramGraph;

public class Histogram implements GraphStatisticsMeasure{
	@Override
	public Graph getGraph(Activity activity, ObjectDB ob) {
		PuzzledDatabase db = new PuzzledDatabase(activity);
		return new HistogramGraph("Solve Histogram", db.getAllSolves((PuzzleDB)ob));
	}

}
