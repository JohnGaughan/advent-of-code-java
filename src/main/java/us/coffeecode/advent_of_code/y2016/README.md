# Year 2016: Santa v. Easter Bunny

## Day 1: No Time for a Taxicab

[Year 2016, day 1][1.0]

This problem asks us to traverse through a Point2D space and find distances to two locations.

This is a fairly simply problem with a simple solution. For part one, just navigate until there are no more instructions, then
find the distance to origin. For part two, we need to track each intermediate step along the way and stop as soon as we encounter
a location twice. This means iterating through each step instead of short cutting to the end of a move.

## Day 2: Bathroom Security

[Year 2016, day 2][2.0]

This problem is about finding a sequence of keys to press on a keypad based on starting at "5" and moving your finger up, down,
left or right before pressing another key. However, some keys may be at the edge of the keypad, and a particular move might not be
allowed so it should be ignored. The two parts have keypads of different sizes and layouts, with part two having non-numeric keys.

The two parts use the exact same solution, except with different data. The input data is the same, but they use a different keypad
mapping. This works by feeding in an array of strings that consist of a starting key, direction, and destination key. Then go
through the program input and use the mapping to move around the keypad, saving each key press after iterating each input line.

## Day 3: Squares With Three Sides

[Year 2016, day 3][3.0]

This problem asks us to evaluate triangles. Based on the lengths of their sides, which ones are possible in Euclidean geometry?

The solution is fairly straightforward. Given a grid of numbers, read them into a 2D array. Next, for part two only, reorganize
the numbers since they are given in a different order. Then sort each row which represents the side lengths of a triangle.
Finally, check that the sum of the two short sides is greater than the length of the third (longest) side.

## Day 4: Security Through Obscurity

[Year 2016, day 4][4.0]

This has two different parts. The first is figuring out which inputs are valid. This requires counting letter frequency and
comparing two checksums to see if they are equal. For part one, sum up the sector IDs of valid inputs. For part two, "decrypt" the
input with a Caesar cipher where the rotation is the sector ID.

This is mostly menial low-level input formatting and handling and there is not much interesting to say about it.

## Day 5: How About a Nice Game of Chess?

[Year 2016, day 5][5.0]

Similar to 2015 day 4, this is a brute force MD5 exercise.

The worst part of this problem is how long it takes, even on fast hardware. I added in some skips so unit tests run faster, but it
produces correct results even without them.

## Day 6: Signals and Noise

[Year 2016, day 6][6.0]

This problem asks us to perform frequency analysis on letters across many strings, then find the most or least frequent character
in each position. Concatenate them all to get the result string.

This is a fairly simple exercise. First, stuff the data into an array representing frequency. The first ordinal is the position in
the string, and the second is the letter. The value in each cell is how many times it appears. Once this is done, all we need to
do is iterate and keep track of which letter is most - or least - frequent in that position, then concatenate it onto a
`StringBuilder`. Then simply return the string.

## Day 7: Internet Protocol Version 7

[Year 2016, day 7][7.0]

This problem requires parsing strings and finding which ones have regions that meet certain criteria (part one) or that match
other regions in certain ways (part two).

There may be ways to solve this that are more complicated but also more efficient. The code is pretty clear so I will go with the
simpler approach. There is a lot of iterating and looking for patterns, but no iterating multiple times so that is good. The only
other design decision worth noting is the input parsing. The method I opted for can technically fail in the general case, but not
for the specific case of the provided data. Brackets never begin or end a line, and never occur consecutively. This makes a simple
regex split practical and makes this implementation very concise.

## Day 8: Two-Factor Authentication

[Year 2016, day 8][8.0]

This problem asks us to run a simulation on a bitmap, using commands to shift values around. Once done, we need to extract data
from it. For part one, we need to know how many bits are set: this is a sanity check to know the simulation ran correctly. Then in
part two, we need to parse letters based on the bits set to true. This is similar to a simple digital LED display.

The simulation is fairly straightforward, but the parsing is a little rough to do without knowing the font used. I printed out the
bitmap and used those letters, then looked at other solutions with other inputs to get more of the alphabet. Since the letters are
5x6, therefore 30 bits, this fits in an integer. I convert each segment into an integer, and match them up based on precomputed
values. This matches up up with known letters, so the system can convert bitmap chunks to letters without machine learning or
other advanced topics outside the scope of Advent of Code.

## Day 9: Explosives in Cyberspace

[Year 2016, day 9][9.0]

This problem requires calculating a string length, where the string length in part two is not feasible to calculate by expanding
the string and counting its length.

The two parts work nearly the same, except for part two, it uses recursion to expand substrings. In both cases, the algorithm only
calculates how long a substring would be if it were expanded.

## Day 10: Balance Bots

