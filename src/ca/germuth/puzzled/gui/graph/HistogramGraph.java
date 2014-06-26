package ca.germuth.puzzled.gui.graph;

import java.util.ArrayList;

import ca.germuth.puzzled.database.SolveDB;
import ca.germuth.puzzled.util.Utils;

public class HistogramGraph extends Graph{
	public static final int X_SIZE = 500;
	public static final int Y_SIZE = 250;

	private String mTitle;
	private ArrayList<SolveDB> mSolves;

	public HistogramGraph(String title, ArrayList<SolveDB> solves) {
		mTitle = title;
		mSolves = solves;
	}

	// TODO stringbuilder much better
	public String getJavaScript() {
		String curr = "";
		for (int i = 0; i < mSolves.size(); i++) {
			curr += "['"
					+ Utils.solveDateToString(mSolves.get(i).getDateSolved())
					+ "', ";
			curr += Utils.solveDurationToStringSeconds(mSolves.get(i).getDuration())
					+ "]";
			if (i != mSolves.size() - 1) {
				curr += ", ";
			}
		}
//		curr += "['Sauronithoides (narrow-clawed lizard)', 2.0], ['Seismosaurus (tremor lizard)', 45.7], ['Spinosaurus (spiny lizard)', 12.2], ['Supersaurus (super lizard)', 30.5], ['Tyrannosaurus (tyrant lizard)', 15.2], ['Ultrasaurus (ultra lizard)', 30.5], ['Velociraptor (swift robber)', 1.8]";

			
			String content = "<html>"
	                + "  <head>"
	                + "    <script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>"
	                + "    <script type=\"text/javascript\">"
	                + "      google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});"
	                + "      google.setOnLoadCallback(drawChart);"
	                + "      function drawChart() {"
	                + "        var data = google.visualization.arrayToDataTable(["
	                + "          ['Solve Date', 'Solve Duration'], ";
//	              + "          ['1',   25.14],"
//	              + "          ['20',  24.67],"
//	              + "          ['30',  24.11],"
//	              + "          ['40',  24.01]"
			
			content += curr + "        ]);"
	                + "        var options = {"
	                + "          title: '" + mTitle + "',"
	                + "          legend: {position: 'none' },"
	                + "          colors: ['#FFAD5F'],"
//	                + "          hAxis: {title: '" + mXAxisName      + "', titleTextStyle: {color: 'blue'}},"
//	                + "          vAxis: {title: '" + this.mYAxisUnit + "', titleTextStyle: {color: 'blue'}}"
	                + "        };"
	                + "        var chart = new google.visualization.Histogram(document.getElementById('chart_div'));"
	                + "        chart.draw(data, options);"
	                + "      }"
	                + "    </script>"
	                + "  </head>"
	                + "  <body>"
	                + "    <div id=\"chart_div\" style=\"width: " + X_SIZE + "px; height: " + Y_SIZE + "px;\"></div>"
//	                + "       <img style=\"padding: 0; margin: 0 0 0 330px; display: block;\" src=\"truiton.png\"/>"
	                + "  </body>" + "</html>";
			return content;
		}	
}
