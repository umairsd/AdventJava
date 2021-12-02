# Advent of Code 2021
# Day 1. Problem 2

def readDataFromFile(fileName):
  data = []
  with open(fileName) as file:
    for line in file:
      data.append(int(line))
  return data


def getCountIncreasing(data, windowSize):
  if len(data) < windowSize:
    return

  count = 0
  # Initialize the sum in the first window.
  previousSum = 0
  for idx in range(0, windowSize):
    previousSum += data[idx]

  # Loop over the data.
  for idx in range(windowSize, len(data)):
    val = data[idx]
    currentSum = previousSum - data[idx - 3] + val
    if currentSum > previousSum:
      count += 1

    previousSum = currentSum

  return count


def advent01b():
  print("**Advent of Code - 01b")
  data = readDataFromFile('advent01a.txt')
  print("Size of data = ", len(data))
  count = getCountIncreasing(data, 3)
  print(count)


advent01b()
