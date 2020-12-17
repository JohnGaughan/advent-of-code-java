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
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * <a href="https://adventofcode.com/2020/day/10">Year 2020, day 10</a>. The problem throws a lot of wordiness around
 * but ultimately it is about combinations of numbers where numbers are related to those close to them. Specifically,
 * they must be between one and three of their neighbors. Part one asks for a product related to the spacing. Part two
 * asks how many combinations there are given the constraints.
 * </p>
 * <p>
 * Part one is simple: sort the input numbers, and keep a running tally of how many jumps of one there are compared to
 * jumps of three. Multiple those two sums.
 * </p>
 * <p>
 * Part two sounds like a recursive algorithm to enumerate all of the combinations. However, there are so many that such
 * an algorithm may never actually complete. Even if it did, it would probably run out of memory first. Instead,
 * consider memoization. If we encounter a number that already has a solution, just use that solution and tack it on to
 * the current search state. However, what if we memoized the search before searching? Another way to think of this is
 * what if we applied mathematical induction? Calculate the base state first: that is, we already know that the highest
 * number in the input has exactly one possible solution: itself. Cache that result. Now decrease through the input,
 * grabbing monotonically decreasing values. For each number, the quantity of solutions is equal to the sum of the
 * quantity of solutions for the three next higher numbers, or zero if a number does not exist. Repeat until all numbers
 * are exhausted. Since we start at zero, that element will have the total number of combinations.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2020Day10 {

  public int calculatePart1(final Path path) {
    int previous = 0;
    int diff1 = 0;
    int diff3 = 0;
    for (final int i : parse(path)) {
      final int diff = i - previous;
      if (diff == 1) {
        ++diff1;
      }
      else if (diff == 3) {
        ++diff3;
      }
      previous = i;
    }
    // add 3 for the device.
    ++diff3;
    return diff1 * diff3;
  }

  public long calculatePart2(final Path path) {
    final int[] numbers = prepareForPart2(parse(path));
    final Map<Integer, Long> numCombinations = new HashMap<>();

    // Base case: last number has one path.
    numCombinations.put(numbers[0], 1l);

    // Iterate the numbers, skipping the base case.
    for (int i = 1; i < numbers.length; ++i) {
      long newPaths = 0;
      for (int j = numbers[i] + 1; j <= numbers[i] + 3; ++j) {
        if (numCombinations.containsKey(j)) {
          newPaths += numCombinations.get(j);
        }
      }
      numCombinations.put(numbers[i], newPaths);
    }

    return numCombinations.get(0);
  }

  /** Prepare the input for part 2. */
  private int[] prepareForPart2(final int[] input) {
    // Array is sorted low to high. We need it high to low, with a zero on the end.
    final int[] output = new int[input.length + 1];
    for (int i = 0; i < input.length; ++i) {
      output[output.length - i - 2] = input[i];
    }
    output[output.length - 1] = 0;
    return output;
  }

  /** Parse the file located at the provided path location. */
  private int[] parse(final Path path) {
    try {
      return Files.readAllLines(path).stream().mapToInt(Integer::valueOf).sorted().toArray();
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
