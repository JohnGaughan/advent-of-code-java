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

## Day 2: TBD

[Year 2025, day 2][2.0]



## Day 3: TBD

[Year 2025, day 3][3.0]



## Day 4: TBD

[Year 2025, day 4][4.0]



## Day 5: TBD

[Year 2025, day 5][5.0]



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
