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
 * <a href="https://adventofcode.com/2017/day/9">Year 2017, day 9</a>. This problem requires us to examine an input
 * stream of characters and perform analysis on it. The stream contains garbage with well-defined rules. Part one asks
 * us to remove the garbage, then analyze what remains to ensure we removed it correctly. Part two asks us to count the
 * amount of garbage removed.
 * </p>
 * <p>
 * While parsing this string would theoretically require a pushdown automata because it describes a context-free
 * grammar, we do not actually need to parse it using a grammar. All we need to do is assume it is well-formed and
 * calculate some statistics from it. A single pass through the string is sufficient. Track whether the previous
 * character was an escape, and whether we are in a garbage block. Then track the depth of the braces, a running score
 * total, and the amount of garbage skipped. Then simply return two integers for the score (part one) and garbage count
 * (part two).
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2017Day09 {

  public long calculatePart1() {
    return calculate(getInput())[0];
  }

  public long calculatePart2() {
    return calculate(getInput())[1];
  }

  private int[] calculate(final String input) {
    boolean inGarbage = false;
    boolean escape = false;
    int garbage = 0;
    int score = 0;
    int depth = 1;
    for (int i = 0; i < input.length(); ++i) {
      final int ch = input.codePointAt(i);
      if (inGarbage) {
        if (escape) {
          escape = false;
        }
        else if (ch == '!') {
          escape = true;
        }
        else if (ch == '>') {
          inGarbage = false;
        }
        else {
          ++garbage;
        }
      }
      else if (ch == '<') {
        inGarbage = true;
      }
      else if (ch == '{') {
        score += depth;
        ++depth;
      }
      else if (ch == '}') {
        --depth;
      }
    }
    return new int[] { score, garbage };
  }

  /** Get the input data for this solution. */
  private String getInput() {
    try {
      return Files.readString(Utils.getInput(2017, 9)).trim();
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
