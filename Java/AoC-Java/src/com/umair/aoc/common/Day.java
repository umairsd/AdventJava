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
   * and then returns the result.
   */
  public String solvePart1() {
    List<String> data = readData(part1Filename());
    String result = part1(data);
    return result;
  }

  /**
   * Reads the file for part 2, passes it to `part2()` to solve,
   * and then returns the result.
   */
  public String solvePart2() {
    List<String> data = readData(part2Filename());
    String result = part2(data);
    return result;
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
    String result = RESOURCES_PATH_PREFIX +
        PATH_SEPARATOR +
        yearFormat.format(year) +
        PATH_SEPARATOR +
        FILE_NAME_PREFIX +
        dayFormat.format(dayNumber) +
        FILE_NAME_SUFFIX +
        part +
        FILE_EXTENSION;

    return result;
  }
}
