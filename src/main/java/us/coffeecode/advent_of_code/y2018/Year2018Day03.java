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

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2018, day = 3, title = "No Matter How You Slice It")
@Component
public final class Year2018Day03 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final int size = 1_000;
    final int[][] fabric = new int[size][size];
    long result = 0;
    for (final Claim claim : il.linesAsObjects(pc, Claim::make)) {
      for (int y = claim.y1; y < claim.y2; ++y) {
        for (int x = claim.x1; x < claim.x2; ++x) {
          ++fabric[y][x];
          if (fabric[y][x] == 2) {
            ++result;
          }
        }
      }
    }
    return result;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Claim[] claims = il.linesAsObjects(pc, Claim::make)
                             .toArray(Claim[]::new);
    for (int i = 0; i < claims.length; ++i) {
      boolean overlaps = false;
      for (int j = 0; j < claims.length; ++j) {
        if (i == j) {
          continue;
        }
        if ((claims[i].x2 > claims[j].x1) && (claims[i].x1 < claims[j].x2) && (claims[i].y2 > claims[j].y1)
          && (claims[i].y1 < claims[j].y2)) {
          overlaps = true;
          break;
        }
      }
      if (!overlaps) {
        return claims[i].id;
      }
    }
    return 0;
  }

  private static record Claim(int id, int x1, int y1, int x2, int y2) {

    private static final Pattern DIGITS = Pattern.compile("\\d+");

    static Claim make(final String input) {
      final int[] i = DIGITS.matcher(input)
                            .results()
                            .map(r -> r.group())
                            .mapToInt(Integer::parseInt)
                            .toArray();
      return new Claim(i[0], i[1], i[2], i[1] + i[3], i[2] + i[4]);
    }

  }

}
