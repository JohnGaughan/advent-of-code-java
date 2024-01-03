# Year 2019: Journey Through the Solar System with IntCode

## Day 1: The Tyranny of the Rocket Equation

[Year 2019, day 1][1.0]

This year starts off with a simple puzzle demonstrating the tyranny of the rocket. For part one, we need to figure out how much
fuel is required to lift each mass, and total up the fuel. Part two introduces the idea that the fuel also needs fuel, with larger
masses requiring a lot more fuel.

Essentially, this is simply repeatedly applying the same function a variable number of times. For part one, we apply it once per
input value. For part two, we keep applying it until it returns zero: the specific number of times will vary based on the input.

## Day 2: 1202 Program Alarm

[Year 2019, day 2][2.0]

Today introduces IntCode with only add and multiply instructions.

This is pretty basic: IntCode is implemented in a separate class for later reuse. For part one, just run it and get the output
value. For part two do the same, but loop over the valid inputs.

## Day 3: Crossed Wires

[Year 2019, day 3][3.0]

Today's puzzle has us map two wires on a grid.

Part one asks for the point with coordinates closest to origin where the wires cross. Part two asks for the point where they cross
with the lowest combined distance to origin, where the distance is measured in wire length.

This is a very sparse grid. Points are stored in a map rather than a 2D array, which ends up being substantially faster. The
"grid" is populated the same way for both parts. For part one we measure Manhattan distances of points, using the map value
(distances) to know if wires cross there. For part two we simply look at distances and find the lowest combined value.

## Day 4: Secure Container

[Year 2019, day 4][4.0]

This puzzle is a password puzzle that is really just matching digits of a number or string. Parts one and two ask to find the
quantity of numbers in a range that match rules, where part two adds an extra rule.

This is a fairly simple challenge that runs quickly enough with a naive brute-force algorithm that it is not worth optimizing. The
main difference between the two parts is part two needs to look at surrounding digits when looking for pairs of digits.

## Day 5: Sunny with a Chance of Asteroids

[Year 2019, day 5][5.0]

Today's puzzle extends the IntCode from day two with more instructions and an additional parameter mode.

This was fairly straightforward. I refactored day two's code into separate classes that were easier to modify and had
better-defined boundaries to their responsibilities. Then I added the new instructions, parameter mode, and the ability to use
inputs and outputs. In both cases correct execution _will_ return output, so assume the output arrays have the necessary number of
elements and let an exception indicate failure.

## Day 6: Universal Orbit Map

[Year 2019, day 6][6.0]

Today's puzzle is an exercise in trees that have an arbitrary number of children per node. Part one asks for the sum of the depths
of all nodes in the tree. Part two asks for the number of pivots to get two nodes having the same parent.

Part one is a simple exercise in assembling the tree correctly and understanding how to calculate a node's depth.

Part two is also fairly simple if you understand properties of trees. The question is asking for the distance along the tree.
Keeping in mind that a tree is an acyclic graph, there can be only one path between two nodes. That path starts at one of the
child nodes and traverses up the tree until the other child node exists in that subtree. The path then traverses down to the other
node. The steps from each child node to the common parent is simply the difference of each child's depth and the common parent's
depth. Then sum those two differences to get the length of both legs It is important to note that the path is from each child
node's parent, not the child node, so it is two less than that distance.

## Day 7: Amplification Circuit

[Year 2019, day 7][7.0]

Today's puzzle extends IntCode by introducing multiple independent IntCode computers that read each others' outputs as inputs as
well as some static data. Part one is a simple run through five such computers, while part two repeats the run until the final
computer halts while also maintaining state between iterations.

Part two was a bit of a chore because of edge conditions. The problem statement was a little unclear which wasted some time, but
ultimately, it works pretty much as expected. Issues were with deciding when a specific IntCode computer was truly halted and when
it blocked for input. Furthermore, we ignore halts on all but the final computer (Amp E in the problem description) which was
another edge case that _will_ produce incorrect results if omitted. Ultimately, this was a fun challenge that met more road blocks
in my own code than in the problem statement but eventually produced correct results.

