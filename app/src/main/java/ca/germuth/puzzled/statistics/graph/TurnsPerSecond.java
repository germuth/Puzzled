package ca.germuth.puzzled.statistics.graph;

import android.app.Activity;
import ca.germuth.puzzled.ReplayParser;
import ca.germuth.puzzled.database.ObjectDB;
import ca.germuth.puzzled.database.SolveDB;
import ca.germuth.puzzled.gui.graph.GaugeGraph;
import ca.germuth.puzzled.gui.graph.Graph;


public class TurnsPerSecond implements GraphStatisticsMeasure{

	@Override
	public Graph getGraph(Activity activity, ObjectDB ob){ 
		SolveDB db = (SolveDB) ob;
		ReplayParser rp = new ReplayParser(db.getReplay());
		
		double moveCount = rp.getMoveCount();
		double duration = db.getDuration();
		
		//convert duration to second
		duration /= 1000.0;
		
		double tps = moveCount / duration;
		
		return new GaugeGraph("Total Turns Per Second (ETM)", new String[]{"TPS"}, new double[]{tps});		
	}
}
