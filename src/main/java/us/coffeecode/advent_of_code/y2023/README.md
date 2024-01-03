# Year 2023: Global Snow Production

## Day 1: Trebuchet?!

[Year 2023, day 1][1.0]

As usual, day one is a softball.

Part one asks us to find the first digits starting from either end of each line, and treat them as a two-digit number. Find the
sum of all such two-digit numbers across all lines.

Part two adds the complexity that digits might be spelled out, e.g. "two" instead of "2". My solution performs the same overall
solution, except in addition to finding numerical digits, it also looks for digit names at each string index and returns the first
match of either.

## Day 2: Cube Conundrum

[Year 2023, day 2][2.0]

This implementation spends most of its effort on parsing the input. From there, both parts have some very simple requirements for
how to filter and calculate games which is easily implemented using stream operations.

After parsing, part one filters the games based on a maximum number of cubes of each color. Part two does not filter.

Once that is done, simply stream the games and convert each one to a long integer. For part one, we simply need the game's ID. For
part two, get the power of the game. The game record has a method that gets the maximum values for each color of cube, then
multiplies those cube counts together to get the power.

Once each game is converted to integer form, all that is left is to sum the stream to get the final answer.

## Day 3: Gear Ratios

[Year 2023, day 3][3.0]

Today's puzzle was more tedious than anything. Instead of dealing directly with raw string input throughout, my solution parses
the input board into two sets upon which it operates until the end. First is a set of Point2Ds that represent either symbols (part
one) or gears (part two). Next, there is a set of Range2Ds that mark the endpoints of numbers on the board.

From here the code reduces the sets as appropriate. For part one, it finds all number ranges that are not adjacent to a symbol.
Once we have only those ranges, then convert the raw board data for that range into an integer and get the sum of all such ranges.

For part two, the logic is inverted. Inspect each gear, and find all numeric ranges that are adjacent to the gear. If there are
exactly two such ranges, convert them from string to integer, multiply those two numbers, and add the result to the running total
to get the final answer.

## Day 4: Scratchcards

[Year 2023, day 4][4.0]

We are given some lottery cards and two ways to score them.

Part one asks us to count the matching numbers on each card. Given that one matching number has a score of one, each additional
match doubles the score. This essentially means the real score is exponentiation base two. First we have to subtract one from the
number of matches, then bit shift one left by that number. Once this is done, sum the scores of all the cards. This is done in a
stream operation that filters for winning cards only, then performs the math conversion to score each card. Finally, use the
built-in `sum()` function to get the answer.

Part two changes things up a bit. When a card wins, it duplicates the cards that come after it in sequence. So if card ten has
three matching numbers, we duplicate cards eleven through thirteen and play those duplicates again. We are also given that winning
cards will never reference cards that do not exist.

Cards only ever reference cards with IDs that are greater in value. Given that cards can have quite a few matching numbers, it is
easy to see that any particular card could be duplicated _many_ times. However, each card will always have the same score every
time it is referenced due to the one-way nature of the evaluation. This is a perfect opportunity to apply a memoization algorithm.

Starting at the end and working backwards, we cache the score of each card. If a card references other cards, it is guaranteed
that those other cards will already be scored. We can then simply grab the cached value and add it to the current card without
recalculating what was already done. The answer for my input data was in the millions, which means it would have taken a _long_
time to run without memoization.

## Day 5: If You Give A Seed A Fertilizer

[Year 2023, day 5][5.0]

This is our first real optimization problem. Part one works fine with a brute force solution, but part two takes a bit longer. Not
so long as to be infeasible, but certainly less than ideal.

We are given a mapping function and some inputs. Given the input values, run them through multiple stages of mapping. If a value
falls within any of the source ranges, replace it with the corresponding value at the same location in the destination range. If
it is not mapped, it keeps its value. Perform this a total of seven times to get the final value. Perform this entire algorithm
once for each input value, then find the lowest output value.

Part one is fairly simple, due to the ranges not overlapping. Simply find the zero or one translation ranges and translate the
value if necessary. Process each input and track the lowest output.

Part two changes the meaning of the input: instead of a sequence of input values, pairs represent ranges where the first value is
the start of the range and the next value is the size of the range.

Checking each individual value in all of the ranges is possible, but very time-consuming. Given the sizes of the ranges in the
actual input data, as opposed to the example data, there are trillions of numbers that will not and cannot be the answer because
they make it through each step as part of a much larger range where only the start of said range has a chance at being the answer.

The solution is not to process individual numbers as inputs: instead, process ranges of numbers. At each step, an input range can
be chopped up one of several ways or not at all. When processing one transformation step such as seed-to-soil, dump all of the
inputs into an "unmapped" collection. Process each range mapping one at a time. For each range mapping, see if any unmapped ranges
overlap with it. If so, map the intersection of the ranges and add it to the results: this is not processed again during this
step. If any part of the input range hangs below or above the range mapping, chop off that part of the range and add it to the
unmapped collection: this portion may overlap with another range mapping that belongs to this step. At the end, take all of the
unmapped pieces and carry them forward into the results collection for this step.

My code performs a merge step that takes all of these resultant ranges and merges together whatever it can. I found this did not
materially affect the execution time, likely due to the merge time roughly equaling the extra time spent calculating redundant
ranges. The time is also so fast that it is likely dominated by overhead and run-to-run randomness anyway. However, it feels more
technically correct to me to merge ranges which may matter with a different input file.

Both parts are able to use the exact same solution code: the key insight here is that for part one, we can represent the
individual input values as ranges of size one. While technically the code would be simpler for a more optimized part one, I
greatly prefer having a single algorithm and no duplication of code or portions of code.

## Day 6: Wait For It

[Year 2023, day 6][6.0]

This is a simple puzzle where we need to calculate distances traveled in a boat race. The distance is simply its speed multiplied
by time. Speed is determined by the amount of time spent holding the boat at the start, multiplied by the remaining time after
letting go when the boat is free to travel.

The different between the two parts is how we interpret the input data: either several races with their own times and distances,
or by concatenation all times and distances separately into one massive race.

Either way, the calculation is the same with the only different being whether we multiply the count of different best distances
together in part one or find the one answer for part two.

This was a simple problem with a simple solution that easily ran very efficiently without any tricks. The only meaningful editing
I employed after getting the answer was to make my code more concise.

## Day 7: Camel Cards

[Year 2023, day 7][7.0]

Poker is today's game. However, this version is different from normal poker. Cards have no suite. There are more than four of each
card in the deck: five of a kind without wild cards is possible. Ranking hands goes by the type of hand as usual, but ties are
broken by comparing cards, in sequence, between the hands. Finally, scoring a hand is done by taking its relative position among
all of the other hands and multiplying by its bid. The answer to each problem is then calculated by adding up all of those scores.

Part one is fairly straightforward as usual. Each hand needs to know its cards and bid. Additionally, the code predetermines its
type as well, since this always needs to be done, never changes, and will be used quite frequently to sort the hands. May as well
do the calculation once and save it.

From here we simply sort the hands and calculate the scores.

Part two adds the curve ball that Jacks are now Jokers, and wild. First, this is handled via a parameter in the problem's
properties file. That way the code dynamically handles both scenarios. Next, this makes two changes to tha algorithm.

Card ranking needs the option to sort Jokers below all other cards. This means the "J" in the input can have one of two values
used for sorting. This is the easy part.

Being wild, jokers can dramatically change the type of a hand. This means the logic that figures out the type of hand works
differently with wild cards enabled and present. However, it is actually simpler. Consider this logic in the case of wildcards
either disabled or not present in a hand:

1. Sort the cards so identical cards are next to each other.
1. Count the number of unique cards.

Now inspect the cards:

* If there is one unique card (11111), there must be a five of a kind.
* If there are two unique cards, there must be a four of a kind or full house. Four of a kind must be 12222 or 11112; full house
  must be 11222 or 11122. The locations where the cards change have no overlap, so check either set of conditions to tell them
  apart.
