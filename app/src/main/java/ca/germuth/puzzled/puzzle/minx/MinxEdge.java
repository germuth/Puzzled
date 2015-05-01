package ca.germuth.puzzled.puzzle.minx;

import ca.germuth.puzzled.puzzle.Tile;

public class MinxEdge {
	Tile[] edge;
	
	public MinxEdge(int size, Tile colour){
		edge = new Tile[size];
		for(int i = 0; i < size; i++){
			edge[i] = new Tile(colour.getRed(), colour.getGreen(), colour.getBlue(), colour.getAlpha());
		}
	}
}
