package ca.germuth.puzzled.leaderboard;

public class Solve{
	private int solve_id;
	private String username;
	private int duration;
	private int puzzle_id;
	
	public Solve(){
		
	}
	
	public int getSolve_id() {
		return solve_id;
	}
	public void setSolve_id(int solve_id) {
		this.solve_id = solve_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getPuzzle_id() {
		return puzzle_id;
	}
	public void setPuzzle_id(int puzzle_id) {
		this.puzzle_id = puzzle_id;
	}
}
