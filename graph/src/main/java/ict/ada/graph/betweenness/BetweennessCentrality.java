package ict.ada.graph.betweenness;

import ict.ada.graph.util.BCUtil;
import ict.ada.graph.util.Float;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.zookeeper.KeeperException;

public class BetweennessCentrality {
  private static final String GROUP = " BetweenessCentrality  Process";
  private static final String CNT_SUCCESS_SOURCE = "Success sources";
  private static final int BATCH = 50;
  private ArrayList<Integer>[] graph;
  private HashMap<Integer, Float>[] edges;
  private Integer[] integerCache;
  private float[] CB;
  private float[] R;
  private int[] S;
  private ArrayList<Integer>[] P;
  private int[] E;
  private int[] D;
  private int len;
  private Queue<Integer> Q;
  private Context context;

  public BetweennessCentrality(ArrayList<Integer>[] graph, Context context) {
    this.graph = graph;
    this.len = graph.length;
    edges = new HashMap[len];
    integerCache = new Integer[len];
    Q = new LinkedList<Integer>();
    this.R = new float[len];
    this.S = new int[len];
    this.P = new ArrayList[len];
    for (int i = 0; i < len; i++) {
      edges[i] = new HashMap<Integer, Float>();
      integerCache[i] = Integer.valueOf(i);
      P[i] = new ArrayList<Integer>();
    }
    this.E = new int[len];
    this.D = new int[len];
    this.CB = new float[len];
    this.context = context;
  }

  private int getSource() {
    try {
      return BCUtil.getSource(50);
    } catch (KeeperException e) {
      e.printStackTrace();
      return -1;
    } catch (InterruptedException e) {
      e.printStackTrace();
      return -1;
    }
  }

  public void run() {
    int s = getSource();
    while (true) {
      if (s >= graph.length) break;
      for (int k = 0; k < BATCH; k++) {
        context.progress();
        s += k;
        if (s >= graph.length) break;
        for (int j = 0; j < len; j++) {
          E[j] = 0;
          P[j].clear();
          D[j] = -1;
          R[j] = 0.0f;
        }
        E[s] = 1;
        D[s] = 0;
        Q.clear();
        Q.add(integerCache[s]);
        int i = 0;
        while (!Q.isEmpty()) {
          int v = Q.poll();
          S[i++] = v;
          for (int w : graph[v]) {
            if (D[w] < 0) {
              Q.add(integerCache[w]);
              D[w] = D[v] + 1;
            }
            if (D[w] == D[v] + 1) {
              E[w] = E[w] + E[v];
              P[w].add(integerCache[v]);
            }
          }
        }
        for (i = i - 1; i >= 0; i--) {
          int w = S[i];
          for (int v : P[w]) {
            float edgeCB = ((E[v] * 1.0f) / E[w]) * (1.0f + R[w]);
            if (v > w) {
              HashMap<Integer, Float> edgesCB = edges[w];
              Float cb = edgesCB.get(integerCache[v]);
              if (cb == null) edgesCB.put(integerCache[v], new Float(edgeCB));
              else cb.add(edgeCB);
            } else {
              HashMap<Integer, Float> edgesCB = edges[v];
              Float cb = edgesCB.get(integerCache[w]);
              if (cb == null) edgesCB.put(integerCache[w], new Float(edgeCB));
              else cb.add(edgeCB);
            }
            R[v] = R[v] + edgeCB;
          }
          if (w != s) CB[w] = CB[w] + R[w];
        }
      }
      context.getCounter(GROUP, CNT_SUCCESS_SOURCE).increment(BATCH);
      s = getSource();
    }
  }

  public float[] getCB() {
    return CB;
  }

  public HashMap<Integer, Float>[] getEdgesCB() {
    return edges;
  }
}
