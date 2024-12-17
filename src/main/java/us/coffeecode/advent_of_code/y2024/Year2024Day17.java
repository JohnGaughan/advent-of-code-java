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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2024, day = 17, title = "Chronospatial Computer")
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
    final Collection<Long> solutions = solvePart2(state.instructions, state.program);
    var solution = solutions.stream()
                            .mapToLong(Long::longValue)
                            .min();
    return solution.isPresent() ? solution.getAsLong() : Long.MIN_VALUE;
  }

  private Collection<Long> solvePart2(final int[] instructions, final String needle) {
    final Collection<Long> a_values = new HashSet<>();
    // Initial case: get all solutions for the final digit
    if (needle.length() == 1) {
      for (long a = 0; a < 8; ++a) {
        if (needle.equals(exec(instructions, new long[] { a, 0, 0 }))) {
          a_values.add(Long.valueOf(a));
        }
      }
    }
    // Recursive case: get candidate values recursively, then see what values work for each one.
    else {
      // Get all values that solve the next smaller step.
      final Collection<Long> a_candidates = solvePart2(instructions, needle.substring(2));
      // See what values solve the current step
      for (final Long a_candidate : a_candidates) {
        for (long i = 0; i < 8; ++i) {
          // Add the bit triplet "i" to the right of "a"
          final long a = (a_candidate.longValue() << 3L) | i;
          if (needle.equals(exec(instructions, new long[] { a, 0, 0 }))) {
            a_values.add(Long.valueOf(a));
          }
        }
      }
    }
    return a_values;
  }

  /** Execute the program instructions using the initial register values, returning whatever the program outputs. */
  private String exec(final int[] instructions, final long[] registers) {
    int ip = 0;
    final List<Long> output = new ArrayList<>();
    while (ip < instructions.length) {
      // ADV: A = A / 2^combo
      if (instructions[ip] == 0) {
        final long denominator = (1 << combo(instructions[ip + 1], registers));
        registers[0] = (registers[0] / denominator);
        ip += 2;
      }
      // BXL: B ^= operand
      else if (instructions[ip] == 1) {
        registers[1] ^= instructions[ip + 1];
        ip += 2;
      }
      // BST: B = combo(operand) % 8
      else if (instructions[ip] == 2) {
        registers[1] = combo(instructions[ip + 1], registers) & 7;
        ip += 2;
      }
      // JNZ: if A != 0, set IP to operand
      else if (instructions[ip] == 3) {
        if (registers[0] == 0) {
          ip += 2;
        }
        else {
          ip = instructions[ip + 1];
        }
      }
      // BXC: B ^= C
      else if (instructions[ip] == 4) {
        registers[1] ^= registers[2];
        ip += 2;
      }
      // OUT: add (combo % 8) to output
      else if (instructions[ip] == 5) {
        output.add(Long.valueOf(combo(instructions[ip + 1], registers) & 7));
        ip += 2;
      }
      // BDV: B = A / 2^combo
      else if (instructions[ip] == 6) {
        final long denominator = (1 << combo(instructions[ip + 1], registers));
        registers[1] = (registers[0] / denominator);
        ip += 2;
      }
      // CDV: C =A / 2^combo
      else if (instructions[ip] == 7) {
        final long denominator = (1 << combo(instructions[ip + 1], registers));
        registers[2] = (registers[0] / denominator);
        ip += 2;
      }
    }
    return output.stream()
                 .map(n -> n.toString())
                 .collect(Collectors.joining(","));
  }

  /**
   * Perform the combo transformation that gets either the input itself or the value of a register depending on the
   * input's value.
   */
  private long combo(final int input, final long[] registers) {
    if (input < 4) {
      return input;
    }
    return registers[input - 4];
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
