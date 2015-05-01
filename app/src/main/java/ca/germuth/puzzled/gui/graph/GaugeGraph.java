package ca.germuth.puzzled.gui.graph;

public class GaugeGraph extends Graph{

	public static final int X_SIZE = 500;
	public static final int Y_SIZE = 250;
	
	private String mTitle;
	private String[] mGaugeNames;
	private String[] mValues;
	
	public GaugeGraph(String title, String[] gaugeNames, double[] values){
		mTitle = title;
		mGaugeNames = gaugeNames;
		mValues = new String[values.length];
		for(int i = 0; i < values.length; i++){
			mValues[i] = String.format("%.2f", values[i]);
//			mValues[i] = (int)values[i] + "";
		}
	}
	
	//TODO stringbuilder much better
	public String getJavaScript(){
		String yAxesName = "";
		for(int i = 0; i < this.mGaugeNames.length; i++){
			yAxesName += "'" + mGaugeNames[i] + "'";
			if( i != mGaugeNames.length - 1){
				yAxesName += ", ";
			}
		}
		
		String data = "";
		for(int i = 0; i < mValues.length; i++){
			String curr = "['" + mGaugeNames[i] + "', " + mValues[i] + "]";
			
			if( i != mValues.length - 1){
				curr += ", ";
			}
			data += curr;
		}
		
		String content = "<html>"
                + "  <head>"
                + "    <script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>"
                + "    <script type=\"text/javascript\">"
                + "      google.load(\"visualization\", \"1\", {packages:[\"gauge\"]});"
                + "      google.setOnLoadCallback(drawChart);"
                + "      function drawChart() {"
                + "        var data = google.visualization.arrayToDataTable(["
//              + "          ['Total Solves', 'Average Duration'],"
                + "          ['Label', 'Value'],";
//              + "          ['1',   25.14],"
//              + "          ['20',  24.67],"
//              + "          ['30',  24.11],"
//              + "          ['40',  24.01]"
		
		content += data;
		content +=
                  "        ]);"
                + "        var options = {"
//                + "          title: '" + mTitle + "',"
                + "          width: 150, height: 150,"
                + "          max: 10, min: 0,"
                + "          redFrom: 8, redTo: 10,"
                + "          yellowFrom: 6, yellowTo: 8,"
                + "          majorTicks: 10, minorTicks: 5"
                + "        };"
                + "        var chart = new google.visualization.Gauge(document.getElementById('chart_div'));"
                + "        chart.draw(data, options);"
                + "      }"
                + "    </script>"
                + "  </head>"
                + "  <body>"
//                + "    <div id=\"chart_div\"></div>"      //style=\"width: " + X_SIZE + "px; height: " + 200 + "px;
                + "    <div id=\"chart_div\" align=\"center\"; \"></div>"
//                + "           <img style=\" margin-left: 100px !important; \">"
//                + "         <img style=\" display: block; margin: 0 auto;\">"
//                + "       <img style=\"padding: 0; margin: auto; display: block;\" src=\"truiton.png\"/>"
                + "  </body>" + "</html>";
		return content;
	}

}
