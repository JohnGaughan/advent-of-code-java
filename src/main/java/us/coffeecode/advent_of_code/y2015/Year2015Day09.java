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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Collections2;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2015, day = 9, title = "All in a Single Night")
@Component
public final class Year2015Day09 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Map<String, Map<String, Long>> distances = getInput(pc);
    return calculateDistances(distances).stream()
                                        .mapToLong(Long::longValue)
                                        .min()
                                        .getAsLong();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Map<String, Map<String, Long>> distances = getInput(pc);
    return calculateDistances(distances).stream()
                                        .mapToLong(Long::longValue)
                                        .max()
                                        .getAsLong();
  }

  /** Calculate all unique distances. */
  private Set<Long> calculateDistances(final Map<String, Map<String, Long>> distances) {
    final Collection<List<String>> routes = Collections2.permutations(new ArrayList<>(distances.keySet()));
    final Set<Long> results = new HashSet<>();
    for (final List<String> route : routes) {
      results.add(calculateDistance(route, distances));
    }
    return results;
  }

  /** Get the distance for a specific route. */
  private Long calculateDistance(final List<String> route, final Map<String, Map<String, Long>> distances) {
    long distance = 0;
    String previousLocation = null;
    for (final String location : route) {
      if (previousLocation != null) {
        distance += distances.get(previousLocation)
                             .get(location)
                             .longValue();
      }
      previousLocation = location;
    }
    return Long.valueOf(distance);
  }

  private static Pattern SPLIT = Pattern.compile(" (=|to) ");

  /** Get the input data for this solution. */
  private Map<String, Map<String, Long>> getInput(final PuzzleContext pc) {
    final Map<String, Map<String, Long>> distances = new HashMap<>();
    for (final String line : il.lines(pc)) {
      final String[] tokens = SPLIT.split(line);
      final Long distance = Long.valueOf(tokens[2]);
      if (!distances.containsKey(tokens[0])) {
        distances.put(tokens[0], new HashMap<>());
      }
      if (!distances.containsKey(tokens[1])) {
        distances.put(tokens[1], new HashMap<>());
      }
      distances.get(tokens[0])
               .put(tokens[1], distance);
      distances.get(tokens[1])
               .put(tokens[0], distance);
    }
    return distances;
  }

}
