/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2022  John Gaughan
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
package us.coffeecode.advent_of_code.y2022;

import java.util.Arrays;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Range;

@AdventOfCodeSolution(year = 2022, day = 4, title = "Camp Cleanup")
@Component
public class Year2022Day04 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return Arrays.stream(getInput(pc)).filter(a -> a[0].containsInclusive(a[1]) || a[1].containsInclusive(a[0])).count();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return Arrays.stream(getInput(pc)).filter(a -> a[0].overlaps(a[1])).count();
  }

  private Range[][] getInput(final PuzzleContext pc) {
    return il.linesAsObjectsArray(pc, RANGES_SPLIT, s -> {
      final String[] parts = SPLIT.split(s);
      return new Range(parts[0], parts[1]);
    }, Range[]::new, Range[][]::new);
  }

  private static final Pattern RANGES_SPLIT = Pattern.compile(",");

  private static final Pattern SPLIT = Pattern.compile("-");
}
