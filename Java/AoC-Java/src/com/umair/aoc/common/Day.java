package com.umair.aoc.common;

import com.umair.aoc.util.FileUtils;

import java.text.DecimalFormat;
import java.util.List;

public abstract class Day {

  private static final String RESOURCES_PATH_PREFIX = "./resources";
  private static final String PATH_SEPARATOR = "/";
  private static final String FILE_NAME_PREFIX = "day";
  private static final String FILE_NAME_SUFFIX = "-data";
  private static final String FILE_EXTENSION = ".txt";

  protected int dayNumber;
  protected int year;

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

  protected abstract String part1Filename();

  protected abstract String part2Filename();

  protected List<String> readData(String filename) {
    return FileUtils.readAllLinesFromFile(filename);
  }

  protected String filenameFromDataFileNumber(int part) {
    DecimalFormat dayFormat = new DecimalFormat("00");
    DecimalFormat yearFormat = new DecimalFormat("0000");

    // year/day<dayNumber>-part2.txt
    StringBuilder builder = new StringBuilder();
    builder.append(RESOURCES_PATH_PREFIX);
    builder.append(PATH_SEPARATOR);
    builder.append(yearFormat.format(year));
    builder.append(PATH_SEPARATOR);
    builder.append(FILE_NAME_PREFIX);
    builder.append(dayFormat.format(dayNumber));
    builder.append(FILE_NAME_SUFFIX);
    builder.append(part);
    builder.append(FILE_EXTENSION);

    return builder.toString();
  }
}
