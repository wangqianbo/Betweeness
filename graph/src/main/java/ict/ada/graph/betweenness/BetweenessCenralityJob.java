package ict.ada.graph.betweenness;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 
 * 
 * 
 */
public class BetweenessCenralityJob extends Configured implements Tool {

  private static Log LOG = LogFactory.getLog(BetweenessCenralityJob.class);

  public int run(String[] args) throws Exception {
    System.out.println(args[0]);
    String inputfolder = args[0];
    JobConf conf = new JobConf(getConf());
    FileOutputFormat.setOutputPath(conf,  new Path(args[1]));
    Job job = new Job(conf, "graph BC Process:" + inputfolder);
    int numMapTasks = Integer.parseInt(args[2]);
    job.setJarByClass(BetweenessCenralityJob.class);
    job.setNumReduceTasks(1);// no Reducer
    job.setMapperClass(BetweenessCentralityMapper.class);
    job.setReducerClass(BetweenessCentralityReducer.class);
    job.setInputFormatClass(EmptyInputFormat.class);
    FileOutputFormat.setOutputPath(conf,  new Path(args[1]));
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(FloatWritable.class);
   
    job.setSpeculativeExecution(false);// 
    job.getConfiguration().set("mapred.map.max.attempts", "1");// never retry
    job.getConfiguration().set("mapper.tasks", String.valueOf(numMapTasks));
    job.getConfiguration().set("graph.location", inputfolder);
    job.waitForCompletion(true);
    return 0;
  }

  public static void main(String[] args) throws Exception {
    ToolRunner.run(new BetweenessCenralityJob(), args);
  }

}
