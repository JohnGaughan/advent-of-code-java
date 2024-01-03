# Year 2015: Make it Snow

## Day 1: Not Quite Lisp

[Year 2015, day 1][1.0]

This problem involves counting two different characters in the input and comparing them, as if Santa starts on floor one and we
want to know what floors he visits based on going up or down. Part one asks for his final floor, while part two asks when he first
enters the basement.

My approach to this puzzle was simple. For part one, count the number of ups and downs, and take the difference. Since addition is
commutative, order does not matter. For part two, order _does_ matter because the simulation ends when Santa first enters the
basement floors. For this part, iterate over the input and track the current floor along with how many steps he took.

## Day 2: I Was Told There Would Be No Math

[Year 2015, day 2][2.0]

This problem involves inspecting a large number of boxes (perfect right rectangular prisms). For part one, we need to figure out
how much wrapping paper the boxes need: this is the surface area plus a little extra. For part two, we need to know how much
ribbon: this is the perimeter of its smallest side plus some extra for the bow.

This was a simple streams exercise where the stream operations take dimensions as input, and transform each one into a numerical
answer for that input. Then sum up the numbers to get the requested answer.

## Day 3: Perfectly Spherical Houses in a Vacuum

[Year 2015, day 3][3.0]

This problem deals with Santa visiting houses on a grid. Given directions to move in the four cardinal directions, Santa moves
around and visits each house in turn. Part one asks how many houses he visits. Part two adds a second Santa, and they take turns
moving. How many houses does that team visit?

There is no easy reduction here, since intermediate steps need to be tracked in order to know exactly how many houses Santa and
his robot visited. My approach here was simply to iterate over the directions and track the current Point2D and save them off in a
set. Since we do not care about repeat visits, a set is ideal to track the unique Point2D visited. For part two, I track two sets
of Point2D and alternate which one is modified via a small array.

## Day 4: The Ideal Stocking Stuffer

[Year 2015, day 4][4.0]

This problem asks us to generate MD5 hashes using a constant prefix appended with a monotonically increasing suffix. The goal is
to generate a hash that starts with a number of zeros. Part one asks for five zeros, part two asks for six.

No way about it, brute force is the optimal solution here. MD5 is quite broken but not in a way that provides any easy shortcuts
for this problem. It was probably chosen because of the specific weakness that it is very fast: not a desirable trait for
cryptography, where you want to put a speed limit on password cracking. My Ryzen 5800X takes around 600ms to solve part two: any
visitors from 2030 or so want to laugh at how slow that was?

## Day 5: Doesn't He Have Intern-Elves For This?

[Year 2015, day 5][5.0]

This problem asks us to determine if strings are naughty or nice, and to count the number that meet this criteria. Parts one and
two have different criteria.

I could not find any fancy shortcuts for this one. Instead, I opted for efficiency: early on, I noticed that there could be some
clean and simple solutions that take more time to solve it, compared with a single-pass solution. I opted for the latter. While
the Advent of Code is not about punishing levels of input where a polynomial-time solution might take millions of years the way
Project Euler can, I still like to add a little extra challenge for simpler puzzles like this one.

## Day 6: Probably a Fire Hazard

[Year 2015, day 6][6.0]

Given an array of a million lights in a 1,000 by 1,000 grid, turn sections of them on, off, or toggle their state. Part one asks
how many lights are lit after executing the input commands in order. Part two changes things up by giving each light a brightness
level where on and off increase and decrease the light level (minimum of zero), while toggling it increases the brightness by two.
It then asks the total brightness across all lights.

Simple solution to a puzzle where brute force is the only option. Nothing much to say here: just iterate through the input and see
what happens.

## Day 7: Some Assembly Required

[Year 2015, day 7][7.0]

This problem introduces what is essentially a network of logic gates. Instead of booleans, however, they operate on integers and
perform bitwise operations on them. Part one asks the value of "a", the terminal gate, once everything is wired up. Part two then
has us reset the network and change it by assigning "b" the value "a" had in part one, and seeing how that changes "a".

