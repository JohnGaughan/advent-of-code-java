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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2022, day = 3)
@Component
public class Year2022Day03 {

  @Autowired
  private InputLoader il;

  private static final Map<Integer, Long> PRIORITY = new HashMap<>(128);

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return il.lines(pc)
             .stream()
             .map(this::intersection)
             .mapToLong(s -> s.stream()
                              .mapToLong(this::priority)
                              .sum())
             .sum();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return groups(il.lines(pc)).stream()
                               .map(Group::intersection)
                               .mapToLong(this::priority)
                               .sum();
  }

  private List<Group> groups(final List<String> lines) {
    final List<Group> groups = new ArrayList<>(lines.size() / 3);
    for (int i = 0; i < lines.size(); i += 3) {
      groups.add(new Group(toSet(lines.get(i)), toSet(lines.get(i + 1)), toSet(lines.get(i + 2))));
    }
    return groups;
  }

  private Set<Integer> intersection(final String s) {
    return Sets.intersection(toSet(s.substring(0, s.length() >> 1)), toSet(s.substring(s.length() >> 1, s.length())));
  }

  private long priority(final Integer i) {
    return PRIORITY.get(i)
                   .longValue();
  }

  private Set<Integer> toSet(final String s) {
    return s.chars()
            .mapToObj(Integer::valueOf)
            .collect(Collectors.toSet());
  }

  private record Group(Set<Integer> elf1, Set<Integer> elf2, Set<Integer> elf3) {

    Integer intersection() {
      return Sets.intersection(elf1, Sets.intersection(elf2, elf3))
                 .iterator()
                 .next();
    }
  }

  static {
    long priority = 0;
    for (int i = 'a'; i <= 'z'; ++i) {
      PRIORITY.put(Integer.valueOf(i), Long.valueOf(++priority));
    }
    for (int i = 'A'; i <= 'Z'; ++i) {
      PRIORITY.put(Integer.valueOf(i), Long.valueOf(++priority));
    }
  }

}
