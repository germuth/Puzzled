package at.technikum.mti.fancycoverflow;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import ca.germuth.puzzled.R;

public class FancyCoverFlowPuzzleAdapter extends FancyCoverFlowAdapter{

	public FancyCoverFlowPuzzleAdapter(){
		super();
	}
	// =============================================================================
	// Private members
	// =============================================================================

	private int[] images = { R.drawable.puzzle_3by3, R.drawable.puzzle_3by3,
			R.drawable.puzzle_3by3, R.drawable.puzzle_3by3};

	// =============================================================================
	// Supertype overrides
	// =============================================================================

	@Override
	public int getCount() {
		return images.length;
	}

	@Override
	public Integer getItem(int i) {
		return images[i];
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getCoverFlowItem(int i, View reuseableView, ViewGroup viewGroup) {
		ImageView imageView = null;

		if (reuseableView != null) {
			imageView = (ImageView) reuseableView;
		} else {
			imageView = new ImageView(viewGroup.getContext());
			imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			imageView
					.setLayoutParams(new FancyCoverFlow.LayoutParams(300, 400));

		}

		imageView.setImageResource(this.getItem(i));
		return imageView;
	}
}
