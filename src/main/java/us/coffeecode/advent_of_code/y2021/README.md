# Year 2021: Ocean Exploration

## Day 1: Sonar Sweep

[Year 2021, day 1][1.0]

The first puzzle is a real softball. It simply requires comparing array elements and counting how many are larger than a preceding
element. Both parts can be done in a single iteration of the input array.

Part one requires us to check adjacent elements, counting when element `n` is greater than element `n - 1` then returning that
count.

Part two says to take three-element sums, then compare those sums. However, each adjacent sum has two elements in common, so those
can be ignored. If we have the inequality `A + B + C < B + C + D`, we can subtract the common terms `B` and `C` and be left with
`A < D`. This leaves us with what is essentially part one, except we compare `n` with `n - 3` and return the count of the
elements where the condition is true.

Even better, the solutions are identical other than the difference between element indices so the code can be refactored into a
single computational method.

## Day 2: Dive!

[Year 2021, day 2][2.0]

This puzzle is another easy one, as is expected early in the month. It is a variation on a theme we see every year in Advent of
Code: given a bunch of lines that encode instructions on how to move in a coordinate plane, move in that plane and calculate some
value, the answer, based on the destination coordinates.

Part one is straightforward and ensures the input parsing and basic decision logic works correctly. In my solution I maintain
coordinates and alter them based on the rules. The only potential issue here might trip up people new to AoC: the Y coordinate is
measured from the top down. In other words, origin would be at the top left of your monitor instead of the bottom left as is
common in mathematics.

For part two we need to track another variable for the aim of the submarine, and use that to alter the depth (Y coordinate) when
moving forward.

## Day 3: Binary Diagnostic

[Year 2021, day 3][3.0]

Today's puzzle introduces the first bit-based problem of the year. We need to count bits in numbers and filter them based on some
rules. Part one asks us to count the most and least common bits in a particular position among the input integers, then use those
bits to construct new numbers which we multiply together to find the answer. This is fairly straightforward.

Part two has us iterate through the bit positions and filter the numbers at each step based on the most common bit values of the
remaining numbers, which implies removing input numbers as we go. Filtering is similar to the first part, except there can now be
ties where 0 and 1 are equally as common. This was not the case in part one, where the complete input had no ties. However, after
removing input values, the remaining values can have ties.

Overall this is not a difficult problem but there are a few opportunities to make mistakes due to the increased level of detail in
the instructions compared to previous days. My approach uses integer arrays, which is less than ideal among the better ideas I
came up with. Java is not well-suited to this type of problem and truly good options are usually limited.

## Day 4: Giant Squid

[Year 2021, day 4][4.0]

We see our first array manipulation puzzle today in the form of a game of Bingo. Given a list of numbers to draw, we mark Bingo
cards. This goes on until there is a winner in part one, or until the final card wins in part two.

My code tracks this with a Card class containing a 2D array with the numbers on the card. Draw each number in turn, and mark all
occurrences of that number on each card.

Part one stops as soon as there is a winning card: we then get the score for that card. Part two continues until there is a single
card remaining and there is a winning card: we then get the score for that lone remaining card.

I made quite a bit of effort to edit the code down to be concise and expressive, but one optimization is elusive: checking that a
card won. It is a bit verbose, and using stream operators does compact the code quite a bit. However, it also makes the code
execute several times slower. Making this code more concise without harming performance - which is a major goal of mine for Advent
of Code solutions - is not feasible here. The only optimizations I can think of involve tricky bit hacks that are error-prone and
difficult to understand.

## Day 5: Hydrothermal Venture

[Year 2021, day 5][5.0]

Today's puzzle requires us to map line segments onto a 2D plane, then count how many points on that plane have two or more lines
that overlap at that point.

My approach was to model the plane as a hash map with 2D points as keys, and boolean flags as values. When a point is first
mapped, set its value to false which indicates no overlap. If a point is then mapped again later, we know it must overlap so set
the flag to true. At the end we count up the number of points that were mapped, but filter out all points with false values first.

Originally I used integers to count the overlaps, however, we do not care about the number of overlaps: only that a point is
overlapped at all.

This was overall a straightforward puzzle _except_ for the gotcha that line segments can be defined in any direction. Horizontal
lines can go left to right or right to left: vertical lines can go up to down or down to up. Diagonal lines can start in any of
the four corners of their bounding box and end in the opposite corner. On top of that, the bounds are inclusive, while programmers
typically treat the upper bound as exclusive. Handling this correctly was challenging when I was coding after midnight, rocking
'till the dawn. I eventually landed in the ten-minute penalty box due to too many incorrect answers.

I later refactored the solution a little bit. Instead of tracking bounding boxes, track the starting point of a line segment along
with its length and deltas for both X and Y coordinates. Then when marking that segment we know where to start, and apply those
deltas a number of times equal to the segment length. This ends up being a _much_ cleaner solution that is more concise and easier
to read and understand.

## Day 6: Lanternfish

