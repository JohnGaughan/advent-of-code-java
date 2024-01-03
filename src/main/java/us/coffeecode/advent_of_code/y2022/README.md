# Year 2022: Jungle Expedition

## Day 1: Calorie Counting

[Year 2022, day 1][1.0]

As usual, the first puzzle is easy.

Use the InputLoader I already wrote along with a lambda to read in groups of longs, then collapse those groups by summing them.
From here it is a simple matter to sort the array, and either get the last element (part one) or the sum of the last three (part
two).

## Day 2: Rock Paper Scissors

[Year 2022, day 2][2.0]

Today's puzzle describes a rock-paper-scissors tournament with the two parts specifying different ways to interpret part of the
input. Scoring is the same. The second part of the input means what choice the second player makes with the result being the
outcome of the round for the first part. For the second part of the puzzle, the second part of the input designates the desired
outcome of the round with the result being how the second player should play. In both cases, the score is based off of how the
second player plays and the outcome (winner) of the round.

This solution uses streams to read the input file. It then scores each line, converting it to a long integer. Finally, it sums all
of the scores which provides the solution.

Scoring is implemented using a lambda which allows each part of the solution to provide its own implementation into the common
parsing logic, allowing it to reduce each line to a long integer using different logic.

Both parts read the first code point and convert it to a Play representing the opponent's play. Next we need our play and the
outcome of the match: given either piece of information we can get the other. Part one parses our play and determines the outcome,
while part two parses the outcome and determines our play.

Given those two pieces of information we have the score for that line: it is simply the sum of the values for our play and the
outcome. These numerical values are given in the puzzle statement.

## Day 3: Rucksack Reorganization

[Year 2022, day 3][3.0]

This puzzle describes a group of elves carrying items in rucksacks, where items are identified by case-sensitive letters. In part
one we treat each line of input as two compartments and need to find the items that are in common between those two compartments.
Then we need to find the sum of their priorities, which is a simple mapping from a letter to a number.

The key insight here is we need to use set operations. If a letter is duplicated, we only count it once. This is covered by the
problem statement where it mentions item "types" instead of items.

First, I implemented some helper methods that are reused in multiple places. Using this technique helps make stream operations
easier to read when the methods have descriptive names. First is a `toSet(String)` method that gets an IntStream for the code
points in the string. It then converts the ints to Integers, and collects them in a set. This is a convenient way to convert a
string into a set of unique code points. Second, I implemented a method that gets the priority of a letter.

My solution for part one uses stream operations. Load all of the lines, then in one big step map each line to the intersection of
the two sets. I implemented this operation in a separate method to make it easier to follow what the code is doing. It chops the
input stream in half and calls the `toSet()` method on each half to get the letters, then both of those are input into Guava's
`Sets.intersection()` method that returns their intersection. Back to the first stream operation: from here I map each set to a
long that is the sum of the priorities for the letters in that set, which is a nested stream operation. Then I sum the stream to
get the answer.

Part two asks us to split the input into groups of three elves. We need to find the single letter in common that they carry, and
the compartments from part one are not relevant here. There is no clear or simple way to do this with Java streams, so I
implemented a `groups()` method that iterates every third line in the `List<String>` input. It checks that line and the following
two lines, converting each line to a set, and putting the resulting sets in a `Group` record. This record type has a single method
that finds the intersection of its three sets and returns the single letter that subset contains.

From here the stream operation to reduce the input is easy. Get a stream over the groups. Map each group to its intersection,
which is a single letter. Map that to a long, which is that letter's priority. Get the sum over the entire stream and that is the
answer.

## Day 4: Camp Cleanup

[Year 2022, day 4][4.0]

Today's puzzle describes elves that need to clean areas in camp, but these areas overlap. This is a simple range checking puzzle.
Given a large number of pairs of ranges, count how many range pairs meet criteria.

Part one asks us to count how many pairs have one range that completely contains the other, while part two asks us to count the
pairs that have any overlap at all.

This is a simple exercise in stream operations. Load and parse the input into records, where each line is a `Range[]` with two
elements. Then filter the stream, retaining only those elements that pass the requirements for each part. Finally, count the
elements in the stream.

The only other piece is implementing the range checks. For part one, we check if the start of one range is equal to or greater
than the start of the other, and that the end is equal to or less than the end of the other. This check must be performed in both
directions, because either range could be contained inside the other.

Part two's overlap check is similar, except there are four conditions that indicate an overlap. If one range contains the other's
start or end, that is an overlap (two checks). Then we need to perform this check in the opposite direction. The reason is if one
range fully contains the other, checking the larger range against the smaller will not indicate an overlap.

## Day 5: Supply Stacks

[Year 2022, day 5][5.0]

The fifth puzzle is an exercise in manipulating stacks. We need to move items between stacks using a series of instructions that
specify how many items to move, from which stack, to which stack. The two parts differ in how to order items during the move: part
one moves items one at a time, which ends up reversing their order after the move, while part two moves the items maintaining
their relative order.

Most of the code parses the input and constructs the stacks and move operations, with a small part implementing the algorithm used
to solve the puzzle. The code uses the Deque interface because Java's Stack class is heavily discouraged from use. I will not go
into detail here but read the JavaDoc for both classes if you are not familiar with this. Anyway: part one performs a simple
operation that retrieves and removes the head of the "from" Deque then adds it to the "to" Deque and repeats this process a number
of times equal to the quantity given.

