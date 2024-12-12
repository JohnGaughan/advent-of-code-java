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

## Day 5: Print Queue

[Year 2024, day 5][5.0]

Today's puzzle involves custom sort ordering of integers other than their natural ordering.

We start by parsing the input into something useful. The file contains two groups of lines. The first contains the sort ordering.
Given a line with two pipe-separated integers, the first integer is less than the second regardless of what you learned in first
grade. To accomplish this, we build a mapping. The key is the integer, a page number. The value is a set of all integers that sort
lower than it. We first ensure both integers map to an empty set if not already mapped, which is a crucial step because one page
will not have any mapping in the input file because it sorts lowest. Next, we get the mapping for the _larger_ of the two
elements, the one on the right, and add the integer on the left of the delimiter to its set. Do this for all rules, and we are
done. This logic assumes that the input defines a full mapping and does not leave any edge cases that are implied, such as
transitive sorting where `a < b < c` but `a < c` is not defined. This assumption holds true for the test data and my real input
data.

Next, we perform some simple list stream processing to split the lines of manuals into a double-list of integers. The inner list
represents a single manual, while the outer list simply holds all of the manuals.

There are three methods used in both parts of the question:

* `isCorrect()` checks whether a manual is in correct order. It does this by sorting a copy of the manual and checking it against
  the input. Since manuals are short and the sorting algorithm is efficient, this is not a problem.

* `sorted()` returns a copy of the input manual, run through a sorting algorithm. This uses a custom comparator that checks the
  left side against the set of page numbers that are lower than it. If the right side is found in the list, then return `1`
  indicating the left is larger. Otherwise return `-1` indicating the right is larger. Even if the input data contained manuals
  with duplicate pages, which it does not, this would still work although it would be an [unstable sort][5.1] in that case.

* `score()` calculates the score for a manual, which is the value of its center page.

Part one filters all manuals that are in correct order, removing incorrect manuals. It adds up their scores.

Part two filters all manuals that are _not_ in correct order, removing correct manuals. It sorts each manual, then adds up their
scores.

For a simple problem such as this, it was a bit of a wild ride. Programming after midnight does not help, but it took me a while
to work through an issue where the sample data worked fine but my real input sorted incorrectly. Turns out I had some faulty
assumptions about the input rules, and we really do only need to store the mapping of page number to lower page numbers.

The method `isCorrect()` does extra work to sort each manual. In theory this could be wildly inefficient, and there is a better
way to do this if that were the case which is an `O(n)` algorithm instead of `O(n log n)`. This would iterate the list once,
maintaining a set of seen page numbers. At each step, check that the set of seen numbers is wholly contained in the current page's
mapping of lower page numbers. However, the sorting technique is trivially concise and easy to read on a single line, which trumps
the technically correct algorithm given the low input sizes and run times that are lower than the time it takes the JVM to
initialize and run the unit test. If for some reason you want to run this on much larger inputs, then this method should be
rewritten.

## Day 6: Guard Gallivant

[Year 2024, day 6][6.0]

Today we get our first problem of navigating a grid. There is an actor that follows set rules, moving around a room until it
leaves the area. As is typical, part one has us write a foundation for solving part two.

The algorithm I wrote tracks the actor's location and facing. It keeps following the two basic movement rules laid out in the
problem statement until one of two conditions are true. Either the actor leaves the area, or enters an infinite loop because there
is a duplicate combination of location and facing. Given the simple rules, this guarantees the actor will repeat its movement
forever. This simulation also returns why it stopped, which is one of those two exit conditions.

For part one we simply get the results of the simulation, discard the facing data, and count the unique locations.

Part two also runs the initial simulation. Then get the candidates for the new obstruction by getting the unique locations on that
path that are _not_ the initial actor location.

For each location in this set, add an obstruction. Then re-run the simulation and see if the reason it stopped is an infinite
loop. Count those cases and return that sum as the answer.

This takes a while to run, but is well under my self-imposed one second time limit. I pre-allocated a large set of "seen" moves
during the simulation because constant resizing of the underlying hash map nearly doubled the runtime. I still think there is a
bit of overhead hidden in the JRE, but I am not sure if I can optimize that further.

## Day 7: Bridge Repair

[Year 2024, day 7][7.0]

