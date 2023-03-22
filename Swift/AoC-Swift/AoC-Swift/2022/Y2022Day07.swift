// Created on 3/21/23.

import Foundation
import RegexBuilder

class Y2022Day07: Day {

  var dayNumber: Int = 7
  var year: Int = 2022
  var dataFileNumber: Int = 2

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }

  func part1(_ lines: [String]) -> String {
    guard let root = parseFileSystem(from: lines) else {
      return ""
    }

    var filteredNodes: [FileSystemNode] = []
    // Filter directories with size that's less than 100_000
    root.filter(into: &filteredNodes) { node in
      if case .directory = node {
        return node.size() <= 100_000
      }
      return false
    }

    let total = filteredNodes.map( { $0.size() }).reduce(0, +)
    return "\(total)"
  }
  

  func part2(_ lines: [String]) -> String {
    ""
  }

}

/// Represents a file system node.
fileprivate enum FileSystemNode {

  case file(name: String, size: Int)
  case directory(name: String, size: Int, children: [FileSystemNode])

  func size() -> Int {
    switch self {
      case .file(_, let size):
        return size
      case .directory(_, let size, _):
        return size
    }
  }

  func filter(into result: inout [FileSystemNode], _ isIncluded: (FileSystemNode) -> Bool) {
    switch self {
      case .file:
        if isIncluded(self) {
          result.append(self)
        }
        return
      case .directory(_, _, let children):
        if isIncluded(self) {
          result.append(self)
        }
        for c in children {
          c.filter(into: &result, isIncluded)
        }
    }
  }
}


// MARK: - Parsing

extension Y2022Day07 {

  private static let listFilesCommand = "$ ls"
  private static let goUpOneLevelCommand = "$ cd .."

  private static let commandCD = Regex {
    "$ cd "
    Capture {
      OneOrMore(.any)
    }
  }

  private static let commandLS = Regex {
    "$ ls"
  }

  private static let directoryRegex = Regex {
    "dir "
    Capture {
      OneOrMore(.any)
    }
  }

  private static let fileRegex = Regex {
    Capture {
      OneOrMore(.digit)
    }
    " "
    Capture {
      OneOrMore(.any)
    }
  }


  private func parseFileSystem(from lines: [String]) -> FileSystemNode? {
    let map = buildDirectoryMap(from: lines)
    let root = buildFileSystemNode(from: "dir /", map)
    return root
  }


  private func buildDirectoryMap(from lines: [String]) -> [String: [String]] {
    var map: [String: [String]] = [:]
    // A stack to maintain the current directory.
    var stack: [String] = []

    for line in lines {
      if line == Self.listFilesCommand {
        continue
      }
      else if line == Self.goUpOneLevelCommand {
        // Going up a level.
        stack.removeLast()
      }
      else if line.starts(with: Self.commandCD) {
        guard let match = line.firstMatch(of: Self.commandCD) else {
          continue
        }
        // Store the key in the form "dir <name>", as the lines are also stored in this form.
        // This makes it easier to recursively build the `FileSystemNode` structure.
        let name = "dir \(String(match.1))"
        stack.append(name)
      }
      else {
        guard let dirName = stack.last else {
          fatalError("Attempting to process a file even though there's no directory on the stack")
        }
        guard !line.isEmpty else {
          continue
        }
        map[dirName, default: []].append(line)
      }
    }
    return map
  }


  private func buildFileSystemNode(
    from line: String,
    _ directoryMap: [String: [String]]
  ) -> FileSystemNode? {

    if line.starts(with: Self.directoryRegex) {
      guard let result = line.firstMatch(of: Self.directoryRegex) else { return nil }
      let name = String(result.1)

      // The key is in the form "dir <name>". `values` is the list of directory contents.
      guard let values = directoryMap[line] else {
        return nil
      }

      let childrenNodes = values.compactMap { buildFileSystemNode(from: $0, directoryMap) }
      let size = childrenNodes.map( { $0.size() }).reduce(0, +)
      return .directory(name: name, size: size, children: childrenNodes)

    }
    else if line.starts(with: Self.fileRegex) {
      guard let result = line.firstMatch(of: Self.fileRegex) else { return nil }
      guard let size = Int(result.1) else { return nil }

      let name = String(result.2)
      return .file(name: name, size: size)

    }
    else {
      fatalError("Bad data. Should not happen")
    }
  }
}

