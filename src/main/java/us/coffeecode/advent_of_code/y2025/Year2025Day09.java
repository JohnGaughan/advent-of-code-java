/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2025  John Gaughan
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
package us.coffeecode.advent_of_code.y2025;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2025, day = 9)
@Component
public class Year2025Day09 {

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
    final List<Point2D> points = il.linesAsObjects(pc, Point2D::valueOf);
    // Only do bounds checking in part two.
    final Collection<Segment> segments = (pc.getPart() == 2 ? findSegments(points) : List.of());
    long maxArea = -1;
    for (int i = 0; i < points.size() - 1; ++i) {
      final Point2D p1 = points.get(i);
      for (int j = i + 1; j < points.size(); ++j) {
        final Point2D p2 = points.get(j);
        // Part one: segments will be empty and this will always be true.
        if (segments.stream()
                    .allMatch(b -> b.isValid(p1, p2))) {
          // Add one because the ranges are exclusive, not inclusive.
          final long x = Math.abs((p1.getX() - p2.getX())) + 1;
          final long y = Math.abs((p1.getY() - p2.getY())) + 1;
          maxArea = Math.max(maxArea, x * y);
        }
      }
    }
    return maxArea;
  }

  /**
   * Given the points that sequentially define corners of a polygon, find all segments that together represent its
   * perimeter.
   */
  private List<Segment> findSegments(final List<Point2D> points) {
    final List<Segment> segments = new ArrayList<>(points.size());
    for (int i = 0; i < points.size(); ++i) {
      final Point2D p1 = points.get(i);
      // Last segment wraps around to the start
      final Point2D p2 = points.get((i + 1) % points.size());
      segments.add(new Segment(p1, p2));
    }
    return segments;
  }

  /** Represents one segment on the big input polygon. */
  private static final class Segment {

    private final int x1;

    private final int x2;

    private final int y1;

    private final int y2;

    Segment(final Point2D p1, final Point2D p2) {
      x1 = Math.min(p1.getX(), p2.getX());
      x2 = Math.max(p1.getX(), p2.getX());
      y1 = Math.min(p1.getY(), p2.getY());
      y2 = Math.max(p1.getY(), p2.getY());
    }

    /**
     * Determines if the rectangle bounded by the given points is valid for the current polygon segment. To be valid,
     * the segment cannot be contained within the bounding rectangle, although it may overlap on the rectangle's
     * perimeter.
     */
    public boolean isValid(final Point2D p1, final Point2D p2) {
      final int r_x1 = Math.min(p1.getX(), p2.getX());
      final int r_x2 = Math.max(p1.getX(), p2.getX());
      final int r_y1 = Math.min(p1.getY(), p2.getY());
      final int r_y2 = Math.max(p1.getY(), p2.getY());
      return (x2 <= r_x1) || (x1 >= r_x2) || (y2 <= r_y1) || (y1 >= r_y2);
    }
  }
}
