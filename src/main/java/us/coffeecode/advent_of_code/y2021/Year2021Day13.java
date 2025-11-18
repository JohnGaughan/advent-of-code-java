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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.DigitConverter;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2021, day = 13)
@Component
public final class Year2021Day13 {

  @Autowired
  private InputLoader il;

  @Autowired
  private DigitConverter dc;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Input input = getInput(pc);
    final Collection<Point2D> points = fold(input.points, input.instructions.getFirst());
    return points.size();
  }

  @Solver(part = 2)
  public String calculatePart2(final PuzzleContext pc) {
    final Input input = getInput(pc);
    Collection<Point2D> points = input.points;
    for (final Instruction instruction : input.instructions) {
      points = fold(points, instruction);
    }
    boolean[][] array = toArray(points);
    return toString(array);
  }

  private boolean[][] toArray(final Iterable<Point2D> points) {
    int maxX = Integer.MIN_VALUE;
    int maxY = Integer.MIN_VALUE;
    for (final Point2D point : points) {
      maxX = Math.max(maxX, point.getX());
      maxY = Math.max(maxY, point.getY());
    }
    final boolean[][] array = new boolean[maxY + 1][maxX + 1];
    for (final Point2D point : points) {
      array[point.getY()][point.getX()] = true;
    }
    return array;
  }

  private String toString(final boolean[][] array) {
    final StringBuilder answer = new StringBuilder(8);
    for (int x1 = 0; x1 < array[0].length; x1 += 5) {
      answer.appendCodePoint(dc.toCodePoint(array, x1, 4));
    }
    return answer.toString();
  }

  private Set<Point2D> fold(final Collection<Point2D> points, final Instruction instruction) {
    final Set<Point2D> newPoints = new HashSet<>(points.size());
    for (final Point2D original : points) {
      if (instruction.isX) {
        if (original.getX() < instruction.value) {
          newPoints.add(original);
        }
        else {
          final int x = (instruction.value << 1) - original.getX();
          newPoints.add(new Point2D(x, original.getY()));
        }
      }
      else {
        if (original.getY() < instruction.value) {
          newPoints.add(original);
        }
        else {
          final int y = (instruction.value << 1) - original.getY();
          newPoints.add(new Point2D(original.getX(), y));
        }
      }
    }
    return newPoints;
  }

  private Input getInput(final PuzzleContext pc) {
    final List<List<String>> groups = il.groups(pc);

    final Set<Point2D> points = groups.getFirst()
                                      .stream()
                                      .map(s -> Arrays.stream(POINT_SPLIT.split(s))
                                                      .mapToInt(Integer::parseInt)
                                                      .toArray())
                                      .map(Point2D::new)
                                      .collect(Collectors.toSet());

    final List<Instruction> instructions = new ArrayList<>(groups.get(1)
                                                                 .size());
    for (final String line : groups.get(1)) {
      final boolean isX = line.codePointAt(11) == 'x';
      final int value = Integer.parseInt(line.substring(13));
      instructions.add(new Instruction(isX, value));
    }

    return new Input(points, instructions);
  }

  private static final Pattern POINT_SPLIT = Pattern.compile(",");

  private record Instruction(boolean isX, int value) {}

  private record Input(Set<Point2D> points, List<Instruction> instructions) {}
}
