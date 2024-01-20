package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.umair.aoc.util.DataUtils.splitIntoChunks;

/**
 * Day 11: Monkey in the Middle
 * <a href="https://adventofcode.com/2022/day/11">2022, Day-11</a>
 */
public class Day11 extends Day {

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


    long divisor = 3;
    long activityLevel = runSimulation(monkeyMap, 20, (worryLevel -> worryLevel / divisor));
    return Long.toString(activityLevel);
  }

  @Override
  protected String part2(List<String> lines) {
    Map<String, Monkey> monkeyMap = splitIntoChunks(lines)
        .stream()
        .map(Day11::parseMonkey)
        .collect(Collectors.toMap(Monkey::getName, Function.identity()));

    long lcm = monkeyMap.values().stream().map(m -> m.testDivisor).reduce(1L, (a, b) -> a * b);
    long activityLevel = runSimulation(monkeyMap, 10_000, (worryLevel -> worryLevel % lcm));
    return Long.toString(activityLevel);
  }

  private static long runSimulation(
      Map<String, Monkey> monkeyMap,
      int numRounds,
      WorryManager worryManager
  ) {
    int round = 1;
    while (round <= numRounds) {
      for (var entrySet : monkeyMap.entrySet()) {
        Monkey m = entrySet.getValue();
        for (long item : m.getItemsList()) {
          // Inspect the item.
          m.incrementInspectionCount();
          long updatedWorryLevel = m.worryLevelOperation.recalculateWorryLevel(item);

          updatedWorryLevel = worryManager.manage(updatedWorryLevel);

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
    return activityLevel;
  }

  @Override
  protected String part1Filename() {
    return fileNameFromFileNumber(1);
  }

  @Override
  protected String part2Filename() {
    return fileNameFromFileNumber(1);
  }

  private static Monkey parseMonkey(List<String> lines) {
    assert (lines.size() == 6);

    String nameTokens = lines.get(0).strip().split(" ")[1].strip();
    String name = nameTokens.substring(0, nameTokens.length() - 1);

    String itemsLine = lines.get(1).strip();
    int itemsTokenIndex = itemsLine.indexOf(':');
    String itemsSegment = itemsLine.substring(itemsTokenIndex + 1);
    String[] itemsToken = itemsSegment.strip().split(",");
    List<Long> items = Arrays.stream(itemsToken)
        .map(String::strip)
        .map(Long::parseLong)
        .toList();

    WorryLevelOperation operation = parseWorryLevelOperation(lines.get(2));
    long divisor = Long.parseLong(getEndOfLineToken(lines.get(3)));
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
    private final List<Long> items = new ArrayList<>();
    private final String name;
    private final WorryLevelOperation worryLevelOperation;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final long testDivisor;
    private final TargetMonkeyIdentifier targetMonkeyIdentifier;
    private long inspectionCount = 0;

    private Monkey(
        String name,
        WorryLevelOperation worryLevelOperation,
        long testDivisor,
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

    private List<Long> getItemsList() {
      return items;
    }

    @Override
    public String toString() {
      return "Monkey " + name;
    }

    private static class MonkeyBuilder {
      private String name;
      private WorryLevelOperation worryLevelOperation;
      private long testDivisor;
      private TargetMonkeyIdentifier targetMonkeyIdentifier;

      private MonkeyBuilder setName(String name) {
        this.name = name;
        return this;
      }

      private MonkeyBuilder setWorryLevelOperation(WorryLevelOperation operation) {
        this.worryLevelOperation = operation;
        return this;
      }

      private MonkeyBuilder setTestDivisor(long testDivisor) {
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

    private long recalculateWorryLevel(long firstArgument) {
      long secondArgument = secondArgumentStr.equals("old")
          ? firstArgument
          : Long.parseLong(secondArgumentStr);
      long result = switch (op) {
        case ADD -> firstArgument + secondArgument;
        case MULTIPLY -> firstArgument * secondArgument;
        case SUBTRACT -> firstArgument - secondArgument;
        case DIVIDE -> firstArgument / secondArgument;
      };

      return result;
    }
  }

  /**
   * Lambda that determines the target monkey based on the current worry level.
   */
  @FunctionalInterface
  private interface TargetMonkeyIdentifier {
    String identify(long worryLevel);
  }

  /**
   * Lambda that manages the worry level for an item by generating the updated worry levels.
   */
  @FunctionalInterface
  private interface WorryManager {
    long manage(long worryLevel);
  }
}