Part two uses a buffer Deque. Move items there first, then from the buffer to the destination. This reverses their order twice,
effectively maintaining their order.

## Day 6: Tuning Trouble

[Year 2022, day 6][6.0]

Today's puzzle is a simple text search to find four or fourteen consecutive characters in a string that are unique. We need to
report the index of first character where the previous _N_ characters are unique.

This is trivially implemented by converting the input string to an array of code points. Then iterate through the array,
converting a window of it to a stream. Use the stream operator to reduce it to distinct elements, then count the remaining
elements and compare that quantity to the desired number of digits. If it matches then we know the answer is the end of that
search window.

## Day 7: No Space Left On Device

[Year 2022, day 7][7.0]

As expected for about a week in, we have a tree parsing and processing puzzle. This time it relates to files in a file system that
are populated based on Linux commands typed into a command line.

The bulk of the work is translating the shell I/O into a set of commands and results, and then into a tree structure that is aware
of its total size at each node, where each node counts its children's sizes.

From here, part one gets the sum of all nodes with a total size under a threshold. Part two asks to find the smallest node greater
than a value we need to compute based on the given total file system size and the size of the root node. Stream operations make
this trivially easy to implement and read.

## Day 8: Treetop Tree House

[Year 2022, day 8][8.0]

This puzzle is an exercise in a two-dimensional height map where each coordinate is a tree of a specified height. For part one we
need to count how many trees are visible from outside the map, while part two asks us to find the map location that can see the
most trees. This should be a map location surrounded in the four cardinal directions by trees with a smaller height value than
it.

There is not much to say here: we need to iterate through the map and count values, scoring each location and finding the highest
score.

## Day 9: Rope Bridge

[Year 2022, day 9][9.0]

Today's puzzle describes a series of knots in a rope that move as the head knot moves. The question is how many unique
coordinates the tail visits. The two parts differ in that for part one there is only a head and tail, two knots; while in part
two there are ten total knots.

Part one is easier because the tail's movements are simpler by virtue of the head only moving in the four cardinal directions.
The tail can move diagonally to keep up while the head cannot. However, in part two, the previous tail (knot 2) is now the head
of another head-tail connection and it _can_ move diagonally. Thankfully, the puzzle instructions provide examples that show this
and describe how the algorithm works. This was text I did not pay much attention to while coding part one but it is key for part
two.

When two knots adjacent in sequence are separated, we need to snap the tail knot to the position immediately behind the head knot
when they are collinear in one dimension: that is, their X or Y coordinates match. If they are no collinear, then we need to move
to whatever diagonal coordinate makes them adjacent in the plane.

## Day 10: Cathode-Ray Tube

[Year 2022, day 10][10.0]

The tenth puzzle describes a simple processor that tracks its clock cycle and a single register. It understands two instructions.

Part one's point is to ensure we parse the data and understand the basic semantics of the simple processor. We need to capture
values of the register at specific clock intervals and use the cycle number and that register to compute a value. Add up those
values to get the answer.

Part two has us test values to determine if a pixel on a display should be lit up or not. The display ends up showing letters,
and I already wrote logic to convert parts of a 2D array to code points which Java understands. One thing that might trip people
up is the problem statement says the clock starts at cycle one, but Java, like most languages, has zero-based arrays and the
clock value determines which pixel to process.

I also found it a little difficult to understand the condition for turning on a pixel, but I have a more obvious explanation than
the instructions. Using a 0-indexed array and a cycle that starts at zero, we focus on one pixel at a time. The pixel number and
cycle number are the same. Cycle 0 we consider pixel 0. Cycle 100 we determine what to do with pixel 100. In my solution I
implemented a 2D boolean array and use divide-and-remainder logic to determine the X/Y coordinate in the array, but the same idea
of "pixel number" is valid. At each clock cycle we look at the X register. If its value is equal to or one lower or greater than
the clock cycle, turn on the pixel indexed by the clock cycle number.

Once that is complete I call into logic I wrote a while ago that turns that 2D boolean array of on/off pixels into a string based
on known patterns from this and other Advent of Code puzzles.

The only thing I really do not like about my solution is the if/else/for logic in both parts' loops over each line of the input.
I wish there were a better way to combine some of that logic and avoid repetition, but I believe I have done as much as I can.

## Day 11: Monkey in the Middle

[Year 2022, day 11][11.0]

Every year usually has a problem that cannot be beat without modulo arithmetic, and today is the day for 2022. We are given a
problem where a group of monkeys continuously calculate a number and send it to another monkey who then performs its own
calculation. The problem is the calculation functions in the program input always increase the number. Given enough rounds, that
number will overflow and produce incorrect results.

For part one, we are given that this number is divided by three as part of its calculation. Part two says that is no longer the
case, but leaves open-ended how to reduce that number (worry level). Generally when a puzzle says you have to calculate something
but does not give the calculation, we need to look for patterns in the input to come up with the only safe alteration to the
calculation that will not change the results.

