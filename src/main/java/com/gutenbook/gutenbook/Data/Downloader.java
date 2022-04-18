package com.gutenbook.gutenbook.Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.gutenbook.gutenbook.Search.Search;




public class Downloader {
	

    private static ExecutorService executorService = new ThreadPoolExecutor(
            4,
            4,
            1,
            TimeUnit.HOURS, new LinkedBlockingQueue<Runnable>());

    

    
    public static void download(int number) throws Exception {
    	System.out.println("Building Book Library: \n");
        System.out.println(buildBooksDatabase(number).size());
    }
    
    public static ArrayList<Integer> buildBooksDatabase(int nbBooks) throws Exception {
        ArrayList<Integer> listofBooksIds= new ArrayList<Integer>();
        int cpt=1; 
        while (listofBooksIds.size()<nbBooks){
            listofBooksIds.addAll(auxBuildBooksDatabase(cpt,nbBooks));
            System.out.println("Progress("+listofBooksIds.size()+"/"+nbBooks+")");
            cpt++;
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);
        return listofBooksIds;
    }

    private static ArrayList<Integer> auxBuildBooksDatabase(int page,int nbbooks) throws Exception {

        ArrayList<Integer> listofBooksIds= new ArrayList<Integer>();
     
        URL url = new URL("https://gutendex.com/books/?page="+page); 
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();

        JSONObject contentJson = new JSONObject(content.toString());
        JSONArray jsonArray = contentJson.getJSONArray("results");
        for(int i=0;i<jsonArray.length();i++){
            AtomicInteger id = new AtomicInteger(jsonArray.getJSONObject(i).getInt("id"));
            
         
            if(listofBooksIds.size()>=nbbooks) {
            	
                return listofBooksIds;
            }
            
            if (countWordsIdBook(id.intValue())) { 
            	
  
                listofBooksIds.add(id.intValue());
                	executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            downloadBook("https://www.gutenberg.org/files/"+id+"/"+id+"-0.txt", id.intValue());
                        } catch (IOException e) {e.printStackTrace();}
                    }});}}

        return listofBooksIds;
    }
    
    private static boolean countWordsIdBook(int idbook){
        String urlbook = "https://www.gutenberg.org/files/"+idbook+"/"+idbook+"-0.txt";
        try {
            URL url = new URL(urlbook);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            int comptemot=0;
            while ((inputLine = in.readLine()) != null) {
                comptemot+= countWords(inputLine);
            }
            in.close();
            con.disconnect();
	
            return (comptemot>=10000);
            
        } catch (Exception e) {
        	
            return false;
        } 
    }

    public static int countWords(String s){
        String line = s.replaceAll("\\p{Punct}", "");
        int wordCount = 0;

        boolean word = false;
        int endOfLine = line.length() - 1;

        for (int i = 0; i < line.length(); i++) {
            if (Character.isLetter(line.charAt(i)) && i != endOfLine) {
                word = true;
            } else if (!Character.isLetter(line.charAt(i)) && word) {
                wordCount++;
                word = false;
            } else if (Character.isLetter(line.charAt(i)) && i == endOfLine) {
                wordCount++;
            }
        }
        return wordCount;
    }


    public static void downloadBook(String urlbooks,int id) throws IOException {
        File theDir = new File(Search.path+"Books");
        if (!theDir.exists()){
            theDir.mkdirs();
        }
        String dir= Search.path+"Books/"+id+".txt";
        
        FileUtils.copyURLToFile(new URL(urlbooks), new File(dir));
    }


    public static String getParamsString(Map<String, String> params)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }
}
