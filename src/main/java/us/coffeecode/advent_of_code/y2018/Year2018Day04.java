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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2018, day = 4, title = "Repose Record")
@Component
public final class Year2018Day04 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Guard mostAsleep = getInput(pc).stream()
                                         .max((a, b) -> Integer.compare(a.asleep, b.asleep))
                                         .get();
    return (long) mostAsleep.id * (long) mostAsleep.mostAsleepMinute;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    Guard selected = null;
    for (final Guard guard : getInput(pc)) {
      if (selected == null) {
        selected = guard;
      }
      else if (guard.timesAsleep > selected.timesAsleep) {
        selected = guard;
      }
    }
    return (selected == null) ? 0L : ((long) selected.id * (long) selected.mostAsleepMinute);
  }

  /** Get the input data for this solution. */
  private Collection<Guard> getInput(final PuzzleContext pc) {
    final Event[] events = il.linesAsObjects(pc, Event::make)
                             .stream()
                             .sorted()
                             .toArray(Event[]::new);

    // Aggregate events into guards.
    final Map<Integer, Guard> guards = new HashMap<>();
    int start = 0;
    int end = 1;
    while (true) {
      while (start < events.length && !events[start].message.startsWith("G")) {
        ++start;
      }
      if (start >= events.length) {
        break;
      }
      end = start + 1;
      while (end < events.length && !events[end].message.startsWith("G")) {
        ++end;
      }

      final Log log = new Log(events, start, end);
      final Integer key = Integer.valueOf(log.guard);
      if (!guards.containsKey(key)) {
        guards.put(key, new Guard(log.guard));
      }

      guards.get(key)
            .add(log);

      start = end;
      end = start + 1;
    }
    return new ArrayList<>(guards.values());
  }

  private static final class Guard {

    final int id;

    int asleep;

    private final int[] frequency = new int[60];

    int mostAsleepMinute;

    int timesAsleep;

    Guard(final int _id) {
      id = _id;
    }

    void add(final Log log) {
      for (final int[] interval : log.intervals) {
        for (int i = interval[0]; i < interval[1]; ++i) {
          ++frequency[i];
          if (frequency[i] > frequency[mostAsleepMinute]) {
            mostAsleepMinute = i;
          }
        }
        asleep += interval[1] - interval[0];
      }
      timesAsleep = frequency[mostAsleepMinute];
    }

  }

  private static final class Log {

    final int guard;

    final int[][] intervals;

    Log(final Event[] events, final int start, final int end) {
      int guardNumberStart = events[start].message.indexOf(' ') + 2;
      int guardNumberEnd = events[start].message.indexOf(' ', guardNumberStart);
      guard = Integer.parseInt(events[start].message.substring(guardNumberStart, guardNumberEnd));
      intervals = new int[(end - start) / 2][2];
      int j = 0;
      for (int i = start + 1; i < end; i += 2, ++j) {
        intervals[j][0] = events[i].minute;
        intervals[j][1] = events[i + 1].minute;
      }
    }

  }

  private static record Event(int month, int day, int hour, int minute, String message)
  implements Comparable<Event> {

    static Event make(final String input) {
      return new Event(Integer.parseInt(input.substring(6, 8)), Integer.parseInt(input.substring(9, 11)),
        Integer.parseInt(input.substring(12, 14)), Integer.parseInt(input.substring(15, 17)), input.substring(19));
    }

    @Override
    public int compareTo(final Event o) {
      int result = Integer.compare(month, o.month);
      if (result != 0) {
        return result;
      }
      result = Integer.compare(day, o.day);
      if (result != 0) {
        return result;
      }
      result = Integer.compare(hour, o.hour);
      if (result != 0) {
        return result;
      }
      return Integer.compare(minute, o.minute);
    }

  }

}
