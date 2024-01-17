// Created on 1/12/24.

import Foundation
import RegexBuilder

/// --- Day 25: Snowverload ---
/// https://adventofcode.com/2023/day/25
/// 
class Y2023Day25: Day {
  var dayNumber: Int = 25
  var year: Int = 2023
  var dataFileNumber: Int

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }


  func part1(_ lines: [String]) -> String {
    let graph = parseGraph(lines)
    let minCut = Graph.minimumCut(graph)
    return ""
  }


  func part2(_ lines: [String]) -> String {
    ""
  }
}


// MARK: - Types

fileprivate struct Edge: Hashable {
  let source: String
  let destination: String
  let weight: Int

  func hash(into hasher: inout Hasher) {
    hasher.combine(source)
    hasher.combine(destination)
  }
}


fileprivate class Graph {
  //  private let adjacencyList: [String: [String]]
  private var adjacencyEdges: [String: [Edge]]
  private var allVertices: Set<String>

  init(adjacencyEdges: [String : [Edge]] = [:]) {
    self.adjacencyEdges = adjacencyEdges
    self.allVertices = Set(adjacencyEdges.keys)
      .union(Set(adjacencyEdges.values.joined().map { $0.source }))
      .union(adjacencyEdges.values.joined().map { $0.destination})
  }

  var vertexCount: Int {
    return allVertices.count
  }

  func edge(from: String, to: String) -> Edge? {
    guard let v = adjacencyEdges[from] else {
      return nil
    }
    return v.first { $0.destination == to }
  }

  func vertices() -> [String] {
    return Array(allVertices)
  }

  func edges() -> [Edge] {
    Array(adjacencyEdges.values.joined())
  }

  func containsVertex(_ v: String) -> Bool {
    allVertices.contains(v)
  }

  func addEdge(from source: String, to destination: String, weight: Int) {
    var x = adjacencyEdges[source, default: []]
    x.append(Edge(source: source, destination: destination, weight: weight))
    adjacencyEdges[source] = x

    var y = adjacencyEdges[destination, default: []]
    y.append(Edge(source: destination, destination: source, weight: weight))
    adjacencyEdges[destination] = y
  }


  func graphByMergingVertex(_ t: String, into s: String) -> Graph {
    let originalGraph = self
    let mergedNodeName = "\(s),\(t)"

    var adjacencyEdges: [String: [Edge]] = originalGraph.adjacencyEdges

    // Move all edges from `x` to `t` `Edge(x-t)` so that they point to `s`, i.e. `Edge(x-s)`.
    // If there already is an `Edge(x-s)`, then update its weight by adding the weight
    // of `Edge(x-t)`.
    let edgesFromT = adjacencyEdges.removeValue(forKey: t)!

    for edgeFromT in edgesFromT {
      let x = edgeFromT.destination

      var edgesFromX = adjacencyEdges[x, default: []]
      guard let xToT = edgesFromX.filter({ $0.destination == t }).first else {
        continue
      }

      // Remove all edges from (x-t).
      edgesFromX.removeAll { $0.destination == t }

      if let xToS = edgesFromX.filter({ $0.destination == s }).first {
        edgesFromX.removeAll { $0.destination == s }
        let newXToS = Edge(source: x, destination: s, weight: xToS.weight + xToT.weight)
        edgesFromX.append(newXToS)
      } else {
        let newXToS = Edge(source: x, destination: s, weight: xToT.weight)
        edgesFromX.append(newXToS)
      }

      // TODO: Rename the vertex to `mergedNodeName`
      adjacencyEdges[x] = edgesFromX
    }

    let newGraph = Graph(adjacencyEdges: adjacencyEdges)
    return newGraph
  }
}