[Year 2021, day 6][6.0]

This is an exercise in seeing the forest, given a bunch of trees. We need to model a population of fish that reproduces quickly.
Parts one and two differ only in the number of iterations of the simulation. However, part two has so many fish that it is simply
not feasible to model them directly.

The key insight to make here is that fish are essentially fungible, and we do not need to model a million identical fish
separately. Instead, we can lump all fish into buckets based on their current timer value. Two fish with the same internal timer
can be treated as one unit with quantity two.

After making this insight, it is plain to see that there are only nine pairs of values to track. Stuffing over a trillion fish
into an array is easy if we group identical fish together.

## Day 7: The Treachery of Whales

[Year 2021, day 7][7.0]

Today's puzzle asks us to find a value representing the center point of a given integer array representing positions of some
crabs. I came up with a brute-force algorithm that still completes very quickly. However, there is a key insight that greatly
reduces the search space which I will explain later.

The algorithm I came up with first sorts the input. Then we check positions from low to high. Iterate all of those values - even
those not in the array - because the answer might be between input values. For at least some of the inputs and puzzle parts, the
answer is not in the input array.

For each possible solution, generate a score which is the sum of some function of each crab's distance from the proposed location.
The algorithm allows the calling code to pass in a lambda to score the distance. For part one this is the identity function,
meaning the cost of moving is equal to the distance. For part two, we need to plug the distance into the formula that generates
[triangular numbers][7.1] instead.

The key here is the scoring functions cause the result to be either the median of the input (part one) or the mean of the input
(part two). My input has 1,000 elements requiring taking the mean of the center two to find the median. With integer division used
for both calculations, the calculated values may be the floor of a non-integral real number. For the algorithm to work with all
inputs, we need to check both the computed value and one greater. Many people have pointed out that the input (1, 10, 11, 12) will
produce an incorrect answer if we do not consider the ceiling of the real mean. My solution actually verifies whether the computed
value is exact or not: if it is implicitly the floor of a non-integer, then also consider the next higher integer.

## Day 8: Seven Segment Search

[Year 2021, day 8][8.0]

Seven-segment displays, like the LEDs on an alarm clock, are the theme of today's puzzle. Given letters representing the segments
that are all jumbled up, we need to decode numbers in the input text and find properties that prove they are decoded correctly.

As usual, part one is a hint for what is to come. Numbers one, four, seven, and eight are unique in terms of how many segments
they use. This means we know which ones they are with trivial effort. Simply count how many of them show up.

Part two requires us to find the segment mapping for each digit so we can reconstruct the correct output value. The key here is in
part one: it specifically says "focus on the easy digits." This statement obviously is intended to guide users toward an algorithm
where we deduce digits one at a time based on information we have, such as "this is the only remaining digit with X segments that
we do not already have mapped."

* We are given the segment representations of 1, 4, 7, and 8 by nature of them having unique numbers of segments.
* 9 is the only six-segment number that completely overlaps with 4.
* 0 is the only six-segment number other than 9 that completely overlaps with 1.
* 6 is the remaining six-segment number.
* 3 is the only five-segment number that completely overlaps with 1.
* 5 is the only five-segment number that completely overlays with 6.
* 2 is the remaining five-segment number.

Finally, build the output number and add it into the running sum which becomes part two's answer.

Segment mappings are stored as integers. The strings in the input data are letters representing segments. However, it is more
concise to store those segments as bits in an integer instead of characters in a string or set. This reduces many operations to
counting bits, which is built-in to `java.lang.Integer`, or bitwise operations. These are blazing fast to execute and are
intuitive to most programmers.

My first implementation looked for individual segments as well, which is not a bad idea when one is in a hurry to get on a
leaderboard and the clock is past midnight. However, this information is a means to an end: we do not use it to calculate the
answer. Not calculating and storing this information makes the code quite a bit shorter, easier to read and understand, and it
executes faster at run time. This earlier implementation also stored segment data in sets, which is intuitive, but cumbersome,
very verbose in Java, and was also very slow.

The only code I _do not_ like about the current implementation is how verbose it is to construct the output for a given line once
we have the mapping. Streams would be the logical choice, except stream reduction does not guarantee order which gives incorrect
results. While the input only ever has four digits so I could hard-code it on one line, I prefer general-purpose algorithms when
feasible.

## Day 9: Smoke Basin

[Year 2021, day 9][9.0]

Today's puzzle is an exercise in reading and processing a simple 2D array.

Part one wants us to find local minimums, where all surrounding numbers are greater than the number. This is a fairly simple
exercise in iterating an array and proper bounds checking. Then calculate a risk value for that number, which is an exercise in
reading comprehension in order to get the correct answer.

Part two describes the concept of a basin where elements of height 9 form the edges and are not part of the basin. However, this
concept can be simplified a bit. I thought of this as filling in a region in an image editor with a fill tool, with outlines (of
nines) marking the edges. Iterate the array in regular reading order, looking for any value that is not a nine. If found, call a
method that fills in that basin and returns its size. Filling in the basin changes all values adjacent to the provided coordinate
to nine if they are not already nine, then repeats that for coordinates that were updated.

