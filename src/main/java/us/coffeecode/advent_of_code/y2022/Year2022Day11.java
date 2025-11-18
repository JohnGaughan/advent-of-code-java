/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2022  John Gaughan
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
package us.coffeecode.advent_of_code.y2022;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.LongPredicate;
import java.util.function.LongUnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.MyLongMath;

@AdventOfCodeSolution(year = 2022, day = 11)
@Component
public class Year2022Day11 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(getInput(pc), 20, n -> n / 3);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final SortedMap<Integer, Monkey> monkeys = getInput(pc);
    final long lcm = monkeys.values()
                            .stream()
                            .mapToLong(Monkey::divisible)
                            .reduce(MyLongMath::lcm)
                            .getAsLong();
    return calculate(monkeys, 10_000, n -> n % lcm);
  }

  private long calculate(final SortedMap<Integer, Monkey> monkeys, final long rounds, final LongUnaryOperator reduction) {
    final Map<Integer, Long> inspections = new HashMap<>();
    monkeys.keySet()
           .forEach(k -> inspections.put(k, Long.valueOf(0)));
    for (int round = 0; round < rounds; ++round) {
      for (final Monkey m : monkeys.values()) {
        for (final Long item : m.items()) {
          final long worry = reduction.applyAsLong(m.f()
                                                    .applyAsLong(item.longValue()));
          monkeys.get(m.getTarget(worry))
                 .items()
                 .add(Long.valueOf(worry));
        }
        inspections.compute(m.id, (k, v) -> Long.valueOf(v.longValue() + m.items()
                                                                          .size()));
        m.items()
         .clear();
      }
    }
    final long[] values = inspections.values()
                                     .stream()
                                     .mapToLong(Long::intValue)
                                     .sorted()
                                     .toArray();
    return values[values.length - 2] * values[values.length - 1];
  }

  private SortedMap<Integer, Monkey> getInput(final PuzzleContext pc) {
    return new TreeMap<>(il.groupsAsObjects(pc, Monkey::valueOf)
                           .stream()
                           .collect(Collectors.toMap(m -> m.id(), m -> m)));
  }

  private record Monkey(Integer id, List<Long> items, LongUnaryOperator f, LongPredicate test, Integer targetTrue,
    Integer targetFalse, long divisible) {

    Integer getTarget(final long value) {
      return test.test(value) ? targetTrue : targetFalse;
    }

    private static final Pattern SPLIT_ITEMS = Pattern.compile(", ");

    private static final Pattern SPLIT_SPACE = Pattern.compile(" ");

    private static final String OLD = "old";

    static Monkey valueOf(final List<String> lines) {
      final String first = lines.getFirst();
      final int colon = first.indexOf(':');
      final Integer id = Integer.valueOf(first.substring(7, colon));

      final String second = lines.get(1);
      final List<Long> items = new ArrayList<>(Arrays.stream(SPLIT_ITEMS.split(second.substring(second.indexOf(':') + 2)))
                                                     .map(Long::valueOf)
                                                     .toList());

      final String[] third = SPLIT_SPACE.split(lines.get(2));
      final LongUnaryOperator f;
      if ("+".equals(third[6])) {
        if (OLD.equals(third[7])) {
          f = i -> i << 1;
        }
        else {
          final long c = Long.parseLong(third[7]);
          f = i -> i + c;
        }
      }
      else {
        if (OLD.equals(third[7])) {
          f = i -> i * i;
        }
        else {
          final long c = Long.parseLong(third[7]);
          f = i -> i * c;
        }
      }

      final String[] fourth = SPLIT_SPACE.split(lines.get(3));
      final long divisible = Long.parseLong(fourth[fourth.length - 1]);

      final String[] fifth = SPLIT_SPACE.split(lines.get(4));
      final Integer targetTrue = Integer.valueOf(fifth[fifth.length - 1]);

      final String[] sixth = SPLIT_SPACE.split(lines.get(5));
      final Integer targetFalse = Integer.valueOf(sixth[sixth.length - 1]);

      return new Monkey(id, items, f, i -> i % divisible == 0, targetTrue, targetFalse, divisible);
    }
  }

}
