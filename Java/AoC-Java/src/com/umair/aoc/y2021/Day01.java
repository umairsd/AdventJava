package com.umair.aoc.y2021;

import com.umair.aoc.common.Day;
import com.umair.aoc.util.FileUtils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class Day01 extends Day {

  public Day01() {
    super(1, 2021);
  }

  @Override
  protected String part1(List<String> lines) {
    if (lines.isEmpty()) {
      return "Input is empty";
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
    if (lines.isEmpty()) {
      return "Input is empty";
    }

    int count = 0;
    long current;
    long previous = lines.stream()
        .skip(0)
        .limit(3)
        .map(Long::parseLong)
        .collect(Collectors.summingLong(Long::longValue));

    for (int i = 1; i < lines.size(); i++) {
      current = lines
          .stream()
          .skip(i)
          .limit(3)
          .map(Long::parseLong)
          .collect(Collectors.summingLong(Long::longValue));

      if (current > previous) {
        count += 1;
      }
      previous = current;
    }

    return Integer.toString(count);
  }

  @Override
  protected List<String> readData(String filename) {
    return FileUtils.readLinesFromFile(filename);
  }

  @Override
  protected String part1Filename() {
    // E.g. day01-data1.txt
    return filenameForPart(2);
  }

  @Override
  protected String part2Filename() {
    return filenameForPart(2);
  }
}
