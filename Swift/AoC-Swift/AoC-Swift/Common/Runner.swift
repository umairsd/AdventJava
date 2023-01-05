// Created on 11/23/22.

import Foundation

struct Runner {

  /// Programmatically loads all files for a given year (based on naming convention of YnnnnDaymm)
  /// and solves both parts.
  ///
  /// - Parameter year: The year for which to load and run the test cases.
  func run(for year: Int) {
    print("== Running Tests for the year \(year) ==")

    let prefix = getClassNamePrefix()
    let yearString = "Y\(year)"

    for day in 1...25 {
      let dayString = String(format: "%02d", day)
      // e.g. "AoC_Swift.Y2021Day02"
      let className = "\(prefix).\(yearString)Day\(dayString)"

      guard let dayClass = try? Bundle.main.loadConcreteDayClass(named: className) else {
        continue
      }

      print("\n-- Tests for day \(day)")
      let dayInstance = dayClass.init(dataFileNumber: 2)
      print("Part 1: \(dayInstance.solvePart1())")
      print("Part 2: \(dayInstance.solvePart2())")
    }
  }

  private func getClassNamePrefix() -> String {
    let name = String(reflecting: Day.self)
    let components = name.components(separatedBy: ".")
    guard components.count > 1 else {
      return ""
    }
    return components[0]
  }
}
