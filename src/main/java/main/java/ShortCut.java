package main.java;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class ShortCut {
	
	
	private ShortCut() {
	}
	
public static ArrayList<Vertex> run(LinkedList<Vertex> tour) {
	return new ArrayList<>(tour)
			.stream()
			.distinct() //for duplicated elements, the element appearing first in the encounter order is preserved.
			.collect(Collectors.toCollection(ArrayList::new));
	}
}