[Year 2016, day 10][10.0]

This is a directed graph problem. Bots are internal nodes. Inputs are nodes with edges moving away, into bots. Outputs are the
opposite: nodes with a single edge moving into them. Part one asks us to find the internal node with two specific inputs, and part
two asks us to multiply the values of three outputs.

This was an interesting problem compounded by the fact that an earlier version parsed the input file _mostly_ correct. I used a
regular expression approach that did not always produce correct results. Part one got my confidence up with the correct answer,
but part two kept throwing exceptions. Tearing out and replacing the input parsing fixed it right away. One pattern I noticed here
is the graph has no cycles. Therefore, each rule applies exactly once. This means we can prune rules as we go along, and never
look at them again.

## Day 11: Radioisotope Thermoelectric Generators

[Year 2016, day 11][11.0]

This problem requires moving objects between floors in a way that incompatible items are not left together. Parts one and two use
the same algorithm, but part two has more data.

At first, this seems like a good candidate for A\*. The search space is potentially massive, so a true search algorithm would need
to use caching to avoid redundancy as well as heuristics to avoid searching parts of the search space that cannot ever produce the
correct result. A\* lends itself well to this.

However, the problem statement and input data have some constraints and patterns which make this even simpler. The user has to
travel between one floor at a time and always must have an item to allow the elevator to work. The best solution candidates will
always move two items up and one item down if possible. Nothing else can be more efficient than that. The initial layout of items
must be a legal one, as well. Some inspection of the data indicates that we really do not even need to consider the RTG and
microchip constraints, either. This problem can be simplified. Starting with floor one: move two items up. Then if there are still
items on the floor below: move one item down, two up. Repeat until the lower floor is empty. Repeat that until all but the top
floors are empty. As it turns out if there are `n` items on a floor, it takes `2n - 3` moves to move them up a floor. Then repeat,
using the newly embiggened number of items, snowballing until the problem is complete. In other words, this is an algebra problem,
not an A\* problem.

## Day 12: Leonardo's Monorail

[Year 2016, day 12][12.0]

This is a simple virtual machine running a scaled down assembly language. We need to run a program and return the result in a
register. The only difference between the two parts is the starting state.

This is a fairly simple problem with a simple solution. I do not like passing around Object and checking its type, but it is the
simplest of several alternatives I considered.

## Day 13: A Maze of Twisty Little Cubicles

[Year 2016, day 13][13.0]

This puzzle is all about traversing a maze (graph) where the walls are generated from an algorithm using coordinates as inputs.
For part 1 we need to know the shortest path to a specific coordinate, and for part 2, the number of distinct points within a
given traversal distance.

The overall traversal algorithm is the same between parts. Traverse the graph a certain number of steps, keeping track of the
shortest distance to each node. This algorithm is a breadth-first algorithm because it is simpler, avoiding recursion, and because
there may be multiple ways to reach a given node. Breadth-first makes it easier to avoid duplicating work. In the end we either
get the path length corresponding to the desired node, or simply the number of nodes, as appropriate for each part.

## Day 14: One-Time Pad

[Year 2016, day 14][14.0]

This problem asks us to calculate a ton of MD5 hashes and find patterns in the results. Specifically, a hash with a run of three
hex digits where there is another hash with a run of the same digit except five times. Part two adds 2016 "hashing the hash"
operations.

The algorithm is not ideal but it is simple. The big problem is the MD5 hashing: it is slow, because there is so much hashing to
perform. For this reason I split the MD5 operations across multiple threads and kept the simple search algorithm instead of
something clever, but that wouldn't save any practical time.

## Day 15: Timing is Everything

[Year 2016, day 15][15.0]

This is a Chinese remainder theorem problem similar to year 2020, day 13. Find a number that lines up with coprime repeating
cycles.

The key here is that the number of positions on each disc is coprime to all the other discs. The algorithm would be similar
without that, but this way is simpler. For each disc, figure out the remainder (time offset). Then add a time increment repeatedly
until the remainder matches. The interval starts out at one. After processing each disc, multiply the increment by the positions
for the disc we just finished. This guarantees that when we calculate future discs, we do not misalign previous ones.

## Day 16: Dragon Checksum

[Year 2016, day 16][16.0]

This problem asks us to generate a string, then process that string by reducing it according to a rule.

On modern hardware, this is achievable using brute force. However, there are faster ways to do this. First, a naive approach to
string generation would be to double it iteratively until there are enough digits. However, this involves a lot of redundant
string copying. Constructing the string in-place using a StringBuilder chops off about 20% of the time. Similar to the generation
approach, reduction can take a shortcut. Effectively, each digit in the answer represents one partition of the dragon string. We
know how big each partition is by computing the integer log base 2 of the length of the dragon string. The "checksum" algorithm is
really just calculating parity: each digit is the parity of one partition of the dragon string. Doing this instead of the naive
"reduce one step at a time" approach cuts execution time by around 65%.

