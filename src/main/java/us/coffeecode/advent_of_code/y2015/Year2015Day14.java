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
package us.coffeecode.advent_of_code.y2015;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2015, day = 14)
@Component
public final class Year2015Day14 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final int time = pc.getInt("time");
    return il.linesAsObjects(pc, Rule::make)
             .stream()
             .mapToLong(r -> getPosition(r, time))
             .max()
             .getAsLong();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final int time = pc.getInt("time");
    final Iterable<Rule> rules = il.linesAsObjects(pc, Rule::make);
    // Initialize maps of points and locations
    final Map<String, Long> points = new HashMap<>();
    final Map<String, Long> locations = new HashMap<>();
    for (final Rule rule : rules) {
      points.put(rule.reindeer, Long.valueOf(0));
    }
    // Run the race
    for (int i = 1; i <= time; ++i) {
      // Update the locations
      long farthest = 0;
      for (final Rule rule : rules) {
        final long location = getPosition(rule, i);
        if (location > farthest) {
          farthest = location;
        }
        locations.put(rule.reindeer, Long.valueOf(location));
      }
      // Increase points for each reindeer in the lead.
      for (final Map.Entry<String, Long> location : locations.entrySet()) {
        if (location.getValue()
                    .longValue() == farthest) {
          final String key = location.getKey();
          final long point = 1 + points.get(key)
                                       .longValue();
          points.put(key, Long.valueOf(point));
        }
      }
    }
    return points.values()
                 .stream()
                 .mapToLong(Long::longValue)
                 .max()
                 .getAsLong();
  }

  /** Get a reindeer's position after a given amount of elapsed time. */
  private long getPosition(final Rule rule, final int time) {
    final int cycleTime = rule.flyTime + rule.restTime;
    final int cycles = time / cycleTime;
    final int finalCycle = Math.min(time % cycleTime, rule.flyTime);
    return rule.velocity * ((cycles * rule.flyTime) + finalCycle);
  }

  /** Represents one rule in the input file. */
  private static final record Rule(String reindeer, int velocity, int flyTime, int restTime) {

    private static final Pattern SPLIT = Pattern.compile(" ");

    static Rule make(final String line) {
      final String[] tokens = SPLIT.split(line);
      return new Rule(tokens[0], Integer.parseInt(tokens[3]), Integer.parseInt(tokens[6]), Integer.parseInt(tokens[13]));
    }

  }

}