* If there are three unique cards, there must be two pair or three of a kind. Two pair must be 11223, 11233, or 12233; three of a
  kind must be 11122, 12223, or 11122. It is easy to check the three sets of card indices that are separated by two ((0, 2), (1,
  3), (2, 4)) to see if there are two cards with the same value and another card in between them. If that is the case, there is a
  three of a kind: otherwise, two pair.
* If there are four unique cards (11234), the only option is one pair.
* Anything else (12345), there is no special hand.

With wildcards, we change things up a bit but in some ways it gets easier.

1. Count how many wildcards there are. If zero, simply use the previous algorithm. Otherwise, continue.
1. Remove the wildcards from the hand and sort. Save this modified hand: we use it once during the main algorithm.
1. Count the number of unique cards.

Now inspect the cards and counts:

* If there is one wildcard and one unique non-wildcard (J, 1111), then the best hand is five of a kind.
* If there is one wildcard and two unique non-wildcards (J, 1222 or 1122), then check whether there is a 3-1 or 2-2 split of
  non-wildcards. The best hand will either be four of a kind (3-1) or full house (2-2).
* If there is one wildcard and three unique non-wildcards (J, 1123), then the best hand is three of a kind.
* If there is one wildcard and four unique non-wildcards (J, 1234), then the best hand is one pair.
* If there are two wildcards and one unique non-wildcard (JJ, 111), then the best hand is five of a kind.
* If there are two wildcards and two unique non-wildcards (JJ, 112), then the best hand is four of a kind.
* If there are two wildcards and three unique non-wildcards (JJ, 123), then the best hand is three of a kind.
* If there are three wildcards one one unique non-wildcard (JJJ, 11), the the best hand is five of a kind.
* If there are three wildcards and two unique non-wildcards (JJJ, 12), then the best hand is four of a kind.
* If there are four wildcards (JJJJ, 1), then the best hand is five of a kind.
* If there are five wildcards (JJJJJ), then the best hand is five of a kind.

There are more steps, but far less checking of cards. Most of what we need to know is the number of wildcards and the number of
unique non-wildcards. Only one possibility is further split into two options.

At first, the wildcard reveal for part two looked daunting to me. I thought it might need some complex algorithm to try different
options and rank them. But then I started thinking about how to narrow it down. For a given number of wildcards, what
possibilities are there? In real poker with wildcards it is generally trivial for a human to figure out, even if we rarely play
with wildcards in the real world. But taking away suites, flushes, straights, and true poker ranking based on card value (e.g.
AAA22 beats QQQ99) sure simplifies it a bit. There honestly aren't many options, despite the above list being longer: many of them
gravitate toward three, four, and five of a kind. Full house and one pair show up only once, and two pair is absent.

## Day 8: Haunted Wasteland

[Year 2023, day 8][8.0]

We get our first problem today where the solution is fairly straight-foward and does not require memoization or caching, but will
still take an incredibly long time to calculate with brute force.

The problem presents us with a maze that is defined by connected rooms. Some rooms are starts, others are ends. We move between
rooms by turning left or right: by this specification, every room is a T intersection. We need to find the end of the maze to get
out.

Parts one and two differ in that part one has a single start and a single end: part two has multiple starts and ends, traversed in
parallel. To finish, all of the ghosts in the maze must finish in an end room on the same turn.

My implementation uses the same algorithm for both parts, with each part providing lambdas used to identify start and end rooms.
Since room IDs are unique and part one specifies entire room IDs for start and finish, it will only ever have one start and one
finish. Part two uses the last character in the room name for the same purpose, so the algorithm actually works in parallel there.

The algorithm works by calculating each path, one at a time: once it reaches an end, it returns the number of steps. For part one,
this is simply the answer. For part two, we are left with multiple path lengths but notably _do not keep iterating_. The problem
with that approach is the answer, for me, was more than ten trillion: this will take a very long time to calculate even on fast
hardware. Furthermore, there are loops in the maze: the algorithm would be redoing the same work many, many times to get that
answer.

They key to solving this problem is found in middle school math class: the least common multiple. It is also fortunate that each
start has only one end: otherwise, this could be a bit more complicated. By taking the LCM of the path lengths, we arrive at the
answer in a tiny fraction of a second instead days, weeks, or until the Sun destroys Earth in a billion years.

Finally, we need to mention that this solution is not guaranteed to work for all inputs. A generalized solution that is guaranteed
to work would apply the Chinese Remainder Theorem. The reason is it is possible for the loop to start somewhere other than the
starting point. Think of it like a running track. They are shaped in a loop where each straight and each turn are 100 meters long.
However, they often have an extra part hanging off where runners competing in short races start outside the loop, then end before
the first turn which gives them room to slow down at the end.

Much like the running track, it is technically possible that the starting point will advance a few rooms before hitting the true
start of the loop. With that type of input, this solution will produce incorrect results because it sizes the repeating loop
portion incorrectly. The Chinese Remainder Theorem is useful to optimize out repeating portions when the whole run does not
repeat, only a portion does. You can see similar examples in Advent of Code year 2016, day 15; year 2017, day 13; and year 2020,
day 13.

In this case, similar to the other three problems I mentioned, we can implement a simpler algorithm due to the nature of the
input. The number of turns on the first line of input is always prime, for all inputs: I verified this on (sigh) reddit.
Furthermore, the input describes multiple disjoint sets of rooms. One starting point always has one and one only ending point, and
paths never cross. Finally, the length of each loop is the product of two primes, one of which is the number of turns. In other
words, the turns are repeated an integer number of times _and_ the number of times they are repeated are coprime across all of the
starting locations.

These properties mean the simplified solution using the least common multiple is guaranteed to produce correct results _as long as
the input meets these constraints_. If you cook up an input that does not follow those rules, this algorithm will produce an
incorrect answer.

## Day 9: Mirage Maintenance

[Year 2023, day 9][9.0]

Today's puzzle is a fairly simple math problem. We need to find the next value in a sequence. To do so, we essentially perform
differential calculus by hand until the difference between values is zero: then we unwind back up the triangle of values by adding
the difference to each terminal value of the previous sequence. When we arrive at the top, we calculated the next value of the
original sequence.

The implementation is recursive. First, we calculate the differences between each value in the current level. If these differences
are all zero, then the answer must be any value of the current level.

Otherwise, we add the final value of the current level to the result of calling the recursive function with the differences, that
is, the values in the next level.

One thing that tripped me up at first was the zero condition. My code checked for values greater than zero, instead of not equal
to zero. This worked for the sample data, which only had monotonically increasing sequences. That condition is not true for the
real input: while I appreciate the free work by the Advent of Code maintainers, this is a recurring issue where the real input has
edge cases that are not present in the example input.

The only difference between parts one and two is that part two reverses the input. That is a trivial operation that does not
change the algorithm at all. It is a change to the input data.

## Day 10: Pipe Maze

[Year 2023, day 10][10.0]

Advent of code would not be what it is without a puzzle involving an ASCII pipe maze of some type.

We are given a pipe maze in the form of ASCII art. There is a starting point, and a guarantee that the pipes connected to that
start form a closed loop with no branching. Locations outside of that loop may be bare ground, or random "junk" pipes. Part one
asks us to find the size of the loop by way of asking for the farthest point from the start location measured by traversing
through the pipe.

Part two asks us to find the number of locations that are enclosed by the loop: what is its internal area? There are several ways
to approach this, some more complicated than others. I opted for the
[even-odd rule][10.1]. The short version is you take every point in the input area and draw a straight line to the edge. You then
count how many boundaries you cross: if this number is odd, then the point is inside the shape; if even, it is outside. A simple
way to visualize this is with a circle drawn in the center of a sheet of paper. If you take a pencil and draw a line segment from
the center the edge of the paper, you will cross the circle's edge one time. If you draw a line segment from outside the circle
and through it, you will cross it twice, so the starting point is outside. If you draw a segment tangent to the circle, then it
does not actually cross and that is an even point.

