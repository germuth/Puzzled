package at.technikum.mti.fancycoverflow;

import java.util.ArrayList;

import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import ca.germuth.puzzled.PuzzleSelectActivity;
import ca.germuth.puzzled.R;
import ca.germuth.puzzled.puzzle.Puzzle;

public class FancyCoverFlowPuzzleAdapter extends FancyCoverFlowAdapter{
	public interface OnSelectListener{
		void OnSelect();
	}
	public FancyCoverFlowPuzzleAdapter(PuzzleSelectActivity ac, ArrayList<PuzzleItem> images){
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
	protected ArrayList<PuzzleItem> images;
	
	private OnSelectListener onSelectListener;

	// =============================================================================
	// Supertype overrides
	// =============================================================================

	@Override
	public int getCount() {
		return images.size();
	}

	@Override
	public Integer getItem(int i) {
		return images.get(i).getResource();
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
			imageView = new ImageView(viewGroup.getContext()){
				private Boolean isClick;
				@Override
				public boolean dispatchTouchEvent(MotionEvent event) {
					switch (event.getAction()) {
					    case MotionEvent.ACTION_DOWN:
					        isClick = true;
					        return true;
					    case MotionEvent.ACTION_CANCEL:
					    case MotionEvent.ACTION_UP:
					        if (isClick) {
					        	activity.puzzleSelected(i,  images.get(i) );
					        	return true;
					        }
					        break;
					    case MotionEvent.ACTION_MOVE:
							//this was breaking it on larger screen
//					        isClick = false;
					        break;
					    default:
					        break;
					}
					return false;
				}
				
			};
			imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

			//TODO fix for larger displays?
//			DisplayMetrics metrics = new DisplayMetrics();
//			this.activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
//			imageView.setLayoutParams(new FancyCoverFlow.LayoutParams(metrics.widthPixels/5, metrics.heightPixels/3));
			imageView.setLayoutParams(new FancyCoverFlow.LayoutParams(300, 400));

		}
		//TODO i don't know
		//imageView.setImageResource(this.getItem(i));
		imageView.setImageResource(images.get(i).getResource());
//		imageView.setOnClickListener(new OnClickListener(){
//			@Override
//			public void onClick(View v) {
//				activity.puzzleSelected(i);
//			}
//		});
		return imageView;
	}

}