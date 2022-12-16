package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Day 15: Beacon Exclusion Zone
 * <a href="https://adventofcode.com/2022/day/15">2022, Day-15</a>
 */
public class Day15 extends Day {

  private static final long ROW_NUM_PART1 = 2_000_000;

  public Day15() {
    super(15, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    List<Sensor> sensors = parseSensors(lines);
    long disallowedColumns = disallowedColumnsCount(sensors, ROW_NUM_PART1);
    return Long.toString(disallowedColumns);
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

  @SuppressWarnings("SameParameterValue")
  private static int disallowedColumnsCount(List<Sensor> sensors, long currentY) {
    Set<Long> disallowedColumns = new HashSet<>();

    // Columns covered by an existing beacon or sensor.
    Set<Long> coveredColumns = columnsCoveredAtRow(sensors, currentY);

    for (Sensor s : sensors) {
      // The row delta between the `currentY`, and the sensor's vertical position.
      long deltaY = Math.abs(s.position.y - currentY);
      // The number of columns covered to the left and right of the sensors column position.
      long halfColumnCount = s.manhattanDistance() - deltaY;
      long minX = s.position.x - halfColumnCount;
      long minY = s.position.x + halfColumnCount;

      while (minX <= minY) {
        if (!coveredColumns.contains(minX)) {
          disallowedColumns.add(minX);
        }
        minX++;
      }
    }

    return disallowedColumns.size();
  }

  private static Set<Long> columnsCoveredAtRow(List<Sensor> sensorCoverages, long currentY) {
    Set<Long> coveredColumns = new HashSet<>();
    for (Sensor s : sensorCoverages) {
      if (s.position.y == currentY) {
        coveredColumns.add(s.position.x);
      }
      if (s.beacon.position.y == currentY) {
        coveredColumns.add(s.beacon.position.x);
      }
    }
    return coveredColumns;
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

  private static class Sensor {
    Vertex position;
    Beacon beacon;

    Sensor(Vertex position, Beacon beacon) {
      this.position = position;
      this.beacon = beacon;
    }

    long manhattanDistance() {
      long distance = Math.abs(beacon.position.x - position.x)
          + Math.abs(beacon.position.y - position.y);
      return distance;
    }

    @Override
    public String toString() {
      return "S: (x: " + position.x + ", y: " + position.y + "), " +
          "B: (x: " + beacon.position.x + ", y: " + beacon.position.y + "), " +
          "r = " + manhattanDistance();
    }
  }

  private record Beacon(Vertex position) { }

  private record Vertex(long x, long y) { }
}
