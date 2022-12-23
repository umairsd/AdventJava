package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.*;

public class Day07 extends Day {

  public Day07() {
    super(7, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    FSNode root = parseFilesystem(lines);
    traverseTreeToComputeDirectorySizes(root);

    long totalSizeOfSmallDirectories = traverseTreeToComputeDirectoriesBelowThreshold(root,
        100_000);
    return Long.toString(totalSizeOfSmallDirectories);
  }

  @Override
  protected String part2(List<String> lines) {
    FSNode root = parseFilesystem(lines);
    traverseTreeToComputeDirectorySizes(root);

    long totalFileSize = root.size;
    long totalFilesystemSize = 70_000_000;
    long spaceNeededForUpgrade = 30_000_000;

    long availableSpace = totalFilesystemSize - totalFileSize;
    long spaceToBeDeleted = spaceNeededForUpgrade - availableSpace;

    List<Long> sizes = new ArrayList<>();
    traverseTreeToCollectSizesAboveThreshold(root, spaceToBeDeleted, sizes);
    Collections.sort(sizes);
    return Long.toString(sizes.get(0));
  }

  @Override
  protected String part1Filename() {
    return fileNameFromFileNumber(2);
  }

  @Override
  protected String part2Filename() {
    return fileNameFromFileNumber(2);
  }

  /**
   * Traversal of the tree to collect all directory sizes above the given threshold.
   */
  private static void traverseTreeToCollectSizesAboveThreshold(
      FSNode root,
      long threshold,
      List<Long> collector
  ) {
    if (root.type == FileType.FILE) {
      return;
    }
    if (root.size >= threshold) {
      collector.add(root.size);
    }

    for (String childName : root.children.keySet()) {
      FSNode child = root.children.get(childName);
      traverseTreeToCollectSizesAboveThreshold(child, threshold, collector);
    }
  }

  /**
   * Traversal of the tree to compute the total size of all directories below the given threshold.
   */
  private static long traverseTreeToComputeDirectoriesBelowThreshold(FSNode root, long threshold) {
    if (root.type == FileType.FILE) {
      return 0;
    }

    long totalSize = root.size <= threshold ? root.size : 0;
    for (String childName : root.children.keySet()) {
      FSNode child = root.children.get(childName);
      totalSize += traverseTreeToComputeDirectoriesBelowThreshold(child, threshold);
    }
    return totalSize;
  }

  /**
   * Post order traversal of the tree to compute the total size of all directories.
   */
  private static long traverseTreeToComputeDirectorySizes(FSNode root) {
    if (root.type == FileType.FILE) {
      return root.size;
    }

    long totalSizeOfChildren = 0;
    for (String childName : root.children.keySet()) {
      FSNode child = root.children.get(childName);
      totalSizeOfChildren += traverseTreeToComputeDirectorySizes(child);
    }

    root.size = totalSizeOfChildren;
    return totalSizeOfChildren;
  }

  private static final String LINE_START_COMMAND = "$";
  private static final String LINE_START_CD = "$ cd";

  private static FSNode parseFilesystem(List<String> lines) {
    FSNode root = new FSNode("/", FileType.DIRECTORY);
    FSNode currentNode = root;

    Stack<FSNode> stack = new Stack<>();
    stack.push(root);

    for (String line : lines.stream().skip(1).toList()) {
      if (line.startsWith(LINE_START_COMMAND)) {
        if (line.startsWith(LINE_START_CD)) {
          String[] tokens = line.split(" ");
          String path = tokens[2].strip();

          if (path.equals("..")) {
            stack.pop();
            currentNode = stack.peek();

          } else if (currentNode.children.containsKey(path)) {
            currentNode = currentNode.children.get(path);
            stack.push(currentNode);

          } else {
            throw new IllegalArgumentException();
          }
        }
      } else {
        FSNode file = parseFSNode(line);
        currentNode.children.put(file.name, file);
      }
    }
    return root;
  }

  private static FSNode parseFSNode(String line) {
    if (line.strip().startsWith("dir")) {
      return parseDirectory(line);
    } else {
      return parseFile(line);
    }
  }

  private static FSNode parseFile(String line) {
    String[] tokens = line.strip().split(" ");
    assert (tokens.length == 2);
    long size = Long.parseLong(tokens[0]);
    FSNode fileNode = new FSNode(tokens[1], FileType.FILE);
    fileNode.setSize(size);
    return fileNode;
  }

  private static FSNode parseDirectory(String line) {
    String[] tokens = line.strip().split(" ");
    assert (tokens[0].equals("dir"));
    assert (tokens.length == 2);
    FSNode node = new FSNode(tokens[1], FileType.DIRECTORY);
    return node;
  }

  private enum FileType {
    FILE,
    DIRECTORY,
  }

  private static class FSNode {
    // If a file, size is the size of the file. If a directory, sum of all the children.
    private long size = 0;
    private final FileType type;
    private final String name;
    private final Map<String, FSNode> children = new HashMap<>();

    FSNode(String name, FileType type) {
      this.type = type;
      this.name = name;
    }

    private void setSize(long size) {
      this.size = size;
    }

    @Override
    public String toString() {
      return name
          + " ("
          + (type == FileType.FILE ? "f" : "d")
          + ", size="
          + size
          + ")";
    }
  }
}
