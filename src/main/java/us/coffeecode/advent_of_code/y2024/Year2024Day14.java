/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2024  John Gaughan
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
package us.coffeecode.advent_of_code.y2024;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2024, day = 14, title = "Restroom Redoubt")
@Component
public class Year2024Day14 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final List<Robot> robots = getInput(pc);
    final int width = pc.getInt("width");
    final int height = pc.getInt("height");
    final List<Point2D> locations = robots.stream()
                                          .map(r -> getFinalLocation(r, width, height, 100))
                                          .toList();
    return getSafetyFactor(locations, width, height);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final List<Robot> robots = getInput(pc);
    final int width = pc.getInt("width");
    final int height = pc.getInt("height");
    final int max_iterations = width * height;

    long bestSafetyFactor = Long.MAX_VALUE;
    long answer = Long.MAX_VALUE;

    for (int i = 1; i < max_iterations; ++i) {
      final long iterations = i;
      final List<Point2D> locations = robots.stream()
                                            .map(r -> getFinalLocation(r, width, height, iterations))
                                            .toList();
      final long safetyFactor = getSafetyFactor(locations, width, height);
      if (safetyFactor < bestSafetyFactor) {
        answer = iterations;
        bestSafetyFactor = safetyFactor;
      }
    }
    return answer;
  }

  /** Get the final location for the given robot after the specified number of iterations. */
  private Point2D getFinalLocation(final Robot robot, final long width, final long height, final long iterations) {
    // Parameters are type long
    int x = (int) ((robot.start.getX() + (iterations * robot.v.getX())) % width);
    int y = (int) ((robot.start.getY() + (iterations * robot.v.getY())) % height);
    // Java modulo can produce negative numbers, so correct for that.
    if (x < 0) {
      x += width;
    }
    if (y < 0) {
      y += height;
    }
    return new Point2D(x, y);
  }

  /** Get the safety factor when robots are in the given locations, for a board with the given dimensions. */
  private long getSafetyFactor(final Collection<? extends Point2D> locations, final int width, final int height) {
    final int[] robotQuadrants = locations.stream()
                                          .mapToInt(p -> getQuadrant(p, width, height))
                                          .toArray();
    final long[] quadrants = new long[5];
    for (final int robotQuadrant : robotQuadrants) {
      ++quadrants[robotQuadrant];
    }
    return quadrants[1] * quadrants[2] * quadrants[3] * quadrants[4];
  }

  /** Get the Cartesian quadrant for the robot, or zero if it is on a line between quadrants. */
  private int getQuadrant(final Point2D robot, final int width, final int height) {
    final int center_x = (width >> 1);
    final int center_y = (height >> 1);
    if (robot.getX() > center_x) {
      if (robot.getY() > center_y) {
        // 1: upper right
        return 1;
      }
      else if (robot.getY() < center_y) {
        // 4: lower right
        return 4;
      }
    }
    else if (robot.getX() < center_x) {
      if (robot.getY() > center_y) {
        // 2: upper left
        return 2;
      }
      else if (robot.getY() < center_y) {
        // 3: lower left
        return 3;
      }
    }
    // 0: on a boundary
    return 0;
  }

  /** Get all of the robots comprising the program input. */
  private List<Robot> getInput(final PuzzleContext pc) {
    return il.lines(pc)
             .stream()
             .map(this::parse)
             .toList();
  }

  /** Parse a single line of input text into a robot record. */
  private Robot parse(final String line) {
    final int eq1 = line.indexOf('=');
    final int comma1 = line.indexOf(',');
    final int space = line.indexOf(' ');
    final int eq2 = line.indexOf('=', space);
    final int comma2 = line.indexOf(',', eq2 + 1);
    final int px = Integer.parseInt(line.substring(eq1 + 1, comma1));
    final int py = Integer.parseInt(line.substring(comma1 + 1, space));
    final int vx = Integer.parseInt(line.substring(eq2 + 1, comma2));
    final int vy = Integer.parseInt(line.substring(comma2 + 1));
    return new Robot(new Point2D(px, py), new Point2D(vx, vy));
  }

  /** Record that models a robot which has a starting location and velocity. */
  private record Robot(Point2D start, Point2D v) {}
}
