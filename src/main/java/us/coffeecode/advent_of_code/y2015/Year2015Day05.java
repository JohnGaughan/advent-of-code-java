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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2015, day = 5, title = "Doesn't He Have Intern-Elves For This?")
@Component
public final class Year2015Day05 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return il.lines(pc)
             .stream()
             .filter(this::isNicePart1)
             .count();
  }

  /** Determine if a string is nice per the requirements of part 1. */
  private boolean isNicePart1(final String str) {
    // 1. three vowels
    // 2. at least one double letter
    // 3. does not have ab, cd, pq, or xy
    int vowels = 0;
    boolean repeated = false;
    for (int i = 0; i < str.length(); ++i) {
      final char c = str.charAt(i);
      if ((c == 'a') || (c == 'e') || (c == 'i') || (c == 'o') || (c == 'u')) {
        ++vowels;
      }
      if (i > 0) {
        final char previous = str.charAt(i - 1);
        if (previous == c) {
          repeated = true;
        }
        if (((previous == 'a') && (c == 'b')) || ((previous == 'c') && (c == 'd')) || ((previous == 'p') && (c == 'q'))
          || ((previous == 'x') && (c == 'y'))) {
          return false;
        }
      }
    }
    return (vowels >= 3) && repeated;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return il.lines(pc)
             .stream()
             .filter(this::isNicePart2)
             .count();
  }

  /** Determine if a string is nice per the requirements of part 2. */
  private boolean isNicePart2(final String str) {
    // 1. pair of letters that appear at least twice without overlapping.
    // 2. contains at least one letter that repeats with another letter in between them.
    boolean pairRepeat = false;
    boolean singleRepeat = false;
    final Map<String, Set<Integer>> pairs = new HashMap<>();
    for (int i = 0; i < str.length() && !(singleRepeat && pairRepeat); ++i) {
      // Check for a single repeat two indices apart, e.g. "aba"
      if ((i > 1) && !singleRepeat) {
        singleRepeat = str.charAt(i) == str.charAt(i - 2);
      }
      // Check for a pair of characters already existing.
      if (i < str.length() - 1 && !pairRepeat) {
        final String thisPair = str.substring(i, i + 2);
        if (pairs.containsKey(thisPair)) {
          for (final int previousIndex : pairs.get(thisPair)) {
            // No overlap
            if (previousIndex + 2 <= i) {
              pairRepeat = true;
              break;
            }
          }
        }
        // First time this pair has been seen
        else {
          pairs.put(thisPair, new HashSet<>());
        }
        pairs.get(thisPair)
             .add(Integer.valueOf(i));
      }
    }
    return pairRepeat && singleRepeat;
  }

}
