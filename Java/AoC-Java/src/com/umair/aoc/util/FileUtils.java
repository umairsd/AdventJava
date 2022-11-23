package com.umair.aoc.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {

  public static List<Long> readLongFromFile(String fileName) {
    List<String> stringList = readLinesFromFile(fileName);
    List<Long> result = stringList.stream().map(Long::parseLong).collect(Collectors.toList());
    return result;
  }

  public static List<String> readLinesFromFile(String filename) {
    File f = new File(filename);
    List<String> result = readFileLineByLine(f);
    return result;
  }

  private static List<String> readFileLineByLine(File file) {
    List<String> result = new ArrayList<>();

    try (
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr)
    ) {
      while (br.ready()) {
        String line = br.readLine();
        if (line.isBlank()) {
          continue;
        }
        result.add(line);
      }
    } catch (IOException ioe) {
      System.out.printf("Exception when reading file %s%n", file.getName());
      ioe.printStackTrace();
      System.out.println();
    }

    return result;
  }
}
