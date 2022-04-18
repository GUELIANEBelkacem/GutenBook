package com.gutenbook.gutenbook.Data;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.gutenbook.gutenbook.Search.Search;
import com.gutenbook.gutenbook.Tools.Graph;
import com.gutenbook.gutenbook.Tools.Matrix;
import com.gutenbook.gutenbook.Tools.Serialisation;

 public class Jaccard {
	
    public static void build(double jc) throws Exception {
    	createJaccardMatrix();
        HashMap<Integer, HashMap<Integer, Double>> matrix =  Serialisation.loadMatrix(new File(Search.path+"MatrixJaccard/matrix.txt"));
        Graph g = createVertexForAllIndexBooks(jc,matrix);
        Serialisation.storeGraph(g); 
    }
    
    public static Graph createVertexForAllIndexBooks(double constanteJaccard, HashMap<Integer, HashMap<Integer, Double>> cache) throws Exception {
        Graph graph = Graph.createIndexGraph();
        int cpt = 1;

        for(Map.Entry<Integer,HashMap<Integer,Double>> key: cache.entrySet()){

            for(Map.Entry<Integer,Double> value: key.getValue().entrySet()){
                if(key.getKey() !=  value.getKey() && value.getValue()<constanteJaccard){
                    graph.addEdge(key.getKey(), value.getKey());
                }

            }
            if(cpt%5==0) System.out.println("In Progress : "+cpt+"/"+cache.size());
            cpt++;
        }
        return graph;
    }

    

    public static double jaccardDistance(Set<String> set1, Set<String>  set2) {

  
        Set<String> intersection = set1.parallelStream()
                .collect(Collectors.toSet());;
        intersection.retainAll(set2);
        double union = set1.size() + set2.size() - intersection.size(); 
        return (union - intersection.size()) / union;
    }




    public static void createJaccardMatrix() throws Exception {
        int cpt = 1;
        File folder = new File(Search.path+"Maps");
        Matrix cache = new Matrix();
        for (final File f1 : folder.listFiles()) {
            
            int id = Integer.parseInt(f1.getName().replace(".map", "")); 
            Map<String,Integer> map1= Serialisation.loadData(f1);
            for (final File f2 : folder.listFiles()) {
                int id2 = Integer.parseInt(f2.getName().replace(".map", "")); 
                Map<String,Integer> map2=Serialisation.loadData(f2);
                if(cache.get(id,id2)== -1.0) {
                    double distanceJaccard = jaccardDistance(map1.keySet(),map2.keySet());
                    cache.add(id,id2,distanceJaccard);
                }
            }
            if(cpt%5==0)System.out.println("In Progress : " + cpt + "/"+folder.listFiles().length);
            cpt++;
        }
        Serialisation.storeMatrix(cache);
    }

}
