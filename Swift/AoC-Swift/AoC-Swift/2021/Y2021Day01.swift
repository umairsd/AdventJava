// Created on 11/23/22.

import Foundation

class Y2021Day01: Day {
  var dayNumber: Int = 1
  var year: Int = 2021

  required init() {}
  
  func part1(_ lines: [String]) -> String {
    guard !lines.isEmpty else {
      return Constants.emptyInput
    }
    
    let data: [Int] = lines.compactMap { Int($0) }
    var count = 0
    var previous = data[0]

    for current in data.dropFirst() {
      if current > previous {
        count += 1
      }
      previous = current
    }

    return "\(count)"
  }
  
  func part2(_ lines: [String]) -> String {
    guard !lines.isEmpty else {
      return Constants.emptyInput
    }

    let data: [Int] = lines.compactMap { Int($0) }

    var previous = data.prefix(3).reduce(0, +)
    var current = 0
    var count = 0

    for i in 1...(data.count - 3)  {
      current = data.dropFirst(i).prefix(3).reduce(0, +)
      if current > previous {
        count += 1
      }
      previous = current
    }

    return "\(count)"
  }
  
  func part1Filename() -> String {
    return "day01-data2"
  }
  
  func part2Filename() -> String {
    return "day01-data2"
  }

}
