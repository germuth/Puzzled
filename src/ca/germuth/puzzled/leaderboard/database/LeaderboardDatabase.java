package ca.germuth.puzzled.leaderboard.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import net.sourceforge.jtds.jdbc.Driver;
import android.util.Log;
import ca.germuth.puzzled.leaderboard.RequestType;
import ca.germuth.puzzled.leaderboard.Solve;
import ca.germuth.puzzled.leaderboard.database.RemoteDatabaseSchema.PuzzleTable;
import ca.germuth.puzzled.leaderboard.database.RemoteDatabaseSchema.SolveTable;

public class LeaderboardDatabase{
	private static Connection ConnectToDatabase() {
		try {
			Driver.class.newInstance();

			Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
			String username = "PuzzledAndroidApp";
			String password = "puzzle";
			Connection DbConn = DriverManager
					.getConnection("jdbc:jtds:sqlserver://puzzleddatabase.cnessmlzxvbo.us-west-2.rds.amazonaws.com:1433;"
							+ "DatabaseName=Puzzled;user=" + username + ";password=" + password);
			return DbConn;
		} catch (Exception e) {
			Log.w("Error connection", "" + e.getMessage());
		}
		return null;
	}
	
	public static void submitSolve(Solve sol, String puzName){
		Connection DbConn = ConnectToDatabase();
		Boolean failure = false;
		try {
			//check of puzzle exists
			Statement stm = DbConn.createStatement();
			ResultSet rst = stm.executeQuery(
					" SELECT " + PuzzleTable.COLUMN_ID +
					" FROM " + PuzzleTable.TABLE_NAME +
					" WHERE " + PuzzleTable.COLUMN_PUZZLE_NAME + " = '" + puzName + "'");
			//if puzzle doesn't exist
			if( !rst.isBeforeFirst() ){
				//must insert puzzle
				stm = DbConn.createStatement();
				stm.execute(
						" INSERT INTO " + PuzzleTable.TABLE_NAME + " (" + PuzzleTable.COLUMN_PUZZLE_NAME + ") " +
						" VALUES ( '" + puzName + "')");
			}
			//now we know puzzle exists, can query for puzzle_id
			stm = DbConn.createStatement();
			rst = stm.executeQuery(
					" SELECT " + PuzzleTable.COLUMN_ID +
					" FROM " + PuzzleTable.TABLE_NAME +
					" WHERE " + PuzzleTable.COLUMN_PUZZLE_NAME + " = '" + puzName + "'");
			//move to first now
			rst.next();
			int puzzle_id = rst.getInt("puzzle_id"); 
			
			String scrambleEdit = sol.getScramble().replace("'", "''");
			String replayEdit = sol.getReplay().replace("'", "''");
			Statement stmt = DbConn.createStatement();
			String s = "INSERT INTO " + SolveTable.TABLE_NAME + " (" + 
					SolveTable.COLUMN_USER + ", " + SolveTable.COLUMN_SOLVE_DURATION + ", " +
					SolveTable.COLUMN_REPLAY + ", " + SolveTable.COLUMN_SCRAMBLE + ", " +
					SolveTable.COLUMN_SOLVE_DATE + ", " + SolveTable.COLUMN_PUZZLE + ") " +
			"VALUES ('" + 
					sol.getUsername() + "', " + sol.getDuration() + ", '" + 
					//sol.getReplay() + "'], ['" + sol.getScramble() + "'], " +
					replayEdit + "', '" + scrambleEdit + "', " + 
					sol.getDateSolved() + ", " + puzzle_id + " )";
			failure = stmt.execute(s);
					
			//returns false on successful insertion
			failure = !failure;
			
			DbConn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			failure = false;
		}
	}
	
	public static ArrayList<Solve> getAllSolves(int puzzle_id, RequestType type){
		ArrayList<Solve> solves = new ArrayList<Solve>();
		
		Connection DbConn = ConnectToDatabase();
		
		try {
			Statement stmt = DbConn.createStatement();
			String qry = 
					" SELECT * " + 
					" FROM " + SolveTable.TABLE_NAME + 
					" WHERE " + SolveTable.COLUMN_PUZZLE + " = " + puzzle_id;
			switch (type){
				//get all solves since a day ago
				case TODAY:
					Calendar c = Calendar.getInstance();
					c.add(Calendar.DAY_OF_YEAR, -1);
					long time = c.getTimeInMillis();
					qry += " AND " + SolveTable.COLUMN_SOLVE_DATE + ">= " + time; 
					break;
				//get all solves since week ago
				case THIS_WEEK:
					c = Calendar.getInstance();
					c.add(Calendar.WEEK_OF_YEAR, -1);
					time = c.getTimeInMillis();
					qry += " AND " + SolveTable.COLUMN_SOLVE_DATE + ">= " + time; 
					break;
				//get all solves ever
				case ALL_TIME:
					break;
			}
			qry += " ORDER BY " + SolveTable.COLUMN_SOLVE_DURATION + " ASC";
			ResultSet reset = stmt.executeQuery(qry);

			while (reset.next()) {
				Solve sol = new Solve();
				sol.setSolve_id( reset.getInt( SolveTable.COLUMN_ID));
				sol.setDuration( reset.getInt( SolveTable.COLUMN_SOLVE_DURATION));
				sol.setPuzzle_id( reset.getInt(SolveTable.COLUMN_PUZZLE));
				sol.setUsername( reset.getString(SolveTable.COLUMN_USER));
				sol.setDateSolved( reset.getLong(SolveTable.COLUMN_SOLVE_DATE));
				sol.setReplay( reset.getString(SolveTable.COLUMN_REPLAY));
				sol.setScramble( reset.getString(SolveTable.COLUMN_SCRAMBLE));
				solves.add(sol);
			}

			DbConn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return solves;
	}
}