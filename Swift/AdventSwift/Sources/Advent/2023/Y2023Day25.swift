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
//    let graph = buildSampleGraph()
    let minCut = Graph.minimumCut(graph)
    let result = minCut.first.vertices.count * minCut.second.vertices.count
    return "\(result)"
  }


  func part2(_ lines: [String]) -> String {
    ""
  }

  private func buildSampleGraph() -> Graph {
    let edges: [(Edge, Int)] = [
      (Edge(source: "1", destination: "2"), 2),
      (Edge(source: "1", destination: "5"), 3),

      (Edge(source: "2", destination: "5"), 2),
      (Edge(source: "2", destination: "6"), 2),
      (Edge(source: "2", destination: "3"), 3),
      
      (Edge(source: "3", destination: "4"), 4),
      (Edge(source: "3", destination: "7"), 2),
      
      (Edge(source: "4", destination: "8"), 2),
      (Edge(source: "4", destination: "7"), 2),
      
      (Edge(source: "5", destination: "6"), 3),
      
      (Edge(source: "6", destination: "7"), 1),
      
      (Edge(source: "7", destination: "8"), 3),
    ]
    var graph = Graph()
    for (e, w) in edges {
      graph.addEdge(from: e.source, to: e.destination, weight: w)
      graph.addEdge(from: e.destination, to: e.source, weight: w)
    }
    return graph
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
    var graph = Graph()

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
        graph.addEdge(from: srcName, to: destinationName, weight: 1)
        graph.addEdge(from: destinationName, to: srcName, weight: 1)
      }
    }

    return graph
  }
}



// MARK: - Types

fileprivate class Vertex: Hashable {
  let id: String
  var metadata: [String]

  init(id: String, metadata: [String] = []) {
    self.id = id
    self.metadata = metadata
  }

  static func == (lhs: Vertex, rhs: Vertex) -> Bool {
    lhs.id == rhs.id
  }

  func hash(into hasher: inout Hasher) {
    hasher.combine(id)
  }
}

fileprivate struct Edge: Hashable {
  let source: String
  let destination: String
}


fileprivate struct Graph {

  private var adjacencyList: [String: [Edge]] = [:]
  private var allVertices = Set<String>()
  private var weights: [Edge: Int] = [:]
  private var vertexMetadata: [String: [String]] = [:]

  // MARK: Properties

  // Returns all the vertices in the graph.
  var vertices: Set<String> {
    allVertices
  }

  func weightFor(_ e: Edge) -> Int {
    return weights[e]!
  }

  /// Add or update an edge.
  mutating func addEdge(from source: String, to destination: String, weight: Int) {
    allVertices.insert(source)
    allVertices.insert(destination)

    if vertexMetadata[source] == nil {
      vertexMetadata[source] = [source]
    }
    if vertexMetadata[destination] == nil {
      vertexMetadata[destination] = [destination]
    }

    let e = Edge(source: source, destination: destination)
    var edges = adjacencyList[source, default: []]

    if let index = edges.firstIndex(of: e) {
      // Remove the old edge.
      edges.remove(at: index)
    }

    edges.append(e)
    adjacencyList[source] = edges
    weights[e] = weight
  }


  func containsVertex(_ v: String) -> Bool {
    allVertices.contains(v)
  }

  func containsEdge(_ e: Edge) -> Bool {
    let edges = adjacencyList[e.source, default: []]
    return edges.contains { $0.destination == e.destination }
  }


  func graphByMergingVertex(_ t: String, into s: String) -> Graph {
    let originalGraph = self
    var newGraph = Graph()
    var adjacencyList = originalGraph.adjacencyList
    
    var metadata = originalGraph.vertexMetadata
    metadata[s] = metadata[s, default: []] + metadata[t, default: []]
    newGraph.vertexMetadata = metadata


    // 1. Remove the edge from s-t, and t-s.
    for (source, edges) in adjacencyList {
      guard source == t || source == s else {
        continue
      }
      var mutatedEdges: [Edge] = []
      for edge in edges {
        if (edge.source == t && edge.destination == s) ||
            (edge.destination == t && edge.source == s) {
          continue
        }
        mutatedEdges.append(edge)
      }
      adjacencyList[source] = mutatedEdges
    }

    assert(adjacencyList.values.joined().count % 2 == 0)


    // 2. Remove all edges (t-x) and gather in `edgesFromT`.
    let edgesFromT = adjacencyList.removeValue(forKey: t) ?? []
    let neighborsOfT = Set<String>(edgesFromT.map({ $0.destination }))

    // 3. Remove all the edges (x-t).
    for v in neighborsOfT {
      let edges = adjacencyList[v, default: []]
      let mutatedEdges = Array(edges.filter { $0.destination != t })
      adjacencyList[v] = mutatedEdges
    }

    assert(adjacencyList.values.joined().count % 2 == 0)

    // 4. At this point, no edges (x-t) or (t-x) exist. Go through all the remaining
    // edges, and add them to the new graph.
    let allEdges = adjacencyList.values.joined()
    for e in allEdges {
      let w = originalGraph.weightFor(e)
      newGraph.addEdge(from: e.source, to: e.destination, weight: w)
    }

    // 5. Finally, add all the edges in `edgesFromT` (and their inverses) such that
    // they all point to `s`.
    for edgeFromT in edgesFromT {
      assert(originalGraph.containsEdge(edgeFromT))
      let weightTtoX = originalGraph.weightFor(edgeFromT)

      let edgeXtoS = Edge(source: edgeFromT.destination, destination: s)
      let edgeStoX = Edge(source: s, destination: edgeFromT.destination)

      var weight = weightTtoX
      if originalGraph.containsEdge(edgeXtoS) {
        assert(originalGraph.containsEdge(edgeStoX))
        let w = originalGraph.weightFor(edgeXtoS)
        weight += w
      }

      newGraph.addEdge(from: s, to: edgeFromT.destination, weight: weight)
      newGraph.addEdge(from: edgeFromT.destination, to: s, weight: weight)
    }

    return newGraph
  }
}


