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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;
import us.coffeecode.advent_of_code.util.Point3D;

@AdventOfCodeSolution(year = 2019, day = 20, title = "Donut Maze")
@Component
public final class Year2019Day20 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    // Load the maze and get its metadata needed for searching through it.
    final int[][] maze = il.linesAsCodePoints(pc);
    reduce(maze);
    final PointToStringCache portals = getPortals(maze);
    final StringToPointCache destinations = getPortalDestinations(portals);
    final Point2D entry = getEntry(portals);
    final Point2D exit = getExit(portals);

    // Breadth-first search of the maze.
    long steps = 0;
    final Set<Point2D> visited = new HashSet<>();
    visited.add(entry);
    Collection<Point2D> visiting = List.of(entry);
    while (!visiting.isEmpty()) {
      final Collection<Point2D> nextVisiting = new HashSet<>();
      for (final Point2D point : visiting) {
        // Standing on the maze exit.
        if (Objects.equals(exit, point)) {
          return steps;
        }

        // Standing on a portal that is not the starting point.
        if ((portals.a.containsKey(point) || portals.b.containsKey(point)) && !Objects.equals(entry, point)) {
          final Point2D destination;
          if (portals.a.containsKey(point)) {
            destination = destinations.a.get(portals.a.get(point));
          }
          else {
            destination = destinations.b.get(portals.b.get(point));
          }
          // Already passed through the portal. Don't use it.
          if (visited.contains(destination)) {
            for (final Point2D neighbor : point.getCardinalNeighbors()) {
              if ((neighbor.get(maze) == OPEN) && !visited.contains(neighbor)) {
                visited.add(neighbor);
                nextVisiting.add(neighbor);
              }
            }
          }
          // Have not used the portal: use it.
          else {
            visited.add(destination);
            nextVisiting.add(destination);
          }
          continue;
        }

        // Not standing on a portal, or standing at the start.
        for (final Point2D neighbor : point.getCardinalNeighbors()) {
          final int ch = neighbor.get(maze);
          if ((ch == OPEN) && !visited.contains(neighbor)) {
            visited.add(neighbor);
            nextVisiting.add(neighbor);
          }
        }
      }
      visiting = nextVisiting;
      ++steps;
    }
    return 0;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    // Load the maze and get its metadata needed for searching through it.
    final int[][] maze = il.linesAsCodePoints(pc);
    reduce(maze);
    final PointToStringCache portals = getPortals(maze);
    final StringToPointCache destinations = getPortalDestinations(portals);
    final Point3D entry = new Point3D(getEntry(portals), 0);
    final Point3D exit = new Point3D(getExit(portals), 0);

    // Breadth-first search of the maze.
    long steps = 0;
    final Set<Point3D> visited = new HashSet<>();
    visited.add(entry);
    Collection<Point3D> visiting = List.of(entry);
    while (!visiting.isEmpty()) {
      final Collection<Point3D> nextVisiting = new HashSet<>();
      for (final Point3D point : visiting) {
        // Standing on the maze exit.
        if (Objects.equals(exit, point)) {
          return steps;
        }
        // Get whether we are standing on a portal.
        final Point2D p2d = new Point2D(point);
        final int z = point.getZ();
        final boolean onOuterPortal = portals.a.containsKey(p2d);
        final boolean onInnerPortal = portals.b.containsKey(p2d);
        final boolean onEntryOrExit = "AA".equals(portals.a.get(p2d)) || "ZZ".equals(portals.a.get(p2d));

        final Point3D destination;

        // Inner portals are always valid.
        if (onInnerPortal) {
          final String id = portals.b.get(p2d);
          destination = new Point3D(destinations.b.get(id), point.getZ() + 1);
        }

        // Outer portals are valid when not at level 0 and the portal is not entry or exit.
        else if (onOuterPortal && (z > 0) && !onEntryOrExit) {
          final String id = portals.a.get(p2d);
          destination = new Point3D(destinations.a.get(id), point.getZ() - 1);
        }

        else {
          destination = null;
        }

        // This is a valid move, and is the only move.
        if ((destination != null) && !visited.contains(destination)) {
          visited.add(destination);
          nextVisiting.add(destination);
          continue;
        }

        // Not standing on a portal, or standing at the start.
        for (final Point2D neighbor : new Point2D(point).getCardinalNeighbors()) {
          final int ch = neighbor.get(maze);
          final Point3D neighbor3D = new Point3D(neighbor, point.getZ());
          if ((ch == OPEN) && !visited.contains(neighbor3D)) {
            visited.add(neighbor3D);
            nextVisiting.add(neighbor3D);
          }
        }
      }
      visiting = nextVisiting;
      ++steps;
    }
    return 0;
  }

  private static final int WALL = '#';

  private static final int OPEN = '.';

  private Point2D getEntry(final PointToStringCache portals) {
    return portals.a.entrySet().stream().filter(e -> "AA".equals(e.getValue())).findFirst().get().getKey();
  }

  private Point2D getExit(final PointToStringCache portals) {
    return portals.a.entrySet().stream().filter(e -> "ZZ".equals(e.getValue())).findFirst().get().getKey();
  }

  /** Flip the maps so that IDs point to points in the opposite map. */
  private StringToPointCache getPortalDestinations(final PointToStringCache portals) {
    final StringToPointCache destinations = new StringToPointCache(new HashMap<>(), new HashMap<>());
    for (final var entry : portals.a.entrySet()) {
      for (final var entry2 : portals.b.entrySet()) {
        if (Objects.equals(entry.getValue(), entry2.getValue()) && !Objects.equals(entry.getKey(), entry2.getKey())) {
          destinations.a.put(entry.getValue(), entry2.getKey());
          destinations.b.put(entry.getValue(), entry.getKey());
        }
      }
    }
    return destinations;
  }

  /** Find all of the portals and map their coordinates to IDs. There can be two coordinates per ID. */
  private PointToStringCache getPortals(final int[][] maze) {
    final PointToStringCache portals = new PointToStringCache(new HashMap<>(), new HashMap<>());

    // Outer portals

    final int y_top = 0;
    final int y_bottom = maze.length - 2;
    final int x_left = 0;
    final int x_right = maze[0].length - 2;
    // Top row
    for (int x = 0; x < maze[y_top].length; ++x) {
      if (Character.isAlphabetic(maze[y_top][x])) {
        final Point2D key = new Point2D(x, y_top + 2);
        final String value = codePointsToString(new int[] { maze[y_top][x], maze[y_top + 1][x] });
        portals.a.put(key, value);
      }
    }
    // Left column
    for (int y = 0; y < maze.length; ++y) {
      if (Character.isAlphabetic(maze[y][x_left])) {
        final Point2D key = new Point2D(x_left + 2, y);
        final String value = codePointsToString(new int[] { maze[y][x_left], maze[y][x_left + 1] });
        portals.a.put(key, value);
      }
    }
    // Right column
    for (int y = 0; y < maze.length; ++y) {
      if (Character.isAlphabetic(maze[y][x_right])) {
        final Point2D key = new Point2D(x_right - 1, y);
        final String value = codePointsToString(new int[] { maze[y][x_right], maze[y][x_right + 1] });
        portals.a.put(key, value);
      }
    }
    // Bottom row
    for (int x = 0; x < maze[y_bottom].length; ++x) {
      if (Character.isAlphabetic(maze[y_bottom][x])) {
        final Point2D key = new Point2D(x, y_bottom - 1);
        final String value = codePointsToString(new int[] { maze[y_bottom][x], maze[y_bottom + 1][x] });
        portals.a.put(key, value);
      }
    }

    // Inner portals

    final int y_center = maze.length >> 1;
    final int x_center = maze[0].length >> 1;
    int y_inner_top = y_center;
    int y_inner_bottom = y_center;
    int x_inner_left = x_center;
    int x_inner_right = x_center;
    while ((maze[y_inner_top - 1][x_center] != WALL) && (maze[y_inner_top - 1][x_center] != OPEN)) {
      --y_inner_top;
    }
    while ((maze[y_inner_bottom + 1][x_center] != WALL) && (maze[y_inner_bottom + 1][x_center] != OPEN)) {
      ++y_inner_bottom;
    }
    while ((maze[y_center][x_inner_left - 1] != WALL) && (maze[y_center][x_inner_left - 1] != OPEN)) {
      --x_inner_left;
    }
    while ((maze[y_center][x_inner_right + 1] != WALL) && (maze[y_center][x_inner_right + 1] != OPEN)) {
      ++x_inner_right;
    }

    // Inner top row
    for (int x = x_inner_left; x < x_inner_right; ++x) {
      if (Character.isAlphabetic(maze[y_inner_top][x])) {
        final Point2D key = new Point2D(x, y_inner_top - 1);
        final String value = codePointsToString(new int[] { maze[y_inner_top][x], maze[y_inner_top + 1][x] });
        portals.b.put(key, value);
      }
    }
    // Inner left column
    for (int y = y_inner_top; y < y_inner_bottom; ++y) {
      if (Character.isAlphabetic(maze[y][x_inner_left])) {
        final Point2D key = new Point2D(x_inner_left - 1, y);
        final String value = codePointsToString(new int[] { maze[y][x_inner_left], maze[y][x_inner_left + 1] });
        portals.b.put(key, value);
      }
    }
    // Inner right column
    for (int y = y_inner_top; y < y_inner_bottom; ++y) {
      if (Character.isAlphabetic(maze[y][x_inner_right])) {
        final Point2D key = new Point2D(x_inner_right + 1, y);
        final String value = codePointsToString(new int[] { maze[y][x_inner_right - 1], maze[y][x_inner_right] });
        portals.b.put(key, value);
      }
    }
    // Inner bottom row
    for (int x = x_inner_left; x < x_inner_right; ++x) {
      if (Character.isAlphabetic(maze[y_inner_bottom][x])) {
        final Point2D key = new Point2D(x, y_inner_bottom + 1);
        final String value = codePointsToString(new int[] { maze[y_inner_bottom - 1][x], maze[y_inner_bottom][x] });
        portals.b.put(key, value);
      }
    }

    return portals;
  }

  /** Reduce the maze by removing dead ends which cannot be part of the optimal route. */
  private void reduce(final int[][] maze) {
    boolean modified = true;
    while (modified) {
      modified = false;
      for (int y = 3; y < maze.length - 3; ++y) {
        for (int x = 3; x < maze[y].length - 3; ++x) {
          if (maze[y][x] == OPEN) {
            int walls = 0;
            if (maze[y + 1][x] == WALL) {
              ++walls;
            }
            if (maze[y - 1][x] == WALL) {
              ++walls;
            }
            if (maze[y][x + 1] == WALL) {
              ++walls;
            }
            if (maze[y][x - 1] == WALL) {
              ++walls;
            }
            if (walls > 2) {
              maze[y][x] = WALL;
              modified = true;
            }
          }
        }
      }
    }
  }

  private String codePointsToString(final int[] codePoints) {
    return new String(codePoints, 0, codePoints.length);
  }

  private static record PointToStringCache(Map<Point2D, String> a, Map<Point2D, String> b) {}

  private static record StringToPointCache(Map<String, Point2D> a, Map<String, Point2D> b) {}
}
