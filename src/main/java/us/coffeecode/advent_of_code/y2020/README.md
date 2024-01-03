# Year 2020: Tropical Vacation

## Day 1: Report Repair

[Year 2020, day 1][1.0]

This problem asks for the product of numbers that sum to 2020. Part one asks for the product of two numbers that sum to 2020, and
part two asks for the product of three numbers.

Simple problem with a simple solution. Start with sorted input, then iterate over the input numbers and brute force it. I added
some short-circuits to avoid numbers that we know cannot be solutions.

## Day 2: Password Philosophy

[Year 2020, day 2][2.0]

This problem requires simple string parsing and pattern matching. There are two numbers and a character, along with a string. The
goal is to count the number lines that meet some criteria. Part one wants to count the lines where the number of times the
character appears is between the two provided numbers. Part two wants to count the lines where the given character appears at
exactly one of two locations in the string, offsets given by those numbers.

My solution was to create the Input class which contains each parsed input line, which makes it simpler to reference values in
lambdas. Filter the lines with lambdas that validate the "passwords," and count what is left over.

## Day 3: Toboggan Trajectory

[Year 2020, day 3][3.0]

This problem involves a slope of a hill. Starting in the upper left, how many trees would a person encounter based on the angle
they take down the hill? The hill repeats horizontally, but not vertically. Part one asks for the number of tree for a specific
angle, while part two asks for the product of several counts of trees for multiple angles.

Most of the logic is in the Slope class. It can count the trees encountered by giving it the rise and run angle components. Other
than that, not much to say about a fairly simple brute force calculation.

## Day 4: Passport Processing

[Year 2020, day 4][4.0]

This problem concerns itself with passports. Passports have eight fields, and north pole credentials have all of those except one.
Part one asks us to validate the passport data in the input file by simply checking that it exists, while part two has more strict
validation on each field. In either case, "cid" is optional.

Part one was trivial: simply validate that the required fields are present, removing those passports that have missing fields.
Part two does the same, then filters a second time using a more complex predicate class that digs into each field a bit more. The
only way I could find to make this more concise affected readability too much, so enjoy Java's lack of brevity.

## Day 5: Binary Boarding

[Year 2020, day 5][5.0]

This problem deals with airplane boarding passes. Each pass uses binary space partitioning to specify the row and column, with
seven bits for the row and three for the column. Each pass has an ID that is `8 * row + col`. This means that each boarding pass
is essentially a regular binary number, except it uses F and B for the row digits and L and R for the column digits.

Part one wants the highest boarding pass ID, that is, the highest number in the input file. Part two wants to find the only gap
between two digits in the input.

The key here is understanding that the input is simply a binary number encoded as a string using alternate characters than zero
and one. The next step is to convert it to something Java understands via string replacement, then parse it into a number using
base two. Once that is done, the actual processing is trivial. The hardest part was understanding the problem statement well
enough to make that logical leap. Sometimes, problems are so complex it is difficult to see the trees instead of the forest.

## Day 6: Custom Customs

[Year 2020, day 6][6.0]

This problem states that each line may contain a bunch of characters. Lines are in groups in the input, separated by blank lines.
Part one wants the union of the characters in each group: essentially a set of all unique characters on its lines. Part two asks
for the intersection, that is, the set of characters common between the lines.

My solution is to convert each line to a set of integers (code points), then perform set operations on them. Specifically, union
and intersection. Once these are reduced, sum all of the groups of input (sets).

## Day 7: Handy Haversacks

[Year 2020, day 7][7.0]

This problem states that there are different colors of bags, and each one must contain certain other bags in various quantities.
Part one asks which color bags can contain shiny gold bags directly or indirectly. Part two asks how many bags a shiny gold bag
must contain.

Similar to year 2015 day 7, this is a test in recursion. I use memoization for both parts due to redundancy in the input. One
gotcha in the problem is not to count the shiny gold bag itself: an easy off-by-one error.

## Day 8: Handheld Halting

