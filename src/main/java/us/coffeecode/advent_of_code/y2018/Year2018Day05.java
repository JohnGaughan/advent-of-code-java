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
import java.util.ArrayDeque;
import java.util.Deque;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2018/day/5">Year 2018, day 5</a>. Today's puzzle asks us to reduce a string using
 * the rule that two adjacent characters with different case are removed from the string. Part one asks us to reduce the
 * input string, while part two asks us to remove one character (both cases) first and see what the shortest string is
 * that can be generated.
 * </p>
 * <p>
 * The reduction code is fairly straightforward, removing all compatible characters using one pass through the input.
 * This works by using a stack and constantly removing anything that can be removed at each step. For part one, we stop
 * here. For part two, we save this reduced state and work off of it 26 times, once for each letter of the alphabet.
 * Attempt to reduce further using the "ignore" variant of the reduce() method, which skips over characters in the
 * input. Since part one reduces the string size by roughly 80%, that saves a bunch of redundant steps through the input
 * on subsequence iterations.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2018Day05 {

  public long calculatePart1() {
    return getInput().length;
  }

  public long calculatePart2() {
    final char[] input = getInput();
    int best = Integer.MAX_VALUE;
    for (char i = 'A'; i <= 'Z'; ++i) {
      final char[] output = reduce(input, Character.valueOf(i));
      best = Math.min(output.length, best);
    }
    return best;
  }

  private char[] reduce(final char[] input) {
    return reduce(input, null);
  }

  private char[] reduce(final char[] input, final Character ignore) {
    final char _ignore = ignore == null ? 0 : ignore.charValue();
    final Deque<Character> stack = new ArrayDeque<>(input.length);
    for (char ch : input) {
      if ((ch & 0xDF) == _ignore) {
        continue;
      }
      if (stack.isEmpty()) {
        stack.addFirst(Character.valueOf(ch));
      }
      else {
        final char top = stack.peekFirst().charValue();
        if ((top ^ ch) == 0x20) {
          stack.removeFirst();
        }
        else {
          stack.addFirst(Character.valueOf(ch));
        }
      }
    }
    final char[] result = new char[stack.size()];
    for (int i = 0; i < result.length; ++i) {
      result[i] = stack.removeLast().charValue();
    }
    return result;
  }

  /** Get the input data for this solution. */
  private char[] getInput() {
    try {
      return reduce(Files.readString(Utils.getInput(2018, 5)).trim().toCharArray());
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
