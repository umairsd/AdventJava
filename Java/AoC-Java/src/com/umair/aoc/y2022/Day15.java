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

  public Day15() {
    super(15, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    List<Sensor> sensors = parseSensors(lines);
    long disallowedColumns = disallowedColumnsCount(sensors, 2000000);
    return Long.toString(disallowedColumns);
  }

  private static int disallowedColumnsCount(List<Sensor> sensors, long currentRow) {
    Set<Long> disallowedColumns = new HashSet<>();

    // Columns covered by an existing beacon or sensor.
    Set<Long> coveredColumns = columnsCoveredAtRow(sensors, currentRow);

    for (Sensor s : sensors) {
      long rowDelta = Math.abs(s.sensorLocation.row - currentRow);
      // The number of columns covered to the left and right of the sensors column position.
      long halfColumnCount = s.manhattanDistance() - rowDelta;
      long minCol = s.sensorLocation.column - halfColumnCount;
      long maxCol = s.sensorLocation.column + halfColumnCount;

      while (minCol <= maxCol) {
        if (!coveredColumns.contains(minCol)) {
          disallowedColumns.add(minCol);
        }
        minCol++;
      }
    }

    return disallowedColumns.size();
  }

  private static Set<Long> columnsCoveredAtRow(List<Sensor> sensors, long currentRow) {
    Set<Long> coveredColumns = new HashSet<>();
    for (Sensor s : sensors) {
      if (s.sensorLocation.row == currentRow) {
        coveredColumns.add(s.sensorLocation.column);
      }
      if (s.beaconLocation.row == currentRow) {
        coveredColumns.add(s.beaconLocation.column);
      }
    }

    return coveredColumns;
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

  private static List<Sensor> parseSensors(List<String> lines) {
    List<Sensor> sensors = lines.stream()
        .map(Day15::parseSensor)
        .toList();
    return sensors;
  }

  private static Sensor parseSensor(String line) {
    String[] tokens = line.split(":");

    Coord sensorLocation = parseCoord(tokens[0]);
    Coord beaconLocation = parseCoord(tokens[1]);
    return new Sensor(sensorLocation, beaconLocation);
  }

  private static Coord parseCoord(String s) {
    // Split by ','
    String[] outerTokens = s.strip().split(",");

    // split by '=', and get the last part.
    String[] xTokens = outerTokens[0].strip().split("=");
    long x = Long.parseLong(xTokens[xTokens.length - 1].strip());

    String[] yTokens = outerTokens[1].strip().split("=");
    long y = Long.parseLong(yTokens[yTokens.length - 1].strip());

    return new Coord(y, x);
  }

  private static int getMax(List<Sensor> sensors, CoordToInt f) {
    int max = sensors.stream()
        .map(s -> Math.max(f.intValue(s.sensorLocation), f.intValue(s.beaconLocation)))
        .mapToInt(v -> v)
        .max()
        .orElseThrow(IllegalArgumentException::new);
    return max;
  }

  private static int getMin(List<Sensor> sensors, CoordToInt f) {
    int min = sensors.stream()
        .map(s -> Math.min(f.intValue(s.sensorLocation), f.intValue(s.beaconLocation)))
        .mapToInt(v -> v)
        .min()
        .orElseThrow(IllegalArgumentException::new);
    return min;
  }

  private static class Sensor {
    Coord sensorLocation;
    Coord beaconLocation;

    Sensor(Coord s, Coord b) {
      this.sensorLocation = s;
      this.beaconLocation = b;
    }

    long manhattanDistance() {
      long distance = Math.abs(beaconLocation.row - sensorLocation.row)
          + Math.abs(beaconLocation.column - sensorLocation.column);
      return distance;
    }

    @Override
    public String toString() {
      return "S: (r: " + sensorLocation.row + ", c: " + sensorLocation.column + "), " +
          "B: (r: " + beaconLocation.row + ", c: " + beaconLocation.column + "), " +
          "d = " + manhattanDistance();
    }
  }

  private record Coord(long row, long column){}

  @FunctionalInterface
  private interface CoordToInt {
    int intValue(Coord coord);
  }
}
