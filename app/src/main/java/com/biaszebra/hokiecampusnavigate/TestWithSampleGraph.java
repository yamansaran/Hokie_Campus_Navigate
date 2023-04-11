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
    System.out.println(csvReader.entries);

    ValueGraph<String, Integer> graph = createSampleGraph(csvReader);
    System.out.println("graph = " + graph);

    findAndPrintShortestPath(graph, "A","D");
    findAndPrintShortestPath(graph, "A","J");
    List<String> temp = sendShortestPath(graph, one,two);
    String tempString = temp.toString();
    /*
    findAndPrintShortestPath(graph, "D", "H");
    findAndPrintShortestPath(graph, "A", "F");
    findAndPrintShortestPath(graph, "E", "H");
    findAndPrintShortestPath(graph, "B", "H");
    findAndPrintShortestPath(graph, "B", "I");
    findAndPrintShortestPath(graph, "E", "H");
    findAndPrintShortestPath(graph, "A", "J");
    findAndPrintShortestPath(graph, "A", "K");
    */
 return tempString;
  }

  public ArrayList<String> getTable(){
      ArrayList<String> table = new ArrayList<String>();


      return table;
  }

  private static void findAndPrintShortestPath(
          ValueGraph<String, Integer> graph, String source, String target) {
    List<String> shortestPath = DijkstraWithPriorityQueue.findShortestPath(graph, source, target);
    System.out.printf("shortestPath from %s to %s = %s%n", source, target, shortestPath);
  }
  private static List<String> sendShortestPath(
          ValueGraph<String, Integer> graph, String source, String target) {
    List<String> shortestPath = DijkstraWithPriorityQueue.findShortestPath(graph, source, target);
    return shortestPath;
  }

  private static ValueGraph<String, Integer> createSampleGraph() {
    MutableValueGraph<String, Integer> graph = ValueGraphBuilder.undirected().build();
    graph.putEdgeValue("A", "C", 2);
    graph.putEdgeValue("A", "E", 3);
    graph.putEdgeValue("B", "E", 5);
    graph.putEdgeValue("B", "I", 15);
    graph.putEdgeValue("C", "D", 3);
    graph.putEdgeValue("C", "G", 2);
    graph.putEdgeValue("D", "E", 1);
    graph.putEdgeValue("D", "F", 4);
    graph.putEdgeValue("E", "F", 6);
    graph.putEdgeValue("F", "H", 7);
    graph.putEdgeValue("G", "H", 4);
    graph.putEdgeValue("H", "I", 3);
    graph.putEdgeValue("I", "J", 5);
    graph.putEdgeValue("J", "K", 7);
    graph.putEdgeValue("A", "L", 8);
    //graph.putEdgeValue("F", "K", 11);
    String f = "F";
    String k ="K";
    int el = 11;
    graph.putEdgeValue(f, k, el);
    return graph;
  }
  private static ValueGraph<String, Integer> createSampleGraph(CSVReader reader){//overloaded method with csv reader
    MutableValueGraph<String,Integer> graph = ValueGraphBuilder.undirected().build();
    for(int i = 0; i< reader.entries;i++) {
      graph.putEdgeValue(reader.name1.get(i), reader.name2.get(i) , reader.cost.get(i));
    }

    return graph;
  }




}