This was an interesting puzzle that I really enjoyed programming. I saw two potential approaches. The first would be to create a
big tree where the parent node is the wire "a" and its operator. Each child node is either a constant value or another wire with
its operator, and so on until the tree can be reduced bit by bit and constants roll up to the root node. That would have been a
bit more tedious and a lot more code. I also noted that the digraph of wires in the input is not a tree, although without any
loops that is easy enough to work around. However, it indicates this is probably not the best approach.

Instead, I went with a simpler approach. Given wire "a", recursively evaluate its children until a particular wire can be reduced
to a constant value. I use memoization to avoid redundancy, although the input size is small enough that it does not take much
time either way. Since the entire solution is calculated outside of the rules, reusing them for part two is trivial.

## Day 8: Matchsticks

[Year 2015, day 8][8.0]

This problem asks us to count the number of characters in various strings based on some rules. Specifically, the strings are
encoded in a way similar to how compilers expect them in a program: surrounded by double quotes, with some backslashes escaping
other characters. Counting the strings as-is means counting their contents without the surrounding quotes. Part one asks us to
figure out how many characters are inside the quotes after parsing the escapes, and subtracting from the first calculation. Part
two asks us to count how many characters each string would take up after escaping them and surrounding with new quotes, then
subtracting the first number from this.

The solution is made simpler than it otherwise would be due to the input all being well-formed, so minimal validation necessary;
as well as the fact that only the string lengths are needed, not the entire strings. Simply counting the characters that _would_
be there makes this program shorter than if it needed full validation and construction of strings.

## Day 9: All in a Single Night

[Year 2015, day 9][9.0]

This problem states that Santa wants to visit eight locations, visiting each one once. He can start and finish at any location.
Each pair of locations is separated by a particular distance. Part one asks for the route that minimizes the total distance, while
part two asks for the maximum distance.

This is a basic Hamiltonian path. The locations are vertices in a graph, while the distances between them are edges. Thankfully,
this graph is fully-connected: all locations are connected to all other locations. This reduces the problem from finding complete
paths with their distances to a simpler to solve one, which is how I implemented this.

My solution calculates all of the permutations of routes first, using a simple exhaustive depth-first algorithm. Then it
calculates distances second, not even bothering to correlate them with routes since it does not matter, at all. These problems ask
for integer answers, not all the intermediate data. That simplifies the algorithm and data structures. Furthermore, a brute force
algorithm is suitable given the small input size. If this were larger, then optimizations would be required to keep the time and
space complexity reasonable: the number of permutations grows quickly, scaling with `O(n!)`.

Ideally this program would prune the solution space as it goes along in order to reduce the time and space complexity, but doing
so would increase the code complexity. I do not believe that is a worthwhile tradeoff for this specific code challenge.

## Day 10: Elves Look, Elves Say

[Year 2015, day 10][10.0]

The problem asks for the length of the string generated by applying the [look-and-say algorithm][10.1] to a particular input
value. Parts one and two use the same algorithm but with a different number of iterations.

It appears there are no shortcuts here, just plain old brute-forcing. However, it is possible to minimize the amount of string
allocations and temporary objects created.

## Day 11: Corporate Policy

[Year 2015, day 11][11.0]

This problem asks us to increment a password until it meets some validation criteria. Part one wants the next valid password, part
two wants the next valid password after that.

There are two important points here. First is that there are quite a few increments and validations to perform, and that both
Strings and chars are inefficient when used this extensively. Using integer arrays might not be as object-oriented, but it is an
order of magnitude faster: roughly 1/30th the time on my hardware. Strings require a ton of construction and deconstruction,
allocating many, many extra arrays behind the scenes. Character arrays help somewhat, but not a lot: the incrementing logic has
casts in there, and characters are not efficient when doing a lot of casting. Converting to an integer array once at the start and
back into a string at the end adds a tremendous amount of speed and efficiency in the middle.

Second, the password can be thought of as one big integer that is formatted as base-26 for human consumption. Combining the array
elements into a long, incrementing, and converting back is fairly efficient because it is mostly integer arithmetic. This is in
contrast to carrying values manually between array elements, which adds control structures that require a lot more CPU and
possibly cache misses. This is worse than the division and modulo functions.

