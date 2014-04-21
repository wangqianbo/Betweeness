package ict.ada.graph.betweenness;

import ict.ada.graph.util.GenGraph;
import ict.ada.graph.util.Float;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class BetweenessCentralityMapper extends
    Mapper<NullWritable, NullWritable, Text, FloatWritable> {
  /*
   * JSON Data processor
   */
  private BetweennessCentrality processor;

  @Override
  protected void setup(Context context) throws IOException, InterruptedException {
    String graphPath = context.getConfiguration().get("graph.location");
    ArrayList<Integer>[] graph = GenGraph.genGraph(graphPath);
    processor = new BetweennessCentrality(graph,context);
    context.setStatus(context.getInputSplit().toString());
  }

  @Override
  protected void map(NullWritable key, NullWritable value, Context context) throws IOException,
      InterruptedException {
    processor.run();
    float[] result = processor.getCB();
    for (int i = 0; i < result.length; i++) {
      context.write(new Text("V"+i), new FloatWritable(result[i]));
    }
    HashMap<Integer,Float>[] edgesCB = processor.getEdgesCB();
    for(int i =0; i< result.length; i++){
      for(Entry<Integer, Float>e:edgesCB[i].entrySet()){
        context.write(new Text("E"+i+"\t"+e.getKey()), new FloatWritable(e.getValue().floatValue()));
      }
    }
  }

  @Override
  public void run(Context context) throws IOException, InterruptedException {
    setup(context);
    try {
      map(context.getCurrentKey(), context.getCurrentValue(), context);
    } finally {// make sure processor is always closed so that internal cache is flushed.
      if (processor != null) {
        
      }
    }
    cleanup(context);
  }

}
