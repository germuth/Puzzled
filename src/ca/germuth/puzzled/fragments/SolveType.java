package ca.germuth.puzzled.fragments;

import android.os.Parcel;
import android.os.Parcelable;

public enum SolveType implements Parcelable {
	LOCAL, GLOBAL;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(ordinal());
	}

	public static final Creator<SolveType> CREATOR = new Creator<SolveType>() {
		@Override
		public SolveType createFromParcel(final Parcel source) {
			return SolveType.values()[source.readInt()];
		}

		@Override
		public SolveType[] newArray(final int size) {
			return new SolveType[size];
		}
	};
}