For more information and examples of how this works, please reference the Wikipedia article on [flood fill][9.1].

Once that is complete, find the three greatest values in the returned sizes and calculate their product to get the answer.

## Day 10: Syntax Scoring

[Year 2021, day 10][10.0]

This is the first proper string parsing puzzle of the year. Given a bunch of input strings that follow specific rules, see which
strings are corrupt (do not follow the rules) or incomplete (follow the rules until there is more string to parse). Then score the
string based on what is wrong about it.

My implementation is a simple push-down automata, because the rules describe a [context-free language][10.1] with only a few easy
rules comprising eight non-terminal symbols. There are parenthesis, braces, brackets, and angle bracket pairs. They must match. To
determine this, I have a stack that tracks the hierarchy of the string, that is, the current state of what we expect to see later
in the string.

Anyway, the algorithm, for both parts, reads a line one character at a time. If it is an open character, it adds it to the stack.
Otherwise, it pops the stack and compares with the current character. If they comprise a valid open+close pair, then great, keep
going. If not, we either calculate a score for corruption (part one) or skip the remainder of the string (part two).

Part two continues by checking the stack. If it is empty, then we skip the second loop and move on to the next input string.
Otherwise, we pop the elements off the stack and score each character. Once there is a score for the line, we add it to a list
that contains all of the scores. Once part two is done parsing all of the lines, it sorts the scores and returns the middle one.
Done.

## Day 11: Dumbo Octopus

[Year 2021, day 11][11.0]

Today's puzzle is an exercise in 2D array processing. There is ten by ten grid of octopi that have energy levels. When their
energy rises above nine, they light up and increase the energy levels of their neighbors. This is slightly tricky because it is
possible to get into circular logic where several neighbors affect each other. Without careful tracking of updates during an
iteration, it is possible to get into infinite loops or stack overflow exceptions.

Overall it was fairly simple even with it requiring some careful guards in the code.

## Day 12: Passage Pathing

[Year 2021, day 12][12.0]

Enumerating paths through a maze is today's game. The maze is not a typical Advent of Code maze with a 2D grid, however, it is a
graph. We are given mappings between nodes, and need to find how many paths exist between start and finish. Instead of the usual
constraint in problems like this that nodes in a path must be unique, some nodes can be duplicated. In part one, upper-case node
names can be repeated. Part two adds the constraint that a single lower-case (small) node can be repeated in a path.

My solution is a recursive one. There is not really anything too special about it. Paths are lists of strings. Each part supplies
a lambda to the recursive function that tests whether it is legal to add a cave to the end of the path so far. This method call
enumerates all paths, and each part simply gets the number
of paths as the answer.

One important caveat to mention is that my solution will not work if two big caves are directly connected. This is not the case in
my input data or any of the test data provided. If this were to occur, the problem statement would need to be updated to handle
it. Otherwise, an infinite loop would occur, and this would be a big deal. I think it is safe to ignore this condition given the
puzzle requirements do not mention it and at least one input (mine) does not have this condition.

## Day 13: Transparent Origami

[Year 2021, day 13][13.0]

Today we get our first problem of the year that requires building a 2D array of points, and parsing them into a string.

Implementing the point transformation is fairly straightforward, although I suspect there is a more "correct" implementation using
matrix transformations.

I copied some code from year 2016 day 8 that converts known characters into integers whose bits represent which cells are turned
on. Then we do the same with the bits calculated from the input, and look them up in a map to get the equivalent character. Use
that to build a string and we get the answer.

## Day 14: Extended Polymerization

[Year 2021, day 14][14.0]

Advent of Code usually has a string processing problem where part two requires so much processing that a simple approach will
start bringing "heat death of the Universe" into the discussion, and today is 2021's one of those problems.

We need to count the letters in a string that grows per the following recurrence:

* L(0) = 20
* L(n) = 2 \* L(n-1) - 1

The string ends up being 19,457 characters long for part one, and 20,890,720,927,745 characters long for part two. While we can
easily generate the full string for part one, it is utterly infeasible for part two. A different approach is needed. There are
several properties of the input data and string generation rules that are relevant to my solution:

* Input rules cover every possible combination of characters that can appear in the string: in other words, additions to the
  string are always possible.
* At each step, the adjacent characters from the previous step are separated in the next step. Equivalent combinations may result,
  but the original characters are always separated.
* One separated, the previously-adjacent characters never interact again. Characters only interact with whatever is adjacent in
  the current step.
* Two pairs of characters in different parts of the string react identically.
* Characters are never removed from the string, only added.
* Finally, we do not actually need the string, which is good due to its size. We only care about the count of each unique
  character in the string.

Hopefully these insights make the result obvious: we only need to track the count of characters and pairs of characters. Here is
how the algorithm works:

