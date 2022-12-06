package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day06 extends Day {

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
    for (int i = 4; i < characters.length; i++) {
      seen.clear();
      char currentC = characters[i];
      seen.add(currentC);

      int j = i - 1;
      while (j > (i - 4)) {
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
}