[Year 2020, day 8][8.0]

Today's problem asks us to run a simple virtual machine that executes three types of instructions. The program as given does not
halt: it has an infinite loop. Part one asks us to determine the value in the VM's sole register (accumulator) when it first loops
over an instruction it already ran. Part two says that modifying a single instruction can cause the program to halt, and to find
that instruction and the value of the accumulator when it does halt.

This is a simple VM implementation: so simple it doesn't even need any complex state, just a few local variables. Part one is
easily `O(n)` because the program never sees any given instruction more than once: otherwise, it could not loop. Part two has some
different implementations with differing complexity. While a naive implementation works well enough, this solution adds some
optimizing. First, it figures out which instructions are guaranteed to lead to the program halting. Then it executes the algorithm
similarly to part one, except it checks each instruction to see if modifying its operation will result in landing on any
instruction guaranteed to halt. If so, it modifies the operation but it only does this once, per the problem requirements.

## Day 9: Encoding Error

[Year 2020, day 9][9.0]

This problem requires searching a list of integers and finding numbers based on specific properties they have as related to
numbers around them. Part one asks for an integer that cannot be formed from the sum of any two integers in the preceding 25
locations in the list. Part two requires us to find consecutive integers anywhere in the list that add up to that number: then we
sum the lowest and highest integers in that list and return the result. The input data is not sorted.

Part one would have some optimizations if the input were sorted. Without that, a brute force `O(n^3)` algorithm is the best I
could come up with. Thankfully, the input data is small and the program ends in under 1ms.

Part two starts by running the algorithm for part one to get the number. Then it applies an algorithm where it tracks two indices.
It sums all the integers in the list between those indices, inclusive. If the sum is too low, increase the upper bound to include
more integers. If it is too high, increase the lower bound to exclude more integers. If it is just right, terminate. Furthermore,
it keeps a running sum rather than recomputing each time it changes a boundary. This should be, at a minimum, close to the lowest
complexity possible.

## Day 10: Adapter Array

[Year 2020, day 10][10.0]

The problem throws a lot of wordiness around but ultimately it is about combinations of numbers where numbers are related to those
close to them. Specifically, they must be between one and three of their neighbors. Part one asks for a product related to the
spacing. Part two asks how many combinations there are given the constraints.

Part one is simple: sort the input numbers, and keep a running tally of how many jumps of one there are compared to jumps of
three. Multiple those two sums.

Part two sounds like a recursive algorithm to enumerate all of the combinations. However, there are so many that such an algorithm
may never actually complete. Even if it did, it would probably run out of memory first. Instead, consider memoization. If we
encounter a number that already has a solution, just use that solution and tack it on to the current search state. However, what
if we memoized the search before searching? Another way to think of this is what if we applied mathematical induction? Calculate
the base state first: that is, we already know that the highest number in the input has exactly one possible solution: itself.
Cache that result. Now decrease through the input, grabbing monotonically decreasing values. For each number, the quantity of
solutions is equal to the sum of the quantity of solutions for the three next higher numbers, or zero if a number does not exist.
Repeat until all numbers are exhausted. Since we start at zero, that element will have the total number of combinations.

## Day 11: Seating System

[Year 2020, day 11][11.0]

This problem is another variation on Conway's Game of Life that pops up in this challenge regularly. This time, there are grid
elements that are ignored. Part one is an ordinary variation, while part two considers a neighbor to be a seat in any of the eight
directions that is visible across floor elements.

Nothing really to say about this one, other than it was easy but tedious to type out and ensure all those array subscripts were
not fat-fingered.

## Day 12: Rain Risk

[Year 2020, day 12][12.0]

This problem requires us to move a boat around on a grid using navigation instructions. Part one has us track the boat's location
and heading, where the heading is aligned to 90 degree angles. It moves around a few hundred times, then we return the Manhattan
distance of the boat compared to origin. Part two changes how we interpret the instructions by tracking a waypoint near the boat.
Most of the instructions now move the waypoint or rotate it around the boat. Moving the boat moves it toward the waypoint a number
of times equal to the distance argument.

