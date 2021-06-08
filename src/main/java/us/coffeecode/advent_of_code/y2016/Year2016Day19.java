/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2020  John Gaughan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package us.coffeecode.advent_of_code.y2016;

/**
 * <p>
 * <a href="https://adventofcode.com/2016/day/19">Year 2016, day 19</a>. This day's problem asks us to calculate the
 * value of one of two sequences. For part one, this is the
 * <a href="https://en.wikipedia.org/wiki/Josephus_problem">Josephus Problem</a> whose sequence is specified in the
 * <a href="https://oeis.org/A006257">Online Encyclopedia of Integer Sequences</a>. Part two is a modified sequence.
 * </p>
 * <p>
 * Part one was trivially easy: simply code the OEIS sequence. This is a <code>O(log(n))</code> solution because each
 * recursive call divides <code>n</code> in half. Part two was a little more tricky. A brute-force solution using arrays
 * or lists works, but is slow. However, it does enable us to see the pattern.
 * </p>
 * <p>
 * As it turns out, the pattern is based on powers of three. Consider an input n. Take the logarithm base 3 of n, and
 * round down: this is the integral log base 3 of n. If that log is n, then n is a power of three and the answer is n.
 * Otherwise, let us consider how the sequence changes. As n increases by one, b(n) increases by one until n is greater
 * than twice the integral log. Then b(n) increases by two each time n increases by one.
 * </p>
 * <p>
 * All together, this means we can take the integral log of n base 3 and partition the solution space into three. Equal
 * to the power of three, equal to or less than twice that power of three, or greater than twice the power. For each
 * partition we can directly calculate b(n) since the function follows consistent rules.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2016Day19 {

  private static final int INPUT = 3_001_330;

  public long calculatePart1() {
    return a(INPUT);
  }

  private long a(final long n) {
    if (n == 1) {
      return 1;
    }
    else if (n % 2 == 0) {
      return 2 * a(n / 2) - 1;
    }
    else {
      return 2 * a(n / 2) + 1;
    }
  }

  public long calculatePart2() {
    return b(INPUT);
  }

  private long b(final long n) {
    // Note: dealing with doubles and longs here of the scale of our input will not cause any FP wonkyness.
    // Take the log3 of the input, then power it back out. This gives us the integral log3 as well as the lower bound
    // for the current partition. We have to be between 3^pow3 (inclusive) and 3^(pow3+1) (exclusive).
    long log3 = (long) (Math.log10(n) / Math.log10(3));
    long pow3 = (long) Math.pow(3, log3);
    if (n == pow3) {
      return n;
    }
    else if (n - pow3 <= pow3) {
      return n - pow3;
    }
    else {
      // Multiply n by 2 to get the two-spacing needed, then subtract the next higher power of three to scale it back
      // down.
      return (n << 1) - 3 * pow3;
    }
  }

}
