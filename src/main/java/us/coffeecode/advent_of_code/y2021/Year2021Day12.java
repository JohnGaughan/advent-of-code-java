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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2021, day = 12, title = "Passage Pathing")
@Component
public final class Year2021Day12 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc);
  }

  private long calculate(final PuzzleContext pc) {
    return countPaths(getInput(pc), new Path(), pc.getBoolean("AllowOneSmallDuplicate"));
  }

  private long countPaths(final Map<String, Collection<String>> system, final Path pathSoFar, final boolean allowOneSmallDuplicate) {
    long paths = 0;
    final String previous = pathSoFar.elements[pathSoFar.elements.length - 1];
    for (final String next : system.get(previous)) {
      // Found the end node: add this path and complete.
      if (END.equals(next)) {
        ++paths;
      }

      // Next node is not the end, and it is legal to add it to the current path. Do so, then recurse.
      else {
        // Determine if we can visit this node, and whether there are any duplicate small caves.
        final boolean canVisit;
        final boolean duplicate;
        final boolean small = Character.isLowerCase(next.codePointAt(0));

        if (allowOneSmallDuplicate) {
          // Next is a small cave and it has already been visited.
          if (small && alreadyVisited(pathSoFar.elements, next)) {
            canVisit = !pathSoFar.duplicate;
            duplicate = true;
          }
          // Next is large, or it is small and not already visited.
          else {
            canVisit = true;
            duplicate = pathSoFar.duplicate;
          }
        }
        else {
          canVisit = !small || !alreadyVisited(pathSoFar.elements, next);
          duplicate = false;
        }

        if (canVisit) {
          final String[] nextPath = new String[pathSoFar.elements.length + 1];
          System.arraycopy(pathSoFar.elements, 0, nextPath, 0, pathSoFar.elements.length);
          nextPath[nextPath.length - 1] = next;
          paths += countPaths(system, new Path(nextPath, duplicate), allowOneSmallDuplicate);
        }
      }
    }
    return paths;
  }

  private boolean alreadyVisited(final String[] path, final String next) {
    // Arrays class only has binary search, which requires the input to be sorted.
    for (final String p : path) {
      if (p.equals(next)) {
        return true;
      }
    }
    return false;
  }

  private Map<String, Collection<String>> getInput(final PuzzleContext pc) {
    final Map<String, Collection<String>> system = new HashMap<>();
    for (final List<String> line : il.linesAsStrings(pc, SPLIT)) {
      for (final String token : line) {
        system.computeIfAbsent(token, s -> new ArrayList<>());
      }
      final String a = line.getFirst();
      final String b = line.get(1);
      // Add the mapping in both directions, except that start can never be a destination and end can never be a source.
      if (!END.equals(a) && !START.equals(b)) {
        system.get(a)
              .add(b);
      }
      if (!END.equals(b) && !START.equals(a)) {
        system.get(b)
              .add(a);
      }
    }
    return system;
  }

  private record Path(String[] elements, boolean duplicate) {

    Path() {
      this(new String[] { START }, false);
    }
  }

  private static final String START = "start";

  private static final String END = "end";

  private static final Pattern SPLIT = Pattern.compile("-");
}
