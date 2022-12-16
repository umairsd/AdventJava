package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.*;

/**
 * Day 15: Beacon Exclusion Zone
 * <a href="https://adventofcode.com/2022/day/15">2022, Day-15</a>
 */
public class Day15 extends Day {

  private static final long ROW_NUM_PART1 = 2_000_000;
  private static final long MAX_COORDINATE = 4_000_000;
  private static final long SIGNAL_CONSTANT = 4_000_000;

  public Day15() {
    super(15, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    List<Sensor> sensors = parseSensors(lines);
    List<Range> mergedRanges = mergeRangesForRow(sensors, ROW_NUM_PART1);

    long columnsCoveredBySignal = mergedRanges.stream()
        .mapToLong(Range::size)
        .sum();
    long coveredByItem = sensors.stream()
        .filter(s -> s.beacon.position.y == ROW_NUM_PART1)
        .map(s -> s.beacon.position.x)
        .distinct()
        .count();
    return Long.toString(columnsCoveredBySignal - coveredByItem);
  }

  @Override
  protected String part2(List<String> lines) {
    List<Sensor> sensors = parseSensors(lines);
    long y = -1;
    long x = -1;

    for (int row = 0; row <= MAX_COORDINATE; row++) {
      boolean foundGap = false;
      List<Range> mergedRanges = mergeRangesForRow(sensors, row);

      Range previousMin = mergedRanges.get(0);
      Range min = new Range(Math.max(0, previousMin.min), previousMin.max);

      Range previousMax = mergedRanges.get(mergedRanges.size() - 1);
      Range max = new Range(previousMax.min, Math.min(MAX_COORDINATE, previousMax.max));

      mergedRanges.set(0, min);
      mergedRanges.set(mergedRanges.size() - 1, max);

      long total = mergedRanges.stream().mapToLong(Range::size).sum();
      if (total < MAX_COORDINATE + 1) {
        // Find the gap in the ranges.
        for (int j = 1; j < mergedRanges.size(); j++) {
          Range previous = mergedRanges.get(j - 1);
          Range current = mergedRanges.get(j);

          if (previous.max < current.min) {
            foundGap = true;
            y = row;
            x = current.min - 1;
            break;
          }
        }

        if (foundGap) {
          break;
        }
      }
    }

    long signalTuningFrequency = (SIGNAL_CONSTANT * x + y);
    return Long.toString(signalTuningFrequency);
  }

  @Override
  protected String part1Filename() {
    return filenameFromDataFileNumber(2);
  }

  @Override
  protected String part2Filename() {
    return filenameFromDataFileNumber(2);
  }

  private static List<Sensor> parseSensors(List<String> lines) {
    List<Sensor> sensors = lines.stream()
        .map(Day15::parseSensor)
        .toList();
    return sensors;
  }

  private static Sensor parseSensor(String line) {
    String[] tokens = line.split(":");

    Vertex sensorPosition = parseVertex(tokens[0]);
    Vertex beaconPosition = parseVertex(tokens[1]);
    return new Sensor(sensorPosition, new Beacon(beaconPosition));
  }

  private static Vertex parseVertex(String s) {
    // Split by ','
    String[] outerTokens = s.strip().split(",");

    // split by '=', and get the last part.
    String[] xTokens = outerTokens[0].strip().split("=");
    long x = Long.parseLong(xTokens[xTokens.length - 1].strip());

    String[] yTokens = outerTokens[1].strip().split("=");
    long y = Long.parseLong(yTokens[yTokens.length - 1].strip());

    return new Vertex(x, y);
  }

  @SuppressWarnings("SameParameterValue")
  private static Stack<Range> mergeRangesForRow(List<Sensor> sensors, long row) {
    List<Range> ranges = sensors.stream()
        .map(s -> s.rangeAtRow(row))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .sorted(Comparator.comparingLong(r -> r.min))
        .toList();

    Stack<Range> mergedRanges = new Stack<>();

    for (Range currentRange : ranges) {
      if (mergedRanges.isEmpty() || mergedRanges.peek().max < currentRange.min) {
        mergedRanges.push(currentRange);
      } else {
        Range top = mergedRanges.pop();
        Range newRange = new Range(top.min, Math.max(top.max, currentRange.max));
        mergedRanges.push(newRange);
      }
    }

    return mergedRanges;
  }

  private static class Sensor {
    Vertex position;
    Beacon beacon;

    Sensor(Vertex position, Beacon beacon) {
      this.position = position;
      this.beacon = beacon;
    }

    long manhattanDistance() {
      return position.manhattanDistance(beacon.position);
    }

    Optional<Range> rangeAtRow(long currentY) {
      // The row delta between the `currentY`, and the sensor's vertical position.
      long deltaY = Math.abs(position.y - currentY);
      // The number of columns covered to the left and right of the sensors column position.
      long halfColumnCount = manhattanDistance() - deltaY;
      long minX = position.x - halfColumnCount;
      long maxX = position.x + halfColumnCount;

      if (minX <= maxX) {
        return Optional.of(new Range(minX, maxX));
      }
      return Optional.empty();
    }

    @Override
    public String toString() {
      return "S: (x: " + position.x + ", y: " + position.y + "), " +
          "B: (x: " + beacon.position.x + ", y: " + beacon.position.y + "), " +
          "r = " + manhattanDistance();
    }
  }

  private record Range(long min, long max) {
    long size() {
      return max - min + 1;
    }
  }

  private record Beacon(Vertex position) { }

  private record Vertex(long x, long y) {
    long manhattanDistance(Vertex otherVertex) {
      long distance = Math.abs(x - otherVertex.x) + Math.abs(y - otherVertex.y);
      return distance;
    }
  }
}
