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

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2024, day = 16, title = "Reindeer Maze")
@Component
public class Year2024Day16 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Maze maze = getInput(pc);
    final Queue<State> queue = new PriorityQueue<>();
    queue.add(new State(Set.of(maze.start), maze.start, Facing.EAST, 0));
    final Map<CacheKey, Long> bestScores = new HashMap<>();
    while (!queue.isEmpty()) {
      final State state = queue.poll();
      if (state.location.equals(maze.end)) {
        // Queue is sorted: this has to be the best answer.
        return state.score;
      }
      else {
        queue.addAll(getNextStates(state, maze, bestScores));
      }
    }
    return -1;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Maze maze = getInput(pc);
    final Queue<State> queue = new PriorityQueue<>();
    queue.add(new State(Set.of(maze.start), maze.start, Facing.EAST, 0));
    final Map<CacheKey, Long> bestScores = new HashMap<>();
    OptionalLong lowestScore = OptionalLong.empty();
    final Set<Point2D> bestPaths = new HashSet<>();
    while (!queue.isEmpty()) {
      final State state = queue.poll();
      // Current best state cannot at least match the best end-state score: quit.
      if (lowestScore.isPresent() && (lowestScore.getAsLong() < state.score)) {
        break;
      }
      if (state.location.equals(maze.end)) {
        // First time finding the end: this is the best score.
        if (lowestScore.isEmpty()) {
          lowestScore = OptionalLong.of(state.score);
        }
        // If the current score matches the best score, add the path to the best path locations.
        if (state.score == lowestScore.getAsLong()) {
          bestPaths.addAll(state.visited);
        }
      }
      else {
        queue.addAll(getNextStates(state, maze, bestScores));
      }
    }
    return bestPaths.size();
  }

  private Collection<State> getNextStates(final State state, final Maze maze, final Map<CacheKey, Long> bestScores) {
    final Collection<State> nextStates = new ArrayList<>(3);
    for (final Facing nextFacing : Facing.values()) {
      final Point2D nextLocation = nextFacing.apply(state.location);
      // Candidate location must exist in the maze and not already be visited.
      if (maze.locations.contains(nextLocation) && !state.visited.contains(nextLocation)) {
        // Keep moving in the same direction.
        if (nextFacing == state.facing) {
          final Set<Point2D> nextVisited = new HashSet<>(state.visited);
          nextVisited.add(nextLocation);
          nextStates.add(new State(nextVisited, nextLocation, nextFacing, state.score + 1));
        }
        // Turn, but do not move.
        else {
          nextStates.add(new State(state.visited, state.location, nextFacing, state.score + 1_000));
        }
      }
    }
    // Remove any of these next states that can never match the lowest score.
    for (var iter = nextStates.iterator(); iter.hasNext();) {
      final State nextState = iter.next();
      final CacheKey key = nextState.getCacheKey();
      if (bestScores.containsKey(key) && bestScores.get(key)
                                                   .longValue() < nextState.score) {
        iter.remove();
      }
      else {
        bestScores.put(key, Long.valueOf(nextState.score));
      }
    }
    return nextStates;
  }

  /** Get the program input, including any optimizations for pruning dead space. */
  private Maze getInput(final PuzzleContext pc) {
    final List<String> lines = il.lines(pc);
    Point2D start = null;
    Point2D end = null;
    final Set<Point2D> points = new HashSet<>(1_024);
    for (int y = 0; y < lines.size(); ++y) {
      final String line = lines.get(y);
      for (int x = 0; x < line.length(); ++x) {
        final int ch = line.codePointAt(x);
        if (ch != '#') {
          points.add(new Point2D(x, y));
        }
        if (ch == 'S') {
          start = new Point2D(x, y);
        }
        else if (ch == 'E') {
          end = new Point2D(x, y);
        }
      }
    }
    // Prune dead ends. This saves far more time than it costs.
    boolean updated = true;
    while (updated) {
      updated = false;
      final Set<Point2D> remove = new HashSet<>();
      for (final Point2D point : points) {
        // Never process the start and end points.
        if (!point.equals(start) && !point.equals(end)) {
          // If this location has less than two open neighbors, mark it for removal since it is impossible to travel
          // through it.
          int neighbors = 0;
          for (final Facing f : Facing.values()) {
            if (points.contains(f.apply(point))) {
              ++neighbors;
            }
          }
          if (neighbors < 2) {
            remove.add(point);
          }
        }
      }
      // Remove all locations marked for removal. This cannot be done inside the above loop because it would throw a
      // ConcurrentModificationException.
      if (!remove.isEmpty()) {
        points.removeAll(remove);
        updated = true;
      }
    }
    return new Maze(points, start, end);
  }

  /** Represents one direction an actor can face while traversing the maze. */
  private enum Facing {

    NORTH(new Point2D(0, -1)),
    SOUTH(new Point2D(0, 1)),
    EAST(new Point2D(1, 0)),
    WEST(new Point2D(-1, 0));

    final Point2D delta;

    private Facing(final Point2D _delta) {
      delta = _delta;
    }

    public Point2D apply(final Point2D p) {
      return p.add(delta);
    }
  }

  /** Compound key used in a map that tracks best scores at each location in the maze. */
  private record CacheKey(Point2D location, Facing facing) {}

  /** Represents one state of maze traversal. */
  private record State(Set<Point2D> visited, Point2D location, Facing facing, long score)
  implements Comparable<State> {

    public CacheKey getCacheKey() {
      return new CacheKey(location, facing);
    }

    @Override
    public int compareTo(final State o) {
      return Long.compare(score, o.score);
    }

  }

  /** Program input containing the traversable locations in the maze and the start and end locations. */
  private record Maze(Set<Point2D> locations, Point2D start, Point2D end) {}
}