One benefit of implementing this as JUnit test cases is the ability to expand on IntCode and have near-instant validation that
previous solutions are not broken.

## Day 8: Space Image Format

[Year 2019, day 8][8.0]

Today's puzzle involves some basic array processing for part one, then more for part two along with some pattern matching to find
strings in an image.

This is a recurring theme in Advent of Code: finding strings in raw pixel data. This approach builds a basic font and stores them
in an integer format based on the thirty bits set in each character. Then when parsing the program output, create an integer
bitmap of each character. Look it up in the map to get the character (actually a one-character string) to add to the output
string, and return that string for validation.

This program contains a few extra characters not used in my problem's solution which I found at various sources where people
shared their output to the same problem. There are some obvious easy characters such as D and O that are missing, but I did not
see those in any other variations on this problem. Any characters not part of my specific solution are not tested.

## Day 9: Sensor Boost

[Year 2019, day 9][9.0]

Today's puzzle introduces more IntCode requirements, with both parts of the puzzle differing only by the program input.

This again required some refactoring. The IntCode computer already worked with longs due to the previous IntCode problem rolling
values negative, but today also added dynamic memory and a new addressing mode. Relative addressing was really easy to get wrong.
I had a few tiny bugs that prevented the tests from passing but eventually figure out I had an incorrect address parameter. Turns
out a lot of other people did as well. After solving it, it seems like half the people who tried this also had the 203 error.

## Day 10: Monitoring Station

[Year 2019, day 10][10.0]

Today's puzzle is all about comparing points in a plane. Given that these points truly are points and not blocks that occupy
pixels, we need to find straight lines between these points and figure out which ones block vision. For part one we need to
calculate how many points a given point can see, and return the greatest count of visible points. For part two, we need to spin
that point around and destroy other points in a specific order, getting the coordinates of the 200th point destroyed.

This was interesting given that this looks like a trigonometry problem on the surface, but it has much better solutions grounded
in integer arithmetic. Trigonometry would be ideal with a pen and paper problem, but computers use floating point math which can
get messy and sometimes return incorrect results when high precision is needed. I went with integer arithmetic, using rise over
run to identify angles rather than radians or degrees.

For part one which is also the first section of part two, I iterate over each asteroid. I then iterate over each _other_ asteroid,
and store its reduced rise over run angle in a map. If one asteroid blocks another, its angle will reduce to the same value and
essentially be ignored because it is not a unique value. Then I simply find the asteroid with the largest number of associated
angles.

For part two I run the same calculation except instead of returning the maximum value, I get the asteroid associated with it and
turn it into my Death Star. From here I run a similar calculation to the first part. I store each other asteroid in a map, except
the key is its reduced angle and the map value is a set of all asteroids that share that angle. The map is sorted using a custom
comparator on the angles so iteration order works the same as in the problem. The value sets are sorted based on distance from the
Death Star. Then I simply spin the Death Star and take each angle in turn, removing the closest asteroid on that angle. After
two-hundred confirmed kills, I then return that asteroid's coordinates.

## Day 11: Space Police

[Year 2019, day 11][11.0]

Today's puzzle is an IntCode exercise where the IntCode program emulates Langton's Ant. Part one, as usual, ensures the
implementation is set up correctly. Part two asks for the graphical output of the program.

This is the first IntCode program where we are not actually updating the IntCode computer itself. Instead, we need to communicate
with it similar to day 7, where we actually had multiple IntCode computers talking to each other. It also shares details with day
8, which requires image recognition of a font. Today brought nothing new to the table, making it a relaxing endeavor with minimal
challenge.

## Day 12: The N-Body Problem

[Year 2019, day 12][12.0]

