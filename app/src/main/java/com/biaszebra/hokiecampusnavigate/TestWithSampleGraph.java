package com.biaszebra.hokiecampusnavigate;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests the implementation of Dijkstra's algorithm using the following sample graph:
 *
 *              38
 *        A--------------L------
 *      / \                     \
 *    2/   \3                    \ 5
 *    /     \                     |
 *   / 3   1 \    1               |
 *  C-----D---E-------B       21  |
 *  |      \  |       |   /------ |
 *  |      4\ |6      |  /      \ |
 *  |        \| ------|--       K
 * 2|         F/      |15       /
 *  |         |       |        /
 *  |         |7      |       /  7
 *  |         |       |      /
 *  G---------H-------I-----J
 *       4        3      5
 *
 *
 *
 *
 * .
 */
@SuppressWarnings({"squid:S106", "PMD.SystemPrintln"}) // System.out is OK in this test program
public class TestWithSampleGraph {

  public String startRun(String one , String two, InputStream is){
    CSVReader csvReader = new CSVReader(is);
    csvReader.CSVIni();
    ValueGraph<String, Integer> graph = createSampleGraph(csvReader);
    List<String> temp = sendShortestPath(graph, one,two);
    String tempString = temp.toString();
 return tempString;
  }
  public ArrayList<String> getTable(){
      ArrayList<String> table = new ArrayList<String>();
      return table;
  }
  private static void findAndPrintShortestPath(
          ValueGraph<String, Integer> graph, String source, String target) {
    List<String> shortestPath = DijkstraWithPriorityQueue.findShortestPath(graph, source, target);
  }
  private static List<String> sendShortestPath(
          ValueGraph<String, Integer> graph, String source, String target) {
    List<String> shortestPath = DijkstraWithPriorityQueue.findShortestPath(graph, source, target);
    return shortestPath;
  }
  private static ValueGraph<String, Integer> createSampleGraph(CSVReader reader){//overloaded method with csv reader
    MutableValueGraph<String,Integer> graph = ValueGraphBuilder.undirected().build();
    for(int i = 0; i< reader.entries;i++) {
      graph.putEdgeValue(reader.name1.get(i), reader.name2.get(i) , reader.cost.get(i));
    }

    return graph;
  }




}
