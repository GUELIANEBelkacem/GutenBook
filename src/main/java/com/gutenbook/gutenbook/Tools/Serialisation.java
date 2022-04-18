package com.gutenbook.gutenbook.Tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.MapSerializer;
import com.gutenbook.gutenbook.Search.Search;

public class Serialisation {


    public static void storeData(HashMap<String,Integer> map, int idbook) throws Exception {
        String pathfile = Search.path+"Maps/"+idbook+".map";
        File file = new File(pathfile);
        FileOutputStream fos = new FileOutputStream(file);
        Kryo kryo = new Kryo();
        kryo.register(HashMap.class, new MapSerializer());
        Output output = new Output(fos);
        kryo.writeClassAndObject(output, map);
        output.close();

        fos.close();
    }

    
    public static HashMap<String,Integer> loadData(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);

        Kryo kryo = new Kryo();
        kryo.register(HashMap.class, new MapSerializer());
        Input input = new Input(fis);
        @SuppressWarnings("unchecked")
		HashMap<String,Integer> res = (HashMap<String,Integer>) kryo.readClassAndObject(input);
        input.close();

        fis.close();
        return res;
    }


    public static void storeMatrix(Matrix matrice) throws Exception {

        File fileToSaveObject=new File(Search.path+"MatrixJaccard/matrix.txt");
        FileOutputStream fos = new FileOutputStream(fileToSaveObject);
        Kryo kryo = new Kryo();
        kryo.register(HashMap.class, new MapSerializer());
        Output output = new Output(fos);
        kryo.writeClassAndObject(output, matrice.matrix);
        output.close();

        fos.close();
    }


    public static HashMap<Integer,HashMap<Integer,Double>> loadMatrix(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);

        Kryo kryo = new Kryo();
        kryo.register(HashMap.class, new MapSerializer());
        Input input = new Input(fis);
        @SuppressWarnings("unchecked")
		HashMap<Integer,HashMap<Integer,Double>> res = (HashMap<Integer,HashMap<Integer,Double>>) kryo.readClassAndObject(input);
        input.close();

        fis.close();
        return res;
    }


    public static void storeGraph(Graph mygraph) throws Exception {

        File fileToSaveObject=new File(Search.path+"Graph/");
        if (!fileToSaveObject.exists()){
            fileToSaveObject.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(fileToSaveObject+"/graph.txt");
        Kryo kryo = new Kryo();
        kryo.register(HashMap.class, new MapSerializer());
        kryo.register(HashSet.class);
        Output output = new Output(fos);
        kryo.writeClassAndObject(output, mygraph.getNeighbours());
        output.close();

        fos.close();
    }

    public static Map<Integer, Set<Integer>> loadGraph(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);

        Kryo kryo = new Kryo();
        kryo.register(HashMap.class, new MapSerializer());
        kryo.register(HashSet.class);
        Input input = new Input(fis);
        @SuppressWarnings("unchecked")
		Map<Integer, Set<Integer>> res = (Map<Integer, Set<Integer>>) kryo.readClassAndObject(input);
        input.close();

        fis.close();
        return res;
    }
}
