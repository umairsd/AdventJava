package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Day 11: Monkey in the Middle
 * <a href="https://adventofcode.com/2022/day/11">2022, Day-11</a>
 */
public class Day11 extends Day {

  private static final int NUM_ROUNDS = 20;

  public Day11() {
    super(11, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    List<List<String>> chunks = splitIntoChunks(lines);
    Map<String, Monkey> monkeyMap = chunks
        .stream()
        .map(Day11::parseMonkey)
        .collect(Collectors.toMap(Monkey::getName, Function.identity()));

    int round = 1;
    while (round <= NUM_ROUNDS) {
      for (var entrySet : monkeyMap.entrySet()) {
        Monkey m = entrySet.getValue();
        for (int item : m.getItemsList()) {
          // Inspect the item.
          m.incrementInspectionCount();
          int updatedWorryLevel = m.worryLevelOperation.recalculateWorryLevel(item);
          updatedWorryLevel /= 3;

          // Throw the item.
          String targetMonkey = m.targetMonkeyIdentifier.identify(updatedWorryLevel);
          monkeyMap.get(targetMonkey).getItemsList().add(updatedWorryLevel);
        }
        // Once we're done throwing all the items, clear the current list.
        m.getItemsList().clear();
      }

      round++;
    }

    // Find the two most active monkeys.
    List<Long> activeMonkeys = monkeyMap.values()
        .stream()
        .map(Monkey::getInspectionCount)
        .sorted(Collections.reverseOrder())
        .toList();

    assert (activeMonkeys.size() >= 2);
    long activityLevel = activeMonkeys.get(0) * activeMonkeys.get(1);
    return Long.toString(activityLevel);
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

  private static List<List<String>> splitIntoChunks(List<String> lines) {
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

  private static Monkey parseMonkey(List<String> lines) {
    assert (lines.size() == 6);

    String nameTokens = lines.get(0).strip().split(" ")[1].strip();
    String name = nameTokens.substring(0, nameTokens.length() - 1);

    String itemsLine = lines.get(1).strip();
    int itemsTokenIndex = itemsLine.indexOf(':');
    String itemsSegment = itemsLine.substring(itemsTokenIndex + 1);
    String[] itemsToken = itemsSegment.strip().split(",");
    List<Integer> items = Arrays.stream(itemsToken)
        .map(String::strip)
        .map(Integer::parseInt)
        .toList();

    WorryLevelOperation operation = parseWorryLevelOperation(lines.get(2));
    int divisor = Integer.parseInt(getEndOfLineToken(lines.get(3)));
    String monkeyIfTrue = getEndOfLineToken(lines.get(4));
    String monkeyIfFalse = getEndOfLineToken(lines.get(5));

    Monkey monkey = new Monkey.MonkeyBuilder()
        .setName(name)
        .setWorryLevelOperation(operation)
        .setTestDivisor(divisor)
        .setTargetMonkeyIdentifier(i -> (i % divisor == 0 ? monkeyIfTrue : monkeyIfFalse))
        .build();
    // Add all the items to the monkey.
    items.forEach(i -> monkey.getItemsList().add(i));
    return monkey;
  }

  private static WorryLevelOperation parseWorryLevelOperation(String line) {
    String[] t1 = line.strip().split("=");
    String[] tokens = t1[1].strip().split(" ");
    assert (tokens.length == 3);

    Operator op = switch (tokens[1]) {
      case "+" -> Operator.ADD;
      case "*" -> Operator.MULTIPLY;
      case "-" -> Operator.SUBTRACT;
      case "/" -> Operator.DIVIDE;
      default -> throw new IllegalArgumentException();
    };

    WorryLevelOperation operation = new WorryLevelOperation(op, tokens[2].strip());
    return operation;
  }

  private static String getEndOfLineToken(String s) {
    String[] tokens = s.strip().split(" ");
    return tokens[tokens.length - 1].strip();
  }

  private enum Operator {
    ADD,
    MULTIPLY,
    SUBTRACT,
    DIVIDE
  }

  static class Monkey {
    private final List<Integer> items = new ArrayList<>();
    private final String name;
    private final WorryLevelOperation worryLevelOperation;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final int testDivisor;
    private final TargetMonkeyIdentifier targetMonkeyIdentifier;
    private long inspectionCount = 0;

    private Monkey(
        String name,
        WorryLevelOperation worryLevelOperation,
        int testDivisor,
        TargetMonkeyIdentifier targetMonkeyIdentifier
    ) {
      this.name = name;
      this.testDivisor = testDivisor;
      this.worryLevelOperation = worryLevelOperation;
      this.targetMonkeyIdentifier = targetMonkeyIdentifier;
    }

    private void incrementInspectionCount() {
      inspectionCount++;
    }

    private long getInspectionCount() {
      return inspectionCount;
    }

    private String getName() {
      return name;
    }

    private List<Integer> getItemsList() {
      return items;
    }

    @Override
    public String toString() {
      return "Monkey " + name;
    }

    private static class MonkeyBuilder {
      private String name;
      private WorryLevelOperation worryLevelOperation;
      private int testDivisor;
      private TargetMonkeyIdentifier targetMonkeyIdentifier;

      private MonkeyBuilder setName(String name) {
        this.name = name;
        return this;
      }

      private MonkeyBuilder setWorryLevelOperation(WorryLevelOperation operation) {
        this.worryLevelOperation = operation;
        return this;
      }

      private MonkeyBuilder setTestDivisor(int testDivisor) {
        this.testDivisor = testDivisor;
        return this;
      }

      private MonkeyBuilder setTargetMonkeyIdentifier(TargetMonkeyIdentifier monkeyIdentifier) {
        this.targetMonkeyIdentifier = monkeyIdentifier;
        return this;
      }

      private Monkey build() {
        return new Monkey(name, worryLevelOperation, testDivisor, targetMonkeyIdentifier);
      }
    }
  }

  private static class WorryLevelOperation {
    Operator op;
    String secondArgumentStr;

    private WorryLevelOperation(Operator op, String secondArgumentStr) {
      this.op = op;
      this.secondArgumentStr = secondArgumentStr;
    }

    private int recalculateWorryLevel(int firstArgument) {
      int secondArgument = secondArgumentStr.equals("old")
          ? firstArgument
          : Integer.parseInt(secondArgumentStr);
      int result = switch (op) {
        case ADD -> firstArgument + secondArgument;
        case MULTIPLY -> firstArgument * secondArgument;
        case SUBTRACT -> firstArgument - secondArgument;
        case DIVIDE -> firstArgument / secondArgument;
      };

      return result;
    }
  }

  @FunctionalInterface
  private interface TargetMonkeyIdentifier {
    /**
     * Takes the fully resolved worryLevel, and returns the name of the target monkey.
     */
    String identify(int worryLevel);
  }
}
