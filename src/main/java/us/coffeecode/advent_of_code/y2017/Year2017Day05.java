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
package us.coffeecode.advent_of_code.y2017;

import java.nio.file.Files;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2017/day/5">Year 2017, day 5</a>. This problem asks us to evaluate a series of
 * jumps, as in an assembly program. However, there are rules about how the jumps change each time they are evaluated.
 * In part one, a jump increments by one each time after it is evaluated. In part two, the same rule applies, unless the
 * jump value is three or more: then it decreases. In both cases, we need to count how many jumps it takes to escape the
 * program.
 * </p>
 * <p>
 * This is a trivial simulation where we simply track an instruction pointer and the number of jumps, and increment
 * until the pointer is invalid.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2017Day05 {

  public long calculatePart1() {
    final int[] instructions = getInput();
    int pointer = 0;
    long jumps = 0;
    while (pointer >= 0 && pointer < instructions.length) {
      final int oldPointer = pointer;
      pointer += instructions[pointer];
      ++jumps;
      ++instructions[oldPointer];
    }
    return jumps;
  }

  public long calculatePart2() {
    final int[] instructions = getInput();
    int pointer = 0;
    long jumps = 0;
    while (pointer >= 0 && pointer < instructions.length) {
      final int oldPointer = pointer;
      pointer += instructions[pointer];
      ++jumps;
      if (instructions[oldPointer] < 3) {
        ++instructions[oldPointer];
      }
      else {
        --instructions[oldPointer];
      }
    }
    return jumps;
  }

  /** Get the input data for this solution. */
  private int[] getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2017, 5)).stream().mapToInt(Integer::parseInt).toArray();
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
