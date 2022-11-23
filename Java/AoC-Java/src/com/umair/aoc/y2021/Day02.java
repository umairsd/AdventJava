package com.umair.aoc.y2021;

import com.umair.aoc.common.Constants;
import com.umair.aoc.common.Day;

import java.util.List;

public class Day02 extends Day {

  public Day02() {
    super(2, 2021);
  }

  @Override
  protected String part1(List<String> lines) {
    if (lines.isEmpty()) {
      return Constants.INPUT_EMPTY;
    }

    long horizontal = 0;
    long depth = 0;

    // Go line by line, and parse into a `Movement` object.
    for (String line : lines) {
      String[] tokens = line.split(" ");
      Direction d = Direction.valueOf(tokens[0].toUpperCase());
      long magnitude = Long.parseLong(tokens[1]);

      switch (d) {
        case UP:
          depth -= magnitude;
          break;
        case DOWN:
          depth += magnitude;
          break;
        case FORWARD:
          horizontal += magnitude;
          break;
      }
    }

    long result = horizontal * depth;
    return Long.toString(result);
  }

  @Override
  protected String part2(List<String> lines) {
    return Constants.NOT_IMPLEMENTED;
  }

  @Override
  protected String part1Filename() {
    return filenameForPart(1);
  }

  @Override
  protected String part2Filename() {
    return filenameForPart(1);
  }

  private static class Movement {
    Direction direction;
    int magnitude;
  }

  private enum Direction {
    FORWARD, UP, DOWN
  }
}