1. Get an initial state for the template string consisting of counts of each character and pair of characters.
1. Loop however many times are required for whatever part we are on.
1. Create a new map of pair counts, and update that instead of the initial state.
1. Loop over each map entry of pair->count in the previous pair count map.
1. Calculate the new character to add.
1. Increment that new character's count by the number of times this pair occurred previously.
1. Construct two new pairs resulting from inserting the new character in the middle of the existing pair. Increment their counts
   by the number of times the pair occurred previously.
1. After the inner loop is done, replace the pair count map with the new one, making it the new previous map.
1. After the outer loop is done, get the most and least character counts from the character count map and calculate the answer.

Using this algorithm, we do not need to track the string as a whole: we only need to know how many times each character and pair
of character appears. This information is easily derived from inspecting the original template string and applying rules to the
counts.

It is possible to forego the count of individual characters, at least while iterating. Each character in a pair appears in two
pairs, so it is double counted. The only exceptions are the first and last characters in the template string. One could create an
individual count of characters from the count of pairs, then increment the first and last characters. Take the minimum and maximum
and subtract as before, then divide that number by two since it double-counts characters. However, I feel that this is a bit more
messy of a solution that adds more work. Especially with Java's wrapper objects, there are more boxing and unboxing operations
this way.

## Day 15: Chiton

[Year 2021, day 15][15.0]