We get our first truly recursive problem today. Given a list of long integers we need to figure out if there is any way to
combine all of them after the colon into the first value before the colon. We are given three binary operators that can combine
values: add, multiply, and concatenate, with the latter only being allowed in part two.

Parsing the input is easy: split the line, set the test value (desired result) to the first value, and the operands are all of the
other numbers. Long integers are required here, as they will not fit in 32 bit integers.

Both solution parts use the same logic. Call into the recursive function with the equation and the allowed operators for that
solution part.

Recursive logic is fairly simple here. Maintain an accumulator, which is the result of combining everything up to whatever the
current point is. At each step, iterate over the allowed operators and use each one to combine the accumulator with the next
operand. If the operands are exhausted, check the accumulator against the equation's test value. If it matches, return true: this
is the tail call and ends recursion. The recursive step short-circuits. If recursion further down the operands returns true, then
stop: there is no need to check other operands. This makes the algorithm a depth-first search that quits as soon as it finds a
single solution.

Getting the answer is a simple series of stream operators. Get the file lines, convert each one to an equation, filter the stream
so it only contains equations that pass the test, convert to a long stream where the value of each element is the equation's test
value, then sum the values to get the answer.

## Day 8: Resonant Collinearity

[Year 2024, day 8][8.0]

Two days later, we get another problem about calculating grid positions. For this one, we need to find points that are in a line,
and count the number of unique points.

To start with, we parse input into a map of each frequency (character code point) to the coordinates of points that use that
frequency. We also hang on to the input grid for bounds checking.

Both algorithms start the same way: get the input and iterate over each antenna frequency. Get the antinodes for that frequency,
and collect them into a set which ensures uniqueness. Once done, simply return the size of the set.

To get the antinodes for each frequency, we first create a set of points into which will be stored antinodes for each pair of
antennas. This ensures uniqueness at this step as well which is not strictly required, but helps keep storage from ballooning.
Process each pair of antennas: this is where the algorithm varies between parts one and two.

In both cases, get the difference in coordinates between them: delta X and delta Y. For part one we add these deltas to the first
point, and subtract them from the second, providing two antinodes. Filter them to ensure they are within bounds, and return what
remains.

For part two we need an iterative approach. Start with the first point. If it is within bounds (it will be), add it to the
antinodes. Add the deltas, then loop back to the bounds check. Eventually, it _will_ be out of bounds and the loop breaks. Repeat
these exact steps for the second point, except subtract the deltas instead of adding them.

## Day 9: Disk Fragmenter

[Year 2024, day 9][9.0]

This was a good one. I could picture the solution in my head, but kept going back and forth on how to express it in code and
actually tried several solutions. Some worked, some did not. Some took a long time, the final solution is fast. Today, parts one
and two are completely separate implementations although there are similarities. It all starts with input parsing: what should the
data look like after reading it and before processing it?

For part one, we move file system blocks from the end to the front. This means the algorithm is focused on specific locations in
the file system, not the files themselves. I ended up creating two data structures:

* A tree map containing all of the file blocks. The key is the location, and the value is the file ID. This models a sparse list
  where many entries are missing.
* A linked list containing the locations of all of the gaps. Since gaps will always be removed at the head of the list, this is
  rare case where a linked list outperforms an array list.

Part one works by continuously checking the location of the final file block to see if it matches the size of the file system.
When the location is larger than the file system size, there must be a gap. Get the first gap in the list and reassign that last
file block to that location. Remove the gap.

Once complete, walk the tree map containing the file blocks using a stream operation. Multiply each key by its corresponding value
then get the sum of the stream to find the answer.

Part two works differently because files need to remain contiguous. However, the requirements simplify the algorithm a bit which
affects input parsing slightly. We start with the last file and work our way toward the front, so we do not actually need to track
all the files in the same structure, rearranging and sorting. Get each file and each gap as a range which holds its start and end
blocks. Then sort the files in reverse order so iteration is easier. Gaps are stored in a tree set which has sorting built-in.

The main part of the algorithm first creates a collection of defragmented files which will eventually receive every file in the
file system. It then walks the files one by one, but in reverse due to the sorting. Then look through the gaps until one of two
conditions is true. First, we run out of gaps that are ahead of the file and none of them were big enough. In this case, simply
add the file to the defragmented collection. Second, we find a gap big enough. In this case, "move" the file by creating a new one
with the same ID but whose range starts at the gap's start. Next, remove the gap. Then, if there is any remaining space left in
the gap, add a new gap representing that leftover space.

