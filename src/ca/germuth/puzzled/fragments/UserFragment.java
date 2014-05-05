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
import ca.germuth.puzzled.gui.Graph;
import ca.germuth.puzzled.gui.StatisticsPanel;
import net.peterkuterna.android.apps.swipeytabs.SwipeyTabFragment;

public class UserFragment extends SwipeyTabFragment{
	private static final String NAME = "USER";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.statistic_panel, null);
		ViewGroup list = (ViewGroup) root.getChildAt(0);
		
		//create header panel which left half is last solve, and right half is averages
		StatisticsPanel st = new StatisticsPanel(this.getActivity());
		View.inflate(this.getActivity(), R.layout.statistic_user_header, st);
		
		//create graph
		StatisticsPanel st2 = new StatisticsPanel(this.getActivity());
		
		WebView webview = new WebView(this.getActivity());
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webview.requestFocusFromTouch();
        webview.setWebViewClient(new WebViewClient());
        webview.setWebChromeClient(new WebChromeClient());
        
        Graph g = new Graph("Average Solve Duration over Time", 
        		"Solve Number",
        		new String[]{"Average Duration"},
        		new double[]{1, 20, 30, 40},
        		new double[][]{ {25.14, 24.67, 24.11, 24.01} });
        
        // Load the URL
        webview.loadDataWithBaseURL( "file:///android_asset/", g.getJavaScript(), "text/html", "utf-8", null );
        st2.addView(webview);
        
		list.addView(st);
		list.addView(st2);
		
		return root;
	}
	
	@Override
	public String getName() {
		return NAME;
	}
}
