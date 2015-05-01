package ca.germuth.puzzled.puzzle.minx;

import ca.germuth.puzzled.puzzle.Tile;

public class MinxCorner {
	Tile[][] corner;
	
	public MinxCorner(int size, Tile colour){
		corner = new Tile[size][size];
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				corner[i][j] = new Tile(colour.getRed(), colour.getGreen(), colour.getBlue(), colour.getAlpha());
			}
		}
	}
}
