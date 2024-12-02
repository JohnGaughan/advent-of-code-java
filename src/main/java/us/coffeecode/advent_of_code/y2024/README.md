# Year 2024: Finding the Chief Historian

## Day 1: Historian Hysteria

[Year 2024, day 1][1.0]

As usual, the first day is somewhat easy. We are given two lists that we need to compare.

For part one, we are told to sort the lists then find the distance between pairs of numbers that occupy the same location in each
list. Meaning the distance between the first elements in each, the second, and so on. This is trivially easy by subtracting one
from the other and finding the absolute value. Once this is done, sum up all of the distances to find the answer.

The input parsing logic performs the sort. For part one, I used a stream operation to iterate all of the indices and map that to
the distance for each pair. Once that is complete, collapse the stream by getting its sum.

Part two asks us to compare the lists in a different way. For each element in the first list, find how many times it appears in
the second list. Then multiply the number in the first list by the number of times it appears in the second list. Add up all of
these values to get the answer.

Since it does not matter if the list is sorted this time around, I used the same input parsing logic. While a naive implementation
that loops the second list each time works on inputs of this size and I used this to get the answer quickly, I later opted to use
a solution that scales better with large inputs.

After parsing the input, process the second list into an occurrence map. The key is the list element, and the value is the number
of times it appears. This can be done in `O(n)` time instead of `O(n^2)`. Then it becomes trivial to stream the first list and map
it to a new value that is its score, following the rules in the problem statement.

For each value in the first list, use it as a key to get the value in the occurrence map, or zero if not present. Then map the
stream by converting each element to the element's value times the value retrieved from the occurrence map. Once that is done sum
the stream to get the answer.

## Day 2: Red-Nosed Reports

[Year 2024, day 2][2.0]

Today's puzzle asks us to examine a series of integer arrays and test to see if they meet a condition. They must be either
strictly increasing or decreasing: the values cannot have duplicates and cannot change direction. Furthermore, they must not
increase or decrease by more than three.

Part one has us count the number of inputs that meet this condition. Part two adds the requirement that if an array does not pass
the initial test, it can also pass if removing any single element causes it to pass.

This is a simple brute-force algorithm that tests each array in turn. For part one, simply perform the test. For part two, if an
input array fails, iterate that array. Make a copy minus each element in turn, and see if that copy passes. If so, count the input
as passing and continue to the next array.

There are probably more efficient ways to accomplish this task, but the code is simple and still runs extremely quickly.


[1.0]: https://adventofcode.com/2024/day/1
[2.0]: https://adventofcode.com/2024/day/2
