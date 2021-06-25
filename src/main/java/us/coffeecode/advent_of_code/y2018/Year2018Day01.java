/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2021  John Gaughan
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
package us.coffeecode.advent_of_code.y2018;

import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2018/day/1">Year 2018, day 1</a>. Today's puzzle has us process a list of integers.
 * Part one asks for its sum. Part two has us repeatedly process the list, tracking the sum along the way, until we
 * encounter a duplicate sum.
 * </p>
 * <p>
 * Part one has a trivial solution using Java's stream operations. Part two calculates over 129,000 values until it
 * finds a repeat. This takes a trivial amount of time on the given input. I am aware of ways to calculate this that are
 * faster using advanced math. When JUnit reports 0ms as the run time, I would rather move on to the next day.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2018Day01 {

  public long calculatePart1() {
    return Arrays.stream(getInput()).sum();
  }

  public long calculatePart2() {
    int frequency = 0;
    final Set<Integer> seen = new HashSet<>(1 << 17);
    final int[] input = getInput();
    outer: while (true) {
      for (final int change : input) {
        final Integer key = Integer.valueOf(frequency);
        if (seen.contains(key)) {
          break outer;
        }
        seen.add(key);
        frequency += change;
      }
    }
    return frequency;
  }

  /** Get the input data for this solution. */
  private int[] getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2018, 1)).stream().mapToInt(Integer::parseInt).toArray();
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
