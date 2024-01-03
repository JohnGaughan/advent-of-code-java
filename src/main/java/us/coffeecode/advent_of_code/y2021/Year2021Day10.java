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
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2021, day = 10, title = "Syntax Scoring")
@Component
public final class Year2021Day10 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    long answer = 0;
    outer: for (final int[] line : il.linesAsCodePoints(pc)) {
      final Deque<Integer> stack = new LinkedList<>();
      for (final int codePoint : line) {
        if (isOpen(codePoint)) {
          stack.addFirst(Integer.valueOf(codePoint));
        }
        else if (isClose(codePoint)) {
          final int open = stack.removeFirst().intValue();
          if (!matches(open, codePoint)) {
            if (codePoint == ')') {
              answer += 3;
            }
            else if (codePoint == ']') {
              answer += 57;
            }
            else if (codePoint == '}') {
              answer += 1_197;
            }
            else if (codePoint == '>') {
              answer += 25_137;
            }
            continue outer;
          }
        }
      }
    }
    return answer;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final List<Long> scores = new ArrayList<>(45);
    outer: for (final int[] line : il.linesAsCodePoints(pc)) {
      final Deque<Integer> stack = new LinkedList<>();
      for (final int codePoint : line) {
        if (isOpen(codePoint)) {
          stack.addFirst(Integer.valueOf(codePoint));
        }
        else if (isClose(codePoint)) {
          final int open = stack.removeFirst().intValue();
          if (!matches(open, codePoint)) {
            // Corrupt: skip it.
            continue outer;
          }
        }
      }
      // Incomplete.
      long score = 0;
      while (!stack.isEmpty()) {
        final int open = stack.removeFirst().intValue();
        if (open == '(') {
          score *= 5;
          ++score;
        }
        else if (open == '[') {
          score *= 5;
          score += 2;
        }
        else if (open == '{') {
          score *= 5;
          score += 3;
        }
        else if (open == '<') {
          score *= 5;
          score += 4;
        }
      }
      scores.add(Long.valueOf(score));
    }
    scores.sort(null);
    return scores.get(scores.size() >> 1).longValue();
  }

  private boolean matches(final int open, final int close) {
    return (open == '(') && (close == ')') || (open == '<') && (close == '>') || (open == '{') && (close == '}')
      || (open == '[') && (close == ']');
  }

  private boolean isOpen(final int codePoint) {
    return (codePoint == '(') || (codePoint == '<') || (codePoint == '{') || (codePoint == '[');
  }

  private boolean isClose(final int codePoint) {
    return (codePoint == ')') || (codePoint == '>') || (codePoint == '}') || (codePoint == ']');
  }

}
