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
    List<SensorCoverage> sensorCoverages = parseSensorCoverages(lines);
    long disallowedColumns = disallowedColumnsCount(sensorCoverages, ROW_NUM_PART1);
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
  private static int disallowedColumnsCount(List<SensorCoverage> sensorCoverages, long currentRow) {
    Set<Long> disallowedColumns = new HashSet<>();

    // Columns covered by an existing beacon or sensor.
    Set<Long> coveredColumns = columnsCoveredAtRow(sensorCoverages, currentRow);

    for (SensorCoverage s : sensorCoverages) {
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

  private static Set<Long> columnsCoveredAtRow(
      List<SensorCoverage> sensorCoverages,
      long currentRow
  ) {
    Set<Long> coveredColumns = new HashSet<>();
    for (SensorCoverage s : sensorCoverages) {
      if (s.sensorLocation.row == currentRow) {
        coveredColumns.add(s.sensorLocation.column);
      }
      if (s.beaconLocation.row == currentRow) {
        coveredColumns.add(s.beaconLocation.column);
      }
    }

    return coveredColumns;
  }

  private static List<SensorCoverage> parseSensorCoverages(List<String> lines) {
    List<SensorCoverage> sensorCoverages = lines.stream()
        .map(Day15::parseSensorCoverage)
        .toList();
    return sensorCoverages;
  }

  private static SensorCoverage parseSensorCoverage(String line) {
    String[] tokens = line.split(":");

    Coord sensorLocation = parseCoord(tokens[0]);
    Coord beaconLocation = parseCoord(tokens[1]);
    return new SensorCoverage(sensorLocation, beaconLocation);
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

  private static class SensorCoverage {
    Coord sensorLocation;
    Coord beaconLocation;

    SensorCoverage(Coord s, Coord b) {
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

  private record Coord(long row, long column) {
  }
}
