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

  @Override
  protected String part2(List<String> lines) {
    var graph = buildGraph(lines);

    Map<String, Integer> visitedCounts = new HashMap<>();
    for (String key : graph.keySet()) {
      visitedCounts.put(key, 0);
    }

    var pathCount = dfsVisitSmallCavesOnce2("start", graph, visitedCounts);
    return Integer.toString(pathCount);
  }

  @Override
  protected String part1Filename() {
    return filenameFromDataFileNumber(4);
  }

  @Override
  protected String part2Filename() {
    return filenameFromDataFileNumber(4);
  }


  private static int dfsVisitSmallCavesOnce2(
      String node,
      Map<String, List<String>> graph,
      Map<String, Integer> visitedCounts
  ) {
    if (node.equals("end")) {
      // This is a valid path.
      return 1;
    }

    // Mark the node (cave) as visited if it isn't a large one, OR if it hasn't been visited before.
    int count = visitedCounts.get(node);
    boolean hasVisitedTwice = isAnySmallCaveVisitedTwice(visitedCounts);
    boolean canVisit =
        isLargeCave(node) || count == 0 || (count == 1 && !hasVisitedTwice && !node.equals("start"));
    if (!canVisit) {
      return 0;
    }

    // We can visit this node. Mark it as visited.
    visitedCounts.put(node, count + 1);

    int pathCount = 0;
    List<String> neighbors = graph.getOrDefault(node, new ArrayList<>());
    for (String neighborNode : neighbors) {
      pathCount += dfsVisitSmallCavesOnce2(neighborNode, graph, visitedCounts);
    }

    // Mark it as unvisited, now that we're done.
    visitedCounts.put(node, count);
    return pathCount;
  }

  private static boolean isAnySmallCaveVisitedTwice(Map<String, Integer> visitedCounts) {
    for (Map.Entry<String, Integer> entry : visitedCounts.entrySet()) {
      if (!isLargeCave(entry.getKey()) && entry.getValue() == 2) {
        return true;
      }
    }
    return false;
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
