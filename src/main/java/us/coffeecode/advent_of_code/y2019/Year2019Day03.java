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
package us.coffeecode.advent_of_code.y2019;

import java.util.Arrays;
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
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2019, day = 3, title = "Crossed Wires")
@Component
public final class Year2019Day03 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Map<Point2D, Map<Wire, Integer>> map = getMap(pc);
    long result = Long.MAX_VALUE;
    for (final var entry : map.entrySet()) {
      final Point2D p = entry.getKey();
      if (!p.isOrigin()) {
        if (entry.getValue().size() > 1) {
          final int distance = p.getManhattanDistance();
          result = Math.min(distance, result);
        }
      }
    }
    return result;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Map<Point2D, Map<Wire, Integer>> map = getMap(pc);
    long result = Long.MAX_VALUE;
    for (final var distances : map.values()) {
      if (distances.size() > 1) {
        final int steps = distances.get(Wire.A).intValue() + distances.get(Wire.B).intValue();
        result = Math.min(steps, result);
      }
    }
    return result;
  }

  private Map<Point2D, Map<Wire, Integer>> getMap(final PuzzleContext pc) {
    final List<List<Step>> input = getInput(pc);
    final Map<Point2D, Map<Wire, Integer>> map = new HashMap<>(1 << 19);
    for (int i = 0; i < 2; ++i) {
      final List<Step> paths = input.get(i);
      final Wire wire = (i == 0) ? Wire.A : Wire.B;
      int x = 0;
      int y = 0;
      int d = 0;
      for (final Step path : paths) {
        for (int j = 0; j < path.distance; ++j) {
          if (path.direction == 'L') {
            --x;
          }
          else if (path.direction == 'R') {
            ++x;
          }
          else if (path.direction == 'U') {
            --y;
          }
          else if (path.direction == 'D') {
            ++y;
          }
          ++d;
          final Point2D p = new Point2D(x, y);
          if (map.containsKey(p)) {
            final Map<Wire, Integer> value = map.get(p);
            if (!value.containsKey(wire)) {
              value.put(wire, Integer.valueOf(d));
            }
          }
          else {
            final Map<Wire, Integer> value = new HashMap<>();
            value.put(wire, Integer.valueOf(d));
            map.put(p, value);
          }
        }
      }
    }
    return map;
  }

  private static final Pattern SPLIT = Pattern.compile(",");

  /** Get the input data for this solution. */
  private List<List<Step>> getInput(final PuzzleContext pc) {
    return il.linesAsObjects(pc,
      s -> Arrays.stream(SPLIT.split(s)).map(t -> new Step(t.codePointAt(0), Integer.parseInt(t.substring(1)))).toList());
  }

  private static enum Wire {
    A,
    B;
  }

  private static record Step(int direction, int distance) {}
}
