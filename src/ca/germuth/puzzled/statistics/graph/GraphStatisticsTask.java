package ca.germuth.puzzled.statistics.graph;

import java.lang.reflect.InvocationTargetException;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import ca.germuth.puzzled.database.ObjectDB;
import ca.germuth.puzzled.gui.StatisticsPanel;
import ca.germuth.puzzled.gui.graph.GaugeGraph;
import ca.germuth.puzzled.gui.graph.Graph;
import ca.germuth.puzzled.gui.graph.LineGraph;

public class GraphStatisticsTask extends AsyncTask<Void, Void, Void> {
	private ProgressBar mPB;
	private Graph mGraph;
	private ViewGroup list;
	private StatisticsPanel sPanel;
	private Activity mActivity;
	private Class<?> mClass;
	private ScrollView mScrolly;
	private ObjectDB mObjectDB;

	public GraphStatisticsTask(Activity activity, ObjectDB o, Class<?> c,
			ScrollView scrolly, ViewGroup list, StatisticsPanel panel) {
		mActivity = activity;
		this.list = list;
		sPanel = panel;
		mClass = c;
		mScrolly = scrolly;
		mObjectDB = o;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		mPB = new ProgressBar(mActivity);
		sPanel.setMinimumHeight(LineGraph.Y_SIZE);
		sPanel.setMinimumWidth(LineGraph.X_SIZE);
		sPanel.setGravity(Gravity.CENTER);
		sPanel.addView(mPB);
		list.addView(sPanel, 1);
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			GraphStatisticsMeasure gsm = (GraphStatisticsMeasure) mClass.getConstructors()[0].newInstance();

			mGraph = gsm.getGraph(mActivity, mObjectDB);

		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);

		WebView webview = new WebView(mActivity);
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setBuiltInZoomControls(true);
		webview.requestFocusFromTouch();
		webview.setWebViewClient(new WebViewClient());
		webview.setWebChromeClient(new WebChromeClient());
		// Load the URL
		webview.loadDataWithBaseURL("file:///android_asset/",
				mGraph.getJavaScript(), "text/html", "utf-8", null);

		sPanel.removeAllViews();
		sPanel.addView(webview);

		mScrolly.fullScroll(ScrollView.FOCUS_UP);
	}
}
