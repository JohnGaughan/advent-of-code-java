# Year 2025: Decorating the North Pole

## Day 1: Secret Entrance

[Year 2025, day 1][1.0]

This year's first puzzle was a little more tricky than most: while simple, it has some edge cases which proved to require some
extra thought. Specifically, Java's handling of negative modulus always surprises me because I always forget it until it becomes
an issue.

The solution I came up with was to have common methods for parsing and running each round, with the logic to count the number of
clicks separate for each part. Starting at the given value of 50, consider each line of text. Translate the string so if it starts
with `L` it is negative, else positive. Add this value to the previous value, and call a function to count the number of clicks
which varies between parts one and two. Then normalize the new value to be between zero and the dial size and repeat using the
next input line. Note that throughout this explanation I use the term "dial size" which is `100` in the problem text. This is a
constant in the code, so I extend that to here.

Part one's code is trivially easy: if the new number for the value of the dial is a multiple of the dial size including zero, then
that is one click. Otherwise, it is zero clicks. We never need to consider multiple rotations of the dial, and by extension, the
previous value.

Part two gets a little more tricky due to edge cases but thankfully the code simplifies a bit. The issue is with counting multiple
rotations, and the fact that simple integer division will not tell us the number of rotations in all cases.

The easy case is a right rotation. Integer division of the next value by the dial size _will_ tell us the number of rotations for
two reasons. First, it is impossible to rotate right and get to zero (before normalization), and no other multiple of the dial
size is a valid previous value so we do not need to check for double-counting a rotation.

Left rotations have more edge cases but they simplify. If the previous value was zero, then we can simply divide the next value by
the dial size and negate the result to make it positive. This is because we do _not_ want to count zero, only other multiples of
the dial size. For example: if we end up on `-250`, then we count `-200` and `-100` but not `0` which gives us two clicks. Since
truncation of integer division ends up rounding toward zero, this is what we need.

Otherwise, we need to count that first zero but there are three cases here.

First, the next value is positive which means zero clicks. Second, the next value is zero which means one click. Third, it is
negative which could mean one or more clicks.

The math ends up as subtracting the next value from the dial size, then divide that quantity by the dial size.

If the next value is positive then it must be smaller than the dial size: this then takes a small number and divides it by a
larger number which results in zero in computing (but not math class).

If the next value is zero then this results in one number divided by itself, which is one: this counts that first (and only) zero.

Else, the next value is negative. Subtracting a negative is adding, so this ends up increasing the numerator. This can wrap around
many times to some multiple of the denominator, resulting in multiple clicks.

I found this to be tricky to get right because of a combination of logic, trying to handle edge cases with zeros, and wrestling
with the semantics of integer division and modulus. Java provides methods on the `Math` class to truncate toward either positive
or negative infinity instead of zero for division, and methods to manage the sign for modulus when one or both arguments are
negative. This is because modulus is a bit ambiguous with signs in computing: different platforms and programming languages have
different semantics. In my opinion, Java chose poorly. This meant a lot of trial and error late at night.

## Day 2: Gift Shop

[Year 2025, day 2][2.0]

This was another puzzle I found a little bit tricky. Not complicated like some of the puzzles that dig deep into esoteric computer
science algorithms, but maybe a little unclear. Turns out all we need do is determine if a string consists only of repeated
substrings. Not contains repeats, but is _only_ repeated strings.

Part one matches strings where it consists of a single substring occurring twice. This means we can find the center of the string
and compare the two halves which is trivially easy.

Part two allows two or more occurrences, which is slightly more complicated.

The easy approach is to use regular expressions. While the strings contain numbers, we are not performing math on them during the
matching phase so we can ignore the type of string contents. The following regular expressions will work for each part. Note that
they already contain necessary escapes for Java strings:

* Part one: `"^(.+)\\1{1}$"`
* Part two: `"^(.+)\\1+$"`

While this works, I wanted a more efficient approach. I like regular expressions for certain real-world tasks but not for
programming exercises or competitions. They can trivialize the solution and often perform poorly.

My solution for part one is simple. If the input string has an odd length, it is valid. Otherwise, find the midpoint of the string
then compare the two halves. If they are identical, the string is invalid.

Part two iterates token lengths starting at one and ending at half the string's length. For each token length check if its length
evenly divides the input string's length. If it does, then get a substring of the token length from the start of the string. Check
if the token is repeated throughout the string: if so, it is invalid. Fail fast as soon as a mismatch is found and iterate to the
next token length. If no repeats are found, then the string is valid.

This is about half the speed of regular expressions and is a bit more satisfying to implement.

## Day 3: Lobby

[Year 2025, day 3][3.0]

