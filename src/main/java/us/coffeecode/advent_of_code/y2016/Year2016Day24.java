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

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2016/day/24">Year 2016, day 24</a>. This is a maze traversal problem. The maze
 * contains several numbered points of interest, and we must visit them all in the least amount of steps. The two parts
 * differ in whether we must return to the starting position or not.
 * </p>
 * <p>
 * There are a two main steps here. First is calculate the distance between each pair of points. Second is iterate the
 * permutations of visit orders and see which route has the lowest cost. Calculating distances is a breadth-first search
 * that has an optimization where the algorithm first closes off dead ends. This is a simple linear operation that looks
 * at each tile in the maze and walls it off if it has three or four adjacent walls. This is fast and reduces the search
 * time in the next step. I use a library method to calculate permutations, then look up the route costs, total them up,
 * and keep track of the minimum-cost route.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2016Day24 {

  public long calculatePart1() {
    return calculate(false);
  }

  public long calculatePart2() {
    return calculate(true);
  }

  private long calculate(final boolean returnToStart) {
    final char[][] maze = getInput();
    reduce(maze);
    Point[] points = getPoints(maze);
    final int[][] distances = getDistances(maze, points);
    List<Integer> pointIds = new ArrayList<>(points.length - 1);
    for (int i = 1; i < points.length; ++i) {
      pointIds.add(Integer.valueOf(i));
    }
    int lowest = Integer.MAX_VALUE;
    for (List<Integer> route : Utils.permutations(pointIds)) {
      int previous = 0;
      int distance = 0;
      for (Integer node : route) {
        final int next = node.intValue();
        distance += distances[previous][next];
        previous = next;
      }
      if (returnToStart) {
        distance += distances[previous][0];
      }
      lowest = Math.min(distance, lowest);
    }
    return lowest;
  }

  /** Reduce the maze, returning true if reduced this time. */
  private void reduce(final char[][] maze) {
    boolean modified = false;
    // Maze has walls around it. We can move around inside those walls which makes logic simpler. Look for any square
    // with three walls around it and no number in its cell.
    do {
      modified = false;
      for (int y = 1; y < maze.length - 1; ++y) {
        for (int x = 1; x < maze[y].length - 1; ++x) {
          // Only process empty squares - skip walls and numbers.
          if (maze[y][x] != '.') {
            continue;
          }
          int walls = 0;
          if (maze[y + 1][x] == '#') {
            ++walls;
          }
          if (maze[y - 1][x] == '#') {
            ++walls;
          }
          if (maze[y][x + 1] == '#') {
            ++walls;
          }
          if (maze[y][x - 1] == '#') {
            ++walls;
          }
          if (walls > 2) {
            maze[y][x] = '#';
            modified = true;
          }
        }
      }
    } while (modified);
  }

  private Point[] getPoints(final char[][] maze) {
    final Map<Integer, Point> collect = new HashMap<>();
    for (int y = 0; y < maze.length; ++y) {
      for (int x = 0; x < maze[y].length; ++x) {
        if (Character.isDigit(maze[y][x])) {
          final Integer key = Integer.valueOf(Character.toString(maze[y][x]));
          final Point value = new Point(x, y);
          collect.put(key, value);
        }
      }
    }
    final Point[] points = new Point[collect.size()];
    for (Map.Entry<Integer, Point> entry : collect.entrySet()) {
      points[entry.getKey().intValue()] = entry.getValue();
    }
    return points;
  }

  private int[][] getDistances(final char[][] maze, final Point[] points) {
    final int[][] distances = new int[points.length][points.length];
    for (int i = 0; i < distances.length; ++i) {
      // For each point, get the distance to all points with a point ID greater than this one.
      for (int j = i + 1; j < distances.length; ++j) {
        distances[i][j] = distances[j][i] = getDistance(maze, points[i], points[j]);
      }
    }
    return distances;
  }

  private int getDistance(final char[][] maze, final Point start, final Point end) {
    // Perform a breadth-first search without any overlap.
    final Set<Point> visited = new HashSet<>();
    Set<Point> current = new HashSet<>();
    current.add(start);
    int distance = 0;
    while (!current.isEmpty()) {
      final Set<Point> nextCurrent = new HashSet<>();
      for (Point p : current) {
        if (p.equals(end)) {
          return distance;
        }
        else if (!visited.contains(p)) {
          visited.add(p);
          for (Point adjacent : adjacentTo(maze, p)) {
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

  private Set<Point> adjacentTo(final char[][] maze, final Point point) {
    final Set<Point> adjacent = new HashSet<>();
    if (maze[point.y - 1][point.x] != '#') {
      adjacent.add(new Point(point.x, point.y - 1));
    }
    if (maze[point.y + 1][point.x] != '#') {
      adjacent.add(new Point(point.x, point.y + 1));
    }
    if (maze[point.y][point.x - 1] != '#') {
      adjacent.add(new Point(point.x - 1, point.y));
    }
    if (maze[point.y][point.x + 1] != '#') {
      adjacent.add(new Point(point.x + 1, point.y));
    }
    return adjacent;
  }

  /** Get the input data for this solution. */
  private char[][] getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2016, 24)).stream().map(s -> s.toCharArray()).toArray(char[][]::new);
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final class Point {

    final int x;

    final int y;

    Point(final int _x, final int _y) {
      x = _x;
      y = _y;
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      else if (!(obj instanceof Point)) {
        return false;
      }
      final Point p = (Point) obj;
      return x == p.x && y == p.y;
    }

    @Override
    public int hashCode() {
      return Objects.hash(Integer.valueOf(x), Integer.valueOf(y));
    }

    @Override
    public String toString() {
      return "(" + x + "," + y + ")";
    }
  }

}
