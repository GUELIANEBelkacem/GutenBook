package com.gutenbook.gutenbook.models;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.json.JSONObject;

import com.gutenbook.gutenbook.Data.ClosenessCentrality;
import com.gutenbook.gutenbook.Search.Search;
import com.gutenbook.gutenbook.Tools.Pair;




public class GutenProvider {
	   public static void main(String[] args) throws Exception {
	    }

	    
	   public static JSONObject simpleSearch(String word) throws Exception {
	     
		   
		    double before = System.currentTimeMillis();
	        Map<Integer,Integer> map = Search.simpleSearch(word.toLowerCase());
	        JSONObject res = (JSONObject) new JSONObject().put("books",Search.SortedMapDescending(map).keySet());
	        double after = System.currentTimeMillis();
	        double temps = after-before;
	        System.out.println("Simple Search Time : "+temps/1000);
	        
	        before = System.currentTimeMillis();
	        res.put("suggestions", recommendations(rank(map.keySet()).keySet()));
	        after = System.currentTimeMillis();
	        temps = after-before;
	        System.out.println("Simple Search Reccomendation + ranking Time : "+temps/1000);
	        //res.put("suggestions", recommendations(map.keySet()));
	        return res;
	    }

	  
	    public static JSONObject multipleSearch(List<String> words) throws Exception {
	        if(words.isEmpty()){
	            return (JSONObject) new JSONObject().put("error","empty key word");
	        }
	        ConcurrentHashMap<Integer, Pair<Integer,Integer>> map;
	        double before = System.currentTimeMillis();
	        map = Search.multipleSearch(words.stream().map(String::toLowerCase).collect(Collectors.toList()));
	        JSONObject res = (JSONObject) new JSONObject().put("books",Search.sortedBooksFromKeywords(map).keySet());
	        double after = System.currentTimeMillis();
	        double temps = after-before;
	        System.out.println("Multiple Search Time : "+temps/1000);
	        
	        before = System.currentTimeMillis();
	        res.put("suggestions", recommendations(rank(map.keySet()).keySet()));
	        after = System.currentTimeMillis();
	        temps = after-before;
	        System.out.println("Multiple Search Reccomendation + ranking Time : "+temps/1000);
	        return res;
	    }

	
	    public static JSONObject regExSearch(String regex) throws Exception {
	    	
	    	
	        if(regex.isEmpty()){
	            return (JSONObject) new JSONObject().put("error","empty regex");
	        }
	     
	        if(regex.contains("|") || regex.contains("*") || regex.contains("+")){
	            ConcurrentHashMap<Integer, Pair<Integer,Integer>> books;
	            
	            double before = System.currentTimeMillis();
	            books = Search.regExSearch(regex.toLowerCase());
	            JSONObject res= (JSONObject) new JSONObject().put("books",Search.sortedBooksFromKeywords(books).keySet());
	            double after = System.currentTimeMillis();
		        double temps = after-before;
		        System.out.println("RegEx Search Time : "+temps/1000);
		        
		        before = System.currentTimeMillis();
	            res.put("suggestions", recommendations(rank(books.keySet()).keySet()));
	            after = System.currentTimeMillis();
		        temps = after-before;
		        System.out.println("RegEx Search Reccomendation + ranking Time : "+temps/1000);
	            return res;
	        }
	        
	        regex = regex.replaceAll("[.()]", "");

	        return simpleSearch(regex.toLowerCase());
	    }

	 
	    public static Set<Integer> recommendations(Set<Integer> books) throws Exception {
	        return Search.recommendations(books);
	    }

	    public static Map<Integer,Double> rank(Set<Integer> bookIDs) throws Exception {
	        return ClosenessCentrality.rank(bookIDs);
	    }
	    
}