fileprivate extension Graph {

  struct CutPhaseResult {
    let vertexS: String
    let vertexT: String
    let weight: Int
  }

  struct MinCutResult {
    let first: Graph
    let second: Graph
    let edgesOnTheCut: [Edge]
    let cutWeight: Int
  }


  /// MinimumCutPhase, aka "maximum adjacency search", which results in finding two
  /// vertices `s` and `t`, which are the last two vertices that were found during the 
  /// search. In addition, we return the sum of the weights of the edges connecting
  /// to `t` from the rest of the graph, including `s`.
  ///
  /// ```
  /// MinimumCutPhase(G, a):
  ///   A <- {a}
  ///   while A != V:
  ///     add to A the most tightly connected vertex
  ///   return s, t, and the cut weight as the "cut of the phase"
  /// ```
  ///
  static func minimumCutPhase(_ graph: Graph) -> CutPhaseResult {
    let vertices: [String] = graph.vertices()

    let start = vertices[0]
    var foundSet = [start]
    var cutWeight: [Int] = []
    var candidates: Set<String> = Set(vertices)
    candidates.remove(start)

    while !candidates.isEmpty {
      var maxNextVertex: String!
      var maxWeight = Int.min

      for nextVertex in candidates {
        var weightSum = 0
        for s in foundSet {
          guard let edge = graph.edge(from: nextVertex, to: s) else {
            continue
          }
          weightSum += edge.weight
        }

        if weightSum > maxWeight {
          maxNextVertex = nextVertex
          maxWeight = weightSum
        }
      }

      candidates.remove(maxNextVertex)
      foundSet.append(maxNextVertex)
      cutWeight.append(maxWeight)
    }

    assert(foundSet.count >= 2)
    let n = foundSet.count
    // Take the last two vertices and their weight as a cut of the phase.
    return CutPhaseResult(
      vertexS: foundSet[n - 2],
      vertexT: foundSet[n - 1],
      weight: cutWeight.last!)
  }



  static func minimumCut(_ g: Graph) -> MinCutResult {
    var graph = g
    var currentPartition = Set<String>()
    var bestPartition = Set<String>()
    var bestCut: CutPhaseResult!

    while graph.vertexCount > 1 {
      let currentCut = minimumCutPhase(graph)
      if bestCut == nil || currentCut.weight < bestCut.weight {
        bestCut = currentCut
        bestPartition = currentPartition
        bestPartition.insert(bestCut.vertexT)
      }
      currentPartition.insert(currentCut.vertexT)
      graph = graph.graphByMergingVertex(currentCut.vertexT, into: currentCut.vertexS)
    }

    // `bestPartition` now contains our first partition.
    let r = constructMinCutResult(g, partition: bestPartition, bestCut: bestCut)
    return r
  }


  static func constructMinCutResult(
    _ originalGraph: Graph,
    partition: Set<String>,
    bestCut: CutPhaseResult
  ) -> MinCutResult {

    let first = Graph()
    let second = Graph()

    var cuttingEdges: [Edge] = []
    var cutWeight = 0

    for v in originalGraph.vertices() {
      if partition.contains(v) {
        // first.addVertex(v)
      } else {
        // second.addVertex(v)
      }
    }

    for e in originalGraph.edges() {
      if first.containsVertex(e.source) && first.containsVertex(e.destination) {
        // first.addEdge(e.source, e.destination, e.weight)
      } else if second.containsVertex(e.source) && second.containsVertex(e.destination) {
        // second.addEdge(e.source, e.destination, e.weight)
      } else {
        cuttingEdges.append(Edge(source: e.source, destination: e.destination, weight: e.weight))
        cutWeight += e.weight
      }
    }

    return MinCutResult(
      first: first,
      second: second,
      edgesOnTheCut: cuttingEdges,
      cutWeight: cutWeight)
  }

}


// MARK: - Parsing

fileprivate extension Y2023Day25 {
  private static let nodeRef = Reference(String.self)
  private static let remainingRef = Reference(String.self)

  private static let lineRegex = Regex {
    TryCapture(as: nodeRef) {
      OneOrMore(.any)
    } transform: { w in
      String(w)
    }
    ": "
    TryCapture(as: remainingRef) {
      OneOrMore(.any)
    } transform: { w in
      String(w)
    }
  }

  func parseChildren(_ line: String) -> [String] {
    let line = line.trimmingCharacters(in: .whitespacesAndNewlines)
    guard !line.isEmpty else {
      return []
    }

    let tokens = line
      .split(separator: " ")
      .map { String($0) }
    return tokens
  }

  func parseLine(_ line: String) -> [String] {
    guard let match = line.firstMatch(of: Self.lineRegex) else {
      return []
    }
    return [match[Self.nodeRef]] + parseChildren(match[Self.remainingRef])
  }


  func parseGraph(_ lines: [String]) -> Graph {
    var graph: [String: [Edge]] = [:]
    for line in lines {
      guard !line.isEmpty else {
        continue
      }
      let nodes = parseLine(line)
      // First node is the src.
      assert(nodes.count > 0)
      let srcName = nodes[0]
      let destinationNames = nodes.suffix(from: 1)

      for destinationName in destinationNames {
        let e1 = Edge(source: srcName, destination: destinationName, weight: 1)
        let e2 = Edge(source: destinationName, destination: srcName, weight: 1)
        
        var srcNeighbors = graph[srcName, default: []]
        srcNeighbors.append(e1)
        graph.updateValue(srcNeighbors, forKey: srcName)

        var destinationNeighbors = graph[destinationName, default: []]
        destinationNeighbors.append(e2)
        graph.updateValue(destinationNeighbors, forKey: destinationName)
      }
    }

    return Graph(adjacencyEdges: graph)
  }
}
