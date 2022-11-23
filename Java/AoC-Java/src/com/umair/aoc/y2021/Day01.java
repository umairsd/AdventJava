package com.umair.aoc.y2021;

import com.umair.aoc.common.Day;
import com.umair.aoc.util.FileUtils;

import java.util.List;

public class Day01 extends Day {

  public Day01() {
    super(1, 2021);
  }

  @Override
  protected String part1(List<String> lines) {
    if (lines.isEmpty()) {
      return "";
    }

    int count = 0;
    long previous, current;

    for (int i = 1; i < lines.size(); i++) {
      previous = Long.parseLong(lines.get(i - 1));
      current = Long.parseLong(lines.get(i));
      if (current > previous) {
        count += 1;
      }
    }

    return Integer.toString(count);
  }

  @Override
  protected String part2(List<String> lines) {
    return "Not implemented";
  }

  @Override
  protected List<String> readData(String filename) {
    return FileUtils.readLinesFromFile(filename);
  }
}
