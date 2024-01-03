# Year 2017: Deus ex Machina

## Day 1: Inverse Captcha

[Year 2017, day 1][1.0]

This problem asks us to add up a checksum for an integer array following rules: add up numbers when they are equal to the next
number (part 1) or the opposite number (part 2) thinking of it as a cyclic buffer.

This is a fairly simple and self-explanatory array iteration.

## Day 2: Corruption Checksum

[Year 2017, day 2][2.0]

This problem asks us to perform calculations on arrays of integers (rows in a spread sheet). For part one, we need to calculate
the difference between the maximum and minimum value in each row, and add up these differences for all rows. For part two, we need
to find the two values in each row where one value evenly divides the other, and add up the quotients for each row.

This is a simple brute force algorithm. For part one, let the streams library do the heavy lifting. For part two, divide each
unique pair of values and see which pair divides evenly. To simplify the logic, sort first.

## Day 3: Spiral Memory

[Year 2017, day 3][3.0]

This problem concerns itself with spiral squares. Part 1 asks us for a Manhattan distance of the `Nth` element, where `N` is the
puzzle input. Part 2 asks for the first value greater than N when storing the sums rather than the cell sequence numbers.

Part one is the well-defined sequence [A214526][3.1]. The answer can be calculated directly using the formula in the OEIS.

Part two is another sequence, [A141481][3.2]. However, there is no known formula to calculate the answer directly. Even the OEIS
has a formula that iterates instead of calculating directly. The simplest solution is to fill in a matrix and stop once the
current value is greater than the input.

## Day 4: High-Entropy Passphrases

[Year 2017, day 4][4.0]

This problem asks us to perform string comparisons among each line of words. Part one asks us to count how many lines have all
unique words. Part two asks us to count lines where no word is an anagram of another word.

Part one is trivial with Java collections. Put each list of strings (words) into a set, then see if its size matches the list used
to construct it. Part two requires us to dig into the letters in the words. The simplest way I found to solve this was to sort the
characters in each string, since two anagrams will produce the same sorted string. However, Java does not make this easy with its
stream API. There is no built-in character stream, and no trivial way to convert an IntStream into a string, interpreting each
integer as a code point. I went with a tiny bit more verbose solution.

## Day 5: A Maze of Twisty Trampolines, All Alike

[Year 2017, day 5][5.0]

This problem asks us to evaluate a series of jumps, as in an assembly program. However, there are rules about how the jumps change
each time they are evaluated. In part one, a jump increments by one each time after it is evaluated. In part two, the same rule
applies, unless the jump value is three or more: then it decreases. In both cases, we need to count how many jumps it takes to
escape the program.

This is a trivial simulation where we simply track an instruction pointer and the number of jumps, and increment until the pointer
is invalid.

## Day 6: Memory Reallocation

[Year 2017, day 6][6.0]

This problem asks us to crunch an integer array using an algorithm repeatedly, until the array is in a state seen before. Part one
and the first half of part two ask us to process the array until any state is seen again. The second half of part two asks us to
repeat the algorithm until that duplicated state is seen again. In both cases, return the number of iterations.

This solution uses a simple integer array along with a wrapper class which makes it trivial to compare two arrays both inside of a
set as well as in regular code. There are two calculate methods that differ slightly because the exit conditions are different,
but they leverage a common method for the distribution code. Part two has an additional step on top of what part one does.

## Day 7: Recursive Circus

[Year 2017, day 7][7.0]

This is a problem about balancing trees. Part one asks for the root node, which verifies we construct the tree correctly. Part two
asks for us to find a node in the tree that is unbalanced, and find its correct weight to balance it.

Building the tree is simple. Figure out the root node, then recursively build its children and add them. For finding the
unbalanced node, we assume that a node must have three children to be unbalanced because one has to be different from the others
and this must be unambiguous. This works for the program input and its assumptions. From here we move around the tree looking for
the node that is unbalanced, but its children are balanced. That means that node itself must have the wrong weight. Then we
compare to its siblings and do a little math to figure out its correct weight. We get the different which is the correct weight
based on its siblings, minus the weight of the current node. We add this to the weight of the current node only, which is the
current node's total weight minus the weight of its children. The current node's weight cancels out, giving a simplified
expression.

## Day 8: I Heard You Like Registers

[Year 2017, day 8][8.0]

This problem simulates a simple instruction set as in a CPU. It iterates through all of the instructions, one after another,
performing conditional arithmetic. We either need to know the maximum value in any register at the end of execution (part one) or
at any point during execution (part two).

This is fairly simple. Encode the logic in an enumeration and some values for register names and integers, then go through and
evaluate the conditionals and possibly add a value to a register. Even having an indeterminate number of registers is trivial,
since they are stored in a map and we can simply add a new register before using it if the current instruction references one that
does not exist.

