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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;
import us.coffeecode.advent_of_code.util.Range;

@AdventOfCodeSolution(year = 2022, day = 15)
@Component
public class Year2022Day15 {

  /*
   * Note: part 1 appears to have an off-by-one error but only with the example input.
   */

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final int y = pc.getInt("y");
    final List<Input> inputs = il.linesAsObjects(pc, Input::make);
    final Set<Integer> beacons = inputs.stream()
                                       .filter(p -> y == p.beacon.getY())
                                       .mapToInt(p -> p.beacon.getX())
                                       .boxed()
                                       .collect(Collectors.toSet());
    final List<Range> ranges = new ArrayList<>();
    for (final Input input : inputs) {
      final int rowDiff = Math.abs(input.sensor.getY() - y);
      if (rowDiff <= input.range) {
        final int dx = input.range - rowDiff;
        int x1 = input.sensor.getX() - dx;
        if (beacons.contains(Integer.valueOf(x1))) {
          ++x1;
        }
        int x2 = input.sensor.getX() + dx;
        if (beacons.contains(Integer.valueOf(x2))) {
          --x2;
        }
        if (x1 <= x2) {
          ranges.add(new Range(x1, x2));
        }
      }
    }
    return Range.merge(ranges)
                .stream()
                .mapToLong(Range::sizeInclusive)
                .sum();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final List<Input> inputs = il.linesAsObjects(pc, Input::make);

    for (final Point2D candidate : getIntersections(pc, inputs)) {
      // If this intersection is not within any sensor's area, it is the unique solution.
      if (inputs.stream()
                .noneMatch(p -> p.sensor.getManhattanDistance(candidate) <= p.range)) {
        return candidate.getX() * X_MULTIPLIER + candidate.getY();
      }
    }
    throw new IllegalStateException("Answer not found");
  }

  /** Get all intersections between lines that are inside the problem's boundaries. */
  private Set<Point2D> getIntersections(final PuzzleContext pc, final List<Input> inputs) {
    final int maxXY = pc.getInt("maxXY");
    final List<Line> lines = getLines(inputs);
    final Set<Point2D> results = new HashSet<>();
    for (int i = 0; i < lines.size() - 1; ++i) {
      final Line line1 = lines.get(i);
      for (int j = i + 1; j < lines.size(); ++j) {
        final Line line2 = lines.get(j);
        if (line1.slope != line2.slope) {
          final int y = (line1.y_intercept + line2.y_intercept) / 2;
          final int x = y - Math.min(line1.y_intercept, line2.y_intercept);
          if (x >= 0 && x <= maxXY && y >= 0 && y <= maxXY) {
            results.add(new Point2D(x, y));
          }
        }
      }
    }
    return results;
  }

  /**
   * Get all lines tangent to sensor diamonds that are duplicated, that is, two sensors have a one-unit gap between them
   * along an edge.
   */
  private List<Line> getLines(final List<Input> inputs) {
    final Set<Line> found = new HashSet<>(inputs.size() << 3);
    final List<Line> lines = new ArrayList<>();
    for (final Input input : inputs) {
      final int x = input.sensor.getX();
      final int y1 = input.sensor.getY() - input.range - 1;
      final int y2 = input.sensor.getY() + input.range + 1;
      for (final Line line : List.of(new Line(1, y1 - x), new Line(-1, y1 + x), new Line(1, y2 - x), new Line(-1, y2 + x))) {
        if (!found.add(line)) {
          lines.add(line);
        }
      }
    }
    return lines;
  }

  private record Line(int slope, int y_intercept) {}

  private static final long X_MULTIPLIER = 4_000_000L;

  private record Input(Point2D sensor, Point2D beacon, int range) {

    private static final Pattern SPLIT = Pattern.compile(" ");

    static Input make(final String input) {
      final String[] tokens = SPLIT.split(input);
      final int x1 = Integer.parseInt(tokens[2].substring(2, tokens[2].length() - 1));
      final int y1 = Integer.parseInt(tokens[3].substring(2, tokens[3].length() - 1));
      final int x2 = Integer.parseInt(tokens[8].substring(2, tokens[8].length() - 1));
      final int y2 = Integer.parseInt(tokens[9].substring(2));
      final Point2D sensor = new Point2D(x1, y1);
      final Point2D beacon = new Point2D(x2, y2);
      return new Input(sensor, beacon, sensor.getManhattanDistance(beacon));
    }
  }
}
