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

## Day 13: Claw Contraption

[Year 2024, day 13][13.0]

We come to our first linear algebra problem of the year today.

The problem statement says there is a claw machine and we can press one of two buttons to move the claw to get a prize. Each
button moves the claw a different amount in the X and Y dimensions. Each button has a different cost, and we need to minimize the
cost.

It turns out that minimizing the cost is a red herring: the data seems to have only one solution. This makes sense in retrospect,
given the solution to the problem: there would be either one solution, or infinitely many with the ideal solution being trivial.

Anyway, brute force is impractical for part two where the target coordinates are excessively large. We need to come up with a
direct solution. Unfortunately, I never took linear algebra in college so this is my weak point, but Advent of Code has taught me
enough to blunder my through it. [Year 2023, day 24][13.1] is a great example.

Instead of looking at this as a sequence of button presses, look at it as solving two equations in two variables:

    a * Ax + b * Bx = Px
    a * Ay + b * By = Py

Where `a` and `b` are the number of button presses for their respective buttons, which are the variables for which we need to
solve. `Ax` and `Ay` are the coefficients for `a`, `Bx` and `By` are `b`'s coefficients. `Px` and `Py` are the coordinates of the
prize. Everything other than `a` and `b` are constants, although these constants change between each "claw machine" or system of
equations.

From here it looks like there are multiple ways to derive the general equations for `a` and `b`. However, most of the methods use
math that is above my level (calculus and discrete math). I tried working it out algebraically but got a huge mess that works on
paper, but fails in code due to using integer math. Somehow, the above equations can be solved for both `a` and `b` below which is
not what I get when I use algebra:

    a = (Px * By - Py * Bx) / (By * Ax - Bx * Ay);
    b = (Py * Ax - Px * Ay) / (By * Ax - Bx * Ay);

I know this because these equations keep coming up year after year in Advent of Code. While I do not understand how to derive
them, I can certainly plug them into code and make minor adjustments as necessary until they produce the correct answer.

