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

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2019, day = 18)
@Component
public final class Year2019Day18 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final int[][] map = getInput(pc);
    return calculate(map);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final int[][] map = getInput(pc);
    return calculate(map);
  }

  private long calculate(final int[][] map) {
    final Map<Symbols, Collection<RouteSegment>> routeSegments = getRouteSegments(map);
    final int keyCount = (int) Arrays.stream(map)
                                     .mapToLong(a -> Arrays.stream(a)
                                                           .filter(i -> ('a' <= i) && (i <= 'z'))
                                                           .count())
                                     .sum();
    final Route route = getShortestRoute(routeSegments, keyCount);
    return route.distance;
  }

  /** Aggregate routes into larger routes that pick up all keys. */
  private Route getShortestRoute(final Map<Symbols, Collection<RouteSegment>> segments, final int keyCount) {
    Route result = null;
    final int robots = countRobots(segments);
    // Maps key status to distance.
    final Map<Integer, Long> cache = new HashMap<>(1 << 16);
    for (int i = 0; i < robots; ++i) {
      for (final RouteSegment fromStart : segments.get(new Symbols(Integer.valueOf(START), Integer.valueOf(i)))) {
        if (fromStart.doors == 0) {
          final Route candidate =
            getShortestRoute(segments, new Route(fromStart, robots, i, keyCount), maskFor(fromStart.endSymbol), cache, robots);
          if ((candidate != null) && ((result == null) || (candidate.distance < result.distance))) {
            result = candidate;
          }
        }
      }
    }
    return result;
  }

  private int countRobots(final Map<Symbols, Collection<RouteSegment>> segments) {
    int result = 0;
    for (final var entry : segments.entrySet()) {
      result = Math.max(result, entry.getKey().b.intValue());
    }
    return result + 1;
  }

  private Route getShortestRoute(final Map<Symbols, Collection<RouteSegment>> segments, final Route current, final int keys, final Map<Integer, Long> cache, final int robots) {
    Route result = null;
    for (int robot = 0; robot < robots; ++robot) {
      for (final RouteSegment segment : segments.get(current.currentSymbols.get(robot))) {
        if ((keys & maskFor(segment.endSymbol)) > 0) {
          // Already picked up this key.
          continue;
        }
        if ((segment.doors & keys) - segment.doors != 0) {
          // Cannot unlock the doors on the way to this key.
          continue;
        }
        final Route next = new Route(current, segment, robot);
        final Integer cacheKey = next.getCacheKey();
        if (cache.containsKey(cacheKey)) {
          final int oldDistance = cache.get(cacheKey)
                                       .intValue();
          if (next.distance < oldDistance) {
            cache.put(cacheKey, Long.valueOf(next.distance));
          }
          else {
            continue;
          }
        }
        else {
          cache.put(cacheKey, Long.valueOf(next.distance));
        }
        final Route candidate;
        if (next.isComplete()) {
          candidate = next;
        }
        else {
          candidate = getShortestRoute(segments, next, keys | maskFor(segment.endSymbol), cache, robots);
        }
        if ((candidate != null) && ((result == null) || (candidate.distance < result.distance))) {
          result = candidate;
        }
      }
    }
    return result;
  }

  /**
   * Get the routes between the starting locations and all keys. Each route tracks its length and what doors are in the
   * way. That way, we can check if a given route is viable at any time given the then-current key status.
   */
  private Map<Symbols, Collection<RouteSegment>> getRouteSegments(final int[][] map) {
    final Map<Symbols, Collection<RouteSegment>> routes = new HashMap<>(64);
    int start = 0;
    for (int y = 1; y < map.length - 1; ++y) {
      for (int x = 1; x < map[y].length - 1; ++x) {
        final int ch = map[y][x];
        if ((ch == START) || Character.isLowerCase(ch)) {
          final Symbols key;
          if (ch == START) {
            key = new Symbols(Integer.valueOf(ch), Integer.valueOf(start));
            ++start;
          }
          else {
            key = new Symbols(Integer.valueOf(ch), ZERO);
          }
          routes.put(key, getRouteSegments(map, new Point2D(x, y)));
        }
      }
    }
    return routes;
  }

  private Collection<RouteSegment> getRouteSegments(final int[][] map, final Point2D start) {
    final Collection<RouteSegment> routes = new ArrayList<>(52);
    // Maze has no loops so we never need to consider previous locations for any purpose.
    final Set<Point2D> visited = new HashSet<>(map.length * map.length);
    visited.add(start);
    Map<Point2D, RouteSegment> visiting = new HashMap<>();
    final int startSymbol = start.get(map);
    visiting.put(start, new RouteSegment(startSymbol));
    while (!visiting.isEmpty()) {
      final Map<Point2D, RouteSegment> nextVisiting = new HashMap<>();
      for (final var entry : visiting.entrySet()) {
        final Point2D here = entry.getKey();
        final RouteSegment soFar = entry.getValue();
        for (final Point2D neighbor : here.getCardinalNeighbors()) {
          final int ch = neighbor.get(map);
          if ((ch == WALL) || visited.contains(neighbor)) {
            continue;
          }
          if (Character.isLowerCase(ch)) {
            // Found a destination.
            routes.add(new RouteSegment(soFar, ch, 0));
          }
          final int doorMask = Character.isUpperCase(ch) ? maskFor(ch) : 0;
          nextVisiting.put(neighbor, new RouteSegment(soFar, doorMask));
          visited.add(neighbor);
        }
      }
      visiting = nextVisiting;
    }
    return routes;
  }

  /**
   * Reduce the map. This finds dead-ends and fills them in. Filling them in with walls essentially reduces the search
   * space by removing routes that cannot possibly be part of a solution. This also removes doors that are dead ends: we
   * have no reason to open those doors so it makes sense to remove them.
   */
  private void reduce(final int[][] map) {
    boolean modified = true;
    while (modified) {
      modified = false;
      for (int y = 1; y < map.length - 1; ++y) {
        for (int x = 1; x < map[y].length - 1; ++x) {
          if ((map[y][x] == EMPTY) || Character.isUpperCase(map[y][x])) {
            int walls = 0;
            if (map[y - 1][x] == WALL) {
              ++walls;
            }
            if (map[y + 1][x] == WALL) {
              ++walls;
            }
            if (map[y][x - 1] == WALL) {
              ++walls;
            }
            if (map[y][x + 1] == WALL) {
              ++walls;
            }
            if (walls > 2) {
              map[y][x] = WALL;
              modified = true;
            }
          }
        }
      }
    }
  }

  private int[][] getInput(final PuzzleContext pc) {
    final int[][] map = il.linesAsCodePoints(pc);
    if (pc.getBoolean("ReplaceMiddle")) {
      final Point2D start = getStartLocation(map);
      final int x = start.getX();
      final int y = start.getY();
      map[y][x] = map[y - 1][x] = map[y + 1][x] = map[y][x - 1] = map[y][x + 1] = WALL;
      map[y - 1][x - 1] = map[y - 1][x + 1] = map[y + 1][x - 1] = map[y + 1][x + 1] = START;
    }
    reduce(map);
    return map;
  }

  private Point2D getStartLocation(final int[][] map) {
    for (int y = 1; y < map.length - 1; ++y) {
      for (int x = 1; x < map[y].length - 1; ++x) {
        if (START == map[y][x]) {
          return new Point2D(x, y);
        }
      }
    }
    return null;
  }

  @SuppressWarnings("unused")
  private void print(final int[][] map) {
    for (int y = 0; y < map.length; ++y) {
      for (int x = 0; x < map[y].length; ++x) {
        System.out.print(Character.toString(map[y][x]));
      }
      System.out.println();
    }
  }

  private static final class Route {

    private final List<List<RouteSegment>> segments;

    private final long distance;

    private final int keys;

    private final List<Symbols> currentSymbols;

    private final Integer cacheKey;

    private final int size;

    final int totalKeys;

    Route(final RouteSegment route, final int robots, final int robot, final int _totalKeys) {
      segments = new ArrayList<>(robots);
      for (int i = 0; i < robots; ++i) {
        if (robot == i) {
          segments.add(Collections.singletonList(route));
        }
        else {
          segments.add(Collections.emptyList());
        }
      }
      distance = route.distance;
      keys = maskFor(route.endSymbol);
      currentSymbols = new ArrayList<>(robots);
      for (int i = 0; i < robots; ++i) {
        final Integer ii = Integer.valueOf(i);
        if (i == robot) {
          currentSymbols.add(new Symbols(Integer.valueOf(route.endSymbol), ZERO));
        }
        else {
          currentSymbols.add(new Symbols(Integer.valueOf(START), ii));
        }
      }
      cacheKey = Integer.valueOf(Objects.hash(currentSymbols, Integer.valueOf(keys)));
      size = 1;
      totalKeys = _totalKeys;
    }

    Route(final Route previous, final RouteSegment route, final int robot) {
      segments = new ArrayList<>(previous.segments.size());
      for (int i = 0; i < previous.segments.size(); ++i) {
        final List<RouteSegment> previousSegment = previous.segments.get(i);
        if (i == robot) {
          final List<RouteSegment> segment = new ArrayList<>(previousSegment.size() + 1);
          segment.addAll(previousSegment);
          segment.add(route);
          segments.add(segment);
        }
        else {
          segments.add(previousSegment);
        }
      }
      distance = previous.distance + route.distance;
      keys = previous.keys | maskFor(route.endSymbol);
      currentSymbols = new ArrayList<>(previous.currentSymbols.size());
      for (int i = 0; i < previous.currentSymbols.size(); ++i) {
        if (i == robot) {
          currentSymbols.add(new Symbols(Integer.valueOf(route.endSymbol), ZERO));
        }
        else {
          currentSymbols.add(previous.currentSymbols.get(i));
        }
      }
      cacheKey = Integer.valueOf(Objects.hash(currentSymbols, Integer.valueOf(keys)));
      size = previous.size + 1;
      totalKeys = previous.totalKeys;
    }

    Integer getCacheKey() {
      return cacheKey;
    }

    boolean isComplete() {
      return size == totalKeys;
    }

  }

  private static record Symbols(Integer a, Integer b) {}

  private static record RouteSegment(int startSymbol, int endSymbol, int distance, int doors) {

    RouteSegment(final int _startSymbol) {
      this(_startSymbol, 0, 0, 0);
    }

    RouteSegment(final RouteSegment previous, final int doorMask) {
      this(previous.startSymbol, 0, 1 + previous.distance, previous.doors | doorMask);
    }

    RouteSegment(final RouteSegment previous, final int _endSymbol, final int doorMask) {
      this(previous.startSymbol, _endSymbol, 1 + previous.distance, previous.doors | doorMask);
    }
  }

  private static final int maskFor(final int doorOrKey) {
    if (('a' <= doorOrKey) && (doorOrKey <= 'z')) {
      return 1 << (doorOrKey - 'a');
    }
    else if (('A' <= doorOrKey) && (doorOrKey <= 'Z')) {
      return 1 << (doorOrKey - 'A');
    }
    throw new IllegalArgumentException();
  }

  private static final char WALL = '#';

  private static final char EMPTY = '.';

  private static final char START = '@';

  private static final Integer ZERO = Integer.valueOf(0);
}