Finally, we calculate the checksum similar to in part one but using different objects. Iterate through the files and multiply its
ID by the sum of all of its file locations. Then get the sum of the stream to find the answer.

## Day 10: Hoof It

[Year 2024, day 10][10.0]

Today was a quick and fun little maze navigation puzzle where I accidentally solved part two first because I did not read the
instructions correctly.

In both parts, we need to search through the puzzle input for the number zero. Each time, we need to find paths from that zero to
the number nine, incrementing by one. This is a simple recursive function with nearly identical structure in both cases.

For part one, we get the unique set of "nine" locations that can be reached from the given zero. Then, for each zero, we count
those high points as its score and add up all of the scores. For part two, we need to count the number of unique paths even if two
paths end at the same high point. The first method returns a set from which we get its size, the second we instead return an
integer for each path. The difference is in the second case, visiting the same end point again will increment the counter while in
the first case we return a duplicate location which gets discarded by the hash set.

## Day 11: Plutonian Pebbles

[Year 2024, day 11][11.0]

Today we get our first [memoization][11.1] problem. The input is a short series of numbers that transform according to one of
three rules over many iterations. One of these rules splits the number in two, while the others transform it in ways that make it
difficult to predict how it will transform in future steps. As far as I can tell, there is no way to calculate the answer directly
and iterating through the transformations is required.

The issue is with the time required. Part one uses twenty-five iterations, which does not take long with a simple brute-force
algorithm. Part two uses seventy-five iterations, which is prohibitively time-expensive because there are hundreds of trillions of
iterations.

Memoization in this case caches program state by using the current value and number of iterations as a key into a map, where the
value is the final count of numbers the current value will eventually split into. There must be a _lot_ of duplicated state,
because the run time went from "gave up" to a few tens of milliseconds.

The reason for including the number of iterations is simple. Let us assume the current value is `10`. If this is the final
iteration, then there is a single number: `10`. It cannot transform, because we reached an end state. If this was instead the
first iteration, then there must be at least two numbers because per the second rule, 10 will split into `1` and `0`.

Every year has at least one problem like this, and it is a great exercise in this methodology that is not needed very often but is
a huge benefit when it is.

One final note: this is a problem where you _need_ to use long integers because many of the numbers your program needs to handle
will not fit in 32 bits.

## Day 12: Garden Groups

[Year 2024, day 12][12.0]

Today's puzzle has us divide a map into regions, then perform calculations on those regions based on their area and shape.

The first task is to read in the input: this is done by iterating the grid and storing it in a map of code points (letters in the
grid) to sets of points that have that letter.

Next, we need to examine each set of points that share identifiers and split them into disjoint regions. If ten points are
identified by the letter `A` but five are grouped together and do not touch the other five, they should be two separate regions.

To do this, pick a point, any point. I use the first in iteration order for simplicity, but this is not important to the
algorithm. Put it in a queue, then loop while the queue has elements. Return and move the head of the queue, then check its four
cardinal neighbors. If any of them exist in the set of points that share the same identifier and is not already part of the
current region, then add it to the current region and to the queue. Loop back and repeat until there are no more points that can
be part of the current region. Repeat that until the input points are exhausted, and return the collection of regions.

The final step of the common algorithm is to convert each region into a score value, then add up the scores and return it as the
answer. This is where the two parts differ.

The score for a given region is its area, or number of points, multiplied by another value that differs between parts one and two.
Getting the size is a trivial exercise: call `Collection.size()` on the points of the region. Part one multiplies this by the
perimeter of the region, part two by the number of sides.

For part one, the perimeter is somewhat easy and straightforward to get. Iterate all of the points in the region. For each point,
count the number of neighbors not in the region. That count is the perimeter. Consider a point on the corner: it has two external
sides and two internal sides. In that little square, there are two units of perimeter. A point that is fully internal has zero
units of perimeter. Similar logic for one or three sides.

For part two, we need the number of sides of each region. This is a bit more complicated to calculate than the perimeter, but not
nearly the most difficult task this event it likely to contain.

Consider any of the examples. If you look at any point, it can have between zero and four borders around it. Some are interior
points with no borders, others are lone points with four borders. This means we need to track not only the number of borders as in
part one, but also the direction of each border.

