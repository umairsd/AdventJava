package com.umair.aoc.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {

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