The solution could be more object-oriented, but it would cause the size of the program to balloon quite severely. Instead, just
handle the various decisions in a loop. Simple and effective.

## Day 13: Shuttle Search

[Year 2020, day 13][13.0]

This problem is an exercise in quotients and remainders. Part one asks to find the next bus ID that will arrive. Part two asks us
to find a time when all buses will return at the same time.

Part one is a simple matter of checking each minute to see if any bus evenly divides the current time. Part two is a bit more
involved, and brute force is not feasible. There are actually two ways to solve part two. One is to apply the Chinese remainder
theorem. The other is to perform a heavily optimized search. Since that solution is far simpler than the Chinese remainder theorem
and it completes in a trivial amount of time, it is good enough.

The algorithm first solves the trivial base case, that is, the time when the first bus will be at the station. This is simply its
ID and offset added together. We know that from here, it will only return at times equal to this initial time plus some multiple
of its ID. This means we can set an increment equal to its ID, and only check those multiples. Now we solve it for the combination
of this base case and the next input, iterating through all of the inputs.

For each subsequent bus we keep adding that increment until the bus ID evenly divides the sum of the current time and the bus's
offset. This is necessarily a solution to all of the previous buses: the increment ensures that. Now we multiply the increment by
the bus ID, ensuring that future iterations also solve the current iteration.

It is also worth pointing out that the maximum interval is all of the bus IDs multiplied together, which is a little bit over 836
trillion. The actual solution is close to that maximum value, which helps explain why brute force is not viable: that is a massive
amount of times to check.

## Day 14: Docking Data

[Year 2020, day 14][14.0]

This problem describes a simple computer, and we need to set memory at various addresses to specific values, then get the sum of
those values. It has us use a bit mask to do so, but in different ways for each part. Part one requires us to use a bit mask to
modify the values set, while part two uses the bit mask to modify the memory address. Part two not only changes how to interpret
the bit mask, but the mask can actually return multiple values because Xs can be either zero or one.

Part one is fairly simple, with the logical leap being that the mask encapsulates both bitwise `and` and bitwise `or`. Then it is
a simple matter of applying both operations to the input and returning a single value. Part two instead has us modifying
addresses, and reinterpreting the mask. The solution below is fairly efficient and straightforward, and comments explain the
algorithm.

## Day 15: Rambunctious Recitation

[Year 2020, day 15][15.0]

This is a simple numbers puzzle where each value in the sequence is either zero if the previous number does not exist earlier in
the sequence, or it is equal to the difference between its current and previous positions in the sequence. Parts one and two run
this algorithm for different iteration counts.

The naive implementation is to store everything in a list or array where the index is the iteration number: f(2020) would be
stored in position 2020, for example. This works fine for small numbers of iterations, but quickly grows infeasible as that
algorithm is `O(n^2)`. Swapping the mapping so the array or list index is the value seen and the value stored at that location is
the last iteration it was seen turns this into an `O(n)` operation.

Incidentally, using a list in Java rather than an array has a practical side effect of increasing the runtime dramatically due to
a large amount of constant operations: notably, boxing and unboxing. This is also true if using a hash map to store the values,
except it is worse due to additional operations such as function calls to hash keys and dealing with chained entries. Using the
array approach is the most efficient by an order of magnitude.

## Day 16: Ticket Translation

[Year 2020, day 16][16.0]

This problem has us look at train tickets where we cannot read the field names, figure out what those field names are, then do
some math with certain fields to prove it. We do this because we know what the field names are and what valid data is for each
one, we just do not know which numbers on a ticket match up with which field definition. Part one starts out by having us remove
all invalid tickets, where a ticket cannot meet any of the validation rules. Part two extends this by asking us to map the fields,
then do some math with our own ticket to prove that we mapped them correctly.