Next, consider a simple square shape. Two points on the top of the square may each have a top border, but for our purposes here,
they are a single border of length two (or longer). Clearly, we need to identify borders, then merge them together to find the
entire side of a shape.

Finally, consider the `E` shape in the examples for part two. That shape has _three_ top (North) borders. Clearly, they are
separate sides and should not be merged. They do not touch each other, and we can express this by saying the points' X coordinates
may be the same, but their Y coordinates are different.

On to the actual algorithm. We iterate all the points in a region, and figure out which sides of each point have borders. When we
find a border, we create a mapping. The key is the direction of the border combined with the relevant coordinate. For North and
South borders, this will be the Y coordinate, and for West and East, the X coordinate. We then store that key in a map, pointing
to a collection of `Range` objects: for now, the range is the other coordinate for both the start and end.

To visualize the result of this step, imagine that `E` shape from earlier. We will have something like this.

North borders:

    -----
    
     ----
     
     ----

East borders:

         |
     |
         |
     |
         |

(plus south and west, not pictured)

The next step the code takes is a stream operation that merges and counts the borders in one line of code, but let's consider the
merge operation in depth.

Process each map key: in the above visual, this would be the top bar of the `E`, followed by the middle, and bottom. Then the same
for the two vertical lines of the East borders. For each collection of ranges, copy it into a list and sort the list. The natural
ordering of `Range` is such that it puts them into integer order, since the two ends of the range start the same.

Iterate through the sorted list once. At each step, look at the current and next range. If they are adjacent, remove those ranges
and add a new, merged range. If they are not adjacent, increment the loop counter. At the end, we will have a list where each
range is not adjacent to any other range. In the visuals above, each of the horizontal bars will be a single range. The vertical
bars will all be separate, since they cannot be merged.

Once this is done, simply count the number of ranges which correspond to sides of the region. Multiply by the size, or area, of
the region to get the score for that region.

## Day 13: ?

[Year 2024, day 13][13.0]

## Day 14: ?

[Year 2024, day 14][14.0]

## Day 15: ?

[Year 2024, day 15][15.0]

## Day 16: ?

[Year 2024, day 16][16.0]

## Day 17: ?

[Year 2024, day 17][17.0]

## Day 18: ?

[Year 2024, day 18][18.0]

## Day 19: ?

[Year 2024, day 19][19.0]

## Day 20: ?

[Year 2024, day 20][20.0]

## Day 21: ?

[Year 2024, day 21][21.0]

## Day 22: ?

[Year 2024, day 22][22.0]

## Day 23: ?

[Year 2024, day 23][23.0]

## Day 24: ?

[Year 2024, day 24][24.0]

## Day 25: ?

[Year 2024, day 25][25.0]



[1.0]: https://adventofcode.com/2024/day/1
[2.0]: https://adventofcode.com/2024/day/2
[3.0]: https://adventofcode.com/2024/day/3
[4.0]: https://adventofcode.com/2024/day/4
[5.0]: https://adventofcode.com/2024/day/5
[5.1]: https://en.wikipedia.org/wiki/Sorting_algorithm#Stability
[6.0]: https://adventofcode.com/2024/day/6
[7.0]: https://adventofcode.com/2024/day/7
[8.0]: https://adventofcode.com/2024/day/8
[9.0]: https://adventofcode.com/2024/day/9
[10.0]: https://adventofcode.com/2024/day/10
[11.0]: https://adventofcode.com/2024/day/11
[11.1]: https://en.wikipedia.org/wiki/Memoization
[12.0]: https://adventofcode.com/2024/day/12
[13.0]: https://adventofcode.com/2024/day/13
[14.0]: https://adventofcode.com/2024/day/14
[15.0]: https://adventofcode.com/2024/day/15
[16.0]: https://adventofcode.com/2024/day/16
[17.0]: https://adventofcode.com/2024/day/17
[18.0]: https://adventofcode.com/2024/day/18
[19.0]: https://adventofcode.com/2024/day/19
[20.0]: https://adventofcode.com/2024/day/20
[21.0]: https://adventofcode.com/2024/day/21
[22.0]: https://adventofcode.com/2024/day/22
[23.0]: https://adventofcode.com/2024/day/23
[24.0]: https://adventofcode.com/2024/day/24
[25.0]: https://adventofcode.com/2024/day/25
