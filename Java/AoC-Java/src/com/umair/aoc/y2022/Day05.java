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
    List<String> stackLines = new ArrayList(lines.subList(0, splitIndex));
    List<String> instructionLines = new ArrayList(lines.subList(splitIndex + 1, lines.size()));

    Map<Integer, Stack<String>> stackMap = parseStacks(stackLines);
    List<MoveInstruction> instructions = parseInstructions(instructionLines);

    for (MoveInstruction instruction : instructions) {
      int remainingCount = instruction.count;
      Stack<String> sourceStack = stackMap.get(instruction.source);
      Stack<String> destinationStack = stackMap.get(instruction.destination);

      assert(remainingCount >= sourceStack.size());

      while (remainingCount > 0) {
        destinationStack.push(sourceStack.pop());
        remainingCount--;
      }
    }

    StringBuilder sb = new StringBuilder();
    for (int startingID = 1; startingID <= stackMap.size(); startingID++) {
      sb.append(stackMap.get(startingID).peek());
    }

    return sb.toString();
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

  private static Map<Integer, Stack<String>> parseStacks(List<String> stackLines) {
    // The last line is the count of stacks. Set up the map.
    Map<Integer, Stack<String>> stackMap = new HashMap<>();
    String lastLine = stackLines.get(stackLines.size() - 1);
    String[] stackTokens = lastLine.strip().split("(?<=\\G.{4})");;
    int stackCount = stackTokens.length;

    for (String t : stackTokens) {
      stackMap.put(Integer.parseInt(t.strip()), new Stack<>());
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
      Stack<String> stack = stackMap.get(key);
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
