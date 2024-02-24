package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Day 21: Monkey Math
 * <a href="https://adventofcode.com/2022/day/21">2022, Day-21</a>
 */
public class Day21 extends Day {

  private static final String HUMAN_NAME = "humn";

  public Day21() {
    super(21, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    Node root = parseTree(lines);
    long result = preorderCompute(root);
    return Long.toString(result);
  }

  @Override
  protected String part2(List<String> lines) {
    Node root = parseTree(lines);
    boolean isInLeft = isInTree(HUMAN_NAME, root.left);
    long result;
    if (isInLeft) {
      // Get the value of the right subtree. This is the expected value that the left
      // subtree should produce.
      long rightResult = preorderCompute(root.right);
      result = valueOfHumanNodeToGeneratedExpectedResult(root.left, rightResult, HUMAN_NAME);

    } else {
      // Get the value of the left subtree. This is the expected value that the right
      // subtree should produce.
      long leftResult = preorderCompute(root.left);
      result = valueOfHumanNodeToGeneratedExpectedResult(root.right, leftResult, HUMAN_NAME);
    }
    return Long.toString(result);
  }



  private static long valueOfHumanNodeToGeneratedExpectedResult(
      Node root,
      long expectedResult,
      String humanNodeName
  ) {
    if (root == null) {
      throw new IllegalStateException();
    }
    if (root.name.equals(humanNodeName)) {
      return expectedResult;
    }

    boolean isInLeft = isInTree(humanNodeName, root.left);
    if (isInLeft) {
      long rightResult = preorderCompute(root.right);
      long expectedLeftResult = switch (root.operation) {
        // ex = l + r; => l = ex - r
        case ADD -> expectedResult - rightResult;
        // ex = l - r; => l = ex + r
        case SUBTRACT -> expectedResult + rightResult;
        // ex = l * r; => l = ex / r
        case MULTIPLY -> expectedResult / rightResult;
        // ex = l / r; => l => ex * r
        case DIVIDE -> expectedResult * rightResult;
        default -> throw new IllegalStateException();
      };
      return valueOfHumanNodeToGeneratedExpectedResult(
          root.left,
          expectedLeftResult,
          humanNodeName);

    } else {
      long leftResult = preorderCompute(root.left);
      long expectedRightResult = switch (root.operation) {
        // ex = l + r; => r = ex - l
        case ADD -> expectedResult - leftResult;
        // ex = l - r; => r = l - ex
        case SUBTRACT -> leftResult - expectedResult;
        // ex = l * r; => r = ex / l
        case MULTIPLY -> expectedResult / leftResult;
        // ex = l / r; => r = l / ex
        case DIVIDE -> leftResult / expectedResult;
        // default
        default -> throw new IllegalStateException();
      };
      return valueOfHumanNodeToGeneratedExpectedResult(
          root.right,
          expectedRightResult,
          humanNodeName);
    }
  }

  private static long preorderCompute(Node root) {
    if (root == null) {
      return 0;
    }
    long leftValue = preorderCompute(root.left);
    long rightValue = preorderCompute(root.right);
    long result = switch (root.operation) {
      case LITERAL -> root.value;
      case ADD -> leftValue + rightValue;
      case SUBTRACT -> leftValue - rightValue;
      case MULTIPLY -> leftValue * rightValue;
      case DIVIDE -> leftValue / rightValue;
    };
    return result;
  }

  private static boolean isInTree(String nodeName, Node root) {
    if (root == null) {
      return false;
    }

    if (root.name.equals(nodeName)) {
      return true;
    }

    boolean isInLeft = isInTree(nodeName, root.left);
    boolean isInRight = isInTree(nodeName, root.right);
    return isInLeft || isInRight;
  }

  private static Node parseTree(List<String> lines) {
    Map<String, NodeData> dataMap = lines.stream()
        .map(Day21::parseNodeData)
        .collect(Collectors.toMap(NodeData::name, Function.identity()));

    Node root = buildTree("root", dataMap);
    return root;
  }

  private static Node buildTree(String nodeName, Map<String, NodeData> dataMap) {
    if (nodeName == null) {
      return null;
    }
    if (!dataMap.containsKey(nodeName)) {
      throw new IllegalStateException("Node name not found in the data map: " + nodeName);
    }

    Node root = new Node(nodeName);
    NodeData data = dataMap.get(nodeName);
    if (data.value != null) {
      // This is a leaf node.
      root.value = Integer.parseInt(data.value);
      root.operation = Operation.LITERAL;
      return root;
    }
    root.operation = parseOperation(data.op);
    root.left = buildTree(data.leftName, dataMap);
    root.right = buildTree(data.rightName, dataMap);
    return root;
  }

  private static Operation parseOperation(String s) {
    if (s == null) {
      return Operation.LITERAL;
    }

    return switch (s) {
      case "+" -> Operation.ADD;
      case "-" -> Operation.SUBTRACT;
      case "*" -> Operation.MULTIPLY;
      case "/" -> Operation.DIVIDE;
      default -> throw new IllegalArgumentException("Cannot parse operation for " + s);
    };
  }

  private static final Pattern nodePattern = Pattern.compile("(.*): (.*) (.*) (.*)");
  private static final Pattern leafPattern = Pattern.compile("(.*): (.*)");

  private static NodeData parseNodeData(String line) {
    Matcher leafMatcher = leafPattern.matcher(line);
    Matcher nodeMatcher = nodePattern.matcher(line);

    if (nodeMatcher.matches()) {
      var name = nodeMatcher.group(1);
      var leftName = nodeMatcher.group(2);
      var operation = nodeMatcher.group(3);
      var rightName = nodeMatcher.group(4);
      NodeData node = new NodeData(name, leftName, operation, null, rightName);
      return node;

    } else if (leafMatcher.matches()) {
      var name = leafMatcher.group(1);
      var value = leafMatcher.group(2);

      NodeData node = new NodeData(name, null, null, value, null);
      return node;

    } else {
      throw new IllegalStateException("Bad input line: " + line);
    }
  }

  private record NodeData(String name, String leftName, String op, String value, String rightName) {
  }

  private enum Operation {
    LITERAL,
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE
  }

  private static class Node {
    String name;
    Operation operation;
    Node left;
    Node right;
    int value;

    Node(String name) {
      this.name = name;
    }
  }
}
