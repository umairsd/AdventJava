// Created on 1/2/24.

import Foundation


/// --- Day 20: Pulse Propagation ---
/// https://adventofcode.com/2023/day/20
/// 
class Y2023Day20: Day {
  var dayNumber: Int = 20
  var year: Int = 2023
  var dataFileNumber: Int

  fileprivate static let broadcastModuleName = "broadcaster"

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }


  func part1(_ lines: [String]) -> String {
    let graph = parseGraph(lines)

    var totalLow = 0
    var totalHigh = 0

    (1...1000).forEach { _ in
      let (low, high) = pressButton(graph)
      totalLow += low
      totalHigh += high
    }

    let result = totalLow * totalHigh
    return "\(result)"
  }


  func part2(_ lines: [String]) -> String {
    return ""
  }


  private func pressButton(_ graph: [Module: [Module]]) -> (lowPulses: Int, highPulses: Int) {

    guard let broadcaster = graph.keys.first(where: { $0.name == Self.broadcastModuleName }) else {
      fatalError()
    }
    let button = Module("button")
    button.addChild(broadcaster)

    var queue: [QNode] = []
    queue.append(QNode(receiver: broadcaster, sender: button, pulse: .low))

    var totalLow = 0
    var totalHigh = 0

    while !queue.isEmpty {
      let current = queue.removeFirst()

      // Send the pulse to the current destination.
      switch current.pulse {
      case .high: totalHigh += 1
      case .low: totalLow += 1
      }

      let nextSteps: [(module: Module, pulse: Pulse)] = current.receiver.receivePulse(current.pulse, from: current.sender)

      for (nextReceiver, nextPulse) in nextSteps {
        let qNode = QNode(receiver: nextReceiver, sender: current.receiver, pulse: nextPulse)
        queue.append(qNode)
      }
    }

    return (totalLow, totalHigh)
  }
}


// MARK: - Parsing

fileprivate extension Y2023Day20 {

  func parseGraph(_ lines: [String]) -> [Module: [Module]] {
    var nameToModuleMap: [String: Module] = [:]
    var moduleNeighborNames: [Module: [String]] = [:]
    var graph: [Module: [Module]] = [:]

    // Go through each line, and first, create a (name, Module) mapping for each LHS entity.
    for line in lines.filter({ !$0.isEmpty }) {
      let tokens = line.split(separator: "->")
      assert(tokens.count == 2)
      let m = parseModule(String(tokens[0]))!
      nameToModuleMap[m.name] = m
      moduleNeighborNames[m] = tokens[1]
        .split(separator: ",")
        .map { $0.trimmingCharacters(in: .whitespaces) }
    }

    // Go through each line again, to build the graph.
    for (module, neighborNames) in moduleNeighborNames {
      var neighbors: [Module] = []

      for name in neighborNames {
//        if nameToModuleMap[name] == nil {
//          nameToModuleMap[name] =
//        }
//        guard let neighbor = nameToModuleMap[name, default: Module(name)] else {
//          // No module has been constructed for this neighbor, so we can ignore it.
////          continue
//          fatalError()
//        }

        let neighbor = nameToModuleMap[name, default: Module(name)]
        neighbor.addParent(module)
        module.addChild(neighbor)
        neighbors.append(neighbor)

        // In case this neighbor was just created, add it back to the map.
        nameToModuleMap[name] = neighbor
      }

      graph[module] = neighbors
    }

    return graph
  }


  private func parseModule(_ token: String) -> Module? {
    let t = token.trimmingCharacters(in: .whitespaces)
    assert(!t.isEmpty)
    if t == Self.broadcastModuleName {
      return Broadcast(t)
    }

    var i = t.startIndex
    let first = t[i]
    i = t.index(after: i)
    let remaining = String(t[i...])

    switch first {
    case "%":
      return FlipFlop(remaining)
    case "&":
      return Conjunction(remaining)
    default:
      fatalError()
    }
  }
}


// MARK: - Types

fileprivate struct QNode {
  /// The receiver of the pulse (i.e. the destination)
  let receiver: Module
  /// The source of the pulse.
  let sender: Module
  /// The pulse that the `source will send to `destination`.
  let pulse: Pulse
}


fileprivate enum Pulse {
  case high, low
}


/// A module. This is a node in the graph.
fileprivate class Module: Hashable, Equatable {
  let name: String
  private(set) var parentModules: [Module] = []
  private(set) var childModules: [Module] = []

  init(_ name: String) {
    self.name = name
  }

  /// Receives the pulse, and returns the list of (module, pulse) that need to be handled.
  func receivePulse(_ pulse: Pulse, from module: Module) -> [(module: Module, pulse: Pulse)] {
    // The default module has no rules about how to handle the pulses, so simply return an
    // empty list.
    []
  }

  func addParent(_ module: Module) {
    parentModules.append(module)
  }

  func addChild(_ module: Module) {
    childModules.append(module)
  }

  func generatePulsesForChildren(_ pulse: Pulse) -> [(module: Module, pulse: Pulse)] {
    let result: [(module: Module, pulse: Pulse)] = childModules.map { ($0, pulse) }
    return result
  }

  // MARK: Protocol (Hashable, Equatable)

  func hash(into hasher: inout Hasher) {
    hasher.combine(name)
  }

  static func == (lhs: Module, rhs: Module) -> Bool {
    lhs.name == rhs.name
  }
}


fileprivate class FlipFlop: Module {
  enum State {
    case on, off

    func toggle() -> State {
      return switch self {
      case .on: .off
      case .off: .on
      }
    }
  }

  /// Flip flop properties.
  private var state: FlipFlop.State = .off

  /// Receives the pulse, and returns the list of (module, pulse) that need to be handled.
  override func receivePulse(_ pulse: Pulse, from module: Module) -> [(module: Module, pulse: Pulse)] {
    switch pulse {
    case .high:
      return []

    case .low:
      self.state = state.toggle()
      let nextPulse = nextPulse(for: self.state)
      return generatePulsesForChildren(nextPulse)
    }
  }

  private func nextPulse(for state: FlipFlop.State) -> Pulse {
    return switch state {
    case .on: Pulse.high
    case .off: Pulse.low
    }
  }
}


fileprivate class Conjunction: Module {
  // The most recent pulse received for each module. Initital value for each module is .low
  private var pulsesReceived: [Module: Pulse] = [:]

  override init(_ name: String) {
    super.init(name)
  }

  override func addParent(_ p: Module) {
    super.addParent(p)
    pulsesReceived[p] = .low
  }


  /// Receives the pulse, and returns the list of (module, pulse) that need to be handled.
  override func receivePulse(_ pulse: Pulse, from module: Module) -> [(module: Module, pulse: Pulse)] {
    pulsesReceived[module] = pulse

    let areAllHighPulses = pulsesReceived.values.allSatisfy { $0 == .high }
    let nextPulse = areAllHighPulses ? Pulse.low : Pulse.high
    return generatePulsesForChildren(nextPulse)
  }
}

fileprivate class Broadcast: Module {

  override func receivePulse(_ pulse: Pulse, from module: Module) -> [(module: Module, pulse: Pulse)] {
    return generatePulsesForChildren(pulse)
  }
}

