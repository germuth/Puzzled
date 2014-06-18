package ca.germuth.puzzled.leaderboard;

public class Solve{
	private int solve_id;
	private String username;
	private int duration;
	private int puzzle_id;
	private long dateSolved;
	private String scramble;
	private String replay;
	
	public Solve(String username, int duration, long dateSolved, String scramble, String replay, int puz_id){
		this.username = username;
		this.dateSolved = dateSolved;
		this.duration = duration;
		this.scramble = scramble;
		this.replay = replay;
		this.puzzle_id = puz_id;
	}
	
	public Solve() {
		// TODO Auto-generated constructor stub
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

	public long getDateSolved() {
		return dateSolved;
	}

	public void setDateSolved(long dateSolved) {
		this.dateSolved = dateSolved;
	}

	public String getScramble() {
		return scramble;
	}

	public void setScramble(String scramble) {
		this.scramble = scramble;
	}

	public String getReplay() {
		return replay;
	}

	public void setReplay(String replay) {
		this.replay = replay;
	}
}
