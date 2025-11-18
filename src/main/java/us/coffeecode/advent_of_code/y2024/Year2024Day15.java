/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2024  John Gaughan
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
package us.coffeecode.advent_of_code.y2024;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2024, day = 15)
@Component
public class Year2024Day15 {

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

  /** Perform the calculation and return the answer using the given move algorithm. */
  private long calculate(final PuzzleContext pc) {
    final Input input = getInput(pc);
    Point2D robot = input.robotStart;
    for (final Direction d : input.directions) {
      robot = handleMove(input.grid, robot, d);
    }
    return score(input.grid);
  }

  /** Attempt to move the robot and anything in its way. */
  private Point2D handleMove(final int[][] grid, final Point2D robotCurrent, final Direction d) {
    final Point2D robotNext = d.apply(robotCurrent);
    final int occupier = robotNext.get(grid);
    if (occupier == '#') {
      return handleWall(grid, robotCurrent, robotNext, d);
    }
    else if (occupier == 'O') {
      return handleBox(grid, robotCurrent, robotNext, d);
    }
    else if ((occupier == '[') || (occupier == ']')) {
      return handleWideBox(grid, robotCurrent, robotNext, d);
    }
    else if (occupier == '.') {
      return handleOpen(grid, robotCurrent, robotNext, d);
    }
    throw new IllegalStateException();
  }

  /** Handle a move into a wall. */
  private Point2D handleWall(final int[][] grid, final Point2D robotCurrent, final Point2D robotNext, final Direction d) {
    return robotCurrent;
  }

  /** Handle a move into an open location. */
  private Point2D handleOpen(final int[][] grid, final Point2D robotCurrent, final Point2D robotNext, final Direction d) {
    return robotNext;
  }

  /** Handle a move into a regular box. */
  private Point2D handleBox(final int[][] grid, final Point2D robotCurrent, final Point2D robotNext, final Direction d) {
    Point2D end = robotNext;
    while (end.get(grid) == 'O') {
      end = d.apply(end);
    }
    // Push everything into the open location
    if (end.get(grid) == '.') {
      end.set(grid, 'O');
      robotNext.set(grid, '.');
      return robotNext;
    }
    return robotCurrent;
  }

  /** Handle a move into a wide box. */
  private Point2D handleWideBox(final int[][] grid, final Point2D robotCurrent, final Point2D robotNext, final Direction d) {
    // Vertical and horizontal movement work very differently: handle them separately.
    if (d.isHorizontal()) {
      return handleWideBoxHorizontal(grid, robotCurrent, robotNext, d);
    }
    else {
      return handleWideBoxVertical(grid, robotCurrent, robotNext, d);
    }
  }

  /** Handle a horizontal move into a wide box. */
  private Point2D handleWideBoxHorizontal(final int[][] grid, final Point2D robotCurrent, final Point2D robotNext, final Direction d) {
    Point2D end = robotNext;
    // Find the end of this run of one or more boxes.
    while ((end.get(grid) == '[') || (end.get(grid) == ']')) {
      end = d.apply(end);
    }
    // If there is an open space after the boxes, push them.
    if (end.get(grid) == '.') {
      int replacement = ((d == Direction.LEFT) ? ']' : '[');
      Point2D p = d.apply(robotNext);
      // We need to update the end location, so the bounds test is to break one location after that.
      final Point2D bounds = d.apply(end);
      do {
        p.set(grid, replacement);
        replacement = ((replacement == '[') ? ']' : '[');
        p = d.apply(p);
      } while (!p.equals(bounds));
      robotNext.set(grid, '.');
      return robotNext;
    }
    // No space to push the boxes: robot stays where it is.
    return robotCurrent;
  }

  /** Handle a vertical move into a wide box. */
  private Point2D handleWideBoxVertical(final int[][] grid, final Point2D robotCurrent, final Point2D robotNext, final Direction d) {
    // Get all of the box moves that would occur from the robot pushing the first box.
    final Set<Point2D> moves = getWideBoxVerticalMoves(grid, robotCurrent, d);
    // If any moves are invalid because it would push into a wall, the robot stays where it is.
    if (moves.stream()
             .anyMatch(m -> (m == null))) {
      return robotCurrent;
    }
    // Otherwise, perform all of the moves in reverse order so they do not overwrite each other.
    final Set<Point2D> alreadyMoved = new HashSet<>();
    final List<Point2D> orderedMoves = new ArrayList<>(moves);
    if (d == Direction.DOWN) {
      Collections.sort(orderedMoves, Collections.reverseOrder());
    }
    else {
      Collections.sort(orderedMoves);
    }
    for (final Point2D move : orderedMoves) {
      // If there is a diamond configuration, don't move a point with two pushers twice.
      if (!alreadyMoved.contains(move)) {
        final Point2D next = d.apply(move);
        next.set(grid, move.get(grid));
        move.set(grid, '.');
        alreadyMoved.add(move);
      }
    }
    return robotNext;
  }

