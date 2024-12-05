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
package us.coffeecode.advent_of_code.y2016;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Collections2;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2016, day = 24, title = "Air Duct Spelunking")
@Component
public final class Year2016Day24 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc);
  }

  private long calculate(final PuzzleContext pc) {
    final int[][] maze = il.linesAsObjects(pc, s -> s.codePoints()
                                                     .toArray())
                           .toArray(int[][]::new);
    reduce(maze);
    Point2D[] points = getPoints(maze);
    final int[][] distances = getDistances(maze, points);
    List<Integer> pointIds = new ArrayList<>(points.length - 1);
    for (int i = 1; i < points.length; ++i) {
      pointIds.add(Integer.valueOf(i));
    }
    long lowest = Long.MAX_VALUE;
    for (final List<Integer> route : Collections2.permutations(pointIds)) {
      int previous = 0;
      int distance = 0;
      for (Integer node : route) {
        final int next = node.intValue();
        distance += distances[previous][next];
        previous = next;
      }
      if (pc.getBoolean("ReturnToStart")) {
        distance += distances[previous][0];
      }
      lowest = Math.min(distance, lowest);
    }
    return lowest;
  }

  /** Reduce the maze, removing dead ends. */
  private void reduce(final int[][] maze) {
    boolean modified = false;
    // Maze has walls around it. We can move around inside those walls which makes logic simpler. Look for any square
    // with three walls around it and no number in its cell.
    do {
      modified = false;
      for (int y = 1; y < maze.length - 1; ++y) {
        for (int x = 1; x < maze[y].length - 1; ++x) {
          final Point2D here = new Point2D(x, y);
          // Only process empty squares - skip walls and numbers.
          if (here.get(maze) == '.') {
            int walls = 0;
            for (final Point2D neighbor : here.getCardinalNeighbors()) {
              if (neighbor.get(maze) == '#') {
                ++walls;
              }
            }
            if (walls > 2) {
              here.set(maze, '#');
              modified = true;
            }
          }
        }
      }
    } while (modified);
  }

  private Point2D[] getPoints(final int[][] maze) {
    final Map<Integer, Point2D> collect = new HashMap<>();
    for (int y = 0; y < maze.length; ++y) {
      for (int x = 0; x < maze[y].length; ++x) {
        if (Character.isDigit(maze[y][x])) {
          final Integer key = Integer.valueOf(Character.toString(maze[y][x]));
          final Point2D value = new Point2D(x, y);
          collect.put(key, value);
        }
      }
    }
    final Point2D[] points = new Point2D[collect.size()];
    for (Map.Entry<Integer, Point2D> entry : collect.entrySet()) {
      points[entry.getKey()
                  .intValue()] = entry.getValue();
    }
    return points;
  }

  private int[][] getDistances(final int[][] maze, final Point2D[] points) {
    final int[][] distances = new int[points.length][points.length];
    for (int i = 0; i < distances.length; ++i) {
      // For each point, get the distance to all points with a point ID greater than this one.
      for (int j = i + 1; j < distances.length; ++j) {
        distances[i][j] = distances[j][i] = getDistance(maze, points[i], points[j]);
      }
    }
    return distances;
  }

  private int getDistance(final int[][] maze, final Point2D start, final Point2D end) {
    // Perform a breadth-first search without any overlap.
    final Set<Point2D> visited = new HashSet<>();
    Set<Point2D> current = new HashSet<>();
    current.add(start);
    int distance = 0;
    while (!current.isEmpty()) {
      final Set<Point2D> nextCurrent = new HashSet<>();
      for (final Point2D p : current) {
        if (p.equals(end)) {
          return distance;
        }
        else if (!visited.contains(p)) {
          visited.add(p);
          for (final Point2D adjacent : adjacentTo(maze, p)) {
            if (!visited.contains(adjacent)) {
              nextCurrent.add(adjacent);
            }
          }
        }
      }
      ++distance;
      current = nextCurrent;
    }
    return Integer.MAX_VALUE;
  }

  private Set<Point2D> adjacentTo(final int[][] maze, final Point2D point) {
    final Set<Point2D> adjacent = new HashSet<>();
    for (final Point2D neighbor : point.getCardinalNeighbors()) {
      if (neighbor.get(maze) != '#') {
        adjacent.add(neighbor);
      }
    }
    return adjacent;
  }

}
