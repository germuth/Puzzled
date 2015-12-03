package ca.germuth.puzzled.database;

import android.provider.BaseColumns;

class DatabaseSchema {
	
	/* Inner class that defines the table contents */
	//BaseColumn provides _id column for primary key
    public static abstract class PuzzleTable implements BaseColumns {
        public static final String TABLE_NAME = "puzzle";
        public static final String COLUMN_PUZZLE_NAME = "name";
        
        protected static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                PuzzleTable._ID + " integer primary key autoincrement," + 
                COLUMN_PUZZLE_NAME + " varchar(50) not null );";
    }
    
    public static abstract class SolveTable implements BaseColumns {
    	public static final String TABLE_NAME = "solve";
    	public static final String COLUMN_SOLVE_DURATION = "duration";
    	public static final String COLUMN_REPLAY = "replay";
    	public static final String COLUMN_SCRAMBLE = "scramble";
    	public static final String COLUMN_PUZZLE = "puzzle";
    	public static final String COLUMN_SOLVE_DATE = "time";
		public static final String COLUMN_FINISHED = "finished";
    	//TODO make protected
    	public static final String SQL_CREATE_TABLE = 
    			"CREATE TABLE " + TABLE_NAME + " (" + 
    			SolveTable._ID + " integer primary key autoincrement," + 
    			COLUMN_SOLVE_DURATION + " integer," + 
    			COLUMN_REPLAY + " TEXT," + 
    			COLUMN_SCRAMBLE + " TEXT, " +
    			COLUMN_PUZZLE + " integer," + 
    			//milliseconds since 1970 or System.currentTimeMillis()
    			//stores both date and time of finishing the solve
    			//to retrieve go 
    			//	Date date=new Date(millis);
    			//  convert to SimpleDateFormat
    			//  bam you can prompt for day, year, month, etc
    			COLUMN_SOLVE_DATE + " integer," +
				COLUMN_FINISHED + " integer, " +
    			"FOREIGN KEY(" + COLUMN_PUZZLE + ") REFERENCES " + PuzzleTable.TABLE_NAME + "(" + PuzzleTable._ID + ") );";
    }

}
