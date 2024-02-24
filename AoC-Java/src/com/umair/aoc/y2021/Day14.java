package com.umair.aoc.y2021;

import com.umair.aoc.common.Day;

import java.util.*;

import static com.umair.aoc.common.Constants.INPUT_EMPTY;

public class Day14 extends Day {

  private static final int PART1_ITERATIONS = 10;
  private static final int PART2_ITERATION2 = 40;

  public Day14() {
    super(14, 2021);
  }

  /**
   * Keeping part-1 as suboptimal, by generating the entire polymer string. This is helpful
   * to get a feel for the problem.
   */
  @Override
  protected String part1(List<String> lines) {
    if (lines.isEmpty()) {
      return INPUT_EMPTY;
    }

    String polymerTemplate = lines.get(0);
    Map<String, String> insertionRules = parseInsertionRules_Part1(lines.stream().skip(2).toList());
    int iterationCount = 1;

    while (iterationCount <= PART1_ITERATIONS) {
      List<LetterPair> letterPairs = convertPolymerTemplateToLetterPairs(polymerTemplate);
      polymerTemplate = rebuildPolymerTemplateAsString(letterPairs, insertionRules);
      iterationCount++;
    }

    Map<Character, Integer> countMap = new HashMap<>();
    for (char c : polymerTemplate.toCharArray()) {
      int count = countMap.getOrDefault(c, 0);
      countMap.put(c, count + 1);
    }
    long max = countMap.values().stream().max(Long::compare).orElse(0);
    long min = countMap.values().stream().min(Long::compare).orElse(0);
    return Long.toString(max - min);
  }

  private static String rebuildPolymerTemplateAsString(
      List<LetterPair> letterPairs,
      Map<String, String> insertionRules) {

    StringBuilder sb = new StringBuilder();
    for (LetterPair lp : letterPairs) {
      sb.append(lp.getFirst());
      sb.append(insertionRules.getOrDefault(lp.letters, ""));
    }

    // Add the second for the last one:
    sb.append(letterPairs.get(letterPairs.size() - 1).getSecond());
    return sb.toString();
  }

  private static List<LetterPair> convertPolymerTemplateToLetterPairs(String polymerTemplate) {
    List<LetterPair> pairs = new ArrayList<>();
    for (int i = 1; i < polymerTemplate.length(); i++) {
      String sub = polymerTemplate.substring(i - 1, i + 1);

      LetterPair p = new LetterPair(sub);
      pairs.add(p);
    }

    return pairs;
  }


  @Override
  protected String part2(List<String> lines) {
    if (lines.isEmpty()) {
      return INPUT_EMPTY;
    }

    Polymer polymer = new Polymer(lines.get(0));
    Map<LetterPair, List<LetterPair>> insertionRules =
        parseInsertionRules(lines.stream().skip(2).toList());
    int iterationCount = 1;

    while (iterationCount <= PART2_ITERATION2) {
      polymer = rebuildPolymerUsingInsertionRules(polymer, insertionRules);
      iterationCount++;
    }

    Map<Character, Long> countMap = buildCharacterCountMapFromPolymer(polymer);
    long max = countMap.values().stream().max(Long::compare).orElse(0L);
    long min = countMap.values().stream().min(Long::compare).orElse(0L);
    return Long.toString(max - min);
  }


  private static Polymer rebuildPolymerUsingInsertionRules(
      Polymer polymer,
      Map<LetterPair, List<LetterPair>> insertionRules
  ) {
    Map<LetterPair, Long> newLetterPairCounts = new HashMap<>();

    for (Map.Entry<LetterPair, Long> polymerEntry : polymer.letterPairCounts.entrySet()) {
      LetterPair key = polymerEntry.getKey();
      long countOfLetterPair = polymerEntry.getValue();

      if (insertionRules.containsKey(key)) {
        // For a rule such as "NN -> C", if the letter pair NN is included in the polymer, it
        // splits into two letter pairs NC, and CN.
        // The new polymer does not contain NN, so we don't need to worry about it. For each of
        // the elements that it splits into (NC, CN) increase their count by the count of the
        // current letter pair. Basically, 5 NNs will split into 5 NC and 5 CN.
        for (LetterPair p : insertionRules.get(key)) {
          long pCount = newLetterPairCounts.getOrDefault(p, 0L);
          newLetterPairCounts.put(p, pCount + countOfLetterPair);
        }
      }
    }

    return new Polymer(newLetterPairCounts);
  }