The solution is just a lot of number crunching. Finding invalid tickets is trivial: for each ticket, check against each field
definition. If it cannot meet any of them, it is invalid. Each field definition starts out valid for each field index: we then go
through each field definition and see if there is any ticket that is not valid for that field. If so, we remove that field index
and keep going. Eventually, each field definition is only valid for a single field index. From there, we can check our own ticket
against the field definitions and get the necessary values.

## Day 17: Conway Cubes

[Year 2020, day 17][17.0]

This problem is another Conway's Game of Life. Part one simulates it in 3D, part two in 4D. In both cases, it starts with a 2D
grid that expands into higher dimensions.

This is very similar to year 2015, day 18, but more complex. More than anything, coding this was simply tedious. As far as I am
aware, Java does not have a way to code this generically in terms of array dimensions. This was a lot of copy and paste with minor
tweaks to array indices.

## Day 18: Operation Order

[Year 2020, day 18][18.0]

This problem is an exercise in parsing a string into a mathematical expression, where the expression only has numerical constants,
addition, multiplication, and parenthesis. Part one has the rule that there is no operator precedence: addition and multiplication
occur left to right. Part two switches it up by stating that addition has precedence.

There are multiple ways to handle this problem, and I opted to use two different algorithms. For part one, I wrote a
[recursive-descent][18.1] left-associative parser. Given part one has no operator precedence, this is a fairly simple
implementation that can greedily consume tokens and add them to Expression objects without more advanced logic.

For part two, I implemented a [shunting-yard algorithm][18.2] that handles operator precedence: since it has to manage extra data
structures anyway, it did not add any measurable complexity. After it runs, I reverse the output which converts it to [RPN][18.3].
I opted not to generalize the solution for part two and apply it to part one, simply so I have an example of both approaches.

In both cases, I parse input tokens into Expression objects. This ends up being a highly-unbalanced binary tree, but is an
interesting intermediate form that aids in debugging.

## Day 19: Monster Messages

[Year 2020, day 19][19.0]

This problem requires parsing input using grammar rules that describe a linear grammar. Part one has no loops in the rules, while
part two does.

The input to the program is a set of production rules for a finite state machine along with strings to test whether or not they
are in its language: i.e. whether this particular finite state machine can produce those strings. One important property of these
grammar rules is there are no loops: essentially, starting with the nonterminal 0, the rules form an acyclic directed graph. The
algorithm for part one reduces the rules to a single regular expression which Java understands, and can use to match the input
strings.

Part two is a bit trickier. It is no longer a true regular language, instead, it is context-free. Implementing a parser for this
would be a lot of work for an AoC challenge: instead, I opted to "unwind" the grammar a bit and construct a set of regular
expressions that work for the input provided.

## Day 20: Jurassic Jigsaw

[Year 2020, day 20][20.0]

This problem requires us to piece together a grid of tiles. We know the tiles are all square, and they only fit together one way:
tiles are also transformed randomly, either rotated, flipped, or both. This means we can make certain assumptions which simplify
the logic. Part one requires us to find the proper tessellation, and prove it by multiplying the IDs of the four corner tiles
together. Part two requires us to find hidden sea monsters in the virtual monochrome image, then calculate a value based on the
bits set outside of the sea monsters themselves.

This is an ugly one. Wrapping tiles and grids in objects that transform coordinates saves some space, but most of the bulk is in
assembling the tiles. There are other approaches, but they are more complicated. I really do not like this solution, but tinkering
with other approaches really did not improve anything. Regardless, it works; while it is a little repetitive and verbose, it is at
least somewhat clear in its function.

## Day 21: Allergen Assessment

[Year 2020, day 21][21.0]

This puzzle requires some basic set operations. Part one asks us to find ingredients that cannot be food allergens, and count how
many times they occur in all of the foods. Part two requires us to reduce the potential allergens down and solve which ingredient
maps to which allergen, then prove the answer by emitting the problematic ingredients in alphabetic order - of their corresponding
allergen names.

There is not much to say about this one. It is basic set manipulation, finding intersections of sets then reducing them to make
them unique.

