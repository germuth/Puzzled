package ca.germuth.puzzled.leaderboard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
	
	//check for puz name
	//if exists query for puz id and insert solve
	//if not exists, insert puz, then query for puz id and insert solve
	public static void submitSolve(Solve sol, String puzName){
		Connection DbConn = ConnectToDatabase();
		Boolean failure = false;
		try {
			//check of puzzle exists
			Statement stm = DbConn.createStatement();
			ResultSet rst = stm.executeQuery(
					"SELECT puzzle_id " +
					"FROM Puzzle " +
					"WHERE name = '" + puzName + "'");
			//if puzzle doesn't exist
			if( !rst.isBeforeFirst() ){
				//puzzle hasn't been found
				//must insert puzzle
				stm = DbConn.createStatement();
				stm.execute(
						"INSERT INTO Puzzle (name) " +
						"VALUES ( '" + puzName + "')");
			}
			
			//query puzzle for puzzle id
			stm = DbConn.createStatement();
			rst = stm.executeQuery(
					"SELECT puzzle_id " +
					"FROM Puzzle " +
					"WHERE name = '" + puzName + "'");
			//move to first now
			rst.next();
			int puzzle_id = rst.getInt("puzzle_id"); 
			
			//actually insert the solve
			Statement stmt = DbConn.createStatement();
			failure = stmt.execute(
					"INSERT INTO Solve (username, duration, puzzle_id) " +
					"VALUES ('" + sol.getUsername() + "', " + sol.getDuration() + ", " + puzzle_id + " )");
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
			ResultSet reset = stmt.executeQuery("SELECT * FROM Solve WHERE puzzle_id = " + puzzle_id);

			while (reset.next()) {
				Solve sol = new Solve();
				sol.setSolve_id( reset.getInt("solve_id"));
				sol.setDuration( reset.getInt("duration"));
				sol.setPuzzle_id( reset.getInt("puzzle_id"));
				sol.setUsername( reset.getString("username"));
				solves.add(sol);
			}

			DbConn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return solves;
	}
}