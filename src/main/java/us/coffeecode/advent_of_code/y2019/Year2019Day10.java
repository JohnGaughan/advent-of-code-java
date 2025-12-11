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

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.MyIntMath;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2019, day = 10)
@Component
public final class Year2019Day10 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return getVisible(getInput(pc)).values()
                                   .stream()
                                   .mapToLong(Long::longValue)
                                   .max()
                                   .getAsLong();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    // Load the asteroids and make the Death Star to destroy them.
    final Set<Point2D> asteroids = getInput(pc);
    final Map<Point2D, Long> visible = getVisible(asteroids);
    final Point2D deathStar = visible.entrySet()
                                     .stream()
                                     .max((a, b) -> a.getValue()
                                                     .compareTo(b.getValue()))
                                     .get()
                                     .getKey();

    // Arrange the asteroids based on angle (map key) and store them in an ordered set closest to farthest (map value).
    final Map<Point2D, NavigableSet<Point2D>> anglesToAsteroids = new TreeMap<>(new CircularAngleComparator());
    final Comparator<Point2D> comp = new AsteroidDistanceComparator(deathStar);
    for (final Point2D other : asteroids) {
      if (!deathStar.equals(other)) {
        // Get the angle (key)
        final Point2D key = getAngle(deathStar, other);

        // Add it to the coordinates
        anglesToAsteroids.putIfAbsent(key, new TreeSet<>(comp));
        anglesToAsteroids.get(key)
                         .add(other);
      }
    }

    // Now spin around and zap them.
    int i = 0;
    Point2D lastDestroyed = null;
    // Iterate until the 200th asteroid is destroyed.
    while (i < 200) {
      // Keep iterating over the asteroids. When the iterator is exhausted, start over.
      for (final var iter = anglesToAsteroids.entrySet()
                                             .iterator(); iter.hasNext();) {
        var entry = iter.next();
        ++i;

        lastDestroyed = entry.getValue()
                             .pollFirst();

        // Remove this entry if there is nothing along this line, so the code above will work as expected.
        if (entry.getValue()
                 .isEmpty()) {
          iter.remove();
        }

        if (i == 200) {
          break;
        }
      }
    }

    return (lastDestroyed == null) ? 0L : (lastDestroyed.getX() * 100L) + lastDestroyed.getY();
  }

  /** Map all asteroids to a count of other asteroids it can see. */
  private Map<Point2D, Long> getVisible(final Iterable<Point2D> asteroids) {
    final Map<Point2D, Long> visible = new HashMap<>();
    for (final Point2D asteroid : asteroids) {
      final Set<Point2D> matches = new HashSet<>();
      for (final Point2D other : asteroids) {
        if (!asteroid.equals(other)) {
          matches.add(getAngle(asteroid, other));
        }
      }
      visible.put(asteroid, Long.valueOf(matches.size()));
    }
    return visible;
  }

  /** Get the angle (rise and run) in reduced form from me to you. */
  private Point2D getAngle(final Point2D me, final Point2D you) {
    final int dx = you.getX() - me.getX();
    final int dy = you.getY() - me.getY();
    final int gcd = MyIntMath.gcd(Math.abs(dx), Math.abs(dy));
    return new Point2D(dx / gcd, dy / gcd);
  }

  /** Get the input data for this solution. */
  private Set<Point2D> getInput(final PuzzleContext pc) {
    final int[][] input = il.linesAsCodePoints(pc);
    final Set<Point2D> asteroids = new HashSet<>();
    for (int y = 0; y < input.length; ++y) {
      for (int x = 0; x < input[y].length; ++x) {
        if (input[y][x] == '#') {
          asteroids.add(new Point2D(x, y));
        }
      }
    }
    return asteroids;
  }

  /** Compares two angles and orders them with due north being lowest, and increasing clockwise. */
  private static final class CircularAngleComparator
  implements Comparator<Point2D> {

    @Override
    public int compare(final Point2D o1, final Point2D o2) {
      // Compare quadrants first for a quick win.
      if (getQuadrant(o1) != getQuadrant(o2)) {
        return getQuadrant(o1) - getQuadrant(o2);
      }

      // Check for vertical lines since they have infinite slope.
      if (o1.getX() == 0) {
        if (o2.getX() == 0) {
          return 0;
        }
        return (o1.getY() > 0) ? -1 : 1;
      }
      else if (o2.getX() == 0) {
        if (o1.getX() == 0) {
          return 0;
        }
        return (o2.getY() > 0) ? 1 : -1;
      }

      final int quadrant = getQuadrant(o1);
      final double o1slope = getSlope(o1);
      final double o2slope = getSlope(o2);

      if (quadrant < 2) {
        return Double.compare(o2slope, o1slope);
      }

      return Double.compare(o1slope, o2slope);
    }

    /**
     * Treating the point as an angle, return the quadrant for sorting purposes: this is not the typical mathematical
     * quadrant.
     */
    int getQuadrant(final Point2D point) {
      if (point.getX() >= 0) {
        if (point.getY() >= 0) {
          return 0;
        }
        else {
          return 1;
        }
      }
      else if (point.getY() >= 0) {
        return 2;
      }
      else {
        return 3;
      }
    }

    /** Treating the point as an angle, get the slope of the line it describes. */
    double getSlope(final Point2D point) {
      if (point.getX() == 0) {
        return (point.getY() < 0) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
      }
      return (double) point.getY() / (double) point.getX();
    }

  }

  /**
   * Compare the distance between one asteroid and a fixed point against the distance of a second asteroid and the same
   * fixed point. This is used to order asteroids that lie along the same line from the Death Star, closest to farthest.
   */
  private static final class AsteroidDistanceComparator
  implements Comparator<Point2D> {

    final Point2D origin;

    AsteroidDistanceComparator(final Point2D _origin) {
      origin = _origin;
    }

    @Override
    public int compare(final Point2D o1, final Point2D o2) {
      return Integer.compare(o1.getManhattanDistance(origin), o2.getManhattanDistance(origin));
    }

  }

}
