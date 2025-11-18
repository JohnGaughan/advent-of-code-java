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
package us.coffeecode.advent_of_code.y2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2021, day = 23)
@Component
public final class Year2021Day23 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(getInput(pc));
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(getInput(pc));
  }

  private long calculate(final Input input) {
    final Queue<State> queue = new PriorityQueue<>(1 << 8);
    queue.add(input.initial);
    final Map<String, Long> cache = new HashMap<>(1 << 13);
    while (!queue.isEmpty()) {
      final State state = queue.poll();
      final String key = state.id();

      if (isSolved(state)) {
        // Only check for solution here. The priority queue guarantees the _best_ solution is at the front.
        return state.energy;
      }

      // See if this is a duplicate state. If so, skip it unless it has lower energy than last time.
      if (cache.containsKey(key)) {
        long energy = cache.get(key)
                           .longValue();
        if (state.energy >= energy) {
          continue;
        }
        else if (state.energy < energy) {
          cache.put(key, Long.valueOf(state.energy));
        }
      }
      else {
        cache.put(key, Long.valueOf(state.energy));
      }

      for (final Move move : getMoves(state, input.paths)) {
        final State next = state.apply(move);
        queue.add(next);
      }
    }
    return 0;
  }

  private boolean isSolved(final State state) {
    for (final var entry : state.locations.entrySet()) {
      final Amphipod a = entry.getValue();
      if (a != null) {
        final Point2D p = entry.getKey();
        if (p.getX() != a.targetX) {
          return false;
        }
      }
    }
    return true;
  }

  /** Get all valid moves for the current state. */
  private Iterable<Move> getMoves(final State state, final Map<Start, ? extends Iterable<Path>> paths) {
    // Bulk of time spent in this method or sub-method.
    final Collection<Move> movesToHallway = new ArrayList<>();
    for (final var entry : state.locations.entrySet()) {
      final Amphipod a = entry.getValue();
      if (a != null) {
        final Point2D from = entry.getKey();
        final Start start = new Start(a, from);
        if (canMoveFrom(state, start)) {
          for (final Path path : paths.get(start)) {
            if (isPathClear(state, path) && canMoveTo(state, start, path)) {
              final long energy = a.energy * path.path.size();
              if (path.to.getY() > 1) {
                // If any move is a move home, use it. This move must be taken at some point, so do it ASAP to avoid
                // unnecessary branches.
                return List.of(new Move(a, from, path.to, energy));
              }
              else {
                movesToHallway.add(new Move(a, from, path.to, energy));
              }
            }
          }
        }
      }
    }
    return movesToHallway;
  }

  /** Is it valid to move from the start of this path? */
  private boolean canMoveFrom(final State state, final Start start) {
    // In the correct room. Only move out if this room contains an incorrect Amphipod.
    if (start.who.targetX == start.from.getX()) {
      for (int y = 2; y < state.roomBottom; ++y) {
        final Amphipod a = state.locations.get(new Point2D(start.who.targetX, y));
        if ((a != null) && (a != start.who)) {
          return true;
        }
      }
      return false;
    }

    // Not in the correct room. Always valid to leave.
    return true;
  }

  /** Get whether this path is clear. If not, it cannot be valid. */
  private boolean isPathClear(final State state, final Path path) {
    for (final Point2D p : path.path) {
      final Amphipod a = state.locations.get(p);
      if (a != null) {
        return false;
      }
    }
    return true;
  }

  /** Is it valid to move to the end of this path? */
  private boolean canMoveTo(final State state, final Start start, final Path path) {
    // If the destination is in the hallway, then it is valid.
    if (path.to.getY() == 1) {
      return true;
    }

    // Destination is in the target room, and the target cannot be obstructed. This must be the bottom of the room or
    // only Amphipods that belong here are below the destination.
    for (int y = path.to.getY() + 1; y < state.roomBottom; ++y) {
      final Point2D p = new Point2D(path.to.getX(), y);
      final Amphipod at = state.locations.get(p);
      if ((at == null) || (at.targetX != start.who.targetX)) {
        return false;
      }
    }

    return true;
  }

  private Input getInput(final PuzzleContext pc) {
    // Populate all starting locations.
    int[][] input = il.linesAsCodePoints(pc);
    if (pc.getBoolean("BonusAmphipods")) {
      final int[][] inputP2 = new int[input.length + 2][];
      inputP2[0] = input[0];
      inputP2[1] = input[1];
      inputP2[2] = input[2];
      inputP2[3] = PART_2_INSERT[0];
      inputP2[4] = PART_2_INSERT[1];
      inputP2[5] = input[3];
      inputP2[6] = input[4];
      input = inputP2;
    }
    final Map<Point2D, Amphipod> locations = new HashMap<>(32);
    for (int y = 0; y < input.length; ++y) {
      for (int x = 0; x < input[y].length; ++x) {
        if (Character.isAlphabetic(input[y][x])) {
          final Amphipod a = Amphipod.valueOf(input[y][x]);
          locations.put(new Point2D(x, y), a);
        }
        else if (input[y][x] == '.') {
          locations.put(new Point2D(x, y), null);
        }
      }
    }

    // One past the last valid location in a room.
    final int roomBottom = input.length - 1;

    // Enumerate all valid paths.
    final Map<Start, List<Path>> paths = new HashMap<>(128);

    // Enumerate from each hallway location into each room.
    for (final Amphipod a : Amphipod.values()) {
      for (final int x0 : HALLWAY_STOPS) {
        final Start key = new Start(a, new Point2D(x0, 1));
        paths.putIfAbsent(key, new ArrayList<>());
        final List<Point2D> path = new ArrayList<>();
        // Move left to above the room.
        if (a.targetX < x0) {
          for (int x = x0 - 1; x >= a.targetX; --x) {
            path.add(new Point2D(x, 1));
          }
        }
        // Move right to above the room
        else {
          for (int x = x0 + 1; x <= a.targetX; ++x) {
            path.add(new Point2D(x, 1));
          }
        }

        // We are above the room. Now add each location inside the room.
        for (int y1 = 2; y1 < roomBottom; ++y1) {
          final Point2D last = new Point2D(a.targetX, y1);
          path.add(last);
          paths.get(key)
               .add(new Path(last, new ArrayList<>(path)));
        }
      }
    }

    // Enumerate from each starting location.
    for (final Amphipod a : Amphipod.values()) {
      for (final int x0 : ROOM_X) {
        for (int y0 = 2; y0 < roomBottom; ++y0) {
          final Start key = new Start(a, new Point2D(x0, y0));
          paths.putIfAbsent(key, new ArrayList<>());

          // Go up to the hallway.
          final List<Point2D> path = new ArrayList<>();
          for (int y = y0 - 1; y >= 1; --y) {
            path.add(new Point2D(x0, y));
          }

          // Go left.
          final List<Point2D> pathLeft = new ArrayList<>(path);
          for (int x1 = x0 - 1; x1 > 0; --x1) {
            final Point2D last = new Point2D(x1, 1);
            pathLeft.add(last);
            if (Arrays.binarySearch(HALLWAY_STOPS, x1) >= 0) {
              paths.get(key)
                   .add(new Path(last, new ArrayList<>(pathLeft)));
            }
          }

          // Go right.
          final List<Point2D> pathRight = new ArrayList<>(path);
          for (int x1 = x0 + 1; x1 < 12; ++x1) {
            final Point2D last = new Point2D(x1, 1);
            pathRight.add(last);
            if (Arrays.binarySearch(HALLWAY_STOPS, x1) >= 0) {
              paths.get(key)
                   .add(new Path(last, new ArrayList<>(pathRight)));
            }
          }

          // Go to above the Amphipod's target room.
          if (a.targetX != x0) {
            // Move either left or right.
            if (a.targetX < x0) {
              for (int x1 = x0 - 1; x1 >= a.targetX; --x1) {
                path.add(new Point2D(x1, 1));
              }
            }
            else {
              for (int x1 = x0 + 1; x1 <= a.targetX; ++x1) {
                path.add(new Point2D(x1, 1));
              }
            }

            // Descend into the target room.
            for (int y1 = 2; y1 < roomBottom; ++y1) {
              final Point2D last = new Point2D(a.targetX, y1);
              path.add(last);
              paths.get(key)
                   .add(new Path(last, new ArrayList<>(path)));
            }
          }
        }
      }
    }

    return new Input(new State(locations, roomBottom, 0), paths);
  }

  /** Locations in the hallway that can be start and stop locations for movement. */
  private static final int[] HALLWAY_STOPS = new int[] { 1, 2, 4, 6, 8, 10, 11 };

  /** X coordinates for all rooms. */
  private static final int[] ROOM_X = new int[] { 3, 5, 7, 9 };

  private static enum Amphipod {

    A(1, 3),
    B(10, 5),
    C(100, 7),
    D(1_000, 9);

    static Amphipod valueOf(final int codePoint) {
      return Arrays.stream(values())
                   .filter(a -> a.name()
                                 .codePointAt(0) == codePoint)
                   .findFirst()
                   .get();
    }

    public final int energy;

    public final int targetX;

    Amphipod(final int e, final int x) {
      energy = e;
      targetX = x;
    }
  }

  private record Move(Amphipod who, Point2D from, Point2D to, long energy) {}

  private record Input(State initial, Map<Start, List<Path>> paths) {}

  private record Start(Amphipod who, Point2D from) {}

  private record Path(Point2D to, List<Point2D> path) {}

  private record State(Map<Point2D, Amphipod> locations, int roomBottom, long energy)
  implements Comparable<State> {

    State apply(final Move move) {
      final Map<Point2D, Amphipod> newLocations = new HashMap<>(locations);
      newLocations.put(move.from, null);
      newLocations.put(move.to, move.who);
      return new State(newLocations, roomBottom, energy + move.energy);
    }

    String id() {
      // This is a consistent and deterministic representation of all of the points in this state.
      return locations.toString();
    }

    @Override
    public int compareTo(final State o) {
      return Long.compare(energy, o.energy);
    }

  }

  private static final int[][] PART_2_INSERT = new int[][] { "  #D#C#B#A#".codePoints()
                                                                          .toArray(),
      "  #D#B#A#C#".codePoints()
                   .toArray() };
}