Today's puzzle is an exercise in calculating repeating numbers. The puzzle input and rules describe the location and movement of
four moons. As usual, part one asks for some basic information to ensure we load, parse, and set up data correctly. Part two asks
for how many iterations before the system repeats its cycle.

Part two was challenging, but for the wrong reasons. This is clearly an application of finding the lowest common multiple of some
numbers. Knowing the three dimensions do not affect each other simplifies this a bit. However, finding the LCM of the planets'
movements separately gave the wrong answer. Instead, I settled on finding the period of each dimension, then finding the LCM of
each period. It is also worth pointing out that the location is not enough when determining if a state is unique. Velocity also
matters because the moons could all be in the exact same locations between two states, but with different velocities, the next set
of locations will be different.

## Day 13: Care Package

[Year 2019, day 13][13.0]

Today's problem provides an IntCode program that is a Breakout-style video game. We need to run the program and get some results.
Part one verifies that we set up the game state correctly. Part two has us play the game and return the final score.

Part one is fairly simple. Iterate through the output, interpreting the tiles looking for blocks. However, some blocks are output
more than once, so we need to track coordinates. Part two is a bit more involved. There are several approaches. Keeping in line
with the "let the computer play itself and return a value that JUnit can verify" requirement, I found it best to let the IntCode
interpreter block when it produces enough output to consume (three values) then let the test case figure out what the next input
should be.

The concrete implementation I settled on manages the input and output pipes. Every three outputs, update the game state in this
test case. The input pipe is decoupled from the output: it knows the ball and paddle locations and knows how to move the paddle at
any given instant. All the game loop needs to do is update variables.

## Day 14: Space Stoichiometry

[Year 2019, day 14][14.0]

Today's puzzle is similar to language generation but not entirely. We are given generation rules that combine in such a way that
there is one "root" rule, ORE, which can generate other strings. There is one terminal rule, FUEL. Between these two end points
there are quite a few intermediate steps. We need to correlate the amounts of ORE and FUEL. Part one wants the least amount of ORE
to produce one FUEL. Part two wants to know how many units of FUEL we can produce with one trillion ORE.

The input rules do not have cycles or duplicates of what their outputs are. This makes it simpler, but we also have to contend
with quantities. Sometimes we have to produce more than is needed to satisfy an intermediate rule, so we need to track leftovers.
At any given point we have two maps of "chemicals we currently have on-hand" and "chemicals we need." It is possible that a
previous reaction produced extras that we can use later on.

The algorithm works by adding a given number of fuel to the "need" map. Then as long as there is something we need, get a random
chemical from that map and process. If we can satisfy its needs with what we have, then do so. Otherwise, find out the minimum
number of reactions needed. Iterate over the reactants and add them in to what we need. Then on the next iteration we will check
what we have left over, consume that, and so on.

The same algorithm works for both parts. For part two, we use calculate a theoretical lower bound for the answer and double that
value for the upper bound. Then perform a binary search to find the answer.

## Day 15: Oxygen System

[Year 2019, day 15][15.0]

Today's puzzle involves generating a map and getting properties about it. Part one asks to find a specific tile, part two asks how
long it takes to traverse the entire map from the goal time of part one.

Both parts use the same logic, except part two continues after part one. Part one implements a breadth-first search of the maze
until the target is found, returning the number of steps. Part two continues after the maze is generated, expanding the oxygen
tiles from the edge of the gas cloud, one step at a time. Both parts track the amount of steps needed to achieve the requirement,
and returns that number.

## Day 16: Flawed Frequency Transmission

[Year 2019, day 16][16.0]

This is a puzzle about crunching numbers using what amounts to a simple hashing algorithm. The two parts differ in the size of the
input, with part two requiring so many resources that a naive implementation will not finish in a reasonable amount of time.