Of course, the shapes in this puzzles example and real inputs are far more complex than a simple circle or regular polygon.

My implementation for part two actually runs the same logic as part one to get started, since that code determines which points
are part of the loop. Once that is done, it performs some pre-processing to simplify the implementation. First, it marks every
point not on the loop as being bare ground (a period). This is an `O(n)` operation in relation to the board size. Next, it finds
each turn that goes down: `F` and `7`. It searches to find the corresponding turn below it. If it matches `F` and `J`, or `7` and
`L`, then that means the combined turns are equivalent to a straight horizontal section. It then replaces the top turn with `-`.
This technically breaks the input for its original purpose, but greatly simplifies the even-odd implementation.

Next, we iterate the input board from the top row down. At each point we figure out its parity.

* Is the current point part of the loop? If so, ignore it and continue with the next point.
* Is the current point in the top row? If so, its parity must be even: mark it as such and continue with the next point.
* Now it gets interesting. Search upward, counting the number of `-` encountered until we find either a point already marked as
  even or odd, or the top of the board.
    * If we find the top of the board, save the current point in either the even or odd bucket as appropriate.
    * If we find another point, combine the parities of the current and other point and save in the even or odd bucket as
      appropriate. For example: the previous point above was odd, and we saw an odd number of `-` between them. The combined
      parity is even.

Once we are done processing each point, the answer is simply the number of elements in the odd bucket. Those are the points inside
the loop.

There are other algorithms that could work here, but this one wound up being very simple. It also runs very quickly because it
uses memoization to cache results. While it does loop upward into rows previously calculated, it never needs to look up beyond the
previous point that was calculated. This avoids a ton of redundant calculations, saving a ton of time especially in regions that
have a lot of empty space.

## Day 11: Cosmic Expansion

[Year 2023, day 11][11.0]

Today's puzzle is another one where a naive implementation works in part one but is infeasible in part two.

We are given a two-dimensional array containing galaxies and empty space. We need to expand all rows and columns that contain no
galaxies, then find the Manhattan distance between each pair of galaxies accounting for that expansion. Part one has expand the
space by doubling it: part two requires multiplying it by a million.

Dynamically altering the input array is completely infeasible due to the space requirements. Therefore, the solution is to mark
the indices of all rows and columns that are empty. Then we construct Point2D objects with the coordinates of all of the galaxies.
Stored in a List, it is easy to iterate with two loop counters to get every unique pair. The Point2D class has a built-in function
to get its Manhattan distance from another Point2D: all that is left is adding the expansion factor.

There is another method that counts the numbers of rows and columns between two points are marked as expanded. It sums up the
number of expansions, then multiplies by the expansion factor minus one to get the total expansion. The minus one is needed to
avoid double-counting the first empty row or column that was counted during the original Manhattan distance calculation.

This was a short and sweet puzzle that was not trivially easy, but certainly not difficult, either. I fully expect difficulty to
ramp up _hard_ by the end of this week.

## Day 12: Hot Springs

[Year 2023, day 12][12.0]

Today's problem is all about pattern matching, but not in a way that regular expressions work.

We are given a series of pattern containing a sequence of three symbols, and each pattern has a corresponding list of integers.
The problem refers to them as gears which can be broken(`#`), working(`.`), or have an unknown status(`?`). We need to match
consecutive ranges of broken gears, accounting for the fact that all known broken gears must match with a range and all ranges
must match with broken or possibly broken gears.

The ultimate goal is to see how many ways a particular gear string can be covered by the gear quantities. This number can get
quite large: well beyond the range of a 32 bit integer for part two.

Part two expands the input by a factor of five, which exponentially increases the match count for some input strings while barely
changing other inputs. Either way, the point of part two is to break algorithms that do not use memoization to find short cuts.

The approach that I took was to search through each line of input recursively, and carry along a map used as a memoization cache.
The input to each recursive call is the remainder of the matching string and size of each group of broken gears remaining that
need to be matched. In the below description, the symbol string is the string while the gear groups are the integer list.

First, start off by computing the cache key. This is the hash code of the current state, being the string and array passed in to
the method. This operation needs to be really fast, because it is used frequently. Then we immediately perform a cache lookup to
see if the current state has been seen before: if so, simply return its value.

Next, inspect the state string. Get the number of unknown gears(`?`), broken gears(`#`), and the number of gears remaining that
need to be matched. We can short cut the logic here by looking for impossible scenarios. If there are more broken gears in the
input string than there are gears in the gear groups, there can be no solutions. Conversely, if there are more gears in the gear
groups left than there are possible matches between the unknown and broken gears, there can also be no solution. In either case,
we can put a zero in the cache and return it.

Moving on, we look for the case where there are no more gears in the gear groups. Since we already checked for there being no
solution, there cannot be anything left to match in the string so this is a success condition. Put a one in the cache and return
it.

Now we move on to the recursive case, but it requires some setup. The idea is to grab a region in the symbol string equal in size
to the next gear group, check it for validity, then consume it and make a recursive call. Then move the region to the right and
keep iterating.

First, we can calculate the final string index that can possibly work based on the gear groups. We can add up their values, then
add one in between each group. If we have an array `[2, 4, 1]` then we can add those values to get `7`. There are two gaps,
equivalent to counting the commas in the list, for a total of `9`. Subtract that from the length of the symbol string to get the
last possible index to search, because the groups will not fit any later in the string. Great, now we have a range of zero to that
last beginning value.

Iterate over each index in the string and perform a series of checks:

* Are there any working gears in the range? If so, this is not a valid region, and skip it.
* Is the start of the region at the start of the string? If not, is the region preceded by a `?` or `.`? If so, this is a valid
  start location.
* Is the end of the region at the end of the string? If not, is the region followed by a `?` or `.`? If so, the end location is
  valid.
* Are there any broken gears, `#`, between the start of the string and the begin offset of the region? If so, we skipped a broken
  gear and this is an invalid region.

Once those checks pass, we are ready for the iterative call. First, we need to find the start index of the substring for the
recursive call. We can start with the end index (exclusive) of the region we just consumed. However, we need to advance one index
because gear regions cannot touch. Furthermore, we can cycle forward past any working gears. While the recursive call will do the
same with its main loop, this actually does speed up the various stream operations used for counting when spread across millions
of calls. It shaves off around 20% of the run time for part two.

Overall, this was an interesting problem. I was able to get the correct answer with minimal fuss, but the code was very ugly. I
spent some time cleaning it up, refactoring for readability, optimizing (and measuring!) to get the run time down from over a
second to around 160ms. But the code broke, and I had so many revisions in my history that fixing it that way was more confusing
than rewriting the algorithm from memory. In the end I got it into good shape, even if I forgot a few edge cases early on. At
least I had the correct answers for the example and real inputs which means unit testing my fixes was trivially easy.

## Day 13: Point of Incidence

[Year 2023, day 13][13.0]

Today's puzzle asks us to find reflection lines in a grid of characters. Each grid has one line, either horizontal or vertical,
over which the grid is mirrored. If one side of the mirror image hits the edge, then any leftovers on the other side are ignored.

Part two adds the requirement that there is a smudge on the mirror which, once corrected, changes the mirror line. It could be
reflected the same direction but in another location, or the direction can change.

There are several ways to approach this, but I opted for one that is quite simple and efficient as well. The algorithm is
identical for both parts, differing only in the input, which is the required number of smudges that _must_ be in the mirror
region. That is, the smudge cannot be in an ignored region due to one part of the mirror hitting the edge of the grid as described
earlier.

Parse the input into a series of 2D integer arrays. For each array, calculate its score. The score method accepts the grid as well
as the required number of smudges.

