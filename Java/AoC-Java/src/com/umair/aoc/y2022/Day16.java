package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    GraphContext graphContext = parseGraph(lines);
    Map<Valve, ValveState> valveStateMap = new HashMap<>();
    for (Valve v : graphContext.graph.keySet()) {
      valveStateMap.put(v, new ValveState(false, 0));
    }

    Valve start = new Valve("AA");
    Map<MemoKey, Integer> memo = new HashMap<>();
    int rate = flowRate(start, graphContext, 30, valveStateMap, memo);
    return Integer.toString(rate);
  }

  @Override
  protected String part2(List<String> lines) {
    return null;
  }

  private static int flowRate(
      Valve current,
      GraphContext gctx,
      int remainingTime,
      Map<Valve, ValveState> stateMap,
      Map<MemoKey, Integer> memo
  ) {

    if (remainingTime == 0) {
      int totalFlow = 0;

      System.out.print("Open valves: ");
      for (Valve v : stateMap.keySet()) {
        ValveState state = stateMap.get(v);
        if (state.isOpen) {
          System.out.print("(" + v.name + ", m=" + state.minutesOpen + "), ");
          totalFlow += (gctx.valveFlowRateMap.get(v) * state.minutesOpen);
        }
      }
      System.out.println();
      return totalFlow;
    }

    MemoKey key = new MemoKey(current, remainingTime);
    if (memo.containsKey(key)) {
      return memo.get(key);
    }

    int maxFlowRate = Integer.MIN_VALUE;
    ValveState currentState = stateMap.get(current);

    if (!currentState.isOpen && gctx.valveFlowRateMap.get(current) != 0) {
      // Open the valve. And note down how many minutes does it stay open.
      ValveState newState = new ValveState(true, remainingTime - 1);

      var copiedStates = new HashMap<>(stateMap);
      copiedStates.put(current, newState);
      int fr1 = flowRate(current, gctx, remainingTime - 1, copiedStates, memo);
      maxFlowRate = fr1;
    }

    List<Valve> neighbors = gctx.graph.get(current);
    for (Valve neighbor : neighbors) {
      // Travel to each of them that are NOT open
      if (!stateMap.get(neighbor).isOpen) {
        int flowRate = flowRate(neighbor, gctx, remainingTime - 1, stateMap, memo);
        maxFlowRate = Math.max(maxFlowRate, flowRate);
      }
    }

    memo.put(key, maxFlowRate);
    return maxFlowRate;
  }

  @Override
  protected String part1Filename() {
    return filenameFromDataFileNumber(1);
  }

  @Override
  protected String part2Filename() {
    return filenameFromDataFileNumber(1);
  }

  private static GraphContext parseGraph(List<String> lines) {
    GraphContext ctx = new GraphContext();
    for (String line : lines) {
      parseLine(line, ctx);
    }
    return ctx;
  }

  private static void parseLine(String line, GraphContext graphContext) {
    String[] outerTokens = line.split(";");
    // Parse valve.
    String[] valveNameTokens = outerTokens[0].strip().split(" ");
    String name = valveNameTokens[1].strip();

    String[] rateTokens = outerTokens[0].strip().split("=");
    int flowRate = Integer.parseInt(rateTokens[rateTokens.length - 1].strip());

    Valve v = new Valve(name);

    Pattern p = Pattern.compile("tunnels* leads* to valves* ");
    Matcher m = p.matcher(outerTokens[1]);
    String neighborsString = m.replaceAll("");
    String[] neighborTokens = neighborsString.strip().split(",");

    List<Valve> neighbors = Arrays.stream(neighborTokens)
        .map(String::strip)
        .map(Valve::new)
        .toList();

    graphContext.graph.put(v, neighbors);
    graphContext.valveFlowRateMap.put(v, flowRate);
  }

  private record MemoKey(Valve valve, int minutesRemaining) { }

  private static class GraphContext {
    private final Map<Valve, List<Valve>> graph = new HashMap<>();
    private final Map<Valve, Integer> valveFlowRateMap = new HashMap<>();
  }

  private record ValveState(boolean isOpen, int minutesOpen) { }

  private static class Valve {
    String name;
    Valve(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return name;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Valve valve = (Valve) o;
      return name.equals(valve.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name);
    }
  }
}
