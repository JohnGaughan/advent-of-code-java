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

## Day 6: TBD

[Year 2025, day 6][6.0]



## Day 7: TBD

[Year 2025, day 7][7.0]



## Day 8: TBD

[Year 2025, day 8][8.0]



## Day 9: TBD

[Year 2025, day 9][9.0]



## Day 10: TBD

[Year 2025, day 10][10.0]



## Day 11: TBD

[Year 2025, day 11][11.0]



## Day 12: TBD

[Year 2025, day 12][12.0]



[1.0]: https://adventofcode.com/2024/day/1
[2.0]: https://adventofcode.com/2024/day/2
[3.0]: https://adventofcode.com/2024/day/3
[4.0]: https://adventofcode.com/2024/day/4
[5.0]: https://adventofcode.com/2024/day/5
[6.0]: https://adventofcode.com/2024/day/6
[7.0]: https://adventofcode.com/2024/day/7
[8.0]: https://adventofcode.com/2024/day/8
[9.0]: https://adventofcode.com/2024/day/9
[10.0]: https://adventofcode.com/2024/day/10
[11.0]: https://adventofcode.com/2024/day/11
[12.0]: https://adventofcode.com/2024/day/12
