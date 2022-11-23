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

    for (String line : lines) {
      Movement m = parseLine(line);

      switch (m.direction) {
        case UP:
          depth -= m.magnitude;
          break;
        case DOWN:
          depth += m.magnitude;
          break;
        case FORWARD:
          horizontal += m.magnitude;
          break;
      }
    }

    long result = horizontal * depth;
    return Long.toString(result);
  }

  @Override
  protected String part2(List<String> lines) {
    if (lines.isEmpty()) {
      return Constants.INPUT_EMPTY;
    }

    long horizontal = 0;
    long depth = 0;
    long aim = 0;

    // Go line by line, and parse into a `Movement` object.
    for (String line : lines) {
      Movement m = parseLine(line);

      switch (m.direction) {
        case UP:
          aim -= m.magnitude;
          break;
        case DOWN:
          aim += m.magnitude;
          break;
        case FORWARD:
          horizontal += m.magnitude;
          depth += (aim * m.magnitude);
          break;
      }
    }

    long result = horizontal * depth;
    return Long.toString(result);
  }

  @Override
  protected String part1Filename() {
    return filenameFromDataFileNumber(2);
  }

  @Override
  protected String part2Filename() {
    return filenameFromDataFileNumber(2);
  }

  private static Movement parseLine(String line) {
    String[] tokens = line.split(" ");
    Movement m = new Movement();
    m.direction = Direction.valueOf(tokens[0].toUpperCase());
    m.magnitude = Long.parseLong(tokens[1]);
    return m;
  }

  private static class Movement {
    Direction direction;
    long magnitude;
  }

  private enum Direction {
    FORWARD, UP, DOWN
  }
}
