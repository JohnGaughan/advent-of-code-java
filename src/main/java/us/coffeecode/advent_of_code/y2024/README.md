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

## Day 3: Mull It Over

[Year 2024, day 3][3.0]

This is a problem about pattern matching. We need to scan an input string and find text representing instructions for multiplying
two numbers. Part two adds additional strings that enable or disable those multiply instructions until altered further.

My solution is a simple Java regex exercise. Write a function that accepts a string region and extracts and multiplies the
numbers, and use it as-is for part one. For part two, use additional matching logic that finds the indices of the string where
instructions enable and disable multiplication. Using a navigable map, we can easily find the most recent map entry in terms of
the string index. Then we know whether or not to evaluate each multiply instruction.

When solving this problem, keep in mind that the example input provided is different between the two parts. This is not uncommon,
but typically does not happen until later in each event. The sample input looks quite similar between the two, but is subtly
different.


## Day 4: Ceres Search

[Year 2024, day 4][4.0]

We are asked to search a grid for variations of the string XMAS, and count the number of occurrences. Both parts use similar but
different algorithms.

First, static state includes eight `Point2D` objects that are one unit away from origin and an array containing them all. These
can then be leveraged to simplify search logic.

For part one, we need to find the count of all unique instances of the string XMAS which can occur in eight directions. Start by
checking every point in the grid. At each point, look in all eight directions and count how many matches there are.

There is a simple method to check for a match. Create a new point representing the current location, and set it to the point being
iterated above. Iterate over each character in the search string. Does the character in the grid match the current location? If
not, return false: this is not a match. If it does match, update the current point by adding the point corresponding to the
direction being checked. Since this directional point is one unit away, the algorithm will keep moving one point away for each
character being checked. If we run out of characters to check then they must have all matched, so we return true indicating this
is a match. There is also a bounds check at each step, so it never attempts to go outside the grid.

Once all combinations of points and directions are exhaustively searched, we have the answer.

Part two instead has us check that two instances of the string MAS form a visual X, where both words can run in either direction.

Again iterate the entire grid, except now we can omit the edge rows and columns since they will never match because it would
require checking out of bounds. This time we do not iterate directions, since those are fixed.

Observe that the two MAS strings must have the character `A` in the center: the first check we perform is that the current point
has an `A`. If not, fail fast and keep going. Construct points for north-west and south-east. Check both combinations where one is
`M` and the other is `S`. If not, fail fast and keep going. Otherwise, construct points for north-east and south-west. Perform the
same check as above and we have our answer for this grid point.

Overall, this is a simple problem and I found the only halfway difficult part to be expressing the algorithm clearly in code in a
way that avoided repetition and verbose coordinate math. While it obscures some of the algorithm behind library code, I think
using the home-grown `Point2D` class makes the intent of the code more clear while being easier to read.


[1.0]: https://adventofcode.com/2024/day/1
[2.0]: https://adventofcode.com/2024/day/2
[3.0]: https://adventofcode.com/2024/day/3
[4.0]: https://adventofcode.com/2024/day/4
