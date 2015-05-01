package at.technikum.mti.fancycoverflow;

public class PuzzleItem {
	private int mResource;
	private String mName;
	private Class<?> mClass;
	
	public PuzzleItem(int r, String n, Class<?> c){
		this.mResource = r;
		this.mName = n;
		this.mClass = c;
	}

	public int getResource() {
		return mResource;
	}

	public void setResource(int mResource) {
		this.mResource = mResource;
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public Class<?> getmClass() {
		return mClass;
	}

	public void setClass(Class<?> mClass) {
		this.mClass = mClass;
	}
	
	
}
