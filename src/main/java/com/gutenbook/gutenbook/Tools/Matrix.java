package com.gutenbook.gutenbook.Tools;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.gutenbook.gutenbook.Search.Search;

public class Matrix {
	

    protected HashMap<Integer,HashMap<Integer,Double>> matrix= new HashMap<Integer, HashMap<Integer, Double>>();

    public Matrix(){}


    public void add(int ligne, int colonne, double value){
        if(matrix.get(ligne)==null){
            HashMap<Integer,Double> val= new HashMap<Integer, Double>();
            matrix.put(ligne, val);
        }

        if(matrix.get(colonne)==null){
            HashMap<Integer,Double> val= new HashMap<Integer, Double>();
            matrix.put(colonne, val);
        }

        if(matrix.get(ligne).get(colonne) == null){
            HashMap<Integer,Double> val= matrix.get(ligne);
            val.put(colonne,value);
            matrix.put(ligne,val);
        }

        if(matrix.get(colonne).get(ligne) == null){
            HashMap<Integer,Double> val= matrix.get(colonne);
            val.put(ligne,value);
            matrix.put(colonne,val);
        }
    }

    public double get(int ligne, int colonne){
        if(matrix.get(ligne)!=null && matrix.get(ligne).get(colonne)!=null )
            return matrix.get(ligne).get(colonne);
        else
            return -1.0;
    }

	public static HashMap<Integer,HashMap<Integer,Double>> loadMatrix() throws Exception {
       
        HashMap<Integer,HashMap<Integer,Double>> ldapContent;
        ldapContent = Serialisation.loadMatrix(new File(Search.path+"MatrixJaccard/matrix.txt"));
        return ldapContent;
        
        
        /*
    	File fileToSaveObject=new File(Search.path+"MatrixJaccard/matrix.txt");
        FileOutputStream fos = new FileOutputStream(fileToSaveObject);
        Kryo kryo = new Kryo();
        kryo.register(HashMap.class, new MapSerializer());
        Output output = new Output(fos);
        kryo.writeClassAndObject(output, matrice.matrix);
        output.close();

        fos.close();
    	*/
    }
	
	   public String toString (){
	        String res="";

	        for(Map.Entry<Integer,HashMap<Integer,Double>> key: matrix.entrySet()){

	            for(Map.Entry<Integer,Double> value: key.getValue().entrySet()){
	                res+="["+key.getKey()+"]"+"["+value.getKey()+"]"+" = "+value.getValue()+" ";
	            }
	            res+="\n";
	        }
	        return res;
	    }



}
