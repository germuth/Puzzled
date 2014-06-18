package ca.germuth.puzzled.leaderboard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import ca.germuth.puzzled.leaderboard.RemoteDatabaseSchema.PuzzleTable;
import ca.germuth.puzzled.leaderboard.RemoteDatabaseSchema.SolveTable;
import net.sourceforge.jtds.jdbc.Driver;
import android.util.Log;

public class LeaderboardRequest{
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
	
	public static ArrayList<Solve> getAllSolves(int puzzle_id){
		ArrayList<Solve> solves = new ArrayList<Solve>();
		
		Connection DbConn = ConnectToDatabase();
		
		try {
			Statement stmt = DbConn.createStatement();
			ResultSet reset = stmt.executeQuery(
					" SELECT * " + 
				    " FROM " + SolveTable.TABLE_NAME + 
				    " WHERE " + SolveTable.COLUMN_PUZZLE + " = " + puzzle_id);

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