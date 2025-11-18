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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;
import us.coffeecode.advent_of_code.util.Range2D;

@AdventOfCodeSolution(year = 2023, day = 3)
@Component
public class Year2023Day03 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final List<String> board = il.lines(pc);
    final Collection<Point2D> symbols = getSymbols(board, ch -> ((ch != EMPTY) && !Character.isDigit(ch)));

    long answer = 0;
    for (final Range2D number : getNumbers(board)) {
      if (getAdjacents(number).stream()
                              .anyMatch(p -> symbols.contains(p))) {
        answer += asLong(board, number);
      }
    }
    return answer;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final List<String> board = il.lines(pc);
    final Collection<Range2D> numbers = getNumbers(board);

    long answer = 0;
    for (final Point2D gear : getSymbols(board, ch -> (ch == GEAR))) {
      final Collection<Range2D> adjacents = getAdjacents(gear, numbers);
      if (adjacents.size() == 2) {
        answer += adjacents.stream()
                           .mapToLong(r -> asLong(board, r))
                           .reduce(1, (a, b) -> a * b);
      }
    }
    return answer;
  }

  /** Given a range where a number exists on the board, parse those locations as a number. */
  private long asLong(final List<String> board, final Range2D number) {
    return Long.parseLong(board.get(number.getY1())
                               .substring(number.getX1(), number.getX2() + 1));
  }

  /** Get the coordinates of all symbols on the board. */
  private Set<Point2D> getSymbols(final List<String> board, final IntPredicate predicate) {
    final Set<Point2D> symbols = new HashSet<>();
    for (int y = 0; y < board.size(); ++y) {
      final String line = board.get(y);
      for (int x = 0; x < line.length(); ++x) {
        final int codePoint = line.codePointAt(x);
        if (predicate.test(codePoint)) {
          symbols.add(new Point2D(x, y));
        }
      }
    }
    return symbols;
  }

  /** Get the endpoints of each number on the board. */
  private Set<Range2D> getNumbers(final List<String> board) {
    final Set<Range2D> numbers = new HashSet<>();
    for (int y = 0; y < board.size(); ++y) {
      final int[] line = board.get(y)
                              .codePoints()
                              .toArray();
      int x = -1;
      int start = Integer.MIN_VALUE;
      boolean inNumber = false;
      while (true) {
        ++x;
        if (inNumber) {
          // Reached end of line, and we are in a number so add it before breaking.
          if (x == line.length) {
            numbers.add(new Range2D(start, y, x - 1, y));
            break;
          }
          // Found a non-digit character, so add the number and flip the search switch.
          else if (!Character.isDigit(line[x])) {
            numbers.add(new Range2D(start, y, x - 1, y));
            inNumber = false;
            start = Integer.MIN_VALUE;
          }
        }
        else {
          // Reached end of line, but not in a number so nothing to do.
          if (x == line.length) {
            break;
          }
          // Not in a number, but we found a digit so mark the start.
          else if (Character.isDigit(line[x])) {
            start = x;
            inNumber = true;
          }
        }
      }
    }
    return numbers;
  }

  /** Get all points adjacent to this range per the requirements in part one. */
  private Set<Point2D> getAdjacents(final Range2D number) {
    final Set<Point2D> adjacents = new HashSet<>();
    for (int x = number.getX1() - 1; x < number.getX2() + 2; ++x) {
      adjacents.add(new Point2D(x, number.getY1() - 1));
      adjacents.add(new Point2D(x, number.getY1() + 1));
    }
    adjacents.add(new Point2D(number.getX1() - 1, number.getY1()));
    adjacents.add(new Point2D(number.getX2() + 1, number.getY1()));
    return adjacents;
  }

  private Set<Range2D> getAdjacents(final Point2D gear, final Collection<Range2D> numbers) {
    final Collection<Point2D> neighbors = gear.getAllNeighbors();
    return numbers.stream()
                  .filter(r -> r.containsAnyInclusive(neighbors))
                  .collect(Collectors.toSet());
  }

  /** If a board location matches this code point, it is considered empty. */
  private static final int EMPTY = '.';

  /** If a board location matches this code point, it is a gear. */
  private static final int GEAR = '*';
}
