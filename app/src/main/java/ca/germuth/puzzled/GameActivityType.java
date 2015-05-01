package ca.germuth.puzzled;

import android.os.Parcel;
import android.os.Parcelable;

public enum GameActivityType implements Parcelable{
	PLAY, REPLAY;
	

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(ordinal());
    }

    public static final Creator<GameActivityType> CREATOR = new Creator<GameActivityType>() {
        @Override
        public GameActivityType createFromParcel(final Parcel source) {
            return GameActivityType.values()[source.readInt()];
        }

        @Override
        public GameActivityType[] newArray(final int size) {
            return new GameActivityType[size];
        }
    };
}