Today we get our first path finding puzzle of the year. This is a straightforward exercise in navigating nodes, where each square
represents the cost to travel to that square from any adjacent location. This problem lends itself well to
[Dijkstra's algorithm][15.1], which is the one I chose. It is a slight variation, but effectively performs all of the same checks.

The sample data for _both_ parts had an optimal route that only traveled down and right. This is not an actual constraint on the
solution, but it leads programmers to believe that it is. This is a very big edge case that the sample data completely missed and
I found this misleading. If that actually were a constraint, then there are much better algorithms and this problem could be
solved in a trivial amount of time.

## Day 16: Packet Decoder

[Year 2021, day 16][16.0]

The subject of today's puzzle is trees, and for once, part one is the more difficult of the two parts. The puzzle statement uses
the word "packet" since the story describes network packets, however, it builds a tree. Both in the Java code and in this write-up
I will use the computer science terms for trees instead of packets.

Like any tree, there are leaf nodes and internal nodes. Together, they describe a mathematical expression that simplifies into a
single number. This is a fairly standard [abstract syntax tree][16.1]. Internal nodes represent operations that have one to many
operands, which are their child nodes. Comparison operators are restricted to two child nodes. Leaf nodes represent constant
numbers.

The algorithm starts by parsing the input, which is a hexadecimal string. Complicating matters is the fact that nothing is
guaranteed to align on any particular boundary, since as bytes or half bytes. Node headers are six bits. Number constants used a
packed format similar to how [UTF-8 handles encoding][16.2], with five-bit groups that repeat as long as the first bit is true.
This means we cannot simply chop up the hexadecimal into digits or pairs of digits, and instead must track all of the raw bits
somehow. I tried extracting the bit from the hexadecimal directly but the performance was awful. Instead, the code now builds a
boolean array and it constructs larger integers as needed from the raw bits. Using bit shifting, this is extremely fast.

Mathematical operations are implemented as an enumeration that operates on child nodes using streams to aggregate children into a
single value where possible. Comparison operators are not compatible with how streams work, so that is an old-fashioned binary
comparison operator. Given the root node, a single method call provides the answer for either part. For part one we get a version
number and add them all up. For part two we tell the root node to calculate its value, which uses its operator across all of its
children. That recurses to all of the child nodes and provides the answer.

## Day 17: Trick Shot

[Year 2021, day 17][17.0]

Today's puzzle is about calculating trajectories with integer coordinates and how they intersect with ranges. On the surface, this
appears to be a brute force problem, but there are some tricks we can apply.

The first observation to make is how the X and Y velocities are related to the current position. Consider X first, since it has no
inflection points. The object moves right N positions, then N-1, then N-2... until it decays to zero. Recall that if we sum that
sequence, we get the [triangular numbers][17.1]. Y is more complicated because with positive velocity it reaches an inflection
point and essentially has two arcs. However, the same rule applies to it.

For part one, we only need to know the highest possible position where it hits the target. Consider that the velocities work
independently. We know there are X velocities that hit it because there has to be an answer. Furthermore, consider the movement in
Y. No matter what its velocity is, the graph of its Y velocity follows a symmetrical arc. It must pass back through 0, the X-axis.
The best possible launch is one where it is at Y=0, then in the next iteration, it ends up at the bottom of the target. We don't
even need to know _when_ it hits, or where it hits in the X-axis. Given the distance from Y=0 to the bottom of the range, we can
use the triangular number formula to get the height.

Let's say the bottom of the box was at -6. That means it traveled 0, 5, 9, 12, 14, 15, 14, 12, 9, 5, 0, -6. If we plug the 6 into
the triangular number formula, we get t(6) = 6(5)/2 = 30/2 = 15, which is the answer. We can directly calculate part one this way.

Part two is a little more tricky but we can at least speed it up and reduce the search space. Specifically, we can calculate the
precise boundary conditions based on the input data so we never omit any potential velocities.

* X, lower bound: we need to reach the minimum X in the range, so we need the first triangular number equal to or greater than the
  minimum X. Imagine trying to hit its lower-left corner: this is the minimum horizontal velocity that will barely nick that edge.
* X, upper bound: similarly, if we want to the upper-right corner, the maximum X velocity that can do this is that upper-bound
  itself. We would hit it in one iteration.
* Y, lower bound: the first Y velocity that can possibly hit the range is the range's Y-lower-bound itself. Imagine throwing the
  ball really hard at the lower-left corner. It would hit in one step. Throwing any lower cannot possibly hit the range.
* Y, upper bound: The highest we can throw to hit the range would be a high lob that barely nicks the upper-right edge. Similar to
  how we directly calculated the answer to part one, this is the Y velocity that will pass through the _top_ of the range on the
  next step after passing back through zero: it is the absolute value of the range's maximum Y value.

The final minor optimization is detecting when to stop simulating the submarine toss because it mathematically cannot hit the
range. This occurs when its current position is either below or to the right of the range.

There are certainly other optimizations, such as pruning combinations that don't work together. A high lob (large Y velocity) is
not compatible with a high X velocity, even if either on its own could potentially hit. However, the solution runs very quickly
as-is and is quite readable. I would like to keep it that way.

## Day 18: Snailfish

[Year 2021, day 18][18.0]

What a ride today was. This is a puzzle about binary trees, although it would be possible to implement it using direct string
manipulation. Regardless, I opted for using binary trees.

One key observation to make is that every node in the tree has zero or two children, never one. This makes sense since the puzzle
is about addition, which is a binary operation. This means that an internal node always has two children, although they can be any
combination of leaf and internal nodes. This tree also is not balanced, and we never need to rearrange nodes as we would in a more
standard data structure such as TreeMap. However, this puzzle has its own problems which can be tricky to solve.

* Parsing is straightforward and took me the least time to implement. This is an [LL parser][18.1] highly specific to the
  particular grammar described in the puzzle.
* The exploding part of the reduction was a little tricky. Distributing a pair of values requires finding the leaf nodes
  immediately before and after the current node when performing an in-order (or reverse in-order) traversal. The key there is
  _leaf_ nodes. Most algorithms visit all of the nodes, but that is not what we need. Furthermore, due to the constraints of all
  nodes having zero or two children, we can optimize this a little bit anyway. Since all nodes are full, we find the next leaf
  node by traversing up until the node we just came from is the current node's left child. Then we traverse to its right child,
  and descend down the left side until we hit a leaf node. Then do the reverse to find the previous leaf node.
* Splitting is trivially easy. Perform an in-order traversal until we find a leaf node with a value greater than nine. Add two
  children to that node and erase its value.
* Part one asks us to find the sum of all the expressions. This does not mean evaluate each expression and sum those numbers. It
  means combine trees one and two, then reduce them. Add tree three to that first reduced tree, and reduce the "sum" of those
  trees. Repeat until we have a single tree that is completely reduced.
* Part two requires us to check every combination of trees, and in both orders because this tree "addition" is not commutative. It
  is important to make a copy of the tree because in my implementation, trees are not immutable and my code updates them in-place.
  Without copying, the result of one calculation will ruin later calculations.

## Day 19: Beacon Scanner

[Year 2021, day 19][19.0]

Today's puzzle was the first one that truly gave me trouble. Conceptually it was not difficult, but coercing the code to run
quickly was a challenge. It took several iterations to get its performance comfortably and consistently under my self-imposed
limit of one second.

A simple approach works, but can take a _long_ time to finish. This is fine for getting one's stars for the day, but ultimately, I
want an algorithm that performs well without being an unreadable mess. The issue here is there are thirty-eight scanners, and
millions of ways to compare their beacons. Comparing beacons millions of times, including transforming them first, takes a lot of
time. We _need_ a way to reduce the search space.

Think about the problem statement: two scanners' fields overlap when twelve or more of their beacons overlap. Those twelve beacons
are common between them, but may not have the same coordinates due to having different transformations. However, no matter how we
transform them, their relative positions must stay the same because none of the transformations are mirrors. Imagine rotating a
die however you like: each face, one through six, maintain their position relative to every other face. The same principle applies
here. No matter how we transform those twelve points, their relation to each other remains the same.

There are several ways to express this in an algorithm, but the one I chose is to store the distances between points in addition
to the beacon's points for each scanner. After parsing each chunk of program input, we have an `UnmatchedScanner` record. This
contains the points' coordinates as well as a set of all distances between those points. This is the [Euclidean distance][19.1],
not the [Manhattan distance][19.2] so often used in Advent of Code, because the latter does not work here. Even with the Euclidean
distance, we need more precision than an integer, which is probably why the Manhattan distance does not work. There are too many
distances that round to the same integer value. When storing the distance I use `BigDecimal` and round it to two decimal places.
This provides precision while also avoiding the [many pitfalls with comparing floating-point values][19.3]. One decimal place
works for my input, while zero does not: I added an extra order of magnitude to be safe.

The next step is to map each scanner to every other scanner that overlaps it. This can be done quickly using the distances. Add
the first `UnmatchedScanner` to the mapped scanners as the starting point. Then iterate the input and consume it until nothing
remains. We look for another `UnmatchedScanner` that has sixty-six distances in common. This is because we need twelve beacons
in common. Given a beacon, it needs to have eleven distances in common with another beacon. Given a second beacon that must
already have a distance in common with the first, it must have ten distances in common. This ends up being the
[triangle number][19.4] `T(11) = 11(12)/2 = 11(6) = 66`. If two scanners have this many distances in common, then they must be
linked. Build a map going both ways: every `UnmatchedScanner` is mapped to a list of other `UnmatchedScanner`s whose fields
overlap. This mapping goes both ways.

Next, we extract the first scanner from that input mapping and add it to the output mapping. Then, one by one, process the input
mapping. At each step, see if an `UnmatchedScanner` maps to anything that is processed and has a known location. If so, process it
by comparing to the processed `Scanner` and figuring out its transformation and shift. Once that is done add it to the processed
mapping and remove it from the unprocessed mapping. This has to be done carefully to avoid a `ConcurrentModificationException`.

During processing of a scanner, we also calculate its distance to every other known scanner and add that distance to a set of
distances. This is required for part two.

Once all of that is done, we have two collections. One has all of the scanners with the locations of the scanners and their
beacons fully transformed and shifted, while the other has all distances between scanners. All that is left is to record a record
containing the answers to each part: the number of beacons, and the maximum distance.

This solution is not perfect and could be improved further, but it has acceptable performance for now.

## Day 20: Trench Map

[Year 2021, day 20][20.0]

Today's puzzle is a modified [Conway's Game of Life puzzle.][20.1] simulation.

