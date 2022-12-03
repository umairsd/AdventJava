package com.umair.aoc.y2021;

import com.umair.aoc.common.Day;

import java.util.*;

public class Day12 extends Day {

  public Day12() {
    super(12, 2021);
  }

  @Override
  protected String part1(List<String> lines) {
    var graph = buildGraph(lines);

    Set<String> visited = new HashSet<>();
    var pathCount = dfsVisitSmallCavesOnce("start", graph, visited);
    return Integer.toString(pathCount);
  }

  private static int dfsVisitSmallCavesOnce(
      String node,
      Map<String, List<String>> graph,
      Set<String> visited
  ) {
    if (node.equals("end")) {
      // This is a valid path.
      return 1;
    }

    // Mark the node as visited if it isn't a large node.
    boolean isLargeCave = isLargeCave(node);
    if (!isLargeCave) {
      visited.add(node);
    }

    List<String> neighbors = graph.getOrDefault(node, new ArrayList<>());
    int pathCount = 0;
    for (String neighborNode : neighbors) {
      // Can visit a node (cave) if it is a large cave, or if it has not been visited before.
      if (isLargeCave(neighborNode) || !visited.contains(neighborNode)) {
        // DFS
        pathCount += dfsVisitSmallCavesOnce(neighborNode, graph, visited);
      }
    }

    if (!isLargeCave) {
      visited.remove(node);
    }
    return pathCount;
  }

  @Override
  protected String part2(List<String> lines) {
    return null;
  }

  @Override
  protected String part1Filename() {
    return filenameFromDataFileNumber(4);
  }

  @Override
  protected String part2Filename() {
    return filenameFromDataFileNumber(1);
  }

  private static Map<String, List<String>> buildGraph(List<String> lines) {
    Map<String, List<String>> graph = new HashMap<>();
    for (String line : lines) {
      String[] tokens = line.strip().split("-");
      String vertexA = tokens[0];
      String vertexB = tokens[1];

      // Add the first node (cave).
      List<String> adjListA = graph.getOrDefault(vertexA, new ArrayList<>());
      adjListA.add(vertexB);
      graph.put(vertexA, adjListA);

      // Add the second node (cave).
      List<String> adjListB = graph.getOrDefault(vertexB, new ArrayList<>());
      adjListB.add(vertexA);
      graph.put(vertexB, adjListB);
    }

    return graph;
  }

  private static boolean isLargeCave(String cave) {
    return cave.chars().allMatch(Character::isUpperCase);
  }
}
