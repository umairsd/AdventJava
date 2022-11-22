# Advent of Code 2021
# Day 2. Problem 2

def parseFileAndCompute(fileName):

  aim = 0
  horizontal = 0
  depth = 0

  with open(fileName) as file:
    for line in file:
      tokens = line.split(" ")
      direction = tokens[0]
      value = int(tokens[1])

      if direction == 'forward':
        horizontal += value
        depth += aim * value
      elif direction == 'up':
        aim -= value
      elif direction == 'down':
        aim += value

  return horizontal * depth


def advent02a():
  print("**Advent of Code - 02b")
  result = parseFileAndCompute('advent02.txt')
  print(result)


advent02a()
