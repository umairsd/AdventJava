// Created on 11/23/22.

import Foundation

struct FileUtils {

  private static let inputDataBundleName = "InputData.bundle"
  private static let fileExtension = "txt"

  enum Error: Swift.Error {
    case bundleNotLoaded
    case fileNotFound(name: String)
    case cannotLoadFileContents
  }

  static func readLines(fromFileNamed filename: String) throws -> [String] {
    let currentDirectoryURL = URL(fileURLWithPath: FileManager.default.currentDirectoryPath)
    let bundleURL = URL(fileURLWithPath: inputDataBundleName, relativeTo: currentDirectoryURL)

    guard let bundle = Bundle(url: bundleURL) else {
      throw Error.bundleNotLoaded
    }

    guard let dataFileURL = bundle.url(forResource: filename, withExtension: fileExtension) else {
      throw Error.fileNotFound(name: filename)
    }

    guard let fileContents = try? String(contentsOf: dataFileURL) else {
      throw Error.cannotLoadFileContents
    }

    let lines = fileContents.components(separatedBy: .newlines)
    return lines
  }
}
