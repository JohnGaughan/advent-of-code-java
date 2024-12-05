/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2023  John Gaughan
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
package us.coffeecode.advent_of_code.y2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Range3D;

@AdventOfCodeSolution(year = 2023, day = 22, title = "Sand Slabs")
@Component
public class Year2023Day22 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final List<Brick> bricks = getBricks(pc);

    long answer = 0;
    for (final Brick brick : bricks) {
      // We can safely remove a brick if all bricks directly above it have more than one support.
      boolean canRemove = true;
      for (final Brick above : brick.above) {
        if (above.below.size() == 1) {
          canRemove = false;
          break;
        }
      }
      if (canRemove) {
        ++answer;
      }
    }
    return answer;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final List<Brick> bricks = getBricks(pc);
    long answer = 0;
    // Test each brick by removing it, then seeing how many bricks would fall.
    for (final Brick brick : bricks) {
      // The removed set tracks all bricks that have been removed. The queue tracks bricks that need to be checked.
      final Set<Brick> removed = new HashSet<>();
      final Queue<Brick> removing = new LinkedList<>();

      removing.offer(brick);
      removed.add(brick);
      while (!removing.isEmpty()) {
        final Brick b = removing.poll();
        // Check every brick above this brick. If all of them lack supports, then remove that brick and enqueue it.
        for (final Brick above : b.above) {
          if (removed.containsAll(above.below)) {
            removed.add(above);
            removing.offer(above);
          }
        }
      }
      answer += (removed.size() - 1);
    }
    return answer;
  }

  /** Get the bricks after they have fallen onto the ground. */
  private List<Brick> getBricks(final PuzzleContext pc) {
    // Load the input, which is a snapshot of the bricks falling in mid-air.
    final List<Range3D> bricksInAir = il.linesAsObjects(pc, s -> new Range3D(Arrays.stream(SPLIT.split(s))
                                                                                   .mapToInt(Integer::parseInt)
                                                                                   .toArray()));

    // Construct a two-dimensional array that tracks the current height of each X/Y coordinate. As the bricks fall, this
    // tracks the floor of each coordinate so we know how far each brick can fall.
    final int x_max = 1 + bricksInAir.stream()
                                     .mapToInt(Range3D::getX2)
                                     .max()
                                     .getAsInt();
    final int y_max = 1 + bricksInAir.stream()
                                     .mapToInt(Range3D::getY2)
                                     .max()
                                     .getAsInt();
    final int[][] field = new int[y_max][x_max];

    // Build a mutable list of the bricks and sort them by Z1. Iteration order will then be by layer starting at the
    // ground and moving up.
    final List<Brick> bricks = bricksInAir.stream()
                                          .map(Brick::new)
                                          .collect(Collectors.toCollection(ArrayList::new));
    Collections.sort(bricks);

    // Iterate each brick and make it fall. At any point in iteration, there are no in-air bricks below the current
    // brick. We use a list iterator here because it allows both removal and insertion, i.e. replacing a brick with a
    // new one.
    for (final var iter = bricks.listIterator(); iter.hasNext();) {
      Brick brick = iter.next();
      // Determine how far this brick can fall. This is a delta-Z value, meaning that negative values are down and zero
      // is no shift. This should never be positive.
      final int dz = getZShift(brick, field);
      if (dz < 0) {
        // Remove the old brick. Replace it with a new one shifted down in the Z-axis such that it fell as far as it
        // can.
        iter.remove();
        brick = new Brick(brick.location.shiftZ(dz));
        iter.add(brick);
      }
      // Updated the height map by iterating the brick's X/Y coordinates and setting them to Z2, which is the higher of
      // its height values.
      for (int y = brick.location.getY1(); y <= brick.location.getY2(); ++y) {
        for (int x = brick.location.getX1(); x <= brick.location.getX2(); ++x) {
          field[y][x] = brick.location.getZ2();
        }
      }
    }

    // Now that we have bricks in ascending order, set up the above/below relationships.
    for (int i = 1; i < bricks.size(); ++i) {
      final Brick upper = bricks.get(i);
      for (int j = 0; j < i; ++j) {
        final Brick lower = bricks.get(j);
        if (lower.supports(upper)) {
          lower.above.add(upper);
          upper.below.add(lower);
        }
      }
    }

    return bricks;
  }

  /**
   * Given a brick, find the amount to shift it down such that it is resting either on the ground or on one or more
   * other bricks. This value can also be zero, meaning it is already resting on the ground. For any brick not at the
   * lowest Z-level, this should not return zero. It should always return negative, indicating a shift down, or zero,
   * indicating no shift.
   */
  private int getZShift(final Brick brick, final int[][] field) {
    int height = 0;
    for (int y = brick.location.getY1(); y <= brick.location.getY2(); ++y) {
      for (int x = brick.location.getX1(); x <= brick.location.getX2(); ++x) {
        height = Math.max(height, field[y][x]);
      }
    }
    // The shift down is the difference between the maximum height under the brick and its Z1 height, which is its lower
    // Z height. Since we want the brick to rest one higher than the obstruction underneath it, correct it by adding
    // one. Since the shift needs to be negative indicating down, we invert the operands from what is intuitive.
    return height - brick.location.getZ1() + 1;
  }

  private static final Pattern SPLIT = Pattern.compile("[^\\d]");

  /** Represents one brick's location as well as bricks that it touches both above and below. */
  private static final class Brick
  implements Comparable<Brick> {

    final Range3D location;

    final Set<Brick> above = new HashSet<>();

    final Set<Brick> below = new HashSet<>();

    Brick(final Range3D loc) {
      location = loc;
    }

    /** Get whether this brick directly supports the other brick. */
    boolean supports(final Brick other) {
      return (location.getZ2() == other.location.getZ1() - 1) && location.overlapsX(other.location)
        && location.overlapsY(other.location);
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }
      else if (obj instanceof Brick b) {
        return location.equals(b.location);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return location.hashCode();
    }

    @Override
    public int compareTo(final Brick o) {
      return Integer.compare(location.getZ1(), o.location.getZ1());
    }
  }
}