Part one works with the naive approach, in fact, it even requires it. Part two requires some inspection of what the algorithm is
actually doing we well as the input data. The first catch is the offset to read is in the input data, not the data after the
algorithm runs. We can grab this right away and make optimizations that might mess up those first digits. The first thing to
realize is the offset is in the second half of the input data. Due to the way the phase multipliers work, digits in the second
half will never look at digits in the first half. Their multipliers will always be zero, so we can ignore them. Furthermore, the
multiplier in the second half will always be positive one. No multiplying required. We can simply add up the numbers.

The output of index N is the sum of input indexes N and greater, which means we can work backwards and compute one sum per
iteration. We know the offset for the answer from the beginning, and we also know that digits earlier in the array cannot
influence digits later in the array given their multipliers are zero. We do not even need to calculate those digits. We can go a
step further and not bother allocating space for those digits, copying them, anything. Before running the algorithm, trim
everything before the answer's offset. Then do the reverse digit sum over what remains to get the answer.

This is one of those rare times where the optimization for part two breaks part one.

## Day 17: Set and Forget

[Year 2019, day 17][17.0]

Today's puzzle combines two different ideas. The first is traversal of a route in 2D space with simple rules. The second is
expressing the route taken in a compact way.

Part one is trivially easy. Map out the scaffolding, then look for patterns shaped like a plus. Those are the only valid crossings
given the clunky nature of expressing the scaffolding using ASCII.

Part two looks more difficult than it really is. It _seems_ like the search space for the answer is much larger. There is a simple
algorithm, however. First of all, I worked off the assumption that all movement functions would combine pairs of turns with
complete runs. Function A would not stop in the middle of moving forward, and B would continue then have a turn in the middle. I
was prepared to code a more complex algorithm, but it turns out this assumption is valid at least for my input.

On to the algorithm. Clearly, one of the functions _must_ start at the start of the route. Let that be function A. Try each
possibility starting from 0 until function A is more than twenty characters: if we hit this condition, there is no solution (we do
not hit this condition). Similarly, since there are three functions, one of them must start where A stops. Let this function be B.
Be careful to allow for routes starting A,A. Find the next gap, and iterate until the gap is filled, or B is more than twenty
characters. Finally, find the first remaining gap between existing functions and call that C. If C is twenty or fewer characters
and the final routine consisting of A,B,C is twenty or fewer, we have a solution. All that remains is to feed it into the IntCode
computer.

## Day 18: Many-Worlds Interpretation

[Year 2019, day 18][18.0]

This puzzle requires us to find the shortest path through a maze where there are keys and doors in the maze. We must collect all
of the keys, and along the way, we can unlock additional doors. Part one has a more traditional maze with a single robot moving
around, while part two has a total of four robots that share keys. This was not a difficult puzzle to solve, however, it was
difficult to solve well. There are several approaches, and most of the ones I tried performed very poorly. After implementing part
one, part two required tricky refactoring to avoid breaking it again.

There are some easy ways to simplify the problem. I start off by filling in areas of the maze that cannot be part of any solution
and are not worth looking at. Specifically, any dead ends: this includes dead ends that contain doors, because the requirement is
to collect keys and we do not need to unlock anything that is not in the way. This alone reduces the search space considerably.

Next, I find all route segments. These are simply paths from one point of interest to another. Specifically, the start locations
and keys. Along the way I track the distance and any doors that are in the way. This works because the maze has no loops.

From here the code finds the shortest route that has 26 segments. Visiting old nodes is not permitted: while a path may pass
through the coordinate of a key already gathered, pathing specifically to that location is not allowed. That means the code can
simply check that there are 26 segments and know all keys are collected exactly once. When checking a potential segment to add to
the route, it also ensures that the path is traversable by validating the robot has all the necessary keys to unlock doors
encountered along that segment.

They key to performance is caching. There are millions of routes where the robot may visit the same five nodes all in a row, but
in a different order. The order affects the total distance, however. If we end up in a situation where we are at the same key
location and gathered the same keys as before, compare the distance traveled. If the distance is greater than any previous time,
then the current branch of the search space cannot possibly produce better results. Stop looking. That change alone enables the
algorithm to run in under one second, instead of taking so long I stopped it.

