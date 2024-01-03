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
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.MyLongMath;

@AdventOfCodeSolution(year = 2019, day = 12, title = "The N-Body Problem")
@Component
public final class Year2019Day12 {

  private static final int DIMENSIONS = 3;

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Moon[] moons = il.linesAsObjects(pc, Moon::new).toArray(Moon[]::new);
    final int iterations = pc.getInt("iterations");
    for (int iteration = 0; iteration < iterations; ++iteration) {
      // Apply gravity to velocity
      for (int i = 0; i < moons.length - 1; ++i) {
        for (int j = i + 1; j < moons.length; ++j) {
          for (int dimension = 0; dimension < DIMENSIONS; ++dimension) {
            if (moons[i].location[dimension] < moons[j].location[dimension]) {
              ++moons[i].velocity[dimension];
              --moons[j].velocity[dimension];
            }
            else if (moons[i].location[dimension] > moons[j].location[dimension]) {
              --moons[i].velocity[dimension];
              ++moons[j].velocity[dimension];
            }
          }
        }
      }

      // Apply velocity to position.
      for (final Moon moon : moons) {
        for (int i = 0; i < moon.location.length; ++i) {
          moon.location[i] += moon.velocity[i];
        }
      }
    }
    return Arrays.stream(moons).mapToInt(Moon::getEnergy).sum();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    // Get the period of each dimension
    final long periods[] = new long[DIMENSIONS];

    for (int dimension = 0; dimension < DIMENSIONS; ++dimension) {
      // Find the period of each dimension separately.
      final Moon[] moons = il.linesAsObjects(pc, Moon::new).toArray(Moon[]::new);
      // Make it big enough to avoid resizing later. Shaves off 20% time.
      final Set<State> seen = new HashSet<>(1 << 19);
      State state = new State(moons, dimension);
      while (!seen.contains(state)) {
        seen.add(state);
        // Apply gravity to velocity
        for (int i = 0; i < moons.length - 1; ++i) {
          for (int j = i + 1; j < moons.length; ++j) {
            if (moons[i].location[dimension] < moons[j].location[dimension]) {
              ++moons[i].velocity[dimension];
              --moons[j].velocity[dimension];
            }
            else if (moons[i].location[dimension] > moons[j].location[dimension]) {
              --moons[i].velocity[dimension];
              ++moons[j].velocity[dimension];
            }
          }
        }

        // Apply velocity to position.
        for (final Moon moon : moons) {
          moon.location[dimension] += moon.velocity[dimension];
        }
        state = new State(moons, dimension);
      }
      periods[dimension] = seen.size();
    }

    return Arrays.stream(periods).limit(3).reduce(1, MyLongMath::lcm);
  }

  /**
   * Snapshot of the state of a moon system in one dimension, including their locations and velocities in that
   * dimension.
   */
  private static final class State {

    final int[] state;

    final int hashCode;

    State(final Moon[] moons, final int dimension) {
      state = new int[moons.length << 1];
      for (int i = 0; i < moons.length; ++i) {
        state[i] = moons[i].location[dimension];
        state[i + moons.length] = moons[i].velocity[dimension];
      }
      hashCode = Arrays.hashCode(state);
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      else if (obj instanceof State o) {
        return Arrays.equals(state, o.state);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return hashCode;
    }

  }

  /** Tracks the changing state of a single moon. */
  private static final class Moon {

    final int[] location = new int[DIMENSIONS];

    final int[] velocity = new int[DIMENSIONS];

    Moon(final String line) {
      int beforeStart = line.indexOf('=');
      int end = line.indexOf(',');
      location[0] = Integer.parseInt(line.substring(beforeStart + 1, end));
      beforeStart = line.indexOf('=', end);
      end = line.indexOf(',', beforeStart);
      location[1] = Integer.parseInt(line.substring(beforeStart + 1, end));
      beforeStart = line.indexOf('=', end);
      location[2] = Integer.parseInt(line.substring(beforeStart + 1, line.length() - 1));
    }

    int getEnergy() {
      final int potential = Arrays.stream(location).map(Math::abs).sum();
      final int kinetic = Arrays.stream(velocity).map(Math::abs).sum();
      return potential * kinetic;
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      else if (obj instanceof Moon o) {
        return Arrays.equals(location, o.location) && Arrays.equals(velocity, o.velocity);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return Arrays.deepHashCode(new int[][] { location, velocity });
    }

    @Override
    public String toString() {
      return Arrays.toString(location) + "->" + Arrays.toString(velocity);
    }
  }

}
