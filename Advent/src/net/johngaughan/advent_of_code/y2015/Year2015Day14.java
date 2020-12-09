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
package net.johngaughan.advent_of_code.y2015;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * <a href="https://adventofcode.com/2015/day/13">Year 2015, day 13</a>. This problem asks us to race reindeer. In part
 * one, we simply run the reindeer and see who progressed the farthest after the number of seconds in the race. In part
 * two, we need to determine who is in the lead each second and award a point to that reindeer. Rather than seeing who
 * made it the farthest, the winner is the one who spent the most time in the lead.
 * </p>
 * <p>
 * Part one is easy because we can directly calculate a reindeer's location after a given amount of time. Simply
 * calculate each one and map rules (representing reindeer) to their distance traveled, and use the streams interface to
 * get the maximum distance. For part two, we can still use that calculation method, but we need to apply it each second
 * and track each reindeer's points and locations along the way. Then we can get the maximum number of points among all
 * the reindeer.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2015Day14 {

  private static final int TIME = 2503;

  public int calculatePart1(final Path path) {
    return parse(path).stream().mapToInt(r -> getPosition(r, TIME)).max().getAsInt();
  }

  public int calculatePart2(final Path path) {
    final Collection<Rule> rules = parse(path);
    // Initialize maps of points and locations
    final Map<String, Integer> points = new HashMap<>();
    final Map<String, Integer> locations = new HashMap<>();
    for (final Rule rule : rules) {
      points.put(rule.reindeer, 0);
    }
    // Run the race
    for (int i = 1; i <= TIME; ++i) {
      // Update the locations
      int farthest = 0;
      for (Rule rule : rules) {
        final int location = getPosition(rule, i);
        if (location > farthest) {
          farthest = location;
        }
        locations.put(rule.reindeer, location);
      }
      // Increase points for each reindeer in the lead.
      for (final Map.Entry<String, Integer> location : locations.entrySet()) {
        if (location.getValue() == farthest) {
          final String key = location.getKey();
          int point = 1 + points.get(key);
          points.put(key, point);
        }
      }
    }
    return points.values().stream().mapToInt(i -> i).max().getAsInt();
  }

  /** Get a reindeer's position after a given amount of elapsed time. */
  private int getPosition(final Rule rule, final int time) {
    final int cycles = time / rule.cycleTime;
    final int finalCycle = Math.min(time % rule.cycleTime, rule.flyTime);
    return rule.velocity * ((cycles * rule.flyTime) + finalCycle);
  }

  /** Parse the file located at the provided path location. */
  private Collection<Rule> parse(final Path path) {
    try {
      return Files.readAllLines(path).stream().map(s -> new Rule(s)).collect(Collectors.toList());
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /** Represents one rule in the input file. */
  private static final class Rule {

    private static final Pattern SPLIT = Pattern.compile(" ");

    final String reindeer;

    final int velocity;

    final int flyTime;

    final int restTime;

    final int cycleTime;

    Rule(final String line) {
      final String[] tokens = SPLIT.split(line);
      reindeer = tokens[0];
      velocity = Integer.parseInt(tokens[3]);
      flyTime = Integer.parseInt(tokens[6]);
      restTime = Integer.parseInt(tokens[13]);
      cycleTime = flyTime + restTime;
    }
  }

}
