package com.umair.aoc.common;

import java.text.DecimalFormat;
import java.util.List;

public abstract class Day {

  private static final String RESOURCES_PATH_PREFIX = "./resources";
  private static final String PATH_SEPARATOR = "/";
  private static final String FILE_EXTENSION = ".txt";

  protected int dayNumber = 0;
  protected int year = 0;

  public Day(int day, int year) {
    this.dayNumber = day;
    this.year = year;
  }

  /**
   * Reads the file for part 1, passes it to `part1()` to solve,
   * and then prints out the result.
   */
  public void solvePart1() {
    List<String> data = readData(part1Filename());
    String result = part1(data);
    System.out.println("Part 1: " + result);
  }

  /**
   * Reads the file for part 2, passes it to `part2()` to solve,
   * and then prints out the result.
   */
  public void solvePart2() {
    List<String> data = readData(part2Filename());
    String result = part2(data);
    System.out.println("Part 2: " + result);
  }

  protected abstract String part1(List<String> lines);

  protected abstract String part2(List<String> lines);

  protected abstract List<String> readData(String filename);

  protected String part1Filename() {
    return filenameForPart(1);
  }

  protected String part2Filename() {
    return filenameForPart(2);
  }

  private String filenameForPart(int part) {
    DecimalFormat dayFormat = new DecimalFormat("00");
    DecimalFormat yearFormat = new DecimalFormat("0000");

    // year/day<dayNumber>-part2.txt
    StringBuilder builder = new StringBuilder();
    builder.append(RESOURCES_PATH_PREFIX);
    builder.append(PATH_SEPARATOR);
    builder.append(yearFormat.format(year));
    builder.append(PATH_SEPARATOR);
    builder.append("day");
    builder.append(dayFormat.format(dayNumber));
    builder.append("-part");
    builder.append(part);
    builder.append(FILE_EXTENSION);

    return builder.toString();
  }
}
