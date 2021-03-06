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
    public static final int DATABASE_VERSION = 4;
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
    	values.put(SolveTable.COLUMN_PUZZLE, solve.getPuzzle().getmId());
    	values.put(SolveTable.COLUMN_REPLAY, solve.getReplay());
    	values.put(SolveTable.COLUMN_SCRAMBLE, solve.getScramble());
    	values.put(SolveTable.COLUMN_SOLVE_DATE, solve.getDateSolved());
    	values.put(SolveTable.COLUMN_SOLVE_DURATION, solve.getDuration());
		values.put(SolveTable.COLUMN_FINISHED, solve.isFinished() ? 1 : 0);

    	// Insert the new row, returning the primary key value of the new row
    	db.insert(SolveTable.TABLE_NAME, null, values);
    }
    
    public void deleteSolve(SolveDB solve){
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	if( solve.getLocalId() != 0){
    		db.delete(SolveTable.TABLE_NAME, SolveTable._ID + " =?", new String[]{solve.getLocalId() + ""});
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
    
    public String getPuzzleName(SolveDB sol){
    	SQLiteDatabase db = this.getWritableDatabase();
    	Cursor c = db.rawQuery(
    			"SELECT * " +
    			"FROM " + PuzzleTable.TABLE_NAME + " " +
    			"WHERE " + PuzzleTable._ID + " = " + sol.getPuzzle().getmId(), null);
    	c.moveToFirst();
    	return c.getString(c.getColumnIndex(PuzzleTable.COLUMN_PUZZLE_NAME));
    }

	public PuzzleDB getPuzzleByName(String name){
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c = db.rawQuery(
				" SELECT * " +
				" FROM " + PuzzleTable.TABLE_NAME +
				" WHERE " + PuzzleTable.COLUMN_PUZZLE_NAME + " = " + name, null );
		if( c.moveToLast() ){
			int id = c.getInt(c.getColumnIndex(PuzzleTable._ID));

			PuzzleDB puz = new PuzzleDB(id, name);
			return puz;
		}

		return null;
	}

	public PuzzleDB getPuzzleByID(int ID){
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c = db.rawQuery(
				" SELECT * " +
						" FROM " + PuzzleTable.TABLE_NAME +
						" WHERE " + PuzzleTable._ID + " = " + ID, null );
		if( c.moveToLast() ){
			String name = c.getString(c.getColumnIndex(PuzzleTable.COLUMN_PUZZLE_NAME));

			PuzzleDB puz = new PuzzleDB(ID, name);
			return puz;
		}

		return null;
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
    		String scramble = c.getString(c.getColumnIndex(SolveTable.COLUMN_SCRAMBLE));
    		long dateTime = c.getLong(c.getColumnIndex(SolveTable.COLUMN_SOLVE_DATE));
    		int duration = c.getInt(c.getColumnIndex(SolveTable.COLUMN_SOLVE_DURATION));
    		int puzzleId = c.getInt(c.getColumnIndex(SolveTable.COLUMN_PUZZLE));
			int finished = c.getInt(c.getColumnIndex(SolveTable.COLUMN_FINISHED));
    		
    		Cursor c2 = db.rawQuery(
    				" SELECT * " +
    				" FROM " + PuzzleTable.TABLE_NAME +
    				" WHERE " + PuzzleTable._ID + " =? ",
    				new String[]{ puzzleId + "" });
    		if( c2.moveToFirst() ){
    			PuzzleDB puz = new PuzzleDB(c2.getInt(c2.getColumnIndex(PuzzleTable._ID)),
        				c2.getString(c2.getColumnIndex(PuzzleTable.COLUMN_PUZZLE_NAME)));  
    			SolveDB SolveTable = new SolveDB(duration, replay, scramble, puz, dateTime, finished == 1 ? true : false);
    			SolveTable.setLocalId(SolveTableID);
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
        		String scramble = c.getString(c.getColumnIndex(SolveTable.COLUMN_SCRAMBLE));
        		long dateTime = c.getLong(c.getColumnIndex(SolveTable.COLUMN_SOLVE_DATE));
        		int duration = c.getInt(c.getColumnIndex(SolveTable.COLUMN_SOLVE_DURATION));
				int finished = c.getInt(c.getColumnIndex(SolveTable.COLUMN_FINISHED));
        		
        		SolveDB SolveTable = new SolveDB(duration, replay, scramble, puz, dateTime, finished == 1 ? true : false);
        		SolveTable.setLocalId(SolveTableID);
        		SolveTables.add(SolveTable);
        	}
    	}
    	
    	return SolveTables;
    }

	/**
	 * Returns an unfinished solve for the given puzzle. Right now it is limited to only
	 * save one partial solve
	 * @return
	 */
	public SolveDB getUnfinishedSolve(){//PuzzleDB puzzle){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(
				" SELECT * " +
						" FROM " + SolveTable.TABLE_NAME +
//						" WHERE " + SolveTable.COLUMN_PUZZLE  + " = ?" +
						//only want unfinished solve
						" WHERE " + SolveTable.COLUMN_FINISHED + " = 0" +
						//most recent solves last
						" ORDER BY " + SolveTable.COLUMN_SOLVE_DATE + " ASC",

				null);//new String[] { puzzle.getmId() + "" });
		if( c.moveToLast() ){
				int SolveTableID = c.getInt(c.getColumnIndex(SolveTable._ID));
				String replay = c.getString(c.getColumnIndex(SolveTable.COLUMN_REPLAY));
				String scramble = c.getString(c.getColumnIndex(SolveTable.COLUMN_SCRAMBLE));
				long dateTime = c.getLong(c.getColumnIndex(SolveTable.COLUMN_SOLVE_DATE));
				int duration = c.getInt(c.getColumnIndex(SolveTable.COLUMN_SOLVE_DURATION));
				int finished = c.getInt(c.getColumnIndex(SolveTable.COLUMN_FINISHED));
				int puzID = c.getInt(c.getColumnIndex(SolveTable.COLUMN_PUZZLE));

				SolveDB SolveTable = new SolveDB(duration, replay, scramble, this.getPuzzleByID(puzID), dateTime,
						finished == 1 ? true : false);
				SolveTable.setLocalId(SolveTableID);
				return SolveTable;
		}
		return null;
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