I found day three to be somewhat easy. Given an array of integers, find the largest number that can be formed by picking two or
twelve of them and reading their digits in order: do not pick the largest numbers and sort big to small. Instead, consider their
relative positions in the array when interpreting them.

My intuition from part one was two-part. First, we want to start the two-digit number with the largest possible tens digit.
Ninety-something will always be larger than eighty-something. Second, if there are two occurrences of the same largest digit in
the array, always pick the first one. That provides more opportunities for the one's digit to be larger. Consider `9891`. If we
pick the second `9` then that is sub-optimal because it is impossible to use `8` as the one's digit.

This intuition turned out correct, and I coded up a nested loop to find the answer. Then I got to part two, where we need twelve
digits.

I was not about to copy and paste code with that many nested loops. Could recursion work here? Of course. It still has the same
nested loop to find the first occurrence of the largest digit, but instead of stopping before the final array element, stop just
before consuming too many digits to form a valid solution. That number of digits changes at each recursive step. At first we need
to leave eleven digits. Once we find the first digit we then recurse with two parameter changes. First, we add the newly-consumed
digit to the array containing used indices. Second, we reduce the number of needed digits by one.

Through some quick program analysis I found that once the algorithm finds that first largest digit, it never keeps searching at
that level. That really is the optimal choice, and can at best be tied by an occurrence later in the array which does not matter.
In that way this recursive algorithm is really just a way to nest many loops together. Generally, we use recursive algorithms for
exponential algorithms such as `O(2^n)`. However, this recursive algorithm is pretty much `O(n^2)`, or maybe `O(m*n^2)` if someone
wants to factor in the number of digits needed and also wants to stretch Big-O notation slightly.

## Day 4: Printing Department

[Year 2025, day 4][4.0]

Today's puzzle was another simple one. We need to find all grid locations that have fewer than four neighbors and return that
quantity of locations. This is it for part one. For part two, we continually remove those locations then repeat until we reach a
point where the algorithm makes no further updates. Then return the total number of locations removed.

This is a really simple algorithm. The meat of it is in a common method that walks the 2D array and returns a set of points that
match the puzzle criteria. Part one runs this once and returns the size of that set. Part two adds the size of that set into a
running total then iterates the set, updating the grid to set those point locations to be blank. Then it repeats until the set is
empty. It really is that simple.

## Day 5: Cafeteria

[Year 2025, day 5][5.0]

This was an easy one, thanks to utility code I wrote years ago. First, we parse the input into an array of primitive `long`s and
list of the utility class `LongRange`.

For part one, we simply stream the `long[]`, filter on which ones are fresh, and return the size of that stream. The `fresh()`
method is quite simple: given an ingredient (`long`) and the ranges of freshness, iterate over those ranges and if it is contained
in any of them return `true`. Otherwise, return `false`.

Part two is even simpler. The `LongRange` class contains a static method to merge a `List<LongRange>` such that the returned list
of ranges are all disjoint. Call that method, stream it, convert each range into its size, then sum the stream elements.

## Day 6: Trash Compactor

[Year 2025, day 6][6.0]

The algorithm for today is trivially easy: stream problems, reduce them to a single number, then sum those numbers.

The difficult part was parsing the input correctly without throwing any exceptions.

## Day 7: Laboratories

[Year 2025, day 7][7.0]

This was another relatively simple one. I represent the input by recording the start location of the beam and a list of integer
arrays representing all of the splitter locations. The parsing algorithm ignores empty lines which is not strictly required but
might shave a millisecond off the run time.

Both parts use the same algorithm, but return different data. The algorithm works by tracking a map where the key is a beam's
location and the value is how many beams are at that location. Another way to think about the value is it answers the question
"how many ways can a beam reach this location?"

It then iterates over each line of splitters, building a new beam mapping as it goes which represents the beam state _after_ the
current row. For each beam, see if it hit a splitter.

* If it did hit a splitter, then add two new beams to the left and right of the current location. Increment the split counter.
* Otherwise, it is not split: add the current location to the new mapping.

In both instances we cannot simply add the new map entry: we must merge it because there may already be a beam at that location
which was added in a previous loop iteration.

Finally, the loop replaces the old beam mapping with the new one built up during the loop.

When the input is exhausted we return both the split counter as well as the sum of all values in the map.

## Day 8: Playground

[Year 2025, day 8][8.0]

This was a fun one. Today's puzzle has us building a graph piece by piece then answering a question based on its end state. At
first it may be tempting to build an actual graph in code, but this is not necessary and can slow the runtime due to inefficiency.

