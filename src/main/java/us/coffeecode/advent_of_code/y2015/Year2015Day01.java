/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2020  John Gaughan
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
package us.coffeecode.advent_of_code.y2015;

import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2015/day/1">Year 2015, day 1</a>. This problem involves counting two different
 * characters in the input and comparing them, as if Santa starts on floor one and we want to know what floors he visits
 * based on going up or down. Part one asks for his final floor, while part two asks when he first enters the basement.
 * </p>
 * <p>
 * My approach to this puzzle was simple. For part one, count the number of ups and downs, and take the difference.
 * Since addition is commutative, order does not matter. For part two, order <i>does</i> matter because the simulation
 * ends when Santa first enters the basement floors. For this part, iterate over the input and track the current floor
 * along with how many steps he took.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2015Day01 {

  public long calculatePart1() {
    final List<Direction> input = getInput();
    final long ups = input.stream().filter(d -> d == Direction.UP).count();
    final long downs = input.stream().filter(d -> d == Direction.DOWN).count();
    return ups - downs;
  }

  public int calculatePart2() {
    int floor = 0;
    int step = 1;
    for (final Direction d : getInput()) {
      if (d == Direction.UP) {
        ++floor;
      }
      else {
        --floor;
      }
      if (floor < 0) {
        return step;
      }
      ++step;
    }
    return Integer.MIN_VALUE;
  }

  /** Get the input data for this solution. */
  private List<Direction> getInput() {
    try {
      return Files.readString(Utils.getInput(2015, 1)).trim().chars().boxed().map(Direction::valueOf).collect(
        Collectors.toList());
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /** A direction that Santa can take. */
  private static enum Direction {

    UP('('),
    DOWN(')');

    public static Direction valueOf(final int c) {
      for (final Direction d : values()) {
        if (d.ch == c) {
          return d;
        }
      }
      throw new IllegalArgumentException("Invalid character [" + c + "]");
    }

    private final int ch;

    private Direction(final int c) {
      ch = c;
    }
  }

}
