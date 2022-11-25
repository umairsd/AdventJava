package com.umair.aoc.y2021;

import com.umair.aoc.common.DataUtils;
import com.umair.aoc.common.Day;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Day05 extends Day {

  public Day05() {
    super(5, 2021);
  }

  @Override
  protected String part1(List<String> data) {
    List<Line> lines = buildLines(data);
    int maxDimension = getMaxDimension(lines);
    int[][] grid = new int[maxDimension + 1][maxDimension + 1];

    lines.forEach(ln -> {
      Point start = ln.start;
      Point end = ln.end;

      if (start.x == end.x) { // x is column
        for (int row = Math.min(start.y, end.y); row <= Math.max(start.y, end.y); row++) {
          grid[row][start.x] += 1;
        }
      } else if (start.y == end.y) { // y is row
        for (int column = Math.min(start.x, end.x); column <= Math.max(start.x, end.x); column++) {
          grid[start.y][column] += 1;
        }
      }
    });

    int dangerousAreas = countDangerousAreas(grid);
    return Integer.toString(dangerousAreas);
  }

  @Override
  protected String part2(List<String> data) {
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

  private static int countDangerousAreas(int[][] grid) {
    int dangerousAreas = 0;
    for (int[] row : grid) {
      for (int node : row) {
        dangerousAreas += (node >= 2) ? 1 : 0;
      }
    }
    return dangerousAreas;
  }

  private static List<Line> buildLines(List<String> data) {
    data = DataUtils.removeBlank(data);
    List<Line> lines = data
        .stream()
        .filter(Objects::nonNull)
        .filter(Predicate.not(String::isBlank))
        .map(Day05::buildLine)
        .toList();
    return lines;
  }

  private static int getMaxDimension(List<Line> lines) {
    int maxDimension = lines
        .stream()
        .map(ln -> Stream.of(ln.start.x, ln.start.y, ln.end.x, ln.end.y).max(Integer::compare))
        .map(Optional::get)
        .mapToInt(v -> v)
        .max()
        .orElseThrow(NoSuchElementException::new);
    return maxDimension;
  }

  private static Line buildLine(String ln) {
    String[] tokens = ln.strip().split("->");
    String[] startTokens = tokens[0].strip().split(",");
    Point start = new Point(
        Integer.parseInt(startTokens[0].strip()),
        Integer.parseInt(startTokens[1].strip()));

    String[] endTokens = tokens[1].strip().split(",");
    Point end = new Point(
        Integer.parseInt(endTokens[0].strip()),
        Integer.parseInt(endTokens[1].strip()));
    return new Line(start, end);
  }

  private static class Point {
    int x;
    int y;
    Point(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }

  private static class Line {
    Point start;
    Point end;
    Line(Point start, Point end) {
      this.start = start;
      this.end = end;
    }

    @Override
    public String toString() {
      return "(" + start.x + "," + start.y + ") -> (" + end.x + "," + end.y + ")";
    }
  }
}
