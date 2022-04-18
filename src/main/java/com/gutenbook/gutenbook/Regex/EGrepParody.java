package com.gutenbook.gutenbook.Regex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import com.gutenbook.gutenbook.Regex.Tools.*;




public class EGrepParody {

	public Automate automate = null;
	public EGrepParody(String regex) {
		if(regex.length() == 0) System.out.println("[Err]: Empty RegEx");
		RegEx rxm;
        RegExTree ret;
        Automate a;
        

        
        try {
	    	rxm = new RegEx(regex);
	        ret = rxm.parse();
	        a = new Automate(ret);
	        automate = a;
        }catch(Exception e){
        	e.printStackTrace();
        }
	}
	public boolean search(String word) {
		if(word.length() == 0) return false;
		if(automate == null) {System.out.println("[Err]: Null Automaton"); return false;}
		if(automate.match(word)) System.out.println(word);
		return automate.match(word);
        
		
	}
	public static void main(String arg[]) {
	   String myFile = "sargon.txt";
	    String rx; 
	    RegEx rxm;
        RegExTree ret;
        Automate a;
        ArrayList<Integer> found ;
        KMP kmp;
        
        
	    if (arg.length!=0) {
	      rx = arg[0];
	    } else {
	      Scanner scanner = new Scanner(System.in);
	      System.out.print("  >> Please enter a regEx: ");
	      rx = scanner.next();
	      scanner.close();
	    }
	    
	    if (rx.length()<1) System.err.println("  >> ERROR: empty regEx.");
	    
	    boolean canKMP;
	    try {
	    	
	    if(rx.contains("*") || rx.contains("|")) { 
	    	canKMP = false;
	    	kmp = new KMP("empty");
	    	rxm = new RegEx(rx);
	        ret = rxm.parse();
	        a = new Automate(ret);
	    	}
	    else {
	    	canKMP = true;
	    	rx.replace(".", "");
	    	rx.replace("(", "");
	    	rx.replace(")", "");
	    	kmp = new KMP(rx);
	    	rxm = new RegEx(rx);
	        ret = rxm.parse();
	        a = new Automate(ret);
	    	
	    }
	    
	    //canKMP = false;
      
    	  
    	
        File file = new File("text/"+myFile);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String t;
        long startTime=0;
        long endTime=-1;
        long stime=0;
        
        int nline = 1;
        while ((t = br.readLine()) != null) {
	    
	    	if(canKMP) {
	    		
	    		startTime = System.nanoTime();
	    		found = kmp.search(t);
	    		endTime = System.nanoTime();
	    		stime+=endTime-startTime;
	    	}
	    	else {
		    	
		        startTime = System.nanoTime();
		        found = a.search(t);
		        endTime = System.nanoTime();
		        stime+=endTime-startTime;
	        }
	        
	        if(found.isEmpty()) {nline++; continue;}
	        else {
	        	String lindex = "["+nline+"]: ";
	        	System.out.println(lindex + t);
	        	String s = "";
	        	for(int i = 0; i<lindex.length(); i++) s+=" ";
	        	for(int i = 0; i<t.length(); i++) {
	        		if(found.contains(i)) s += "^";
	        		else s+= " ";
	        	}
	        	System.out.println(s);
	        	
	        }
	        nline++;
	        System.out.println("");
        }
	        
	    System.out.println("  >> Search Time: " +((int)(stime/1000000))+" ms");
        br.close();
        
      } catch (Exception e) {
        e.printStackTrace();
      }
    
	    
	    
	    System.out.println("  >> Search completed.");

	  }

}
