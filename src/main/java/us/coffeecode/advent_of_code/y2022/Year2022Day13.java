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
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2022, day = 13)
@Component
public class Year2022Day13 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final List<JSONArray[]> input =
      il.groupsAsObjects(pc, l -> new JSONArray[] { new JSONArray(l.getFirst()), new JSONArray(l.get(1)) });
    long answer = 0;
    for (int i = 0; i < input.size(); ++i) {
      final JSONArray[] a = input.get(i);
      if (compare(a[0], a[1]) < 0) {
        answer += (i + 1);
      }
    }
    return answer;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final List<JSONArray> input = new ArrayList<>(il.lines(pc)
                                                    .stream()
                                                    .filter(s -> !s.isEmpty())
                                                    .map(s -> new JSONArray(s))
                                                    .toList());
    input.add(DIVIDER_1);
    input.add(DIVIDER_2);
    Collections.sort(input, this::compare);
    long idx1 = Integer.MIN_VALUE;
    long idx2 = Integer.MIN_VALUE;
    for (int i = 0; (i < input.size()) && ((idx1 < 0) || (idx2 < 0)); ++i) {
      if (DIVIDER_1 == input.get(i)) {
        idx1 = i + 1;
      }
      else if (DIVIDER_2 == input.get(i)) {
        idx2 = i + 1;
      }
    }
    return idx1 * idx2;
  }

  private int compare(final Object left, final Object right) {
    if ((left instanceof Integer l) && (right instanceof Integer r)) {
      return l.compareTo(r);
    }
    else if ((left instanceof JSONArray l) && (right instanceof JSONArray r)) {
      for (int i = 0; i < Math.max(l.length(), r.length()); ++i) {
        if ((i == l.length()) && (i <= r.length())) {
          return -1;
        }
        else if ((i == r.length()) && (i < l.length())) {
          return 1;
        }
        final int innerCompare = compare(l.get(i), r.get(i));
        if (innerCompare != 0) {
          return innerCompare;
        }
      }
      return 0;
    }
    else if ((left instanceof JSONArray l) && (right instanceof Integer r)) {
      return compare(l, new JSONArray(new Integer[] { r }));
    }
    else if ((left instanceof Integer l) && (right instanceof JSONArray r)) {
      return compare(new JSONArray(new Integer[] { l }), r);
    }
    final String leftClass = (left == null ? null : left.getClass()
                                                        .getName());
    final String rightClass = (right == null ? null : right.getClass()
                                                           .getName());
    throw new IllegalArgumentException("Unhandled classes [" + leftClass + "] and [" + rightClass + "]");
  }

  private static final JSONArray DIVIDER_1 = new JSONArray("[[2]]");

  private static final JSONArray DIVIDER_2 = new JSONArray("[[6]]");
}