## Day 9: Stream Processing

[Year 2017, day 9][9.0]

This problem requires us to examine an input stream of characters and perform analysis on it. The stream contains garbage with
well-defined rules. Part one asks us to remove the garbage, then analyze what remains to ensure we removed it correctly. Part two
asks us to count the amount of garbage removed.

While parsing this string would theoretically require a pushdown automata because it describes a context-free grammar, we do not
actually need to parse it using a grammar. All we need to do is assume it is well-formed and calculate some statistics from it. A
single pass through the string is sufficient. Track whether the previous character was an escape, and whether we are in a garbage
block. Then track the depth of the braces, a running score total, and the amount of garbage skipped. Then simply return two
integers for the score (part one) and garbage count (part two).

## Day 10: Knot Hash

[Year 2017, day 10][10.0]

This problem asks us to implement a hash function. Part one implements a single round. Part two repeats the single round while
also modifying the input and processing the output.

There are no special tricks here. This solution is a little different than others because the two parts only reuse the code for
calculating a single round. The input and output are different.

The hashing code is in a separate class because it is reused. See KnotHash for more information.

## Day 11: Hex Ed

[Year 2017, day 11][11.0]

This problem is about calculating distances on a hexagonal grid. Given a set of steps, part one asks how far from origin we end
up. Part two asks the farthest distance at any point while traveling.

The crux of this problem is the hexagonal grid. Knowing how to move around and calculate distances is a little tricky at first,
but there is a [very useful web site][11.1] that I have used a few times for various coding exercises that explains how to do
this. Specifically, using axial coordinates allows us to use simple Cartesian formulas to calculate distances. The short version
is in order to handle left and right, we need to skew the horizontal axis along either the NW/SE or NE/SW direction. Once we do
that, the math is trivial.

## Day 12: Digital Plumber

[Year 2017, day 12][12.0]

This is an exercise in graph traversal. Given a set of nodes and mapping between the nodes, part one asks us how many nodes are
reachable from node 0: that is, how many nodes are in its group. Part two asks us how many discrete groups of nodes there are.

The first task is isolating a group. Rather than building an actual graph, we can simply look up each node, one at a time, and see
where it leads, starting with an arbitrary starting node. This is a breadth-first traversal where we omit paths to nodes already
visited. As we visit nodes, add them to a set. Then this group is isolated. For part one we can simply get the group for node zero
and return its size. For part two, get an arbitrary node from the mapping and find its group. Save the group into a collection of
groups used solely to be able to get the number of groups. Then remove everything in the group's set from the node mapping. Repeat
until all mappings are exhausted. We are left with a collection of discrete groups, which we can get its size for the puzzle
answer.

This algorithm only works reliably if the input describes a bidirectional graph. However, this appears to be the case for the
provided input.

## Day 13: Packet Scanners

[Year 2017, day 13][13.0]

At a given point in time, is an oscillating scanner at position zero? Part one verifies the calculation is correct by asking for a
weight of all scanners where this occurs, while part two asks us to find the first time offset where we can pass by all scanners
without being caught.

This is a remainder problem. Each scanner has a period, and at the start of each period, it will catch any packet going through.
However, this is flipped from how these problems usually go: instead of trying to find a value where the remainders are all zero,
we need to fine a value where none of them are zero. The solution works, but optimizing is problematic. The Chinese Remainder
Theorem suggests techniques to optimize a program when we need remainders to be zero. In this case, however, we need the
remainders to be anything except zero. The only low-hanging fruit is that the time must be an even value for the specific inputs
provided. Other than that, we could optimize by checking if the cumulative forbidden intervals block off all but two of a given
scanner (inner loop in part two), and the current time value is invalid. Then we know the other unchecked value is valid, and can
increment by the interval of that scanner, skipping other values. However, tracking this was a bit tricky so I decided to leave it
as-is. It runs fairly quickly as it is, so I think clarity of code is more important than esoteric optimizations that buy little
improvement.

## Day 14: Disk Defragmentation

[Year 2017, day 14][14.0]

This problem asks us to analyze knot hashes, reused from day 10. Part one is a sanity check that the code is reused correctly, by
counting the bits set to true. Part two asks us to group bits based on them being adjacent in a matrix.

Part one is trivial: iterate the integers in the hash and use Java's built-in function to count the bits. Part two iterates over
all bits in the grid. Once we find a bit set to true, erase that group by setting its bits to false, then keep iterating. To erase
a group, perform a breadth-first search of all neighboring bits set to true, and set them to false.

## Day 15: Dueling Generators

[Year 2017, day 15][15.0]

This problem asks us to run two pseudo-random number generators that use different seeds and factors, and compare the values they
produce to see when the lower 16 bits match. Part two adds a constraint that some values produced need to be skipped.

