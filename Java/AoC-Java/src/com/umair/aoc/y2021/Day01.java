package com.umair.aoc.y2021;

import com.umair.aoc.common.Constants;
import com.umair.aoc.common.DataUtils;
import com.umair.aoc.common.Day;

import java.util.List;
import java.util.stream.Collectors;

public class Day01 extends Day {

  public Day01() {
    super(1, 2021);
  }

  @Override
  protected String part1(List<String> lines) {
    lines = DataUtils.removeBlank(lines);
    if (lines.isEmpty()) {
      return Constants.INPUT_EMPTY;
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
    lines = DataUtils.removeBlank(lines);
    if (lines.isEmpty()) {
      return Constants.INPUT_EMPTY;
    }

    int count = 0;
    long current;
    long previous = lines.stream()
        .skip(0)
        .limit(3)
        .map(Long::parseLong)
        .reduce(0L, Long::sum);

    for (int i = 1; i < lines.size(); i++) {
      current = lines
          .stream()
          .skip(i)
          .limit(3)
          .map(Long::parseLong)
          .reduce(0L, Long::sum);

      if (current > previous) {
        count += 1;
      }
      previous = current;
    }

    return Integer.toString(count);
  }

  @Override
  protected String part1Filename() {
    // E.g. day01-data1.txt
    return filenameFromDataFileNumber(2);
  }

  @Override
  protected String part2Filename() {
    return filenameFromDataFileNumber(2);
  }
}
