package at.technikum.mti.fancycoverflow;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import ca.germuth.puzzled.PuzzleSelectActivity;
import ca.germuth.puzzled.R;
import ca.germuth.puzzled.puzzle.Puzzle;

public class FancyCoverFlowPuzzleAdapter extends FancyCoverFlowAdapter{

	public FancyCoverFlowPuzzleAdapter(PuzzleSelectActivity ac, ArrayList<Integer> images){
		super();
		this.activity = ac;
		this.images = images;
	}
	// =============================================================================
	// Private members
	// =============================================================================

	//private int[] images = { R.drawable.puzzle_3by3, R.drawable.puzzle_3by3,
	//		R.drawable.puzzle_3by3, R.drawable.puzzle_3by3};
	private PuzzleSelectActivity activity;
	private ArrayList<Integer> images;

	// =============================================================================
	// Supertype overrides
	// =============================================================================

	@Override
	public int getCount() {
		return images.size();
	}

	@Override
	public Integer getItem(int i) {
		return images.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getCoverFlowItem(final int i, View reuseableView, ViewGroup viewGroup) {
		ImageView imageView = null;

		if (reuseableView != null) {
			imageView = (ImageView) reuseableView;
		} else {
			imageView = new ImageView(viewGroup.getContext());
			imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			imageView
					.setLayoutParams(new FancyCoverFlow.LayoutParams(300, 400));

		}
		//TODO i don't know
		//imageView.setImageResource(this.getItem(i));
		imageView.setImageResource(R.drawable.puzzle_3by3);
		imageView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				activity.puzzleSelected(i);
			}
		});
		return imageView;
	}
	
}
