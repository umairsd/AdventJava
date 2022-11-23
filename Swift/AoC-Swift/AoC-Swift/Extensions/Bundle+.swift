// Created on 11/23/22.

import Foundation

/// An extension that loads a specific instance of a class that conforms to the `Day` protocol.
extension Bundle {

  enum ClassLoadError: Error {
    case moduleNotFound
    case classNotFound
    case invalidClassType(loaded: String, expected: String)
  }

  func loadConcreteDayClass(named name: String? = nil) throws -> Day.Type {
    let name = name ?? String(reflecting: Day.self)

    guard name.components(separatedBy: ".").count > 1 else {
      throw ClassLoadError.moduleNotFound
    }
    guard let loadedClass = Bundle.main.classNamed(name) else {
      throw ClassLoadError.classNotFound
    }
    guard let castedClass = loadedClass as? Day.Type else {
      throw ClassLoadError.invalidClassType(loaded: name, expected: String(describing: Day.self))
    }

    return castedClass
  }
}
