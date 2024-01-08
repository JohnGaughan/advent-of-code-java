/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2023  John Gaughan
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
package us.coffeecode.advent_of_code.y2023;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2023, day = 23, title = "A Long Walk")
@Component
public class Year2023Day23 {

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

  /** Calculate the answer to the puzzle. Common between both parts. */
  private long calculate(final PuzzleContext pc) {
    final Input input = getInput(pc);
    return calculate(input, input.start, input.start, 0);
  }

  /** Recursively calculate the longest path. */
  private long calculate(final Input input, final long path, final long current, final long cost) {
    long answer = 0;
    for (final Edge edge : input.edges.get(Long.valueOf(current))) {
      if (((path & edge.end) == 0) && (input.ignoreRestrictions || !edge.restricted)) {
        final long nextCost = cost + edge.cost;
        if (input.end == edge.end) {
          return nextCost;
        }
        answer = Math.max(answer, calculate(input, (path | edge.end), edge.end, nextCost));
      }
    }
    return answer;
  }

  /** Get the puzzle input. */
  private Input getInput(final PuzzleContext pc) {
    final int[][] grid = il.linesAsCodePoints(pc);

    // Identify the start and end points.
    int startX = Integer.MIN_VALUE;
    int endX = Integer.MIN_VALUE;
    for (int x = 0; x < grid[0].length; ++x) {
      if (grid[0][x] == '.') {
        startX = x;
      }
      if (grid[grid.length - 1][x] == '.') {
        endX = x;
      }
    }
    final Point2D start = new Point2D(startX, 0);
    final Point2D end = new Point2D(endX, grid.length - 1);

    // Find all intersections
    final Collection<Point2D> intersections = new HashSet<>();
    intersections.add(start);
    intersections.add(end);

    for (int y = 1; y < grid.length - 1; ++y) {
      for (int x = 1; x < grid[y].length - 1; ++x) {
        final Point2D pt = new Point2D(x, y);
        if ((pt.get(grid) != '#') && isIntersection(grid, pt)) {
          intersections.add(pt);
        }
      }
    }

    // Find all edges.
    final Map<Point2D, Collection<PointEdge>> pointEdges = new HashMap<>();
    for (final Point2D pt : intersections) {
      pointEdges.put(pt, getEdges(grid, pt, end, intersections));
    }

    // Convert points to long bit masks.
    final Map<Point2D, Long> pointToLong = new HashMap<>();
    final Set<Long> intersectionsLong = new HashSet<>();
    long value = 1;
    for (final Point2D pt : intersections) {
      final Long key = Long.valueOf(value);
      pointToLong.put(pt, key);
      intersectionsLong.add(key);
      value <<= 1;
    }
    final Map<Long, Collection<Edge>> edges = new HashMap<>();
    for (final var entry : pointEdges.entrySet()) {
      final Collection<Edge> values = new HashSet<>();
      edges.put(pointToLong.get(entry.getKey()), values);
      for (final PointEdge pe : entry.getValue()) {
        values.add(new Edge(pointToLong.get(pe.start).longValue(), pointToLong.get(pe.end).longValue(), pe.cost, pe.restricted));
      }
    }

    return new Input(pointToLong.get(start).longValue(), pointToLong.get(end).longValue(), intersectionsLong, edges,
      pc.getBoolean("IgnoreRestrictions"));
  }

  /** Get whether the point is an intersection of multiple paths. */
  private boolean isIntersection(final int[][] grid, final Point2D pt) {
    int openings = 0;
    for (final Point2D neighbor : pt.getCardinalNeighbors()) {
      if (neighbor.isIn(grid) && (neighbor.get(grid) != '#')) {
        ++openings;
      }
    }
    return openings > 2;
  }

  /** Given a point, find the edges that lead to other points. */
  private Collection<PointEdge> getEdges(final int[][] grid, final Point2D start, final Point2D end, final Collection<Point2D> intersections) {
    final Collection<PointEdge> edges = new HashSet<>();
    boolean endFound = false;
    for (final Direction d : Direction.values()) {
      final Point2D step = d.apply(start);
      if (step.isIn(grid)) {
        final int ch = step.get(grid);
        if (ch != '#') {
          // This is the start of a valid edge.
          final PointEdge edge = getEdge(grid, start, intersections, d.isRestricted(ch), List.of(start, step));
          endFound |= edge.end.equals(end);
          edges.add(edge);
        }
      }
    }
    // If one of the edges is the end of the maze, remove every other edge because they are essentially dead weight.
    if (endFound) {
      for (final var iter = edges.iterator(); iter.hasNext();) {
        if (!iter.next().end.equals(end)) {
          iter.remove();
        }
      }
    }
    return edges;
  }

  /** Get the edge found by following the given path. */
  private PointEdge getEdge(final int[][] grid, final Point2D start, final Collection<Point2D> intersections, final boolean restricted, final List<Point2D> path) {
    final Point2D current = path.getLast();
    for (final Direction d : Direction.values()) {
      final Point2D next = d.apply(current);
      // This is a valid direction to travel.
      if (next.isIn(grid) && (next.get(grid) != '#') && !path.contains(next)) {
        if (intersections.contains(next)) {
          return new PointEdge(start, next, path.size(), restricted);
        }
        final List<Point2D> nextPath = new ArrayList<>(path.size() + 1);
        nextPath.addAll(path);
        nextPath.add(next);
        boolean nextRestricted = (restricted || d.isRestricted(next.get(grid)));
        return getEdge(grid, start, intersections, nextRestricted, nextPath);
      }
    }
    return null;
  }

  /** One of the cardinal directions that we can move in the maze. */
  private static enum Direction {

    NORTH(0, -1, '^'),
    EAST(1, 0, '>'),
    SOUTH(0, 1, 'v'),
    WEST(-1, 0, '<');

    public final int dx;

    public final int dy;

    public final int slope;

    private Direction(final int _dx, final int _dy, final int _slope) {
      dx = _dx;
      dy = _dy;
      slope = _slope;
    }

    /** Get the point that is one step in this direction. */
    public Point2D apply(final Point2D p) {
      return p.add(dx, dy);
    }

    /**
     * Get whether the given map character is valid for this direction. It must either be open or have this direction's
     * slope.
     */
    public boolean isRestricted(final int ch) {
      // Both not clear, and not my slope character.
      return (ch != '.') && (ch != slope);
    }
  }

  /** Input contains the start and end locations as well as all graph nodes and edges.. */
  private record Input(long start, long end, Collection<Long> nodes, Map<Long, Collection<Edge>> edges,
    boolean ignoreRestrictions) {}

  /** An edge is a path from one node to another. It also tracks whether directional travel is restricted. */
  private record PointEdge(Point2D start, Point2D end, int cost, boolean restricted) {}

  private record Edge(long start, long end, int cost, boolean restricted) {}
}
