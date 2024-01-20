package com.umair.aoc.common;

import com.umair.aoc.util.FileUtils;

import java.util.List;

public abstract class Day {

  private final int dayNumber;
  private final int year;

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

  public int getDayNumber() {
    return dayNumber;
  }

  public int getYear() {
    return year;
  }

  protected abstract String part1(List<String> lines);

  protected abstract String part2(List<String> lines);

  protected abstract String part1Filename();

  protected abstract String part2Filename();

  public List<String> readData(String filename) {
    return FileUtils.readAllLinesFromFile(filename);
  }

  public String fileNameFromFileNumber(int number) {
    return FileUtils.filenameForDayAndFileNumber(this, number);
  }
}
