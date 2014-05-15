package ca.germuth.puzzled.statistics.text;

import java.util.ArrayList;

import ca.germuth.puzzled.database.SolveDB;

public class TimesSolved {
	public static double getValues(ArrayList<SolveDB> solves)
			throws IllegalArgumentException {
		return solves.size();
	}
}
