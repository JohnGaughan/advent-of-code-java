# Year 2018: Backwards Through Time

## Day 1: Chronal Calibration

[Year 2018, day 1][1.0]

Today's puzzle has us process a list of integers. Part one asks for its sum. Part two has us repeatedly process the list, tracking
the sum along the way, until we encounter a duplicate sum.

Part one has a trivial solution using Java's stream operations. Part two calculates over 129,000 values until it finds a repeat.
This takes a trivial amount of time on the given input. I am aware of ways to calculate this that are faster using advanced math.
When JUnit reports 0ms as the run time, I would rather move on to the next day.

## Day 2: Inventory Management System

[Year 2018, day 2][2.0]

This problem asks us to evaluate some strings. For part one, count how many strings have a character repeated exactly twice
anywhere in the string, and how many have a character repeated exactly three times. Then multiply these counts together. For part
two, we need to find the pair of strings that differ by a single character, and return the string that is those strings minus the
different character.

Part one is some simple iteration and counting. Part two is a variation on the [Hamming distance][2.1] algorithm. We need two
strings with a Hamming distance of one. The method to check two strings tracks the index of the character that is different. If
there is a second different character, return null to indicate this is not a solution. Otherwise, once done comparing the strings,
get two substrings on either side of that index, concatenate, and return them.

## Day 3: No Matter How You Slice It

[Year 2018, day 3][3.0]

This puzzle relates to overlapping rectangles. For part one, we need to see how many coordinates have multiple rectangles that
contain them. For part two, we need to find the single rectangle that does not overlap with any others.

There are a few approaches to this problem. For part one, the simplest is to run the simulation and count the squares. The only
optimization I added here is to have a single group of loops, and to count collisions as they occur instead of looping over the
entire grid after marking each claim.

For part two, I use the standard "do rectangles overlap" boolean expression inside of nested loops. Unfortunately it is not
possible to constrict the inner loop or to remove rectangles as they collide, as this produces incorrect results due to messing up
transitive properties. If rectangles `A` and `B` overlap, and `B` and `C` overlap, we cannot prune `B` because after checking `A`,
we still need it when checking `C`. Every method I tried to remove rectangles from the search space broke it. This still runs in
less than 1ms, so the naive approach is fine here.

## Day 4: Repose Record

[Year 2018, day 4][4.0]

This problem presents a quantity of log entries describing different guard IDs and when they sleep on the job. We need to parse
the data, measure metrics, and multiply two numbers together based on the contents of that data.

Most of the work is in parsing the input into a usable format. From there we just need to search through the guards' data for
values matching whatever metrics are applicable to the part of the problem.

## Day 5: Alchemical Reduction

[Year 2018, day 5][5.0]

Today's puzzle asks us to reduce a string using the rule that two adjacent characters with different case are removed from the
string. Part one asks us to reduce the input string, while part two asks us to remove one character (both cases) first and see
what the shortest string is that can be generated.

The reduction code is fairly straightforward, removing all compatible characters using one pass through the input. This works by
using a stack and constantly removing anything that can be removed at each step. For part one, we stop here. For part two, we save
this reduced state and work off of it 26 times, once for each letter of the alphabet. Attempt to reduce further using the "ignore"
variant of the reduce() method, which skips over characters in the input. Since part one reduces the string size by roughly 80%,
that saves a bunch of redundant steps through the input on subsequence iterations.

## Day 6: Chronal Coordinates

[Year 2018, day 6][6.0]

This problem has us create regions on a map where regions are those locations closest to each point on the map. Part one asks for
the largest region that is not infinite in size, while part two asks for the region with a total Manhattan distance to all points
under 10,000.

Part one was actually more involved this time. The algorithm iterates over a grid, saving the closest point in each array
location. If there is a tie, store -1 which means no single point is closest. Then figure out which regions are infinite in size:
these are the ones that touch the outside border, since after a while no other points can ever be encroach on that point's region.
Finally, count the number of elements in each region, ignoring those we identified as infinite. Take the largest region and that
is the answer.

