// Created on 1/2/24.

import Foundation


/// --- Day 20: Pulse Propagation ---
/// https://adventofcode.com/2023/day/20
/// 
class Y2023Day20: Day {
  var dayNumber: Int = 20
  var year: Int = 2023
  var dataFileNumber: Int

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }


  func part1(_ lines: [String]) -> String {
    return ""
  }


  func part2(_ lines: [String]) -> String {
    return ""
  }
}


// MARK: - Types


fileprivate enum FlipFlopState {
  case on, off

  func toggle() -> FlipFlopState {
    return switch self {
    case .on: .off
    case .off: .on
    }
  }
}


fileprivate enum Pulse {
  case high, low
}

/**

 ```
 // connection
 Edge(Module, Module)

 // module
 Node {
   incomingEdges: [Edge]
   outgoingEdges: [Edge]
   state: ModuleState

   func pulse(_ pulse: Pulse, from connection: Edge)
 }

 FlipFlopState: ModuleState {
   updateState(_ pulse, incomingEdge) {
   }
 }

 ConjunctionState: ModuleState {
   incomingEdgePulseStates: [Edge: Pulse] = [.low]

   updateState(_ pulse) {
     // 1. update memory of the given incoming edge.
     // 2. if all states are `.high`
     //      send out a .low pulse to all outgoing edges.
     //    else: send out a .high pulse to all outgoing edges.

   }
 }

 ```
 */
