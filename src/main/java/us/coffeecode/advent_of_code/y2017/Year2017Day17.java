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
import java.util.ArrayList;
import java.util.List;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2017/day/17">Year 2017, day 17</a>. This problem is about a spinlock which is a
 * circular buffer. We insert values into the buffer, and want to know which value is inserted at a given step.
 * </p>
 * <p>
 * Part one is trivial to use brute force. Part two, not quite. However, there is an interesting optimization. We really
 * only need to track where element zero is, and what number is inserted after it. Other than that we really do not care
 * what values we insert or where we insert them. Iterate over the values, and if we insert after zero, that is our new
 * answer. Otherwise, keep going. Do not bother storing the whole buffer since it simply does not matter. This saves a
 * lot of time and space complexity and allows the algorithm to complete in a very short amount of time (slower than
 * most 2017 problems, but still quick).
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2017Day17 {

  public long calculatePart1() {
    final int bound = 2018;
    final int input = getInput();
    final List<Integer> spinlock = new ArrayList<>(bound);
    spinlock.add(Integer.valueOf(0));
    int index = 0;
    for (int i = 1; i < bound; ++i) {
      index = (index + input) % spinlock.size() + 1;
      spinlock.add(index, Integer.valueOf(i));
    }
    ++index;
    if (index == spinlock.size()) {
      index = 0;
    }
    return spinlock.get(index).intValue();
  }

  public long calculatePart2() {
    final int input = getInput();
    int index = 0;
    int answer = 0;
    for (int i = 1; i <= 50000001; ++i) {
      index = (index + input) % i;
      if (index == 0) {
        answer = i;
      }
      ++index;
    }
    return answer;
  }

  /** Get the input data for this solution. */
  private int getInput() {
    try {
      return Integer.parseInt(Files.readString(Utils.getInput(2017, 17)).trim());
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
