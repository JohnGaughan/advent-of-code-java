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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.ArrayInfinitelyIterable;
import us.coffeecode.advent_of_code.util.MyLongMath;

@AdventOfCodeSolution(year = 2023, day = 8, title = "Haunted Wasteland")
@Component
public class Year2023Day08 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc, s -> "AAA".equals(s), s -> !"ZZZ".equals(s));
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc, s -> s.endsWith("A"), s -> !s.endsWith("Z"));
  }

  /** Calculate the answer for all ghosts. */
  private long calculate(final PuzzleContext pc, final Predicate<String> isStart, final Predicate<String> isNotEnd) {
    final Input in = getInput(pc);
    final String[] locations = in.turns.keySet()
                                       .stream()
                                       .filter(isStart)
                                       .toArray(String[]::new);
    final long[] steps = Arrays.stream(locations)
                               .mapToLong(l -> calculate(l, in, isNotEnd))
                               .toArray();
    return MyLongMath.lcm(steps);
  }

  /** Calculate the answer for one ghost. */
  private long calculate(final String start, final Input in, final Predicate<String> isNotEnd) {
    String location = start;
    long steps = 0;
    final Iterator<Direction> iter = in.dirs.iterator();
    while (isNotEnd.test(location)) {
      final Direction next = iter.next();
      location = next.apply(in.turns.get(location));
      ++steps;
    }
    return steps;
  }

  private Input getInput(final PuzzleContext pc) {
    final List<List<String>> groups = il.groups(pc);
    final Iterable<Direction> dirs = Direction.parse(groups.getFirst()
                                                           .getFirst());
    final Map<String, Turns> turns = groups.get(1)
                                           .stream()
                                           .collect(Collectors.toMap(s -> s.substring(0, 3),
                                             s -> new Turns(s.substring(7, 10), s.substring(12, 15))));
    return new Input(dirs, turns);
  }

  private static enum Direction {

    L(t -> t.left),
    R(t -> t.right);

    static Iterable<Direction> parse(final String line) {
      final Direction[] array = line.codePoints()
                                    .mapToObj(cp -> cp == 'L' ? L : R)
                                    .toArray(Direction[]::new);
      return new ArrayInfinitelyIterable<>(array);
    }

    private final Function<Turns, String> f;

    private Direction(final Function<Turns, String> _f) {
      f = _f;
    }

    String apply(final Turns turns) {
      return f.apply(turns);
    }
  }

  private record Turns(String left, String right) {}

  private record Input(Iterable<Direction> dirs, Map<String, Turns> turns) {}
}