  private static Map<Character, Long> buildCharacterCountMapFromPolymer(Polymer polymer) {
    Map<Character, Long> countMap = new HashMap<>();
    // First, get the total counts of each of the characters in the polymer.
    for (LetterPair key : polymer.letterPairCounts.keySet()) {
      long keyCount = polymer.letterPairCounts.get(key);

      long firstCharCount = countMap.getOrDefault(key.getFirst(), 0L);
      countMap.put(key.getFirst(), firstCharCount + keyCount);

      long secondCharCount = countMap.getOrDefault(key.getSecond(), 0L);
      countMap.put(key.getSecond(), secondCharCount + keyCount);
    }

    /*
    - Using the example in the question, the starting template is NNCB. This gets broken into
      letter pairs as [NN, NC, CB]. Notice that each of the inner characters is repeated twice.
      The second N of NN, and the first N of NC are the same character in the final polymer "NNCB",
      and is thus duplicated.
    - After step 1, these pairs are further broken into [NC, CN, NB, BC, CH, HB], representing
      NCNBCHB. Again, notice that the inner characters are duplicated.

    - This means that the inner characters will always sum to even values, and we need to divide
      them by 2 to get the actual count.
    - At the same time, the extreme left and right character(s) are only used once, and thus not
      duplicated. This means that these values will be odd, and thus need to be handled correctly.
     */
    for (Character c : countMap.keySet()) {
      long count = countMap.get(c);
      if (count % 2 == 0) {
        countMap.put(c, count / 2);
      } else {
        countMap.put(c, (count / 2) + 1);
      }
    }
    return countMap;
  }

  private static Map<String, String> parseInsertionRules_Part1(List<String> lines) {
    Map<String, String> rules = new HashMap<>();
    for (String line : lines) {
      String[] tokens = line.strip().split("->");
      rules.put(tokens[0].strip(), tokens[1].strip());
    }
    return rules;
  }

  /**
   * Builds a map of rules, such that NN -> C is parsed as {key: NN, value: [NC, CN]}
   */
  private static Map<LetterPair, List<LetterPair>> parseInsertionRules(List<String> lines) {
    Map<LetterPair, List<LetterPair>> rules = new HashMap<>();
    for (String line : lines) {
      String[] tokens = line.strip().split("->");

      assert (tokens[0].strip().length() == 2);
      assert (tokens[1].strip().length() == 1);

      String key = tokens[0].strip();
      LetterPair p1 = new LetterPair(key.charAt(0) + tokens[1].strip());
      LetterPair p2 = new LetterPair(tokens[1].strip() + key.charAt(1));

      rules.put(new LetterPair(tokens[0].strip()), List.of(p1, p2));
    }
    return rules;
  }

  /**
   * A wrapper class the represents all the letter pairs that make up a polymer.
   */
  private static class Polymer {
    private final Map<LetterPair, Long> letterPairCounts;

    private Polymer(String polymerTemplate) {
      this.letterPairCounts = new HashMap<>();
      stringToLetterPairCounts(polymerTemplate);
    }

    private Polymer(Map<LetterPair, Long> letterPairCounts) {
      this.letterPairCounts = letterPairCounts;
    }

    private void stringToLetterPairCounts(String s) {
      for (int i = 1; i < s.length(); i++) {
        String sub = s.substring(i - 1, i + 1);
        LetterPair letterPair = new LetterPair(sub);
        long currentCount = letterPairCounts.getOrDefault(letterPair, 0L);
        letterPairCounts.put(letterPair, currentCount + 1);
      }
    }
  }

  /**
   * A wrapper class around String.
   */
  private record LetterPair(String letters) {

    Character getFirst() {
      return this.letters.charAt(0);
    }

    Character getSecond() {
      return this.letters.charAt(1);
    }

    @Override
    public String toString() {
      return this.letters;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      LetterPair that = (LetterPair) o;
      return Objects.equals(letters, that.letters);
    }

  }
}
