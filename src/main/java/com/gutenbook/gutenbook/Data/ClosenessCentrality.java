package com.gutenbook.gutenbook.Data;

import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.gutenbook.gutenbook.Search.Search;
import com.gutenbook.gutenbook.Tools.Matrix;
import com.gutenbook.gutenbook.Tools.Serialisation;

public class ClosenessCentrality {

	private static double closenessCentrality(Map<Integer,Set<Integer>> graph, HashMap<Integer, HashMap<Integer, Double>> matrix, int node)throws Exception {

        double sum =0.0;
        for(int book : graph.keySet()){ if(!graph.get(node).isEmpty()) sum=matrix.get(node).get(book);}
        return 1/sum;

	}

		   
    public static  LinkedHashMap<Integer,Double> rank(Set<Integer> books) throws Exception {

        LinkedHashMap<Integer,Double> ranks = new LinkedHashMap<Integer, Double>();
        Map<Integer,Set<Integer>> graph = Serialisation.loadGraph(new File(Search.path+"Graph/graph.txt"));
        HashMap<Integer, HashMap<Integer, Double>> matrixJaccard = Matrix.loadMatrix();
        for(Integer book : books){
            double sum =closenessCentrality(graph,matrixJaccard,book);
            if (sum!=0) ranks.put(book,1/sum);
            else ranks.put(book,0.0); 
        }

        LinkedHashMap<Integer, Double> reverseSortedMap = new LinkedHashMap<>();
        ranks.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

        return reverseSortedMap;
    }
}
