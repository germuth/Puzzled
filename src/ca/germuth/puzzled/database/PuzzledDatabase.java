package ca.germuth.puzzled.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import ca.germuth.puzzled.database.DatabaseSchema.PuzzleTable;
import ca.germuth.puzzled.database.DatabaseSchema.SolveTable;
import ca.germuth.puzzled.puzzle.Puzzle;

public class PuzzledDatabase extends SQLiteOpenHelper{
	// If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    public PuzzledDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PuzzleTable.SQL_CREATE_TABLE);
        db.execSQL(SolveTable.SQL_CREATE_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + PuzzleTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SolveTable.TABLE_NAME);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    
    public void insertPuzzle(PuzzleDB puz){
    	SQLiteDatabase db = this.getWritableDatabase();

    	// Create a new map of values, where column names are the keys
    	ContentValues values = new ContentValues();
    	values.put(PuzzleTable._ID, puz.getmId());
    	values.put(PuzzleTable.COLUMN_PUZZLE_NAME, puz.getmName());

    	// Insert the new row, returning the primary key value of the new row
    	db.insert(PuzzleTable.TABLE_NAME, null, values);
    }
    
    public void insertSolve(SolveDB solve){
    	SQLiteDatabase db = this.getWritableDatabase();

    	// Create a new map of values, where column names are the keys
    	ContentValues values = new ContentValues();
    	//values.put(SolveTable._ID, solve.getmId());
    	values.put(SolveTable.COLUMN_PUZZLE, solve.getmPuzzle().getmId());
    	values.put(SolveTable.COLUMN_REPLAY, solve.getmReplay());
    	values.put(SolveTable.COLUMN_SOLVE_DATE, solve.getmDateTime());
    	values.put(SolveTable.COLUMN_SOLVE_DURATION, solve.getmDuration());

    	// Insert the new row, returning the primary key value of the new row
    	db.insert(SolveTable.TABLE_NAME, null, values);
    }
    
    public void deleteSolve(SolveDB solve){
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	if( solve.getmId() != 0){
    		db.delete(SolveTable.TABLE_NAME, SolveTable._ID + " =?", new String[]{solve.getmId() + ""});
    	}else{
    		Log.wtf("DATABASE", "CANNOT delete solve without its id bro");
    	}
    }
    
    public void deleteAllSolves(PuzzleDB puz){
    	SQLiteDatabase db = this.getWritableDatabase();
    	if( puz != null){
    		db.execSQL(
    				"DELETE FROM " + 
    				SolveTable.TABLE_NAME + 
    				" WHERE " + SolveTable.COLUMN_PUZZLE + " = " + puz.getmId()
    				);
    	}else{
    		db.delete(SolveTable.TABLE_NAME, null, null);
    	}
    }
    
    public SolveDB getLastSolve(){
    	SQLiteDatabase db = this.getWritableDatabase();
    	Cursor c = db.rawQuery(
    			" SELECT * " + 
    			" FROM " + SolveTable.TABLE_NAME +
    			//most recent solves last
    			" ORDER BY " + SolveTable.COLUMN_SOLVE_DATE + " ASC", 
    			
    			null );
    	if( c.moveToLast() ){
    		int SolveTableID = c.getInt(c.getColumnIndex(SolveTable._ID));
    		String replay = c.getString(c.getColumnIndex(SolveTable.COLUMN_REPLAY));
    		long dateTime = c.getLong(c.getColumnIndex(SolveTable.COLUMN_SOLVE_DATE));
    		int duration = c.getInt(c.getColumnIndex(SolveTable.COLUMN_SOLVE_DURATION));
    		int puzzleId = c.getInt(c.getColumnIndex(SolveTable.COLUMN_PUZZLE));
    		
    		Cursor c2 = db.rawQuery(
    				" SELECT * " +
    				" FROM " + PuzzleTable.TABLE_NAME +
    				" WHERE " + PuzzleTable._ID + " =? ",
    				new String[]{ puzzleId + "" });
    		if( c2.moveToFirst() ){
    			PuzzleDB puz = new PuzzleDB(c2.getInt(c2.getColumnIndex(PuzzleTable._ID)),
        				c2.getString(c2.getColumnIndex(PuzzleTable.COLUMN_PUZZLE_NAME)));  
    			SolveDB SolveTable = new SolveDB( SolveTableID, duration, replay, puz, dateTime);
        		return SolveTable;
    		}else{
    			return null;
    		}
    	}else{
    		return null;
    	}
    }
    /**
     * Returns all solves for a given puzzle. Solves are ordered by date, 
     * with more recent solves appearing later in the list
     * If PuzzleTable is null, will return for all PuzzleTables
     * @param puz
     * @return
     */
    public ArrayList<SolveDB> getAllSolves(PuzzleDB puz){
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	ArrayList<SolveDB> SolveTables = new ArrayList<SolveDB>();
    	
    	Cursor c = db.rawQuery(
    			" SELECT * " + 
    			" FROM " + SolveTable.TABLE_NAME +
    			" WHERE " + SolveTable.COLUMN_PUZZLE  + " = ?" + 
    			//most recent solves last
    			" ORDER BY " + SolveTable.COLUMN_SOLVE_DATE + " ASC", 
    			
    			new String[] { puz.getmId() + "" });
    	if( c.moveToFirst() ){
    		while(c.moveToNext()){
        		int SolveTableID = c.getInt(c.getColumnIndex(SolveTable._ID));
        		String replay = c.getString(c.getColumnIndex(SolveTable.COLUMN_REPLAY));
        		long dateTime = c.getLong(c.getColumnIndex(SolveTable.COLUMN_SOLVE_DATE));
        		int duration = c.getInt(c.getColumnIndex(SolveTable.COLUMN_SOLVE_DURATION));
        		
        		SolveDB SolveTable = new SolveDB( SolveTableID, duration, replay, puz, dateTime);
        		SolveTables.add(SolveTable);
        	}
    	}
    	
    	return SolveTables;
    }
    
    public PuzzleDB convert(Puzzle p){
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	Cursor c = db.rawQuery(
    			" SELECT * " + 
    			" FROM " + PuzzleTable.TABLE_NAME +
    			" WHERE " + PuzzleTable.COLUMN_PUZZLE_NAME  + " = ?", 
    			
    			new String[] { p.getName() + "" });
    	
    	if( c.moveToFirst() ){
    		return new PuzzleDB(c.getInt(c.getColumnIndex(PuzzleTable._ID)),
    				c.getString(c.getColumnIndex(PuzzleTable.COLUMN_PUZZLE_NAME)));    		
    	}else{
    		ContentValues values = new ContentValues();
    		values.put(PuzzleTable.COLUMN_PUZZLE_NAME, p.getName());
    		db.insert(PuzzleTable.TABLE_NAME, null, values);
    		
    		return convert(p);
    	}
    }
}
