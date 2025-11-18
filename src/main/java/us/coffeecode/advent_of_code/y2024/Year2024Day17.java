/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2024  John Gaughan
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
package us.coffeecode.advent_of_code.y2024;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2024, day = 17)
@Component
public class Year2024Day17 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public String calculatePart1(final PuzzleContext pc) {
    final State state = getInput(pc);
    return exec(state.instructions, state.registers);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final State state = getInput(pc);
    final long[] solutions = solvePart2(state.instructions, state.program);
    final OptionalLong solution = Arrays.stream(solutions)
                                        .min();
    return solution.isPresent() ? solution.getAsLong() : Long.MIN_VALUE;
  }

  private long[] solvePart2(final int[] instructions, final String needle) {
    final LongStream.Builder a_values = LongStream.builder();
    // Initial case: get all solutions for the final digit
    if (needle.length() == 1) {
      for (long a = 0; a < 8; ++a) {
        if (needle.equals(exec(instructions, new long[] { a, 0, 0 }))) {
          a_values.accept(a);
        }
      }
    }
    // Recursive case: get candidate values recursively, then see what values work for each one.
    else {
      // Get all values that solve the next smaller step.
      final long[] a_candidates = solvePart2(instructions, needle.substring(2));
      // See what values solve the current step
      for (final long a_candidate : a_candidates) {
        final long a_shifted = (a_candidate << 3);
        for (long i = 0; i < 8; ++i) {
          // Add the bit triplet "i" to the right of "a"
          final long a = (a_shifted | i);
          if (needle.equals(exec(instructions, new long[] { a, 0, 0 }))) {
            a_values.accept(a);
          }
        }
      }
    }
    return a_values.build()
                   .distinct()
                   .toArray();
  }

  /** Execute the program instructions using the initial register values, returning whatever the program outputs. */
  private String exec(final int[] ins, final long[] reg) {
    int ip = 0;
    final LongStream.Builder builder = LongStream.builder();
    while (ip < ins.length) {
      // ADV: A = A / 2^combo
      if (ins[ip] == 0) {
        reg[0] = (reg[0] >> combo(ins[ip + 1], reg));
        ip += 2;
      }
      // BXL: B ^= operand
      else if (ins[ip] == 1) {
        reg[1] ^= ins[ip + 1];
        ip += 2;
      }
      // BST: B = combo(operand) % 8
      else if (ins[ip] == 2) {
        reg[1] = combo(ins[ip + 1], reg) & 7;
        ip += 2;
      }
      // JNZ: if A != 0, set IP to operand
      else if (ins[ip] == 3) {
        ip = ((reg[0] == 0) ? (ip + 2) : ins[ip + 1]);
      }
      // BXC: B ^= C
      else if (ins[ip] == 4) {
        reg[1] ^= reg[2];
        ip += 2;
      }
      // OUT: add (combo % 8) to output
      else if (ins[ip] == 5) {
        builder.accept(combo(ins[ip + 1], reg) & 7);
        ip += 2;
      }
      // BDV: B = A / 2^combo
      else if (ins[ip] == 6) {
        reg[1] = (reg[0] >> combo(ins[ip + 1], reg));
        ip += 2;
      }
      // CDV: C = A / 2^combo
      else if (ins[ip] == 7) {
        reg[2] = (reg[0] >> combo(ins[ip + 1], reg));
        ip += 2;
      }
    }
    return builder.build()
                  .mapToObj(Long::toString)
                  .collect(Collectors.joining(","));
  }

  /**
   * Perform the combo transformation that gets either the input itself or the value of a register depending on the
   * input's value.
   */
  private long combo(final int input, final long[] registers) {
    return (input < 4) ? input : registers[input - 4];
  }

  private State getInput(final PuzzleContext pc) {
    final long[] registers = new long[3];
    final List<String> lines = il.lines(pc);
    for (int i = 0; i < registers.length; ++i) {
      final Matcher m = DIGITS.matcher(lines.get(i));
      m.find();
      registers[i] = Long.parseLong(m.group());
    }
    final String s4 = lines.get(4);
    final String program = s4.substring(s4.indexOf(' ') + 1);
    final int[] instructions = Arrays.stream(PROGRAM_SPLIT.split(program))
                                     .mapToInt(Integer::parseInt)
                                     .toArray();
    return new State(instructions, registers, program);
  }

  private record State(int[] instructions, long[] registers, String program) {}

  private static final Pattern DIGITS = Pattern.compile("\\d+");

  private static final Pattern PROGRAM_SPLIT = Pattern.compile(",");
}
