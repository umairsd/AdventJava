// Created on 12/9/23.

import Foundation
import RegexBuilder

/// --- Day 8: Haunted Wasteland ---
/// https://adventofcode.com/2023/day/8
class Y2023Day08: Day {
  var dayNumber: Int = 08
  var year: Int = 2023
  var dataFileNumber: Int

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }

  func part1(_ lines: [String]) -> String {
    let directions = parseDirections(lines.first!)
    let graph = parseGraph(Array(lines.dropFirst()))

    var steps = 0
    var i = 0
    var currentNode = graph.root!
    while currentNode.name != "ZZZ" {
      let direction = directions[i]
      currentNode = switch direction {
      case .left:
        currentNode.left
      case .right:
        currentNode.right
      }
      steps += 1
      i = (i + 1) % directions.count
    }

    return "\(steps)"
  }


  func part2(_ lines: [String]) -> String {
    ""
  }
}


// MARK: - Extension

extension Y2023Day08 {
  private static let rootNameRef = Reference(String.self)
  private static let leftNameRef = Reference(String.self)
  private static let rightNameRef = Reference(String.self)

  private static let lineRegex = Regex {
    TryCapture(as: rootNameRef) {
      OneOrMore(.any)
    } transform: { match in
      String(match)
    }
    " = ("
    TryCapture(as: leftNameRef) {
      OneOrMore(.any)
    } transform: { match in
      String(match)
    }
    ", "
    TryCapture(as: rightNameRef) {
      OneOrMore(.any)
    } transform: { match in
      String(match)
    }
    ")"
  }


  fileprivate func parseDirections(_ line: String) -> [Direction] {
    let dirs = Array(line).compactMap { Direction.init(rawValue: String($0)) }
    return dirs
  }


  fileprivate func parseGraph(_ lines: [String]) -> Graph {
    var graph = Graph()
    lines.forEach { parseNodesFrom($0, into: &graph) }
    graph.root = graph.nodesMap["AAA"]
    return graph
  }


  fileprivate func parseNodesFrom(_ line: String, into graph: inout Graph) {
    guard let match = line.firstMatch(of: Self.lineRegex) else {
      return
    }
    let rootName = match[Self.rootNameRef]
    let leftName = match[Self.leftNameRef]
    let rightName = match[Self.rightNameRef]
    
    let leftNode = graph.nodesMap[leftName, default: Node(name: leftName)]
    graph.nodesMap[leftName] = leftNode

    let rightNode = graph.nodesMap[rightName, default: Node(name: rightName)]
    graph.nodesMap[rightName] = rightNode

    let rootNode = graph.nodesMap[rootName, default: Node(name: rootName)]
    rootNode.left = leftNode
    rootNode.right = rightNode
    graph.nodesMap[rootName] = rootNode
  }
}


// MARK: - Helpful Types


fileprivate enum Direction: String {
  case left = "L"
  case right = "R"
}


fileprivate class Graph {
  var root: Node!
  var nodesMap: [String: Node] = [:]
}


fileprivate class Node {
  let name: String
  // Force unwrapped optional is OK, as once the graph is built, there will be no nodes
  // that have a nil left or right node.
  var left: Node!
  var right: Node!

  init(name: String, left: Node? = nil, right: Node? = nil) {
    self.name = name
    self.left = left
    self.right = right
  }
}
