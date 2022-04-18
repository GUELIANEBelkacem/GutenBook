package com.gutenbook.gutenbook.Search;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.gutenbook.gutenbook.Regex.EGrepParody;
import com.gutenbook.gutenbook.Tools.Pair;
import com.gutenbook.gutenbook.Tools.Serialisation;

public class Search{
	public static String path = Paths.get("").toAbsolutePath()+"/src/main/java/com/gutenbook/gutenbook/";

    
	//Simple Search function 
    public static ConcurrentHashMap<Integer,Integer> simpleSearch(String word) throws Exception {
        ConcurrentHashMap<Integer,Integer> books = new ConcurrentHashMap<Integer,Integer>(); 
        List<Thread> threads = new ArrayList<Thread>();
        File folder = new File (path+"Maps");
       
        for (final File indexBook : folder.listFiles()) {
            
            int id = Integer.parseInt(indexBook.getName().replace(".map","")); 
            threads.add(new Thread( new Runnable() {
                @Override
                public void run() {
                    HashMap<String,Integer> dic = null;
                    try {dic = Serialisation.loadData(indexBook);} catch (Exception e) {e.printStackTrace();} 
                    if(dic.containsKey(word)){
                    	//add the book if the map contains the word
                        books.put(id, dic.get(word));
                    }
            }}));

        }

        for(Thread t : threads){ t.start(); }
        for(Thread t : threads){ t.join(); }
        return books;
    }

    //Multiple Search function
    public static ConcurrentHashMap<Integer,Pair<Integer,Integer>> multipleSearch(List<String> words) throws Exception{
       
        ConcurrentHashMap<Integer,Pair<Integer,Integer>> books = new ConcurrentHashMap<Integer,Pair<Integer,Integer>>();
        List<Thread> threads = new ArrayList<Thread>();

        File folder = new File (path+"Maps");
        for (final File indexBook : folder.listFiles()) {
                int id = Integer.parseInt(indexBook.getName().replace(".map","")); 
               
                threads.add(new Thread( new Runnable() {
                    @Override
                    public void run() {
                        int nwords = 0; 
                        int total = 0;   
                        HashMap<String,Integer> dic = null;
                        try {
                            dic = Serialisation.loadData(indexBook);
                            for(String word: words){
                                if(dic.containsKey(word)){
                                    nwords++;
                                    total += dic.get(word);
                                }
                            }
                            if(nwords > 0) books.put(id, new Pair<>(nwords, total));

                        } catch (Exception e) {e.printStackTrace();} 
                    }
                }));
        }
    
        for(Thread t : threads){ t.start(); }
        for(Thread t : threads){ t.join(); }
        return books;
    }



    //RegEx Search
    public static ConcurrentHashMap<Integer,Pair<Integer,Integer>> regExSearch(String regex) throws Exception{
       
        ConcurrentHashMap<Integer,Pair<Integer,Integer>> books = new ConcurrentHashMap<Integer,Pair<Integer,Integer>>();
        //Projet1
        EGrepParody egrep = new EGrepParody(regex.toLowerCase());
        List<Thread> threads = new ArrayList<Thread>();

        File folder = new File (path+"Maps");
        for (final File indexBook : folder.listFiles()) {         
            int id = Integer.parseInt(indexBook.getName().replace(".map",""));
       
            threads.add(new Thread( new Runnable() {
                @Override
                public void run() {
                    HashMap<String,Integer> dic = null;
                    int nwords = 0;
                    int total = 0;

                    try {
                        dic = Serialisation.loadData(indexBook);
                        for(String word : dic.keySet()){
                            if(egrep.search(word)){
                                nwords ++;
                                total += dic.get(word);
                            }
                        }
                        //add if there is a match
                        if(nwords > 0) books.put(id, new Pair<>(nwords, total));
                        

                    } catch (Exception e) {e.printStackTrace();}
                }}));           
        }
        for(Thread t : threads){ t.start(); }
        for(Thread t : threads){ t.join(); }
        return books;
    }



    public static Set<Integer> recommendations(Set<Integer> books) throws Exception { 
        Map<Integer,Set<Integer>> g = Serialisation.loadGraph( new File(path+"Graph/graph.txt"));
        Set<Integer> s=new HashSet<>();
        for(Integer book : books){
        	Set<Integer> temp = g.get(book);
        	for(Integer sug:temp) {
        		if(!books.contains(sug)) s.add(sug);
        	}
        }
        return s;
    }


    public static LinkedHashMap<Integer, Integer> SortedMapDescending(Map<Integer, Integer> map){
     
        LinkedHashMap<Integer, Integer> sortedMap = new LinkedHashMap<>();
        map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        return sortedMap;
    }

 
    public static LinkedHashMap<Integer, Pair<Integer, Integer>> sortedBooksFromKeywords(ConcurrentHashMap<Integer,Pair<Integer,Integer>> books){
     
        LinkedHashMap<Integer, Pair<Integer, Integer>> sortedMap;
        sortedMap = books.entrySet()
                .stream()
                .sorted((b1,b2) -> compare(b1.getValue(),b2.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return sortedMap;
    }

 
    private static int compare(Pair<Integer,Integer> p1, Pair<Integer,Integer> p2) {
        return p1.getKey() == p2.getKey()? p2.getValue() - p1.getValue() : p2.getKey() - p1.getKey();
    }


}
