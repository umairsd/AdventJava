// Created on 1/2/24.

import Foundation


/// --- Day 20: Pulse Propagation ---
/// https://adventofcode.com/2023/day/20
/// 
class Y2023Day20: Day {
  var dayNumber: Int = 20
  var year: Int = 2023
  
  fileprivate static let broadcastModuleName = "broadcaster"


  required init() {}

  
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
    /**
     Post:
     https://github.com/mebeim/aoc/blob/master/2023/README.md#day-20---pulse-propagation
     Visualization:
     https://www.reddit.com/r/adventofcode/comments/18mypla/2023_day_20_input_data_plot/

     Based on the visualization, 3 items stand out:
     (1) There's only one `rx` module.
     (2) There's only one input to `rx` module, and it's a conjuction module. Let's call it `A`.
     (3) All the inputs to `A` are also conjuction modules. Let's call them `B1`, `B2`, ...

     Given the behavior of the modules,
     - `A` will send a low pulse to `rx` as soon as all the remembered pulses from its input 
       (`B_i`) are high.
     - Each `B_i` will send a high pulse to `A` every time it receives a low pulse.

     So, we need to find the period for each `B_i` receiving a low pulse. Then take the LCM.
     */

    var graph = parseGraph(lines)

    let rxSources = findSourcesToModuleNamed("rx", in: graph)
    assert(rxSources.count == 1)

    // In the input, this module is named, "vd".
    let moduleA = rxSources.first!
    // In the input, these are, "fv", "pr", "bt", "rd"
    let modulesB = findSourcesToModuleNamed(moduleA.name, in: graph)

    let periods: [Int] = modulesB.map { module in
      resetGraph(&graph)
      return buttonPressCountForLowPulseTo(module, in: graph)
    }

    let result = lcm(periods)
    return "\(result)"
  }


  // MARK: - Private (Part 1)

  
  private func pressButton(_ graph: [Module: [Module]]) -> (lowPulses: Int, highPulses: Int) {
    guard let broadcaster = graph.keys.first(where: { $0.name == Self.broadcastModuleName }) else {
      fatalError()
    }

    var queue: [QNode] = []
    var totalLow = 0
    var totalHigh = 0
    queue.append(QNode(receiver: broadcaster, sender: Module("button"), pulse: .low))

    while !queue.isEmpty {
      let current = queue.removeFirst()

      // Send the pulse to the current destination.
      switch current.pulse {
      case .high: totalHigh += 1
      case .low: totalLow += 1
      }

      let nextSteps = current.receiver.receivePulse(current.pulse, from: current.sender)
      for (nextReceiver, nextPulse) in nextSteps {
        let qNode = QNode(receiver: nextReceiver, sender: current.receiver, pulse: nextPulse)
        queue.append(qNode)
      }
    }

    return (totalLow, totalHigh)
  }


  // MARK: - Private (Part 2)

  private func resetGraph(_ graph: inout [Module: [Module]]) {
    for (sender, receivers) in graph {
      sender.resetState()
      for r in receivers {
        r.resetState()
      }
    }
  }

  private func findSourcesToModuleNamed(_ name: String, in graph: [Module: [Module]]) -> Set<Module> {
    var result = Set<Module>()
    for (source, receivers) in graph {
      if receivers.contains(where: { $0.name == name }) {
        result.insert(source)
      }
    }
    return result
  }

  private func buttonPressCountForLowPulseTo(
    _ module: Module,
    in graph: [Module: [Module]]
  ) -> Int {
    
    guard let broadcaster = graph.keys.first(where: { $0.name == Self.broadcastModuleName }) else {
      fatalError()
    }
    var count = 0

    outerLoop: while true {
      count += 1
      var queue: [QNode] = []
      queue.append(QNode(receiver: broadcaster, sender: Module("button"), pulse: .low))

      while !queue.isEmpty {
        let current = queue.removeFirst()

        if current.pulse == .low && current.receiver == module {
          break outerLoop
        }

        let nextSteps = current.receiver.receivePulse(current.pulse, from: current.sender)
        for (nextReceiver, nextPulse) in nextSteps {
          let qNode = QNode(receiver: nextReceiver, sender: current.receiver, pulse: nextPulse)
          queue.append(qNode)
        }
      }
    }

    return count
  }
}


// MARK: - Types


/// Represents the sending of one pulse.
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

  /// Receives the pulse, and returns the list of (module, pulse) that need to be handled. These
  /// represent the next states.
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

  func resetState() {}

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

  override func receivePulse(
    _ pulse: Pulse, from module: Module
  ) -> [(module: Module, pulse: Pulse)] {
    switch pulse {
    case .high:
      return []

    case .low:
      self.state = state.toggle()
      let nextPulse = nextPulse(for: self.state)
      return generatePulsesForChildren(nextPulse)
    }
  }

  override func resetState() {
    state = .off
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

  override func receivePulse(
    _ pulse: Pulse, from module: Module
  ) -> [(module: Module, pulse: Pulse)] {
    pulsesReceived[module] = pulse

    let areAllHighPulses = pulsesReceived.values.allSatisfy { $0 == .high }
    let nextPulse = areAllHighPulses ? Pulse.low : Pulse.high
    return generatePulsesForChildren(nextPulse)
  }

  override func resetState() {
    for (k, _) in pulsesReceived {
      pulsesReceived[k] = .low
    }
  }
}


fileprivate class Broadcast: Module {

  override func receivePulse(
    _ pulse: Pulse, from module: Module
  ) -> [(module: Module, pulse: Pulse)] {
    return generatePulsesForChildren(pulse)
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
