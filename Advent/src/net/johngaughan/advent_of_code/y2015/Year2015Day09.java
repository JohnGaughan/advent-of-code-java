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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import net.johngaughan.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2015/day/9">Year 2015, day 9</a>. This problem states that Santa wants to visit
 * eight locations, visiting each one once. He can start and finish at any location. Each pair of locations is separated
 * by a particular distance. Part one asks for the route that minimizes the total distance, while part two asks for the
 * maximum distance.
 * </p>
 * <p>
 * This is a basic Hamiltonian path. The locations are vertices in a graph, while the distances between them are edges.
 * Thankfully, this graph is fully-connected: all locations are connected to all other locations. This reduces the
 * problem from finding complete paths with their distances to a simpler to solve one, which is how I implemented this.
 * </p>
 * <p>
 * My solution calculates all of the permutations of routes first, using a simple exhaustive depth-first algorithm. Then
 * it calculates distances second, not even bothering to correlate them with routes since it does not matter, at all.
 * These problems ask for integer answers, not all the intermediate data. That simplifies the algorithm and data
 * structures. Furthermore, a brute force algorithm is suitable given the small input size. If this were larger, then
 * optimizations would be required to keep the time and space complexity reasonable: the number of permutations grows
 * quickly, scaling with O(n!).
 * </p>
 * <p>
 * Ideally this program would prune the solution space as it goes along in order to reduce the time and space
 * complexity, but doing so would increase the code complexity. I do not believe that is a worthwhile tradeoff for this
 * specific code challenge.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2015Day09 {

  public long calculatePart1(final Path path) {
    final Map<String, Map<String, Long>> distances = parse(path);
    return calculateDistances(distances).stream().mapToLong(l -> l).min().getAsLong();
  }

  public long calculatePart2(final Path path) {
    final Map<String, Map<String, Long>> distances = parse(path);
    return calculateDistances(distances).stream().mapToLong(l -> l).max().getAsLong();
  }

  /** Calculate all unique distances. */
  private Set<Long> calculateDistances(final Map<String, Map<String, Long>> distances) {
    final Set<List<String>> routes = Utils.permutations(new ArrayList<>(distances.keySet()));
    final Set<Long> results = new HashSet<>();
    for (final List<String> route : routes) {
      results.add(calculateDistance(route, distances));
    }
    return results;
  }

  /** Get the distance for a specific route. */
  private long calculateDistance(final List<String> route, final Map<String, Map<String, Long>> distances) {
    long distance = 0;
    String previousLocation = null;
    for (final String location : route) {
      if (previousLocation != null) {
        distance += distances.get(previousLocation).get(location);
      }
      previousLocation = location;
    }
    return distance;
  }

  private static Pattern SPLIT = Pattern.compile(" (=|to) ");

  /** Parse the file located at the provided path location. */
  private Map<String, Map<String, Long>> parse(final Path path) {
    try {
      final Map<String, Map<String, Long>> distances = new HashMap<>();
      for (String line : Files.readAllLines(path)) {
        String[] tokens = SPLIT.split(line);
        Long distance = Long.valueOf(tokens[2]);
        if (!distances.containsKey(tokens[0])) {
          distances.put(tokens[0], new HashMap<>());
        }
        if (!distances.containsKey(tokens[1])) {
          distances.put(tokens[1], new HashMap<>());
        }
        distances.get(tokens[0]).put(tokens[1], distance);
        distances.get(tokens[1]).put(tokens[0], distance);
      }
      return distances;
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
