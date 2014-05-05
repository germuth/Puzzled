package ca.germuth.puzzled.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import ca.germuth.puzzled.database.DatabaseSchema.Puzzle;
import ca.germuth.puzzled.database.DatabaseSchema.Solve;

public class PuzzledDatabase extends SQLiteOpenHelper{
	// If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    public PuzzledDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Puzzle.SQL_CREATE_TABLE);
        db.execSQL(Solve.SQL_CREATE_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + Puzzle.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Solve.TABLE_NAME);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    
    public void insertPuzzle(PuzzleDB puz){
    	SQLiteDatabase db = this.getWritableDatabase();

    	// Create a new map of values, where column names are the keys
    	ContentValues values = new ContentValues();
    	values.put(Puzzle._ID, puz.getmId());
    	values.put(Puzzle.COLUMN_PUZZLE_NAME, puz.getmName());

    	// Insert the new row, returning the primary key value of the new row
    	db.insert(Puzzle.TABLE_NAME, null, values);
    }
    
    public void insertSolve(SolveDB solve){
    	SQLiteDatabase db = this.getWritableDatabase();

    	// Create a new map of values, where column names are the keys
    	ContentValues values = new ContentValues();
    	values.put(Solve._ID, solve.getmId());
    	values.put(Solve.COLUMN_PUZZLE, solve.getmPuzzle().getmId());
    	values.put(Solve.COLUMN_REPLAY, solve.getmReplay());
    	values.put(Solve.COLUMN_SOLVE_DATE, solve.getmDateTime());
    	values.put(Solve.COLUMN_SOLVE_TIME, solve.getmDuration());

    	// Insert the new row, returning the primary key value of the new row
    	db.insert(Solve.TABLE_NAME, null, values);
    }
}