## Day 19: Tractor Beam

[Year 2019, day 19][19.0]

Today's puzzle asks us to map out a beam. This beam plots a 2D cone. For part one, we need the number of pixels lit up by the cone
in a square of size fifty. For part two, we need to find the top-left coordinate of a square with length one-hundred that fits in
the cone as close to origin as possible.

Part one guides us toward plotting a 2D array or some other "complete" data structure with all of the pixel data. That works for
part one, however, the IntCode algorithm is extremely slow and inefficient. Even setting some worst-case bounds for part two, it
still takes too long due to that slow speed combined with plotting out hundreds of thousands of pixels that will never be
referenced. Instead, plot pixels on-demand. There is never any need to precompute a region and store it. Instead, compute pixels
as-needed and consume them right away. For part one, this means adding them up as we go along. For part two, as explained below,
we use them for navigation.

The part two solution skips down a bit because the cone is fairly narrow. It picks a coordinate outside the cone, then moves right
until it is just inside the cone. From here it moves down the left edge: go down one row, then right one column if the coordinate
is outside the cone. At each iteration, check that the opposite diagonal is inside the cone: if so, we found the solution. The
top-left and bottom-right corners must also be in the cone, so there is no point in checking them. Get the Y coordinate for the
top-left corner, and account for an off-by-one error due to the square's corners forming a closed interval, as opposed to open.

## Day 20: Donut Maze

[Year 2019, day 20][20.0]

Today's puzzle requires us to navigate a maze, however, this maze has portals at its edges. In part one, these portals move the
player around the same maze. In part two, there is an additional requirement that portals move the player between different copies
of the maze.

Similar to day 18, we can optimize the maze quite a bit by removing dead ends. This greatly reduces the search space of this
specific maze, reducing it to hallways between portals with a small number of branches. From here we employ a breadth-first brute
force approach for both parts and it works well. There is actually more code for wiring up portals than there is traversing the
maze.

Part two introduces the concept that inner portals move to the outer portals on another copy of the maze: I implement this here by
using 3D points to track different Z-levels of the maze where level zero is the top layer and portals along the outside of that
layer are inert. The logic is surprisingly not much more complex than the strictly 2D version of the algorithm.

## Day 21: Springdroid Adventure

[Year 2019, day 21][21.0]

Today's puzzle is figuring out a program to run inside an IntCode program. It simulates a robot walking or running along a track
with holes in it. It must jump under specific conditions to avoid the holes while also not getting into a situation where holes
are unavoidable.

Despite the solution being so simple, this one hurt my brain. Clearly, the robot must jump if the next location is a hole. Jumping
early will avoid situations where there are holes immediately in front of the robot (A) and at the jump landing location (D). For
part two, I found that register H is the one to test to ensure the robot does not jump into a hole.

## Day 22: Slam Shuffle

[Year 2019, day 22][22.0]

Today's puzzle asks about reordering a large list of integers under the guise of shuffling a deck of cards. Part one has a
reasonable sized list and one iteration of the reorder algorithm. Part two has such a large list it will not fit in a Java array
and so many iterations it is not feasible to calculate them all.

This was hell. I never took linear algebra in college. I did have an algorithms course that touched on it, but I am no expert. I
could not solve part two on my own, and this is the only time I have been defeated until this point. I feel like I have all the
mental tools to construct a working solution, but I lack the experience to know how to put those tools together. Eventually, after
reading several blog posts on the topic, reviewing code in languages other than Java that works, and enough wrong answers to
trigger the escalating submission cooldown, I came up with a working solution. While it is my own code, I have to give a lot of
credit to the ten or so external sources who explained how to compose a linear congruence function via blogs and code, and did so
specifically in the context of this particular problem.

## Day 23: Category Six

[Year 2019, day 23][23.0]