Apparently, this is derived from [Cramer's rule][13.2] which requires a few matrix operations. Unfortunately, my math education
only scratched the surface of matrix operations and that was several decades ago. [Gaussian elimination][13.3] is another method
of solving this, but again, that is above my level.

## Day 14: Restroom Redoubt

[Year 2024, day 14][14.0]

Honestly, I do not like problems that require image recognition. That is a huge pain for these problems that should be solved
quickly and in an objective manner.

Thankfully, we really do not need to do that today.

We model robots buzzing around a grid, and we need to calculate a heuristic about their locations after one hundred iterations of
their movement for part one. This is very easy: we know where each robot starts, and their movement at each iteration. Rather than
looping, this is a great opportunity to calculate it directly: multiply the iterations by the delta X and delta Y, then calculate
the modulo with the width and height of the grid. In Java, modulo will sometimes be negative: if so, bump that coordinate up by
the width or height as appropriate.

Then we figure out which quadrant each robot is in, and get the total number of robots in each quadrant. Multiply these four
values together to get the safety factor, which is the answer.

Part two looks really annoying at first: computer code of the sort written for Advent of Code is not artificial intelligence. It
is not meant to do image recognition. In years past there have been problems where the program generates a pixel-art image that we
need to map to a number: not its graphical representation, but its integer value. Today's puzzle is worse: it does not tell us
what the image is we are looking for, and the upper bound on the cycle of robot movement is the width of the grid times its
height. The actual cycle length may be lower, but it cannot be higher.

I found the answer through dumb luck of filtering states based on a heuristic that does not matter, and picked it out by sight
when printing grid states that matched that heuristic. Success! I had the answer, and my second star for the day. Then I went back
to the code to figure out how to automate it. This is where I stalled. I copied and pasted the image, which was a portion of the
entire grid state, and thought about how to encode it in the solution code in a way that it could check for a match.

This is when I reread the problem statement. Why did part one spend so much space talking about the safety factor? The image was
off in one corner of the grid, not split between quadrants. Then it hit me.

When so many robots are in one quadrant, that quadrant will have a large number of robots (obviously). But this means the other
quadrants will have very few robots. Looking at a smaller example, consider two positive integers that sum to ten. Which integers
will have the largest product? Five times five is twenty-five. A the other extreme, one times nine is nine. So if one quadrant is
overloaded with robots and the others are sparse, their product will be lower than a more well-balanced set of quadrants.

The long and short of this is the code searches for the board state with the lowest safety factor. We do not need to find the
specific image: simply search every possible board state and check its safety factor. Find the one with the lowest safety factor,
and return its number of iterations.

## Day 15: Warehouse Woes

[Year 2024, day 15][15.0]

This was tricky to get right, but ultimately, not too bad.

We need to model a warehouse where a robot moves around, pushing boxes. After it is done moving, we calculate a score based on the
positions of the boxes.

For part two, the entire map is stretched width-wise by a factor of two, including the boxes. This means the movement logic is
more complicated, but the algorithm I wrote is the same for both parts. The only caveat is the board state will be different
because the boxes use different symbols. Scoring is the same as well, although only the left half of wide boxes are used for
scoring in part two.

We start off by reading the input grid into a two-dimensional array of code points, replacing the robot with a `.` making its
space empty. Robots are not boxes or walls. We track the robot separately as a `Point2D`. We also read the input into a list of
`Direction` enumeration instances.

From here we process each direction instruction. If the robot would move into a wall, it does not move. If it would move into an
open space, we simply update its location.

If a robot moves into a regular box, we need to keep looking in that direction until encountering an open spot or a wall, skipping
over other boxes. If we see a wall, again do nothing. If there is an open spot, set it to be a box. Change the first box to be
open, and update the robot's location to be where the first box was.

Slightly more complicated is when the robot pushes a wide box `[]` from either the left or the right. The logic is the same,
except we cannot simply update the head and tail of the row of boxes. We actually need to overwrite the entire row with
alternating square brackets. Still, not too tough.

The complex part is pushing wide boxes up or down. There are a few easy mistakes one can make here, which I know because I made a
few during my own development. The first step is to get all of the locations that need to be updated, and to track if this is even
a legal move. There could be a giant pyramid of boxes, with one corner bumping into a wall. But first, let's get the moves. I
implemented a recursive method that gets all of the `Point2D`s to move, and stores them in a set. Early on I recognized that boxes
could be offset such that one box pushes two others, who both push on a box in the middle. We need to look at _unique_ locations
when figuring out which ones to move.

At each step, we look at where the current point will move. If that is an open spot, then we simply add the current point to the
set of points to move. If it is a wall, then we add `null` to the set: this indicates an invalid move. Otherwise, there is a box
in the way. Add the current point to the move set, then make two recursive calls: one for the point in front of the current point,
per the direction we are moving: a second call for the point either to its left or right depending on which box half it is. For
both, we add the result of the recursive call into the current set of points and return that back to the vertical movement method.

The next step is to check for a valid move: if there are any null elements then it is an invalid move, and the robot stays in the
same location. If it is valid, then store the set of points into a list and sort it. Sorting is critical because we need to update
the points farthest from the robot first, so later updates do not overwrite earlier ones. Since the top of the grid is `y = 0`, we
need to sort low to high when moving up. This will process the top rows first, the bottom rows last. This is the natural ordering
of `Point2D`, which is convenient. For moving down, we use a reverse comparator to process larger `y` values first.

Simply iterate through the points. For each one, get the destination point to where it will move. `Point2D` has built-in methods
for getting and setting values in a two-dimensional array using the point's coordinates to access the corresponding array
location. Copy the value from the old location to the new location, then set the old location to blank (`.`). This ensures correct
handling of internal voids in a group of boxes, as well as external edges when they are in a V-shape. Once this is done, return
the robots new location and we are done with a vertical move.

All that is left to do is score the board. Iterate the board in both directions. If we see an `O` for box or `[` for the left half
of a wide box, then apply the scoring as per the requirement. Add up all of the scores to get the answer.

## Day 16: Reindeer Maze

[Year 2024, day 16][16.0]

Today we get a good maze navigation problem. We need to find paths through a maze with the lowest score, using a specified
heuristic that heavily favors not making turns rather than the shortest path. For part one, we need to find that lowest score. For
part two we need to find all the paths that share that lowest score, then count the unique path locations between them.

The overall algorithm is the same between both parts: a [BFS (breadth-first search)][16.1] algorithm that processes search states
in order sorted by their score from low to high. It short-circuits and ends as soon as it finds a worse score. These search states
are stored in a PriorityQueue which has built-in sorting and is pretty fast. While the search part of the algorithm is the same
between both parts, the management of the search queue is different.

Part one ends as soon as a path reaches the end state. Since the queue is sorted on score, no other path can beat the first one to
the finish line: the best one can do is tie. All the code needs to do at this point is return that score.

Part two processes the queue until it finds an end state: this is again the best score. The first time and any subsequent time it
finds that best score at an end state, it adds the visited locations for that path to a combined set of points for all such paths.
The answer is simply the size of that set. Once the queue encounters any state with a worse score than the best score, once that
best score is found, no further processing is necessary as none of it will find any path that matches the best score so it breaks
the loop.

Each time it processes a maze state, it finds all valid facings for the current location. It keeps only those facings that can be
traversed: that is, it will not move into a wall or any already-visited location. It then creates new states for those valid
facings. If the new facing is the same as the old, it takes one step in that direction. If different, it turns.

For these new states, it filter out those that have a worse score than any previous duplicate state. There is no point in
processing those states as they can never beat or match the previous time seeing that state.

## Day 17: Chronospatial Computer

[Year 2024, day 17][17.0]

Advent of Code throws us another difficult problem that requires some thinking outside of the box.

We are given a simple model of a CPU that executes instructions with three registers. Before diving into the two parts to the
problem, the CPU model is fairly simple to implement and is reused. It is a simple loop that executes instructions until the
instruction pointer is out of bounds.

It is worth noting right away that the problem does involve some large numbers, even if it continuously truncates them.
Specifically, the first register, `A`, can get quite large in part two so all math needs to be done with long integers. Other than
that, the logic to execute the program that is the problem input is rather simple. I converted as much as possible to bitwise
operators, which are faster than the alternatives such as modulus and exponentiation by power of two. This is because the program
may need to be executed many times in part two.

For part one, we simply execute the program using the given initial register values and return the output. This is the trivial
case that ensures the solution models the virtual CPU correctly, and can be used as a test to ensure no regression errors while
making changes for part two.

Part two is where it gets really difficult, although previous years did have similar problems. If you completed
[year 2021, day 24: Arithmetic Logic Unit][17.1] then you should be able to look at that solution and have a head start on
analyzing today's problem. We need to figure out which initial value for register `A` will cause the program to output its own
instructions. Brute force algorithms will take so long as to be impractical. We need to come up with an intelligent solution that
uses properties of the input to eliminate the vast majority of the search space.

The first step toward understanding how to design a solution is to convert your program into pseudocode. Program inputs are
different, including the program instructions, so I cannot specify exact steps here. But I noticed several key elements, and these
appear to agree with other comments I later read on the [Adevent of Code subreddit][17.2]:

* The program is a simple loop with a jump-not-zero as the final instruction, looping back to the beginning.
* Registers `B` and `C` are set each time through the loop before being read, making their scope the inside of the loop itself.
* Register `A`'s lowest three bits, an octal digit, are read at the start of each iteration.
* The program seems to be performing some sort of three-bit hash algorithm, and outputting the result each time through the loop.
* Register `A` is shifted right by three bits at the end of each iteration, removing the just-hashed bits and introducing three
  more for the next iteration. If this is the last of the bits, then `A` will instead be zero and the program ends.

In total, the program appears to be a hash algorithm that consumes three bits each time through, transforming those bits and
adding them to the output string.

Thankfully, we know the desired output string and through inspection and debugging can see how the output is constructed. Given a
hypothetical correct value for `A`, the program works right to left, low bits to high bits, and builds the output string from left
to right.

While many of the operations work only with the lower three bits of `A` either directly or indirectly, there is a division using
the entire value of `A` as the numerator. This means the digits in the output are not independent. Two different values of `A`
sharing the same low three bits could produce different results for the first output digit. It is also possible that two different
`A` values produce the same first output digit, meaning there is not a one-to-one mapping. Searching will be a little more
complicated.

The approach that worked for me is a recursive [depth-first search][17.3]. The recursive method accepts the instructions and an
output string as parameters, and returns a set of `Long`s that produce that output string. Start off by making the recursive call
right away, except call it with the first two characters (comma and one digit) removed from the _start_ of the output string. In
other words, solve for the final digit first.

The tail call is when there is only one digit. Iterate from zero to seven, or all three-bit integers, and run the program using
the loop counter as the initial value of register `A`. Return all `A` values that produce the desired output.

Moving up a level in the call stack, we check each value returned by the recursive call. First shift it three bits to the left.
Then iterate as we did in the tail call, except instead of using the loop counter as the `A` register directly, merge it into the
`A` value using a bitwise `or`. This reverses the "unpacking" of `A` that the input program performs at the end of each loop.
Then run the program. Return all the values that produce correct subset of the output.

The top-level call is matching the entire output string, and will return the full `A` register value for all values that produce
correct output: there may be several. The problem statement instructs us to return the first one, or the one with the lowest
value, so the stream operation uses the `min()` function to return the minimal value. Unpacking this value uses some
exception-safe logic to get the value or return nonsense, and the algorithm is complete.

Considering the brute force algorithm involves run times that include terms such as "heat death of the universe" it was nice to
see this algorithm run in one or two milliseconds.

Based on the evidence I have seen and knowing that Advent of Code generally tries to have inputs that are consistent and lack
nasty edge cases, I strongly suspect this program will work on all inputs but I obviously cannot test that.

## Day 18: RAM Run

[Year 2024, day 18][18.0]

Today is another map traversal problem with a different twist which was actually quite nice. We are given a map where the walls
change over time: each unit of time, a new wall appears. For part one we need to find the shortest path after a certain number of
walls appear. For part two we flip it around and need to find the coordinates of the first wall to appear that makes it impossible
to find a solution.

The first step is to implement an algorithm to solve the maze. I chose a [breadth-first search][18.1] that incorporates elements
of both [Dijkstra][18.2] and [A*][18.3]. It uses a priority queue to store traversal states, sorted by their distance from the
start from low to high. It tracks visited nodes across all state objects. Since we process the queue favoring nodes closer to the
start, it makes no sense to process a node a second time. At best, we can tie it for distance from start which is not an
improvement. At each step, process a node's neighbors. If a neighbor is not in a wall and not already visited, add a new state to
the queue for that neighbor being the current node, and the current score plus one. Then mark it as visited. This guarantees the
lowest score at the end, and in what should be very close to the minimum number of states checked.

Part one limits the input, which is an ordered list of walls that are added over time. All we need to do is get the input, limit
it to the number of walls specified in the requirements which are different between the example and real inputs, then run the maze
algorithm to get the answer.

In true Advent of Code tradition, part one serves to tee up part two. Now we have a tested algorithm on which to build.

For part two, we need to figure out which element in the input list causes the maze to be impossible to solve. Brute force would
simply iterate the list of points, add each one, and process. This works, and even in a reasonable amount of time. On my system
and with my inputs, the example finished in a few tens of milliseconds and the real input took around three seconds. That was good
enough to submit my answer, but I wanted something better.

Enter [binary search][18.4], which is nearly always the ideal method of finding a solution in sorted data. In this case, the data
can be envisioned as an array with the same length as the number of points in the input. It has two elements: true and false. The
lower elements are true because they have a solution, the higher elements are false because they do not. We need to find the
specific point where one element is true and the next is false.

The implementation is a forever loop (`while (true)`) that tracks three variables. First is the lower bound of the search
window, starting at zero. Second is the upper bound of the search window, starting at the length of the list of points. The third
is the current search element, which is their average. At the start of each loop iteration, create a maze and fill in the walls.
Calculate its score. Next, set either the lower or upper bound to the current search element based on whether or not the score
indicates successful traversal. If the current element has no path, then lower the upper bound. Otherwise, raise the lower bound.

If the upper and lower bounds have a difference of one, then we have the solution: the first point to break the maze is the
current upper search bound because the lower bound was successful and the upper bound was not. Convert that point to a string and
return it as the answer. If the bounds are further apart, keep processing. Set the current search element to the average of the
two bounds, and loop again.

Choosing binary search over brute force linear search lowered the run time for the real input to under ten milliseconds. It
checked twelve mazes out of approximately 3,500. Linear search checked roughly half of them, which is a lot more computational
work.

## Day 19: Linen Layout

[Year 2024, day 19][19.0]

Today is an exercise in string matching and [memoization][19.1]. Generally when this is required, a solution works for part one
but takes far too long to be feasible for part two. With today's problem, brute force is infeasible for both parts.

We are given a number of different strings. One group of them represents towels, and the other represents stripes, or parts of
towels. We need to figure out how to assemble stripes into towels.

Part one asks us to determine which towels are possible to construct from the given stripes. Part two asks us how many ways there
are to assemble the towels from the given stripes. In both cases, stripes can be used more than once.

These are closely related questions, and the second can actually be used to answer the first. If there are zero permutations of
stripes that build a towel, then that towel is not possible. However, I used separate implementations for two reasons. First, test
cases do not share state, so it is impossible to calculate the answer to part two and leverage that for part one. Second, the
algorithm for part one will be slightly different and definitely faster.

For part one, consider a towel. Iterate through the stripes. For each one that matches the start of the towel, see if it matches
the entire towel: if so, this towel is possible to construct so return true right away. Otherwise, make a recursive call and see
if the remainder of the towel is possible. If no stripes match, this towel, or section of towel, is impossible to construct.

This performs an exhaustive search for all permutations of stripe. However, it can take a very long time. Furthermore, towels and
stripes repeat a lot of the same characters: there are numerous cases where two towels reach the same "remainder" state after
matching a few stripes. In those cases, it does not make sense to repeat the matching logic because it will reach the same
conclusion.

Memoization to the rescue. Once it reaches a definite answer for a towel or portion of a towel, it stores the result in a cache.
Then when starting to process a state, check the cache first. If the current state is a key in the map which is the cache, simply
get the result and return it.

This cache is used across all towels, too: they do use the same stripes, after all. The same substring in two different towels
will have the same answer.

Part two is virtually the same, except instead of returning right away once it knows a towel is possible or impossible, it
continues until processing all stripes and figures out the total permutations even if the value is zero. It stores that result in
the cache and returns it up the call stack.

The only other important thing to note is that the answer for part two will be quite a bit larger than can fit in a 32-bit
integer, necessitating the use of 64-bit integers. This also illustrates why memoization is so important, because processing that
many states will take quite a few more years than Earth has left to live.

## Day 20: Race Condition

[Year 2024, day 20][20.0]

Today's puzzle is a maze with no maze: there is a single path from start to finish. The path wraps around itself like a maze, but
without branching. We need to find cheats where an entity traversing the path can skip through a wall, and the cheat saves a lot
of time.

Part one looks for cheats of length two, while part two looks for cheats of length up to twenty. The algorithm can thus be shared,
with the difference being parameters. One key insight here from the problem requirements is that the number of path elements to
skip ahead is a maximum: it is possible and in fact required at times to stop skipping earlier, at least for part two where
entities are able to skip through multiple walls.

We start by getting the input, which is represented as an ASCII art maze (that is not really a maze). Create a list of points
which is the sequence of steps from the start to the finish. Search the maze for the start location, and add it as the first
element of the path. Then iterate, finding the next path element and adding it to the path until the current element is the end.
Now we have a list of points where the start is at index zero, and the end is at the highest index.

The first part of the algorithm checks the path starting at the start up to the length of the path minus the threshold of 100.
Continuing to check right up to the end will not produce any results, since it is impossible to skip ahead `threshold` steps at
that point. For each loop iteration, we call the main part of the algorithm to count the number of cheats that are present using
the current list index as its starting point. Simply add up the results of counting cheats at each step to get the total number of
cheats which is the answer to the puzzle.

Counting the number of cheats at each step is fairly straightforward. It is essentially a nested loop where we check steps in the
maze after the current step. Furthermore, the end of each cheat must be `threshold` steps after the start of the cheat, so we can
tighten the loop bounds a bit to avoid unnecessary processing.

We get the end of the cheat path and measure the [Taxicab or Manhattan distance][20.1] between them. If this is greater than the
desired skip distance, ignore this end point and keep going. Otherwise, get the time savings. This is the difference between the
list indices, minus the Taxicab distance. This represents the time it would normally take to travel between the points, minus the
time spent cheating which provides the net time. If this net time is greater than the 100 threshold, increment the cheat counter
because this is a useful cheat. When done, return the number of cheats for this starting point.

As an aside, my initial solution did more tracking of what the lengths of the cheats were, as the problem statement illustrates
this. However, the answer does not require any of that metadata. Being able to get rid of accessory data structures and reducing
much of the problem to primitive arithmetic saved an order of magnitude of runtime. When implementing anything in code, it helps
to read carefully and deliver only what is actually in the requirements.

## Day 21: Keypad Conundrum

[Year 2024, day 21][21.0]

Most years have a problem about generating really long string sequences using a set of rules, then figuring out some property of
that string such as its length. This is one of those problems, and it took me a while to come up with a solution that I am not
ashamed to share with the world.

First of all, this is a tricky problem and not only because it can generate strings that are so long that it is not feasible to
work with them. Understanding the rules and their ramifications is easy to get wrong. Figuring out what to optimize or leave alone
can also be tricky.

I started by manually creating two maps: one for the number pad, the other for the direction pad. Given a two-character string
representing the start and end characters, what direction pad inputs are possible to go from start to end? These are static and do
not depend on the input, so it makes sense to hard-code these. We could also implement a search algorithm to do this dynamically.
Either way works, but this is what I chose. It is also worth noting that when traversing a keypad, zig-zags are never optimal
because they require more movement for upstream robots. `<^^^` or `^^^<` will always be equal to or better than `^<^^` or `^^<^`
because upstream robots make fewer movements.

Another key insight is that robots always start on the `A` button. We are given in the problem statement that this is their
initial state. Furthermore, this has to be true while executing movements as well. They will move between buttons, but will always
end on `A` to send a command downstream. This means they start on `A` when they send the next command. This means we can split
each button press into its own segment independent of other commands in the same string.

That independence between segments is crucial, because it allows us to implement memoization. Each time we encounter the same
segment of input at the same depth (robot number), it will _always_ have the same score. We can calculate it once and return it on
subsequent instances of that state.

The algorithm starts by calculating the score using the number pad: given each pair of start and end characters, get the one or
more direction commands that can achieve that result. Find the one with the lowest score, and add that to the cumulative score for
that number pad entry. When processing the string, the current character is the end key and the previous character is the start.
The first time through, we substitute `A` as the start since there is no previous key.

We then enter the recursive part of the algorithm: processing directional pads. This is structured nearly identical to the number
pad function, except for a few minor details. If the depth is zero, then we hit the end of the road: the score is simply the
length of the command string so we return it. Otherwise, construct a map key for the cache map (memoization). If the key already
exists in the map, short-circuit and return the cached value. Otherwise, do basically the same thing as with the number pad,
except here we decrement the depth by one when recursing. Once we have the answer, store it in the memoization cache for next
time.

Both parts execute identical logic, using a parameter stored in `parameters.properties` to control the depth of recursion.

The version of the code currently in my repository is the third iteration. The first one did not perform well, but did return an
answer after nearly a minute for part two. I must have messed up the memoization implementation but I was fed up and deleted that
version. Then I rewrote it and it was faster, but ugly, and still not fast enough (several seconds). The current version is a lot
cleaner, returns answers for both parts in milliseconds, and does not make me want to vomit by looking at it. It was quite a
roller coaster ride getting through the challenges of this day's problem and required two complete rewrites almost from scratch.

## Day 22: Monkey Market

[Year 2024, day 22][22.0]

Today's puzzle constructs a hash function and asks us to use it to perform several actions. For part one, feed each seed value to
the hash function and iterate 2,000 rounds. Sum up the end results across all seeds. For part two, we need to step through the
hash rounds, calculate differences between values, and find the combination across all seeds where the same difference provides
the greatest sum.

The first step is to write the code for one round of the hash function. The algorithm is fairly simple, but one important note is
we only care about the lower 24 bits. The operands given are also all powers of two. This means we can use integer math, as well
as bitwise operators instead of arithmetic operators. This actually does provide a significant speedup, as division (including
modulo) is very slow. For the operators that could overflow to negative values, we can ignore that. Unlike multiplication which
will overflow to negative values, the left shift operators do not overflow and also perform signed shift which leaves the sign bit
alone.

For part one, simply write a function that calls the first function 2,000 times. Map each seed to its hash value, and sum the
stream.

For part two, more steps are required. One thing that really helps is once a "monkey" sees a sequence of four differences, the
result is locked in place. We will never evaluate that difference sequence again for that seed. Another key insight is that while
a record of four integers would be a great object-oriented way to reference a sequence of differences, it becomes a performance
problem when there are a lot of keys being created and checked for equality. Hash maps generally perform well, but this comes with
a bit of overhead.

Instead, treat each sequence of differences as a single integer. Given the range -9 to 9, we can shift this by adding 9 to get 0
to 18. That takes five bits to hold, and with four differences, that is a total of 20 bits. That is just over a million, which is
quite feasible to model using a simple integer array.

Create the integer array, which will be initialized to all zeros. Then process each hash seed. The seed is the first value, and we
get its score by performing modulo arithmetic to get the final decimal digit. Then iterate the rest of the way by moving the
previous value and score into the first element in their respective two-element arrays, generate the next hash round, and get its
score. Now we can start populating the delta value by shifting it left five bits, adding the difference in scores, and adding 9.

If we are at the fourth element or later and this delta has not been seen yet, add the current score into the master array. This
will run for all seeds, and get the total value for that difference if that is the one we pick to sell banana hiding locations.

Once that is all done, simply return the largest value in the array.

## Day 23: LAN Party

[Year 2024, day 23][23.0]

We finally get a graph problem, and it was a bit tricky. The common code here reads in the pairs of connected vertices and
constructs a mapping where every vertex has a key. The value is the set of vertex names to which the key vertex is connected. From
here, the logic diverges.

For part one, we need to find all triplets where three vertices are connected to each other, and at least one of them is
identified by a string that starts with the letter `t`. While not the most optimized algorithm, the input is small enough that a
triply-nested loop works fine. Check all combinations of three vertices and add them to a set: this also takes care of duplicates,
since the algorithm does not attempt to prune the search space and reduce redundancy. If we find such a triplet, check the name
condition and if that passes, add it to the set of triplets. The answer is the size of that set.

For part two, this becomes much more complicated. We need to find the largest [clique][23.1] in the graph. This means we do not
need to find all of the cliques: an algorithm can fail to find small ones as long as it finds the largest. I settled on an
iterative approach that definitely will not find them all.

Start with a new clique for each vertex in the graph. Add that "root" vertex to the clique, then iterate through all of its
neighbors. If that neighbor is connected to every vertex already in the clique, proceed. Iterate another nested loop through the
same vertices as the first loop, starting after the vertex in the first loop. If those two are connected and the second vertex is
connected to the entire clique, add them both to the clique.

This essentially builds up a spider-web of vertices, finding the maximum clique that includes the root vertex. Once that is done,
check the current clique against the largest clique found so far. If this one is larger, set it as the new largest clique. Once
we are done iterating all vertices in the graph, return that clique formatted the way the requirements so to do so.

Once again we have an `O(n^3)` algorithm, although this runs fairly quickly probably in part due to how the inner loops avoid
redundant comparisons. Part two actually runs quite a bit faster than part one, but both are plenty quick enough that I probably
will not perform any additional optimization.

## Day 24: Crossed Wires

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
[13.1]: https://adventofcode.com/2023/day/24
[13.2]: https://en.wikipedia.org/wiki/Cramer's_rule
[13.3]: https://en.wikipedia.org/wiki/Gaussian_elimination
[14.0]: https://adventofcode.com/2024/day/14
[15.0]: https://adventofcode.com/2024/day/15
[16.0]: https://adventofcode.com/2024/day/16
[16.1]: https://en.wikipedia.org/wiki/Breadth-first_search
[17.0]: https://adventofcode.com/2024/day/17
[17.1]: https://adventofcode.com/2021/day/24
[17.2]: https://www.reddit.com/r/adventofcode/
[17.3]: https://en.wikipedia.org/wiki/Depth-first_search
[18.0]: https://adventofcode.com/2024/day/18
[18.1]: https://en.wikipedia.org/wiki/Breadth-first_search
[18.2]: https://en.wikipedia.org/wiki/Dijkstra's_algorithm
[18.3]: https://en.wikipedia.org/wiki/A*_search_algorithm
[18.4]: https://en.wikipedia.org/wiki/Binary_search
[19.0]: https://adventofcode.com/2024/day/19
[19.1]: https://en.wikipedia.org/wiki/Memoization
[20.0]: https://adventofcode.com/2024/day/20
[20.1]: https://en.wikipedia.org/wiki/Taxicab_geometry
[21.0]: https://adventofcode.com/2024/day/21
[22.0]: https://adventofcode.com/2024/day/22
[23.0]: https://adventofcode.com/2024/day/23
[23.1]: https://en.wikipedia.org/wiki/Clique_(graph_theory)
[24.0]: https://adventofcode.com/2024/day/24
[25.0]: https://adventofcode.com/2024/day/25
