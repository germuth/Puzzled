package ca.germuth.puzzled.gui.graph;

public class PieGraph extends Graph{
	
	public static final int X_SIZE = 500;
	public static final int Y_SIZE = 250;
	
	private String mTitle;
	private String mStepName;
	private String mStepLengthName;
	private String[] mXValues;
	private String[] mYValues;
	
	public PieGraph(String title, String stepName, String stepLengthName, String xValues[], String yValues[]){
		mTitle = title;
		mStepName = stepName;
		mStepLengthName = stepLengthName;
		mXValues = xValues;
		mYValues = yValues;
	}
	
	//TODO stringbuilder much better
	public String getJavaScript(){
		
		String[] myValues = new String[mXValues.length];
		for(int i = 0; i < mXValues.length; i++){
			String curr = "['" + mXValues[i] + "', " + mYValues[i] + "]";
			if( i != mXValues.length - 1){
				curr += ",";
			}
			myValues[i] = curr;
		}
		
		String content = "<html>"
                + "  <head>"
                + "    <script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>"
                + "    <script type=\"text/javascript\">"
                + "      google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});"
                + "      google.setOnLoadCallback(drawChart);"
                + "      function drawChart() {"
                + "        var data = google.visualization.arrayToDataTable(["
//              + "          ['Total Solves', 'Average Duration'],"
                + "          ['" + mStepName + "', '" + mStepLengthName + "'],";
//              + "          ['1',   25.14],"
//              + "          ['20',  24.67],"
//              + "          ['30',  24.11],"
//              + "          ['40',  24.01]"
		
		for(int i = 0; i < myValues.length; i++){
			content += myValues[i];
		}
		content +=
                  "        ]);"
                + "        var options = {"
                + "          title: '" + mTitle + "',"
                + "          pieHole: 0.3,"
                + "        };"
                + "        var chart = new google.visualization.PieChart(document.getElementById('donutchart'));"
                + "        chart.draw(data, options);"
                + "      }"
                + "    </script>"
                + "  </head>"
                + "  <body>"
                + "      <div id=\"donutchart\" style=\"width: " + X_SIZE + "px; height: " + Y_SIZE + "px;\"></div>"
                //+ "    <div id=\"chart_div\" style=\"width: " + X_SIZE + "px; height: " + Y_SIZE + "px;\"></div>"
                //+ "       <img style=\"padding: 0; margin: 0 0 0 330px; display: block;\" src=\"truiton.png\"/>"
                + "  </body>" + "</html>";
		return content;
	}	
}
