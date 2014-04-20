package ca.germuth.puzzled.puzzles;

import java.util.ArrayList;
import java.util.Random;

public class RandomMoveScrambler implements Scrambler{

	@Override
	public PuzzleTurn[] scramble(Puzzle puz, int scramble_length) {
		ArrayList<PuzzleTurn> primaryTurns = puz.getPrimaryMoves();
		int numberOfMoves = primaryTurns.size();
		
		Random r = new Random();
		
		PuzzleTurn[] scramble = new PuzzleTurn[scramble_length];
		for(int i = 0; i < scramble_length; i++){
			scramble[i] = primaryTurns.get( r.nextInt(numberOfMoves) );
		}
		
		return scramble;
	}

}
