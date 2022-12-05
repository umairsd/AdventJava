package com.umair.aoc.y2021;

import com.umair.aoc.common.Day;

import java.util.*;

import static com.umair.aoc.common.Constants.INPUT_EMPTY;

public class Day14 extends Day {

  public Day14() {
    super(14, 2021);
  }

  @Override
  protected String part1(List<String> lines) {
    if (lines.isEmpty()) {
      return INPUT_EMPTY;
    }

    String polymerTemplate = lines.get(0);
    Map<String, String> insertionRules = parseInsertionRules(lines.stream().skip(2).toList());
    int iterationCount = 1;

    while (iterationCount <= 10) {
      List<PolymerPair> polymerPairs = convertPolymerTemplateToPairs(polymerTemplate);
      polymerTemplate = rebuildPolymerTemplate(polymerPairs, insertionRules);
      iterationCount++;
    }

    Map<Character, Integer> countMap = buildCharacterCountMap(polymerTemplate);
    long max = countMap.values()
        .stream()
        .max(Long::compare)
        .orElse(0);
    long min = countMap.values()
        .stream()
        .min(Long::compare)
        .orElse(0);

    return Long.toString(max - min);
  }

  @Override
  protected String part2(List<String> lines) {
    return null;
  }

  @Override
  protected String part1Filename() {
    return filenameFromDataFileNumber(2);
  }

  @Override
  protected String part2Filename() {
    return filenameFromDataFileNumber(1);
  }

  private static Map<Character, Integer> buildCharacterCountMap(String polymerTemplate) {
    Map<Character, Integer> map = new HashMap<>();
    for (char c : polymerTemplate.toCharArray()) {
      int count = map.getOrDefault(c, 0);
      map.put(c, count + 1);
    }
    return map;
  }

  private static String rebuildPolymerTemplate(
      List<PolymerPair> polymerPairs,
      Map<String, String> insertionRules) {

    StringBuilder sb = new StringBuilder();
    for (PolymerPair p : polymerPairs) {
      sb.append(p.first);
      sb.append(insertionRules.getOrDefault(p.combination, ""));
    }

    // Add the second for the last one:
    sb.append(polymerPairs.get(polymerPairs.size() - 1).second);


    return sb.toString();
  }

  private static List<PolymerPair> convertPolymerTemplateToPairs(String polymerTemplate) {
    List<PolymerPair> pairs = new ArrayList<>();
    for (int i = 1; i < polymerTemplate.length(); i++) {
      String sub = polymerTemplate.substring(i - 1, i + 1);

      PolymerPair p = new PolymerPair(sub);
      pairs.add(p);
    }

    return pairs;
  }

  private static Map<String, String> parseInsertionRules(List<String> lines) {
    Map<String, String> rules = new HashMap<>();
    for (String line : lines) {
      String[] tokens = line.strip().split("->");

      assert(tokens[1].strip().length() == 2);
      assert(tokens[1].strip().length() == 1);

      rules.put(tokens[0].strip(), tokens[1].strip());
    }
    return rules;
  }

  private static class PolymerPair {
    private final Character first;
    private final Character second;

    private final String combination;

    PolymerPair(String combination) {
      this.combination = combination;
      assert(this.combination.length() == 2);

      first = this.combination.charAt(0);
      second = this.combination.charAt(1);
    }
  }
}
