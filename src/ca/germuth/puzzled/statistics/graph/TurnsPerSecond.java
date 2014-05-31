package ca.germuth.puzzled.statistics.graph;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import ca.germuth.puzzled.PuzzleMoveListener;
import ca.germuth.puzzled.ReplayParser;
import ca.germuth.puzzled.ReplayParser.ReplayMove;
import ca.germuth.puzzled.database.ObjectDB;
import ca.germuth.puzzled.database.SolveDB;
import ca.germuth.puzzled.gui.graph.GaugeGraph;
import ca.germuth.puzzled.gui.graph.Graph;
import ca.germuth.puzzled.gui.graph.PieGraph;
import ca.germuth.puzzled.puzzle.PuzzleTurn;
import ca.germuth.puzzled.puzzle.cube.Cube;


public class TurnsPerSecond implements GraphStatisticsMeasure{

	@Override
	public Graph getGraph(Activity activity, ObjectDB ob){ 
		int crossDone = 0;
		int firstPairDone = 0;
		int secondPairDone = 0;
		int thirdPairDone = 0;
		int fourthPairDone = 0;
		int OLLDone = 0;
		int PLLDone = 0;
		
		SolveDB db = (SolveDB) ob;
		ReplayParser rp = new ReplayParser(db.getmReplay());
		Iterator<ReplayMove> iterator = rp.iterator();
		
		double moveCount = rp.getMoveCount();
		double duration = db.getmDuration();
		
		//convert duration to second
		duration /= 1000.0;
		
		double tps = moveCount / duration;
		
		return new GaugeGraph("Total Turns Per Second (ETM)", new String[]{"TPS"}, new double[]{tps});		
	}
}
