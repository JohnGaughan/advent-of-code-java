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
package net.johngaughan.advent_of_code.y2020;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * <a href="https://adventofcode.com/2020/day/9">Year 2020, day 9</a>. This problem requires searching a list of
 * integers and finding numbers based on specific properties they have as related to numbers around them. Part one asks
 * for an integer that cannot be formed from the sum of any two integers in the preceding 25 locations in the list. Part
 * two requires us to find consecutive integers anywhere in the list that add up to that number: then we sum the lowest
 * and highest integers in that list and return the result. The input data is not sorted.
 * </p>
 * <p>
 * Part one would have some optimizations if the input were sorted. Without that, a brute force O(n<sup>3</sup>)
 * algorithm is the best I could come up with. Thankfully, the input data is small and the program ends in under 1ms.
 * </p>
 * <p>
 * Part two starts by running the algorithm for part one to get the number. Then it applies an algorithm where it tracks
 * two indices. It sums all the integers in the list between those indices, inclusive. If the sum is too low, increase
 * the upper bound to include more integers. If it is too high, increase the lower bound to exclude more integers. If it
 * is just right, terminate. Furthermore, it keeps a running sum rather than recomputing each time it changes a
 * boundary. This should be, at a minimum, close to the lowest complexity possible.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2020Day09 {

  public long calculatePart1(final Path path) {
    return getPart1Answer(parse(path));
  }

  public long calculatePart2(final Path path) {
    final List<Long> numbers = parse(path);
    final long n = getPart1Answer(numbers);
    int lower = 0;
    int upper = 1;
    long sum = numbers.get(lower) + numbers.get(upper);
    // Alternative between growing the upper bound and shrinking the lower bound until we find a range.
    while (upper < numbers.size()) {
      if (sum == n) {
        final List<Long> range = numbers.subList(lower, upper + 1);
        final long min = range.stream().mapToLong(l -> l).min().getAsLong();
        final long max = range.stream().mapToLong(l -> l).max().getAsLong();
        return min + max;
      }

      if (sum < n) {
        // Sum is too low, grab more numbers.
        ++upper;
        if (upper < numbers.size()) {
          // Technically, upper can be out of bounds here. This check will prevent an exception and allow the program to
          // return incorrect results instead.
          sum += numbers.get(upper);
        }
      }
      else {
        // Sum is too high, let go of numbers.
        sum -= numbers.get(lower);
        ++lower;
      }
    }
    return Integer.MIN_VALUE;
  }

  /** Get the answer to part 1, which is reused in part 2. */
  private long getPart1Answer(final List<Long> numbers) {
    for (int i = 25; i < numbers.size(); ++i) {
      final long n = numbers.get(i);
      if (!canSum(n, numbers.subList(i - 25, i))) {
        return n;
      }
    }
    return Integer.MIN_VALUE;
  }

  /** Get whether any two of the provided numbers can add up to the number given. */
  private boolean canSum(final long n, final List<Long> numbers) {
    for (int i = 0; i < (numbers.size() - 1); ++i) {
      for (int j = i + 1; j < numbers.size(); ++j) {
        final long n_i = numbers.get(i);
        final long n_j = numbers.get(j);
        if (n == (n_i + n_j)) {
          return true;
        }
      }
    }
    return false;
  }

  /** Parse the file located at the provided path location. */
  private List<Long> parse(final Path path) {
    try {
      // Note: numbers are not sorted here or in the input file.
      return Files.readAllLines(path).stream().map(s -> Long.valueOf(s)).collect(Collectors.toList());
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
