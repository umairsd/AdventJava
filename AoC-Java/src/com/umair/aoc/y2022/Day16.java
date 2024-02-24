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
    DistanceMatrix distanceMatrix = new DistanceMatrix(graph);
    Valve start = getStartingValve(graph);
    List<Valve> valvesToOpen = graph.keySet().stream().filter(v -> v.flowRate > 0).toList();

    int result = findFlow(start, valvesToOpen, distanceMatrix, 30, graph);
    return Integer.toString(result);
  }

  private static Valve getStartingValve(Map<Valve, List<Valve>> graph) {
    Valve start = graph.keySet()
        .stream()
        .filter(v -> v.name.equals("AA"))
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
    return start;
  }

  @Override
  protected String part2(List<String> lines) {
    Map<Valve, List<Valve>> graph = parseGraph(lines);
    DistanceMatrix distanceMatrix = new DistanceMatrix(graph);
    Valve start = getStartingValve(graph);
    List<Valve> valvesToOpen = graph.keySet().stream().filter(v -> v.flowRate > 0).toList();

    Map<Set<Valve>, Integer> openValvesToFlowMap = new HashMap<>();
    generateOpenValvesToFlowData(
        start,
        26,
        0,
        Collections.emptySet(), // Set of currently open valves.
        valvesToOpen,           // List of valves to be opened.
        distanceMatrix,         // Distance matrix
        openValvesToFlowMap);

    // From all the sets, find two disjoint sets such that the sum of their flows is the largest.
    int result = openValvesToFlowMap.entrySet().stream()
        .flatMapToInt(kv1 -> openValvesToFlowMap.entrySet().stream()
            .filter(kv2 -> areSetsDisjoint(kv1.getKey(), kv2.getKey()))
            .mapToInt(kv2 -> kv1.getValue() + kv2.getValue()))
        .max()
        .orElse(-1);
    return Integer.toString(result);
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
      DistanceMatrix distanceMatrix,
      int remainingMinutes,
      Map<Valve, List<Valve>> ignoredGraph
  ) {

    List<Integer> cumulativeFlows = new ArrayList<>();

    for (Valve v : valvesToOpen) {
      int distanceFromStart = distanceMatrix.getDistance(start.name, v.name);
      if (distanceFromStart >= remainingMinutes) {
        // We can't make it to this valve. So try the next one.
        continue;
      }

      int minutesToGetToAndOpenV = distanceFromStart + 1; // 1 minute to open the valve.
      int minutesLeft = remainingMinutes - minutesToGetToAndOpenV;
      int flowFromV = v.flowRate * minutesLeft;

      List<Valve> updatedValvesToOpen = valvesToOpen.stream()
          .filter(vo -> !vo.name.equals(v.name))
          .toList();

      int totalFlow = flowFromV +
          findFlow(v, updatedValvesToOpen, distanceMatrix, minutesLeft, ignoredGraph);
      cumulativeFlows.add(totalFlow);
    }

    return cumulativeFlows.stream().max(Integer::compare).orElse(0);
  }

  /**
   * From the `start` node, go through each valve from the list of valves to open. For each set of
   * valves that are currently open, store the current flow.
   */
  private static void generateOpenValvesToFlowData(
      Valve start,
      int timeRemaining,
      int flowSoFar,
      Set<Valve> currentOpenValves,
      List<Valve> valvesToOpen,
      DistanceMatrix distanceMatrix,
      Map<Set<Valve>, Integer> openValvesToFlowMap
  ) {

    openValvesToFlowMap.merge(currentOpenValves, flowSoFar, Math::max);

    if (timeRemaining < 0) {
      // This shouldn't happen, as this function is invoked when we have more than 0 minutes.
      // remaining. Adding this for sanity test and easier debugging.
      throw new IllegalArgumentException("Invalid. Zero minutes remaining.");
    }

    for (Valve v : valvesToOpen) {
      int distanceFromStart = distanceMatrix.getDistance(start.name, v.name);
      int minutesToGetToAndOpenV = distanceFromStart + 1; // 1 minute to open the valve.
      int remainingMinutes = timeRemaining - minutesToGetToAndOpenV;

      if (currentOpenValves.contains(v) || remainingMinutes <= 0) {
        // Either this valve is already open, or we don't have time to get to it.
        continue;
      }

      int flowFromV = v.flowRate * remainingMinutes;
      int flowGoingForward = flowSoFar + flowFromV;

      // We've opened `v`, so update the list of valves that still need to be opened.
      List<Valve> remainingValvesToOpen = new ArrayList<>(valvesToOpen)
          .stream()
          .filter(v1 -> !v1.name.equals(v.name))
          .toList();

      Set<Valve> updatedOpenValves = new HashSet<>(currentOpenValves);
      updatedOpenValves.add(v);

      generateOpenValvesToFlowData(
          v,
          remainingMinutes,
          flowGoingForward,
          updatedOpenValves,
          remainingValvesToOpen,
          distanceMatrix,
          openValvesToFlowMap);
    }
  }

  private static <T> boolean areSetsDisjoint(Set<T> a, Set<T> b) {
    Set<T> ab = new HashSet<>(a);
    ab.addAll(b);
    return ab.size() == a.size() + b.size();
  }

  private static class DistanceMatrix {
    Map<String, Map<String, Integer>> distances = new HashMap<>();

    DistanceMatrix(Map<Valve, List<Valve>> graph) {
      buildMatrix(graph);
    }

    int getDistance(String v, String u) {
      return distances.get(v).get(u);
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      for (String v : distances.keySet().stream().sorted().toList()) {
        sb.append(v).append(" | ");
        for (String u : distances.get(v).keySet().stream().sorted().toList()) {
          sb.append(u).append(": ").append(distances.get(v).get(u)).append(",\t");
        }
        sb.append("\n");
      }
      return sb.toString();
    }

    /**
     * Uses BFS to calculate distances from each node to every other node.
     */
    private void buildMatrix(Map<Valve, List<Valve>> graph) {
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
    }
  }

  private record QNode(Valve valve, int distance) {}

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

  private static final Pattern linePattern =
      Pattern.compile("Valve (.*) has flow rate=(.*); tunnels? leads? to valves? (.*)");

  private static Valve parseValve(String line) {
    Matcher m = linePattern.matcher(line);
    if (!m.matches()) {
      throw new IllegalStateException("Bad input line: " + line);
    }
    String name = m.group(1);
    int flowRate = Integer.parseInt(m.group(2));
    return new Valve(name, flowRate);
  }

  private static List<String> parseNeighbors(String line) {
    Matcher m = linePattern.matcher(line);
    if (!m.matches()) {
      throw new IllegalStateException("Bad input line: " + line);
    }
    var neighbors = Arrays.asList(m.group(3).split(", "));
    return neighbors;
  }

  private record Valve(String name, int flowRate) {}
}
