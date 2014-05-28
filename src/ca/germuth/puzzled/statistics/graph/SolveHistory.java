package ca.germuth.puzzled.statistics.graph;

import java.util.ArrayList;

import android.app.Activity;
import ca.germuth.puzzled.database.ObjectDB;
import ca.germuth.puzzled.database.PuzzleDB;
import ca.germuth.puzzled.database.PuzzledDatabase;
import ca.germuth.puzzled.database.SolveDB;
import ca.germuth.puzzled.gui.graph.Graph;
import ca.germuth.puzzled.gui.graph.LineGraph;
import ca.germuth.puzzled.util.Utils;

public class SolveHistory implements GraphStatisticsMeasure {
	private static final String name = "Solve History";

	@Override
	public Graph getGraph(Activity activity, ObjectDB ob) {

		PuzzleDB puzDB = (PuzzleDB) ob;
		PuzzledDatabase db = new PuzzledDatabase(activity);
		ArrayList<SolveDB> solves = db.getAllSolves(puzDB);

		String[] solveDuration = new String[solves.size()];
		String[] solveDate = new String[solves.size()];

		for (int i = 0; i < solves.size(); i++) {
			SolveDB current = solves.get(i);
			int duration = current.getmDuration();
			solveDuration[i] = Utils.solveDurationToStringSeconds(duration);

			long solveD = current.getmDateTime();
			solveDate[i] = Utils.solveDateToString(solveD);
		}

		return new LineGraph(name, "Date", "Seconds",
				new String[] { "Solve Duration" }, solveDate,
				new String[][] { solveDuration });
	}
}
