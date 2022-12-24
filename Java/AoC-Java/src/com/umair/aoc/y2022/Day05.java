package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.*;

public class Day05 extends Day {

  public Day05() {
    super(5, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    int splitIndex = lines.indexOf("");
    List<String> stackLines = new ArrayList<>(lines.subList(0, splitIndex));
    List<String> instructionLines = new ArrayList<>(lines.subList(splitIndex + 1, lines.size()));

    Map<Integer, List<String>> stackMap = parseStacks(stackLines);
    List<MoveInstruction> instructions = parseInstructions(instructionLines);

    for (MoveInstruction instruction : instructions) {
      int remainingCount = instruction.count;
      List<String> sourceStack = stackMap.get(instruction.source);
      List<String> destinationStack = stackMap.get(instruction.destination);

      assert(remainingCount > 0);

      while (remainingCount > 0) {
        String itemToMove = sourceStack.remove(sourceStack.size() - 1);
        destinationStack.add(itemToMove);
        remainingCount--;
      }
    }

    return getTopsOfAllStacks(stackMap);
  }

  @Override
  protected String part2(List<String> lines) {
    int splitIndex = lines.indexOf("");
    Map<Integer, List<String>> stackMap = parseStacks(new ArrayList<>(lines.subList(0, splitIndex)));
    List<MoveInstruction> instructions =
        parseInstructions(new ArrayList<>(lines.subList(splitIndex + 1, lines.size())));

    for (MoveInstruction instruction : instructions) {
      List<String> sourceStack = stackMap.get(instruction.source);
      List<String> destinationStack = stackMap.get(instruction.destination);

      int startIndex = sourceStack.size() - instruction.count;
      List<String> itemsToMove = sourceStack.subList(startIndex, sourceStack.size());
      // Remove the items from the source, and add them to the destination.
      stackMap.put(instruction.source, sourceStack.subList(0, startIndex));
      destinationStack.addAll(itemsToMove);
    }

    return getTopsOfAllStacks(stackMap);
  }

  @Override
  protected String part1Filename() {
    return fileNameFromFileNumber(2);
  }

  @Override
  protected String part2Filename() {
    return fileNameFromFileNumber(2);
  }

  private static String getTopsOfAllStacks(Map<Integer, List<String>> stackMap) {
    StringBuilder sb = new StringBuilder();
    for (int startingID = 1; startingID <= stackMap.size(); startingID++) {
      List<String> stack = stackMap.get(startingID);
      sb.append(stack.get(stack.size() - 1));
    }
    return sb.toString();
  }

  private static Map<Integer, List<String>> parseStacks(List<String> stackLines) {
    // The last line is the count of stacks. Set up the map.
    Map<Integer, List<String>> stackMap = new HashMap<>();
    String lastLine = stackLines.get(stackLines.size() - 1);
    String[] stackTokens = lastLine.strip().split("(?<=\\G.{4})");
    for (String t : stackTokens) {
      stackMap.put(Integer.parseInt(t.strip()), new ArrayList<>());
    }

    // Remove the last line.
    stackLines.remove(stackLines.size() - 1);

    // Now, go line by line.
    for (String line : stackLines) {
      // Split the line into chunks of length 4. These represent items on the stack.
      String[] tokens = line.split("(?<=\\G.{4})");

      int stackID = 1;
      for (String t : tokens) {
        assert(stackMap.containsKey(stackID));

        if (!t.strip().isBlank()) {
          List<String> stack = stackMap.get(stackID);
          stack.add(t.replace("[", "").replace("]", "").strip());
        }

        stackID += 1;
      }
    }

    // Invert each of the stacks.
    for (Integer key : stackMap.keySet()) {
      List<String> stack = stackMap.get(key);
      Collections.reverse(stack);
      stackMap.put(key, stack);
    }

    return stackMap;
  }

  private static List<MoveInstruction> parseInstructions(List<String> instructionLines) {
    return instructionLines.stream()
        .map(String::strip)
        .map(Day05::parseInstruction)
        .toList();
  }

  private static MoveInstruction parseInstruction(String s) {
    String[] tokens = s.strip().split(" ");
    assert(tokens.length == 6);

    int count = Integer.parseInt(tokens[1].strip());
    int source = Integer.parseInt(tokens[3].strip());
    int destination = Integer.parseInt(tokens[5].strip());
    return new MoveInstruction(source, destination, count);
  }

  private static class MoveInstruction {
    int source; //
    int destination;
    int count;  // Number of items to move.
    MoveInstruction(int source, int destination, int count) {
      this.source = source;
      this.destination = destination;
      this.count = count;
    }
  }
}
