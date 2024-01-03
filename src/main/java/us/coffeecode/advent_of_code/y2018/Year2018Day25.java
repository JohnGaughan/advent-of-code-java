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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2018, day = 25, title = "Four-Dimensional Adventure")
@Component
public final class Year2018Day25 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final List<List<int[]>> constellations = new ArrayList<>(400);

    // Build the initial constellations.
    for (final int[] point : il.linesAs2dIntArrayFromSplit(pc, SPLIT)) {

      // Check if this point belongs to an existing constellation.
      List<int[]> previousConstellation = null;
      for (final var iter = constellations.iterator(); iter.hasNext();) {
        final var constellation = iter.next();
        boolean compatible = compatible(point, constellation);
        if (compatible) {
          // Not already added to a constellation: add it.
          if (previousConstellation == null) {
            constellation.add(point);
            previousConstellation = constellation;
          }

          // Already added to a constellation: merge the constellations.
          else {
            previousConstellation.addAll(constellation);
            iter.remove();
          }
        }
      }

      // Otherwise, make a new constellation.
      if (previousConstellation == null) {
        final List<int[]> constellation = new ArrayList<>(400);
        constellation.add(point);
        constellations.add(constellation);
      }
    }
    return constellations.size();
  }

  private boolean compatible(final int[] point, final List<int[]> constellation) {
    for (final int[] point2 : constellation) {
      final int distance = Math.abs(point[0] - point2[0]) + Math.abs(point[1] - point2[1]) + Math.abs(point[2] - point2[2])
        + Math.abs(point[3] - point2[3]);
      if (distance <= 3) {
        return true;
      }
    }
    return false;
  }

  private static final Pattern SPLIT = Pattern.compile(",");
}