Given pixels that are on, locate each pixel. Get the nine pixels forming a square centered on that pixel. Turn them into a number
based on the binary of whether they are on or off, in reading order: left to right, top to bottom. Index that number into another
string. If the value is #, the that pixel is on. Otherwise, off.

It was a little confusing today because the test data worked fine for an algorithm coded to the wording of the problem. However,
it could fail with the real data because of an unspoken edge condition: the index string has # in the zero spot, and "." in the
final spot. This means the entire 2D plane "flashes" each iteration, turning on then off. Since the iterations in the problem are
both even this means they will be off, but it does mean that an implementation needs to assume that unknown coordinates are either
on or off depending on which iteration it is.

## Day 21: Dirac Dice

[Year 2021, day 21][21.0]

This is a simulation of a dice game. Part one has a very large but deterministic die. This is a fairly straightforward
implementation that simulates the game directly. Of note I store the rolls and locations as zero-based because it makes modulo
operations more clear. Other than that it is a direct implementation of the game's rules.

I believe there may be a way to calculate the answer to part one directly, or otherwise simplify the simulation greatly. While I
did find some patterns in the players' movement, I did not find any shortcuts that work across all of the game states.

Part two introduces a twist that I have not seen before in Advent of Code. We now have a quantum die that simultaneously rolls all
possible values. But if that happens, should it not collapse into a single value when we measure it? Anyway, there are far too
many values to simulate them all. Instead, there are other approaches that work here. Memoization is an obvious choice, but I
could not get that to work correctly. Instead, I opted for collapsing parallel states into a single state.

When we make the first move, we must consider _all_ possible first moves. When rolling multiple dice, there is an inherent
probability distribution. This is why there is a 1:36 chance of rolling snake eyes on two standard dice. There is only one way to
roll a sum of two or twelve. On these dice, there is one way to roll a three or nine. All of the dice must come up with one (sum
of three) or three (sum of nine). There are three ways to roll a four or eight. For four, we need two ones and a two, but that two
can appear on any of the three dice. Likewise through the other numbers, we can find all of the possible sums and the number of
unique ways to roll that sum.

With the probability distribution in hand, we can then simultaneously roll all possible turns. The key here is if there are three
ways to roll a sum, we do not need to simulate it three times. How we got that sum is irrelevant, only that it happened in three
parallel universes. Simulate it once, then multiply the win counts by three.

The next improvement is to add [memoization][21.1]. When enumerating all of the possible game states, there are billions of
duplicates that will have the exact same results. Detecting these duplicates early on means we can prune entire subtrees of the
search graph, with each subtree containing even more duplicates. However, memoization has a tricky implementation detail. I store
player scores and locations in integer arrays because it simplifies updating the values for whoever's turn it is.
[Java Records][21.2] do not play nicely with arrays when you use them as keys in a hash table (usually `HashMap<>` or
`HashSet<>`). This is because by default, records use whatever built-in implementation an object uses for `equals()` and
`hashCode()`. If I used a `List<Integer>`, this would use the list's implementation but require tons of boxing and unboxing.
Arrays do not override `equals()` or `hashCode()` and arrays cannot be extended, being core data types. They end up using
reference semantics for those methods, which breaks memoization. This requires overriding those methods for the record, which
diminishes one of records' selling points: conciseness.