Part two is simpler. It does not require storing any grid, actually. Simply iterate over a 2D space - using iterator variables
only - and calculate the total Manhattan distance. If that total is under 10,000, increment the counter for the size of the
region. When complete, we have the number of squares in the region which is the answer.

## Day 7: The Sum of Its Parts

[Year 2018, day 7][7.0]

This is a problem about directed graphs. There are a number of steps to complete and they must be completed in a certain order.
Part one asks for the completion order assuming one person working on the tasks, while part two asks for the completion order with
five workers and a variable amount of time required for each task.

I chose to model this is a digraph where edges point toward the start of the graph. This makes it easy to see which nodes have
dependencies, since they contain their own dependencies instead of needing to search other nodes. At any given stage I can simply
search for nodes with empty dependencies then choose a node. I also used one algorithm for both parts, even though part one is
simpler. There is less duplication this way.

Keep track of what tasks each worker is performing, as well as the completion time. Each tick, see if any nodes are completed. If
so, add them to the output and idle its worker. Next, update the nodes that are available to work. This would ideally use a
priority set if such a thing existed in the Java libraries. Instead, I use a TreeSet which is fine for the tiny amount of data and
has both properties I need. Once that is done, start work if any workers are available and there is anything to work on.

## Day 8: Memory Maneuver

[Year 2018, day 8][8.0]

Today's puzzle involves parsing a tree and extracting values from it. Each node has metadata: part one asks for the sum of all of
the metadata. Part two asks for a slightly more complicated value that varies based on the number of children of each node.

This is a fairly simple solution. First, parse the input. The bulk of this work is done in the Node constructor. Start by using
the header data to initialize the metadata and child node arrays, then construct each child. Once this is done, we have the offset
to the metadata, so copy that in.

Part one is implemented in the sumAllMetadata() method. For each node, add its metadata to the sum of its children's metadata,
recursively. Part two calls the value() method, which returns results based on whether or not it has children. Since nodes can be
referenced multiple times, it also caches the results of its calculation for a minor speed improvement.

## Day 9: Marble Mania

[Year 2018, day 9][9.0]

This problem asks us to simulate a marble game and find a score. Elves take turns inserting marbles into a circle following
certain rules, and we need to know the highest score when the game is complete. Parts one and two differ only in the number of
marbles.

My first implementation used an array sized for the number of marbles, as a type of optimized circular buffer. The problem was
that part two absolutely destroys data structures that are not efficient. I switched to a doubly and circular linked list instead.
This ended up taking less memory and several orders of magnitude less time. Using a custom linked list implementation was overall
better than reusing Java's LinkedList class because I can embed a primitive value directly in the class, removing boxing and
unboxing; I can make it linked in a circle; and access is consistent since there are no ends to wrap around.

## Day 10: The Stars Align

[Year 2018, day 10][10.0]

This puzzle defines a group of points scattered all over, and asks us to figure out the message they form when they move together.
Part one asks for the message, part two asks for the amount of time it takes to form the message.

This is a simple simulation. I use heuristics to determine when the points converge to their tightest grouping: there is no reason
this must be when they form a message, but it turned out to be that point. Rather than hard-coding the iterations, the code
determines when the points diverge and stops there. I also noted that the points move very slowly, so I took a short cut and
started at the closest order of magnitude to the answer to save some time.

The other other task is to convert points into strings. This is similar to year 2016, day 8. Characters are 6 wide by 10 tall, so
60 bits. This fits into a long integer, so the code translates portions of the text into integers which it then maps to
characters. From there it is trivial to construct a string.

## Day 11: Chronal Charge

[Year 2018, day 11][11.0]

This problem asks us to calculate values and store them in a grid, then find the square in the grid with the highest sum. Part one
uses a fixed size square with length 3, while in part two the length varies.

