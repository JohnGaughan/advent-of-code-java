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

import java.nio.file.Files;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2016/day/9">Year 2016, day 9</a>. This problem requires calculating a string
 * length, where the string length in part two is not feasible to calculate by expanding the string and counting its
 * length.
 * </p>
 * <p>
 * The two parts work nearly the same, except for part two, it uses recursion to expand substrings. In both cases, the
 * algorithm only calculates how long a substring would be if it were expanded.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2016Day09 {

  public long calculatePart1() {
    return calculateLength(getInput(), false);
  }

  public long calculatePart2() {
    // 10774309173
    return calculateLength(getInput(), true);
  }

  private long calculateLength(final String input, final boolean recurse) {
    long output = 0;
    int markerStart = -1;
    int x = -1;
    for (int i = 0; i < input.length(); ++i) {
      int ch = input.codePointAt(i);
      // Currently parsing a marker
      if (markerStart > -1) {
        if (ch == 'x') {
          x = i;
        }
        if (ch == ')') {
          final int charsToRepeat = Integer.parseInt(input.substring(markerStart + 1, x));
          final int timesToRepeat = Integer.parseInt(input.substring(x + 1, i));
          if (recurse) {
            String sub = input.substring(i + 1, i + 1 + charsToRepeat);
            output += timesToRepeat * calculateLength(sub, true);
          }
          else {
            output += timesToRepeat * charsToRepeat;
          }
          markerStart = x = -1;
          i += charsToRepeat;
        }
      }
      // Not in a marker, starting one.
      else if (ch == '(') {
        markerStart = i;
      }
      // Not in a marker, not starting one.
      else {
        ++output;
      }
    }
    return output;
  }

  /** Get the input data for this solution. */
  private String getInput() {
    try {
      return Files.readString(Utils.getInput(2016, 9)).trim();
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