There are two stages to scoring a grid. Iterate its columns and see if any of them are valid reflections. If so, return the number
of columns to the left of the reflection line which is the array index plus one. If none are found, do the same horizontally
except the score is one hundred times the number of rows above the reflection line, which itself is one plus the array index.

When determining if the grid can be reflected either direction, define two integers that represent the indexes for the reflection
comparison. The first one is the provided index, the second is that index plus one. Compare each row or column, and add up the
number of differences encountered. After each row or column, decrement the first index and increment the second index, and repeat.
If at any time either index grows out of bounds or the number of differences encountered is greater than the required number,
return early with a `false` result indicating it is not a mirror image.

If the comparison succeeds, there is still one thing left to do: we need to verify that the number of differences is equal to the
desired number of differences. This does not matter for part one, but does for part two: if we need one difference but there are
zero, then the smudge was outside of the compared area and this is an invalid reflection.

Overall this was a pleasant, if not short, puzzle to solve. It was not difficult, but there were a few minor issues in the code
that I had to work out along the way. That final detail about having exactly one smudge in the reflection area for part two was
probably the biggest. The code was incorrect, but it provided the correct answer for the example input while providing an
incorrect answer on my real input. There were some other off-by-one type of errors as well, but nothing truly difficult. The
algorithm as a whole jumped out at me when I read the problem, leaving only minor bugs to fix.

## Day 14: Parabolic Reflector Dish

[Year 2023, day 14][14.0]

Finally, we come to a problem where we need to detect a cycle and fast forward past a prohibitively large number of iterations
that will kill performance.

Part one asks us to mutate a grid and calculate a score to ensure the basic operation works. Part two has us perform three more
related operations, then repeat the entire cycle a billion times.

My input saw its first cycle after 143 iterations.

This is fairly simple. Mutate the grid, then store a hash of its state as a key in a hash table with the iteration number as the
value. If we ever see the same hash, then the state is a duplicate: get the previous iteration number and calculate how many
iterations it will take for the iteration count to be congruent mod one billion. Iterate that many times, then score the grid and
return the results.

The only real problem I encountered was again, the example input worked fine but the answer for my real input was wrong. I had a
suspicion, however, that I did not actually miss anything in the algorithm. I added a custom hash algorithm for the grid array,
and the web site accepted my next answer. There was a hash collision with `Arrays.deepHashCode()`.

I implemented the basic Java hash algorithm but modified the prime number multiplier. Using this hash instead of the built-in, it
works fine.

One improvement I would like to implement is generalizing the four shift algorithms, but I am not sure if that is possible given
my approach. For each one I start in the second row or column and try to shift each `O` toward the tilted down edge, then iterate
uphill. Given the array bounds and increment/decrement operators are all different this is likely not possible. However, the code
sure looks and feels almost identical between the four methods. That bugs me. Maybe it is possible with a few lambdas, but the
resulting unreadability might be worse than the current verbosity.

## Day 15: Lens Library

[Year 2023, day 15][15.0]

Today's puzzle asks us to perform some basic hash table operations.

We are given a string that is tokenized by splitting on a comma, and a custom hash algorithm.

In part one, we hash every token in the string and sum the hashes. This validates the implementation of the hash algorithm.

In part two, we use the token as an instruction to perform an operation on a hash table (`Map`/`HashMap` in Java).

The problem statement identifies the core data structure as a series of boxes. Each box is identified by a hash, and contains zero
or more lenses in a specific order. After creating a new lens record to hold each lens along with its focal length, we define the
"series of boxes" as a hash map of integer (hash value) to a list of lenses. Using the hash as the key, which it itself hashed, is
an odd choice in Java because all objects can themselves be hashed and the `HashMap` implementation calls the `hashCode()` method
for us. However, AoC is language-agnostic and we do not have anything other than an integer to identify each element in the map.

Next, we need to split each token into two pieces: a lens ID and an operation. If you thought you could simply use the first two
characters as the lens ID, think again: AoC once again provides real input that is different from the test input in a crucial way.
All of the test data has two-character lens IDs while the real data has varying lengths. Surprise! To handle this, the simplest
and fastest way is simply to scan each token until we find the first non-alphabetic character. Return two substrings using this
index.

Now we can loop over each input and perform the operation. First, we get the map key which is simply the hash code stored in a
boxed `Integer`. If this key is not already in the map, add it along with an empty `ArrayList`.

If this is a `-` or remove operation, we need to remove that lens from the box. We look up the key in the map and iterate the list
until we find the matching lens, then remove it.

Otherwise, this is a `=` or replace operation. Look up the key in the map and iterate the list until we find the matching lens.
Remove it, then add the new lens record at the same location. Note that this code uses `ListIterator` to do this instead of a
regular `Iterator`. If the element was not found, we add the new lens record to the back of the list.

Once we are done manipulating the map, we need to calculate the score. Iterate over all of the map entries. The box number is
simply one plus the map key. Iterating each list, the position in the list, one-indexed, is simply the list index plus one. the
focal length is stored in each lens record. Multiply the box number, list position, and focal length. Add that result to a running
total, which is the answer.

## Day 16: The Floor Will Be Lava

[Year 2023, day 16][16.0]

Today we are given a grid of mirrors, splitters, and empty locations. A beam of light enters the grid and either reflects 90
degrees off a mirror, passes through a parallel splitter, or splits into two beams, both 90 degrees off of a perpendicular
splitter.

In part one, the beam enters the top-left corner of the grid and travels east. We need to figure out how many grid locations the
beam passes over in total. In part two, we need to find the maximum number of energized grid coordinates by changing the beam's
entry point and direction.

