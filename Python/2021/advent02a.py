# Advent of Code 2021
# Day 2. Problem 1

def parseFileAndCompute(fileName):
  horizontal = 0
  vertical = 0

  with open(fileName) as file:
    for line in file:
      tokens = line.split(" ")
      direction = tokens[0]
      distance = int(tokens[1])

      if direction == 'forward':
        horizontal += distance
      elif direction == 'up':
        vertical -= distance
      elif direction == 'down':
        vertical += distance


  return horizontal * vertical


def advent02a():
  print("**Advent of Code - 02a")
  result = parseFileAndCompute('advent02.txt')
  print(result)


advent02a()