In this case, we are given that monkeys test for divisibility when determining where to send an item. If a monkey looks for
divisibility by five, for example, then we know that adding or subtracting a multiple of five does not change the answer to that
question. Since 8 is not evenly divisible by 5, neither is 13, 18, 23, and so on. This is the core idea of modulo arithmetic. We
know that if a worry level is some huge number such as 1,298,534,063 we can safely reduce that number to 3 and know that future
calculations will work correctly because the residue is the same. Furthermore, this will keep the number safely under the
threshold of integer overflow.

However, we need to ensure this property is the same for all monkeys. The way to do this is to calculate the LCM, lowest common
multiple, for all of the monkeys. I noticed that in both the test input and real input all of the divisibility tests use prime
numbers. This means we can simply multiply all of the numbers used in the divisibility tests together since by definition none of
them have any common factors greater than one. Once we have the LCM, our reduction function is to take the worry level modulo the
LCM to get the new, lower worry level.

## Day 12: Hill Climbing Algorithm

[Year 2022, day 12][12.0]

Today we get our first [Dijkstra's Algorithm][12.1] puzzle. We need to navigate from start to finish using some simple navigation
rules for part one, while for part two, we need to find the shortest path from the finish back to any nodes with the lowest value.

Search algorithms such as Dijkstra and A\* have many variations but in this case we get the correct answer in a short amount of
time with plain old Dijkstra. We do not need any additional heuristics, for example. Since all paths have equal cost,
breadth-first Dijkstra will always have the lowest cost the first time it visits a location with the exception of a tie which
does not change anything.

The search algorithm uses a common method with lambdas used to insert logic specific to each half of the puzzle. Specifically,
part one searches from the starting coordinates while part two starts from the ending coordinates. Of course, this means the end
condition is different so there is a lambda for that as well. Part one completes when we find the end coordinate, while part two
ends as soon as we find a coordinate at the "a" elevation. Finally, each part needs to perform an elevation test opposite the
other part to test if a move is valid.

We start off by reading the input map, identifying the start and end coordinates, and replacing their entries in the map with the
values specified in the problem. Next we create a queue of coordinates we are currently visiting, as well as a map of visited
coordinates and their costs. We seed these initial data structures by feeding in the first coordinates using a lambda function,
then start iterating.

Get the head of the queue and ask it to provide its cardinal neighbors: up, down, left, and right. For each of those test if this
is a coordinate that needs to be processed:

* Coordinate must be in-bounds.
* Step must be valid per the vertical travel requirements. This is tested by lambda since parts one and two find paths in reverse
  from each other.
* Coordinate must not have been already visited.

Once we know this is a valid move, we check whether this is an end condition. Did we find the target coordinates? If so, the
search is complete and we can return the cost of visiting these coordinates. This is the first time visiting this location and we
cannot visit it again anyway, so there is no point in continuing to search. Otherwise we add this location to the queue to visit,
mark it as visited, and iterate again.

## Day 13: Distress Signal

[Year 2022, day 13][13.0]

The past few years have had a puzzle that requires parsing a standard format such as JSON, and that is what today is all about.
We need to parse strings into JSON arrays, then compare those arrays using a custom comparison function described in the puzzle
text.

Java has the [org.json][13.1] library available via Maven, and that is what I use to parse the strings that represent arrays. Part
one requires comparing individual strings to make sure the comparison function is correct. Fun fact: I got the correct answer with
the test input but not the real input. There are edge cases that the test input does not catch. Anyway, the comparison works like
a standard Java compare function, or a comparator. It follows the rules set out in the puzzle, to include the "I don't know" logic
where an individual comparison can return zero to indicate equality. That is an important piece that could be missed and is useful
to ensure that comparisons continue through arrays or equal integers during all of the edge cases.

Part two has us sort all of the arrays together now that the comparison method should work correctly. Add in the two bonus
arrays, sort, then search to get the answer. Once part one is complete, part two should be trivially easy.

The other difficult part is remembering that the arrays are ordered using a one-based index, in contrast to virtually every
programming language in common usage.

## Day 14: Regolith Reservoir

[Year 2022, day 14][14.0]

As the puzzle hints at in a link to the previous year 2018 day 17, this one is similar to the Reservoir Research puzzle four
years ago. The rules are different this time, and actually simpler, thankfully.

We need to simulate sand falling into shapes, then count the number of sand particles that came to a rest. This is a simple
nested loop. The outer loop keeps going as long as there is room for sand, while the inner loop moves a new sand particle until
it either falls into the abyss (part one) or comes to a rest (both parts). The difference is part two has an infinite floor that
means sand cannot disappear off the edge of the map.

The solution I came up with searches for a particle's resting place until it is either found or no such location exists. Then the
algorithm halts and returns the answer, which is the number of particles at rest.

## Day 15: Beacon Exclusion Zone

[Year 2022, day 15][15.0]

Today was quite a journey. The sheer magnitude of the search space makes brute force approaches infeasible. Some optimization or
short cut is required. Representing the search space using individual points is madness if you do so across the entire space, and
is slow but feasible if you can cut it down some.

However, there are better ways.

For part one, we need to find which beacons are situated on row 2,000,000. This is trivially easy. Next, we need to know how each
beacon covers that row, if at all. Given a sensor's coordinates and its Manhattan distance to its closest beacon we know if it is
in range of that row or not. Furthermore, we know its reach on that row: subtract the Y coordinates of the sensor and the row,
and that is the "left over" amount of Manhattan distance. Picture an L. The sensor has to spend its range to reach that row. The
difference is how far left or right it can reach.

Since a beacon cannot be inside the sensor's range, only at its edge, we need to see if there are any beacons right at the
sensor's range and pull it back one in that case. Then we construct a Range object that represents that beacon's reach on that
row. Repeat for all beacons.

Next, we have overlapping ranges so we need to merge them. Today I added convenience methods to Range to get its union with
another range. If ranges overlap, the method combines those ranges and replaces them in a sorted list with the new value. Repeat
until there are no possible merges remaining and the ranges are guaranteed to be disjoint.

Finally, get the size of each range and add those sizes to get the answer.

For part two, there are naive approaches that are very slow. However, I found one that finds an answer in under 10ms. The beacon
that is currently unknown can be touching a sensor's area but cannot be on its edge. There is also only one beacon, so there can
only be one gap in the search area where it can fit. These preconditions lead us toward a powerful conclusion.

Consider a sensor. Its search area is a diamond: a square turned 45 degrees. It is surrounded by four lines that are parallel to
each side and one unit away. These lines are diagonal, having slopes of 1 and -1. This means we can trivially describe these
lines using the equations `y = c + x` and `y = c - x` where `c` is the Y-intercept.

Next, we know there is only one gap because there is a single answer. Consider two sensors. If they had a gap of two between them
then there would be no unique answer. Instead, there must be a diagonal gap of one. This means two sensors must share an
identical line: same slope, same Y-intercept. The answer lies along that line.

Now consider two different sensors with the same property, except the slope is opposite of the first pair of sensors. The
intersection of these lines is a gap of a single coordinate which must be the answer. If the sensors did not have this property
then there would be zero answers or more than one answer. The precondition of a single answer and the overlapping and
intersecting lines is a tautology.

Now we can solve the puzzle nearly directly without looking at millions of coordinates. For each sensor, find the lines parallel
and adjacent to its sides. If we find any duplicate lines, save them separately: those are the important lines we need moving
forward. Unique lines cannot contain the solution.

Once we have non-unique lines, we find all intersections between them that exist within the search space. There should be only
one intersection and this will be the answer.

My code and data actually show three intersections, but only one is the answer. This appears to be due to sensors having edges
that technically meet the requirements, but their search areas are not adjacent. These lines do extend for infinity and are not
treated as segments.

This was a huge undertaking that took quite a bit of refactoring, fixing bugs, finding optimizations, and rewriting most of the
code from scratch after finding a new breakthrough. I am happy that part two ran in around 9.5 seconds in the first version but
now runs in around 7 milliseconds after figuring out the solution is at the level of high school Algebra II and not some brute
force problem that takes gigabytes of memory and trillions of clock cycles.

## Day 16: Proboscidea Volcanium

[Year 2022, day 16][16.0]

This puzzle asks us to move through a cave, stopping in various rooms containing valves. Valves have a flow rate, and will
release pressure over time equal to their flow rate multiplied by the number of minutes remaining in the puzzle.

For part one we get thirty minutes to move around and release pressure. For part two we only get twenty-six minutes, but have a
helper elephant.

We start by parsing the input, and this has several steps. We are given valves, their flow rates, and connections to other rooms
with valves. We need to process this in multiple steps to translate it from the program input into a mapping between rooms. We
then build an array and run the [Floyd–Warshall algorithm][16.1] over it. Since it takes one minute to move between rooms, we can
simplify this slightly and quickly get the total cost of moving between any two rooms. We can simplify this graph by removing dead
nodes: if a valve is stuck, we will only ever move through that room, not to it or even from it. We only need to store the cost of
moving between valves with positive flow rate. The only exception is the first room, AA, is a valid source for a route.

After connecting the graph, the algorithm prunes dead nodes and changes their alphabetic names to numbers. Specifically, they are
all numbered in powers of two, with the initial room having the value zero. This means we can store the visited nodes as bits in
an integer, in this case, a `long` in order to support more than thirty-two nodes. This will be important later.

The two parts have slightly different algorithms. For part one we track the current node and the visited nodes, where visited
nodes use bitwise operations to store them in a single `long`. This algorithm performs an exhaustive depth-first traversal that
returns the maximum score of all the routes it checks. There is no memoization or caching of seen states, although it probably
should. Regardless, it runs very quickly.

Part two also needs the total cost, but there is a tradeoff. There are two players and they start at the same location. There are
several ways to model this, but naive implementations can easily spend minutes or even hours running. I opted for running as if
there was only a single player. Store the visited nodes and the final score.

Once that is done, compare all results against all other results. If the two sets of visited nodes are disjoint, then that is a
valid two-player route. Add the two scores and store that result. This is extremely quick because there are only a few thousand
unique results, and the comparison uses bitwise operations which is far, far faster than comparing lists or arrays of integers.
It only requires a single `&` comparison: and remember, these bitwise operations are small components of larger and more complex
adders and multipliers inside modern CPUs. Some programmers are intimidated by bitwise operations, probably because most of us
have seen brain-melting chains of them in obfuscated C code. However, they are very fast and much better alternatives to more
programmer-friendly data structures when the data can actually be modeled in a single `int` or `long`.

The other optimization for part two is to convert the keys of the map of all paths to `long[]` array. Since the two player
states are interchangeable, we do not need to compare path A to B and later compare B to A. By converting to an array and using
the iteration structure whose name I cannot remember, we turn the iteration from a square into a triangle which cuts the
iteration time in half. Furthermore, we also cut map lookups in half. While `HashMap` has very fast lookups, arrays are even
faster. This alone cut the execution time by two-thirds.

The final key to part two is it may be best for a player _not_ to visit a node even if doing so maximize's that player's
contribution to the final score. Avoiding a specific node for a slightly suboptimal route may provide a far greater benefit to the
other player's route, increasing their combined score by more than the loss. In essence, avoid a strictly greedy algorithm when
calculating routes for part two because it may prevent the algorithm from finding the best combined route.

## Day 17: Pyroclastic Flow

[Year 2022, day 17][17.0]

We need to simulate blocks falling in a tall vertical cave that follow rules vaguely similar to Tetris except they do not rotate
and do not remove rows. How tall will the block structure be? Parts one and two differ only in the number of blocks dropped, and
the same solution works for both. However, we need to find a shortcut for part two or it will take years to run.

This program simulates rows in the tower using integers and bitwise arithmetic. Bits represent positions in the X dimension. It
also flips the rocks because the tower builds up from `y = 0`, but the rocks are specified with `y = 0` on top. Flipping the
rocks brings them into agreement with each other. Bitwise arithmetic makes finding collisions very easy to do in the source code,
and these operations are among the fastest that a modern desktop or laptop CPU can perform. Avoiding 2D arrays also helps with
cache usage and performance. An entire row can fit within seven bits.

Once the simulation works, we need to find repeated patterns. The block pattern will repeat after some time. If we see a state
where the same block is about to fall as before and the wind pattern is at the same index in the input, then that _might_ be
where the edge of the cycle lies. We also need to compare where the block can land, which means we need to look at the top of the
tower and compare that as well. The previous block in the cycle could have landed in a different location, or even the block
before that one could have. A skinny block might fall past the previous one and end up somewhere under it.

This means when we cache patterns we need to use the current block, index into the wind pattern, and some _N_ number of rows at
the top of the tower as a cache key. What _N_ needs to be varies, based on my own input and what I read in the reddit solution
megathread. With my input I get the correct answer with a tiny lookback, but other people needed 50-100 rows.

Anyway, once we find the cycle, we do some math to figure out the cycle length and the amount of height gained in that cycle. We
then figure out how many full cycles can fit in the remaining number of iterations. Advance the primary loop counter by the cycle
size multiplied by the cycle count. Calculate the height that would add by the cycle count multiplied by the height of the cycle,
and store that in a variable for later use.

Once that is complete, resume stacking blocks to get the final cycle portion. Think of it like we start partway through one
cycle, start and complete a cycle, then skip many more which brings us to within under one cycle from the top. Finish that last
partial cycle and get the height. Then add in the skipped height we calculated before, and we have the answer.

## Day 18: Boiling Boulders

[Year 2022, day 18][18.0]

Today's puzzle is a fairly simple one. We need to calculate the surface area of what is essentially a voxel defined by points in
three-dimensional space.

There probably are faster ways to do this, but I accomplished it by iterating all of the given points. For each point, iterate
its neighbors. If the given points do not contain the neighbor, then we increment the surface area by one. This is sufficient for
part one, which wants the whole surface area.

Part two wants to know the exterior surface area: the given points, both test and real data, contain interior voids and we want
to exclude that area. Given the points, we find the minimum and maximum values in the X, Y, and Z dimensions and add or subtract
one from each to get a bounding box slightly larger than the voxel. Then we pick a corner and flood fill that space. This ensures
a proper flood fill regardless of the shape of the object.

Next, we scan the volume and get all points that are not in the set of the original points or the flood filled points. These are
interior voids. We rerun the surface area calculation using this new set of points to get the interior surface area. Since the
total surface area is the sum of internal and external surface areas, we get the exterior surface area by subtracting the
internal surface area from the total surface area.

## Day 19: Not Enough Minerals

[Year 2022, day 19][19.0]

Similar to day 16, we need to simulate deploying resource-collecting robots that gather resources. Based on the order, they can
ramp up more quickly and produce more of the final resource. There are several ways to approach this problem, but they are all
brute force with some clever pruning of the search space.

I modeled this problem as series of robot purchases, not time steps. At each problem state the algorithm loops through all of the
four options and answers several questions about the robot. If the answers indicate that yes, we should build the robot, then we
advance the clock to the point where its construction is complete.

1. Is it possible to acquire resources to build the robot in the first place? If we only have ore robots, do not consider
   obsidian or geode robots because we cannot gather the necessary resources... yet.

1. Do we need more of them? If, for example, we have three ore robots but nothing costs more than three ore, we do not need more
   ore robots. We can only create one robot per minute, and they produce the maximum amount needed each minute. There are two
   exceptions. First, if ore robots cost three ore but everything else costs two ore, we stop at two: there is no point building
   that extra ore robot. This is a unique case because the ore robot is the only one that requires its own resource to build. The
   other exception is geode robots: we _always_ want as many of them as possible.

1. If we build this non-geode robot, will it result in a potential future robot build? This includes resources it will generate
   before time expires. If we can build an obsidian robot that will produce two obsidian before time expires and we have another
   two, then it does not make sense to build it if a geode robot requires six obsidian to build.

We repeat this for every robot and create the next state where the robot is built with resources and clock adjusted accordingly.
Furthermore, there is a cache of known states. If the state was already seen, do not repeat it.

The other change I made from the model described in the puzzle statement is there are no geode robots. Instead, I consider
building a geode robot as purchasing a number of geodes equal to the time limit minus the current time and adding that to a
special geode bucket in the state. I do this for two reasons.

1. Since the algorithm models robot purchases, not time steps, there is no guarantee the algorithm will end on the final time
   unit. With a time limit of 24 minutes, it could end on minute 22, leaving two minutes off.

1. The number of geodes a robot will harvest is deterministic based on the time remaining, and geodes are never used to build
   other robots. This means we can count them immediately.

Overall the algorithm runs slowly for part two, but with parallel streams, it is manageable. I know there are other ways to
optimize this to get it to run in under a second which I may explore later.

## Day 20: Grove Positioning System

[Year 2022, day 20][20.0]

Today is this year's "rearrange a circular list" puzzle. We are given a sequence of numbers and we need to shuffle it based on
the value of each element in the list: the element moves forward a number of elements equal to the element's value. If the value
is negative, then it moves backward. The twist this year is we process the elements in the original order in which they are
loaded from the input file.

At first I used a circular buffer backed by an array, but this proved to be extremely slow. I quickly switched to a doubly-linked
list with an additional array storing all of the nodes in their original order. Since the answer is calculated relative to the
node with value zero, I also store a reference to this node for later use. This has several advantages.

First, it is trivial to iterate the array and always get the nodes in order. There is no math involved: simply iterate using the
enhanced `for` loop. By using a linked list, we do not need to shift elements in-place. The algorithm removes an element from its
original location, iterates the necessary number of elements, and inserts it. All other elements stay where they are.

There are two more details worth mentioning. First, all iteration is done in one direction: forward. Backwards movement adds
complexity to the algorithm that does not pay off with measurably decreased run time. Instead, it converts backwards movement to
forwards movement which works because the list is circular. Then the movement logic can work without any conditionals.

Second, it uses modulo arithmetic to avoid looping over the list multiple times. If the list is seven elements long and we need
to move an element sixteen times, it would loop twice and accomplish nothing useful before finally finding the element's proper
new location. It is worth noting here that when taking the modulus of the list size, we need to subtract one from the modulo
argument because the original item is temporarily removed from the list: it cannot jump over itself.

Part two adds two new requirements. First, we need to multiply each value in the list by a constant, and it is large enough to
bring the element values into the range of a 64-bit `long`. The modulo operation is crucial here to avoid needlessly circling the
list trillions of times. The second is we mix the list ten times. I am not aware of a way to speed this up. There may be a way to
find patterns or to use linear algebra to condense this operation. I feel that if that is true, the work required at run time to
figure that out would probably not save any time. Part two runs in about one-third of a second for me which is good enough.

## Day 21: Monkey Math

[Year 2022, day 21][21.0]

Today's puzzle is an exercise in parsing a mathematical expression (part one) or equation (part two).

We start by parsing the input. This is a multiple-stage process. The input is given as a list of monkeys and their inputs. They
either have a number, or they get numbers from two other monkeys and perform a mathematical operation on them. To turn this into
a proper binary tree we need to parse the data and get it into a useful data structure, then construct a binary tree from the
leaves up to the root.

Nodes have three types. `ParentNodes` hold an operation and references to two children. `LeafNodes` hold a `long`. The
`HumanNode`, used in part two, represents the variable for which we need to solve.

Nodes can evaluate their effective value, where they either return their constant value or ask their children for their values
then perform an operation on those values. This is sufficient to get the answer for part one.

Part two asks us to solve an equation. However, the `HumanNode`, the variable, cannot evaluate itself. Furthermore, the root node
no longer has an operation and instead represents equality, or the `=` sign.

We start by evaluating the side that does _not_ contain the variable. We track this as a simple `long` instead of a `Node`
object. Then we simplify the other side, evaluating all subtrees that are able to be evaluated. This is not strictly necessary,
but aids with debugging and visualizing the sequence of operations. It also ensures that the `HumanNode` is at the maximum depth
of the tree.

Next, we manually process each node on the variable's side. One of its child nodes is guaranteed to be a `LeafNode`. We invert
its operation and apply it to the `long` side of the equation. For example, let us assume we have the following, where `x`
represents the subtree containing the variable and not necessarily the variable itself:

    30 = x * 5

We invert the multiplication to division and divide the left side by the constant while removing the multiplication entirely.
This gives us a new `long` value and eliminates the right subtree of the variable's side:

    6 = x

Another example:

    4 = x - 5

Like before, we invert and add five to the left, dropping the subtraction from the right. What is the variable is on the right,
like this:

    6 = 2 + x

Since addition is associative, we know that `2 + x` is equivalent to `x + 2`. We can simply switch the subtrees and perform
subtraction with the value of two.

The part that is slightly tricky is if the variable is on the right _and_ the operation is not associative? Remember, we are
dealing with assignment statements, not mathematical equations. `5 - x` is not equivalent to `5 + (-x)` in this context because
our parse tree has no concept of negation. What do we do with this?

    4 = 5 - x

We cannot drop the right tree as before. Instead, we need to think about how to update the `long` value and what to chop off of
the parse tree. Let us restate the problem as below. What do we assign to `A` such that `A = x` is a correct statement?

    A = B - x
    (A - B) = -x
    (B - A) = x

Note that we do _not_ invert the subtraction here: instead we invert the order of the constants. That way we do not flip a sign
and end up with the wrong answer. This works for division, too:

    A = B / x
    (A / B) = 1 / x
    (B / A) = x

In both of the above cases, we apply `operation(B, A)` instead of `operation(A, B)` and assign the result to `A`.

With that, we have the correct transformation rules to apply at each step. If `x` is in the left tree we simply apply the inverse
operation to `A` and the constant in the right tree. If `x` is in the right tree we do the same if the operation is associative,
except with the constant in the left tree. If `x` is in the right tree and the operation is not associate, we instead flip the
inputs to the operation.

We can evaluate each level of the parse tree until we are left with a single leaf node, the `HumanNode`. At this point the `long`
value contains the answer to part two.

## Day 22: Monkey Map

[Year 2022, day 22][22.0]

Today's puzzle asks us to traverse an odd shape using some simple rules, where the answer to the puzzle is based on the final
location and facing. The two parts differ in the rules for how to handle edges and wrapping around to other edges.

Part one is a simple wrap in Cartesian space: hit the right edge? Wrap around to the left edge and keep going in the same
direction. Part two says to fold the shape into a cube, but treat direction as if it were still flat.

The algorithm for traversing the shape is shared between both parts, but it accepts a mapping of transitions. Code for each part
maps out the edges and determines where to move should the player step over the edge. That way, the algorithm does not need to
differ and can be reused.

When mapping transitions, we need to know the edge points but also facing. There are corners in the shape where the same point
will map to different destinations depending on what direction we headed to reach that point. Furthermore, the facing relative to
the flat shape can change depending on the source. The mapping is then (point, facing) to (point, facing).

I hard-coded the transitions, although it does use loops to map the entirety of a single edge. There is likely a better way to do
this, but I am not familiar enough with the math required. It probably requires matrix transformations that I touched on briefly
in high school during the 20th century and my Computer Science education never bothered with in college. Maybe some day I will
figure out the general approach to mapping these routes around the cube but not today.

## Day 23: Unstable Diffusion

[Year 2022, day 23][23.0]

We get another puzzle about moving points around on a plane using arbitrary rules today. Elves are trying to spread out such that
they have zero neighbors in all eight surrounding map coordinates. Part one verifies the solution works correctly by running a
small number of movement iterations and calculating a score over the minimum bounding rectangle for the elves. Part two has us
run the algorithm until there are no changes in the elves' positions.

This algorithm works by running the two stages of each turn as specified in the instructions. First, we iterate all elves and
determine what move, if any, they will make. Store this in a map where the key is their destination, and the value is their
current location. If the map contains the key for the destination already, we can simply remove the key and neither elf will
move. This is safe to do because a particular destination cannot be the target of more than two elves.

Consider an empty square. There are only four ways an elf could move there: by occupying the square north, south, west, or east
of that square. Let us assume there are elves west and east of it. They can both pick that square. What if we added an elf to the
north? Then nobody will pick that square because it occupies one of the three squares the elves test before moving. Even if there
are elves west and north, but neither south nor east, they cannot pick that square. This means that this program logic is
consistent with the rules of the puzzle.

Once this is done, we have a map that contains all of the elf movements. If that map is empty we can return early from the method
that performs the turn logic, returning `true` to indicate nobody moved which is the end condition for part two and can simply be
ignored in part one.

If there are moves to make then we iterate the map's entries. Remove the value, which is where the elf started, and add the key,
which is where the elf moved. Then we return `false` to indicate this is not an end state for part two.

So far I have not found much to optimize. It runs in 800-850ms on reasonably fast hardware for 2022. Using hash sets and maps
runs considerably faster than any other Java collection that I tried. The code modifies the elf state in-place, rather than
creating entirely new state each turn. I initialize data structures with a capacity big enough to avoid resizing on the real
puzzle input. Each round runs in under one millisecond, but there are around 1,000 rounds depending on the input. (N.B. one of my
wrong answers elicited the message that it was correct for a different input so I have a ballpark estimate for the overall range
of answers).

Considering how simple the logic is, the lack of any clear bottlenecks, and no obvious shortcuts, this may simply be a matter of
there is a lot of data to crunch.

## Day 24: Blizzard Basin

[Year 2022, day 24][24.0]

This puzzle was not too bad for being late in the month. I spent more time writing the code to parse the input than I did solving
the actual puzzle.

When I read the puzzle I made a few observations that helped make the solution search easier. First, I knew I wanted to model
each minute's state as answering the question of "what locations are legal for the player to occupy on any given turn?" Second, I
observed that the board state would eventually loop back around and repeat. This is based on the dimensions of the board:
specifically, the time required to get back to the initial state is the least common multiple of the width and height _counting
legal positions the blizzards can occupy_. Specifically, this does _not_ include the boundary walls or the start and end
locations that only the player can occupy.

With this in mind, I calculate all board states up front while parsing input. The input logic allocates a three-dimensional
boolean array. The first dimension is time: number of minutes elapsed. The second and third are Y and X distance. Next, we need
to know if at a particular time there will be a blizzard at that location. Since blizzards move deterministically, this is
trivial. Iterate over all three array dimensions. For each location in the array, we can calculate the exact coordinates in the
program input that corresponds to each of the four directions blizzards can travel. We can then set the coordinate in the board
array as either open (true) or occupied (false). This requires only one iteration through the board array which is about as fast
as possible.

Precomputing all board states saves time later, and avoids constantly recalculating blizzard locations. It also makes the main
algorithm for evaluating states much simpler.

The algorithm for traversing the board is fairly simple. We start out by seeding the state queue with the initial state, then
process the head of the queue as long as there is something to process. During this processing, it tracks the current best state.
This has the lowest score. We need the actual state, not only the score, because part two asks us to traverse the board three
times.

1. If the current state has a score equal to or greater than the current best score, abandon this state. It is mathematically
   impossible for it to lead to a new best score.

1. If the current state is one away from the destination, the only move worth considering is moving to the destination. This is a
   success state. If it is better than the previous best or it is the first success, then we save the state.

1. Next, we consider waiting in place. If the current location is available during the next round, we create a new state and
   enqueue it.

1. Finally, we consider moving to all adjacent locations, including the one we came from. Moving backward may make sense since
   the board state constantly shifts.

Any time the algorithm considers enqueuing a new state, it performs two checks. First, it filters out states it already processed
and that are stored in a cache. Second, it calculates the theoretical minimum score that state could produce. This is its current
score plus the Manhattan distance to the destination. If that score is worse than the current best, abandon it.

The key to any search like this is to abandon states that cannot possibly produce useful results. By pruning one such state
early, that could lead to thousands of later garbage states never being processed in the first place.

## Day 25: Full of Hot Air

[Year 2022, day 25][25.0]

The final puzzle this year has a slight twist. We need to convert numbers in base 5 to either base 2 or base 10, whatever the JRE
understands. Sum them up, and convert back to this odd version of base 5. At least, that is what the problem statement suggests.

This implementation does not bother converting inputs between base 5 and base 10. It performs all math using mostly base 5 with
some division and modulo to keep a base 5 representation in a standard integer. More on this later.

The algorithm streams the input numbers and reduces the stream by using an "add" operation until there is one element left, which
it returns. The add operation takes two strings and iterates them each from the unit position backward, adding each digit.

To add the digits, we need to look at a few different scenarios. Are there digits available in each string? Or did one string end
already because it is shorter? What if there is a carry from the previous operation? What if both strings are consumed and we
only have the final carry digit?

In general we use whatever string data is available, defaulting to "0" if a string ran out of digits. We then calculate the
addition operation on those two digits plus the carry digit. This operation converts each digit to a value between -2 and 2,
inclusive. We then add all three values. If the value is larger than 2 or smaller than -2, add or subtract five to bring it back
within range and set the carry to -1 or 1. It is worth noting that the carry can never be greater than 1 or smaller than -1. This
is convenient because it means we can never need an extra carry to a higher digit level. Worst case we get -5 or 5 from the
addition of three numbers, which has -0 or 10 as a result. That carry digit feeds into the next operation but keeps the magnitude
in check. This is similar to how in base 10, you can add 9+9+9 and get 27: digit of 7, carry 2. In base 2, 1+1+1 is 11. We can
never carry past the second position by adding three of the maximum value in the one's position.

We add each group of digits in turn, with the result prepending to a StringBuilder. If there is a carry at the end with no other
digits, we prepend it as-is. Then the StringBuilder contains the result of the addition.

Converting between the Snafu representation and an integer is as simple as using a string with the Snafu symbols in order of
small to large. Get the index of the Snafu character and subtract two to get its integer value. Add two to the value and get the
character at that index to convert back to the Snafu character.

[1.0]: https://adventofcode.com/2022/day/1
[2.0]: https://adventofcode.com/2022/day/2
[3.0]: https://adventofcode.com/2022/day/3
[4.0]: https://adventofcode.com/2022/day/4
[5.0]: https://adventofcode.com/2022/day/5
[6.0]: https://adventofcode.com/2022/day/6
[7.0]: https://adventofcode.com/2022/day/7
[8.0]: https://adventofcode.com/2022/day/8
[9.0]: https://adventofcode.com/2022/day/9
[10.0]: https://adventofcode.com/2022/day/10
[11.0]: https://adventofcode.com/2022/day/11
[12.0]: https://adventofcode.com/2022/day/12
[12.1]: https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
[13.0]: https://adventofcode.com/2022/day/13
[13.1]: https://mvnrepository.com/artifact/org.json/json
[14.0]: https://adventofcode.com/2022/day/14
[15.0]: https://adventofcode.com/2022/day/15
[16.0]: https://adventofcode.com/2022/day/16
[16.1]: https://en.wikipedia.org/wiki/Floyd%E2%80%93Warshall_algorithm
[17.0]: https://adventofcode.com/2022/day/17
[18.0]: https://adventofcode.com/2022/day/18
[19.0]: https://adventofcode.com/2022/day/19
[20.0]: https://adventofcode.com/2022/day/20
[21.0]: https://adventofcode.com/2022/day/21
[22.0]: https://adventofcode.com/2022/day/22
[23.0]: https://adventofcode.com/2022/day/23
[24.0]: https://adventofcode.com/2022/day/24
[25.0]: https://adventofcode.com/2022/day/25