  /** Get all box moves that would occur from a vertical push. */
  private Set<Point2D> getWideBoxVerticalMoves(final int[][] grid, final Point2D current, final Direction d) {
    final Set<Point2D> moves = new HashSet<>();
    final Point2D destination = d.apply(current);
    // Current box part can move up freely.
    if (destination.get(grid) == '.') {
      moves.add(current);
    }
    // Current box part is blocked.
    else if (destination.get(grid) == '#') {
      moves.add(null);
    }
    // Current box part - or robot - will push on another box.
    else {
      moves.add(current);
      moves.addAll(getWideBoxVerticalMoves(grid, destination, d));
      if (destination.get(grid) == '[') {
        moves.addAll(getWideBoxVerticalMoves(grid, Direction.RIGHT.apply(destination), d));
      }
      else {
        moves.addAll(getWideBoxVerticalMoves(grid, Direction.LEFT.apply(destination), d));
      }
    }
    return moves;
  }

  /** Calculate the total score for all boxes in the grid. */
  private long score(final int[][] grid) {
    long score = 0;
    for (int y = 0; y < grid.length; ++y) {
      for (int x = 0; x < grid[y].length; ++x) {
        if ((grid[y][x] == 'O') || (grid[y][x] == '[')) {
          score += (100 * y + x);
        }
      }
    }
    return score;
  }

  /** Get the input. */
  private Input getInput(final PuzzleContext pc) {
    final List<List<String>> groups = il.groups(pc);
    final boolean expand = pc.getBoolean("expand");
    return new Input(getRobotStart(groups.get(0), expand), getGrid(groups.get(0), expand), getDirections(groups.get(1)));
  }

  /** Get the location that the robot starts at. */
  private Point2D getRobotStart(final List<String> lines, final boolean expand) {
    for (int y = 0; y < lines.size(); ++y) {
      final int x = lines.get(y)
                         .indexOf('@');
      if (x > -1) {
        if (expand) {
          return new Point2D(x << 1, y);
        }
        return new Point2D(x, y);
      }
    }
    throw new IllegalArgumentException();
  }

  /** Get the grid containing all entities. */
  private int[][] getGrid(final List<String> lines, final boolean expand) {
    final int[][] grid = new int[lines.size()][];
    for (int y = 0; y < lines.size(); ++y) {
      final String line;
      if (expand) {
        line = expand(lines.get(y));
      }
      else {
        line = lines.get(y)
                    .replace('@', '.');
      }
      grid[y] = line.codePoints()
                    .toArray();
    }
    return grid;
  }

  private String expand(final String line) {
    final StringBuilder str = new StringBuilder(line.length() << 1);
    for (final int c : line.codePoints()
                           .toArray()) {
      if (c == '.') {
        str.append("..");
      }
      else if (c == '#') {
        str.append("##");
      }
      else if (c == '@') {
        str.append("..");
      }
      else if (c == 'O') {
        str.append("[]");
      }
    }
    return str.toString();
  }

  /** Get the directions, in order, from the input. */
  private List<Direction> getDirections(final List<String> lines) {
    final List<Direction> directions = new ArrayList<>(1_024);
    for (final String line : lines) {
      line.codePoints()
          .mapToObj(Direction::valueOf)
          .forEach(d -> directions.add(d));
    }
    return directions;
  }

  /** Represents a direction that the robot moves. */
  private enum Direction {

    UP('^', new Point2D(0, -1)),
    DOWN('v', new Point2D(0, 1)),
    LEFT('<', new Point2D(-1, 0)),
    RIGHT('>', new Point2D(1, 0));

    public static Direction valueOf(final int codePoint) {
      for (final Direction d : values()) {
        if (d.codePoint == codePoint) {
          return d;
        }
      }
      throw new IllegalArgumentException(Character.toString(codePoint));
    }

    final int codePoint;

    final Point2D delta;

    private Direction(final int _codePoint, final Point2D _delta) {
      codePoint = _codePoint;
      delta = _delta;
    }

    public boolean isHorizontal() {
      return (delta.getX() != 0);
    }

    public Point2D apply(final Point2D p) {
      return p.add(delta);
    }
  }

  /** Contains all data in the input. */
  private record Input(Point2D robotStart, int[][] grid, List<Direction> directions) {}
}
