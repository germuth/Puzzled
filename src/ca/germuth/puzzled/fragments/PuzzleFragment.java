package ca.germuth.puzzled.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import ca.germuth.puzzled.R;
import ca.germuth.puzzled.gui.StatisticsPanel;
import net.peterkuterna.android.apps.swipeytabs.SwipeyTabFragment;

public class PuzzleFragment extends SwipeyTabFragment{
	
	private static final String name = "PUZZLE";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.statistic_panel, null);
		ViewGroup list = (ViewGroup) root.getChildAt(0);
		
		//create header panel which left half is last solve, and right half is averages
		StatisticsPanel st = new StatisticsPanel(this.getActivity());
		View.inflate(this.getActivity(), R.layout.statistic_puzzle_header, st);
		
		//create graph
		StatisticsPanel st2 = new StatisticsPanel(this.getActivity());
		
		WebView webview = new WebView(this.getActivity());
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webview.requestFocusFromTouch();
        webview.setWebViewClient(new WebViewClient());
        webview.setWebChromeClient(new WebChromeClient());
        // Load the URL
        webview.loadDataWithBaseURL( "file:///android_asset/", getURL(), "text/html", "utf-8", null );
        st2.addView(webview);
        //webview.loadUrl("file:///android_asset/www/rG.html");

		list.addView(st);
		list.addView(st2);
		
		final String title = this.getName();
		//((TextView) root.findViewById(R.id.text)).setText(title);
		return root;
	}
	
	@Override
	public String getName() {
		return name;
	}

	private String getURL(){
		String content = "<html>"
                + "  <head>"
                + "    <script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>"
                + "    <script type=\"text/javascript\">"
                + "      google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});"
                + "      google.setOnLoadCallback(drawChart);"
                + "      function drawChart() {"
                + "        var data = google.visualization.arrayToDataTable(["
                + "          ['Solve Number', '3x3 Duration', '4x4 Duration'],"
                + "          ['1',  18.67,      37.89],"
                + "          ['2',  17.14,      32.14],"
                + "          ['3',  15.66,      35.77],"
                + "          ['4',  16.84,      40.04]"
                + "        ]);"
                + "        var options = {"
                + "          title: 'Solve Duration over Time',"
                + "          hAxis: {title: 'Solve Number', titleTextStyle: {color: 'red'}}"
                + "        };"
                + "        var chart = new google.visualization.LineChart(document.getElementById('chart_div'));"
                + "        chart.draw(data, options);"
                + "      }"
                + "    </script>"
                + "  </head>"
                + "  <body>"
                + "    <div id=\"chart_div\" style=\"width: 500px; height: 250px;\"></div>"
                + "       <img style=\"padding: 0; margin: 0 0 0 330px; display: block;\" src=\"truiton.png\"/>"
                + "  </body>" + "</html>";
		return content;
	}
}