This is a brute-force algorithm. Run the PRNGs over and over and check the results. I was not able to find any way to speed this
up. Not even optimizing out the modulo operator using bit tricks made any difference. I opted to keep the code simple since
nothing more complex made a dent in the runtime which is an order of magnitude greater than any problems in 2017 so far. My theory
is that between the JVM and CPU, something sees what is going on and has built-in optimizations that code changes cannot beat.

## Day 16: Permutation Promenade

[Year 2017, day 16][16.0]

This is another array scrambling problem where we need to apply transformations to an array then get its final state. The
difference between parts one and two is the number of times we apply the transformations.

The number of iterations in part two is simply too large for a purely brute force algorithm to work. Instead, use memoization and
look for cycles. Once we identify the cycle length, simply take the modulus of the total number of iterations to the iteration
count so far and we have the offset of the answer in the cache.

## Day 17: Spinlock

[Year 2017, day 17][17.0]

This problem is about a spinlock which is a circular buffer. We insert values into the buffer, and want to know which value is
inserted at a given step.

Part one is trivial to use brute force. Part two, not quite. However, there is an interesting optimization. We really only need to
track where element zero is, and what number is inserted after it. Other than that we really do not care what values we insert or
where we insert them. Iterate over the values, and if we insert after zero, that is our new answer. Otherwise, keep going. Do not
bother storing the whole buffer since it simply does not matter. This saves a lot of time and space complexity and allows the
algorithm to complete in a very short amount of time (slower than most 2017 problems, but still quick).

## Day 18: Duet

[Year 2017, day 18][18.0]

This problem is an exercise in a assembly language, but with a twist. Part one is a regular assembly problem seen many times in
Advent of Code. However, part two changes it by adding a second program that runs in parallel, with both programs communicating
with each other.

There are two approaches that make sense here. First, single-threaded. Run one program until it blocks on input, then switch to
the other program until it also blocks. Then switch back to the first program, and repeat until both programs are blocked. Note
that all program inputs do result in deadlock, confirmed by the puzzle's authors. The second approach is an actual threaded
solution, which ended up being simpler. Using thread primitives introduced in Java 5, it is trivial to sit and wait for both
threads to complete. With some carefully-written Callable tasks, we can guarantee that even in a "deadlock" scenario, the threads
are able to detect this and quit. Given how simple and fast the code is to execute assembly, setting a low timeout for polling
input ensures that a thread does not wait too long before giving up and killing itself. However, it is always possible that this
might not work correctly on a slower system than the one I use.

## Day 19: A Series of Tubes

[Year 2017, day 19][19.0]

This problem asks us to navigate an ASCII art graph that is really one long, snaking route. Part one asks us which letters we see
along the route, part two asks for the total number of steps taken.

The input is well-formed and even has a space buffer around it, making navigating around edges easy. Move around the graph, and
find the one valid path each time we turn. Count the steps taken and track the letters seen along the way, and return the answer
as appropriate for each part.

## Day 20: Particle Swarm

[Year 2017, day 20][20.0]

This problem asks us to perform vector arithmetic. Given a collection of particles, calculate their positions based on an initial
position, velocity, and acceleration. Part one asks us to find the particle that accelerates away from the origin the slowest,
being the closest particle in the long term. Part two introduces collisions, and asks us how many particles are present after all
possible collisions occur.

The simulation is straightforward, except for knowing when to end it. Trial and error shows how many iterations it takes until the
answer stabilizes.

## Day 21: Fractal Art

[Year 2017, day 21][21.0]

This problem asks us to transform a grid using rules, with the two parts varying only by the number of iterations.

There are a couple of optimizations here which also simplify the solution a bit. First, we use a wrapper around a boolean 2D
array. This simplifies array access a bit compared to 3D or 4D arrays. It also allows overriding hashCode() and equals() so it can
be used as a key in a HashMap. Next, we can front-load the matrix operations to the time of loading input, which means we only
need to perform them once per transformation. Then the main loop does not need to modify any inputs, it can simply compare them.

Performance is still a little slow for part two, but further optimizations would require identifying patterns and skipping
iterations. It appears this is theoretically possible, but not worth the effort.

## Day 22: Sporifica Virus

[Year 2017, day 22][22.0]

This problem asks us to model a grid where each cell can have one of four states. Rules define state transitions, and we must
iterate a given number of times and report how many of a specific state transition occur. The two parts differ in the number of
iterations and the complexity of the state transitions.

