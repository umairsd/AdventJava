package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day18 extends Day {

  public Day18() {
    super(18, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
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

  private static final Pattern linePattern = Pattern.compile("(.*),(.*),(.*)");

  private static Cube parseCube(String line) {
    Matcher m = linePattern.matcher(line);
    if (!m.matches()) {
      throw new IllegalStateException("Bad input line: " + line);
    }
    int x = Integer.parseInt(m.group(1).strip());
    int y = Integer.parseInt(m.group(2).strip());
    int z = Integer.parseInt(m.group(3).strip());
    return new Cube(x, y, z);
  }

  private record Cube(int x, int y, int z) {}
}
