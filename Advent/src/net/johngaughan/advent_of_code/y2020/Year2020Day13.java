/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2020  John Gaughan
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
package net.johngaughan.advent_of_code.y2020;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <p>
 * <a href="https://adventofcode.com/2015/day/13">Year 2015, day 13</a>. This problem is an exercise in quotients and
 * remainders. Part one asks to find the next bus ID that will arrive. Part two asks us to find a time when all buses
 * will return at the same time.
 * </p>
 * <p>
 * Part one is a simple matter of checking each minute to see if any bus evenly divides the current time. Part two is a
 * bit more involved, and brute force is not feasible. There are actually two ways to solve part two. One is to apply
 * the Chinese remainder theorem. The other is to perform a heavily optimized search. Since that solution is far simpler
 * than the Chinese remainder theorem and it completes in a trivial amount of time, it is good enough.
 * </p>
 * <p>
 * The algorithm first solves the trivial base case, that is, the time when the first bus will be at the station. This
 * is simply its ID and offset added together. We know that from here, it will only return at times equal to this
 * initial time plus some multiple of its ID. This means we can set an increment equal to its ID, and only check those
 * multiples. Now we solve it for the combination of this base case and the next input, iterating through all of the
 * inputs.
 * </p>
 * <p>
 * For each subsequent bus we keep adding that increment until the bus ID evenly divides the sum of the current time and
 * the bus's offset. This is necessarily a solution to all of the previous buses: the increment ensures that. Now we
 * multiply the increment by the bus ID, ensuring that future iterations also solve the current iteration.
 * </p>
 * <p>
 * It is also worth pointing out that the maximum interval is all of the bus IDs multiplied together, which is a little
 * bit over 836 trillion. The actual solution is close to that maximum value, which helps explain why brute force is not
 * viable: that is a massive amount of times to check.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2020Day13 {

  public int calculatePart1(final Path path) {
    final Input input = parse(path);
    for (int i = input.earliestDeparture; (i < Integer.MAX_VALUE) && (i >= 0); ++i) {
      for (final Bus bus : input.buses) {
        if ((i % bus.id) == 0) {
          return (i - input.earliestDeparture) * bus.id;
        }
      }
    }
    return Integer.MIN_VALUE;
  }

  public long calculatePart2(final Path path) {
    final Bus[] buses = parse(path).buses;
    long increment = buses[0].id;
    long time = buses[0].id + buses[0].offset;
    for (int i = 1; i < buses.length; ++i) {
      while (((time + buses[i].offset) % buses[i].id) != 0) {
        time += increment;
      }
      increment *= buses[i].id;
    }
    return time;
  }

  /** Parse the file located at the provided path location. */
  private Input parse(final Path path) {
    try {
      return new Input(Files.readAllLines(path));
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /** Represents properties of a single bus. */
  private static final class Bus {

    final int id;

    final int offset;

    Bus(final int i, final int o) {
      id = i;
      offset = o;
    }
  }

  /** Represents program input. */
  private static final class Input {

    private static final Pattern SPLIT = Pattern.compile(",");

    final int earliestDeparture;

    final Bus[] buses;

    Input(final List<String> lines) {
      earliestDeparture = Integer.parseInt(lines.get(0));
      final String[] tokens = SPLIT.split(lines.get(1));
      int totalBuses = tokens.length;
      for (String token : tokens) {
        if ("x".equals(token)) {
          --totalBuses;
        }
      }
      buses = new Bus[totalBuses];
      int index = 0;
      for (int i = 0; i < tokens.length; ++i) {
        if (!"x".equals(tokens[i])) {
          buses[index++] = new Bus(Integer.parseInt(tokens[i]), i);
        }
      }
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      return "[" + earliestDeparture + "," + Arrays.deepToString(buses) + "]";
    }
  }
}