Even without optimizations this program ran in slightly under a second, but with them, it runs in a few tens of milliseconds.

## Day 12: JSAbacusFramework.io

[Year 2015, day 12][12.0]

This problem requires parsing a JSON string and extracting numbers from it. Part one requires all numbers, part two omits numbers
in or contained in objects that have a "red" property.

This is a fairly straightforward problem that requires moving around a JSON model and returning numbers, which are then summed in
a string. The only thing I really do not like about this solution is the boolean that changes behavior. However, not including it
duplicates a lot of code.

## Day 13: Knights of the Dinner Table

[Year 2015, day 13][13.0]

The problem involves people sitting around a table who prefer to sit next to certain people: who they sit next to affects their
happiness. We need to find the seating arrangement that maximizes total happiness. Part one asks for the maximum happiness given
the inputs. Part two says to inject yourself into the seating arrangement and recalculate again, assuming zero change in happiness
between you and anyone else.

This is a simple job of finding all the permutations, then iterating around the table and calculating the happiness value. For
part two, load the data from the input file, then modify it by adding a neutral party before calculating happiness.

## Day 14: Reindeer Olympics

[Year 2015, day 14][14.0]

This problem asks us to race reindeer. In part one, we simply run the reindeer and see who progressed the farthest after the
number of seconds in the race. In part two, we need to determine who is in the lead each second and award a point to that
reindeer. Rather than seeing who made it the farthest, the winner is the one who spent the most time in the lead.

Part one is easy because we can directly calculate a reindeer's location after a given amount of time. Simply calculate each one
and map rules (representing reindeer) to their distance traveled, and use the streams interface to get the maximum distance. For
part two, we can still use that calculation method, but we need to apply it each second and track each reindeer's points and
locations along the way. Then we can get the maximum number of points among all the reindeer.

## Day 15: Science for Hungry People

[Year 2015, day 15][15.0]

This question asks us to find what proportion of ingredients in a recipe has a maximum score given a scoring algorithm and a
maximum quantity of teaspoons of ingredients used. Part one asks for this score, while part two constrains valid ingredient ratios
based on the number of calories.

There are several ways to go about this, and the more efficient algorithms are based on the fact that there are only four
ingredients. Then it would be possible to write some loops based on those assumptions. Instead, I opted for a general-purpose
algorithm based on an unknown number of ingredients. This led me to a recursive algorithm that does not perform as well, but
produces correct results regardless of the ingredient count.

## Day 16: Aunt Sue

[Year 2015, day 16][16.0]

This problem involves figuring out which rule matches a set of criteria: notably, the criteria is incomplete. Part one looks for
an exact match based on the criteria present, while part two looks for a match based on data that can vary.

There is not much to say about this one. It is fairly straightforward: filter a stream using a predicate that asks each rule if it
matches that predicate. Verify the resultant list has one element, then return the ID of that element.

## Day 17: No Such Thing as Too Much

[Year 2015, day 17][17.0]

Part one is a basic packing problem: how many different ways can you arrange the containers so their capacity adds up to 150? Part
two is a little unclear but it asks us to figure out the minimum number of containers used by any solution in part one, then get
the number of solutions that use exactly that minimum number.

The solution is simple: iterate through all the combinations of containers, keeping in mind that the input data does have
duplicates and both combinations are distinct. From there, do some basic collection and stream operations to process the results
and get the number of solutions valid for each part.

## Day 18: Like a GIF For Your Yard

[Year 2015, day 18][18.0]