First, a little setup. We create a map where the key is a combination of direction and code point, where the code point is one of
the four special characters used in the input: `-`, `|`, `\`, `/`. The value is one of six lists containing the new directions:
lists of one element for each of the four directions, and two lists containing opposite directions for the splits. This way we can
perform a super quick and easy lookup to get the new directions at a decision point, which is the term I used for any of the
special characters, and iterate the one or two new directions (which might not even be new).

The main algorithm sets up three basic data structures.

* Set of energized points in the grid. When the algorithm is done, the answer is simply this set's size.

* Set of points already processed. Elements are a record comprised of the grid location and beam direction. This way we can
  differentiate between several beams hitting the same point. An east beam hitting `\` is different from a west beam hitting the
  same point. A north beam hitting `-` is different from a west beam hitting it, but identical to a south beam hitting it. This
  set therefore contains records of the point and the _outgoing_ directions.

* Queue, containing the beam locations that need to be calculated. At each step we figure out where the beam goes: it either falls
  off the edge of the grid, or it hits a decision point. We then figure out the possible directions at that point. If an outgoing
  direction was not already processed at that point, we add it to the seen set and the queue.

Once all beams are processed then the queue is empty and the main loop ends. We then return the size of the set of energized
points to get the answer.

Part one simply calls the algorithm with a fixed point and direction. Part two loops many times, processing each edge coordinate.
To ensure the corners are handled correctly, there are two loops for iterating `x` and `y`. Each loop processes both edges in
north/south and east/west pairs.

The final part to note is in the code, we need special handling for the beam start. The queue assumes that each element it
contains is a beam _leaving_ a decision point, while the start of the beam might be on an empty grid location (`.`). To ensure the
main part of the algorithm works correctly without any special handling of empty locations, the first part advances the beam to
the first decision point. It energizes points along the way and handles the case where there are no decision points, which is a
valid case in the example input.

## Day 17: Clumsy Crucible

[Year 2023, day 17][17.0]

Today's puzzle has us traverse a grid, keeping score based on the values in the grid, while trying to minimize the score. This is
a classic A\* or Dijkstra's algorithm on the surface, although there is a twist: we are not free to move however we want. Instead,
there is a maximum distance we can travel in a straight line that varies between parts one and two. Part two also introduces a
minimum travel distance before we can turn.

I set this up similar to other puzzles where I used an A\* algorithm. The difference is with tracking the visited nodes: in
addition to mapping the best score for a grid coordinate, we also need to know what direction we were facing as well as how many
steps in that direction we took. These determine future possible moves, meaning that two visits to the same location may not be
the same.

Aside from this there is additional pathfinding logic. A path tracks not only the grid locations on that path, but also its
direction and the number of consecutive moves it made in that direction. These are used to filter allowed moves at each step.

Paths have a weight that determines how good they are, with lower values being better: that is how `PriorityQueue` orders its
elements. The weight is the sum of the distance remaining and the score. The goal here is to take the most promising paths and
explore them fully early on: then we have a baseline temporary answer with which to measure other paths. As other paths find the
target location we can lower that temporary answer until we find the lowest value. Along the way, we can prune really awful paths
early on with a simple heuristic: if a partial path's score cannot possibly beat the current best, discard it. The potential best
score for a partial path is simply its score so far, plus the Manhattan distance to the target which is a highly optimistic route
that assumes a stair step pattern with only values of one. This optimization alone cuts the runtime compared to not using it by
over 98%.

There is another constraint that I missed at first: if we move onto the exit square, we must have moved the minimum number of
steps in order for the path to be valid. Otherwise, we must discard it.

I used the same code for parts one and two. I added parameters for the minimum and maximum travel distance which allows the
algorithm to be identical between the two parts.

Performance is not very good. Part one completes in about half a second, and part two completes in 1.5 seconds. The example input
all completes in milliseconds. I tried a few things to speed this up such as sizing data structures appropriately from the start
which avoids expensive resize and copy operations over hundreds of thousands of objects. That helps, but only so much.

I tried storing the points in a path as a set instead of a list, even going so far as storing the actual hash code instead of the
points. Both of those approaches were worse by an order of magnitude. Apparently, looking for a duplicate in a list is faster than
in a hash table. Or maybe it is faster to populate hundreds of thousands of `ArrayList`s as opposed to `HashSet`s.

After trying a few other optimizations that did not break the algorithm, I was unable to find a way to speed this up. I think the
two most likely time sinks, without having a profiler, are managing the tons of data that it creates and stores, and not pruning
dead end paths in the search tree.

## Day 18: Lavaduct Lagoon

[Year 2023, day 18][18.0]

Earlier during this event, there was discussion around day 10's solution. While I solved it a different way that still worked
well, using a combination of [Pick's theorem][18.1] and the [Shoelace formula][18.2] seemed to be popular.

Today there are not many choices: this was the approach I took. While I do not have a strong grasp of the math behind the
algorithm, I am certainly capable of adapting a Wikipedia article into a computer algorithm.

It is worth noting that a brute force approach such as using flood fill and counting the set elements that were filled is not
viable because there are simply far too many points. There are far more than will fit in a Java array that backs data structures
such as `HashSet` and `ArrayList`. Even if we pick a `TreeSet` or `LinkedList` instead, the space requirements are so vast
that most computers simply cannot store that much data and even if they can, it would take years to find the answer due to all the
overhead. An optimized math approach is required here.

First, we need to iterate the input lines and implement each dig instruction. Start by storing the perimeter of the trench and
the coordinates of each vertex, where coordinates are all relative and the absolute position is irrelevant. I started at origin,
`(0, 0)`. The magnitude of each dig instruction changes based on whether it is part one or part two, but the algorithm is the
same: store the next vertex, add the distance onto the perimeter.

The next step is calculating the shoelace area. For each pair of consecutive vertices, we perform a matrix operation on their
coordinates. Multiply the first vertex's X value by the second vertex's Y value, then subtract the result of multiplying the other
two coordinates. It is worth pointing out that if you use 32 bit integers for coordinates then they should fit, however, they are
large enough that the multiply step can overflow and go negative. I had to cast to `long` here because my `Point2D` class uses
`int` coordinates. Given how ingrained that class is all over this project, converting it to use `long` is problematic due to how
other solvers use it for things such as indexing into arrays which has to use `int`.

Anyway, add the matrix result to a running sum of the area. Once complete, we do not actually have the final area. The value can
be negative depending on the shape and the order of the vertices. The formula says to take the absolute value, so we do. Then we
are given twice the area, so divide by two.

Now we have the internal area and the perimeter, or count of boundary points. Pick's theorem gives us an equation: the total area
is the internal area we just calculated plus half the perimeter, minus one. For some reason I had to add one, not subtract one.
Maybe there was an off-by-one error somewhere else? Regardless, switching subtraction to addition provides correct results for
both inputs and both parts, so the likely case is my code is not counting something that it should be, and is consistent about it.

My code returns all four answers so quickly that the time is basically a rounding error: one millisecond. I am happy with this
result.

## Day 19: Aplenty

[Year 2023, day 19][19.0]

Today was conceptually simple, but difficult to get right. It is similar to [Year 2021, day 22][19.1] and [Year 2023, day 5][19.2]
in ways that I will describe shortly.

We are given the task of accepting or rejecting parts based on various workflows that have rules. Each part has four categories,
essentially four arbitrary integers that need to match the rules. The problem statement does a good job of explaining the flow,
but one of the key insights is seeing that the workflows and rules form a type of decision tree. If a part chooses a patch that
ends in `A` for accept, then it is counted for part one. This is fairly easy to code in a variety of ways.

Part two gives us the requirement that we must map out _all_ of the accept criteria, or at least count them. This cannot be done
using brute force.

After parsing input which itself is a big task this time, we need to figure out what are valid ranges. To do this, we recursively
process the workflows, starting with `in`. At every rule, there are several things that can happen. The input is a mapping of each
part attribute to a range. These ranges are copied and updated during recursion, with the accept step adding the range as it
exists at that time to the returned range maps.

* There is no condition. We either accept the current range (`A`), do nothing but return right away (`R`), or recurse into
  another workflow.

* There is a condition, but the current range is completely in-bounds for the conditional check. This means the alternate path of
  continuing to the next rule in the workflow is impossible. In this case, process the rule as in the first step.

* There is a condition, and the current range is completely out-of-bounds for its check. This means the rule does not matter, and
  we can skip and continue with the next rule in the workflow.

* There is a condition, and the current range is split by it. We need to divide the matching range in two parts. Using the part
  that matches the rule, process the rule as done previously. Using the part that does not match, update the current range and
  continue to the next rule.

In the end, we have a potentially large collection of range maps that tell us every single value that passes the checks.

For part one, we can simply loop over the input parts given and check against the range maps to see if each attribute matches at
least one range map.

For part two, there are some more steps but at least both parts can use the same code so far.

Conceptually, these maps of ranges are really just four-dimensional ranges, that is, [hypercubes][19.3]. The next step to make the
one after easier is to convert these maps into objects of type `Range4D`. This is fairly simple: get the `Range` objects out of
each map, and construct the 4D object.

Now we need to add up the total space taken by these hypercubes, which provides the answer. However, they overlap: this means a
simple algorithm that gets the volume of each and sums them will provide an answer that is too high.

The solution is similar with the previous days I mentioned earlier. Count up the total space, then subtract the intersection
volumes. However, that may subtract too much if three or more hypercubes occupy the same space, so add in the intersections of
those intersections. The algorithm handles this by getting the intersections of the original ranges, then repeatedly summing the
volume of those hypercubes. It flips the sign of the sum each time, and adds it to the total. Then it gets the intersection of the
intersection and repeats until there are no more intersections remaining.

## Day 20: Pulse Propagation

[Year 2023, day 20][20.0]

Today we have a circuit network where we need to find a very long cycle in its output.

For part one we need to wire up the circuit and send a thousand inputs. This triggers modules within the circuit network to
perform various updates and to trigger other downstream modules. Along the way we monitor the number of high and low pulses, which
are essentially the ones and zeros traveling across the wires. The answer comes from multiplying the number of ones and zeros sent
at all of the intermediate steps. This part is fairly simple.

The implementation for part one maintains a queue, and counters for both high and low pulses. We loop one thousand times. Each
time offer a button press to the queue, then process an inner loop until the queue is empty. Poll the queue to get the next action
to perform. Process that action, which may enqueue more actions or not. This ensures that actions are processed in the order they
are generated, per the puzzle's requirements.

Once the queue is empty we start over with the next button press. Once the button presses are exhausted, multiply the low and high
pulse counters for the answer to part one.

Part two asks us to monitor a specific module, `rx`, and determine the number of input button presses required for it to receive a
low pulse. This number is extremely large, easily overflowing a 32 bit integer. Brute forcing the answer by running the simulation
until that condition happens is not feasible.

In this case, we need to inspect the input to determine its structure. One clue that this is a necessary approach is that part two
has no examples. Generally when this is the case, it is true that the input structure holds a major clue to the answer. Revealing
how the example input operates in this case would provide a large hint to the real answer, which is probably why they omit
examples in these cases. Other examples of this include [year 2018, day 21][20.1] and [year 2021, day 24][20.2].

The key insight here is that the input's structure is the broadcaster has four downstream flip flop modules. Each of these have
eleven more downstream flip flops, comprising a twelve-bit register. Each set of flip flops, or registers, has some associated
conjunction modules that help with bit operations as well as output. Each register outputs one signal that eventually meets up
with a common conjunction module, which then connects to the `rx` module.

In other words, the broadcaster contains four integer registers. Each register outputs a signal to `rx` if it contains a specific
value. We need to find the number of button presses until all four registers hold a specific value, unique to each register, at
the same time. These values turn out to be prime numbers, so multiplying their trigger values gives us the answer to part two.

I used the visualization tool [Graphviz Visualizer][20.3] to map out my input. Once I understood the structure, I could see that
certain flip flops point back to a conjunction module that then exits the register on its way to `rx`. I surmised that this means
when the connected flip flops all output a high pulse, or a one bit, that must be the trigger value. Taking the flip flops in
order I manually entered ones and zeros into the Windows calculator app and came up with an even number. Not correct. I flipped
the bits and it was odd, and prime. That was promising. I did the same for the other four registers and had four prime numbers. I
multiplied them together, entered it into the web site, and manually answered the question. My next task was to write code to do
it programmatically.

The algorithm I came up with does not assume much other than the general structure. There could be three registers, or five, and
it will work. The same is true with the number of bits. The input has twelve, but it will work with any number.

We start by finding the children of the broadcaster. Using Java stream operations we map each of those to a `long` and find the
lowest common multiple. Simply multiplying them works because the numbers are prime, but I did not want to assume that.

To map a module to a `long`, we delegate to a method I wrote that walks the register and inspects its connections. Given the ID
of the first module, look it up. Then we enter the main loop that walks all of the downstream flip flops.

First things first, we need to maintain a `long` containing the result of the operation that gets built up in the loop. We also
need a variable that contains the current bit which will be added to the result whenever a bit needs to be set. This bit is left
shifted each iteration so it always represents the next bit to add.

Inside the loop, see if the current module has any downstream conjunction modules. If so, we need to set this bit by adding the
current bit value to the result. Next, we get the current module's downstream flip flop register and set it as the current module
before looping back to the top. We have to be careful because the final flip flop will only have a conjunction downstream: we need
to be sure to set the current module to null instead of throwing an exception. Once null, that breaks the loop and we can return
the value of this register.

Overall this was an interesting problem that I was expecting around this time of the event. As mentioned previously, this is not
the first time Advent of Code has had a problem like this but this year's version was a bit less frustrating than in some previous
years. Previous puzzles similar to this one have sometimes required a bit of manual digging into the input to find patterns, while
this version's pattern popped out as soon as I visualized the module graph.

## Day 21: Step Counter

[Year 2023, day 21][21.0]

Today we get a puzzle about navigating a grid and determining how many unique endpoints there are if we walk a certain number of
steps away from origin, with backtracking allowed.

Part one has us walk a small number of steps that easily fit inside the grid. A simple recursion or queue-based algorithm works
fine. For part two we have an absolutely huge number of steps that is not feasible to calculate using brute force. However, I was
able to reuse my part one solution as part of part two.

The key here is in the structure of the input, specifically, the real input as opposed to the example input. There an empty border
around the grid, which is also true of the example input. Unlike the example input, however, there is a clear path from the center
start location to the edges of the grid. This allows us to make some assumptions and divide the problem up into smaller pieces
which can then be stitched back together, greatly reducing the solution time compared to brute force.

If we imagine the [tessellation][21.1] created by repeating the grid in all directions, we can see there are a bunch of repeating
open lanes that form a grid pattern. This means that rather than plowing through the middle of the grid pattern which has
obstructions, we can reach the edges of the reachable area in a constant and easily computable number of steps.

In other words, we can divide this plane into pieces that are the program input. Many of these are interior pieces which can be
completely explored, and the pieces at the edges have a known number of steps required to enter their edges. From here we can see
that there are different "types" of grid pieces in the tessellation, and it helps to draw this out on a piece of graph paper. I
will go over these types below.

Another important issue to consider is cardinality. While we can reenter squares we already entered before, we have to walk a
specific number of steps as defined in the puzzle. Consider the starting square. If we are given an even number of steps, then we
can end up at the start by walking forward one, then backward one, and repeating until we have the total number of steps. Since it
takes two moves to end up back at the start and there are an even number of steps, that is guaranteed. Conversely, the puzzle
input for part two gives us an odd number of steps. It is impossible to end up at the start. Since we enter the starting tile with
an odd number of steps remaining, we say the starting tile has odd [cardinality][21.2].

Each tile is a square 131 units on each side, and we start in the middle. If we walk the tile length, 131, in one direction, we
end up at a copy of the starting square but with an even number of steps remaining. The total steps is odd, we walked an odd
number of steps to get there, and have an even number remaining. Since we could walk back and forth as in the example above, this
tile has even cardinality.

Imagine a board for chess or checkers with alternating colors. Each color represents either even or odd cardinality. That is what
we have in this problem.

The next piece of the puzzle's answer comes from the size of the input grid compared to the number of steps. The specific numbers
are not too important, but they have important properties:

* The step count is odd, meaning the starting square has odd cardinality.
* This is an exercise in moving along a grid, which is [taxicab geometry][21.3]. For a fixed distance, this means the resultant
  shape would be a diamond if we ignore the rocks (obstacles) in the way.
* The remainder of dividing the step count by the grid size, the [modulo][21.4], is the distance from the center of the input grid
  to its edge. Put another way, it is the result of integer division of the grid length by either coordinate of the starting
  location.
* Dividing the step count by the grid size gives us an even number. Combined with the remainder above, this means the tips of this
  diamond will exactly lie on an outer edge of a repeat of the square, and that square will have odd cardinality.

We can picture this using a much smaller board, like this:

    +---+---+-^-+---+---+
    |   |   |/ \|   |   |
    |   |   /   \   |   |
    |   |  /|   |\  |   |
    +---+-/-+---+-\-+---+
    |   |/  |   |  \|   |
    |   /   |   |   \   |
    |  /|   |   |   |\  |
    +-/-+---+---+---+-\-+
    |/  |   |   |   |  \|
    <   |   | S |   |   >
    |\  |   |   |   |  /|
    +-\-+---+---+---+-/-+
    |  \|   |   |   |/  |
    |   \   |   |   /   |
    |   |\  |   |  /|   |
    +---+-\-+---+-/-+---+
    |   |  \|   |/  |   |
    |   |   \   /   |   |
    |   |   |\ /|   |   |
    +---+---+-V-+---+---+

Given no obstructions, the valid endpoints of a path walked will be the points within those boundaries that have the proper
cardinality. Let us mark the cardinality next:

    +---+---+-^-+---+---+
    |   |   |/ \|   |   |
    |   | E / O \ E |   |
    |   |  /|   |\  |   |
    +---+-/-+---+-\-+---+
    |   |/  |   |  \|   |
    | E / O | E | O \ E |
    |  /|   |   |   |\  |
    +-/-+---+---+---+-\-+
    |/  |   |   |   |  \|
    < O | E | O | E | O >
    |\  |   |   |   |  /|
    +-\-+---+---+---+-/-+
    |  \|   |   |   |/  |
    | E \ O | E | O / E |
    |   |\  |   |  /|   |
    +---+-\-+---+-/-+---+
    |   |  \|   |/  |   |
    |   | E \ O / E |   |
    |   |   |\ /|   |   |
    +---+---+-V-+---+---+

There are a few calculations to perform both in finding the count of valid points in each type of square, as well as the count of
each type of square.

First, there are several types of squares:

* Internal squares of odd cardinality.
* Internal squares of even cardinality.
* Cardinal squares are the tips of the diamond, one in each of the four primary [cardinal directions][21.5].
* Edge squares that contain a small number of points. In the above diagram, these are edges with even cardinality.
* Edge squares that contain a large number of points. In the above diagram, these are edges with odd cardinality.

We need to count the endpoints in each of these types of squares, keeping in mind that the real input has obstructions which means
the size of e.g. the north point will likely be different from the south point. However, square types that are repeated have
identical counts of endpoints. For example: all interior odd squares have the same value. All small edges on the northeast edge
also share a common value.

Before calculating the values of each type of square, we need to determine the starting point for the count and the number of
steps required to get to that start point. Once we have that info, we can run the main algorithm to count the valid endpoints and
feed it into a large expression that gets the final answer. Roughly following the code, the algorithm to count each square type
looks like this:

* We get the value for internal odd squares by simply counting endpoints from the start location with zero steps taken.
* The value for internal even squares works similar, except we lie and say we already moved one square.
* Next, we observe that the shortest path to a square with a tip of the diamond is to move a number of steps equal to the step
  target minus the length of the square. However, this would put us just outside the desired square, so we add one to avoid
  over-counting.
* Use that step count as one input to calling the function to count the endpoints four times. We pass in four starting points that
  are the midpoints of each side of the square.
* Picture the top cardinal square. To get to the small edge to its west, we need to travel a number of steps equal to half the
  square length plus one. This is true for any small edge, due to taxicab geometry.
* Given the step count to get to the small edge in the previous point, the large edge to its south is exactly one square length
  closer to origin so we can store the step count for that by subtracting the square length from the previous value.
* Now make a bunch of calls to count endpoints. We pass in every combination of corner of the square and step count for small and
  large edges, eight calls in total.

The next step is to calculate the answer to the problem by combining these values. We need to know how many of each type of square
is present given `x`, the result of dividing the step target by the square size using integer math. That gives us the number of
squares in each direction, ignoring the remainder. The example I provided earlier has `x = 2`.

* Interior even and odd squares: when `x = 1`, there is a single even square and zero odd. If we increase to `x = 2`, we see the
  center square flips cardinality and we are given a `+` shape with an odd in the center surrounded by even squares. If we keep
  increasing `x`, we see that the stair-step diamond shape keeps increasing. Cardinality flips, and a new layer is added on the
  outside such that every square only borders squares of a different cardinality.

  The sequence of interior squares is 1, 5, 13, 25... which is the sequence of [sums of two consecutive squares][21.6]. In fact if
  we look at the sequence of the two cardinalities we see that they are both perfect squares, and the number being squared differs
  by one with the even cardinality being the greater of the two. In terms of `x`, we see there are `x^2` even squares and
  `(x-1)^2` odd squares.

* There is one of each cardinal square, that is, one of each tip of the diamond.

* Inspecting each edge individually, we see a pattern where there is a small square next to each cardinal square. Then the edge
  alternates between small and large edges such that if there are `n` small edges, there are `n - 1` large edges which are always
  sandwiched between two small edges.

  Consider the northwest edge, viewing it horizontally. The top small edge has a cardinal square to its east. This small edge will
  always be present, even if `x = 1`. If we increase `x` by one, then we add a pair of edges beneath it: a large edge to its
  south, and another small edge to its south-west. This means the number of small edges is always equal to `x`, and the number of
  large edges is equal to `x - 1` which agrees with the assessment I made prior.
  
  We do have to be careful to keep in mind that this is only one edge, however. Edges along each side are also different from each
  other. Since the quantity of each edge type is the same, we can simply add up each type of small edge and each type of large
  edge. Then we multiply the small edge sum by `x` and the large edge sum by `x - 1`.

The final calculation can be viewed in the code, but it looks like this in pseudocode:

`total endpoints = even * x^2 + odd * (x-1)^2 + north + south + east + west + small * x + large * (x - 1)`

I have not tested this code under different assumptions such as the square size being odd, the remainder of the step count divided
by the square length not being half the square length, or the cardinalities being flipped. Some of those may work, some may not.

The code for part two also has no expectation of working for the example input since it lacks the clear path properties of the
real input. Apparently I was not the only person to notice this, so I do expect it to work for other real inputs.

## Day 22: Sand Slabs

[Year 2023, day 22][22.0]

Today's puzzle constructs a [Jenga-like pile of bricks][22.1]. We are given a snapshot of the bricks in mid-air as they fall. The
first task is to map where they land, taking into consideration that they maintain their orientation and do not turn. Then we need
to calculate how many are safe to remove for part one, where a brick is safe to remove if every brick directly above it have at
least one other brick on which they rest. For part two we need to calculate, for each brick, how many bricks above it would fall
if that brick were removed. Then find the sum of this calculation for all bricks.

Most of the work here is done in the input processing. We read in the file and parse each line into a new brick class that wraps
the Range3D class with references to bricks above and below the brick.

Next, we need to make the bricks fall. We start by sorting the input: the brick class is [comparable][22.2] using the Z1 value of
its range. This ensures that after sorting, bricks are in ascending order based on their lowest point. This way they maintain
their order as they fall. To determine how far each brick must fall, we construct a two-dimensional array large enough to contain
all X and Y values present in the bricks: the largest of each X2 and Y2 value, plus one because arrays are zero-indexed. This is a
height map: the value in each array cell is the current largest Z-value at that location.

We look at each brick in turn, and compare its Z1 value against the largest height in the region of the height map corresponding
to the brick's X and Y range values. Then we know how far the brick will fall. Update the list of bricks by removing the old one
and replacing it with a new one shifted down. Then update the height map with the brick's Z2 values across its X and Y ranges.

The final step to processing input is to walk the list again. For each brick, compare against bricks earlier in the list. If the
earlier brick directly supports the current brick, establish the above and below relationship by adding the bricks to each other's
sets of above and below bricks. This is a critical step that makes calculating answers to the puzzle a lot simpler and faster.

For part one, we walk the list of bricks. For each brick, check all of the other bricks in its above set. If each of those higher
bricks has more than one below it, then the current brick is safe to remove.

For part two, we again walk the list of bricks. For each brick, we create a set of removed bricks and a queue of bricks to
process. Start by adding the current brick to both collections. Then loop on the queue until it is empty. Poll the next brick from
the queue, and loop over the bricks in its above set. For each above brick, check if all of its below bricks are in the removed
set. If so, it will fall: add that above brick to the removed set and put it in the queue.

Once the queue is empty, the number of bricks that would fall is simply the size of the removed set minus one, since we do not
count the brick that is manually removed.

Part two is a little slow but well within my self-imposed time limit of one second. There is probably some memoization that could
speed it up, but it is not simple and my attempts broke the code. I may revisit this later.

## Day 23: A Long Walk

[Year 2023, day 23][23.0]

Today's puzzle asks us to find the longest path through a maze, but with a catch: certain segments of a path may or may not be
allowed. In part one, certain paths are restricted due to having slopes. By the time part two rolls around, we apparently learned
how to climb.

My solution starts by parsing the input maze into a graph. Step one is to find all intersections: in addition to the start and end
points, these are interior points that have more than two adjacent open squares. In other words, these are decision points, which
makes them [nodes][23.1] in the graph. The next step is to find all edges between nodes. Starting at each node, search in all four
directions. For every valid direction, follow that path until encountering another node. Along the way, look for slopes: if there
is a slope and it goes the wrong way, then this edge is restricted. That means it cannot be used for part one, but can be for part
two.

Once parsing is done, perform a basic [recursive depth-first search algorithm][23.2]. Return zero for any invalid path and the
path length for a valid path. At each recursive step return the maximum encountered value.

This takes a while to run on the real input for part two. Unfortunately there are simply a large number of search trees and the
nature of trying to find the _most_ expensive route makes heuristics difficult to define and use. The only optimization I found
that worked was pruning edges from the penultimate node that do not go to the end. The end is only reachable by one interior node,
so once we reach that node, no other path can succeed. While this did help, the improvement was very modest and only shaved off
around 6% of the runtime.

## Day 24: Never Tell Me The Odds

[Year 2023, day 24][24.0]

This was a tough one, probably one of the most difficult of any Advent of Code. We are given a storm full of hailstones with
locations and velocities in three dimensions. For part one, we need to find which hailstones will hit each other within a given
range in two dimensions: we ignore the Z coordinate and velocity entirely for part one.

Brute force works fine here, although we need to use Java's `BigDecimal` because some lines intercept each other at fractional
coordinates. For each hailstone I convert it to [slope-intercept form][24.1], then perform a basic exhaustive cross-check double
loop where it cuts out half the search space by only checking unique combinations of hailstones.

For each pair of hailstones, check for collision:

* If the lines have the same slope, they are parallel and never collide: ignore this pair and continue with the next.
* Calculate the value of X where the lines intersect.
* If X is outside of the search window: ignore this pair and continue with the next.
* Calculate the value of Y given X.
* If Y is outside of the search window: ignore this pair and continue with the next.
* Check that the X/Y intersection is in the future for each hailstone. While we treat their paths as lines, they actually only
  extend in one direction with a specific start location. If either hailstone has the intersection in its past, ignore this pair.
* Finally, increment the answer counter.

We ignore lines that are vertical or parallel lines that overlap entirely. These are tacit assumptions in the puzzle given the
structure of the input data.

Part two is a bit more involved and I lack the fundamental understanding of the underlying math to solve this using matrices or
linear algebra. However, there is a more layman-friendly algorithm that works given the assumptions in the puzzle.

We are given that we need to throw a rock and it needs to intersect each hailstone in turn. In other words, we need to find a line
that intersects all of the hailstones' lines. Furthermore, we know there is a specific velocity that will intersect them in turn,
where the rock and each hailstone will occupy the same integer coordinates.

This means we can fit a line to any two hailstones: I pick the first two. Next, assume the rock stands still: we need to find
values by which we can alter the first two hailstones' velocities such that they will hit the rock. Put another way,
[we change our inertial reference frame][24.2] to be the rock.

At first, look in two dimensions. If both hailstones hit the rock in two dimensions, then calculate the position of the rock based
on the time taken to collide. We can do this because we know the starting point of a hailstone, and how long it takes to hit the
rock. Working backwards, we know how long it takes the rock to travel to that collision location. Given we are iterating over the
rock's potential velocities, we can multiply those by time and subtract from the hailstone's location to find the rock's starting
location.

Next, loop over _all_ of the hailstones. Calculate the time until the rock collides with one of the hailstone's coordinates. Then
check that it collides with all three coordinates. This is done in a stream operation using `allMatch(Predicate)`. This means that
the operation fails as soon as one hailstone fails the check.

Once all hailstones pass the predicate test, we have the answer: add the rock's coordinates with each other and return the answer.

The final piece of the puzzle is pruning the search space of rock velocities. This took trial and error to find ranges that worked
well for all three dimensions for both example and real input, but eventually I found a range that produces the correct answers
and ran in a reasonable amount of time.

There are better solutions out there, but this will suffice for now.

## Day 25: Snowverload

[Year 2023, day 25][25.0]

The final puzzle is a graph [minimum cut][25.1] problem. We need to find three edges in a graph that, when removed, split the
graph into two disjoint sets of vertices. This is a difficult problem, in fact, it is [NP-hard][25.2].

Thankfully, the [JGraphT library][25.3] exists which trivializes this problem. The code is quite simple: parse the input into a
graph object, then instantiate a helper object which finds the minimum cut. Get that minimum cut, do the math, out pops the
answer.

[1.0]: https://adventofcode.com/2023/day/1
[2.0]: https://adventofcode.com/2023/day/2
[3.0]: https://adventofcode.com/2023/day/3
[4.0]: https://adventofcode.com/2023/day/4
[5.0]: https://adventofcode.com/2023/day/5
[6.0]: https://adventofcode.com/2023/day/6
[7.0]: https://adventofcode.com/2023/day/7
[8.0]: https://adventofcode.com/2023/day/8
[9.0]: https://adventofcode.com/2023/day/9
[10.0]: https://adventofcode.com/2023/day/10
[10.1]: https://en.wikipedia.org/wiki/Even%E2%80%93odd_rule
[11.0]: https://adventofcode.com/2023/day/11
[12.0]: https://adventofcode.com/2023/day/12
[13.0]: https://adventofcode.com/2023/day/13
[14.0]: https://adventofcode.com/2023/day/14
[15.0]: https://adventofcode.com/2023/day/15
[16.0]: https://adventofcode.com/2023/day/16
[17.0]: https://adventofcode.com/2023/day/17
[18.0]: https://adventofcode.com/2023/day/18
[18.1]: https://en.wikipedia.org/wiki/Pick's_theorem
[18.2]: https://en.wikipedia.org/wiki/Shoelace_formula
[19.0]: https://adventofcode.com/2023/day/19
[19.1]: https://adventofcode.com/2021/day/22
[19.2]: https://adventofcode.com/2023/day/5
[19.3]: https://en.wikipedia.org/wiki/Hypercube
[20.0]: https://adventofcode.com/2023/day/20
[20.1]: https://adventofcode.com/2018/day/21
[20.2]: https://adventofcode.com/2021/day/24
[20.3]: https://www.devtoolsdaily.com/graphviz/
[21.0]: https://adventofcode.com/2023/day/21
[21.1]: https://en.wikipedia.org/wiki/Tessellation
[21.2]: https://en.wikipedia.org/wiki/Cardinality
[21.3]: https://en.wikipedia.org/wiki/Taxicab_geometry
[21.4]: https://en.wikipedia.org/wiki/Modular_arithmetic
[21.5]: https://en.wikipedia.org/wiki/Cardinal_direction
[21.6]: https://oeis.org/A001844
[22.0]: https://adventofcode.com/2023/day/22
[22.1]: https://en.wikipedia.org/wiki/Jenga
[22.2]: https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Comparable.html
[23.0]: https://adventofcode.com/2023/day/23
[23.1]: https://en.wikipedia.org/wiki/Node_(computer_science)
[23.2]: https://en.wikipedia.org/wiki/Depth-first_search
[24.0]: https://adventofcode.com/2023/day/24
[24.1]: https://en.wikipedia.org/wiki/Linear_equation
[24.2]: https://en.wikipedia.org/wiki/Special_relativity
[25.0]: https://adventofcode.com/2023/day/25
[25.1]: https://en.wikipedia.org/wiki/Minimum_cut
[25.2]: https://en.wikipedia.org/wiki/NP-hardness
[25.3]: https://jgrapht.org/
