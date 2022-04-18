package com.gutenbook.gutenbook.Tools;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class Graph {
	private static String absolutePathFile = Paths.get("").toAbsolutePath() + "/src/main/java/com/gutenbook/gutenbook/";

	private static Map<Integer, Set<Integer>> neighbours = new HashMap<>();

	Map<Integer, Integer> prvIndex = new HashMap<>();
	Map<Integer, Integer> futureIndex = new HashMap<>();

	public Graph(Map<Integer, Set<Integer>> neighbours) {

	}

	public static Graph createIndexGraph() throws Exception {
		File folder = new File(absolutePathFile + "Maps");
		for (final File indexBook : folder.listFiles()) {

			int id = Integer.parseInt(indexBook.getName().replace(".map", ""));
			neighbours.put(id, new HashSet<>());

		}

		return new Graph(neighbours);
	}

	public void addEdge(int i, int j) {
		neighbours.get(i).add(j);
		neighbours.get(j).add(i);
	}

	public void addEdgeDirected(int i, int j) {
		neighbours.get(i).add(j);
	}

	public int degree(int n) {
		return neighbours.get(n).size();
	}

	public Set<Integer> neighboors(int n) {
		return neighbours.get(n);
	}

	public int size() {
		return neighbours.size();
	}

	public Map<Integer, Set<Integer>> getNeighbours() {
		return neighbours;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<Integer, Set<Integer>> v : neighbours.entrySet()) {
			sb.append(v.getKey() + " [");
			for (Integer e : v.getValue()) {
				sb.append(e + " ");
			}
			sb.append("]\n");
		}
		return sb.toString();
	}

}
