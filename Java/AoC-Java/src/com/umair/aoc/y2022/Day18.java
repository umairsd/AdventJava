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
    List<Cube> cubes = lines.stream().map(Day18::parseCube).toList();
    int totalExposedSides = cubes.size() * 6;

    for (int i = 0; i < cubes.size(); i++) {
      for (int j = i + 1; j < cubes.size(); j++) {
        var c1 = cubes.get(i);
        var c2 = cubes.get(j);
        if (areCubesTouching(c1, c2)) {
          totalExposedSides -= 2;
        }
      }
    }

    return Integer.toString(totalExposedSides);
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

  private static boolean areCubesTouching(Cube c1, Cube c2) {
    boolean result = (c1.x == c2.x && c1.y == c2.y && Math.abs(c1.z - c2.z) == 1) ||
        (c1.x == c2.x && c1.z == c2.z && Math.abs(c1.y - c2.y) == 1) ||
        (c1.z == c2.z && c1.y == c2.y && Math.abs(c1.x - c2.x) == 1);
    return result;
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