## Day 22: Reactor Reboot

[Year 2021, day 22][22.0]

This is a puzzle that requires a different approach than the simple one which would take _far_ too long to process. The approach I
picked may not be the fastest, but it works.

Consider each volume in the input one at a time. How can we add that volume to the existing, known volumes without double-counting
any specific locations? This may sound weird, but by _triple_ counting it. If two "on" volumes exist and overlap, the intersection
of those volumes will be double-counted as on so we need to represent the intersection as turned off to balance it out. This
quickly gets complicated when many volumes overlap, but keep in mind we are performing these checks for _all_ volumes which means
everything should balance in the end.

* For each existing volume that a new volume overlaps, add the intersection of the volumes. The intersection's "on" status will be
  the opposite of the existing volume's flag.
* Once the previous check is done, add the new volume _only if it is turning on lights_.

This quickly balloons the number of volumes being tracked, resulting in an `O(n^2)` algorithm. This is less than ideal, and there
are better algorithms out there. Partitioning the space with perpendicular planes across each axis would result in far less data
to track, but I could not get this working correctly in a reasonable amount of time so I stuck with the current algorithm. I may
revisit this at some point in the future.

I do have a Java-specific gripe about the implementation. It is slow due to the way Java requires us to manage collections. We
cannot modify the main collection of volumes while iterating it, so we need to perform a few extra allocations along the way as
well as bulk copying of data. `ArrayList` is not what I would call the ideal data structure for this, but `LinkedList` performed
far worse due to it copying data into temporary arrays when adding elements in bulk. The performance is not terrible but I suspect
there are a lot of unnecessary operations being done inside the framework.

## Day 23: Amphipod

[Year 2021, day 23][23.0]

This was a tough one to get just right. There are a lot of ways to trip oneself up while figuring out valid moves, and there are
many cases where one can end up in the same state from multiple previous states: maybe even with the same energy.

This is a basic BFS search algorithm that figures out all of the possible moves from a given state. It then applies those moves to
the current state, making a copy, then adding those new states to a priority queue. At the top of the queue processing, see if
this is a garbage state that we can discard. If so, do it. Otherwise, add it to the cache of known states and process as before.

Pathfinding can be quite redundant, so the algorithm determines all possible paths ahead of time. Then during the main loop we can
simply check each path to see if it is applicable. Is it possible to move away from the current location? For example: if an
Amphipod is in its home and has no strangers in there with it, do not move. Next, is the path obstructed at all? If so, do not
move. Finally, is the destination valid? Precomputing paths means that this logic can be a little simpler than figuring it out
each time. Hallway to hallway paths simply do not exist, and neither do paths that end in the wrong room. The main concern here is
ending a path at the correct depth inside a room, and not even entering the room if a stranger is in there.

Performance is less than ideal but within my self-imposed margins of each part completing in under one second on modern desktop
hardware. I found it was more important than usual to pick the correct data structures to represent state. Earlier versions spent
a lot of time doing busywork caused by inefficient data structures. If I improve this again, I would likely switch to using an
array instead of a map of points. Combined with tight state transition rules, maybe a 2D array, I think it would remove even more
overhead. Whether the cost of clarity and readability is worth it is another question, however.

## Day 24: Arithmetic Logic Unit

[Year 2021, day 24][24.0]

Today's puzzle required some intuition about reading and understanding a simplified form of assembly language. Several Advent of
Code years have had similar puzzles where the code naively blunders ahead and attempts to run so many instructions that the Sun
will consume the Earth by the time the program completes. This year is no exception. Others have done a better job than I could
have explaining this, and there are multiple approaches to the problem.

First, let us analyze the program input. It is an assembly program - not any typical assembly that real computers use, but a
pseudo-assembly that mimics those languages regardless. There are no loops or decision instructions. This deceptively implies it
can run quickly. The issue is the input size, and the fact that the vast majority - well over 99% - of all inputs are invalid.
Here are the patterns to notice about the code:

* Input instructions occur regularly, and divide the program into nearly-identical blocks.
* The _w_ register is only ever used to receive input.
* The _x_ and _y_ registers are reset at the start of each block. Their state does not carry over.
* The _z_ register starts out zero like the rest of them, but its state _does_ carry over between blocks.
* Blocks differ by magic numbers hard-coded into the input.
* During each block, _z_ is always divided by either one (identity) or 26. It may subsequently be multiplied by 26.
* Z can grow or shrink during each block, but eventually needs to be equal to zero.

