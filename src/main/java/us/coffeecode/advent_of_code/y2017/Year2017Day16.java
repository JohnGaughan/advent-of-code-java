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
package us.coffeecode.advent_of_code.y2017;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2017, day = 16, title = "Permutation Promenade")
@Component
public final class Year2017Day16 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public String calculatePart1(final PuzzleContext pc) {
    return doIterations(pc, 1);
  }

  @Solver(part = 2)
  public String calculatePart2(final PuzzleContext pc) {
    return doIterations(pc, 1_000_000_000);
  }

  private String doIterations(final PuzzleContext pc, final int iterations) {
    final int size = pc.getInt("programs");
    final int[] programs = IntStream.range(0, size)
                                    .map(i -> i + 'a')
                                    .toArray();
    final Iterable<DanceMove> input = il.fileAsObjectsFromSplit(pc, SEPARATOR, DanceMove::make);
    final List<String> cache = new ArrayList<>(48);
    for (int i = 0; i < iterations; ++i) {
      final String answer = new String(programs, 0, programs.length);
      if (cache.contains(answer)) {
        // Found a cycle. Short-circuit.
        return cache.get(iterations % i);
      }
      cache.add(answer);
      for (final DanceMove move : input) {
        move.op.apply(programs, move.arg1, move.arg2);
      }
    }
    return new String(programs, 0, programs.length);
  }

  private static enum Operation {

    p {

      @Override
      void apply(final int[] programs, final int arg1, final int arg2) {
        final int position1 = find(programs, arg1);
        final int position2 = find(programs, arg2);
        Operation.x.apply(programs, position1, position2);
      }
    },
    s {

      @Override
      void apply(final int[] programs, final int arg1, final int arg2) {
        for (int rotation = 0; rotation < arg1; ++rotation) {
          final int temp = programs[programs.length - 1];
          for (int i = programs.length - 1; i > 0; --i) {
            programs[i] = programs[i - 1];
          }
          programs[0] = temp;
        }
      }
    },
    x {

      @Override
      void apply(final int[] programs, final int arg1, final int arg2) {
        final int temp = programs[arg1];
        programs[arg1] = programs[arg2];
        programs[arg2] = temp;
      }
    };

    static Operation valueOf(final int codePoint) {
      return valueOf(Character.toString(codePoint));
    }

    abstract void apply(final int[] programs, final int arg1, final int arg2);

    protected int find(final int[] programs, final int program) {
      for (int i = 0; i < programs.length; ++i) {
        if (programs[i] == program) {
          return i;
        }
      }
      throw new IllegalArgumentException("Illegal character [" + program + "] not found in array " + Arrays.toString(programs));
    }

  }

  private static final Pattern SEPARATOR = Pattern.compile(",");

  private static record DanceMove(Operation op, int arg1, int arg2) {

    static DanceMove make(final String input) {
      final Operation op = Operation.valueOf(input.codePointAt(0));
      final int slash = input.indexOf('/');
      if (op == Operation.s) {
        return new DanceMove(op, Integer.parseInt(input.substring(1)), -1);
      }
      else if (op == Operation.x) {
        return new DanceMove(op, Integer.parseInt(input.substring(1, slash)), Integer.parseInt(input.substring(slash + 1)));
      }
      else {
        return new DanceMove(op, input.codePointAt(slash - 1), input.codePointAt(slash + 1));
      }
    }

  }

}
