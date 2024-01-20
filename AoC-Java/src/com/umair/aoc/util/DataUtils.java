package com.umair.aoc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataUtils {

  public static List<String> removeBlankLines(List<String> lines) {
    return lines
        .stream()
        .filter(l -> !l.isEmpty() && !l.isBlank())
        .collect(Collectors.toList());
  }

  public static List<List<String>> splitIntoChunks(List<String> lines) {
    List<List<String>> result = new ArrayList<>();
    List<String> chunk = new ArrayList<>();
    for (String line : lines) {
      if (line.strip().isBlank()) {
        result.add(chunk);
        chunk = new ArrayList<>();
      } else {
        chunk.add(line);
      }
    }
    result.add(chunk);
    return result;
  }
}