The magic numbers are used to test each digit, with the results of the test being stored in _z_ which also acts as a stack that
stores its values in base 26. If that stack cannot be reset back to zero, then the input number is invalid and must be rejected.
The next key intuition here is to match up stack pushes with pops. What are we getting into and out of _z_? There are much better
explanations of what is going on here in detail, but the short version is we alter _x_ with one of the magic numbers, then combine
it with _z_. When popping, we extract the lower value of _z_ and check it against a current magic number. This will either do
nothing or increase _z_ again which eventually results in it being unable to reach 0 at the end (corrupts the stack).

Knowing this, we can extract the magic numbers from each section. When _z_'s divisor is 1, we push a value to the stack, When it
is 26, we pop a value. The value to push is the value that occurs on line 15 of the section. When popping, we need the first magic
number which will also be non-positive. Add these two numbers (one of which is either negative or zero) to get the difference
between two of the digits.

The (Java) program code detects these magic numbers, then processes them in triplets. Run through the magic number array and track
its index. When the divisor indicates a push, push the current index and the third magic number to a stack. When the divisor
indicates a pop, pop the most recent values. Create a new rule object with the two indexes. Add the pushed magic number to the
non-positive magic number. We now have a rule that relates two digit indexes and the integer difference between them.

To construct the number, iterate through the rules. One of the two numbers will be either 1 or 9 depending on whether we need the
greatest or least number. The other number will be whatever the first number is combined with the difference. This also changes
depending on whether the difference is negative or positive: look at the code for a better explanation.

This implementation extracts the magic numbers from the input and _should_ produce correct results for other inputs in theory,
although I have not tested the code against anything other than my inputs.

## Day 25: Sea Cucumber

[Year 2021, day 25][25.0]

The final puzzle this year is a relatively easy one as is tradition. We need to simulate a grid of sea cucumbers that move with
predictable motion. They all face either east or south. Each round, they perform the following steps:

1. All east-facing cucumbers check the location to their east, wrapping around to the start of the row, to see if it is vacant.
1. All east-facing cucumbers that determined they were able to move in step 1 now move.
1. All south-facing cucumbers check the location to their south, wrapping to the start of the column, to see if it is vacant.
1. All south-facing cucumbers that determined they were able to move in step 3 now move.

This continues until there is gridlock and no sea cucumbers are able to move during a round. The number of rounds required to
reach this condition is the answer to the puzzles.

[1.0]: https://adventofcode.com/2021/day/1
[2.0]: https://adventofcode.com/2021/day/2
[3.0]: https://adventofcode.com/2021/day/3
[4.0]: https://adventofcode.com/2021/day/4
[5.0]: https://adventofcode.com/2021/day/5
[6.0]: https://adventofcode.com/2021/day/6
[7.0]: https://adventofcode.com/2021/day/7
[7.1]: https://en.wikipedia.org/wiki/Triangular_number
[8.0]: https://adventofcode.com/2021/day/8
[9.0]: https://adventofcode.com/2021/day/9
[9.1]: https://en.wikipedia.org/wiki/Flood_fill
[10.0]: https://adventofcode.com/2021/day/10
[10.1]: https://en.wikipedia.org/wiki/Chomsky_hierarchy
[11.0]: https://adventofcode.com/2021/day/11
[12.0]: https://adventofcode.com/2021/day/12
[13.0]: https://adventofcode.com/2021/day/13
[14.0]: https://adventofcode.com/2021/day/14
[15.0]: https://adventofcode.com/2021/day/15
[15.1]: https://en.wikipedia.org/wiki/Dijkstra's_algorithm
[16.0]: https://adventofcode.com/2021/day/16
[16.1]: https://en.wikipedia.org/wiki/Abstract_syntax_tree
[16.2]: https://en.wikipedia.org/wiki/UTF-8#Encoding
[17.0]: https://adventofcode.com/2021/day/17
[17.1]: https://en.wikipedia.org/wiki/Triangular_number
[18.0]: https://adventofcode.com/2021/day/18
[18.1]: https://en.wikipedia.org/wiki/LL_parser
[19.0]: https://adventofcode.com/2021/day/19
[19.1]: https://en.wikipedia.org/wiki/Euclidean_distance
[19.2]: https://en.wikipedia.org/wiki/Taxicab_geometry
[19.3]: https://bitbashing.io/comparing-floats.html
[19.4]: https://en.wikipedia.org/wiki/Triangular_number
[20.0]: https://adventofcode.com/2021/day/20
[20.1]: https://en.wikipedia.org/wiki/Conway's_Game_of_Life
[21.0]: https://adventofcode.com/2021/day/21
[21.1]: https://en.wikipedia.org/wiki/Memoization
[21.2]: https://docs.oracle.com/en/java/javase/14/language/records.html
[22.0]: https://adventofcode.com/2021/day/22
[23.0]: https://adventofcode.com/2021/day/23
[24.0]: https://adventofcode.com/2021/day/24
[25.0]: https://adventofcode.com/2021/day/25
