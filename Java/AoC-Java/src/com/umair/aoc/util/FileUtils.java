package com.umair.aoc.util;

import com.umair.aoc.common.Day;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {
  private static final String RESOURCES_PATH_PREFIX = "./resources";
  private static final String PATH_SEPARATOR = "/";
  private static final String FILE_NAME_PREFIX = "day";
  private static final String FILE_NAME_SUFFIX = "-data";
  private static final String FILE_EXTENSION = ".txt";

  public static String filenameForDayAndFileNumber(Day d, int part) {
    DecimalFormat dayFormat = new DecimalFormat("00");
    DecimalFormat yearFormat = new DecimalFormat("0000");

    // year/day<dayNumber>-part2.txt
    String result = RESOURCES_PATH_PREFIX +
        PATH_SEPARATOR +
        yearFormat.format(d.getYear()) +
        PATH_SEPARATOR +
        FILE_NAME_PREFIX +
        dayFormat.format(d.getDayNumber()) +
        FILE_NAME_SUFFIX +
        part +
        FILE_EXTENSION;

    return result;
  }

  public static List<String> readAllLinesFromFile(String filename) {
    File f = new File(filename);
    return readFileLineByLine(f);
  }

  public static List<String> readLinesFromFile(String filename) {
    List<String> lines = readAllLinesFromFile(filename);
    lines = lines
        .stream()
        .filter(l -> !l.isEmpty() && !l.isBlank())
        .collect(Collectors.toList());
    return lines;
  }

  private static List<String> readFileLineByLine(File file) {
    List<String> result = new ArrayList<>();

    try (
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr)
    ) {
      while (br.ready()) {
        result.add(br.readLine());
      }
    } catch (IOException ioe) {
      System.out.printf("Exception when reading file %s%n", file.getName());
      ioe.printStackTrace();
      System.out.println();
    }

    return result;
  }
}
