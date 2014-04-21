package ict.ada.graph.betweenness;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

public class EmptyInputFormat extends InputFormat<NullWritable, NullWritable> {

  private static final String MAPPER_TASKS = "mapper.tasks";
  
  @Override
  public List<InputSplit> getSplits(JobContext job) throws IOException, InterruptedException {
    int numSplits = job.getConfiguration().getInt(MAPPER_TASKS, -1);
    ArrayList<InputSplit> splits = new ArrayList<InputSplit>(numSplits);
    for(int i = 0; i < numSplits; i++){
      splits.add(new FakeInputSplit());
    }
    return splits;
  }

  @Override
  public RecordReader<NullWritable, NullWritable> createRecordReader(InputSplit arg0,
      TaskAttemptContext arg1) throws IOException, InterruptedException {
    return new RecordReader<NullWritable, NullWritable>() {
      public void close() throws IOException {
      }

      public float getProgress() throws IOException {
        return 0.0f;
      }

      @Override
      public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return  NullWritable.get();
      }

      @Override
      public NullWritable getCurrentValue() throws IOException, InterruptedException {
        // TODO Auto-generated method stub
        return NullWritable.get();
      }

      @Override
      public void initialize(InputSplit arg0, TaskAttemptContext arg1) throws IOException,
          InterruptedException {

      }

      @Override
      public boolean nextKeyValue() throws IOException, InterruptedException {
        return false;
      }
    };
  }
}
