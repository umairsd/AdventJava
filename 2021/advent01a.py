# Advent of Code 2021
# Day 1
def advent01a(fileName):
  print("**Advent of Code - 01a")
  count = 0
  with open(fileName) as file:
    prev = None
    for line in file:
      current = int(line)
      if prev != None and current > prev:
        count = count + 1

      prev = current

  return count


count = advent01a('advent01a.txt')
print(count)
