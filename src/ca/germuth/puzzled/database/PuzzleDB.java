package ca.germuth.puzzled.database;

import android.os.Parcel;
import android.os.Parcelable;

public class PuzzleDB extends ObjectDB implements Parcelable{
	private int mId;
	private String mName;
	
	public PuzzleDB(int id, String name){
		this.mId = id;
		this.mName = name;
	}

	public int getmId() {
		return mId;
	}

	public void setmId(int mId) {
		this.mId = mId;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}
	
	  public PuzzleDB(Parcel in){
          this.mId = in.readInt();
          this.mName = in.readString();
    }

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mId);
		dest.writeString(mName);
		
	}
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public PuzzleDB createFromParcel(Parcel in) {
            return new PuzzleDB(in); 
        }

        public PuzzleDB[] newArray(int size) {
            return new PuzzleDB[size];
        }
    };
	
	
}