The naive approach is to store the value of each grid element in the grid, then continuously calculate sums and compare. This
works okay for part one, but is very inefficient for part two. Instead, a [summed-area table][11.1] is a much better data
structure. Since origin is the upper left of the table, start in the opposite corner and use a table size one row and column too
large. Each square is equal to its computed value plus the square to the right and below, minus the square to the diagonal lower
right. Each square contains the sum of itself and all squares to the right and below: the part we subtract is an overlapped area,
so we need to subtract it once after adding it twice.

Now when we want to know the sum of an area of the table, we start with the upper-left square. Take its value and subtract the
square to the right just past the square we want, and the same with the square below. Then add in the square to the lower right
just past the area we want, since it was subtracted twice. This gives us the sum of the area with three arithmetic operations.
This is extremely efficient and allows this program to complete in a very small amount of time.

## Day 12: Subterranean Sustainability

[Year 2018, day 12][12.0]

Similar to Conway's game of life except in one dimension, today's puzzle has us run a simulation that alters which nodes are
living and dead. We then calculate a value based on which positions are alive. The two parts differ only in the number of
iterations.

This is easy enough to model, but fifty billion iterations is not feasible to simulate in any reasonable amount of time. Clearly
there has to be a short cut. As it turns out, there is. We only care about the score at any given point, not the actual
configuration. After a little over 100 iterations, the score stabilizes and never changes. This algorithm detects that situation
and stops, using a direct calculation to get the answer since we know how many iterations remain and what their score is.

## Day 13: Mine Cart Madness

[Year 2018, day 13][13.0]

Today's puzzle presents a maze with carts moving along tracks. Carts will eventually collide. Part one asks for the location of
the first collision, while part two asks for the location of the last cart remaining the instant after the final collision.

There are a few ways to approach this, and I took the simple one. Parse the input, and separate carts from the raw data and track
them separately. Use a priority queue to track carts, so they are sorted and processed in the correct order. Each tick, drain the
queue and process each cart. Each cart goes into a processed" collection, which we add back into the queue at the end of the tick.
If there is a collision then the current cart is not added to the processed collection, and all other carts in either collection
are removed.

For part one, this is where it ends. Return the coordinates for the collision. For part two, keep going until there is only one
cart left. If the queue ever has a single element at the end of a tick, this is the success condition: get the remaining cart and
return its coordinates. Interestingly, my input has seventeen carts and there is never a three-way or four-way collision. I
expected this to be an edge condition the puzzle authors would throw in there to break assumptions. The only collisions in my data
were two carts.

## Day 14: Chocolate Charts

[Year 2018, day 14][14.0]

This is another circular buffer question where we need to generate numbers in the buffer, then process them somehow. Part one asks
us to generate enough of the sequence to get the values at the provided offset from the start, while part two asks us to find the
location of a number in the sequence. The same input value is interpreted differently in the two parts.

Day 9 was a good use of a circular linked list, but this time, the performance was too poor. Instead, I opted for an integer
array. Fill up the array which needs to be sized large enough to have the necessary data, then process it. For part one we
generate the sequence then add up numbers at the provided offset. For part two we search through the array for the search needle,
stopping when found. Using an array instead of a circular linked list reduced the execution time by an order of magnitude.

## Day 15: Beverage Bandits

[Year 2018, day 15][15.0]

The problem asks us to simulate a game where elves and goblins attack each other using very specific rules. Part one asks for the
result of the game expressed as the total hit points of the surviving force multiplied by the number of rounds. Part two asks us
to find the lowest bonus to give to the elves to guarantee they win.

The algorithm has a lot going on, but the bulk of its complexity lies in path finding and deciding which step to make. First, we
need to enumerate valid paths until finding ones that lead to an enemy. This is done using a breadth-first search similar to
Djikstra's algorithm. There is some basic optimization going on, such as not allowing a path to backtrack or cross itself. At
every iteration, look for paths that end at the same square: if so, they are close enough to equivalent that we can prune all
except one. In this case we group them based on their ending square, then pick one that starts with the best starting square which
is rated based on reading order. This minimizes redundant paths.

