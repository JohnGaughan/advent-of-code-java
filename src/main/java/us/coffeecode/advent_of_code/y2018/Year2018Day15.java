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
package us.coffeecode.advent_of_code.y2018;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2018, day = 15, title = "Beverage Bandits")
@Component
public final class Year2018Day15 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc, 0);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    long result = -1;
    int elfBonusAp = 12;
    while (result < 0) {
      ++elfBonusAp;
      result = calculate(pc, elfBonusAp);
    }
    return result;
  }

  private long calculate(final PuzzleContext pc, final int elfBonusAp) {
    final var state = new State(il.lines(pc));
    if (elfBonusAp > 0) {
      state.addBonusAp(Force.ELVES, elfBonusAp);
    }
    int rounds = 0;
    while (true) {
      final Set<Actor> turnTaken = new HashSet<>();
      AttackResult lastAttack = null;
      for (int y = 0; y < state.Y; ++y) {
        for (int x = 0; x < state.X; ++x) {
          final Point2D location = new Point2D(x, y);
          final Actor actor = state.actorAt(location);
          if (actor != null && !turnTaken.contains(actor)) {
            lastAttack = takeTurn(state, location);
            turnTaken.add(actor);
            // For part two, check if an elf was killed. If so, abort this simulation entirely.
            if ((elfBonusAp > 0) && (lastAttack == AttackResult.KILLED) && (actor.force != Force.ELVES)) {
              return -1;
            }
          }
        }
      }
      // Only count a round as complete if every actor took a turn, or the last action resulted in a kill that completed
      // the game: that is, wiped out one force.
      if (state.isComplete()) {
        if (lastAttack == AttackResult.KILLED) {
          ++rounds;
        }
        break;
      }
      ++rounds;
    }
    int hp = 0;
    for (final Actor[] row : state.actors) {
      for (final Actor e : row) {
        if (e != null) {
          hp += e.hp;
        }
      }
    }
    final long result = rounds * hp;
    return result;
  }

  private AttackResult takeTurn(final State state, final Point2D location) {
    // If the current unit starts next to an enemy, attack.
    final AttackResult result = attack(state, location);
    if (result != AttackResult.NONE) {
      return result;
    }

    // Otherwise move, then attack.
    final Point2D newLocation = move(state, location);
    return attack(state, newLocation);
  }

  private AttackResult attack(final State state, final Point2D location) {
    final Actor me = state.actorAt(location);
    final Point2D enemyLocation = state.getAdjacentEnemy(location, me.force);
    if (enemyLocation != null) {
      final Actor enemy = state.actorAt(enemyLocation);
      enemy.hp -= me.ap;
      if (enemy.hp <= 0) {
        state.removeActor(enemyLocation);
        return AttackResult.KILLED;
      }
      return AttackResult.ATTACKED;
    }
    return AttackResult.NONE;
  }

  private Point2D move(final State state, final Point2D location) {
    final Actor me = state.actorAt(location);

    // We already determined that the actor needs to move: are there any enemies with adjacent openings? If not, do not
    // bother path finding because there cannot be a target point to which to move.
    if (!state.isAnyEnemyActorTriviallyReachable(me.force)) {
      return location;
    }

    // 1. Create paths branching out from the current location until we find an enemy, or
    // there are no more paths to try. This produces zero or more shortest paths.
    Set<Path> paths = enumerateAllShortestPaths(state, location);

    // 2. Remove paths that do not end up adjacent to an enemy.
    paths = getPathsEndingAtAnEnemy(state, paths, me.force);

    // 3. Remove paths whose target is not first in reading order.
    paths = getPathsEndingFirstInReadingOrder(state, paths);

    // 4. Of the remaining paths' first steps, pick the step first in the reading order.
    final Point2D step = getFirstStepFirstInReadingOrder(state, paths);

    // 5. Move!
    if (step != null) {
      state.moveActor(location, step);
      return step;
    }
    return location;
  }

  private Set<Path> enumerateAllShortestPaths(final State state, final Point2D location) {
    final Actor me = state.actorAt(location);

    Set<Path> paths = new HashSet<>();
    // Populate the base case, one step from current location.
    for (final Point2D neighbor : getSortedNeighbors(location)) {
      if (state.isOpen(neighbor)) {
        paths.add(new Path(neighbor));
      }
    }

    // Iterate over each path and add on to them until we find an enemy or there are no enemies to find.
    // Always finish the final iteration to find ties.
    while (!paths.isEmpty()) {
      for (final Path path : paths) {
        final Point2D enemy = state.getAdjacentEnemy(path.end(), me.force);
        if (enemy != null) {
          return paths;
        }
      }

      final Set<Path> newPaths = new HashSet<>();
      for (final Path path : paths) {
        final Point2D end = path.end();
        for (final Point2D neighbor : getSortedNeighbors(end)) {
          // Neighbor must not be occupied by a wall or actor: we cannot have visited it already.
          if (state.isOpen(neighbor) && !path.visited.contains(neighbor)) {
            newPaths.add(new Path(path, neighbor));
          }
        }
      }
      paths = reduce(newPaths);
    }
    return paths;
  }

  private Set<Path> getPathsEndingAtAnEnemy(final State state, final Set<Path> paths, final Force myForce) {
    final Set<Path> result = new HashSet<>();
    for (final Path path : paths) {
      final Point2D end = path.end();
      for (final Point2D neighbor : getSortedNeighbors(end)) {
        final Actor actor = state.actorAt(neighbor);
        if ((actor != null) && (actor.force != myForce)) {
          result.add(path);
          break;
        }
      }
    }
    return result;
  }

  private Set<Path> getPathsEndingFirstInReadingOrder(final State state, final Set<Path> paths) {
    final NavigableMap<Point2D, Set<Path>> result = new TreeMap<>();
    for (final Path path : paths) {
      final Point2D end = path.end();
      if (!result.containsKey(end)) {
        result.put(end, new HashSet<>());
      }
      result.get(end).add(path);
    }
    final var firstEntry = result.firstEntry();
    return (firstEntry == null) ? Collections.emptySet() : firstEntry.getValue();
  }

  private Point2D getFirstStepFirstInReadingOrder(final State state, final Set<Path> paths) {
    Point2D first = null;
    for (final Path path : paths) {
      final Point2D start = path.start();
      if ((first == null) || ((start != null) && (start.compareTo(first) < 0))) {
        first = start;
      }
    }
    return first;
  }

  /**
   * Reduce redundant paths. If two paths end at the same coordinate, keep only the one with the lower first step per
   * reading order.
   */
  private Set<Path> reduce(final Set<Path> paths) {
    // Group paths based on their end points.
    final Map<Point2D, Set<Path>> endPointsToPaths = new HashMap<>();
    for (final Path path : paths) {
      final Point2D p = path.end();
      if (!endPointsToPaths.containsKey(p)) {
        endPointsToPaths.put(p, new HashSet<>());
      }
      endPointsToPaths.get(p).add(path);
    }
    // For each point, get the path that is first in reading order. Use this one. At the same time, update that path's
    // visited points to include all other paths that are equivalent to it.
    final Set<Path> result = new HashSet<>();
    for (final Point2D endPoint : endPointsToPaths.keySet()) {
      Path path = null;
      final Set<Point2D> visited = new HashSet<>();
      for (final Path candidate : endPointsToPaths.get(endPoint)) {
        if ((path == null) || (candidate.start().compareTo(path.start()) < 0)) {
          path = candidate;
        }
        visited.addAll(candidate.visited);
      }
      if (path != null) {
        path.addVisited(visited);
      }
      result.add(path);
    }
    return result;
  }

  private static List<Point2D> getSortedNeighbors(final Point2D point) {
    final List<Point2D> neighbors = point.getCardinalNeighbors();
    Collections.sort(neighbors);
    return neighbors;
  }

  private static enum AttackResult {
    ATTACKED,
    KILLED,
    NONE;
  }

  private static final class Path
  implements Comparable<Path> {

    final Point2D[] elements;

    final Set<Point2D> visited = new HashSet<>();

    Path(final Point2D first) {
      elements = new Point2D[] { first };
      visited.add(first);
    }

    Path(final Path first, final Point2D next) {
      elements = new Point2D[first.elements.length + 1];
      System.arraycopy(first.elements, 0, elements, 0, first.elements.length);
      elements[first.elements.length] = next;
      visited.addAll(first.visited);
      visited.add(next);
    }

    void addVisited(final Collection<? extends Point2D> points) {
      visited.addAll(points);
    }

    Point2D start() {
      return elements[0];
    }

    Point2D end() {
      return elements[elements.length - 1];
    }

    @Override
    public int compareTo(final Path o) {
      // Elements array always has at least one element. First compare last element - we select in reading order.
      final int compare = elements[elements.length - 1].compareTo(o.elements[o.elements.length - 1]);
      if (compare != 0) {
        return compare;
      }
      return elements[0].compareTo(o.elements[0]);
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      else if (obj instanceof Path o) {
        return Arrays.equals(elements, o.elements);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return Arrays.hashCode(elements);
    }

    @Override
    public String toString() {
      return Arrays.toString(elements);
    }

  }

  private static enum Force {
    ELVES,
    GOBLINS;
  }

  private static final class Actor {

    int ap = 3;

    int hp = 200;

    final Force force;

    Actor(final Force _force) {
      force = _force;
    }

    @Override
    public boolean equals(final Object obj) {
      return super.equals(obj);
    }

    @Override
    public int hashCode() {
      return super.hashCode();
    }

    @Override
    public String toString() {
      return force.name().substring(0, 1);
    }

  }

  private static final class State {

    final int X;

    final int Y;

    final boolean[][] walls;

    final Actor[][] actors;

    State(final List<String> input) {
      Y = input.size();
      X = input.getFirst().length();
      walls = new boolean[Y][X];
      actors = new Actor[Y][X];
      for (int y = 0; y < Y; ++y) {
        final String line = input.get(y);
        for (int x = 0; x < X; ++x) {
          final int ch = line.codePointAt(x);
          if (ch == '#') {
            walls[y][x] = true;
          }
          else if (ch == 'E') {
            actors[y][x] = new Actor(Force.ELVES);
          }
          else if (ch == 'G') {
            actors[y][x] = new Actor(Force.GOBLINS);
          }
        }
      }
    }

    void addBonusAp(final Force force, final int apBonus) {
      for (int y = 0; y < Y; ++y) {
        for (int x = 0; x < Y; ++x) {
          if (actors[y][x] != null && actors[y][x].force == force) {
            actors[y][x].ap += apBonus;
          }
        }
      }
    }

    /** Get the adjacent enemy with the fewest HP: in a tie, select first in reading order. */
    Point2D getAdjacentEnemy(final Point2D location, final Force me) {
      Point2D currentBest = null;
      for (final Point2D candidateLocation : getSortedNeighbors(location)) {
        final Actor candidate = candidateLocation.get(actors);
        // Found an enemy
        if (candidate != null && candidate.force != me) {
          // First enemy
          if (currentBest == null) {
            currentBest = candidateLocation;
          }
          // Already found one: see if this is a "better" enemy
          else {
            int currentBestHp = currentBest.get(actors).hp;
            int candidateHp = candidateLocation.get(actors).hp;
            if (candidateHp < currentBestHp) {
              currentBest = candidateLocation;
            }
          }
        }
      }
      return currentBest;
    }

    boolean isComplete() {
      boolean hasGoblin = false;
      boolean hasElf = false;
      for (final Actor[] row : actors) {
        for (final Actor actor : row) {
          if (actor != null) {
            if (actor.force == Force.ELVES) {
              hasElf = true;
            }
            else if (actor.force == Force.GOBLINS) {
              hasGoblin = true;
            }
            if (hasGoblin && hasElf) {
              return false;
            }
          }
        }
      }
      return true;
    }

    /**
     * Determine if a force other than the provided one is trivially reachable. If true, another force has an empty
     * square next to it. This does not do any advanced path finding. This only checks that there is an open adjacent
     * space to attack a member of another force.
     */
    boolean isAnyEnemyActorTriviallyReachable(final Force force) {
      for (int y = 0; y < Y; ++y) {
        for (int x = 0; x < Y; ++x) {
          if (actors[y][x] != null && actors[y][x].force != force) {
            // Found an enemy actor: see if there are any openings.
            for (final Point2D p : getSortedNeighbors(new Point2D(x, y))) {
              if (!p.get(walls) && p.get(actors) == null) {
                return true;
              }
            }
          }
        }
      }
      return false;
    }

    void moveActor(final Point2D from, final Point2D to) {
      if (to.get(actors) != null) {
        throw new IllegalArgumentException("Location " + to + " is already occupied");
      }
      to.set(actors, from.get(actors));
      from.set(actors, null);
    }

    Actor actorAt(final Point2D location) {
      return location.get(actors);
    }

    void removeActor(final Point2D location) {
      location.set(actors, null);
    }

    boolean isOpen(final Point2D location) {
      return !location.get(walls) && location.get(actors) == null;
    }

    @Override
    public String toString() {
      final StringBuilder str = new StringBuilder(walls.length * (walls[0].length + 2));
      for (int y = 0; y < Y; ++y) {
        for (int x = 0; x < X; ++x) {
          if (walls[y][x]) {
            str.append('#');
          }
          else if (actors[y][x] == null) {
            str.append('.');
          }
          else {
            str.append(actors[y][x]);
          }
        }
        str.append('\n');
      }
      return str.toString();
    }

  }

}
