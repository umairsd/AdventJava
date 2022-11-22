# Advent of Code 2021
# Day 1, Problem 1


def getCountIncreasing(fileName):
  count = 0
  with open(fileName) as file:
    prev = None
    for line in file:
      current = int(line)
      if prev != None and current > prev:
        count = count + 1

      prev = current

  return count


def advent01a():
  print("**Advent of Code - 01a")
  count = getCountIncreasing('advent01a.txt')
  print(count)

advent01a()