Each path tracks its own visited points. A naive implementation (that is, an earlier version of the code I wrote) would simply
count the points in the path itself as visited. A more advanced optimization that reduces the running time by approximately 80%
combines the visited points of the chosen path along with the visited points of paths pruned out in favor of the chosen path. This
avoids backtracking or crossing not only the chosen path, but any other path deemed equivalent to it at some point. This holds
because crossing over any of those paths _cannot_ be a shortest path to an enemy. This can greatly reduce the search space for
each path.

## Day 16: Chronal Classification

[Year 2018, day 16][16.0]

This is another virtual instruction set. Part one is a test of parsing the input and handling it correctly. Part two builds on
this by further processing the input then running a program.

The bulk of the logic is in two places. First, the test method is responsible for taking a sample and testing each of the opcodes
against that data to see if it can produce the same result. There is a flag that alters the behavior for parts one and two. In
part one we simply count how many opcodes can produce that result. In part two, we assign the opcode its numeric value if it is
the only opcode that can produce that result. Only in part two, we also skip opcodes that are already known so we can, on future
passes through the sample data, further narrow down and assign codes.

The opcode class stores the codes and holds an array where the index is the opcode's ID, 0-15. This is where we assign opcodes to
numeric codes. The static method can tell us if the opcodes are all assigned. In part two we loop over the sample data until this
condition is true, then execute the program and return the answer.

## Day 17: Reservoir Research

[Year 2018, day 17][17.0]

This puzzle asks us to simulate what is essentially pouring water into a series of cups, then calculating how much water they
hold. Part one wants the total water, including water at rest inside cups as well as water flowing over and around them: part two
wants only the water at rest.

This was an interesting problem with some surprisingly tricky edge cases to get the water flow correct. Water fills up, requiring
moving up as the puzzle progresses. Water can spill over one or both sides of a cup, sometimes into the same cup, sometimes into
different cups. The approach I settled on that seems to work is to use a stack: this means backtracking up a cup while it fills is
a built-in feature that needs no special handling. Filling one square at a time was far too error prone. This solution alternates
between two modes. First, water flows down until it hits something. This is a trivial case. Second, water fills a row of a cup. We
need to know if this row will spill over the edge: if so, fill with water in motion which needs to fill one past the edge: add
that hanging bit of water to the stack so it can flow down. Otherwise, fill with water at rest.

Boundaries were not too bad to get right. We need a buffer of two grid columns on either side to ensure there is a dead space that
the algorithm can use. I also shrank the input horizontally for more convenient visualization. Finally, the program specification
requires that we only count water between the bounds of the first and last Y coordinates of program input, inclusive.

## Day 18: Settlers of The North Pole

[Year 2018, day 18][18.0]

This puzzle is a variation of Conway's Game of Life, where a grid mutates itself based on adjacency rules. Part one asks us to
iterate ten times then calculate a score based on the grid state. Part two asks the same, except with one billion iterations.

This is a common theme in Advent of Code: do a thing and prove it works, then do the same thing a ridiculously large number of
times that is not computationally feasible. The underlying computations are trivially easy, but part two adds in a requirement to
look for repetition. In this case there is a lead-in of just over four hundred calculations before the sequence settles in to a
loop. The calculation code needs to look for a duplicate grid state, then figure out not only the period of repetition but also
where in that period the current state is. I use a linked hash map for this purpose. The key is the hash code of the grid state,
with the value being its score. Once we calculate all possible values, all that is left is figuring out the parameters of the
repetition then advancing through the linked map, which iterates in insertion order, to retrieve the previously-calculated value.

## Day 19: Go With The Flow

[Year 2018, day 19][19.0]

Today's puzzle asks us to run a program and get the results in one of the registers after it is complete. Parts one and two differ
by one input, which is a flag that controls an internal control constant. Part one returns quickly, part two runs for a very long
time.

