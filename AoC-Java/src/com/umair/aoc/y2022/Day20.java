package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Day 20: Grove Positioning System
 * <a href="https://adventofcode.com/2022/day/20">2022, Day-20</a>
 */
public class Day20 extends Day {

  public Day20() {
    super(20, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    List<IndexedNumber> indexedNumbers = parseIndexedNumbers(lines);
    decrypt(indexedNumbers);
    long result = groveCoordinates(indexedNumbers);
    return Long.toString(result);
  }

  @Override
  protected String part2(List<String> lines) {
    long key = 811_589_153;
    List<IndexedNumber> indexedNumbers = parseIndexedNumbers(lines).stream()
        .map(i -> new IndexedNumber(i.index, i.value * key))
        .collect(Collectors.toList());

    int round = 0;
    while (round++ < 10) {
      decrypt(indexedNumbers);
    }

    long result = groveCoordinates(indexedNumbers);
    return Long.toString(result);
  }

  @Override
  protected String part1Filename() {
    return fileNameFromFileNumber(2);
  }

  @Override
  protected String part2Filename() {
    return fileNameFromFileNumber(2);
  }

  private static long groveCoordinates(List<IndexedNumber> numbers) {
    int indexOfZero = IntStream.range(0, numbers.size())
        .filter(i -> numbers.get(i).value == 0L)
        .findFirst()
        .orElseThrow();

    AtomicLong sum = new AtomicLong();
    List.of(1000, 2000, 3000).forEach(i -> {
      var index = (i + indexOfZero) % numbers.size();
      sum.addAndGet(numbers.get(index).value);
    });

    return sum.get();
  }

  private static void decrypt(List<IndexedNumber> numbers) {
    for (int originalIndex = 0; originalIndex < numbers.size(); originalIndex++) {
      int finalOriginalIndex = originalIndex;

      int index = IntStream.range(0, numbers.size())
          .filter(i -> numbers.get(i).index == finalOriginalIndex)
          .findFirst()
          .orElseThrow();

      // Pull numbers out from current index and insert into the new index.
      var toBeMoved = numbers.remove(index);

      // New index is current index plus value.
      int newIndex = (int)Math.floorMod(index + toBeMoved.value, (long)numbers.size());
      numbers.add(newIndex, toBeMoved);
    }
  }

  private static List<IndexedNumber> parseIndexedNumbers(List<String> lines) {
    List<IndexedNumber> numbers = new ArrayList<>();
    for (int i = 0; i < lines.size(); i++) {
      numbers.add(new IndexedNumber(i, Long.parseLong(lines.get(i))));
    }
    return numbers;
  }

  private record IndexedNumber(int index, long value) {}
}