// MARK: - Stoer-Wagner Minimum Cut Algorithm

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
  /// MinimumCutPhase(G, W, a):
  ///   A <- {a}
  ///   while A != V:
  ///     add to A the most tightly connected vertex
  ///
  ///   store the cut in which the last remaining vertex is by itself (i.e. CutOfThePhase)
  ///   shrink G by merging the two vertices (s, t) added last
  ///       (the value of "cut-of-the-phase" is the value of minimum s, t cut.)
  ///
  ///   return s, t, and the cut weight as the "cut of the phase"
  /// ```
  ///
  /// ```
  /// MinimumCut(G, W, a):
  ///   while |V| > 1
  ///      MinimumCutPhase(G, W, a)
  ///      if the cut-of-the-phase is lighter than the current minimum cut
  ///         then store the cut-of-the-phase as the current minimum cut
  /// ```
  ///
  static func minimumCutPhase(_ graph: Graph) -> CutPhaseResult {
    guard let start = graph.vertices.first else {
      fatalError()
    }

    var foundSet = [start] // Set A
    var cutWeight: [Int] = []
    var candidates: Set<String> = Set(graph.vertices)
    candidates.remove(start)

    // while A != V
    //     Add to `foundSet` the most tightly connected vertex.
    while !candidates.isEmpty {
      var mostTightlyConnectedVertex: String!
      var maxWeight = Int.min

      // Find the most tightly connected vertex.
      // *Tightly connected vertex* to `foundSet` implies a vertex whose edge weight
      // to any of the vertex in `foundSet` is maximum.
      for vertex in candidates {
        // The total weight of the vertex to all vertices in the `foundSet`.
        var totalVertexWeight = 0
        for s in foundSet {
          let e = Edge(source: vertex, destination: s)
          guard graph.containsEdge(e) else {
            continue
          }
          totalVertexWeight += graph.weightFor(e)
        }

        if totalVertexWeight > maxWeight {
          mostTightlyConnectedVertex = vertex
          maxWeight = totalVertexWeight
        }
      }

      candidates.remove(mostTightlyConnectedVertex)
      foundSet.append(mostTightlyConnectedVertex)
      cutWeight.append(maxWeight)
    }

    assert(foundSet.count >= 2)
    let n = foundSet.count
    // Take the last two vertices and their weight as a cut of the phase.
    let result = CutPhaseResult(
      vertexS: foundSet[n - 2],
      vertexT: foundSet[n - 1],
      weight: cutWeight.last!)
    return result
  }


  static func minimumCut(_ originalGraph: Graph) -> MinCutResult {
    var graph = originalGraph

    var bestCutWeight = Int.max
    var partitionT: [String] = [] 

    while graph.allVertices.count > 1 {
      let currentCut = minimumCutPhase(graph)
      graph = graph.graphByMergingVertex(currentCut.vertexT, into: currentCut.vertexS)

      if currentCut.weight < bestCutWeight {
        bestCutWeight = currentCut.weight
        partitionT = graph.vertexMetadata[currentCut.vertexT, default: []]
      }
    }

    let r = constructMinCutResult(originalGraph, partition: Set(partitionT))
    return r
  }


  // we do a two-pass algorithm to reconstruct the sub-graphs:
  // - first we distribute the vertices into their respective graph
  // - second we align edges: if they cross graphs they will end in the list.
  static func constructMinCutResult(
    _ originalGraph: Graph,
    partition: Set<String>
  ) -> MinCutResult {

    var first = Graph()
    var firstVertices = Set<String>()
    var second = Graph()
    var secondVertices = Set<String>()

    var cuttingEdges: [Edge] = []
    var cutWeight = 0

    for v in originalGraph.allVertices {
      if partition.contains(v) {
        firstVertices.insert(v)
      } else {
        secondVertices.insert(v)
      }
    }

    for (_, edges) in originalGraph.adjacencyList {
      for edge in edges {
        let w = originalGraph.weightFor(edge)

        if firstVertices.contains(edge.source) && firstVertices.contains(edge.destination) {
          first.addEdge(from: edge.source, to: edge.destination, weight: w)
          first.addEdge(from: edge.destination, to: edge.source, weight: w)

        } else if secondVertices.contains(edge.source) && secondVertices.contains(edge.destination) {
          second.addEdge(from: edge.source, to: edge.destination, weight: w)
          second.addEdge(from: edge.destination, to: edge.source, weight: w)

        } else {
          cuttingEdges.append(edge)
          cutWeight += w
        }
      }
    }

    // Note: `cuttingEdges` counts each edge twice, as this is a bidirectional graph.
    return MinCutResult(
      first: first,
      second: second,
      edgesOnTheCut: cuttingEdges,
      cutWeight: cutWeight / 2)
  }

}