There are further reductions in complexity. For example: we can calculate any digit of the dragon string directly since it does
not rely on any other digits, only its position in the string. However, the current implementation executes fairly quickly after
making the only other optimization that really matters: working on a boolean array instead of a string. Instead of a string of
ones and zeros, boolean arrays are much faster. However, the answer must still be a string because part two has a leading zero.
Returning a boolean array or an integer formatted as a binary string will not produce the correct answer.

## Day 17: Two Steps Forward

[Year 2016, day 17][17.0]

This problem asks us to search a maze to find a path from one corner to another using special rules to calculate legal moves.

This is a breadth-first search. For part one, end searching as soon as we find a solution. It must be the shortest path. Part two
wants the longest path. In theory this could be effectively infinite, but in practice, many paths are dead ends. Keep searching
until exhausting all paths.

## Day 18: Like a Rogue

[Year 2016, day 18][18.0]

This problem asks us to translate a string repeatedly using a set of rules, and count certain characters in it. The two parts only
differ based on how many iterations there are.

The simple approach is to perform string operations. This worked but was too slow for my preference. Instead, operate on boolean
arrays. Further, we can do all of the calculations in one pass without translating (and storing!) all of the rows. The final
result is an optimized, but readable, algorithm that performs well. Another simplification is that the transformation rules are
more complicated than needed. The tile directly above the current tile does not matter. All that matters are the diagonal tiles,
and there is only one diagonal for the end tiles. This probably will not affect runtime much, but it simplifies the logic a little
bit.

## Day 19: An Elephant Named Joseph

[Year 2016, day 19][19.0]

This day's problem asks us to calculate the value of one of two sequences. For part one, this is the [Josephus Problem][19.1]
whose sequence is specified in the [Online Encyclopedia of Integer Sequences][19.2]. Part two is a modified sequence.

Part one was trivially easy: simply code the OEIS sequence. This is a `O(log(n))` solution because each recursive call divides `n`
in half. Part two was a little more tricky. A brute-force solution using arrays or lists works, but is slow. However, it does
enable us to see the pattern.

As it turns out, the pattern is based on powers of three. Consider an input `n`. Take the logarithm base 3 of `n`, and round down:
this is the integral log base 3 of `n`. If that log is `n`, then `n` is a power of three and the answer is `n`. Otherwise, let us
consider how the sequence changes. As n increases by one, `b(n)` increases by one until `n` is greater than twice the integral
log. Then `b(n)` increases by two each time `n` increases by one.

All together, this means we can take the integral log of `n` base 3 and partition the solution space into three. Equal to the
power of three, equal to or less than twice that power of three, or greater than twice the power. For each partition we can
directly calculate `b(n)` since the function follows consistent rules.

## Day 20: Firewall Rules

[Year 2016, day 20][20.0]

This problem asks us to perform some analysis  on ranges of numbers. Part one asks for the first valid number not in any range,
while part two requests the total number of integers within an overall range not included in any of the range rules.

The solution is fairly simple. Read in the input and construct Range objects. Store them in a sorted data structure so ordering is
already done for us. Next, merge. This is simple: take the first range, and keep merging it with the next range until we find a
range that cannot be merged. For part one, we do this once, then we can get the value one greater than the upper bound on the
first range (or zero, although the input has a range starting at zero). For part two, keep doing this and storing the results in
another set. This ends up containing all merged ranges where there is no overlap. Then we can simply iterate those ranges and
subtract their size from the total address space, 2^32.

## Day 21: Scrambled Letters and Hash

[Year 2016, day 21][21.0]

This is a problem about scrambling text in a non-destructive way. Part one runs a bunch of transformations on an input string.
Part two has us start with the output work the rules backward to get the input.

Running forward was not too difficult, although some of the indexing was a little tricky. Running backward was also not too bad.
Some rules are strictly reflexive and need no modifications. Others need some simple changes to the input, or calling other rules
(for example, rotating left when rotating right in reverse). The only moderately tricky rule was rotating based on the character
in a given position where its position prior to rotating changes the amount rotated. To solve this I sketched out a matrix of
where a given letter will be in the output for each position in the input. Thankfully, there was a unique mapping. Since there is
no simple formula to calculate this, I saved the mapping in an array and the reverse logic looks up the value needed to reverse
the original rotation.

## Day 22: Grid Computing

[Year 2016, day 22][22.0]

This problem asks us to evaluate a grid of nodes. These nodes represent file servers containing data. Part one is a sanity check
on reading and interpreting the data. Part two requires us to move data around in the grid following specific rules until the
required data traverses from one node to another. This is similar to a maze, except we may move data that we do not need in order
to make room: this is similar to being able to move maze walls following a set of rules.

