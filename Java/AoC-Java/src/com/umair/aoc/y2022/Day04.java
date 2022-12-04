package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.List;

public class Day04 extends Day {

  public Day04() {
    super(4, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    List<List<Interval>> intervalPairs = parseLinesToIntervals(lines);

    int fullyContainedCount = 0;
    for (List<Interval> intervalPair : intervalPairs) {
      assert(intervalPair.size() == 2);
      Interval longer = intervalPair.get(0); // Guaranteed, based on the parsing logic.
      Interval shorter = intervalPair.get(1);
      if (shorter.start >= longer.start && shorter.end <= longer.end) {
        fullyContainedCount += 1;
      }
    }
    return Integer.toString(fullyContainedCount);
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

  private static List<List<Interval>> parseLinesToIntervals(List<String> lines) {
    List<List<Interval>> result = lines
        .stream()
        .map(Day04::parseLine)
        .toList();
    return result;
  }

  private static List<Interval> parseLine(String line) {
    String[] tokens = line.strip().split(",");
    Interval i1 = parseInterval(tokens[0]);
    Interval i2 = parseInterval(tokens[1]);

    // Ensure that the longer interval is the first element.
    if (i1.getLength() >= i2.getLength()) {
      return List.of(i1, i2);
    } else {
      return List.of(i2, i1);
    }
  }

  private static Interval parseInterval(String s) {
    String[] tokens = s.strip().split("-");
    int start = Integer.parseInt(tokens[0].strip());
    int end = Integer.parseInt(tokens[1].strip());
    Interval i = new Interval(start, end);
    return i;
  }

  private static class Interval {
    int start;
    int end;
    Interval(int start, int end) {
      this.start = start;
      this.end = end;
    }

    int getLength() {
      return end - start + 1;
    }
  }
}
