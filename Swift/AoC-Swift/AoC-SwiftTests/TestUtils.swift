// Created on 1/4/23.

import Foundation
import XCTest

private let folderName = "InputDataForTests"
private let fileExtension = ".txt"

struct TestUtils {

  public func loadTestData(
    from filename: String,
    file: StaticString = #file,
    line: UInt = #line
  ) -> [String] {

    let data = try? TestUtils().loadData(for: filename)
    XCTAssertNotNil(data, "Data not loaded.", file: file, line: line)

    let fileContents = String(decoding: data!, as: UTF8.self)
    let lines = fileContents.components(separatedBy: .newlines)
    return lines
  }

  func loadData(for filename: String, fileExtension: String = fileExtension) throws -> Data {
    try Data(contentsOf: fileURL(for: filename, fileExtension: fileExtension))
  }

  func fileURL(for filename: String, fileExtension: String) -> URL {
    let directoryURL = fileDirectory()
    let fileURL = directoryURL.appending(path: "\(filename)\(fileExtension)")
    return fileURL
  }

  func fileDirectory(for filename: String = #file) -> URL {
    let url = URL(fileURLWithPath: filename)
    let testsDir = url.deletingLastPathComponent()
    let res = testsDir.appendingPathComponent(folderName)
    return res
  }
}