Part one is trivial and there is not anything important to note about it. Part two requires some human analysis to arrive at
anything resembling an efficient solution. The grid is has three types of nodes. First is an empty node. Next is a node with so
much data it cannot possibly fit anywhere else. Last is a "regular" node whose data can fit on any other node that is not
oversize. Looking at it this way, the grid is similar to a larger version of a [15 puzzle][22.1] with an immovable wall. This type
of puzzle is a bunch of squares with one square missing. You need to shuffle the pieces around, one at a time, to get the pieces
in the correct locations. In this case, we only care about one piece of data being in the correct location.

Visualizing the grid, there is a horizontal wall of immovable data near the bottom. We need to move the empty node left far enough
to clear the wall, then all the way up to the top row. Next we need to move it all the way to the top right corner, which also
moves the target data one cell left. From here we perform a series of five-move shuffles to move the data one cell to the left
each time. We repeat this for as many cells as we need to move it left. The algorithm simply calculates these moves from the input
data without actually rearranging anything.

## Day 23: Safe Cracking

[Year 2016, day 23][23.0]

This problem uses the Assembunny code from day 12, but with a new instruction. We need to run a program and get the results.

The two parts are basically the same, except the execution time for part 2 is a lot longer. The key here is to identify what the
code is doing, and make an optimization. The short version is the code takes the factorial of the input, then adds the product of
two numbers hidden in the source code. Calculating a factorial using repeated addition is extremely slow, and even slower when the
only addition is incrementing by one. The expected optimization is to implement a multiply opcode and change the source
appropriately. I found the block that does the alleged multiplication (repeated incrementing based on register contents) and
patched it to use real multiplication instead. I padded with no-ops to ensure it is unnecessary to modify jumps elsewhere in the
program.

## Day 24: Air Duct Spelunking

[Year 2016, day 24][24.0]

This is a maze traversal problem. The maze contains several numbered points of interest, and we must visit them all in the least
amount of steps. The two parts differ in whether we must return to the starting position or not.

There are a two main steps here. First is calculate the distance between each pair of points. Second is iterate the permutations
of visit orders and see which route has the lowest cost. Calculating distances is a breadth-first search that has an optimization
where the algorithm first closes off dead ends. This is a simple linear operation that looks at each tile in the maze and walls it
off if it has three or four adjacent walls. This is fast and reduces the search time in the next step. I use a library method to
calculate permutations, then look up the route costs, total them up, and keep track of the minimum-cost route.

## Day 25: Clock Signal

[Year 2016, day 25][25.0]

The final challenge for this year further modifies Assembunny and asks us to find an input that produces a repeating output.

This was fairly simple. I added the output logic which just stores the generated outputs in the state. Once there is an arbitrary
amount of output, compare it against the expected output. While the problem says the output must repeat infinitely, this is tricky
to prove. At first I looked for a cycle where once we got to the second zero, the program state was identical to last time. That
did not work as expected. Instead, I moved to capturing ten array elements and seeing if it repeats over that interval. This
worked as expected.

[1.0]: https://adventofcode.com/2016/day/1
[2.0]: https://adventofcode.com/2016/day/2
[3.0]: https://adventofcode.com/2016/day/3
[4.0]: https://adventofcode.com/2016/day/4
[5.0]: https://adventofcode.com/2016/day/5
[6.0]: https://adventofcode.com/2016/day/6
[7.0]: https://adventofcode.com/2016/day/7
[8.0]: https://adventofcode.com/2016/day/8
[9.0]: https://adventofcode.com/2016/day/9
[10.0]: https://adventofcode.com/2016/day/10
[11.0]: https://adventofcode.com/2016/day/11
[12.0]: https://adventofcode.com/2016/day/12
[13.0]: https://adventofcode.com/2016/day/13
[14.0]: https://adventofcode.com/2016/day/14
[15.0]: https://adventofcode.com/2016/day/15
[16.0]: https://adventofcode.com/2016/day/16
[17.0]: https://adventofcode.com/2016/day/17
[18.0]: https://adventofcode.com/2016/day/18
[19.0]: https://adventofcode.com/2016/day/19
[19.1]: https://en.wikipedia.org/wiki/Josephus_problem
[19.2]: https://oeis.org/A006257
[20.0]: https://adventofcode.com/2016/day/20
[21.0]: https://adventofcode.com/2016/day/21
[22.0]: https://adventofcode.com/2016/day/22
[22.1]: https://en.wikipedia.org/wiki/15_puzzle
[23.0]: https://adventofcode.com/2016/day/23
[24.0]: https://adventofcode.com/2016/day/24
[25.0]: https://adventofcode.com/2016/day/25
