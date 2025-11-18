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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2018, day = 2)
@Component
public final class Year2018Day02 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    long twos = 0;
    long threes = 0;
    for (final String line : il.lines(pc)) {
      final int[] counts = new int[26];
      for (int ch : line.codePoints()
                        .toArray()) {
        ++counts[ch - 'a'];
      }
      boolean countedTwo = false;
      boolean countedThree = false;
      for (int count : counts) {
        if (!countedTwo && (count == 2)) {
          ++twos;
          countedTwo = true;
        }
        if (!countedThree && (count == 3)) {
          ++threes;
          countedThree = true;
        }
      }
    }
    return twos * threes;
  }

  @Solver(part = 2)
  public String calculatePart2(final PuzzleContext pc) {
    final List<String> strings = il.lines(pc);
    for (int i = 0; i < strings.size() - 1; ++i) {
      for (int j = i + 1; j < strings.size(); ++j) {
        final String solution = ham(strings.get(i), strings.get(j));
        if (solution != null) {
          return solution;
        }
      }
    }
    return "NO SOLUTION";
  }

  private String ham(final String str1, final String str2) {
    int difference = -1;
    for (int i = 0; i < str1.length(); ++i) {
      if (str1.codePointAt(i) != str2.codePointAt(i)) {
        if (difference >= 0) {
          // Already found a difference, this cannot be a solution.
          return null;
        }
        difference = i;
      }
    }
    return str1.substring(0, difference) + str1.substring(difference + 1);
  }

}
