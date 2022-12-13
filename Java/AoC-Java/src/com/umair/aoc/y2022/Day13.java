package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import static com.umair.aoc.util.DataUtils.splitIntoChunks;

/**
 * Day 13: Distress Signal
 * <a href="https://adventofcode.com/2022/day/13">2022, Day-13</a>
 */
public class Day13 extends Day {

  public Day13() {
    super(13, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    List<List<String>> chunks = splitIntoChunks(lines);
    List<Pair<Packet>> packetPairs = chunks.stream()
        .map(l -> l.stream()
            .map(Day13::parsePacket)
            .toList())
        .map(l -> new Pair<>(l.get(0), l.get(1)))
        .toList();

    int sumOfCorrectIndices = 0;

    for (int i = 0; i < packetPairs.size(); i++) {
      var pair = packetPairs.get(i);
      ComparisonResult result = comparePackets(pair.first, pair.second);
      if (result == ComparisonResult.CORRECT) {
        sumOfCorrectIndices += (i + 1);
      }
    }

    return Integer.toString(sumOfCorrectIndices);
  }

  @Override
  protected String part2(List<String> lines) {
    List<Packet> packets = lines.stream()
        .filter(l -> !l.isBlank())
        .map(Day13::parsePacket)
        .collect(Collectors.toList());

    Packet decoder1 = buildDecoderPacket(2);
    Packet decoder2 = buildDecoderPacket(6);
    packets.addAll(List.of(decoder1, decoder2));

    packets.sort((l, r) -> switch (comparePackets(l, r)) {
      case CORRECT -> -1;
      case INCORRECT -> 1;
      case INDETERMINATE -> 0;
    });

    int i = packets.indexOf(decoder1) + 1;
    int j = packets.indexOf(decoder2) + 1;

    return Integer.toString(i * j);
  }

  @Override
  protected String part1Filename() {
    return filenameFromDataFileNumber(2);
  }

  @Override
  protected String part2Filename() {
    return filenameFromDataFileNumber(2);
  }

  private static Packet buildDecoderPacket(int value) {
    Packet result = new Packet(new Packet(new Packet(value)));
    return result;
  }

  private static ComparisonResult comparePackets(Packet left, Packet right) {
    if (left.isValueOnly && right.isValueOnly) {
      if (left.value < right.value) {
        return ComparisonResult.CORRECT;
      } else if (left.value > right.value) {
        return ComparisonResult.INCORRECT;
      } else {
        return ComparisonResult.INDETERMINATE;
      }

    }
    else if (!left.isValueOnly && !right.isValueOnly) {
      int leftIdx = 0;
      int rightIdx = 0;

      while (leftIdx < left.packetsList.size() && rightIdx < right.packetsList.size()) {
        ComparisonResult subResult = comparePackets(
            left.packetsList.get(leftIdx),
            right.packetsList.get(rightIdx));

        if (subResult == ComparisonResult.CORRECT) {
          return ComparisonResult.CORRECT;
        } else if (subResult == ComparisonResult.INCORRECT) {
          return ComparisonResult.INCORRECT;
        }

        // Continue if the subResult is to continue
        leftIdx++;
        rightIdx++;
      }

      if (left.packetsList.size() == right.packetsList.size()) {
        return ComparisonResult.INDETERMINATE;
      } else if (leftIdx == left.packetsList.size()) {
        return ComparisonResult.CORRECT;
      } else {
        return ComparisonResult.INCORRECT;
      }

    }
    else if (left.isValueOnly) {
      Packet p = new Packet();
      p.append(new Packet(left.value));
      return comparePackets(p, right);

    } else { // (right.isValueOnly)
      Packet p = new Packet();
      p.append(new Packet(right.value));
      return comparePackets(left, p);
    }
  }

  private static Packet parsePacket(String line) {
    Stack<Packet> stack = new Stack<>();
    Packet result = null;

    int i = 0;
    StringBuilder sb = new StringBuilder();
    while (i < line.length()) {
      char c = line.charAt(i);
      if (c == '[') {
        var next = new Packet();
        if (!stack.isEmpty()) {
          stack.peek().append(next);
        }
        stack.push(next);

      } else if (c == ']') {
        if (sb.length() > 0) {
          var next = new Packet(Integer.parseInt(sb.toString()));
          stack.peek().append(next);
          sb = new StringBuilder();
        }
        result = stack.pop();

      } else if (c == ',') {
        if (sb.length() > 0) {
          int value = Integer.parseInt(sb.toString());
          var next = new Packet(value);
          stack.peek().append(next);
          sb = new StringBuilder();
        }

      } else if (Character.isDigit(c)) {
        sb.append(c);
      }

      i++;
    }

    return result;
  }

  private enum ComparisonResult {
    CORRECT,
    INCORRECT,
    INDETERMINATE
  }

  /**
   * A nested list.
   */
  private static class Packet {
    List<Packet> packetsList = new ArrayList<>();
    int value;
    boolean isValueOnly = false;

    public Packet() {}
    public Packet(int value) {
      this.value = value;
      isValueOnly = true;
    }

    @SuppressWarnings("CopyConstructorMissesField")
    public Packet(Packet p) {
      packetsList.add(p);
    }

    void append(Packet il) {
      packetsList.add(il);
    }

    @Override
    public String toString() {
      if (isValueOnly) {
        return Integer.toString(value);
      } else {
        return packetsList.toString();
      }
    }
  }

  private record Pair<T>(T first, T second) {}

}
