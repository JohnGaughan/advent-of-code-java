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
package us.coffeecode.advent_of_code.y2021;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2021, day = 24, title = "Arithmetic Logic Unit")
@Component
public final class Year2021Day24 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final int[] digits = new int[14];
    for (final Rule rule : getRules(pc)) {
      if (rule.diff < 0) {
        digits[rule.i0] = 9 + rule.diff;
        digits[rule.i1] = 9;
      }
      else {
        digits[rule.i0] = 9;
        digits[rule.i1] = 9 - rule.diff;
      }
    }
    return toLong(digits);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final int[] digits = new int[14];
    for (final Rule rule : getRules(pc)) {
      if (rule.diff < 0) {
        digits[rule.i0] = 1;
        digits[rule.i1] = 1 - rule.diff;
      }
      else {
        digits[rule.i0] = 1 + rule.diff;
        digits[rule.i1] = 1;
      }
    }
    return toLong(digits);
  }

  private long toLong(final int[] digits) {
    long result = 0;
    for (final int digit : digits) {
      result *= 10;
      result += digit;
    }
    return result;
  }

  private Collection<Rule> getRules(final PuzzleContext pc) {
    final int[][] magicNumbers = getMagicNumbers(pc);
    final List<Rule> rules = new ArrayList<>();

    final Deque<Rule> stack = new LinkedList<>();

    for (int i = 0; i < 14; ++i) {
      if (magicNumbers[1][i] == 1) {
        stack.push(new Rule(i, -1, magicNumbers[2][i]));
      }
      else {
        final Rule popped = stack.pop();
        rules.add(new Rule(i, popped.i0, popped.diff + magicNumbers[0][i]));
      }
    }

    return rules;
  }

  private record Rule(int i0, int i1, int diff) {}

  private int[][] getMagicNumbers(final PuzzleContext pc) {
    int[][] numbers = new int[3][14];
    int i0 = 0;
    int i1 = 0;
    int i2 = 0;
    int iLine = 0;
    for (final String line : il.lines(pc)) {
      final String[] tokens = SPLIT.split(line);
      if (line.startsWith("add x") && !line.contains("z")) {
        numbers[0][i0++] = Integer.parseInt(tokens[2]);
      }
      else if (line.startsWith("div z")) {
        numbers[1][i1++] = Integer.parseInt(tokens[2]);
      }
      else if (iLine % 18 == 15) {
        numbers[2][i2++] = Integer.parseInt(tokens[2]);
      }
      ++iLine;
    }
    return numbers;
  }

  private static final Pattern SPLIT = Pattern.compile(" ");
}