All we really need to know is which vertices belong to which graph. We never actually need to traverse the graph. To this end, my
code parses the input and builds lists of both vertices and potential edges, but does not actually add those edges.

Next, build graphs where each graph contains one vertex and is identified by a unique value: I chose an increasing integer for
simplicity but anything could be used. There are two maps here to make merging easier later on:

* Map of each graph ID to a set of vertices in that graph: at the start each set contains one vertex.
* Map of each vertex to its graph ID.

The main loop handles both parts by dynamically limiting the number of edges it adds. For part one there is a limit low enough
that the algorithm cannot finish merging the subgraphs into a single graph. In actuality, it cannot even slim it down to three
subgraphs. For part two there is effectively no limit.

During the loop we iterate the edges sorted from lowest distance to highest distance. If the vertices in the edge are on disjoint
graphs, we merge the graphs. I arbitrarily picked `p2` as belonging to the graph that gets merged into `p1`'s graph. Get `p2`'s
graph, add all of its vertices to `p1`'s graph. Then update the second map by setting the graph ID for the old graph's vertices to
their new graph ID.

Check if there is only a single graph remaining. This is easy because the map of graph IDs to sets of vertices will have size of
one. If so, multiply the X coordinates of both vertices on the current edge and return that value as the answer.

If the loop terminates due to hitting the edge limit, then there must be at least three subgraphs. Do some stream logic to convert
each graph to its size, sort high to low, and multiply the first three elements to get the answer for part one.

