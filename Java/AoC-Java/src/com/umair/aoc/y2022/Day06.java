package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.*;

public class Day06 extends Day {

  private static final int MESSAGE_MARKER_OFFSET = 14;
  private static final int PACKET_MARKER_OFFSET = 4;

  public Day06() {
    super(6, 2022);
  }


  @Override
  protected String part1(List<String> lines) {
    assert(lines.size() == 1);
    String signal = lines.get(0);

    char[] characters = signal.toCharArray();
    int startOfPacketMarker = -1;

    Set<Character> seen = new HashSet<>();
    for (int i = PACKET_MARKER_OFFSET; i < characters.length; i++) {
      seen.clear();
      char currentC = characters[i];
      seen.add(currentC);

      int j = i - 1;
      while (j > (i - PACKET_MARKER_OFFSET)) {
        if (seen.contains(characters[j])) {
          break;
        }
        seen.add(characters[j]);
        j--;
      }

      if (j == i - 4) {
        startOfPacketMarker = i;
        break;
      }
    }

    return Integer.toString(startOfPacketMarker + 1);
  }

  @Override
  protected String part2(List<String> lines) {
    String signal = lines.get(0);
    int startOfMessageMarker = findIndexOfMarker(signal, MESSAGE_MARKER_OFFSET);
    return Integer.toString(startOfMessageMarker);
  }

  @Override
  protected String part1Filename() {
    return fileNameFromFileNumber(2);
  }

  @Override
  protected String part2Filename() {
    return fileNameFromFileNumber(2);
  }

  private static int findIndexOfMarker(String s, int markerOffset) {
    char[] characters = s.toCharArray();
    int markerIndex = -1;

    // Map of characters seen and their counts within the sliding window.
    Map<Character, Integer> seenCountMap = new HashMap<>();
    // Process the first `markerOffset` characters.
    assert(characters.length >= markerOffset);
    for (int i = 0; i < markerOffset; i++) {
      int currentCount = seenCountMap.getOrDefault(characters[i], 0);
      seenCountMap.put(characters[i], currentCount + 1);
    }
    if (seenCountMap.keySet().size() == markerOffset) {
      return markerOffset;
    }

    // b v w b j p l b g v b h s r l p g d m j q w f t v n c z
    // 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7
    // 0                   10                  20
    //
    for (int i = markerOffset; i < characters.length; i++) {
      // Move the sliding window.
      char charToRemove = characters[i - markerOffset];
      char charToAdd = characters[i];

      int countOfCharToRemove = seenCountMap.getOrDefault(charToRemove, 0);
      assert(countOfCharToRemove > 0);
      seenCountMap.put(charToRemove, countOfCharToRemove - 1);
      // If the new count of the character that's been removed is 0, remove it.
      if (seenCountMap.get(charToRemove) == 0) {
        seenCountMap.remove(charToRemove);
      }

      int countOfCharToAdd = seenCountMap.getOrDefault(charToAdd, 0);
      seenCountMap.put(charToAdd, countOfCharToAdd + 1);

      if (seenCountMap.keySet().size() == markerOffset) {
        markerIndex = i;
        break;
      }
    }

    return markerIndex + 1;
  }
}
