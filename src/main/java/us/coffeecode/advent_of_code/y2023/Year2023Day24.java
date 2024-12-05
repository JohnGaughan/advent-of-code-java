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

import static java.math.MathContext.DECIMAL128;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2023, day = 24, title = "Never Tell Me The Odds")
@Component
public class Year2023Day24 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final List<Hailstone> hs = getInput(pc);
    final List<BigDecimal[]> slopeIntercepts = hs.stream()
                                                 .map(this::toSlopeIntercept)
                                                 .toList();
    final BigDecimal x_min = pc.getBigDecimal("AreaXmin");
    final BigDecimal x_max = pc.getBigDecimal("AreaXmax");
    final BigDecimal y_min = pc.getBigDecimal("AreaYmin");
    final BigDecimal y_max = pc.getBigDecimal("AreaYmax");
    long answer = 0;
    for (int i = 1; i < slopeIntercepts.size(); ++i) {
      final BigDecimal[] eq1 = slopeIntercepts.get(i);
      for (int j = 0; j < i; ++j) {
        final BigDecimal[] eq2 = slopeIntercepts.get(j);
        // Check lines not parallel
        if (eq1[0].equals(eq2[0])) {
          continue;
        }
        // Solve for the common X coordinate.
        final BigDecimal aDiff = eq2[0].subtract(eq1[0], DECIMAL128);
        final BigDecimal bDiff = eq1[1].subtract(eq2[1], DECIMAL128);
        final BigDecimal x = bDiff.divide(aDiff, DECIMAL128);
        if ((x.compareTo(x_min) < 0) || (x_max.compareTo(x) < 0)) {
          continue;
        }
        // X is within range: solve for Y.
        final BigDecimal ax = eq1[0].multiply(x, DECIMAL128);
        final BigDecimal y = ax.add(eq1[1], DECIMAL128);
        if ((y.compareTo(y_min) < 0) || (y_max.compareTo(y) < 0)) {
          continue;
        }
        // Lines intersect: need to know if this occurs in the past or future.
        final Hailstone hs1 = hs.get(i);
        final Hailstone hs2 = hs.get(j);
        if (hs1.isInFuture(x, y) && hs2.isInFuture(x, y)) {
          ++answer;
        }
      }
    }
    return answer;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final List<Hailstone> hs = getInput(pc);
    // Grab the first two hailstones, then find a trajectory that intercepts them. It must work for all hailstones.
    final Hailstone h1 = hs.get(0);
    final Hailstone h2 = hs.get(1);
    // These bounds work for the example and my input. Another input might have different velocities.
    final int[] x_s = IntStream.range(-400, 0)
                               .toArray();
    final int[] y_s = IntStream.range(0, 40)
                               .toArray();
    final int[] z_s = IntStream.range(0, 40)
                               .toArray();
    for (final int vx : x_s) {
      for (int vy : y_s) {
        for (int vz : z_s) {
          // Assume the rock stands still. Adjust the two hailstones by the rock's alleged velocity in X/Y, ignoring Z.
          final long h1_vx_diff = h1.vx - vx;
          final long h1_vy_diff = h1.vy - vy;
          final long h2_vx_diff = h2.vx - vx;
          final long h2_vy_diff = h2.vy - vy;
          final long divisor = (h1_vx_diff * h2_vy_diff) - (h1_vy_diff * h2_vx_diff);

          // Trajectory is such that the hailstones move parallel to each other: skip this.
          if (divisor == 0) {
            continue;
          }

          // Given the rock's velocity, find the time delta between hitting the two hailstones.
          final long dt = (h2_vy_diff * (h2.x - h1.x) - h2_vx_diff * (h2.y - h1.y)) / divisor;

          // Find the coordinates of the rock.
          final long x_rock = h1.x + (h1.vx * dt) - (vx * dt);
          final long y_rock = h1.y + (h1.vy * dt) - (vy * dt);
          final long z_rock = h1.z + (h1.vz * dt) - (vz * dt);

          // Now check against all hailstones, in all three dimensions.
          if (hs.stream()
                .allMatch(h -> {
                  // Find the time until the rock intercepts one of the hailstone's coordinates, then check that it
                  // intercepts
                  // all three coordinates.
                  final long time = (h.vx == vx) ? ((h.vy == vy) ? (z_rock - h.z) / (h.vz - vz) : (y_rock - h.y) / (h.vy - vy))
                    : ((x_rock - h.x) / (h.vx - vx));
                  return ((x_rock + (time * vx) == h.x + (time * h.vx)) && (y_rock + (time * vy) == h.y + (time * h.vy))
                    && (z_rock + (time * vz) == h.z + (time * h.vz)));
                })) {
            return x_rock + y_rock + z_rock;
          }
        }
      }
    }
    throw new RuntimeException("No answer found for puzzle context " + pc);
  }

  /** Convert a hailstone to its equivalent equation in slope-intercept form: y = ax + b. */
  private BigDecimal[] toSlopeIntercept(final Hailstone hs) {
    final BigDecimal[] slope_intercept = new BigDecimal[2];
    final BigDecimal x = BigDecimal.valueOf(hs.x);
    final BigDecimal y = BigDecimal.valueOf(hs.y);
    final BigDecimal vx = BigDecimal.valueOf(hs.vx);
    final BigDecimal vy = BigDecimal.valueOf(hs.vy);
    slope_intercept[0] = vy.divide(vx, DECIMAL128);
    slope_intercept[1] = y.subtract(x.multiply(slope_intercept[0], DECIMAL128), DECIMAL128);
    return slope_intercept;
  }

  /** Get the input as hailstones. */
  private List<Hailstone> getInput(final PuzzleContext pc) {
    return il.lines(pc)
             .stream()
             .map(this::make)
             .toList();
  }

  /** Make a hailstone from one line in the input file. */
  private Hailstone make(final String s) {
    final int comma1 = s.indexOf(',');
    final int comma2 = s.indexOf(',', comma1 + 1);
    final int at = s.indexOf('@', comma2);
    final int comma3 = s.indexOf(',', at);
    final int comma4 = s.indexOf(',', comma3 + 1);
    final long x = Long.parseLong(s.substring(0, comma1));
    final long y = Long.parseLong(s.substring(comma1 + 1, comma2)
                                   .trim());
    final long z = Long.parseLong(s.substring(comma2 + 1, at)
                                   .trim());
    final long dx = Long.parseLong(s.substring(at + 1, comma3)
                                    .trim());
    final long dy = Long.parseLong(s.substring(comma3 + 1, comma4)
                                    .trim());
    final long dz = Long.parseLong(s.substring(comma4 + 1)
                                    .trim());
    return new Hailstone(x, y, z, dx, dy, dz);
  }

  /** One hailstone in the storm. It has location and velocity in three dimensions. */
  private record Hailstone(long x, long y, long z, long vx, long vy, long vz) {

    /** Check whether the given X/Y coordinate is in the future path of this hailstone. */
    boolean isInFuture(final BigDecimal xx, final BigDecimal yy) {
      return (xx.subtract(BigDecimal.valueOf(x), DECIMAL128)
                .signum() == Long.signum(vx))
        && (yy.subtract(BigDecimal.valueOf(y), DECIMAL128)
              .signum() == Long.signum(vy));
    }
  }
}
