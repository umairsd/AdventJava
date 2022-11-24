package com.umair.aoc.common;

import java.util.List;
import java.util.stream.Collectors;

public class DataUtils {

  public static List<String> removeBlank(List<String> lines) {
    List<String> filteredLines = lines
        .stream()
        .filter(l -> !l.isEmpty() && !l.isBlank())
        .collect(Collectors.toList());
    return filteredLines;
  }
}
