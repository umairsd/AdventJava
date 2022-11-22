# Advent of Code 2021
# Day 3. Problem a

def parseLine(line):
  lineArray = []
  for value in line:
    if value == '1':
      lineArray.append(1)
    elif value == '0':
      lineArray.append(-1)
  return lineArray

# Assumes: Lines are the same length.
def parseFileAndCompute(fileName):
  counts = []
  isFirstLine = True
  with open(fileName) as file:
    for line in file:
      if isFirstLine:
        counts = parseLine(line)
        isFirstLine = False
      else:
        lineArray = parseLine(line)
        for i in range(0, len(lineArray)):
          counts[i] += lineArray[i]

  return counts


def calculate(counts):
  gamma = ""
  epsilon = ""
  for num in counts:
    if num > 0:
      gamma += '1'
      epsilon += '0'
    elif num < 0:
      gamma += '0'
      epsilon += '1'

  result = int(gamma, base=2) * int(epsilon, base=2)
  return result


def advent03a():
  print("**Advent of Code - 03a")
  data = parseFileAndCompute('advent03.txt')
  result = calculate(data)
  print(result)


advent03a()
