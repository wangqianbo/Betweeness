package ict.ada.graph.util;
import ict.ada.graph.util.FileScanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.hadoop.fs.Path;

public class GenGraph {
  public static ArrayList<Integer>[] genGraph(String graphPath) throws IOException{
    ArrayList<Integer>[] result =null;
    FileScanner fs = new FileScanner(graphPath);
    Path path = fs.next(); 
    final int READ_BUF = 10 * 1024 * 1024;
    BufferedReader cin = new BufferedReader(new InputStreamReader(fs.getFileSystem().open(path,
        READ_BUF), "UTF-8"), READ_BUF);
    try {
      String line = cin.readLine();
      result = new ArrayList[Integer.parseInt(line)];
      int i =0;
      while (null != (line = cin.readLine())) {
        String[] vertexs = line.split("\t");
        ArrayList<Integer> adjVertexs = new ArrayList<Integer>(vertexs.length);
        for(String vertex : vertexs)
          adjVertexs.add(Integer.parseInt(vertex));
        result[i++] = adjVertexs;
      }
    } finally {
      cin.close();
    }
    return result;
    
  }
}
