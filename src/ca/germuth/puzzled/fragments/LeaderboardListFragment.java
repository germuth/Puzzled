package ca.germuth.puzzled.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ca.germuth.puzzled.R;
import ca.germuth.puzzled.leaderboard.LeaderboardItemAdapter;
import ca.germuth.puzzled.leaderboard.Solve;

public class LeaderboardListFragment extends ListFragment{
	private OnSolveSelectedListener mCallback;
	private LeaderboardItemAdapter mAdapter;
	private ArrayList<Solve> mSolves;
	// The container Activity must implement this interface so the frag can deliver messages
    public interface OnSolveSelectedListener {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onSolveSelected(int position);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mSolves = new ArrayList<Solve>();
        this.mAdapter = new LeaderboardItemAdapter(this.getActivity(), mSolves);
        
        setListAdapter(this.mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
        if (getFragmentManager().findFragmentById(R.id.leaderboard_detail_fragment) != null) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }
    
    public void assignSolves(ArrayList<Solve> solves){
    	this.mSolves.clear();
    	this.mSolves.addAll(solves);
    	this.mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnSolveSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSolveSelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Notify the parent activity of selected item
        mCallback.onSolveSelected(position);
        
        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
    }
    
    
    
    
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_leaderboard_list, container, false);
	}
	
}
