package ca.germuth.puzzled.gui;

public class Graph {
	
	private static final int X_SIZE = 500;
	private static final int Y_SIZE = 250;
	
	private String mTitle;
	private String mXAxisName;
	private String[] mYAxesName;
	private double[] mXValues;
	private double[][] mYValues;
	
	public Graph(String title, String xAxisName, String[] yAxesName, double xValues[], double yValues[][]){
		assert ( yAxesName.length == yValues.length && xValues.length == yValues[0].length);
		mTitle = title;
		mXAxisName = xAxisName;
		mYAxesName = yAxesName;
		mXValues = xValues;
		mYValues = yValues;
	}
	
	//TODO stringbuilder much better
	public String getJavaScript(){
		String yAxesName = "";
		for(int i = 0; i < this.mYAxesName.length; i++){
			yAxesName += "'" + mYAxesName[i] + "'";
			if( i != mYAxesName.length - 1){
				yAxesName += ", ";
			}
		}
		
		String[] myValues = new String[mXValues.length];
		for(int i = 0; i < mXValues.length; i++){
			String curr = "['" + mXValues[i] + "', ";
			for(int j = 0; j < mYValues.length; j++){
				curr +=  mYValues[j][i];
				if( j != mYValues[j].length - 1){
					curr += ", ";
				}
			}
			curr += "],";
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
                + "          ['" + mXAxisName + "', " + yAxesName + "],";
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
                + "          hAxis: {title: '" + mXAxisName + "', titleTextStyle: {color: 'red'}}"
                + "        };"
                + "        var chart = new google.visualization.LineChart(document.getElementById('chart_div'));"
                + "        chart.draw(data, options);"
                + "      }"
                + "    </script>"
                + "  </head>"
                + "  <body>"
                + "    <div id=\"chart_div\" style=\"width: " + X_SIZE + "px; height: " + Y_SIZE + "px;\"></div>"
                + "       <img style=\"padding: 0; margin: 0 0 0 330px; display: block;\" src=\"truiton.png\"/>"
                + "  </body>" + "</html>";
		return content;
	}	
}
