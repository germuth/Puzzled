package ca.germuth.puzzled.statistics;

import java.util.ArrayList;

public interface StatisticsMeasure {
	
	abstract ArrayList<Integer> getValues();
	
	abstract ArrayList<String> getNames();
}