The key to part two is figuring out what the algorithm in the program does, and instead calculate it in a more efficient manner.
Personally, I do not like these types of problems where it is not feasible to automate it completely: instead, we essentially need
to rewrite the input in a different format. Anyway, the program sums up the factors of a number it calculates. However, the
factorization is performed in a very inefficient manner: nested loops.

In order to make this program more general such that it actually uses the input rather than hard-coding an answer, it runs the
program for a short while to extract the number being factored. It then generates the necessary prime factors using a sieve, the
gets the prime factorization of that number. From here we can use combinations of the prime factors to get the distinct factors.
Then sum those factors.

## Day 20: A Regular Map

[Year 2018, day 20][20.0]

This is regular expression puzzle where the regular expression describes a maze. We need to find the longest paths through the
maze. Part one wants the length of the longest path, part two asks for the number of paths of at least 1,000 length.

Essentially, we need to run the regular expression in reverse: instead of matching a string, we need to generate strings. There
are two options here. First is actually generating strings, but there are a _lot_ of strings. Second is to build the maze, which
can be done in linear time by iterating through the expression. That is the approach I take here.

The idea is to track the state using a mapping of points in the grid to their walking distance from origin, and a stack of points.
If we encounter a left parenthesis, we put the current location on the stack so we can return to it later when we encounter a
right parenthesis. If we see a pipe, peek at the top of the stack to return to where we were when we last pushed a point on the
stack: since the pipe is an "or" condition, we need to reset and try again. Otherwise, we encountered NSEW, so update the current
location and increment the distance by one. Then, if this is a new location, add the point and new distance to the path mapping.
Otherwise, get the minimum of the new distance and the existing distance and store that. This should handle overlapping or
crossing paths, although the answer is the same with and without that logic for the input provided.

## Day 21: Chronal Conversion

[Year 2018, day 21][21.0]

This is another assembly exercise where part one runs in a reasonable amount of time, but part two does not without optimization.

This was not a fun puzzle to solve. I ended up rewriting the algorithm in Java instead of executing it in a makeshift VM written
in Java as in the previous two exercises. This allowed for some opportunities to simplify and clarify the code a bit. The program
still reads in the assembly and extracts the magic number that differs between inputs so it should work for all inputs. There is
also an optimization to remove a lot of repetition by drastically reducing the upper bound of each iteration in the `c` register.
I am still not sure what this algorithm does, but simply iterating on simplifying the code and looking for patterns seems to have
worked.

## Day 22: Mode Maze

[Year 2018, day 22][22.0]

Today's puzzle creates a procedurally-generated maze where there are no walls: instead, each square has a type, and traversing
each square has a cost. We need to find the lowest-cost route to the destination.

Part one is simple: it merely requires us to generate the map then calculate a checksum to verify it is correct. However, it is
easy to get wrong: the checksum only covers a rectangle from origin (upper left) to the target (lower right), and does not
validate outside that range. However, part two involves traversing outside that range into territory that is not validated by part
one. I actually had a subtle error I needed to correct that nothing validated: the geologic index of the target location is always
zero.

Part two is a little tricky. I used a priority queue that values movement cost first, distance to the target second. This
essentially performs an A\* search, with cost and distance as heuristics to select the most promising path. The issue, as is often
the case with part twos, is running time of the algorithm. I implemented a cache that used the current location as the key, and
the time spent to get there as the value. Before adding any paths to the queue, check if its location is in the cache. If it is in
the cache and its time is worse, do not add it. Otherwise, add it. This was not sufficient. I had to create a custom map key class
that is the location and the currently-equipped tool. The reason is being at a given location with two different tools equipped
but with the same time to get there means something different. Each state could move to a new location but have a different time
to do so because the tool may or may not be compatible between the adjacent locations. After making this change, the algorithm is
still slow, but runs under a second.

## Day 23: Experimental Emergency Teleportation

[Year 2018, day 23][23.0]