Today's puzzle requires fifty IntCode computers acting as a network. They send messages to each other, and we need to answer with
details about messages involving a special NAT computer that exists outside of the IntCode network.

This was a little tricky because it required changing the IntCode implementation. Rather than using threads, I added the ability
to step computers one tick (instruction) at a time. That operates them in lock step, guaranteeing consistent results while also
removing the potential for threading bugs. For part one, we just need to know the second value of the first message sent to
address 255. Part two has this NAT device send a message to computer 0 when the network is idle. The trick here is detecting when
it is idle. There are a few ways to measure this, and I settled on counting the number of ticks where no I/O occurs among the
IntCode computers. After an arbitrary time limit, we consider the network idle. Adjusting this value down much more will produce
an incorrect result, while raising it increases running time.

## Day 24: Planet of Discord

[Year 2019, day 24][24.0]

No Advent of Code would be complete without a variation on Conway's Game of Life, and 2019 is no exception. Part one is a typical
simulation, while part two introduces nested game boards.

There is not much to say about part one. There are ways to optimize it such as using a single integer for each board, at the
expense of clarity. However, it takes such a tiny amount of time and space as it is that there is no value in optimizing further.
Part two uses a map of boards, creating new ones as needed. At the end, count the number of bugs.

## Day 25: Cryostasis

[Year 2019, day 25][25.0]

Today's puzzle has us investigate a maze using text input and output. We need to navigate the maze and find the right combination
of items to pass through a door. Some items lose the game using various mechanics such as instant death, an infinite loop that
effectively freezes the program, or being unable to move. These all add challenges to work through to automate interacting with a
program that is already tedious to automate.

The maze cannot use coordinates, because there would be overlaps. Consider that rooms are connected, but not in a rigid maze: the
distance between them varies. To deal with this the program builds connections between them. Navigating the maze is done one step
at a time using their connections anyway, so this works well. Step one is to map out the maze: what are the room IDs? What items
do they contain? How do we navigate from one room to another?

Once this is done, we can freely move around the maze since each room knows how to get to every other room. The next step is to
figure out which items are lethal: that is, what items lose the game instantly, cause an infinite loop, or disallow movement
permanently. We do this by navigating to each item and picking it up. If it loses the game instantly, we mark it as lethal. If
IntCode does not return in several thousand steps, we detect the infinite loop and mark it as lethal. If we pick up an item and
cannot move to another connected room, it is lethal.

The next step is to try picking up each combination of items... TODO: finish this writeup that I apparently never finished
originally.

[1.0]: https://adventofcode.com/2019/day/1
[2.0]: https://adventofcode.com/2019/day/2
[3.0]: https://adventofcode.com/2019/day/3
[4.0]: https://adventofcode.com/2019/day/4
[5.0]: https://adventofcode.com/2019/day/5
[6.0]: https://adventofcode.com/2019/day/6
[7.0]: https://adventofcode.com/2019/day/7
[8.0]: https://adventofcode.com/2019/day/8
[9.0]: https://adventofcode.com/2019/day/9
[10.0]: https://adventofcode.com/2019/day/10
[11.0]: https://adventofcode.com/2019/day/11
[12.0]: https://adventofcode.com/2019/day/12
[13.0]: https://adventofcode.com/2019/day/13
[14.0]: https://adventofcode.com/2019/day/14
[15.0]: https://adventofcode.com/2019/day/15
[16.0]: https://adventofcode.com/2019/day/16
[17.0]: https://adventofcode.com/2019/day/17
[18.0]: https://adventofcode.com/2019/day/18
[19.0]: https://adventofcode.com/2019/day/19
[20.0]: https://adventofcode.com/2019/day/20
[21.0]: https://adventofcode.com/2019/day/21
[22.0]: https://adventofcode.com/2019/day/22
[23.0]: https://adventofcode.com/2019/day/23
[24.0]: https://adventofcode.com/2019/day/24
[25.0]: https://adventofcode.com/2019/day/25
