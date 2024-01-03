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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2019, day = 25, title = "Cryostasis")
@Component
public final class Year2019Day25 {

  @Autowired
  private IntCodeFactory icf;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final IntCode original =
      icf.make(pc, ExecutionOption.BLOCK_UNTIL_INPUT_AVAILABLE, ExecutionOption.BLOCK_IF_EXCESSIVE_RUNTIME);

    // Create a map.
    final ShipMap map = getMap(icf.make(original));

    // Find all the lethal items.
    for (final String item : map.items.keySet()) {
      testItem(item, map, icf.make(original));
    }

    return solve(map, original);
  }

  /** Solve the puzzle of weight combinations. */
  private long solve(final ShipMap map, final IntCode original) {
    final List<String> items = new ArrayList<>(map.nonlethalItems);
    final List<Set<String>> overweightCombinations = new ArrayList<>();
    for (int k = 1; k < items.size(); ++k) {
      for (final List<String> combination : combinations(items, k)) {
        // If this combination is a superset of another one that is overweight, skip it.
        boolean overweight = false;
        for (final Set<String> overweightCombination : overweightCombinations) {
          if (combination.containsAll(overweightCombination)) {
            overweight = true;
          }
        }
        if (overweight) {
          continue;
        }
        final IntCode ic = icf.make(original);
        fetchItemsAndGoToCheckpoint(map, ic, combination);
        final long weight = checkWeight(map, ic);

        // If this combination is overweight, don't use supersets of it in the future.
        if (weight == 1) {
          overweightCombinations.add(new HashSet<>(combination));
        }
        else if (weight > 1) {
          return weight;
        }
      }
    }

    return 0;
  }

  private List<List<String>> combinations(final List<String> s, final int k) {
    return combinations(s, k, 0, new String[k]);
  }

  private List<List<String>> combinations(final List<String> s, final int k, final int start, final String[] combination) {
    final List<List<String>> results = new ArrayList<>();
    if (k == 0) {
      // Need to copy the array so future changes don't bleed through as with Arrays.asList()
      final List<String> result = new ArrayList<>();
      for (final String combo : combination) {
        result.add(combo);
      }
      results.add(result);
    }
    else {
      for (int i = start; i <= s.size() - k; ++i) {
        combination[combination.length - k] = s.get(i);
        results.addAll(combinations(s, k - 1, i + 1, combination));
      }
    }
    return results;
  }

  /**
   * When standing at the checkpoint, attempt to get through the room that checks weight. Return an integer indicating
   * the result: -1 for weighing under the target, 1 for overweight, and a positive value greater than one for the
   * correct weight.
   */
  private long checkWeight(final ShipMap map, final IntCode ic) {
    final Room checkpoint = map.rooms.get(CHECKPOINT);
    final Direction dir =
      checkpoint.directions.entrySet().stream().filter(e -> SCALE.equals(e.getValue().name)).findFirst().get().getKey();
    ic.getInput().add(dir.getCommand());
    ic.exec();
    final List<String> lines = toStrings(ic.getOutput().removeAll());
    for (final String line : lines) {
      if (line.contains("robotic voice")) {
        if (line.contains("heavier")) {
          return -1;
        }
        else if (line.contains("lighter")) {
          return 1;
        }
        else if (line.contains("Analysis complete")) {
          final Matcher m = DIGITS.matcher(lines.getLast());
          m.find();
          return Long.parseLong(m.group());
        }
      }
    }
    throw new IllegalStateException(lines.toString());
  }

  /** Fetch all items in the set and go to the checkpoint room. */
  private void fetchItemsAndGoToCheckpoint(final ShipMap map, final IntCode ic, final Collection<String> items) {
    Room current = map.rooms.get(map.startRoom);
    for (final String item : items) {
      // Navigate to the room containing the item.
      Room next = map.rooms.get(map.items.get(item));
      navigate(map, ic, current, next);
      // Take the item.
      ic.getInput().add(toInput(TAKE + item + SEPARATOR));
      ic.exec();
      ic.getOutput().clear();
      current = next;
    }
    // Navigate to the security checkout.
    navigate(map, ic, current, map.rooms.get(CHECKPOINT));
  }

  /** Navigate from one room to another, ignoring all program output. */
  private void navigate(final ShipMap map, final IntCode ic, final Room current, final Room target) {
    for (final Direction d : current.navigation.get(target.name)) {
      ic.getInput().add(d.getCommand());
      ic.exec();
      ic.getOutput().clear();
    }
  }

  /** Create a map of the ship. */
  private ShipMap getMap(final IntCode state) {
    final ShipMap map = new ShipMap();
    final Queue<QueueEntry> queue = new LinkedList<>();

    {
      state.exec();
      Room currentRoom = map.update(toStrings(state.getOutput().removeAll()), null, null);
      for (final Direction d : currentRoom.directions.keySet()) {
        queue.add(new QueueEntry(currentRoom, icf.make(state), d));
      }
    }

    while (!queue.isEmpty()) {
      final QueueEntry processing = queue.remove();
      final IntCode ic = processing.state;
      ic.getInput().add(processing.dir.getCommand());
      ic.exec();
      Room nextRoom = map.update(toStrings(ic.getOutput().removeAll()), processing.room, processing.dir);
      if (nextRoom != null) {
        for (final var entry : nextRoom.directions.entrySet()) {
          if (entry.getValue() == null) {
            queue.add(new QueueEntry(nextRoom, icf.make(ic), entry.getKey()));
          }
        }
      }
      if (nextRoom == null) {
        Thread.yield();
      }
    }

    return map;
  }

  private static record QueueEntry(Room room, IntCode state, Direction dir) {}

  private void testItem(final String item, final ShipMap map, final IntCode state) {
    final String targetRoomName = map.items.get(item);
    final Room start = map.rooms.get(map.startRoom);
    state.exec();
    for (final Direction d : start.navigation.get(targetRoomName)) {
      state.getInput().add(d.getCommand());
      state.exec();
      state.getOutput().clear();
    }
    state.getInput().add(toInput(TAKE + item + SEPARATOR));
    state.exec();
    final List<String> output = toStrings(state.getOutput().removeAll());

    // Definitely lethal.
    if (!PROMPT.equals(output.getLast())) {
      map.lethalItems.add(item);
      return;
    }

    // Try moving: the magnet does not allow movement.
    final Direction any = map.rooms.get(targetRoomName).directions.keySet().iterator().next();
    state.getInput().add(any.getCommand());
    state.exec();
    final List<String> output2 = toStrings(state.getOutput().removeAll());
    if ((output2.size() > 1) && output2.get(1).contains(item)) {
      map.lethalItems.add(item);
      return;
    }

    map.nonlethalItems.add(item);
  }

  /** Convert a string to a format IntCode accepts as input. */
  private static long[] toInput(final String str) {
    return str.codePoints().mapToLong(i -> i).toArray();
  }

  /** Given raw output from the program, convert it to a string array for easier parsing. */
  private List<String> toStrings(final long[] array) {
    final String str = Arrays.stream(array).mapToInt(l -> (int) l).collect(StringBuilder::new, StringBuilder::appendCodePoint,
      StringBuilder::append).toString();
    return Arrays.stream(LINE_SPLIT.split(str)).toList();
  }

  private static final String TAKE = "take ";

  private static final String SEPARATOR = "\n";

  private static final String PROMPT = "Command?";

  private static final String CHECKPOINT = "Security Checkpoint";

  private static final String SCALE = "Pressure-Sensitive Floor";

  private static final Pattern LINE_SPLIT = Pattern.compile(SEPARATOR);

  private static final Pattern DIGITS = Pattern.compile("\\d+");

  /** Represents a map of the ship. */
  private static final class ShipMap {

    final Map<String, Room> rooms = new HashMap<>();

    final Map<String, String> items = new HashMap<>();

    final Set<String> nonlethalItems = new HashSet<>();

    final Set<String> lethalItems = new HashSet<>();

    String startRoom;

    /** Update the map for the provided input. */
    Room update(final Iterable<String> lines, final Room previousRoom, final Direction previousDir) {
      String roomName = null;
      final Set<Direction> directions = new HashSet<>();
      final Set<String> roomItems = new HashSet<>();
      boolean inDoors = false;
      boolean inItems = false;
      for (final String line : lines) {
        // Room name
        if (line.startsWith("=")) {
          if (roomName != null) {
            break;
          }
          roomName = line.substring(3, line.length() - 3);
        }
        else if (line.startsWith("Door")) {
          inDoors = true;
        }
        else if (line.startsWith("Item")) {
          inDoors = false;
          inItems = true;
        }
        else if (line.startsWith("-")) {
          if (inDoors) {
            Direction dir = Direction.match(line);
            if (dir == null) {
              inDoors = false;
            }
            else {
              directions.add(dir);
            }
          }
          else if (inItems) {
            roomItems.add(line.substring(2));
          }
        }
      }
      if (roomName == null) {
        return null;
      }
      if (startRoom == null) {
        startRoom = roomName;
      }

      // Found a new room
      if (!rooms.containsKey(roomName)) {
        // Note any items it has at the map level.
        for (final String roomItem : roomItems) {
          items.put(roomItem, roomName);
        }

        // Make the room.
        final Room newRoom = new Room(roomName, directions);
        rooms.put(roomName, newRoom);

        // Wire this up to the previous room.
        if (previousRoom != null) {
          previousRoom.directions.put(previousDir, newRoom);
          newRoom.directions.put(previousDir.opposite(), previousRoom);

          // Process each route the previous room knows about.
          for (final Map.Entry<String, List<Direction>> entry : previousRoom.navigation.entrySet()) {
            // Add a route from the new room to the existing room.
            {
              final List<Direction> fromNewToOld = new ArrayList<>(entry.getValue().size() + 1);
              fromNewToOld.add(previousDir.opposite());
              fromNewToOld.addAll(entry.getValue());
              newRoom.navigation.put(entry.getKey(), fromNewToOld);
            }

            // Add a route from the existing room to the new room.
            {
              final Room existing = rooms.get(entry.getKey());
              final List<Direction> existingRoute = existing.navigation.get(previousRoom.name);
              final List<Direction> fromOldToNew = new ArrayList<>(existingRoute.size() + 1);
              fromOldToNew.addAll(existingRoute);
              fromOldToNew.add(previousDir);
              existing.navigation.put(newRoom.name, fromOldToNew);
            }
          }

          // Add navigation between this room and the previous room.
          newRoom.navigation.put(previousRoom.name, List.of(previousDir.opposite()));
          previousRoom.navigation.put(newRoom.name, List.of(previousDir));
        }
      }

      return rooms.get(roomName);
    }

    @Override
    public String toString() {
      return rooms.values().toString();
    }
  }

  /** One direction the robot may travel. */
  private static enum Direction {

    NORTH("north" + SEPARATOR, "- north"),
    SOUTH("south" + SEPARATOR, "- south"),
    EAST("east" + SEPARATOR, "- east"),
    WEST("west" + SEPARATOR, "- west");

    final long[] command;

    final String match;

    static Direction match(final String m) {
      return Arrays.stream(values()).filter(d -> d.matches(m)).findFirst().get();
    }

    private static final Map<Direction, Direction> OPPOSITES = Map.of(NORTH, SOUTH, SOUTH, NORTH, EAST, WEST, WEST, EAST);

    Direction(final String c, final String m) {
      command = toInput(c);
      match = m;
    }

    long[] getCommand() {
      return command;
    }

    boolean matches(final String line) {
      return match.equals(line);
    }

    Direction opposite() {
      return OPPOSITES.get(this);
    }

  }

  /** Represents one room in the ship. */
  private static final class Room {

    final String name;

    final Map<Direction, Room> directions = new HashMap<>();

    final Map<String, List<Direction>> navigation = new HashMap<>();

    Room(final String n, final Set<Direction> dirs) {
      name = n;
      for (final Direction d : dirs) {
        directions.put(d, null);
      }
    }

    @Override
    public String toString() {
      return name;
    }

  }

}
