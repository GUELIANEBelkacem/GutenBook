package com.gutenbook.gutenbook.Data;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gutenbook.gutenbook.Search.Search;
import com.gutenbook.gutenbook.Tools.Pair;
import com.gutenbook.gutenbook.Tools.Serialisation;

public class Indexation {
	

    public static void index() throws Exception {

    	//index books
        indexBookDatabase();

        //create maps of the index
        createIndexMapToFile();
    }

    public static void indexBookDatabase() throws Exception {
        File folder = new File (Search.path+"Books");
        System.out.println(folder.toString());
        int cpt= 1;
        for (final File indexBook : folder.listFiles()) {
        	if(cpt%5==0)System.out.println("In Progress : "+cpt+"/"+folder.listFiles().length);
            cpt++;
            if (indexBook.isDirectory()) {
                throw new Exception("[ERR] "+Search.path+"Books");
            } else {
                int id = Integer.parseInt(indexBook.getName().replace(".txt",""));
                indexBookToFile(id);
            }
        }

    }

    public static void indexBookToFile(int id) throws IOException {
        Map<String,Pair<Integer, Integer>> index = new HashMap<String,Pair<Integer, Integer>>();

        File book = new File(Search.path+"Books/"+id+".txt");
        Scanner readbook = new Scanner(book);
        while (readbook.hasNext()) {
            String mot =readbook.next().replaceAll("\\p{Punct}", "")
                    .replaceAll("[\"\']", "").toLowerCase();
            if(mot.isEmpty())
                continue;
            Pair<Integer, Integer> myword;

            if(index.containsKey(mot)){

                Pair<Integer, Integer> theword = index.get(mot);
                int currentOccurence= theword.getValue()+1; 
                myword = new Pair<Integer, Integer>(id, currentOccurence);
            }
            else {
                myword = new Pair<Integer,Integer>(id,1);
            }
            index.put(mot,myword);
        }

        readbook.close();
        writeIntoFile(index,id);

    }





    private static void writeIntoFile(Map<String,Pair<Integer, Integer>> index, int id)
            throws IOException
    {
        File theDir = new File(Search.path+"Index");
        if (!theDir.exists()){
            theDir.mkdirs();
        }

     
        FileWriter writer = new FileWriter(Search.path+"Index/"+id+".dex");
        index.forEach((k,v) -> {
            String key = k;
            int occurence = v.getValue();
            try {
                writer.write(key + " : [" + id + " : " + occurence + "]" + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        writer.close();
    }


    public static void createIndexMapToFile() throws Exception {

        File theDir = new File(Search.path+"Maps");
        if (!theDir.exists()){
            theDir.mkdirs();
        }

        int cpt=1;
        File folder = new File (Search.path+"Index");
        for (final File f1 : folder.listFiles()) {
            
            HashMap<String, Integer> indexf1 = fileToMap(f1);
            int id = Integer.parseInt(f1.getName().replace(".dex",""));
            Serialisation.storeData(indexf1,id);
            
            if(cpt%5==0)System.out.println("In Progress : "+cpt+"/"+folder.listFiles().length);
            cpt++;
        }
    }

    public static HashMap<String,Integer> fileToMap(File index) throws FileNotFoundException {
        HashMap<String,Integer> keywords = new HashMap<String,Integer>();
        Pattern p = Pattern.compile("(.*) : \\[.* : (.*)\\]");
        Scanner text = new Scanner(index);
        while (text.hasNextLine()) { 
            Matcher matcher = p.matcher(text.nextLine());
            if(matcher.find()){
                keywords.put(matcher.group(1),Integer.parseInt(matcher.group(2)));
            }
        }
        text.close();

        return keywords;
    }

}