The implementation is fairly simple. Define enumerations for the states and directions. Construct a grid to traverse around, and
implement state transitions in those enumerations. For each iteration, perform the steps described in the problem statement. At
first, I opted for a Map of coordinates which were simple two-int objects. This makes it trivial to operate on grids of arbitrary
size, as the problem says the grid is infinite. However, this introduced a ton of overhead. I used observation to see that the
virus only ever travels 208 units away from origin in any direction, so I constructed a grid capable of holding any coordinate the
virus will visit and got rid of the coordinate class. This sped up the algorithm by four times due to avoiding a ton of operations
involving maps, and using primitives instead. The initialization logic is slightly more complex, but it is an acceptable tradeoff.

## Day 23: Coprocessor Conflagration

[Year 2017, day 23][23.0]

This is another assembly code simulator, similar to day 18. The instructions are a bit simpler, however. Part one asks how many
times the virtual machine executes a multiply instruction, while part two flips an input bit and asks for the value of a register
when the program completes. The caveat here is the program is extremely inefficient and will not finish in any reasonable amount
of time.

The key to part two is figuring out the algorithm and coming up with a better version. Manipulating it by hand, I translated it
into pseudocode. From here I simplified variables. For example, register `g` is only ever used to do math to compare expressions
to zero in a JNZ instruction. This means `g` can be elided into expressions used in conditional statements. Next, I found that
registers `b` and `c` were used as lower and upper bounds for a loop. As I simplified and restructured, these variables actually
survived to the final iteration and initialize with values pulled directly from the program input.

From here, I worked from inner to outer loops to determine that the algorithm is a very simple primality check. It actually counts
composite numbers between `b` and `c` but does so in the most naive way possible: dividing by every single number between two and
the number. Zero optimizations. This is why the program will not complete in a reasonable amount of time. Instead, sieve enough
prime numbers for the algorithm and perform a binary search on each number to test its primality. Testing thousands of numbers in
part two takes more time than testing a few tens of numbers in part one using the less efficient algorithm, even with the
(admittedly, low) overhead of the sieve.

## Day 24: Electromagnetic Moat

[Year 2017, day 24][24.0]

This is problem about combining pairs of integers like dominoes, where they form a chain and a pair can only be added to the end
if one of its values matches the last value on the chain. Part one wants the highest score (sum of integers) possible, while part
two asks for the highest score among all chains with the longest length.

This is a basic depth-first search, with some optimizations added so it runs in a reasonable amount of time. The key is reducing
the number of chains tracked from the over 800,000 possibilities to a much smaller subset. This algorithm reduces time spent using
several micro-optimizations. First, allocate a single result set and size it appropriately to begin with. This avoids resizing
hash tables repeatedly. Next, pass a single result set around and add to it directly instead of returning results at each step.
This avoids a ton of unnecessary set operations. Finally, do not bother storing chains that are nowhere near long enough to be an
answer to either part of the problem. This avoids even more set operations and makes it faster to iterate when finding the result
after enumerating the chains of integers.

## Day 25: The Halting Problem

[Year 2017, day 25][25.0]

The final puzzle for 2017 is an implementation of Turing machine. We need to run it for a given number of iterations then count
the number of true values on the tape. Note that this is not a real Turning machine: as the instructions state, that is a
theoretical model. Specifically, this implementation lacks an infinite tape and we know it will halt since it runs a finite number
of iterations.

This is straightforward, and the bulk of the effort is in parsing the input, not running the simulation.

[1.0]: https://adventofcode.com/2017/day/1
[2.0]: https://adventofcode.com/2017/day/2
[3.0]: https://adventofcode.com/2017/day/3
[3.1]: https://oeis.org/A214526
[3.2]: https://oeis.org/A141481
[4.0]: https://adventofcode.com/2017/day/4
[5.0]: https://adventofcode.com/2017/day/5
[6.0]: https://adventofcode.com/2017/day/6
[7.0]: https://adventofcode.com/2017/day/7
[8.0]: https://adventofcode.com/2017/day/8
[9.0]: https://adventofcode.com/2017/day/9
[10.0]: https://adventofcode.com/2017/day/10
[11.0]: https://adventofcode.com/2017/day/11
[11.1]: https://www.redblobgames.com/grids/hexagons/
[12.0]: https://adventofcode.com/2017/day/12
[13.0]: https://adventofcode.com/2017/day/13
[14.0]: https://adventofcode.com/2017/day/14
[15.0]: https://adventofcode.com/2017/day/15
[16.0]: https://adventofcode.com/2017/day/16
[17.0]: https://adventofcode.com/2017/day/17
[18.0]: https://adventofcode.com/2017/day/18
[19.0]: https://adventofcode.com/2017/day/19
[20.0]: https://adventofcode.com/2017/day/20
[21.0]: https://adventofcode.com/2017/day/21
[22.0]: https://adventofcode.com/2017/day/22
[23.0]: https://adventofcode.com/2017/day/23
[24.0]: https://adventofcode.com/2017/day/24
[25.0]: https://adventofcode.com/2017/day/25
