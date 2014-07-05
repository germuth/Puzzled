package ca.germuth.puzzled.puzzle.minx;

import ca.germuth.puzzled.puzzle.Tile;
import ca.germuth.puzzled.puzzle.PuzzleFace;

public class MinxFace implements PuzzleFace{
	Tile center;
	/**
	 * Array of edges in clockwise fashion. Slighly
	 * right-most one is first.
	 * Edge is this sense is each of the 5 legs out of the center.
	 */
	MinxEdge[] edges;
	/**
	 * Array of corners in clockwise fashion. Top corner
	 * is first. 
	 * Corner is this sense is each nxn array that makes up the corner.
	 * on megaminx, it is 1x1, on gigaminx, 2x2
	 */
	MinxCorner[] corners;
	
	public MinxFace(int size, Tile colour){
		edges = new MinxEdge[size*5];
		for(int i = 0; i < edges.length; i++){
			edges[i] = new MinxEdge(size, colour);
		}
		corners = new MinxCorner[5];
		for(int i = 0; i < corners.length; i++){
			corners[i] = new MinxCorner(size, colour);
		}
		center = new Tile(colour.getRed(), colour.getGreen(), colour.getBlue(), colour.getAlpha());
	}
	
	@Override
	public void setSolved() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSolved() {
		// TODO Auto-generated method stub
		return false;
	}

}