## Day 22: Crab Combat

[Year 2020, day 22][22.0]

This puzzle requires us to play a simple card game called &quot;Combat&quot; which is very similar to War. One helpful property of
the game and the input data is there are no ties: every card has a unique rank. Part one asks us to simulate a regular game, while
part two adds some new rules involve recursive sub-games in certain circumstances.

This is a fairly simple recursive algorithm, definitely simpler than some others in Advent of Code. Each sub-game is completely
separate from the parent games, only needing parent game state to set up the hands for the sub-game. Compared to combinations and
permutations, this is a lot simpler to implement.

## Day 23: Crab Cups

[Year 2020, day 23][23.0]

This is a puzzle where we move cups around in a circle per some rules, and need to prove we have the final layout of the cups
using some math. Parts one and two vary in the number of cups and the number of rounds in the puzzle.

The rules are fairly simple, and there are several ways to go about this. At first glance this appears like a good solution for a
circular linked list, and this did work well for part one. However, part two taxes that solution brutally. Navigating around in
circles takes a lot of time. For this reason I moved to a simple array of integers in a singly-linked list that references itself
circularly. I also removed all list iterations, since with the array we know precisely where each element is stored. This means
all accesses are direct, making this program complete in a timely manner. The linked list implementation I had previously tried
ran for over a minute on part two until I killed the program.

## Day 24: Lobby Layout

[Year 2020, day 24][24.0]

This penultimate puzzle is a variation of Conway's Game of Life using a hexagonal grid.

The only slightly tricky problem here is representing a hexagonal grid using a coordinate system. I opted for treating the X axis
as having a space of two between grid elements, since the diagonal movements move halfway between them. Instead of using floats or
decimals, this is a cleaner and less error-prone approach.

## Day 25: Combo Breaker

[Year 2020, day 25][25.0]

The final, one-part puzzle is a fairly simple brute force algorithm. Using two public keys, reverse engineer an algorithm and
determine an encryption key.

The algorithm is spelled out pretty much verbatim in the problem statement, although I found it a little confusing. Generally, the
example problem includes different data from the real problem. This time, some of the data was the same, namely, the multiplier
used to figure out the number of iterations. Aside from that, this is a simple and straightforward problem.

[1.0]: https://adventofcode.com/2020/day/1
[2.0]: https://adventofcode.com/2020/day/2
[3.0]: https://adventofcode.com/2020/day/3
[4.0]: https://adventofcode.com/2020/day/4
[5.0]: https://adventofcode.com/2020/day/5
[6.0]: https://adventofcode.com/2020/day/6
[7.0]: https://adventofcode.com/2020/day/7
[8.0]: https://adventofcode.com/2020/day/8
[9.0]: https://adventofcode.com/2020/day/9
[10.0]: https://adventofcode.com/2020/day/10
[11.0]: https://adventofcode.com/2020/day/11
[12.0]: https://adventofcode.com/2020/day/12
[13.0]: https://adventofcode.com/2020/day/13
[14.0]: https://adventofcode.com/2020/day/14
[15.0]: https://adventofcode.com/2020/day/15
[16.0]: https://adventofcode.com/2020/day/16
[17.0]: https://adventofcode.com/2020/day/17
[18.0]: https://adventofcode.com/2020/day/18
[18.1]: https://en.wikipedia.org/wiki/Recursive_descent_parser
[18.2]: https://en.wikipedia.org/wiki/Shunting-yard_algorithm
[18.3]: https://en.wikipedia.org/wiki/Reverse_Polish_notation
[19.0]: https://adventofcode.com/2020/day/19
[20.0]: https://adventofcode.com/2020/day/20
[21.0]: https://adventofcode.com/2020/day/21
[22.0]: https://adventofcode.com/2020/day/22
[23.0]: https://adventofcode.com/2020/day/23
[24.0]: https://adventofcode.com/2020/day/24
[25.0]: https://adventofcode.com/2020/day/25
