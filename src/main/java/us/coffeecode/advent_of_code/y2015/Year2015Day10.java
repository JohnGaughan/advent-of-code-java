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
package us.coffeecode.advent_of_code.y2015;

import java.nio.file.Files;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2015/day/10">Year 2015, day 10</a>. The problem asks for the length of the string
 * generated by applying the <a href="https://en.wikipedia.org/wiki/Look-and-say_sequence">look-and-say algorithm</a> to
 * a particular input value. Parts one and two use the same algorithm but with a different number of iterations.
 * </p>
 * <p>
 * It appears there are no shortcuts here, just plain old brute-forcing. However, it is possible to minimize the amount
 * of string allocations and temporary objects created.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2015Day10 {

  public int calculatePart1() {
    return lookAndSay(getInput(), 40);
  }

  public int calculatePart2() {
    return lookAndSay(getInput(), 50);
  }

  /** Apply the look-and-say algorithm to the input. */
  private int lookAndSay(final String input, final int iterations) {
    String value = input;
    for (int i = 0; i < iterations; ++i) {
      value = lookAndSay(value);
    }
    return value.length();
  }

  /** Apply the look-and-say algorithm to the input. */
  private String lookAndSay(final String input) {
    final StringBuilder str = new StringBuilder();
    int groupStart = 0;
    int groupChar = input.codePointAt(0);
    for (int i = 1; i < input.length(); ++i) {
      final int current = input.codePointAt(i);
      // Every time the character changes, save the previous group.
      if (current != groupChar) {
        str.append(i - groupStart);
        str.appendCodePoint(groupChar);
        groupStart = i;
      }
      groupChar = current;
    }
    // Handle the final group.
    str.append(input.length() - groupStart);
    str.appendCodePoint(groupChar);
    return str.toString();
  }

  /** Get the input data for this solution. */
  private String getInput() {
    try {
      return Files.readString(Utils.getInput(2015, 10)).trim();
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
