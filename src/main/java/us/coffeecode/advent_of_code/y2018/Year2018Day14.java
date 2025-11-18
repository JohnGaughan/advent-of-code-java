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

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2018, day = 14)
@Component
public final class Year2018Day14 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public String calculatePart1(final PuzzleContext pc) {
    final int recipes = 10;
    final int input = il.fileAsInt(pc);
    final int[] array = generate(input << 2);
    final StringBuilder answer = new StringBuilder(recipes);
    Arrays.stream(array, input, input + recipes)
          .forEach(i -> answer.append(i));
    return answer.toString();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final int[] array = generate(20_225_750);
    final int[] match = il.fileAsIntsFromDigits(pc);

    for (int i = 0; i < array.length - match.length; ++i) {
      boolean matches = true;
      for (int j = 0; j < match.length; ++j) {
        if (array[i + j] != match[j]) {
          matches = false;
          break;
        }
      }
      if (matches) {
        return i;
      }
    }
    return 0;
  }

  private int[] generate(final int size) {
    final int[] array = new int[size];
    array[0] = 3;
    array[1] = 7;
    int used = 2;
    int first = 0;
    int second = 1;
    while (used < array.length) {
      final int newScore = array[first] + array[second];
      if (newScore > 9) {
        array[used] = newScore / 10;
        ++used;
        if (used < array.length) {
          array[used] = newScore % 10;
          ++used;
        }
      }
      else {
        array[used] = newScore;
        ++used;
      }
      first = (first + array[first] + 1) % used;
      second = (second + array[second] + 1) % used;
    }
    return array;
  }

}
