/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2022  John Gaughan
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
package us.coffeecode.advent_of_code.y2022;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2022, day = 16, title = "Proboscidea Volcanium")
@Component
public class Year2022Day16 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculatePart1(getValves(pc), ZERO, 0, 0, 30);
  }

  private long calculatePart1(final Map<Long, Valve> valves, final Long location, final long visited, final int time, final int timeLimit) {
    if (time >= timeLimit) {
      return 0;
    }
    final Valve current = valves.get(location);
    // Open the current position
    long newVisited = visited;
    long pressure = 0;
    if (current.flowRate > 0) {
      newVisited |= location.longValue();
      pressure = current.flowRate * (timeLimit - time);
    }
    // Visit other nodes
    long visitingPressure = 0;
    for (final var visiting : current.targets.entrySet()) {
      if ((newVisited & visiting.getKey().longValue()) == 0) {
        final long x = calculatePart1(valves, visiting.getKey(), newVisited | visiting.getKey().longValue(),
          time + visiting.getValue().intValue(), timeLimit);
        visitingPressure = Math.max(visitingPressure, x);
      }
    }
    return pressure + visitingPressure;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Map<Long, Valve> valves = getValves(pc);
    final Map<Long, Long> paths = new HashMap<>(1 << 13);

    // Get all paths and the best score for each.
    getPaths(valves, new State(0, ZERO, 0, 0), paths, new HashMap<>(1 << 16), 26);
    final long[] keys = paths.keySet().stream().mapToLong(Long::longValue).toArray();

    // Get scores for all all disjoint pairs of paths
    final Set<Long> scores = new HashSet<>(1 << 12);
    for (int i = 0; i < keys.length - 1; ++i) {
      for (int j = i + 1; j < keys.length; ++j) {
        // No bits in common: they did not visit any of the same nodes.
        if ((keys[i] & keys[j]) == 0) {
          final long value1 = paths.get(Long.valueOf(keys[i])).longValue();
          final long value2 = paths.get(Long.valueOf(keys[j])).longValue();
          scores.add(Long.valueOf(value1 + value2));
        }
      }
    }
    return scores.stream().mapToLong(Long::longValue).max().getAsLong();
  }

  private void getPaths(final Map<Long, Valve> valves, final State state, final Map<Long, Long> paths, final Map<String, Long> seen, final int timeLimit) {
    for (final var candidate : valves.get(state.location).targets.entrySet()) {
      final int nextTime = state.time + candidate.getValue().intValue();
      long destination = candidate.getKey().longValue();
      if ((nextTime <= timeLimit) && ((state.visited & destination) == 0)) {
        final long nextVisited = (state.visited | destination);
        final Long nextPosition = candidate.getKey();
        final Valve nextValve = valves.get(candidate.getKey());
        final int addScore = nextValve.flowRate * (timeLimit - nextTime);
        final State next = new State(nextVisited, nextPosition, nextTime, state.score + addScore);
        final String key = next.toString();
        if (seen.containsKey(key)) {
          final int previousScore = seen.get(key).intValue();
          if (previousScore >= next.score) {
            continue;
          }
        }
        seen.put(key, Long.valueOf(next.score));
        getPaths(valves, next, paths, seen, timeLimit);
      }
    }
    // Always consider the option of not moving: either we are at the end of a path, or need to avoid visiting a node so
    // the other player can do so more efficiently.
    final Long key = Long.valueOf(state.visited);
    final Long value = Long.valueOf(state.score);
    if (!paths.containsKey(key) || (paths.getOrDefault(key, ZERO).compareTo(value) < 0)) {
      paths.put(key, value);
    }
  }

  private static final Long ZERO = Long.valueOf(0);

  private static final Pattern SPLIT = Pattern.compile(" ");

  private record Valve(long id, int flowRate, Map<Long, Integer> targets) {}

  private record State(long visited, Long location, int time, long score) {

    @Override
    public String toString() {
      // Score deliberately omitted
      return "[" + visited + ":" + location + ":" + time + "]";
    }
  }

  /** Load all valves and map each ID to the corresponding valves. */
  private Map<Long, Valve> getValves(final PuzzleContext pc) {
    final List<String> lines = il.lines(pc);

    // Parse each line and map string IDs to target string IDs. We need to do this because nodes have cross-references,
    // but we load them one at a time. This is the first stage of loading the final objects.
    final Map<String, String[]> rawNodes = new TreeMap<>();
    final Map<String, Integer> rawFlow = new HashMap<>();
    for (final String line : lines) {
      final String[] tokens = SPLIT.split(line);
      final String[] targets = new String[tokens.length - 9];
      for (int i = 9; i < tokens.length; ++i) {
        final int comma = tokens[i].indexOf(',');
        if (comma > 0) {
          targets[i - 9] = tokens[i].substring(0, comma);
        }
        else {
          targets[i - 9] = tokens[i];
        }
      }
      rawFlow.put(tokens[1], Integer.valueOf(tokens[4].substring(5, tokens[4].length() - 1)));
      rawNodes.put(tokens[1], targets);
    }

    // Convert each map string ID to a long ID and remap flow rates.
    final Map<String, Integer> rawIdToRealId = new HashMap<>();
    final Map<Integer, Integer> flowRates = new HashMap<>();
    int counter = -1;
    for (final String key : rawNodes.keySet().stream().sorted().toArray(String[]::new)) {
      final Integer intId = Integer.valueOf(++counter);
      rawIdToRealId.put(key, intId);
      flowRates.put(intId, rawFlow.get(key));
    }

    // Build a new mapping of long IDs to their connected long IDs.
    final Map<Integer, int[]> connections = new HashMap<>();
    for (var entry : rawNodes.entrySet()) {
      final int value[] = Arrays.stream(entry.getValue()).mapToInt(s -> rawIdToRealId.get(s).intValue()).toArray();
      connections.put(rawIdToRealId.get(entry.getKey()), value);
    }

    // Floyd–Warshall algorithm - calculate weights from and to each node.
    final int[][] dist = new int[counter + 1][counter + 1];

    for (var entry : connections.entrySet()) {
      final int u = entry.getKey().intValue();
      Arrays.fill(dist[u], 9999);
      for (final int v : entry.getValue()) {
        dist[u][v] = 1;
      }
      dist[u][u] = 0;
    }

    for (int k = 0; k < dist.length; ++k) {
      for (int i = 0; i < dist.length; ++i) {
        for (int j = 0; j < dist.length; ++j) {
          if (dist[i][j] > dist[i][k] + dist[k][j]) {
            dist[i][j] = dist[i][k] + dist[k][j];
          }
        }
      }
    }

    // Mark node 0 as not a destination.
    for (final int[] row : dist) {
      row[0] = 0;
    }

    // Mark all nodes that need to be omitted from the node construction because they are not endpoints.
    final Set<Integer> deletions = new HashSet<>();
    for (int i = 1; i < dist.length; ++i) {
      final Integer flow = flowRates.get(Integer.valueOf(i));
      if (flow.intValue() == 0) {
        deletions.add(Integer.valueOf(i));
      }
    }

    // Next, rename all nodes so they are all identified by a single bit.
    final Map<Integer, Long> oldToNew = new HashMap<>();
    long newKey = 0;
    for (int i = 0; i < dist.length; ++i) {
      final Integer key = Integer.valueOf(i);
      if (!deletions.contains(key)) {
        oldToNew.put(key, Long.valueOf(newKey));
        if (newKey == 0) {
          newKey = 1;
        }
        else {
          newKey <<= 1;
        }
      }
    }

    // Finally, build the graph and return it.
    final Map<Long, Valve> valves = new HashMap<>();
    for (int i = 0; i < dist.length; ++i) {
      final Integer key = Integer.valueOf(i);
      if (!deletions.contains(key)) {
        final Map<Long, Integer> movements = new HashMap<>();
        for (int j = 1; j < dist[i].length; ++j) {
          final Integer targetKey = Integer.valueOf(j);
          if (!deletions.contains(targetKey)) {
            // Add one for the time to open the valve
            movements.put(oldToNew.get(Integer.valueOf(j)), Integer.valueOf(1 + dist[i][j]));
          }
        }
        final Long valveKey = oldToNew.get(key);
        valves.put(valveKey, new Valve(i, flowRates.get(key).intValue(), movements));
      }
    }
    return valves;
  }
}
