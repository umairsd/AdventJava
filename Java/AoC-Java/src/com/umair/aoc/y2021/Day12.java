package com.umair.aoc.y2021;

import com.umair.aoc.common.Day;

import java.util.*;
import static com.umair.aoc.util.CollectionUtils.singleElement;

public class Day12 extends Day {

  public Day12() {
    super(12, 2021);
  }

  @Override
  protected String part1(List<String> lines) {
    var graph = buildGraph(lines);
    CaveVertex start = graph.keySet().stream().filter(CaveVertex::isStart).collect(singleElement());

    // Can visit the cave (vertex) if it is a large one, OR if it hasn't been visited before.
    Visitable visitableFn = (v, gUnused) -> v.isLargeCave || v.getVisitedCount() == 0;

    int pathCount = dfsVisit(start, graph, visitableFn);
    return Integer.toString(pathCount);
  }

  @Override
  protected String part2(List<String> lines) {
    var graph = buildGraph(lines);
    CaveVertex start = graph.keySet().stream().filter(CaveVertex::isStart).collect(singleElement());

    Visitable visitableFn = (v, g) -> {
      // Can visit the cave (vertex) if:
      // - it is a large one, or
      // - it is a small one and hasn't been visited before, or
      // - it has been visited once, is not the start, and no other vertex has been visited twice.
      int count = v.getVisitedCount();
      boolean isAnyVertexVisitedTwice = isAnySmallCaveVisitedTwice(g);
      boolean canVisit =
          v.isLargeCave || count == 0 || (count == 1 && !isAnyVertexVisitedTwice && !v.isStart());
      return canVisit;
    };

    int pathCount = dfsVisit(start, graph, visitableFn);
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

  private static int dfsVisit(
      CaveVertex caveVertex,
      Map<CaveVertex, List<CaveVertex>> graph,
      Visitable visitable
  ) {
    if (caveVertex.isEnd()) {
      // We found a valid path to the end.
      return 1;
    }

    if (!visitable.canVisit(caveVertex, graph)) {
      return 0;
    }

    // Visit the cave.
    caveVertex.visitedCount += 1;

    int pathCount = 0;
    List<CaveVertex> neighbors = graph.getOrDefault(caveVertex, new ArrayList<>())
        .stream()
        .filter(v -> visitable.canVisit(v, graph))
        .toList();
    for (CaveVertex neighbor : neighbors) {
      pathCount += dfsVisit(neighbor, graph, visitable);
    }

    // We are done processing this path, so mark this as unvisited.
    caveVertex.visitedCount -= 1;
    return pathCount;
  }

  private static boolean isAnySmallCaveVisitedTwice(Map<CaveVertex, List<CaveVertex>> graph) {
    for (CaveVertex v : graph.keySet()) {
      if (!v.isLargeCave && v.getVisitedCount() == 2) {
        return true;
      }
    }
    return false;
  }

  private static Map<CaveVertex, List<CaveVertex>> buildGraph(List<String> lines) {
    Map<CaveVertex, List<CaveVertex>> graph = new HashMap<>();
    Map<String, CaveVertex> vertices = new HashMap<>();

    for (String line : lines) {
      String[] tokens = line.strip().split("-");
      CaveVertex vertexA = buildOrGetVertex(tokens[0], vertices);
      CaveVertex vertexB = buildOrGetVertex(tokens[1], vertices);

      // Add the first node (cave).
      List<CaveVertex> adjListA = graph.getOrDefault(vertexA, new ArrayList<>());
      adjListA.add(vertexB);
      graph.put(vertexA, adjListA);

      // Add the second node (cave).
      List<CaveVertex> adjListB = graph.getOrDefault(vertexB, new ArrayList<>());
      adjListB.add(vertexA);
      graph.put(vertexB, adjListB);
    }

    return graph;
  }

  private static CaveVertex buildOrGetVertex(String name, Map<String, CaveVertex> vertices) {
    if (!vertices.containsKey(name)) {
      vertices.put(name, new CaveVertex(name));
    }
    return vertices.get(name);
  }

  @FunctionalInterface
  private interface Visitable {
    boolean canVisit(CaveVertex v, Map<CaveVertex, List<CaveVertex>> graph);
  }

  private static class CaveVertex {
    private static final String START = "start";
    private static final String END = "end";

    private final String name;
    private final boolean isLargeCave;
    private int visitedCount = 0;

    CaveVertex(String name) {
      this.name = name;
      this.isLargeCave = name.chars().allMatch(Character::isUpperCase);
    }

    int getVisitedCount() {
      if (isLargeCave) {
        // Large caves can be visited unlimited times.
        return 0;
      }
      return visitedCount;
    }

    boolean isStart() {
      return name.equals(START);
    }

    boolean isEnd() {
      return name.equals(END);
    }

    @Override
    public String toString() {
      return name + ", " + visitedCount;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      CaveVertex that = (CaveVertex) o;
      return name.equals(that.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name);
    }
  }
}