This problem is an implementation of [Conway's Game of Life][18.1]. Part one runs the simulation for 100 iterations. Part two
changes the rules slightly by stipulating that the four cells in the corners always live.

Another fairly straightforward implementation. Set up the grid, then run the simulation for a hundred generations.

## Day 19: Medicine for Rudolph

[Year 2015, day 19][19.0]

This problem relates to formal language grammar. Part one is an exercise in string replacement using grammar rules: get the total
number of unique strings after one replacement. Part two asks us to perform many string replacements and to get the fewest steps
required to find the molecule string.

Part one is a simple exercise in iterating over each rule and applying it everywhere in the molecule string it can be, then
counting how many unique results there are. This is slightly complicated by the fact that while Java has a way to get the next
substring starting at an index, it cannot do the same with replacements.

Part two looks really daunting at first, and it is, for the general case. However, the puzzle creator gave a hint that greatly
simplifies things: there is exactly one solution. This means there is no need to look for multiple and find the best, and there is
no possibility of applying rules incorrectly and getting into a dead end (when reducing the string to `e` rather than the fool's
errand of generating it).

My solution is to work backwards, reducing the string by applying the grammar rules in reverse over and over again. Iterate
through the rules. Apply each one to the string from left to right, then move on to the next rule. If the string is ever equal to
`e` then stop. There are some more elegant ways to do this, given that the grammar rules have interesting properties such as the
elements `Rn` and `Ar` always appearing in pairs and always in the same order, like parenthesis. Those elements along with `C` and
`Y` are terminals in this grammar. It is never possible to apply a rule in a way that violates the chemistry naming convention,
e.g. both `C` and `Ca` are symbols, but only `Ca` appears on the left-hand side. I think it would be interesting to do more
intelligent analysis and processing of this string, but the solution runs in 4ms so there is no real need to do so.

## Day 20: Infinite Elves and Infinite Houses

[Year 2015, day 20][20.0]

This is a problem about factorization. The elves delivering presents do so in a way mimics how non-prime factors work, and the
problem is essentially asking for the total of all factors of a number, including one and the number itself. Part one wants this
number multiplied by ten. Part two asks for the number multiplied by eleven, with the caveat that elves only visit fifty houses
before giving up.

I tried several approaches, including directly calculating factors both the hard way and via prime factorization. If you add up
the multiple of each unique factor, then multiply all the sums, it equals the sum of the non-prime factors. For example: the
factors of 12 are 1, 2, 3, 4, 6, and 12. Their sum is 28. 12's prime factors are `2^2` and `3`. Multiplying the sums is
`(2^0 + 2^1 + 2^2) * (3^0 + 3^1)`, which is equal to `(1 + 2 + 4) * (1 + 3)`, which is equal to `7 * 4` which is then equal to
`28`, same as before.

The problem with that approach is even cheating in a memory-mapped file of primes, there were simply too many loops and
mathematical operations. In theory that should perform well, but in practice, it was impractical for this particular problem. The
program took several minutes before I stopped it. The naive solution implemented below takes under 20ms to execute for each half.

## Day 21: RPG Simulator 20XX

[Year 2015, day 21][21.0]

This problem describes a very simple RPG, but is really a problem about determining which combinations of items have better
attributes. Part one wants to minimize gold spent, while part two wants to maximize it.

Brute force is not the ideal solution, but with the tiny input sizes is very well within the realm of feasibility. This is a brute
force algorithm. Two notes: the simulation works the same but uses functions to determine which outcome is better. First, a
function that compares gold spent to the current best gold spent and determines which is better: second, a function that
translates the player winning into a successful or unsuccessful outcome. This way the same algorithm works for both parts, by
delegating key decisions to the calling method.

## Day 22: Wizard Simulator 20XX

[Year 2015, day 22][22.0]

This is a combat simulation like the previous day, but with different parameters. Part one asks for the minimum mana spent to win
the game. Part two asks for the same, with an additional rule that the player takes damage at the start of each of its turns.

This is a brute force algorithm with one major modification: it tracks the current best score, and abandons a simulation when it
is impossible to improve on that score. Other than that, this solution is complicated because the rules of the game are
complicated compared to the previous day.

## Day 23: Opening the Turing Lock

[Year 2015, day 23][23.0]

This problem requires running a program through a simple virtual machine and getting the value of an output register once the
program halts. Part one asks for the value in register B when register A is initialized to zero: part two initializes register B
to one.

The solution is a simple loop that performs the changes to the VM state as stipulated in the problem statement. Changes are made
by telling the operation to perform its logic on the state. Once the instruction pointer no longer points to any instructions, the
program halts and returns the value in register B.

## Day 24: It Hangs in the Balance

[Year 2015, day 24][24.0]

This problem is about finding combinations of numbers that meet specific criteria. Specifically, find a combination from an array
of integers that adds up to one-third of the total sum and has the least number of integers in the combination. For example: if
one combination contains three integers and another has four, use the one with three. Among those that tie for the least number of
elements, find the one with the lowest product. Furthermore, the input is broken up into partitions: part one requires the full
array of integers to be broken up into three groups that all have the same sum, while part two has four groups.

The input has useful properties. The numbers are all unique, which simplifies some logic. Among the combinations with the fewest
elements in the set, none of them result in the remaining, unused numbers being unable to form two (or three) combinations that
are unable to add up to the proper sum. The solution I came up with determines the theoretical minimum number of elements that can
form a combination. It then calculates the permutations where the set has that size: if none are found, increment the set size by
one and repeat until there are combinations. Then filter any that result in the remaining numbers not forming two or three more
sets: however, the input data is such that this never actually happens. Finally, compute the product of each set and take the
lowest one.

## Day 25: Let It Snow

[Year 2015, day 25][25.0]

This is a one-part puzzle that requires performing some calculations a large number of times. The input is coordinates on a grid,
where each cell has a unique integer that can be determined by its row and column. Once you have the integer, run the calculations
that number of times to get the final answer.

The first task is determining the number of iterations. There are patterns in the numbers, such as triangular numbers. However,
the important sequence is in the first column: the [Lazy caterer's sequence][25.1]. Correcting for an off-by-one because the
program assumes `f(1, 1) = 1` instead of zero, we can directly calculate any number in the first column. If we go past the desired
row by an amount equal to the column number, we get the diagonal containing the cell we need. Since the numbers simply count up,
we can add the column to that calculated value to get the number in that cell. Again, we need to account for an off-by-one because
the first "real" cell is given, not calculated. In the code below, the calculation to get that specific grid element is simplified
a bit.

Once this is done, apply [modular exponentiation][25.2] to get the result. This completes in 21 iterations instead of 1,073,157
(or over 30 million without optimizing away the repetitions) of a brute force implementation. It is far, far more efficient. Note
that `BigInteger` has a method to compute this, but it is slower than using primitives. Since the numbers all fit in `long`, this
is the better approach.

[1.0]: https://adventofcode.com/2015/day/1
[2.0]: https://adventofcode.com/2015/day/2
[3.0]: https://adventofcode.com/2015/day/3
[4.0]: https://adventofcode.com/2015/day/4
[5.0]: https://adventofcode.com/2015/day/5
[6.0]: https://adventofcode.com/2015/day/6
[7.0]: https://adventofcode.com/2015/day/7
[8.0]: https://adventofcode.com/2015/day/8
[9.0]: https://adventofcode.com/2015/day/9
[10.0]: https://adventofcode.com/2015/day/10
[10.1]: https://en.wikipedia.org/wiki/Look-and-say_sequence
[11.0]: https://adventofcode.com/2015/day/11
[12.0]: https://adventofcode.com/2015/day/12
[13.0]: https://adventofcode.com/2015/day/13
[14.0]: https://adventofcode.com/2015/day/14
[15.0]: https://adventofcode.com/2015/day/15
[16.0]: https://adventofcode.com/2015/day/16
[17.0]: https://adventofcode.com/2015/day/17
[18.0]: https://adventofcode.com/2015/day/18
[18.1]: https://en.wikipedia.org/wiki/Conway's_Game_of_Life
[19.0]: https://adventofcode.com/2015/day/19
[20.0]: https://adventofcode.com/2015/day/20
[21.0]: https://adventofcode.com/2015/day/21
[22.0]: https://adventofcode.com/2015/day/22
[23.0]: https://adventofcode.com/2015/day/23
[24.0]: https://adventofcode.com/2015/day/24
[25.0]: https://adventofcode.com/2015/day/25
[25.1]: https://en.wikipedia.org/wiki/Lazy_caterer%27s_sequence
[25.2]: https://en.wikipedia.org/wiki/Modular_exponentiation
