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
package us.coffeecode.advent_of_code.y2020;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2020, day = 13, title = "Shuttle Search")
@Component
public final class Year2020Day13 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Input input = il.linesAsObject(pc, Input::new);
    for (long i = input.earliestDeparture; i < Integer.MAX_VALUE; ++i) {
      for (final Bus bus : input.buses) {
        if (i % bus.id == 0) {
          return (i - input.earliestDeparture) * bus.id;
        }
      }
    }
    return 0;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Bus[] buses = il.linesAsObject(pc, Input::new).buses;
    long increment = buses[0].id;
    long time = buses[0].id + buses[0].offset;
    for (int i = 1; i < buses.length; ++i) {
      while ((time + buses[i].offset) % buses[i].id != 0) {
        time += increment;
      }
      increment *= buses[i].id;
    }
    return time;
  }

  /** Represents properties of a single bus. */
  private static final class Bus {

    final long id;

    final long offset;

    Bus(final long i, final long o) {
      id = i;
      offset = o;
    }
  }

  /** Represents program input. */
  private static final class Input {

    private static final Pattern SPLIT = Pattern.compile(",");

    final long earliestDeparture;

    final Bus[] buses;

    Input(final List<String> lines) {
      earliestDeparture = Long.parseLong(lines.getFirst());
      final String[] tokens = SPLIT.split(lines.get(1));
      int totalBuses = tokens.length;
      for (final String token : tokens) {
        if ("x".equals(token)) {
          --totalBuses;
        }
      }
      buses = new Bus[totalBuses];
      int index = 0;
      for (int i = 0; i < tokens.length; ++i) {
        if (!"x".equals(tokens[i])) {
          buses[index++] = new Bus(Long.parseLong(tokens[i]), i);
        }
      }
    }

    @Override
    public String toString() {
      return "[" + earliestDeparture + "," + Arrays.deepToString(buses) + "]";
    }
  }
}