This puzzle is about points in 3D space. There are nanobots that have 3D coordinates and a radius. Part one asks for the nanobot
with the greatest range: how many other nanobots are within its range. Part two asks for the coordinate that is in range of the
most nanobots. If multiple locations are tied, we pick the one with the shortest Manhattan distance to origin.

Part one is pretty easy: set up a class to contain the data and perform measurements. Then invoke stream operations on the
nanobots to get the strongest one, then filter the nanobots to get only those within range (including itself).

Part two is a bit trickier and I am still not quite happy with this solution. The [Bron-Kerbosch algorithm][23.1] was made for
this, but proved to be far too slow for this data set. To get the correct answer I wrote a similar algorithm that was able to exit
quickly, but it was still slow. However, it ran in seconds, not hours. Eventually I realized that the first nanobot in the input
was part of the winning clique, so I iterate the input once and build it as I go instead of any backtracking or recursive
approach. Finally, we need the shortest Manhattan distance from any point in the nanobots' overlap to origin. To do this we
compare the Manhattan distance minus the radius for each bot, and take the largest value. Consider a simple case in 1D space. One
point is at 4, radius 3. Another point is at 10, radius 20. The overlapping points are 1 to 7. The closest is 1: the maximal value
of distance minus radius, in this case, 4 minus 3. If we extend this to three dimensions, the same math holds. Adding more bots is
irrelevant: we simply compare more, taking the maximal value.

## Day 24: Immune System Simulator 20XX

[Year 2018, day 24][24.0]

This puzzle is about simulated combat between a reindeer's immune system and an infection. Part one validates the results of a
single simulation. Part two modifies the immune system's power, finding the lowest boost that allows it to win.

The only slightly tricky part about this puzzle is getting the various selection conditions correct in the targeting method.
Between proper sorting and pruning targets that cannot be valid there are a few ways to get this wrong, but in a way that only
breaks part two.

## Day 25: Four-Dimensional Adventure

[Year 2018, day 25][25.0]

The final puzzle of this year requires grouping points into disjoint sets, then counting the number of sets.

Given a set of points, group them based on Manhattan distance. Creating groups is easy, with the minor caveat that groups may need
to be merged later on if a point is close enough to both of them. Overall, the final puzzle follows the tradition of being an easy
one.

[1.0]: https://adventofcode.com/2018/day/1
[2.0]: https://adventofcode.com/2018/day/2
[2.1]: https://en.wikipedia.org/wiki/Hamming_distance
[3.0]: https://adventofcode.com/2018/day/3
[4.0]: https://adventofcode.com/2018/day/4
[5.0]: https://adventofcode.com/2018/day/5
[6.0]: https://adventofcode.com/2018/day/6
[7.0]: https://adventofcode.com/2018/day/7
[8.0]: https://adventofcode.com/2018/day/8
[9.0]: https://adventofcode.com/2018/day/9
[10.0]: https://adventofcode.com/2018/day/10
[11.0]: https://adventofcode.com/2018/day/11
[11.1]: https://en.wikipedia.org/wiki/Summed-area_table
[12.0]: https://adventofcode.com/2018/day/12
[13.0]: https://adventofcode.com/2018/day/13
[14.0]: https://adventofcode.com/2018/day/14
[15.0]: https://adventofcode.com/2018/day/15
[16.0]: https://adventofcode.com/2018/day/16
[17.0]: https://adventofcode.com/2018/day/17
[18.0]: https://adventofcode.com/2018/day/18
[19.0]: https://adventofcode.com/2018/day/19
[20.0]: https://adventofcode.com/2018/day/20
[21.0]: https://adventofcode.com/2018/day/21
[22.0]: https://adventofcode.com/2018/day/22
[23.0]: https://adventofcode.com/2018/day/23
[23.1]: https://en.wikipedia.org/wiki/Bron%E2%80%93Kerbosch_algorithm
[24.0]: https://adventofcode.com/2018/day/24
[25.0]: https://adventofcode.com/2018/day/25
