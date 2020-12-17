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
package net.johngaughan.advent_of_code.y2015;

/**
 * <p>
 * <a href="https://adventofcode.com/2015/day/20">Year 2015, day 20</a>. This is a problem about factorization. The
 * elves delivering presents do so in a way mimics how non-prime factors work, and the problem is essentially asking for
 * the total of all factors of a number, including one and the number itself. Part one wants this number multiplied by
 * ten. Part two asks for the number multiplied by eleven, with the caveat that elves only visit fifty houses before
 * giving up.
 * </p>
 * <p>
 * I tried several approaches, including directly calculating factors both the hard way and via prime factorization. If
 * you add up the multiple of each unique factor, then multiply all the sums, it equals the sum of the non-prime
 * factors. For example: the factors of 12 are 1, 2, 3, 4, 6, and 12. Their sum is 28. 12's prime factors are
 * 2<sup>2</sup> and 3. Multiplying the sums is
 * <code>(2<sup>0</sup> + 2<sup>1</sup> + 2<sup>2</sup>) * (3<sup>0</sup> + 3<sup>1</sup>)</code>, which is equal to
 * <code>(1 + 2 + 4) * (1 + 3)</code>, which is equal to <code>7 * 4</code> which is then equal to 28, same as before.
 * </p>
 * <p>
 * The problem with that approach is even cheating in a memory-mapped file of primes, there were simply too many loops
 * and mathematical operations. In theory that should perform well, but in practice, it was impractical for this
 * particular problem. The program took several minutes before I stopped it. The naive solution implemented below takes
 * under 20ms to execute for each half.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2015Day20 {

  public int calculatePart1() {
    final int input = getInput();
    final int[] presents = new int[900_000];
    for (int i = 1; i < presents.length; ++i) {
      for (int j = i; j < presents.length; j += i) {
        presents[j] += i * 10;
      }
    }
    for (int i = 1; i < presents.length; ++i) {
      if (presents[i] >= input) {
        return i;
      }
    }
    return 0;
  }

  public int calculatePart2() {
    final int input = getInput();
    final int[] presents = new int[1_000_000];
    for (int i = 1; i < presents.length; ++i) {
      for (int j = i, visited = 0; j < presents.length && visited < 50; j += i, ++visited) {
        presents[j] += i * 11;
      }
    }
    for (int i = 1; i < presents.length; ++i) {
      if (presents[i] >= input) {
        return i;
      }
    }
    return 0;
  }

  /** Get the input data for this solution. */
  private int getInput() {
    return 36_000_000;
  }

}
