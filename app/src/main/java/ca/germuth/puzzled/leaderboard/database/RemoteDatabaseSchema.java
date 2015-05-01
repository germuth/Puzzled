package ca.germuth.puzzled.leaderboard.database;

import android.provider.BaseColumns;

class RemoteDatabaseSchema {
	public static abstract class PuzzleTable {
		public static final String TABLE_NAME = "Puzzle";
		public static final String COLUMN_ID = "puzzle_id";
		public static final String COLUMN_PUZZLE_NAME = "name";
	}

	public static abstract class SolveTable {
		public static final String TABLE_NAME = "Solve";
		public static final String COLUMN_ID = "solve_id";
		public static final String COLUMN_SOLVE_DURATION = "duration";
		public static final String COLUMN_REPLAY = "replay";
		public static final String COLUMN_SCRAMBLE = "scramble";
		public static final String COLUMN_PUZZLE = "puzzle_id";
		public static final String COLUMN_SOLVE_DATE = "date_solved";
		public static final String COLUMN_USER = "user_name";
	}
}
