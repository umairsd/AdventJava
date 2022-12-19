package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Day 16: Proboscidea Volcanium
 * <a href="https://adventofcode.com/2022/day/16">2022, Day-16</a>
 */
public class Day16 extends Day {

  public Day16() {
    super(16, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    Map<Valve, List<Valve>> graph = parseGraph(lines);

    Map<String, Map<String, Integer>> distanceMatrix = distanceMatrix(graph);
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    Valve start = graph.keySet().stream().filter(v -> v.name.equals("AA")).findFirst().get();
    List<Valve> valvesToOpen = graph.keySet().stream().filter(v -> v.flowRate > 0).toList();

    int result = findFlow(start, valvesToOpen, distanceMatrix, 30, graph);
    return Integer.toString(result);
  }

  @Override
  protected String part2(List<String> lines) {
    return null;
  }

  @Override
  protected String part1Filename() {
    return filenameFromDataFileNumber(2);
  }

  @Override
  protected String part2Filename() {
    return filenameFromDataFileNumber(1);
  }

  /**
   * From the `start` node, go through each valve from the list of valves to open. For each such
   * valve, determine the time to get to this valve (via distanceMatrix) and see if we can reach
   * this valve within the time remaining. If so, then calculate the contribution to the total flow
   * from this valve, and recursively continue.
   */
  private static int findFlow(
      Valve start,
      List<Valve> valvesToOpen,
      Map<String, Map<String, Integer>> distanceMatrix,
      int remainingMinutes,
      Map<Valve, List<Valve>> ignoredGraph
  ) {

    List<Integer> cumulativeFlows = new ArrayList<>();

    for (Valve v : valvesToOpen) {
      int distanceFromStart = distanceMatrix.get(start.name).get(v.name);
      if (distanceFromStart > remainingMinutes) {
        // We can't make it to this valve. So try the next one.
        continue;
      }

      int minutesToGetToAndOpenV = distanceFromStart + 1; // 1 minute to open the valve.
      int updatedMinutes = remainingMinutes - minutesToGetToAndOpenV;
      int flowFromV = v.flowRate * updatedMinutes;

      List<Valve> updatedOpenToValves = valvesToOpen.stream()
          .filter(vo -> !vo.name.equals(v.name))
          .toList();

      int totalFlow = flowFromV +
          findFlow(v, updatedOpenToValves, distanceMatrix, updatedMinutes, ignoredGraph);
      cumulativeFlows.add(totalFlow);
    }

    return cumulativeFlows.stream().max(Integer::compare).orElse(0);
  }

  /**
   * Uses BFS to calculate distances from each node to every other node.
   */
  private static Map<String, Map<String, Integer>> distanceMatrix(Map<Valve, List<Valve>> graph) {
    Map<String, Map<String, Integer>> distances = new HashMap<>();

    for (Valve v : graph.keySet()) {
      if (!distances.containsKey(v.name)) {
        distances.put(v.name, new HashMap<>());
      }
      Map<String, Integer> distancesFromV = distances.get(v.name);

      Queue<QNode> queue = new ArrayDeque<>();
      Set<Valve> visited = new HashSet<>();
      // Distance from v to v is 0.
      distancesFromV.put(v.name, 0);
      queue.add(new QNode(v, 0));

      while (!queue.isEmpty()) {
        QNode qNode = queue.poll();
        if (visited.contains(qNode.valve)) {
          continue;
        }
        visited.add(qNode.valve);

        for (Valve neighbor : graph.get(qNode.valve)) {
          int distance = qNode.distance + 1;
          int existingDistance = distancesFromV.getOrDefault(neighbor.name, Integer.MAX_VALUE);

          if (distance < existingDistance) {
            distancesFromV.put(neighbor.name, distance);
            queue.add(new QNode(neighbor, distance));
          }
        }
      }
    }

    return distances;
  }

  private record QNode(Valve valve, int distance) {
  }

  private static Map<Valve, List<Valve>> parseGraph(List<String> lines) {
    Map<Valve, List<Valve>> graph = new HashMap<>();
    Map<String, List<String>> nameToNeighborsMap = new HashMap<>();

    for (String line : lines) {
      Valve v = parseValve(line);
      List<String> neighbors = parseNeighbors(line);

      graph.put(v, new ArrayList<>());
      nameToNeighborsMap.put(v.name, neighbors);
    }

    for (Valve valve : graph.keySet()) {
      var neighborNames = nameToNeighborsMap.get(valve.name);

      for (String n : neighborNames) {
        var neighbor = graph.keySet()
            .stream()
            .filter(v -> v.name.equals(n))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
        graph.get(valve).add(neighbor);
      }
    }

    return graph;
  }

  private static Valve parseValve(String line) {
    String[] outerTokens = line.split(";");
    // Parse valve.
    String[] valveNameTokens = outerTokens[0].strip().split(" ");
    String name = valveNameTokens[1].strip();

    String[] rateTokens = outerTokens[0].strip().split("=");
    int flowRate = Integer.parseInt(rateTokens[rateTokens.length - 1].strip());

    Valve v = new Valve(name, flowRate);
    return v;
  }

  private static List<String> parseNeighbors(String line) {
    String[] outerTokens = line.split(";");

    Pattern p = Pattern.compile("tunnels* leads* to valves* ");
    Matcher m = p.matcher(outerTokens[1]);
    String neighborsString = m.replaceAll("");
    String[] neighborTokens = neighborsString.strip().split(",");
    return Arrays.stream(neighborTokens).map(String::strip).toList();
  }

  private record Valve(String name, int flowRate) {
  }
}