Turns out that I figured out [Kruskal's algorithm][8.1] when I compared my solution to the ones in the daily reddit thread. Neat.

## Day 9: Movie Theater

[Year 2025, day 9][9.0]

Part one was fairly simple today: simply consider each pair of points, calculate the 2D area between them, and compare to the
current maximum area. If greater, set the maximum area to the new area. Repeat until all pairs of points are exhausted, then
return the maximum area found.

Part two is a bit tricker due to the constraints. The input points trace the perimeter of a polygon where all angles are right
angles: alternatively, all perimeter segments are either horizontal or vertical. Rectangles must be contained within the polygon,
where the two defining corners must still be vertices of the polygon itself.

I assumed there would be some neat mathematical algorithm to figure this out similar to [year 2023, day 18][9.1]. I browsed
Wikipedia for a bit but nothing jumped out at me.

The issue I saw was the size of this polygon is massive: many billions of integer points. Modeling this a naïve way such as
flood-filling the polygon full of point objects would not be practical. Even figuring out which side of the polygon is its
interior and exterior would be a non-trivial task.

In the absence of a documented (and known to me) algorithm to make this easy, I had to figure it out. Then it clicked. If a
rectangle is wholly-contained in the polygon, then no polygon perimeter segments can cross the rectangle's perimeter segments.
Knowing which side of the polygon is its interior is irrelevant since what we care about is the rectangle crossing between
interior and exterior. For that to happen we must have an intersection of line segments belonging to the rectangle and the big
polygon.

I was able to expand my part one solution by adding an additional test. First, before we even consider rectangle areas, we connect
all adjacent vertices of the polygon into segments being careful to add the final segment that wraps back around to the start. To
make this simple we can have a single class representing a polygon segment containing two each of X and Y coordinates where one of
the pairs of coordinates will be the same: either X1 = X2 or Y1 = Y2. I was also careful to order them so the first coordinates is
always equal to less than the second.

Before considering area, first check that the rectangle formed from each pair of points is valid for all polygon segments. This
checks that the polygon segment is either wholly outside of the rectangle or the two perimeters touch. This means the rectangle
does not cross the polygon's perimeter.

This is an incomplete test given a single segment: it is still possible given some goofy-looking polygons that the rectangle could
still be outside the polygon. However, when this condition is true for _all_ segments on the polygon perimeter _also_ given that
two vertices of the rectangle are shared with the rectangle, that nearly guarantees the rectangle does not have any portion
outside the polygon.

Note that I said "nearly." It is a very strong metric but is not a mathematical guarantee. Consider a polygon shaped like the
letter `C` or `J`, or some other shape that has a long, thin, curved shape with two points sticking out into space. There could be
a large rectangle between those points that is wholly _outside_ of the polygon that is larger than any rectangle contained inside
the polygon. However, that does not appear to be the case in the input provided for this puzzle. This algorithm will fail on that
hypothetical input.

This algorithm runs very quickly, on the order of 100 ms.

## Day 10: Factory

[Year 2025, day 10][10.0]

This was interesting and frustrating.

Part one was easy enough. It makes no sense to press a button more than once, so a simple breadth-first search where we queue up
button states in order of fewest to most button presses will easily find the answer. If there is a tie, it does not matter: we do
not need the actual button press state, only the fewest number of buttons pressed.

Part two is simply not feasible with any BFS or DFS search algorithm. There are far too many states. Instead, it is simple to
represent each machine as a linear algebra problem where each machine can be represented as a system of linear equations like so:

* 3 = b4 + b5
* 5 = b1 + b5
* 4 = b2 + b3 + b4
* 7 = b0 + b1 + b3

This is the translation of the first example input into a set of equations. The issue is there are many equations, some of which
are far more complicated and they can have multiple solutions. Writing a bespoke algorithm for this is possible and I may tackle
that in the future as I learn more about this topic, but for now, I opted to use the Z3 library which does have Java bindings.

The most difficult task here was not figuring out how to represent each system of equations, but wrestling with the Z3 Java API
which is poorly documented. There is Z3 documentation and some of it is quite good: the problem is that most of it is in Python or
other languages that have either weak or flexible type systems combined with some subtle differences in the API.

I managed to figure this out and attempted to document my code a bit better than what I had to deal with so hopefully this helps
someone else who tries to do the same thing.

## Day 11: Reactor

[Year 2025, day 11][11.0]

Today is a graph puzzle where we need to count the number of paths between specific nodes in a directed graph.

Part one asks us to find the number of paths between only two nodes, a simple case. Before proceeding, I decided to view a
graphical representation of the graph in [GraphViz][11.1] to get a general idea of the characteristics of the graph. It appears
there are no cycles, or loops, which helps simplify logic quite a bit. However, there are many routes between `you` and `out`
despite them appearing "close" in the graph.

Given the complexity of the graph I thought [DFS][11.2] might take too long even with [memoization][11.3], so I instead built a
bottom-up algorithm that calculates the count of routes from an interior node to the end node. First, assign a route count of one
to the end node. While the start node has no route count, loop over all of the nodes. If a node's targets all have route counts
but it does not then add up its children's route counts and assign that sum to the node.

Once the loop breaks we are guaranteed to have the number of routes from `you` to `out`. This runs extremely fast, although it
ends up not touching most of the graph's nodes.

I tried adapting this algorithm to part two where we need routes going through two specific interior nodes, but could not get it
to work. I think the reason why is given the one-directional nature of the graph there are many nodes that point to those interior
nodes, but not exclusively as is the case with `out`.

Rather than wrestle with the bottom-up approach I decided to explore DFS with memoization. Starting at the top it finds all paths
through the graph and tracks whether it goes through the two specific interior nodes at each step. Once it reaches `out` it
returns either one if it passed through them both, or zero if it did not. That way it still explores the full depth of the graph
but effectively throws out routes that are invalid.

Memoization uses a key including the interior node and whether the path - at that point - went through either of the named
interior nodes.

Turns out this was also super fast, completing in several  milliseconds.

Later I went back and replaced the bottom-up algorithm in part one with DFS for consistency. It even runs in fewer steps, although
the run time is so short it does not really matter.

## Day 12: Christmas Tree Farm

[Year 2025, day 12][12.0]

Traditionally, the final day in Advent of Code is easy compared to previous days. 2025 is no exception.

I will not spoil it, other than to say there is a really easy solution _however_ there is a single input for which it does not
work: the third region in the example input. My puzzle solution simply filters that input out during parsing then uses the easy
solution.

If you do not already know the trick here then I recommend throwing out the traditional advice against premature optimization.
Optimize early before implementing the difficult logic then test your answer. If nothing else, you can validate your optimization
to see if it is a decent upper bound.

When I used that approach I found the answer. I checked the daily reddit thread and apparently the same thing happened to everyone
else.

[1.0]: https://adventofcode.com/2025/day/1
[2.0]: https://adventofcode.com/2025/day/2
[3.0]: https://adventofcode.com/2025/day/3
[4.0]: https://adventofcode.com/2025/day/4
[5.0]: https://adventofcode.com/2025/day/5
[6.0]: https://adventofcode.com/2025/day/6
[7.0]: https://adventofcode.com/2025/day/7
[8.0]: https://adventofcode.com/2025/day/8
[8.1]: https://en.wikipedia.org/wiki/Kruskal%27s_algorithm
[9.0]: https://adventofcode.com/2025/day/9
[9.1]: https://adventofcode.com/2023/day/18
[10.0]: https://adventofcode.com/2025/day/10
[11.0]: https://adventofcode.com/2025/day/11
[11.1]: https://www.devtoolsdaily.com/graphviz/
[11.2]: https://en.wikipedia.org/wiki/Depth-first_search
[11.3]: https://en.wikipedia.org/wiki/Memoization
[12.0]: https://adventofcode.com/2025/day/12
