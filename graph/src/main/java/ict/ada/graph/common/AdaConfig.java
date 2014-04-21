package ict.ada.graph.common;

import java.io.IOException;
import java.util.Properties;

/**
 * Configuration parameters for ADA<br>
 * TODO redesign
 */
public class AdaConfig {

  // // Hadoop
  // public static final String HADOOP_JOB_TRACKER;
  // public static final String HADOOP_QUEUENAME;
  // public static final String HADOOP_NAMENODE;
  // public static final String ADA_LIB_PATH_URI;// ada dependencies in hdfs for MR jobs
  // public static final String ADA_TMP_PATH_URI;// temp folder for ada in hdfs
  //
  // // Oozie
  // public static final String OOZIE_SERVER_URI;
  // public static final String OOZIE_WORKFLOW_PATH_URI;
  // public static final String OOZIE_USER_NAME;
  //
  // // Neo4j
  // public static final String NEO4J_THRIFT_SERVER_IP;

  /*
   * Search System
   */
  /** Search Index Server Address (with port) */
  public static final String INDEX_SERVER_ADDR;
  /** Multi-thread index server facade address(with port) */
  // public static final String MULTITHREAD_INDEX_SERVER_ADDR;

  public static final String MQ_SERVER_ADDR;
  public static String MQ_NAME;

  public static final String ROWCOUNT_CHANNELS;
  public static final String RELATIONTYPE_CHANNELS;
  public static final String CONNECTION_STRING;
  public static final int TIME_GRANULARITY;
  public static final  boolean GRAPH_NODEATTR_ACTION_TIMESTAMP;
  /*
   * Common
   */
  /** whether self loop(Edge with identical head and tail Nodes) is accepted in graph */
  public static final boolean GRAPH_ACCEPT_SELFLOOP;

  /** Property file name */
  public static final String PROP_FILE_NAME = "ada_config.properties";

  static {
    Properties config = new Properties();
    try {
      config.load(AdaConfig.class.getClassLoader().getResourceAsStream(PROP_FILE_NAME));
      // HADOOP_JOB_TRACKER = getAndCheckString(config, "ada.hadoop.jobtracker");
      // HADOOP_QUEUENAME = getAndCheckString(config, "ada.hadoop.queuename");
      // HADOOP_NAMENODE = getAndCheckString(config, "ada.hadoop.namenode");
      // ADA_LIB_PATH_URI = getAndCheckString(config, "ada.lib.path");
      // ADA_TMP_PATH_URI = getAndCheckString(config, "ada.tmp.path");
      // OOZIE_SERVER_URI = getAndCheckString(config, "ada.oozie.server.uri");
      // OOZIE_WORKFLOW_PATH_URI = getAndCheckString(config, "ada.oozie.workflow.app.path");
      // OOZIE_USER_NAME = getAndCheckString(config, "ada.oozie.user.name");
      // NEO4J_THRIFT_SERVER_IP = getAndCheckString(config, "ada.neo4j.thriftserver.ip");
      // MULTITHREAD_INDEX_SERVER_ADDR = getAndCheckString(config,
      // "ada.indexserver.multithread.address");

      INDEX_SERVER_ADDR = getAndCheckString(config, "ada.indexserver.address");
      GRAPH_ACCEPT_SELFLOOP = getAndCheckBoolean(config, "ada.graph.accept.selfloop");
      GRAPH_NODEATTR_ACTION_TIMESTAMP=getAndCheckBoolean(config,"ada.graph.node.attr.action.timestamp");
      
      MQ_SERVER_ADDR = getAndCheckString(config, "ada.mqserver.address");

      ROWCOUNT_CHANNELS = config.getProperty("ada.gdb.rowcounter");

      RELATIONTYPE_CHANNELS = config.getProperty("ada.gdb.relationtypecounter");

      CONNECTION_STRING = getAndCheckString(config, "ada.gdb.zookeeperserver");
      
      TIME_GRANULARITY=Integer.parseInt(config.getProperty("ada.gdb.time.granularity"));

      if (config.containsKey("ada.mqserver.mqname")) {
        MQ_NAME = getAndCheckString(config, "ada.mqserver.mqname");
      } else {
        MQ_NAME = "ada_search_mq";
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static boolean getAndCheckBoolean(Properties config, String key) throws IOException {
    return Boolean.parseBoolean(getAndCheckString(config, key));
  }

  private static String getAndCheckString(Properties config, String key) throws IOException {
    String value = config.getProperty(key);
    if (value == null || value.trim().length() == 0)
      throw new IOException("Invalid configuration. key=" + key + " value=" + value);
    return value;
  }

  public static void main(String[] args) {
    // System.out.println(AdaConfig.HADOOP_JOB_TRACKER);
    // System.out.println(AdaConfig.NEO4J_THRIFT_SERVER_IP);
    System.out.println(AdaConfig.GRAPH_ACCEPT_SELFLOOP);
    System.out.println(AdaConfig.CONNECTION_STRING);
  }
}
