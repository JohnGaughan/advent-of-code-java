/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2023  John Gaughan
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
package us.coffeecode.advent_of_code.y2023;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.function.BiObjIntFunction;
import us.coffeecode.advent_of_code.visualization.HtmlVisualizationResult;
import us.coffeecode.advent_of_code.visualization.IVisualizationResult;

@AdventOfCodeSolution(year = 2023, day = 1)
@Component
public class Year2023Day01 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return solve(pc, this::getDigit).answer;
  }

  @Solver(part = 1, visual = true)
  public Collection<IVisualizationResult> calculatePart1Visual(final PuzzleContext pc) {
    return solve(pc, this::getDigit).visual;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return solve(pc, this::getDigitOrName).answer;
  }

  @Solver(part = 2, visual = true)
  public Collection<IVisualizationResult> calculatePart2Visual(final PuzzleContext pc) {
    return solve(pc, this::getDigitOrName).visual;
  }

  /** Solves either part of the problem, delegating to a function to match numbers. */
  private Solution solve(final PuzzleContext pc, final BiObjIntFunction<String, Match> f) {
    long sum = 0;
    final StringBuilder str = new StringBuilder(1 << 12);
    str.append("<html><body style=\"font-family:" + HtmlVisualizationResult.MONOSPACED_FONT + "\">\n");
    for (final String line : il.lines(pc)) {
      final Match[] matches = getMatch(line, f);
      sum += (matches[0].value * 10 + matches[1].value);
      str.append(toString(line, matches));
    }
    str.append("</body></html>\n");
    return new Solution(sum, List.of(new HtmlVisualizationResult(pc.getInputId(), str.toString())));
  }

  /** Match the first and last number on a line, using a function to determine what is and is not a match. */
  private Match[] getMatch(final String line, final BiObjIntFunction<String, Match> f) {
    final Match[] matches = new Match[2];
    // First digit (tens)
    for (int i = 0; i < line.length(); ++i) {
      final Match m = f.apply(line, i);
      if (m.isValid()) {
        matches[0] = m;
        break;
      }
    }
    // Last digit (ones)
    for (int i = line.length() - 1; i >= 0; --i) {
      final Match m = f.apply(line, i);
      if (m.isValid()) {
        matches[1] = m;
        break;
      }
    }
    return matches;
  }

  /** Get the integer digit at the provided offset, or a negative integer if invalid. */
  private Match getDigit(final String str, final int offset) {
    final int cp = str.codePointAt(offset);
    if (Character.isDigit(cp)) {
      return new Match(cp - '0', offset, offset + 1);
    }
    return new Match(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
  }

  /**
   * Get the integer digit at the provided offset from either a code point digit or string representing a number, or a
   * negative integer if invalid.
   */
  private Match getDigitOrName(final String str, final int offset) {
    // Check for integer digit.
    final int cp = str.codePointAt(offset);
    if (Character.isDigit(cp)) {
      return new Match(cp - '0', offset, offset + 1);
    }
    // Check for English digit.
    for (final Map.Entry<String, Integer> entry : NUMBERS.entrySet()) {
      final String key = entry.getKey();
      final int end = offset + key.length();
      if ((str.length() >= end) && str.substring(offset, end)
                                      .equals(key)) {
        return new Match(entry.getValue()
                              .intValue(),
          offset, end);
      }
    }
    // No match: return nonsense value.
    return new Match(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
  }

  /** Convert a line of input to an HTML fragment, using the matches to highlight parts of the line. */
  private String toString(final String line, final Match[] matches) {
    final StringBuilder str = new StringBuilder(line.length() + 64);
    str.append(line.substring(0, matches[0].x1));
    str.append("<span style=\"font:bold italic;\">");
    str.append(line.substring(matches[0].x1, matches[0].x2));
    str.append("</span>");
    // If both ends match the same region, this code throws an exception due to negative length substring.
    if (!matches[0].equals(matches[1])) {
      str.append(line.substring(matches[0].x2, matches[1].x1));
      str.append("<span style=\"font:bold italic;\">");
      str.append(line.substring(matches[1].x1, matches[1].x2));
      str.append("</span>");
    }
    str.append(line.substring(matches[1].x2, line.length()));
    str.append("<br/>\n");
    return str.toString();
  }

  /** Solution to the problem including the numerical answer as well as a visual result. */
  private record Solution(long answer, Collection<IVisualizationResult> visual) {}

  /** One match is an integer value at the offsets within a string that represent that value. */
  private record Match(int value, int x1, int x2) {

    boolean isValid() {
      return value >= 0;
    }
  }

  // @formatter:off
  private static final Map<String, Integer> NUMBERS = Map.of(
    "zero", Integer.valueOf(0),
    "one", Integer.valueOf(1),
    "two", Integer.valueOf(2),
    "three", Integer.valueOf(3),
    "four", Integer.valueOf(4),
    "five", Integer.valueOf(5),
    "six", Integer.valueOf(6),
    "seven", Integer.valueOf(7),
    "eight", Integer.valueOf(8),
    "nine", Integer.valueOf(9)
  );
  // @formatter:on
}
