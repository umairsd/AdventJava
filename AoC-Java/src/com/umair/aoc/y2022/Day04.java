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
      if (isFullyContained(intervalPair.get(0), intervalPair.get(1))) {
        fullyContainedCount += 1;
      }
    }
    return Integer.toString(fullyContainedCount);
  }

  @Override
  protected String part2(List<String> lines) {
    List<List<Interval>> intervalPairs = parseLinesToIntervals(lines);

    int overlappingIntervals = 0;
    for (List<Interval> intervalPair : intervalPairs) {
      assert(intervalPair.size() == 2);
      Interval i1 = intervalPair.get(0);
      Interval i2 = intervalPair.get(1);

      if (isOverlap(i1, i2) || isFullyContained(i1, i2)) {
        overlappingIntervals += 1;
      }
    }
    return Integer.toString(overlappingIntervals);
  }



  private static boolean isOverlap(Interval i1, Interval i2) {
    Interval earlierStart = (i1.start <= i2.start ? i1 : i2);
    Interval laterStart = (i1.start > i2.start ? i1 : i2);

    boolean isOverlap = laterStart.start <= earlierStart.end && laterStart.end > earlierStart.end;
    return isOverlap;
  }

  private static boolean isFullyContained(Interval i1, Interval i2) {
    Interval longer = (i1.getLength() >= i2.getLength() ? i1 : i2);
    Interval shorter = (i1.getLength() < i2.getLength() ? i1 : i2);

    return (shorter.start >= longer.start && shorter.end <= longer.end);
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
