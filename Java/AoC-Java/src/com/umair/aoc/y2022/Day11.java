package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Day 11: Monkey in the Middle
 * <a href="https://adventofcode.com/2022/day/11">2022, Day-11</a>
 */
public class Day11 extends Day {

  public Day11() {
    super(11, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    List<List<String>> chunks = splitIntoChunks(lines);
    return null;
  }

  @Override
  protected String part2(List<String> lines) {
    return null;
  }

  @Override
  protected String part1Filename() {
    return filenameFromDataFileNumber(1);
  }

  @Override
  protected String part2Filename() {
    return filenameFromDataFileNumber(1);
  }

  private static List<List<String>> splitIntoChunks(List<String> lines) {
    List<List<String>> result = new ArrayList<>();
    List<String> chunk = new ArrayList<>();
    for (String line : lines) {
      if (line.strip().isBlank()) {
        result.add(chunk);
        chunk = new ArrayList<>();
      } else {
        chunk.add(line);
      }
    }
    result.add(chunk);
    return result;
  }

  private static Monkey parseMonkey(List<String> lines) {
    return null;
  }

  private static class Monkey {
    private final List<Integer> startingItems = new ArrayList<>();
    String name;
    int worryMultiplier;
    int testDivisor;

    Monkey(String name, int multiplier, int divisor) {
      this.name = name;
      this.worryMultiplier = multiplier;
      this.testDivisor = divisor;
    }
  }
  
}
